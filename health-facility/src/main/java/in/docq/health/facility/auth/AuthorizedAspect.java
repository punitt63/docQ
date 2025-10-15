package in.docq.health.facility.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.docq.health.facility.service.HealthProfessionalService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Component
@Aspect
@Order(1)
public class AuthorizedAspect {
    private final HealthProfessionalService healthProfessionalService;
    private final BackendKeyCloakRestClient backendKeyCloakRestClient;

    @Autowired
    public AuthorizedAspect(HealthProfessionalService healthProfessionalService, BackendKeyCloakRestClient backendKeyCloakRestClient) {
        this.healthProfessionalService = healthProfessionalService;
        this.backendKeyCloakRestClient = backendKeyCloakRestClient;
    }

    @Order(1)
    @Around("@annotation(in.docq.health.facility.auth.Authorized)")
    public Object proceed(ProceedingJoinPoint call) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String authenticatedUser = (String) request.getAttribute("authenticatedUser");
        String authenticatedToken = (String) request.getAttribute("authenticatedToken");
        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String healthFacilityID = pathVariables.get("health-facility-id");

        MethodSignature signature = (MethodSignature) call.getSignature();
        Method method = signature.getMethod();
        Authorized annotation = method.getAnnotation(Authorized.class);
        String resource = annotation.resource();
        String scope = annotation.scope();

        return healthProfessionalBelongsToHealthFacility(authenticatedUser, healthFacilityID)
                .thenCompose(healthProfessionalBelongsToHealthFacility -> authorizeUserPermissions(authenticatedToken, resource, scope)
                        .thenApply(userHasPermissions -> healthProfessionalBelongsToHealthFacility && userHasPermissions))
                .thenApply(verdict -> {
                    if(!verdict) {
                        return ResponseEntity.status(401).build();
                    }
                    try {
                        return call.proceed();
                    } catch (Throwable e) {
                        throw new CompletionException(e);
                    }
                })
                .exceptionally(throwable -> {
                    throw new CompletionException(throwable.getCause());
                });
    }

    private CompletionStage<Boolean> healthProfessionalBelongsToHealthFacility(String userName, String healthFacilityID) {
        if(userName.equals("docq-admin")) {
            return completedFuture(true);
        }
        return healthProfessionalService.get(healthFacilityID, userName.split("_")[1])
                .handle((healthProfessional, throwable) -> throwable == null);
    }

    private CompletionStage<Boolean> authorizeUserPermissions(String authenticatedToken, String expectedResource, String expectedScope) {
        // Try RPT permission first
        Boolean rptAllowed = hasRptPermission(authenticatedToken, expectedResource, expectedScope);
        if (rptAllowed != null) {
            return completedFuture(rptAllowed);
        }

        // Fallback to UMA permissions for the token (aud=health-facility-backend-app)
        return hasUmaPermission(authenticatedToken, expectedResource, expectedScope);
    }

    private Boolean hasRptPermission(String token, String resource, String scope) {
        try {
            String raw = token.startsWith("Bearer ") ? token.substring(7) : token;
            String[] parts = raw.split("\\.");
            if (parts.length != 3) return null; // not a JWT or malformed

            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            JsonNode json = new ObjectMapper().readTree(payload);
            JsonNode auth = json.get("authorization");
            if (auth == null || !auth.has("permissions")) return null; // no RPT permissions present

            JsonNode permissions = auth.get("permissions");
            for (JsonNode p : permissions) {
                String rsname = p.has("rsname") ? p.get("rsname").asText() : null;
                if (!resource.equals(rsname)) continue;
                JsonNode scopesNode = p.get("scopes");
                if (scopesNode == null) continue;
                for (JsonNode s : scopesNode) {
                    if (scope.equals(s.asText())) return true;
                }
            }
            return false; // had RPT, but no matching permission
        } catch (Exception e) {
            return null; // parsing failed; defer to UMA
        }
    }

    private CompletionStage<Boolean> hasUmaPermission(String token, String resource, String scope) {
        return backendKeyCloakRestClient.getUserPermissions(token)
                .thenApply(permissions -> permissions.stream()
                        .anyMatch(p -> resource.equals(p.getRsname()) && p.getScopes() != null && p.getScopes().contains(scope)));
    }

}

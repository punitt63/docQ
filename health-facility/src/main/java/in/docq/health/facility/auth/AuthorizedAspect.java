package in.docq.health.facility.auth;

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
        if(userName.equals("docq-admin") || userName.equals("patient-backend-app")) {
            return completedFuture(true);
        }
        return healthProfessionalService.get(healthFacilityID, userName.split("_")[1])
                .handle((healthProfessional, throwable) -> throwable == null);
    }

    private CompletionStage<Boolean> authorizeUserPermissions(String authenticatedToken, String expectedResource, String expectedScope) {
        return backendKeyCloakRestClient.getUserPermissions(authenticatedToken)
                .thenApply(permissions -> permissions.stream().anyMatch(permission -> permission.getRsname().equals(expectedResource) && permission.getScopes().contains(expectedScope)));
    }

}

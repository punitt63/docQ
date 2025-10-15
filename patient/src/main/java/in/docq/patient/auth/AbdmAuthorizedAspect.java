package in.docq.patient.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@Aspect
@Order(2)
public class AbdmAuthorizedAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(AbdmAuthorizedAspect.class);
    
    private final AbdmTokenValidator abdmTokenValidator;
    
    public AbdmAuthorizedAspect(AbdmTokenValidator abdmTokenValidator) {
        this.abdmTokenValidator = abdmTokenValidator;
    }
    
    @Around("@annotation(in.docq.patient.auth.AbdmAuthorized)")
    public Object proceed(ProceedingJoinPoint call) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            
            // Get authenticated patient info (set by AbdmAuthenticationFilter)
            String authenticatedPatientId = (String) request.getAttribute("authenticatedPatientId");
            AbdmTokenInfo tokenInfo = (AbdmTokenInfo) request.getAttribute("abdmTokenInfo");
            
            if (authenticatedPatientId == null || tokenInfo == null) {
                logger.error("Patient authentication information not found in request");
                return CompletableFuture.completedFuture(ResponseEntity.status(401)
                    .body("{\"error\":\"Authentication required\"}"));
            }
            
            // Get method annotation
            MethodSignature signature = (MethodSignature) call.getSignature();
            Method method = signature.getMethod();
            AbdmAuthorized annotation = method.getAnnotation(AbdmAuthorized.class);
            
            // Validate patient ID if required
            if (annotation.validatePatientId()) {
                if (!validatePatientIdFromRequest(request, authenticatedPatientId, annotation.patientIdParam())) {
                    logger.warn("Patient ID validation failed for patient: {}", authenticatedPatientId);
                    return CompletableFuture.completedFuture(ResponseEntity.status(403)
                        .body("{\"error\":\"Access denied: You can only access your own data\"}"));
                }
            }
            
            // Log access for audit purposes
            logAccess(authenticatedPatientId, annotation.resource(), request.getRequestURI());
            
            // Proceed with the method call
            return call.proceed();
            
        } catch (Throwable e) {
            logger.error("Error in ABDM authorization aspect", e);
            throw new CompletionException(e);
        }
    }
    
    private boolean validatePatientIdFromRequest(HttpServletRequest request, String authenticatedPatientId, String patientIdParam) {
        // Get path variables
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        
        if (pathVariables != null && pathVariables.containsKey(patientIdParam)) {
            String requestedPatientId = pathVariables.get(patientIdParam);
            return abdmTokenValidator.validatePatientIdMatch(authenticatedPatientId, requestedPatientId);
        }
        
        // If no patient ID in path, check query parameters
        String queryPatientId = request.getParameter(patientIdParam);
        if (queryPatientId != null) {
            return abdmTokenValidator.validatePatientIdMatch(authenticatedPatientId, queryPatientId);
        }
        
        // If patient ID validation is required but no patient ID found, allow if it's a "me" endpoint
        String uri = request.getRequestURI();
        if (uri.contains("/me/") || uri.endsWith("/me")) {
            return true; // "me" endpoints don't need explicit patient ID validation
        }
        
        logger.warn("Patient ID parameter '{}' not found in request for validation", patientIdParam);
        return false;
    }
    
    private void logAccess(String patientId, String resource, String uri) {
        if (resource != null && !resource.isEmpty()) {
            logger.info("Patient {} accessed resource '{}' at URI: {}", patientId, resource, uri);
        } else {
            logger.debug("Patient {} accessed URI: {}", patientId, uri);
        }
    }
}

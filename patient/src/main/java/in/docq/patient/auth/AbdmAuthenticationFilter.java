package in.docq.patient.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.CompletionException;

@Component
@Order(1)
public class AbdmAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(AbdmAuthenticationFilter.class);
    
    private final AbdmTokenValidator abdmTokenValidator;
    
    @Autowired
    public AbdmAuthenticationFilter(AbdmTokenValidator abdmTokenValidator) {
        this.abdmTokenValidator = abdmTokenValidator;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Skip authentication for certain paths
        if (shouldSkipAuthentication(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader == null || authHeader.trim().isEmpty()) {
                logger.warn("Missing Authorization header for path: {}", path);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Authorization header is required\"}");
                return;
            }
            
            // Validate ABDM token asynchronously
            AbdmTokenInfo tokenInfo = abdmTokenValidator.validateToken(authHeader)
                .toCompletableFuture()
                .join();
            
            // Set authenticated user information in request attributes
            request.setAttribute("abdmTokenInfo", tokenInfo);
            request.setAttribute("authenticatedPatientId", tokenInfo.getPatientId());
            request.setAttribute("abhaAddress", tokenInfo.getAbhaAddress());
            request.setAttribute("abhaNumber", tokenInfo.getAbhaNumber());
            
            logger.debug("Successfully authenticated patient: {}", tokenInfo.getPatientId());
            
            filterChain.doFilter(request, response);
            
        } catch (CompletionException e) {
            handleAuthenticationError(response, e.getCause());
        } catch (Exception e) {
            handleAuthenticationError(response, e);
        }
    }
    
    private boolean shouldSkipAuthentication(String path) {
        // Skip authentication for health checks, login endpoints, etc.
        return path.startsWith("/actuator/") ||
               path.startsWith("/patients/login/") ||
               path.startsWith("/patients/signup/") ||
               path.equals("/") ||
               path.startsWith("/swagger") ||
               path.startsWith("/v3/api-docs");
    }
    
    private void handleAuthenticationError(HttpServletResponse response, Throwable error) 
            throws IOException {
        logger.error("ABDM authentication failed", error);
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        String errorMessage = error instanceof AbdmAuthenticationException ? 
            error.getMessage() : "Authentication failed";
            
        response.getWriter().write(String.format("{\"error\":\"%s\"}", errorMessage));
    }
}

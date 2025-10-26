package in.docq.patient.auth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final String publicKey;
    
    @Autowired
    public AuthenticationFilter(@Value("${phr.publicKey}") String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);

            DecodedJWT decodedJWT = JWT.decode(token);

            // start verification process
            byte[] decodedKey = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Assuming all tokens are signed using RSA256 algorithm otherwise algorithm can be found using jwk.getAlgorithm() method
            Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) publicKey, null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(decodedJWT);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), "***"));
            request.setAttribute("authenticatedUser", decodedJWT.getClaim("preferred_username").asString());
            request.setAttribute("authenticatedToken", token);
        } catch (JWTVerificationException jwtVerificationException){
            logger.error("Verification Exception ", jwtVerificationException);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Validation Failed");
        }
        catch (Exception e){
            logger.error("Exception", e);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Validation Failed");
        }

        filterChain.doFilter(request, response);
        SecurityContextHolder.clearContext();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/actuator/") ||
               path.startsWith("/patients/login/") ||
               path.startsWith("/patients/sign-up/") ||
               path.equals("/") ||
               path.startsWith("/swagger") ||
               path.startsWith("/v3/api-docs");
    }
}

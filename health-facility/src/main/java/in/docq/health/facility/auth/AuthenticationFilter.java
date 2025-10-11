package in.docq.health.facility.auth;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwkProvider jwkProvider;
    private final String keyCloakBaseUrl;
    private final String realm;

    public AuthenticationFilter(
            @Value("${keycloak.base.url}") String keyCloakBaseUrl,
            @Value("${keycloak.jwks.cert}") String keyCloakCertPath,
            @Value("${keycloak.realm}") String realm) throws MalformedURLException {
        this.keyCloakBaseUrl = keyCloakBaseUrl;
        jwkProvider = new JwkProviderBuilder(new URL(keyCloakBaseUrl + keyCloakCertPath)).build();
        this.realm = realm;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);

            DecodedJWT decodedJWT = JWT.decode(token);

            // start verification process
            Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());

            // Assuming all tokens are signed using RSA256 algorithm otherwise algorithm can be found using jwk.getAlgorithm() method
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(keyCloakBaseUrl + "/realms/" + realm)
                    .withAudience("account")
                    .ignoreIssuedAt()
                    .build();
            verifier.verify(decodedJWT);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), "***"));
            request.setAttribute("authenticatedUser", decodedJWT.getClaim("preferred_username").asString());
            request.setAttribute("authenticatedToken", token);
            logger.info(token);
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
        return path.endsWith("/health") ||
                path.endsWith("/prometheus") ||
                path.endsWith("/login") ||
                path.endsWith("/on-generate-token") ||
                path.endsWith("/on_carecontext") ||
                path.endsWith("/on-notify") ||
                path.endsWith("/care-context/discover") ||
                path.endsWith("/care-context/init") ||
                path.endsWith("/care-context/confirm") ||
                path.endsWith("/consent/request/hip/notify") ||
                path.endsWith("/hip/health-information/request") ||
                path.endsWith("/hiu/consent/request/on-init") ||
                path.endsWith("/hiu/consent/request/notify") ||
                path.endsWith("/hiu/consent/on-fetch") ||
                path.endsWith("/hiu/health-information/on-request");
    }
}

package in.docq.health.facility.configuration;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import in.docq.health.facility.service.WsConnectionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WsConnectionHandler handler;
    private final AuthHandshakeInterceptor authInterceptor;

    public WebSocketConfig(WsConnectionHandler handler, AuthHandshakeInterceptor authInterceptor) {
        this.handler = handler;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws")
                .addInterceptors(authInterceptor)
                .setAllowedOrigins("*"); // configure properly in prod
    }

    @Component
    public static class AuthHandshakeInterceptor implements HandshakeInterceptor {

        private final JwkProvider jwkProvider;
        private final String keyCloakBaseUrl;
        private final String realm;  // your custom JWT validator

        public AuthHandshakeInterceptor(@Value("${keycloak.base.url}") String keyCloakBaseUrl,
                                        @Value("${keycloak.jwks.cert}") String keyCloakCertPath,
                                        @Value("${keycloak.realm}") String realm) throws MalformedURLException {
            this.keyCloakBaseUrl = keyCloakBaseUrl;
            jwkProvider = new JwkProviderBuilder(new URL(keyCloakBaseUrl + keyCloakCertPath)).build();
            this.realm = realm;
        }

        @Override
        public boolean beforeHandshake(
                ServerHttpRequest request,
                ServerHttpResponse response,
                WebSocketHandler wsHandler,
                Map<String, Object> attributes) throws JwkException {

            URI uri = request.getURI();
            String token = UriComponentsBuilder.fromUri(uri)
                    .build()
                    .getQueryParams()
                    .getFirst("token");

            if (token == null) {
                // ❌ reject connection
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
            try {
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
                attributes.put("userId", decodedJWT.getClaim("preferred_username").asString());  // store for later use
            } catch (JWTVerificationException jwtVerificationException){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
            }
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
            // no-op
        }
    }

}


package in.docq.patient.processor.configuration;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class KeyCloakClientConfiguration {

    @Bean("KeyCloakOkHttpClient")
    public OkHttpClient getKeyCloakOkHttpClient(@Value("${keycloak.http.max.idle.connections}") int maxIdleConnections,
                                                @Value("${keycloak.http.keep.alive.duration.minutes}") int keepAliveDurationMinutes,
                                                @Value("${keycloak.http.timeout.seconds}") int timeoutSeconds) {
        return new OkHttpClient.Builder()
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .callTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDurationMinutes, TimeUnit.MINUTES))
                .build();

    }
}

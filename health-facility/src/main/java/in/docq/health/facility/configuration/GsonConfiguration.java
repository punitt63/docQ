package in.docq.health.facility.configuration;

import in.docq.abha.rest.client.JSON;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class GsonConfiguration {
    @Bean
    public GsonBuilderCustomizer typeAdapterRegistration() {
        return builder -> {
            builder.registerTypeAdapter(LocalDate.class, new JSON.LocalDateTypeAdapter());
        };
    }
}

package in.docq.patient.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Use SimpleClientHttpRequestFactory which supports PATCH method in modern Spring Boot
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        return builder
                .requestFactory(() -> factory)
                .build();
    }
}

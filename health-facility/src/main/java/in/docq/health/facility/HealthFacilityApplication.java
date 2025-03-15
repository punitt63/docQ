package in.docq.health.facility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {"in.docq.spring.boot.commons", "in.docq"}, excludeFilters={
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= DataSourceAutoConfiguration.class)})
public class HealthFacilityApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(HealthFacilityApplication.class, args);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}

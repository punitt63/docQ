package in.docq.patient.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = {"in.docq.patient", "in.docq.abha"},
        excludeFilters={
        @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= DataSourceAutoConfiguration.class),
                @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= DataSourceTransactionManagerAutoConfiguration.class),
                @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value= HibernateJpaAutoConfiguration.class)})
public class PatientApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(PatientApplication.class, args);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}

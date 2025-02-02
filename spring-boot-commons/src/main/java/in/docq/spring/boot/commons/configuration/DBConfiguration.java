package in.docq.spring.boot.commons.configuration;

import in.docq.spring.boot.commons.postgres.PostgresDAO;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfiguration {

    @Bean
    public PostgresDAO getDB(BasicDataSource dataSource, @Value("${spring.datasource.poolSize}") int poolSize, MeterRegistry meterRegistry) {
        return new PostgresDAO(dataSource, poolSize, meterRegistry);
    }

    @Bean
    public BasicDataSource getDataSource(@Value("${spring.datasource.driver}") String driver,
                                         @Value("${spring.datasource.url}") String url,
                                         @Value("${spring.datasource.username}") String username,
                                         @Value("${spring.datasource.password}") String password,
                                         @Value("${spring.datasource.poolSize}") int poolSize,
                                         @Value("${spring.datasource.timeout}") int timeout) {
        return getBasicDataSource(driver, url, username, password, poolSize, timeout);
    }

    public static BasicDataSource getBasicDataSource(String driver, String url, String username, String password, int poolSize, int timeout) {
        Validate.notNull(driver, "driver must not be null");
        Validate.notNull(url, "url must not be null");
        Validate.notNull(username, "username must not be null");
        Validate.notNull(password, "password must not be null");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setMaxTotal(poolSize);
        dataSource.setMaxWaitMillis(timeout);
        dataSource.setDefaultQueryTimeout(timeout);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setValidationQuery("select 1");
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}

package in.docq.health.facility.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class EncryptionConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(EncryptionConfiguration.class);

    @Value("${encryption.aes.key:}")
    private String aesKeyBase64;

    @Bean
    public SecretKey aesEncryptionKey() {
        logger.info("Loading AES encryption key from configuration");
        byte[] decodedKey = Base64.getDecoder().decode(aesKeyBase64);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }
}
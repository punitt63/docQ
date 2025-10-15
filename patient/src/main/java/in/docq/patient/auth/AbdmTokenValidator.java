package in.docq.patient.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class AbdmTokenValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(AbdmTokenValidator.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Validates ABDM token and extracts patient information
     */
    public CompletionStage<AbdmTokenInfo> validateToken(String token) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (token == null || token.trim().isEmpty()) {
                    throw new AbdmAuthenticationException("ABDM token is required");
                }
                
                // Remove "Bearer " prefix if present
                String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
                
                // Parse JWT token (assuming it's a JWT)
                String[] tokenParts = actualToken.split("\\.");
                if (tokenParts.length != 3) {
                    throw new AbdmAuthenticationException("Invalid ABDM token format");
                }
                
                // Decode payload (second part)
                String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
                JsonNode payloadJson = objectMapper.readTree(payload);
                
                // Extract patient information (treat abhaAddress as patientId)
                String abhaAddress = extractAbhaAddress(payloadJson);
                String abhaNumber = extractAbhaNumber(payloadJson);
                String fullName = extractText(payloadJson, "fullName");
                String gender = extractText(payloadJson, "gender");
                String mobile = extractPreferredMobile(payloadJson);
                long expirationTime = extractExpirationTime(payloadJson);
                long issuedAt = extractIssuedAt(payloadJson);
                
                // Check if token is expired
                if (Instant.now().getEpochSecond() > expirationTime) {
                    throw new AbdmAuthenticationException("ABDM token has expired");
                }
                
                logger.info("Successfully validated ABDM token for patient: {}", abhaAddress);
                
                return AbdmTokenInfo.builder()
                    .abhaAddress(abhaAddress)
                    .abhaNumber(abhaNumber)
                    .fullName(fullName)
                    .gender(gender)
                    .mobile(mobile)
                    .expirationTime(expirationTime)
                    .issuedAt(issuedAt)
                    .originalToken(actualToken)
                    .build();
                    
            } catch (Exception e) {
                logger.error("Failed to validate ABDM token", e);
                throw new AbdmAuthenticationException("Invalid ABDM token: " + e.getMessage());
            }
        });
    }
    
    private String extractAbhaAddress(JsonNode payload) {
        if (payload.has("abhaAddress")) {
            JsonNode n = payload.get("abhaAddress");
            if (n.isArray() && n.size() > 0) return n.get(0).asText();
            if (n.isTextual()) return n.asText();
        }
        if (payload.has("sub")) {
            return payload.get("sub").asText();
        }
        throw new AbdmAuthenticationException("abhaAddress not found in ABDM token");
    }
    
    private String extractAbhaNumber(JsonNode payload) {
        // healthIdNumber in the provided sample
        if (payload.has("healthIdNumber")) return payload.get("healthIdNumber").asText();
        if (payload.has("abhaNumber")) return payload.get("abhaNumber").asText();
        return null;
    }
    
    private long extractExpirationTime(JsonNode payload) {
        if (payload.has("exp")) {
            return payload.get("exp").asLong();
        }
        // If no expiration time, assume token is valid for 1 hour from now
        return Instant.now().getEpochSecond() + 3600;
    }

    private long extractIssuedAt(JsonNode payload) {
        if (payload.has("iat")) {
            return payload.get("iat").asLong();
        }
        return 0L;
    }

    private String extractText(JsonNode payload, String field) {
        return payload.has(field) && !payload.get(field).isNull() ? payload.get(field).asText() : null;
    }

    private String extractPreferredMobile(JsonNode payload) {
        // Prefer mobile, fallback to phrMobile
        if (payload.has("mobile") && !payload.get("mobile").isNull()) {
            return payload.get("mobile").asText();
        }
        if (payload.has("phrMobile") && !payload.get("phrMobile").isNull()) {
            return payload.get("phrMobile").asText();
        }
        return null;
    }
    
    /**
     * Validates that the authenticated patient matches the requested patient ID
     */
    public boolean validatePatientIdMatch(String authenticatedPatientId, String requestedPatientId) {
        if (authenticatedPatientId == null || requestedPatientId == null) {
            return false;
        }
        
        boolean matches = authenticatedPatientId.equals(requestedPatientId);
        if (!matches) {
            logger.warn("Patient ID mismatch: authenticated={}, requested={}", 
                       authenticatedPatientId, requestedPatientId);
        }
        
        return matches;
    }
}

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
                
                // Extract patient information
                String patientId = extractPatientId(payloadJson);
                String abhaAddress = extractAbhaAddress(payloadJson);
                String abhaNumber = extractAbhaNumber(payloadJson);
                long expirationTime = extractExpirationTime(payloadJson);
                
                // Check if token is expired
                if (Instant.now().getEpochSecond() > expirationTime) {
                    throw new AbdmAuthenticationException("ABDM token has expired");
                }
                
                logger.info("Successfully validated ABDM token for patient: {}", patientId);
                
                return AbdmTokenInfo.builder()
                    .patientId(patientId)
                    .abhaAddress(abhaAddress)
                    .abhaNumber(abhaNumber)
                    .expirationTime(expirationTime)
                    .originalToken(actualToken)
                    .build();
                    
            } catch (Exception e) {
                logger.error("Failed to validate ABDM token", e);
                throw new AbdmAuthenticationException("Invalid ABDM token: " + e.getMessage());
            }
        });
    }
    
    private String extractPatientId(JsonNode payload) {
        // Try different possible fields for patient ID
        if (payload.has("sub")) {
            return payload.get("sub").asText();
        }
        if (payload.has("patientId")) {
            return payload.get("patientId").asText();
        }
        if (payload.has("abhaNumber")) {
            return payload.get("abhaNumber").asText();
        }
        throw new AbdmAuthenticationException("Patient ID not found in ABDM token");
    }
    
    private String extractAbhaAddress(JsonNode payload) {
        if (payload.has("abhaAddress")) {
            JsonNode abhaAddressNode = payload.get("abhaAddress");
            if (abhaAddressNode.isArray() && abhaAddressNode.size() > 0) {
                return abhaAddressNode.get(0).asText();
            } else if (abhaAddressNode.isTextual()) {
                return abhaAddressNode.asText();
            }
        }
        return null; // ABHA address might not always be present
    }
    
    private String extractAbhaNumber(JsonNode payload) {
        if (payload.has("abhaNumber")) {
            return payload.get("abhaNumber").asText();
        }
        return null;
    }
    
    private long extractExpirationTime(JsonNode payload) {
        if (payload.has("exp")) {
            return payload.get("exp").asLong();
        }
        // If no expiration time, assume token is valid for 1 hour from now
        return Instant.now().getEpochSecond() + 3600;
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

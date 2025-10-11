package in.docq.health.facility.model;

import in.docq.health.facility.controller.PatientController;
import lombok.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@Builder
@Getter
public class Patient {
    private final String abhaNo;
    private final String abhaAddress;
    private final String name;
    private final String mobileNo;
    private final LocalDate dob;
    private final String gender;

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int AES_KEY_SIZE = 256;

    public String getId() {
        return Optional.ofNullable(abhaAddress).orElse(mobileNo + "-" + name + "-" + dob);
    }

    public static Patient fromRequestBody(PatientController.CreatePatientRequestBody requestBody) {
        return Patient.builder()
                .abhaNo(requestBody.getAbhaNo())
                .abhaAddress(requestBody.getAbhaAddress())
                .name(requestBody.getName())
                .mobileNo(requestBody.getMobileNo())
                .dob(requestBody.getDob())
                .gender(requestBody.getGender())
                .build();
    }

    public Patient encrypt(SecretKey encryptionKey) {
        return Patient.builder()
                .abhaNo(Optional.ofNullable(abhaNo).map(d -> encryptField(d, encryptionKey)).orElse(null))
                .abhaAddress(Optional.ofNullable(abhaAddress).map(d -> encryptField(d, encryptionKey)).orElse(null))
                .name(name)
                .mobileNo(mobileNo)
                .dob(dob)
                .gender(gender)
                .build();
    }

    public Patient decrypt(SecretKey decryptionKey) {
        return Patient.builder()
                .abhaNo(Optional.ofNullable(abhaNo).map(d -> decrypt(d, decryptionKey)).orElse(null))
                .abhaAddress(Optional.ofNullable(abhaAddress).map(d -> decrypt(d, decryptionKey)).orElse(null))
                .name(name)
                .mobileNo(mobileNo)
                .dob(dob)
                .gender(gender)
                .build();
    }

    private String encryptField(String data, SecretKey encryptionKey) {
        try {
            byte[] iv = new byte[12]; // GCM recommended IV size
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, parameterSpec);

            byte[] encryptedData = cipher.doFinal(data.getBytes());

            // Combine IV and encrypted data
            byte[] combined = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    private String decrypt(String encryptedData, SecretKey decryptionKey) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedData);

            // Extract IV
            byte[] iv = new byte[12];
            System.arraycopy(decoded, 0, iv, 0, iv.length);

            // Extract data
            byte[] data = new byte[decoded.length - iv.length];
            System.arraycopy(decoded, iv.length, data, 0, data.length);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, decryptionKey, parameterSpec);

            return new String(cipher.doFinal(data));
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    public static Patient fromRequestBody(PatientController.ReplacePatientRequestBody requestBody) {
        return Patient.builder()
                .abhaNo(requestBody.getAbhaNo())
                .abhaAddress(requestBody.getAbhaAddress())
                .name(requestBody.getNewName())
                .mobileNo(requestBody.getNewMobileNo())
                .dob(requestBody.getNewDob())
                .gender(requestBody.getGender())
                .build();
    }
}
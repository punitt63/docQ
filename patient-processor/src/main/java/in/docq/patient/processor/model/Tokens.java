package in.docq.patient.processor.model;

import in.docq.abha.rest.client.model.AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens;
import in.docq.abha.rest.client.model.AbhaApiV3PhrWebLoginAbhaVerifyPost200ResponseTokens;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Tokens {

    String token;
    Integer expiresIn;
    String refreshToken;
    Integer refreshExpiresIn;

    public static Tokens toTokens(String token, Integer expiresIn, String refreshToken, Integer refreshExpiresIn) {
        return Tokens.builder()
                .token(token)
                .expiresIn(expiresIn)
                .refreshToken(refreshToken)
                .refreshExpiresIn(refreshExpiresIn)
                .build();
    }

    public static Tokens toTokens(AbhaApiV3EnrollmentEnrolByAadhaarPost200ResponseAnyOfTokens tokens) {
        return Tokens.builder()
                .token(tokens.getToken())
                .expiresIn(tokens.getExpiresIn())
                .refreshToken(tokens.getRefreshToken())
                .refreshExpiresIn(tokens.getRefreshExpiresIn())
                .build();
    }

    public static Tokens toTokens(AbhaApiV3PhrWebLoginAbhaVerifyPost200ResponseTokens tokens) {
        return Tokens.builder()
                .token(tokens.getToken())
                .expiresIn(tokens.getExpiresIn())
                .refreshToken(tokens.getRefreshToken())
                .refreshExpiresIn(tokens.getRefreshExpiresIn())
                .build();
    }
}

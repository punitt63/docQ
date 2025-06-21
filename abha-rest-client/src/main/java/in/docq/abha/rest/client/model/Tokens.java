package in.docq.abha.rest.client.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Tokens {

    String token;
    Integer expiresIn;
    String refreshToken;
    Integer refreshExpiresIn;

    public static Tokens toTokens(String token, String expiresIn, String refreshToken, String refreshExpiresIn) {
        return Tokens.builder()
                .token(token)
                .expiresIn(expiresIn != null ? Integer.valueOf(expiresIn) : null)
                .refreshToken(refreshToken)
                .refreshExpiresIn(refreshExpiresIn != null ? Integer.valueOf(refreshExpiresIn) : null)
                .build();
    }

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

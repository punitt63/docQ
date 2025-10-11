package in.docq.health.facility.service;

import java.util.concurrent.CompletionStage;

public interface OTPService {
    public CompletionStage<String> sendOtp(String phoneNumber);
}

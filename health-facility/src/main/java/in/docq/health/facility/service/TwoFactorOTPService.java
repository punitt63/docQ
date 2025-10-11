package in.docq.health.facility.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
public class TwoFactorOTPService implements OTPService {
    private static final String API_KEY = "YOUR_2FACTOR_API_KEY";
    private static final String BASE_URL = "https://2factor.in/API/V1/";
    private final OkHttpClient client = new OkHttpClient();

    private int generateOtp() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // ensures 6 digits
    }

    @Override
    public CompletionStage<String> sendOtp(String phoneNumber) {
        String otp = String.valueOf(generateOtp());
        String url = BASE_URL + API_KEY + "/SMS/" + phoneNumber + "/" + otp;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        CompletableFuture<String> future = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(
                                new RuntimeException("Unexpected code " + response));
                        return;
                    }
                    String body = responseBody.string();
                    System.out.println("Send OTP Response: " + body);
                    // TODO: parse sessionId and persist in DB
                    future.complete(otp);
                }
            }
        });

        return future;
    }
}

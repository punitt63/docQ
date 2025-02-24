package in.docq.abha.rest.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FutureApiCallBack<T> implements ApiCallback<T> {
    private final CompletableFuture<T> future;

    public FutureApiCallBack() {
        this.future = new CompletableFuture<>();
    }

    public static <T> FutureApiCallBack<T> newCallback() {
        return new FutureApiCallBack<>();
    }

    public CompletableFuture<T> getFuture() {
        return future;
    }

    @Override
    public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
        future.completeExceptionally(e);
    }

    @Override
    public void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders) {
        future.complete(result);
    }

    @Override
    public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {

    }

    @Override
    public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {

    }
}

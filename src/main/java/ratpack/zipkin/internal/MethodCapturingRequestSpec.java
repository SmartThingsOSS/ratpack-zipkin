package ratpack.zipkin.internal;

import ratpack.func.Action;
import ratpack.func.Function;
import ratpack.http.HttpMethod;
import ratpack.http.MutableHeaders;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.time.Duration;

public class MethodCapturingRequestSpec implements RequestSpec {

    private HttpMethod capturedMethod = null;

    @Override
    public RequestSpec redirects(int maxRedirects) {
        return this;
    }

    @Override
    public RequestSpec onRedirect(Function<? super ReceivedResponse, Action<? super RequestSpec>> function) {
        return this;
    }

    @Override
    public RequestSpec sslContext(SSLContext sslContext) {
        return this;
    }

    @Override
    public MutableHeaders getHeaders() {
        return null;
    }

    @Override
    public RequestSpec maxContentLength(int numBytes) {
        return this;
    }

    @Override
    public RequestSpec headers(Action<? super MutableHeaders> action) throws Exception {
        return this;
    }

    @Override
    public RequestSpec method(HttpMethod method) {
        capturedMethod = method;
        return this;
    }

    @Override
    public RequestSpec decompressResponse(boolean shouldDecompress) {
        return this;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public RequestSpec connectTimeout(Duration duration) {
        return this;
    }

    @Override
    public RequestSpec readTimeout(Duration duration) {
        return this;
    }

    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public RequestSpec body(Action<? super Body> action) throws Exception {
        return this;
    }

    public HttpMethod getCapturedMethod() {
        return capturedMethod;
    }
}

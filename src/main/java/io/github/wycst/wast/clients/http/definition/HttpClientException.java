package io.github.wycst.wast.clients.http.definition;

/**
 * @author wangy
 * @since 2020/7/2 9:20
 */
public class HttpClientException extends RuntimeException {

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpClientException(Throwable cause) {
        super(cause);
    }
}

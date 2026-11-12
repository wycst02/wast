package io.github.wycst.wast.jdbc.exception;

/**
 * @author wangy
 * @since 2021/2/15 20:06
 */
public class OqlParematerException extends RuntimeException {

    public OqlParematerException(String message) {
        super(message);
    }

    public OqlParematerException(String message, Throwable cause) {
        super(message, cause);
    }
}

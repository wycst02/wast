package io.github.wycst.wast.jdbc.exception;

/**
 * @author wangy
 * @since 2021/2/24 21:53
 */
public class SqlExecuteException extends RuntimeException {

    public SqlExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlExecuteException(String message) {
        super(message);
    }
}

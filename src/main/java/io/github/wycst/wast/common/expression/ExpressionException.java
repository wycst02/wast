package io.github.wycst.wast.common.expression;

/**
 * @author wangy
 * @since 2021/9/25 22:43
 */
public class ExpressionException extends RuntimeException {

    public ExpressionException(String message) {
        super(message);
    }

    public ExpressionException(String message, Throwable cause) {
        super(message, cause);
    }
}

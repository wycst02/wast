package io.github.wycst.wast.common.annotation;

import java.lang.annotation.Annotation;

/**
 * 注解接口处理
 *
 * @author wangy
 * @since 2019/12/8 9:38
 */
public interface AnnotationHandler<A, R> {

    /**
     * 方法调用之前
     *
     * @return
     */
    public R beforeAnnotationHandle(A a);


    /**
     * 方法调用之后
     *
     * @return
     */
    public void afterWithResult(Annotation annotation, R result, Throwable err);

}

package io.github.wycst.wast.json;

import io.github.wycst.wast.common.reflect.GenericParameterizedType;

/**
 * 对于未知抽象类和接口提供构造实例的接口
 *
 * @since 2024/4/11 13:06
 * @author wangyc
 */
public interface JSONImplInstCreator<T> {

    /**
     * @param parameterizedType
     * @return
     */
    public T create(GenericParameterizedType<T> parameterizedType);

}

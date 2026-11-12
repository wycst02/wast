package io.github.wycst.wast.common.expression;

import java.util.Map;

/**
 * @author wangy
 * @since 2022/10/30 11:24
 */
public interface ElInvoker {

    ElSecureTrustedAccess SECURE_TRUSTED_ACCESS = new ElSecureTrustedAccess();

    /**
     * 直接invoke不缓存
     *
     * @param context
     * @return
     */
    Object invokeDirect(Object context);

    /**
     * 直接invoke不缓存
     *
     * @param context
     * @return
     */
    Object invokeDirect(Map context);

    Object invoke(Object entityContext, Object[] variableValues);

    Object invoke(Map mapContext, Object[] variableValues);

    Object invokeCurrent(Map globalContext, Object parentContext, Object[] variableValues);

    Object invokeCurrent(Object globalContext, Object parentContext, Object[] variableValues);

    void internKey();

//    int size();
}

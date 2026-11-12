package io.github.wycst.wast.json;

/**
 * <p>
 * JSON类型模块化注册（集中注册管理）
 * </p>
 *
 * @author wangyc
 */
public interface JSONTypeModule {

    void register(JSONTypeRegistor registor);
}

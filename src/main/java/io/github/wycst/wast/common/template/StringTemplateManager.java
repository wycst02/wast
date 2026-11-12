package io.github.wycst.wast.common.template;

import io.github.wycst.wast.common.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模板管理
 *
 * @author wangy
 * @since 2021/9/21 17:39
 */
public class StringTemplateManager {

    // 模板列表
    private static Map<String, StringTemplate> resourceTemplates = new ConcurrentHashMap<String, StringTemplate>();

    /**
     * 获取资源模板对象
     *
     * @param resource
     * @return
     */
    public static synchronized StringTemplate getStringTemplate(String resource) {
        if (StringUtils.isEmpty(resource)) {
            return null;
        }
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }
        if (resourceTemplates.containsKey(resource)) {
            return resourceTemplates.get(resource);
        }
        String templateSource = StringUtils.fromResource(resource);
        if (templateSource == null) {
            return null;
        }
        StringTemplate template = new StringTemplate(templateSource);
        resourceTemplates.put(resource, template);
        return template;
    }


}

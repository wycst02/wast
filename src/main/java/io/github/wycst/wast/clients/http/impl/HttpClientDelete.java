package io.github.wycst.wast.clients.http.impl;

import io.github.wycst.wast.clients.http.definition.HttpClientConfig;
import io.github.wycst.wast.clients.http.definition.HttpClientMethod;

/**
 * DELETE 资源
 *
 * @author wangy
 * @since 2020/7/2 19:19
 */
public class HttpClientDelete extends HttpClientRequestImpl {

    public HttpClientDelete(String url) {
        super(url, HttpClientMethod.DELETE);
    }

    public HttpClientDelete(String spec, HttpClientConfig httpClientConfig) {
        super(spec, HttpClientMethod.DELETE, httpClientConfig);
    }
}

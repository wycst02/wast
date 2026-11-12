package io.github.wycst.wast.clients.http.provider;

import java.net.URL;

/**
 * @author wangy
 * @since 2021/8/8 1:12
 */
public class RequestServiceInstance {

    private ServiceInstance serviceInstance;
    private URL url;

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}

package io.github.wycst.wast.clients.http.url;

import io.github.wycst.wast.clients.http.executor.HttpClientExecutor;
import io.github.wycst.wast.clients.http.ssl.X509TrustManagerImpl;
import io.github.wycst.wast.common.utils.IOUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.SecureRandom;

/**
 * @Author: wangy
 * @Date: 2020/7/7 19:16
 * @Description:
 */
abstract class AbstractUrlHttpClientExecutor extends HttpClientExecutor {

    private static volatile SSLContext insecureSslContext;

    private static final HostnameVerifier INSECURE_HOSTNAME_VERIFIER = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Apply the trust-all SSL settings to this connection only when explicitly enabled,
     * leaving JVM-wide SSL defaults untouched.
     */
    static void applySslConfigIfNeeded(HttpURLConnection httpConnection, boolean trustAllCertificates) throws Exception {
        if (trustAllCertificates && httpConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) httpConnection;
            httpsConnection.setSSLSocketFactory(insecureSslContext().getSocketFactory());
            httpsConnection.setHostnameVerifier(INSECURE_HOSTNAME_VERIFIER);
        }
    }

    private static SSLContext insecureSslContext() throws Exception {
        SSLContext context = insecureSslContext;
        if (context == null) {
            synchronized (AbstractUrlHttpClientExecutor.class) {
                context = insecureSslContext;
                if (context == null) {
                    context = SSLContext.getInstance("TLS");
                    context.init(null, new TrustManager[]{new X509TrustManagerImpl()}, new SecureRandom());
                    insecureSslContext = context;
                }
            }
        }
        return context;
    }

    protected static byte[] readInputStream(InputStream is, int contentLength) {
        if (is != null) {
            boolean isClosed = false;
            try {
                if (contentLength > 0) {
                    byte[] buf = new byte[contentLength];
                    int offset = 0, len = buf.length, count;
                    // Read until -1 or offset = contentLength
                    while ((count = is.read(buf, offset, len)) != -1) {
                        offset += count;
                        if (count == len) break;
                        len -= count;
                    }
                    if (offset != contentLength) {
                        // The actual length of the read content is less than the content length declared in the header
                        // whether to throw an error
                    }
                    return buf;
                } else {
                    return IOUtils.readBytes(is);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!isClosed) {
                        is.close();
                    }
                } catch (IOException e) {
                }
            }
        }
        throw new RuntimeException("InputStream Empty error");
    }

}

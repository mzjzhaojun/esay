package com.yt.app.common.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;

public class HttpUtils {

    //设置请求和传输超时时间
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();

    /**
     * 发起post请求，请求参数以Map集合形式传入
     *
     * @param httpUrl
     * @param body
     * @return
     * @throws Exception
     */
    public static String post(String httpUrl, Map<String, String> headers, Map<String, Object> body) {
        CloseableHttpClient httpclient = createSSLClientDefault();
        BufferedReader in;
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            httpPost.setHeader("Accept", "application/json");
            for (Map.Entry<String, String> headerMap : headers.entrySet()) {
                httpPost.addHeader(headerMap.getKey(), headerMap.getValue());
            }
            httpPost.setConfig(requestConfig);
            StringEntity entity = new StringEntity(JSON.toJSONString(body), StandardCharsets.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            InputStream content = response.getEntity().getContent();
            in = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 发起get请求，请求参数以Map集合形式传入
     *
     * @param httpUrl
     * @param params
     * @return
     * @throws Exception
     */
    public static String get(String httpUrl, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient httpclient = createSSLClientDefault();
        BufferedReader in;
        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            paramList.add(param.getKey() + "=" + param.getValue());
        }
        HttpGet httpGet = new HttpGet(httpUrl + "?" + String.join("&", paramList));
        try {
            httpGet.setHeader("Accept", "application/json");
            for (Map.Entry<String, String> headerMap : headers.entrySet()) {
                httpGet.addHeader(headerMap.getKey(), headerMap.getValue());
            }
            httpGet.setConfig(requestConfig);

            CloseableHttpResponse response = httpclient.execute(httpGet);
            InputStream content = response.getEntity().getContent();
            in = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            // SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 在JSSE中，证书信任管理器类就是实现了接口X509TrustManager的类。我们可以自己实现该接口，让它信任我们指定的证书。
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            // 信任所有
            X509TrustManager x509mgr = new X509TrustManager() {
                // 该方法检查客户端的证书，若不信任该证书则抛出异常
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) {
                }

                // 该方法检查服务端的证书，若不信任该证书则抛出异常
                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) {
                }

                // 返回受信任的X509证书数组。
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext sslContext = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            sslContext.init(null, new TrustManager[]{x509mgr}, null);
            // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
            SSLConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier());
            // HttpsURLConnection对象就可以正常连接HTTPS了，无论其证书是否经权威机构的验证，只要实现了接口X509TrustManager的类MyX509TrustManager信任该证书。
            return HttpClients.custom().setSSLSocketFactory(sslSf).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 创建默认的httpClient实例.
        return HttpClients.createDefault();
    }
}
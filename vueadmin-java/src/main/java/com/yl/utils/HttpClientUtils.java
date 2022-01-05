package com.yl.utils;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import org.apache.commons.io.IOUtils;

/**
 * @author ：jerry
 * @date ：Created in 2021/11/3 21:27
 * @description：http 请求工具类
 * @version: V1.1
 */
@Slf4j
public abstract class HttpClientUtils {

    public  final int connTimeout = 100000;
    public  final int readTimeout = 100000;
    public  final int connectionRequestTimeout = 100000;
    public  final String charset = "UTF-8";

    public static HttpClient client = null;
    {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }

    public static Map<String, List<String>> convertHeaders(Header[] headers) {
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        for (Header header : headers) {
            List<String> list = results.get(header.getName());
            if (list == null) {
                list = new ArrayList<String>();
                results.put(header.getName(), list);
            }
            list.add(header.getValue());
        }
        return results;
    }

    /**
     * http的get请求
     *
     * @param url
     */
    public static String get(String url) {
        return get(url, "UTF-8");
    }


    /**
     * http的get请求
     *
     * @param url
     */
    public static String get(String url, String charset) {
        HttpGet httpGet = new HttpGet(url);
        return executeRequest(httpGet, charset);
    }


    /**
     * post 请求form表单的形式
     * */
    public String postForm(String url, Map<String, Object> params, HttpClientContext context)
            throws ConnectTimeoutException, SocketTimeoutException, Exception {
        return postParameters(url, params, null, connTimeout, readTimeout, context);
    }
    public  String postParameters(String url, Map<String, Object> params, Map<String, String> headers,
                            Integer connTimeout, Integer readTimeout, HttpClientContext context)
            throws ConnectTimeoutException, SocketTimeoutException, Exception {
        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<org.apache.http.NameValuePair>();
                Set<Map.Entry<String, Object>> entrySet = params.entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置参数
            RequestConfig.Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
                customReqConf.setConnectionRequestTimeout(connectionRequestTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(post, context);
            } else {
                // 执行 Http 请求.
                client =  this.client;
                res = client.execute(post, context);
            }
            return IOUtils.toString(res.getEntity().getContent(), "utf-8");
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }


    /**
     * psot请求 JSON传参数
     * */
    public  String postJson(String url, JSONObject json, LinkedHashMap<String, String> headers,
                            HttpClientContext context) throws ConnectTimeoutException, SocketTimeoutException, Exception {
        return postJSONObj(url, json, headers, context);
    }
    public  String postJSONObj(String url, JSONObject json, LinkedHashMap<String, String> headers, HttpClientContext context)
            throws ConnectTimeoutException, SocketTimeoutException, Exception {
        HttpClient client = null;
        client = this.client;
        HttpPost post = new HttpPost(url);
        HttpResponse res = null;
        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Basic YWRtaW46");
        String result = "";
        try {
            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    post.setHeader(key, headers.get(key));
                }
            }
            // 发送请求
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(post, context);
            } else {
                // 执行 Http 请求.
                client = this.client;
                res = client.execute(post, context);
            }
            result = IOUtils.toString(res.getEntity().getContent(), charset);
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return result;

    }

    /**
     * http的get请求，增加异步请求头参数
     *
     * @param url
     */
    public static String ajaxGet(String url, String charset) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
        return executeRequest(httpGet, charset);
    }

    /**
     * @param url
     * @return
     */
    public static String ajaxGet(CloseableHttpClient httpclient, String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
        return executeRequest(httpclient, httpGet, "UTF-8");
    }

    /**
     * http的post请求，传递map格式参数
     */
    public static String post(String url, Map<String, String> dataMap) {
        return post(url, dataMap, "UTF-8");
    }

    /**
     * http的post请求，传递map格式参数
     */
    public static String post(String url, Map<String, String> dataMap, String charset) {
        HttpPost httpPost = new HttpPost(url);
        try {
            if (dataMap != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, charset);
                formEntity.setContentEncoding(charset);
                httpPost.setEntity(formEntity);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return executeRequest(httpPost, charset);
    }

    /**
     * http的post请求，增加异步请求头参数，传递map格式参数
     */
    public static String ajaxPost(String url, Map<String, String> dataMap) {
        return ajaxPost(url, dataMap, "UTF-8");
    }

    /**
     * http的post请求，增加异步请求头参数，传递map格式参数
     */
    public static String ajaxPost(String url, Map<String, String> dataMap, String charset) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        try {
            if (dataMap != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, charset);
                formEntity.setContentEncoding(charset);
                httpPost.setEntity(formEntity);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return executeRequest(httpPost, charset);
    }

    /**
     * http的post请求，增加异步请求头参数，传递json格式参数
     */
    public static String ajaxPostJson(String url, String jsonString) {
        return ajaxPostJson(url, jsonString, "UTF-8");
    }

    /**
     * http的post请求，增加异步请求头参数，传递json格式参数
     */
    public static String ajaxPostJson(String url, String jsonString, String charset) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");

        StringEntity stringEntity = new StringEntity(jsonString, charset);// 解决中文乱码问题
        stringEntity.setContentEncoding(charset);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return executeRequest(httpPost, charset);
    }

    /**
     * 执行一个http请求，传递HttpGet或HttpPost参数
     */
    public static String executeRequest(HttpUriRequest httpRequest) {
        return executeRequest(httpRequest, "UTF-8");
    }

    /**
     * 执行一个http请求，传递HttpGet或HttpPost参数(兼容https)
     */
    public static String executeRequest(HttpUriRequest httpRequest, String charset) {
        CloseableHttpClient httpclient;
        if ("https".equals(httpRequest.getURI().getScheme())) {
            httpclient = createSSLInsecureClient();
        } else {
            httpclient = HttpClients.createDefault();
        }
        String result = "";
        try {
            try {
                CloseableHttpResponse response = httpclient.execute(httpRequest);
                HttpEntity entity = null;
                try {
                    entity = response.getEntity();
                    result = EntityUtils.toString(entity, charset);
                } finally {
                    EntityUtils.consume(entity);
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String executeRequest(CloseableHttpClient httpclient, HttpUriRequest httpRequest, String charset) {
        String result = "";
        try {
            try {
                CloseableHttpResponse response = httpclient.execute(httpRequest);
                HttpEntity entity = null;
                try {
                    entity = response.getEntity();
                    result = EntityUtils.toString(entity, charset);
                } finally {
                    EntityUtils.consume(entity);
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 创建 SSL连接
     */
    public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}
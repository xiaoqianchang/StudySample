package com.changxiao.downloadservicedemo.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.zritc.colorfulfund.utils.ZRHttpResourceCache;
import com.zritc.colorfulfund.utils.ZRJsonValidator;
import com.zritc.colorfulfund.utils.ZRLog;
import com.zritc.colorfulfund.utils.ZRRequestParams;
import com.zritc.colorfulfund.utils.ZRStrings;
import com.zritc.colorfulfund.utils.ZRUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

/**
 * Utils for http connection.
 *
 * @author gufei
 * @version 1.0
 * @createDate 2014-07-23
 * @lastUpdate 2014-07-23
 */
public class ZRHttpManager {

    public static final String POST = "POST";
    public static final String GET = "GET";

    public static final int HTTP_CONNECTION_OUT_60 = 60000;
    public static final int HTTP_TIME_OUT = 60000;

    public static final String ACCPET_CONTENT = "*/*";
    public static final String ACCPET_KEY = "accpet";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String CONTENT_TYPE_KEY = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "text/xml";
    public static final String CONTENT_TYPE_VALUE2 = "multipart/form-data;boundary=";
    public static final String CONTENT_TYPE_VALUE3 = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_VALUE4 = "image/*";
    public static final String CHARSET_KEY = "charset";
    public static final String GZIP = "gzip";

    static Context context;
    static HttpClient client;
    static HttpParams params;

    public static void init(Context ctx) {
        context = ctx;
        params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, HTTP_CONNECTION_OUT_60);
        HttpConnectionParams.setSoTimeout(params, HTTP_TIME_OUT);
//		ZRLog.i("ZRHttpManager,sys agent", System.getProperty("http.agent"));
//		ZRLog.i("ZRHttpManager,browser agent",
//				ZRDeviceInfo.getUserAgent(context));
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https",
                new ZREasySSLSocketFactory(), 443));
        // schemeRegistry.register(new Scheme("https",
        // new DFEasySSLSocketFactory(), 8443));
        ClientConnectionManager connManager = new ThreadSafeClientConnManager(
                params, schemeRegistry);
        client = new DefaultHttpClient(connManager, params);

        // 代理设置
        // mClient.setRoutePlanner(new HttpRoutePlanner() {
        // @Override
        // public HttpRoute determineRoute(HttpHost target,
        // HttpRequest request, HttpContext context)
        // throws HttpException {
        // return new HttpRoute(target, null, new HttpHost("10.128.20.6",
        // 808), "https".equalsIgnoreCase(target.getSchemeName()));
        // }
        // });

    }

    public static final Object sendMessage(String url, String message,
                                           String method, HashMap<String, String> header)
            throws SSLHandshakeException, IOException {
        return sendMessage(url, message, method, header, true, true, true);
    }

    /**
     * Send a HTTP message. <b>This should not be called in main thread.</b>
     *
     * @param url     URL of target server
     * @param message message content
     * @param method  {@link #POST} or {@link #GET}
     * @return response from server
     * @throws IOException
     */
    public static final Object sendMessage(String url, String message,
                                           String method, HashMap<String, String> header,
                                           boolean withLastModified, boolean isString, boolean useCache)
            throws SSLHandshakeException, IOException {
        ZRLog.d("sendMessage:\n" + url + "\n " + method + "\n " + message);
        if (!ZRUtils.isUrl(url)) {
            throw new IOException();
        }

        HttpUriRequest request = null;
        HttpResponse response = null;
        HttpEntity entity = null;

        if (POST.equalsIgnoreCase(method)) {
            HttpPost post = new HttpPost(url);
            if (!TextUtils.isEmpty(message)) {
                ZRRequestParams params = new ZRRequestParams();
                List<BasicNameValuePair> valuePair = params
                        .getBasicNameValuePair(message);
                post.setEntity(new UrlEncodedFormEntity(valuePair, HTTP.UTF_8));
            }
            request = post;
        } else {
            if (!TextUtils.isEmpty(message)) {
                url += message;
            }
            request = new HttpGet(url.trim());
            if (withLastModified && useCache) {
                String lastModified = ZRHttpResourceCache
                        .getFormatLastModified(url);
                ZRLog.d("Http resouce last modified:" + lastModified);
                request.addHeader(IF_MODIFIED_SINCE, lastModified);
            }
        }

        if (null != header) {
            Iterator<String> it = header.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                request.addHeader(key, header.get(key));
            }
        }
        request.addHeader(ACCPET_KEY, ACCPET_CONTENT);
        request.addHeader(CONTENT_TYPE_KEY, CONTENT_TYPE_VALUE3);
        request.addHeader(CHARSET_KEY, HTTP.UTF_8);

        response = client.execute(request);

        if (null != response) {
            int statusCode = response.getStatusLine().getStatusCode();
            ZRLog.d("HttpStatus.SC_OK: " + statusCode);

            // 200 && 405 right return
            if (HttpStatus.SC_OK == statusCode
                    || HttpStatus.SC_METHOD_NOT_ALLOWED == statusCode) {
                if (isString) {
                    String resp = EntityUtils.toString(response.getEntity(),
                            "UTF-8");
                    ZRLog.d("response: " + resp);
                    if (GET.equalsIgnoreCase(method)) {
                        Header lastModifiedHead = response
                                .getLastHeader(LAST_MODIFIED);
                        if (null != lastModifiedHead) {
                            String lastModified = lastModifiedHead.getValue();
                            if (!TextUtils.isEmpty(lastModified)) {
                                ZRLog.d("Http resouce start cache");
                                if (ZRHttpResourceCache.saveResourceUTF8(url,
                                        resp)) {
                                    ZRLog.d("Http resouce cache success: "
                                            + lastModified);
                                    ZRHttpResourceCache.setFormatLastModified(
                                            url, lastModified);
                                }
                            }
                        } else if (!withLastModified) {
                            if (ZRHttpResourceCache.saveResourceUTF8(url, resp)) {
                                ZRLog.d("Http resouce cache success: " + url);
                                ZRHttpResourceCache.setFormatLastModified(url,
                                        "");
                            }
                        }
                    }
                    if (!new ZRJsonValidator().validate(resp)) {
                        resp = "{" + resp + "}";
                        if (!new ZRJsonValidator().validate(resp)) {
                            resp = getEmptyJson("");
                            ZRLog.d("valid prase response:" + resp);
                        }
                    }
                    return resp;
                } else {
                    try {
                        Bitmap bmp = BitmapFactory
                                .decodeStream(new BufferedHttpEntity(response
                                        .getEntity()).getContent());
                        ZRLog.d("response: " + bmp);
                        if (GET.equalsIgnoreCase(method) && null != bmp
                                && useCache) {
                            Header lastModifiedHead = response
                                    .getLastHeader(LAST_MODIFIED);
                            if (null != lastModifiedHead) {
                                String lastModified = lastModifiedHead
                                        .getValue();
                                if (ZRHttpResourceCache.cacheBitmap(url, bmp)) {
                                    ZRLog.d("url image cache success: " + url);
                                    ZRHttpResourceCache.setFormatLastModified(
                                            url, lastModified);
                                }
                            } else if (!withLastModified) {
                                if (ZRHttpResourceCache.cacheBitmap(url, bmp)) {
                                    ZRLog.d("url image cache success: " + url);
                                    ZRHttpResourceCache.setFormatLastModified(
                                            url, "");
                                }
                            }
                        }
                        return bmp;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (HttpStatus.SC_NOT_MODIFIED == statusCode
                    && GET.equalsIgnoreCase(method)) {
                ZRLog.d("Http resouce not modified");
                entity = response.getEntity();
                if (null != entity) {
                    entity.consumeContent();
                }
                if (isString) {
                    try {
                        return ZRHttpResourceCache.getResourceUTF8(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                        ZRLog.e("read cache failed, ready for reload");
                        return sendMessage(url, message, method, header, false,
                                isString, useCache);
                    }
                } else {
                    Bitmap bmp = ZRHttpResourceCache.getBitmap(url);
                    if (null == bmp) {
                        ZRLog.e("read cache failed, ready for reload");
                        return sendMessage(url, message, method, header, false,
                                isString, useCache);
                    } else {
                        return bmp;
                    }
                }
            } else {
                try {
                    entity = response.getEntity();
                    String resp = EntityUtils.toString(entity, "UTF-8");
                    if (!new ZRJsonValidator().validate(resp)) {
                        resp = getEmptyJson("");
                        ZRLog.d("valid prase response:" + resp);
                    }
                    return resp;
                } catch (ParseException e) {
                    e.printStackTrace();
                    if (null != entity) {
                        entity.consumeContent();
                    }
                }
            }
        }
        throw new IOException();
    }

    private static String getEmptyJson(String msg) {
        return String.format(ZRStrings.get(context, "default_json"), msg);
    }
}

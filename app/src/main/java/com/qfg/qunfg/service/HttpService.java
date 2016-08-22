package com.qfg.qunfg.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.qfg.qunfg.util.Utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rbtq on 8/18/16.
 */
public class HttpService {
    private final static String baseUrl = "http://52.197.199.166/ctu/v1";

    public static boolean isOnline(Context c) {
        ConnectivityManager connMgr = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static String get(String url) throws Exception {
        String content = "";
        URL httpUrl = new URL(baseUrl + url);
        // HttpURLConnection
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) httpUrl.openConnection();

            httpConn.connect();
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = httpConn.getInputStream();
                content = Utils.convertStreamToString(is);
                is.close();
            } else {
                throw new Exception(String.format("responseCode:%d,%s", httpConn.getResponseCode(), httpConn.getResponseMessage()));
            }
         } catch (Exception e) {
            throw e;
        } finally {
            //disconnect
            if (null != httpConn) {
                httpConn.disconnect();
            }
        }

        return content;
    }

    public static String post(String url, String param) throws Exception {
        String content = "";
        URL httpUrl = new URL(baseUrl + url);
        // HttpURLConnection
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection) httpUrl.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type", "application/json");
            OutputStream outStream = httpConn.getOutputStream();
            outStream.write(param.getBytes("UTF-8"));
            outStream.flush();
            outStream.close();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = httpConn.getInputStream();
                content = Utils.convertStreamToString(is);
                is.close();
            } else {
                throw new Exception(String.format("responseCode:%d,%s", httpConn.getResponseCode(), httpConn.getResponseMessage()));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            //disconnect
            if (null != httpConn) {
                httpConn.disconnect();
            }
        }
        return content;
    }
}

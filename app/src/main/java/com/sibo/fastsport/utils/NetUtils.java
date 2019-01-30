package com.sibo.fastsport.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/22.
 */
public class NetUtils {
    private static final int TIMEOUT = 5000;// 5秒

    public static String doGet(String urlStr) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                int len = 0;
                byte[] buf = new byte[1024];

                while ((len = is.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len, "UTF-8"));
                }

                is.close();
            }

        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static InputStream getInputStream(String url){
        InputStream inputStream = null;
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}

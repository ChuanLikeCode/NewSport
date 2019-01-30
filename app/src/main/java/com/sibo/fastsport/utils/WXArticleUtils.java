package com.sibo.fastsport.utils;

import com.sibo.fastsport.application.Constant;
import com.sibo.fastsport.domain.WXItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yyuand on 2016.11.28.
 */

public class WXArticleUtils {
    public static boolean isFirst = true;
    private List<WXItem> temp_wxItemList = new ArrayList<>();
    private String total_count;

    public List<WXItem> getArticle(int getNum,int offset){
        temp_wxItemList.clear();

        try {
            String getAccessToken = NetUtils.doGet(Constant.getAccessToken);
//            Log.e("NewsActivity", "token-------------" + getAccessToken);
            JSONObject object = new JSONObject(getAccessToken);
            String token = object.getString(Constant.ACCESSTOKEN);
            //String token = "SRt5sVtYijqKvB";
//            Log.e("NewsActivity", "token-------------" + token);
            MediaType MEDIA_TYPE_MARKDOWN
                    = MediaType.parse("text/x-markdown; charset=utf-8");
            String postBody = "{\n" +
                    "    \"type\":\"news\",\n" +
                    "    \"offset\":"+offset+",\n" +
                    "    \"count\":"+getNum+"\n" +
                    "}";
            //利用OkHttp来作为网络请求的框架，它的优点有很多
            // 1.Android6.0版本之后不支持httpclient，而他是封装的httpurlconnection
            //2.它支持https请求
            //3.非常高效，支持SPDY、连接池、GZIP和 HTTP 缓存。默认情况下，OKHttp会自动处理常见的网络问题，像二次连接、SSL的握手问题。
            // 如果你的应用程序中集成了OKHttp，Retrofit默认会使用OKHttp处理其他网络层请求。OkHttp是一个相对成熟的解决方案，
            // 据说Android4.4的源码中可以看到HttpURLConnection已经替换成OkHttp实现了。所以我们更有理由相信OkHttp的强大
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(Constant.getMaterial + token)
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                    .build();
            mOkHttpClient.connectTimeoutMillis();
            Response response = mOkHttpClient.newCall(request).execute();
            String responseResult = response.body().string();
//            Log.e("gaolei", "responseresult--------------MessageActivity------" + responseResult);
            JSONObject object1 = new JSONObject(responseResult);
            total_count = object1.getString("total_count");
//            Log.e("total_count", total_count);
            JSONArray jsonArray = object1.getJSONArray("item");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray news_item = jsonArray.getJSONObject(i).getJSONObject("content").getJSONArray("news_item");
                String url = news_item.getJSONObject(0).getString("url");
                String title = news_item.getJSONObject(0).getString("title");
                String author = news_item.getJSONObject(0).getString("author");
                String updateTime = jsonArray.getJSONObject(i).getString("update_time");
//                Log.e("url_img", updateTime);
                XmlParseUtils parseUtils = XmlParseUtils.getInstance();
                WXItem wxItem = new WXItem();
                wxItem.setAuthor(author);
                wxItem.setTitle(title);
                wxItem.setUpdateTime(updateTime);
                wxItem.setUrl(url);
                InputStream inputStream = NetUtils.getInputStream(url);
                List<String> url_img = parseUtils.xmlPraseWXJpg(inputStream);

                wxItem.setImg(url_img);
                temp_wxItemList.add(wxItem);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return temp_wxItemList;
    }
}

package com.sibo.fastsport.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by yyuand on 2016.11.28.
 */

public class XmlParseUtils {
    public static List<String> url_img = new ArrayList<>();
    private XmlPullParser pullParser;
    private int evenType;
    private static XmlParseUtils xmlParseUtils = null;
    private XmlParseUtils(){

    }
    public static XmlParseUtils getInstance(){
        if (xmlParseUtils == null){
            synchronized (XmlParseUtils.class){
                if (xmlParseUtils == null){
                    xmlParseUtils = new XmlParseUtils();
                    return xmlParseUtils;
                }
            }
        }
        return xmlParseUtils;
    }
    public List<String> xmlPraseWXJpg(InputStream input){
        url_img.clear();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            pullParser = factory.newPullParser();
            pullParser.setInput(input,"UTF-8");
            evenType = pullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        while (evenType!=XmlPullParser.END_DOCUMENT) {
            String nodeName = pullParser.getName();
           // Log.e("nodeName",nodeName+"");
            switch (evenType) {
                case XmlPullParser.START_DOCUMENT:
                    //url_img.clear();
                    break;
                case XmlPullParser.START_TAG:
                    if ("img".equals(nodeName)){
                        String src  = pullParser.getAttributeValue(null,"data-src");
                        if (src !=null){
                            url_img.add(src);
                        }
                        //Log.e("img",pullParser.getAttributeValue(null,"data-src")+"");
                    }
                    break;
                case XmlPullParser.END_TAG:

                    break;
            }
            try {
                evenType = pullParser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        return url_img;
    }
}

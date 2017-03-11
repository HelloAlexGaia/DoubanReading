package com.example.android.doubanreading;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张俊秋 on 2017/3/7.
 */

public class DoubanBookFetchr {
    private final String BOOKS="books";
    private final String RATING="rating";
    private final String AVERAGE="average";
    private final String AUTHORS="author";
    private final String PUBDATE="pubdate";
    private final String TITLE="title";
    private byte[] getUrlByte(String urlSpec) throws IOException {
        URL url=new URL(urlSpec);
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        try{
            InputStream inputStream=httpURLConnection.getInputStream();
            if (httpURLConnection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(httpURLConnection.getResponseMessage()+":with "+urlSpec);
            }
            int byteReader=0;
            byte[] buffer=new byte[1024];
            ByteArrayOutputStream out =new ByteArrayOutputStream();
            while ((byteReader=inputStream.read(buffer))>0){
                out.write(buffer,0,byteReader);
            }
            out.close();
            return out.toByteArray();
        }finally {
            httpURLConnection.disconnect();
        }
    }
    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlByte(urlSpec));
    }
    public List<BookInfo> fetchItems(String query){
        List<BookInfo> bookInfos=new ArrayList<>();
        try{
            String s= Uri.parse("https://api.douban.com/v2/book/search?")
                    .buildUpon()
                    .appendQueryParameter("q",query)
                    .build().toString();
            parseingJson(bookInfos,new JSONObject(getUrlString(s)));
        }catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return bookInfos;
    }
    private void parseingJson(List<BookInfo> bookInfos,JSONObject object) throws JSONException {
        JSONArray books=object.getJSONArray(BOOKS);
        for(int i=0;i<books.length();i++){
            JSONObject bookinfo=books.getJSONObject(i);
            String title=bookinfo.getString(TITLE);
            String pubdate=bookinfo.getString(PUBDATE);

            JSONObject rating=bookinfo.getJSONObject(RATING);
            String average=rating.getString(AVERAGE);

            JSONArray authors=bookinfo.getJSONArray(AUTHORS);
            String author="";
            for (int j=0;j<authors.length();j++){
                String s=authors.getString(j);
                if (j>1){
                    author+="等";
                    break;
                }
                author+=s;
            }
            bookInfos.add(new BookInfo(title,author,pubdate,average));
        }
    }
}

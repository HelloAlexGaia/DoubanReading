package com.example.android.doubanreading;

/**
 * Created by 张俊秋 on 2017/3/9.
 */

public class BookInfo {
    private String mTitle;
    private String mAuthors;
    private String mPubdate;
    private String mRating;
    public BookInfo(String title,String authors,String pubdate,String rating){
        mTitle=title;
        mAuthors=authors;
        mPubdate=pubdate;
        mRating=rating;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getPubdate() {
        return mPubdate;
    }

    public String getRating() {
        return mRating;
    }
}

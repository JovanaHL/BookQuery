package com.example.booklist;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class QueryBuilder {

    private static final String HOME = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String END  = "&maxResults=40";

    private Context mContext;

    public QueryBuilder(Context context){
        mContext = context;
    }
    public QueryBuilder(Context context, String apiKey){
        mContext = context;
    }

    // Make the query string apart of the new URL
    public String encodeUrl(String queryParameters){
        StringBuilder builder = new StringBuilder();

        builder.append(HOME);

        try{
            String encodedQuery = URLEncoder.encode(queryParameters, "UTF-8");
            builder.append(encodedQuery);
            builder.append(END);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return builder.toString();
    }
}

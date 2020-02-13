package com.example.booklist;

import android.content.Context;

import android.content.AsyncTaskLoader;

import java.util.List;


// Allows for getting all of the data in the background
public class BookLoader extends AsyncTaskLoader<List<Book>> {

    public static final String LOG_TAG = BookLoader.class.getSimpleName();

    private String mUrl;

    public BookLoader(Context context, String url){
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground(){

        if (mUrl== null){
            return null;
        }
        List<Book> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }
}

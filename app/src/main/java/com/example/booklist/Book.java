package com.example.booklist;

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mCategory;
    private String mDescription;
    private String mImageUrl;
    private String mUrl;

    public Book(String title, String author, String category, String description, String imageURL, String URL){
        mTitle = title;
        mAuthor = author;
        mCategory = category;
        mDescription = description;
        mImageUrl = imageURL;
        mUrl = URL;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getCategory(){
        return mCategory;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getUrl() {
        return mUrl;
    }
}

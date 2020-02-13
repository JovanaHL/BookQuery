package com.example.booklist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


// All the steps needed to connect to the network, get a JSON response and parse it
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils(){

    }

    public static List<Book> fetchBookData(String requestURL){

        URL url = createURL(requestURL);
        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream.", e);
        }

        List<Book> books = extractFeatureFromJSON(jsonResponse);

        return books;
    }

    private static URL createURL(String stringURL){
        URL url = null;

        try{
            url = new URL(stringURL);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFeatureFromJSON(String bookJSON){
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<Book> books = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for(int i = 0; i< booksArray.length(); i++){

                JSONObject thisBook = booksArray.getJSONObject(i);
                JSONObject volumeInfo = thisBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                JSONArray categoriesArray = volumeInfo.getJSONArray("categories");
                String author = authorsArray.get(0).toString();
                String category = categoriesArray.get(0).toString();
                String description = volumeInfo.getString("description");
                JSONObject imageLink = volumeInfo.getJSONObject("imageLinks");
                String imageURL = imageLink.getString("smallThumbnail");
                String URL = volumeInfo.getString("infoLink");

                Book book = new Book(title, author, category, description, imageURL, URL);
                books.add(book);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the book JSON results.", e);
        }
        return books;
    }
}

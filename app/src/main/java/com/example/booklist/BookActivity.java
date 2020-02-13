package com.example.booklist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER_ID = 1;

    // Original test API URL
    private static String API_URL =
            "https://www.googleapis.com/books/v1/volumes?q=men+without+women&maxResults=40";

    private static final String EXTRA_QUERY = "com.example.query";

    private BookAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private String mQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        // Get the API URL/query
       mQuery = getIntent().getStringExtra(EXTRA_QUERY);

       // if the query string not empty, start the loader
        if(!TextUtils.isEmpty(mQuery)){
          LoaderManager loaderManager = getLoaderManager();

           loaderManager.initLoader(BOOK_LOADER_ID, null, this);
       }

        SearchView search = (SearchView) findViewById(R.id.search_book);
        search.setSubmitButtonEnabled(true);
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create the adapter and set the adapter to display in listView
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        bookListView.setEmptyView(mEmptyStateTextView);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find current book that was clicked on
                Book thisBook = mAdapter.getItem(position);
                // Convert string URL into Uri object (to pass into Intent
                Uri bookUri = Uri.parse(thisBook.getUrl());
                // Create intent to view book Uri
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                // Send intent to launch activity
                startActivity(websiteIntent);
            }
        });



        // Connecting to network
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            mEmptyStateTextView.setText("No internet connection.");
        }



    }
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle){
        return new BookLoader(this, mQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books){

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        mEmptyStateTextView.setText("\t      No books found.\nPlease begin new Search.");
        mAdapter.clear();

        if(books != null && !books.isEmpty()){
            mAdapter.addAll(books);
        }

    }
    @Override
    public void onLoaderReset(Loader<List<Book>> loader){
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
    }

    // When searching for a book change the API link, do query
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        final SearchView searchView = (SearchView) findViewById(R.id.search_book);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
                mProgressBar.setVisibility(View.VISIBLE);
                QueryBuilder queryBuilder = new QueryBuilder(BookActivity.this);
                mQuery = queryBuilder.encodeUrl(query);
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}

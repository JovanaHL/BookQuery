package com.example.booklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    private List<Book> mBooks;

    // View that helps display in list
    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    public View getView( int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
        }

        Book thisBook = getItem(position);

        // Add the data to the listItem in the listView
        String title = thisBook.getTitle();
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
        titleTextView.setText(title);

        String category = thisBook.getCategory();
        TextView categoryTextView = (TextView) listItemView.findViewById(R.id.book_category);
        categoryTextView.setText(category);

        String author = thisBook.getAuthor();
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
        authorTextView.setText("By " + author);

        String description = thisBook.getDescription();
        TextView descriptTextView = (TextView) listItemView.findViewById(R.id.book_description);
        descriptTextView.setText(description);

        String imageURL = thisBook.getImageUrl();
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.book_image);
        Picasso.get().load(imageURL).into(imageView);

        return listItemView;


    }
    public void updateAdapterData(List<Book> data) {
        mBooks = data;
        notifyDataSetChanged();
    }




}

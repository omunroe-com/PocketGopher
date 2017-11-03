package com.gmail.afonsotrepa.pocketgopher;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.gmail.afonsotrepa.pocketgopher.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Bookmark> bookmarks;

        try  {
            bookmarks = Bookmark.read(this);
        }
        catch (Exception e) {
            e.printStackTrace();
            //display the error and return
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        //get the ListView to display
        ListView listView = findViewById(R.id.listView);

        //make an array holding the bookmarks (used to make the adapter)
        Bookmark[] bookmarksarray = new Bookmark[bookmarks.size()];
        bookmarksarray = bookmarks.toArray(bookmarksarray);

        //make the adapter
        BookmarkAdapter adapter = new BookmarkAdapter(
                this,
                R.layout.activity_listview,
                bookmarksarray);

        //apply it to listView
        listView.setAdapter(adapter);

        //make the items clickable (open the page/bookmark when clicked)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //the selected bookmark
                Bookmark bookmark = (Bookmark) parent.getItemAtPosition(position);

                //open the bookmarked page
                final Intent intent = new Intent(MainActivity.this, bookmark.activity);
                intent.putExtra(EXTRA_MESSAGE,
                        bookmark.selector+"\t"+bookmark.server+"\t"+bookmark.port.toString());
                startActivity(intent);
            }
        });

        //make the items "long clickable" (edit the bookmark when clicked)
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                Bookmark bookmark = (Bookmark) parent.getItemAtPosition(position);

                //setup the intent and then call it
                final Intent intent = new Intent(getApplication(), EditBookmarkActivity.class);
                intent.putExtra(EXTRA_MESSAGE,
                        bookmark.name+"\t"+
                                bookmark.type.toString()+"\t"+
                                bookmark.selector+"\t"+
                                bookmark.server+"\t"+
                                bookmark.port.toString());
                getApplication().startActivity(intent);

                return true;
            }
        });


        //configure the add bookmark button
        FloatingActionButton addBookmarkFloatingButton = findViewById(R.id.addBookmarkFloatingButton);
        addBookmarkFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplication(), EditBookmarkActivity.class);
                getApplication().startActivity(intent);
            }
        });
    }
}

package com.srivathsanvk.flickrnetwork;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Post> posts;
    ForumListAdapter adapter;
    String user;
    List<String> notesFromParse;
    String objectId;
    EditText dialogText;
    boolean delete;
    int pos;
    public final static String PHOTO_OBJECT_KEY = "photo_object_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        listView = (ListView) findViewById(R.id.forumListView);

        posts = new ArrayList<Post>();



    }

    @Override
    protected void onResume() {
        super.onResume();
        posts = null;
        posts = new ArrayList<Post>();
        getDataFromParse();

        Log.d("tag", "resume");

    }

    public void getDataFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                    for (ParseObject p : list) {
                        Post post = new Post();
                        post.setPhotoLink((String) p.get("photoLink"));
                        post.setUserFullName((String) p.get("userFullName"));

                        posts.add(post);

                    }
                    adapter = new ForumListAdapter(ForumActivity.this, R.layout.forum_row_item, posts);
                    adapter.setNotifyOnChange(true);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ForumActivity.this,DetailsActivity.class);
                            intent.putExtra(PHOTO_OBJECT_KEY, posts.get(position));
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(ForumActivity.this, "No data", Toast.LENGTH_LONG)
                            .show();
                    e.printStackTrace();
                }
            }

        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.newPost) {

            Intent intent = new Intent(ForumActivity.this, GalleryActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.logout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();

            Intent intent = new Intent(ForumActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.srivathsanvk.flickrnetwork;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    ImageView imageView;
    Post post;
    ListView listView;
    DetailsListAdapter adapter;
    ArrayList<Comment> comments;
    EditText dialogText;
    String objectId, userFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        post = (Post) getIntent().getExtras().getSerializable(ForumActivity.PHOTO_OBJECT_KEY);

        ParseUser user = ParseUser.getCurrentUser();

        userFullName = (String) user.get("firstName");


        imageView = (ImageView) findViewById(R.id.detailsImage);

        Picasso.with(DetailsActivity.this).load(post.getPhotoLink()).into(imageView);

        listView = (ListView) findViewById(R.id.detailsListView);

        comments = new ArrayList<Comment>();

        getCommentsFromParse();


        findViewById(R.id.addCommentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialog();
            }
        });



    }

    private void getCommentsFromParse() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("photoLink",post.getPhotoLink() );
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Toast.makeText(DetailsActivity.this, "No comments", Toast.LENGTH_SHORT)
                            .show();

                } else {
                    objectId = object.getObjectId();
                    List<String> commentsText = object.getList("comments");
                    List<String> users = object.getList("users");

                    for (int i = 0; i < commentsText.size(); i++) {
                        Comment comment = new Comment();
                        comment.setComment(commentsText.get(i));
                        comment.setPostedBy(users.get(i));
                        comments.add(comment);
                    }


                }

                adapter = new DetailsListAdapter(DetailsActivity.this, R.layout.details_row_item, comments);

                listView.setAdapter(adapter);

            }
        });

    }

    public void addItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
        final View layout = View.inflate(DetailsActivity.this, R.layout.custom_dialog,
                null);

        dialogText = ((EditText) layout.findViewById(R.id.dialogText));
        builder.setView(layout);

        builder.setTitle("Post New Comment")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if (dialogText != null
                                && dialogText.getText().toString().length() != 0) {

                            addComment();

                        } else {
                            Toast.makeText(DetailsActivity.this,
                                    "Please complete the comment",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub

                            }
                        });

        AlertDialog alert = builder.create();

        alert.show();
    }

    public void addComment() {
        Comment comment = new Comment();
        comment.setPostedBy(userFullName);
        comment.setComment(dialogText.getText().toString());
        comments.add(comment);
        adapter.notifyDataSetChanged();



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("photoLink", post.getPhotoLink());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    ParseObject postObject = new ParseObject("Comments");
                    postObject.put("photoLink", post.getPhotoLink());
                    postObject.add("comments", dialogText.getText().toString());
                    postObject.add("users", userFullName);
                    postObject.saveInBackground();
                } else {

                    object.add("comments", dialogText.getText().toString());
                    object.add("users", userFullName);
                    object.saveInBackground();
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

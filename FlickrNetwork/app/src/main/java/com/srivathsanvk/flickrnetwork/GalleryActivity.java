package com.srivathsanvk.flickrnetwork;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    EditText searchText;
    Button searchButton;
    EditText dialogText;
    ArrayList<Photo> photos;
    String userFullName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ParseUser user = ParseUser.getCurrentUser();
        userFullName = user.get("firstName") + " " + user.get("lastName");
        searchText = (EditText) findViewById(R.id.searchText);
        searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (searchText.getText().length() > 0) {


                    if (isConnectedOnline()) {
                        // TODO Auto-generated method stub
                        RequestParams params = new RequestParams("GET",
                                "https://api.flickr.com/services/rest/?");
                        params.addParams("method", "flickr.photos.search");
                        params.addParams("api_key",
                                "8516682144ce1c10d700f8d752d5100f");
                        params.addParams("text", searchText.getText()
                                .toString());
                        params.addParams("extras", "url_m");
                        params.addParams("per_page", "20");
                        params.addParams("format", "json");
                        params.addParams("nojsoncallback", "1");

                        new GetPhotosAsyncTask().execute(params);

                    } else {
                        Toast.makeText(GalleryActivity.this, "No network",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(GalleryActivity.this, "Please enter a search text", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
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

    private class GetPhotosAsyncTask extends AsyncTask<RequestParams, Void, ArrayList<Photo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GalleryActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressDialog.setMessage("Loading Photos. .");
            progressDialog.setCancelable(false);

            progressDialog.show();
        }

        @Override
        protected ArrayList<Photo> doInBackground(RequestParams... params) {
            URL url;
            try {

                HttpURLConnection con = params[0].setupConnection();

                con.connect();
                int statusCode = con.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = con.getInputStream();


                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = reader.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = reader.readLine();
                    }

                    return PhotoUtil.PhotosJSONParser
                            .parsePhotos(sb.toString());

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Photo> photos) {
            super.onPostExecute(photos);


            if (photos != null && photos.size() != 0) {

                GalleryActivity.this.photos = photos;

                GalleryAdapter adapter = new GalleryAdapter(GalleryActivity.this, R.layout.gallery_grid_item, photos);
                GridView gridView = (GridView) findViewById(R.id.gridview);

                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        createShareComment(position);
                    }
                });

            } else {
                Toast.makeText(GalleryActivity.this, "No results", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();

        }
    }

    private void createShareComment(int pos) {
        final int position = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
        final View layout = View.inflate(GalleryActivity.this, R.layout.gallery_custom_dialog,
                null);

        dialogText = ((EditText) layout.findViewById(R.id.dialogText));
        builder.setView(layout);


        builder.setTitle("Share Photo")
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        addPost(position);
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

    public void addPost(int pos) {

        Photo photo = photos.get(pos);
        ParseObject postObject = new ParseObject("Posts");
        postObject.put("photoLink", photos.get(pos).getPhotoUrl());
        postObject.put("userFullName", userFullName);
        postObject.saveInBackground();


    }


}

package com.srivathsanvk.flickrnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Srivathsan on 03-Sep-15.
 */
public class GalleryAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;
    private List<Photo> photos;


    public GalleryAdapter(Context c, int res, List<Photo> photos) {
        mContext = c;
        mResource = res;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            imageView = (ImageView) convertView.findViewById(R.id.galleryGridImage);


        } else {
            imageView = (ImageView) convertView;
        }

        Photo photo = photos.get(position);
        Picasso.with(mContext).load(photo.getPhotoUrl()).into(imageView);

        return imageView;

    }
}

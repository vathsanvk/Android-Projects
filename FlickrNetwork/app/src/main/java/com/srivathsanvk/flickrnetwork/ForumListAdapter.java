package com.srivathsanvk.flickrnetwork;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Srivathsan on 03-Sep-15.
 */
public class ForumListAdapter extends ArrayAdapter<Post> {

    List<Post> posts;
    Context mContext;
    int mResource;

    public ForumListAdapter(Context context, int resource, List<Post> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.posts = objects;
        this.mResource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.photoLink = (ImageView) convertView
                    .findViewById(R.id.forumImageView);
            holder.userFullName = (TextView) convertView.findViewById(R.id.forumTextView);

            convertView.setTag(holder);
        }



        Post post = posts.get(position);
        holder = (ViewHolder) convertView.getTag();



        Picasso.with(mContext).load(post.getPhotoLink())
                .into(holder.photoLink);


        holder.userFullName.setText("Posted By: " + post.getUserFullName());

        return convertView;
    }

    static class ViewHolder {
        ImageView photoLink;
        TextView userFullName;
    }
}

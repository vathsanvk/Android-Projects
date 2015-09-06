package com.srivathsanvk.flickrnetwork;

import android.content.Context;
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
public class DetailsListAdapter extends ArrayAdapter<Comment>{

    List<Comment> comments;
    Context mContext;
    int mResource;

    public DetailsListAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.comments = objects;
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

            holder.comment = (TextView) convertView.findViewById(R.id.commentText);
            holder.postedBy = (TextView) convertView.findViewById(R.id.postedBy);

            convertView.setTag(holder);
        }

        Comment comment = comments.get(position);
        holder = (ViewHolder) convertView.getTag();


        holder.comment.setText(comment.getComment());
        holder.postedBy.setText(comment.getPostedBy());

        return convertView;
    }

    static class ViewHolder {
        TextView comment;
        TextView postedBy;
    }
}

package com.example.nprclient;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class StoryAdapter extends ArrayAdapter<Story> {

	List<Story> stories;
	Context mContext;
	int mResource;

	public StoryAdapter(Context context, int resource, List<Story> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.stories = objects;
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
			holder.storyImageView = (ImageView) convertView
					.findViewById(R.id.storyImageView);
			holder.storyTitle = (TextView) convertView
					.findViewById(R.id.storyTitle);
			holder.storyPubDate = (TextView) convertView
					.findViewById(R.id.storyPubDate);
			holder.storyMinTeaser = (TextView) convertView
					.findViewById(R.id.storyMinTeaser);
			convertView.setTag(holder);

		}

		Story story = stories.get(position);

		holder = (ViewHolder) convertView.getTag();

		if (story.getThumbnail() != null) {
			Picasso.with(mContext).load(story.getThumbnail())
					.into(holder.storyImageView);
		} else {
			Picasso.with(mContext).load(R.drawable.photo_not_found)
			.into(holder.storyImageView);
		}

		holder.storyTitle.setText(story.getTitle());
		holder.storyPubDate.setText(story.getPubDate());
		holder.storyMinTeaser.setText(story.getMinTeaser());

		return convertView;
	}

	static class ViewHolder {
		ImageView storyImageView;
		TextView storyTitle;
		TextView storyPubDate;
		TextView storyMinTeaser;

	}

}

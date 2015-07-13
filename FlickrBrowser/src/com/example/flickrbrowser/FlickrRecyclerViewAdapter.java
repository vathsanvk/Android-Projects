package com.example.flickrbrowser;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

public class FlickrRecyclerViewAdapter extends
		RecyclerView.Adapter<FlickrImageViewHolder> {
	private List<Photo> photosList;
	private Context context;

	public FlickrRecyclerViewAdapter(Context context, List<Photo> photosList) {

		this.photosList = photosList;
		this.context = context;
	}

	@Override
	public int getItemCount() {

		return (null != photosList ? photosList.size() : 0);
	}

	@Override
	public void onBindViewHolder(FlickrImageViewHolder flickrImageViewHolder,
			int i) {
		Photo photoItem = photosList.get(i);
		Picasso.with(context).load(photoItem.getImage())
				.error(R.drawable.placeholder)
				.placeholder(R.drawable.placeholder)
				.into(flickrImageViewHolder.thumbnail);
		flickrImageViewHolder.title.setText(photoItem.getTitle());
	}

	@Override
	public FlickrImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.browse, null);
		FlickrImageViewHolder flickImageViewHolder = new FlickrImageViewHolder(
				view);

		return flickImageViewHolder;
	}

}

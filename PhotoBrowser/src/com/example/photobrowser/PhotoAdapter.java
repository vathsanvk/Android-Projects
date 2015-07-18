package com.example.photobrowser;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoAdapter extends ArrayAdapter<Photo> {

	List<Photo> photos;
	Context mContext;
	int mResource;

	public PhotoAdapter(Context context, int resource, List<Photo> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.photos = objects;
		this.mResource = resource;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mResource, parent,false);
		}
		
		Photo photo = photos.get(position);
		ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.itemImage);
	    //thumbnailImage.setIma()
		
		Picasso.with(mContext).load(photo.getUrl()).into(thumbnailImage);
		
		TextView photoTitle = (TextView) convertView.findViewById(R.id.itemTitle);
				
		photoTitle.setText(photo.getTitle());
	
		//ImageView itemFav = (ImageView) convertView.findViewById(R.id.itemFav);
		
		//itemFav.setImageResource(R.drawable.favorites_grey);
		
		
		return convertView;
	}

}

package com.example.topiosapps;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AppAdapter extends ArrayAdapter<App> {

	List<App> apps;
	Context mContext;
	int mResource;

	public AppAdapter(Context context, int resource, List<App> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.apps = objects;
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
			holder.appImageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			holder.appPrice = (TextView) convertView.findViewById(R.id.price);
			holder.appTitle = (TextView) convertView.findViewById(R.id.title);
			holder.appRlsDate = (TextView) convertView
					.findViewById(R.id.rlsDate);
			holder.appDevName = (TextView) convertView
					.findViewById(R.id.devName);
			convertView.setTag(holder);
		}

		App app = apps.get(position);
		holder = (ViewHolder) convertView.getTag();

		if (app.getSmallPhotoUrl() != null) {
			Picasso.with(mContext).load(app.getSmallPhotoUrl())
					.into(holder.appImageView);
		} else {
			Picasso.with(mContext).load(R.drawable.photo_not_found)
					.into(holder.appImageView);
		}

		holder.appTitle.setText(app.getTitle());
		holder.appRlsDate.setText(app.getRlsDate());

		if (app.getPrice().equals("0.00000")) {
			holder.appPrice.setText("Free");
		} else {
			holder.appPrice.setText(app.getPrice());
		}
		holder.appDevName.setText(app.getDevName());

		return convertView;
	}

	static class ViewHolder {
		ImageView appImageView;
		TextView appTitle;
		TextView appRlsDate;
		TextView appPrice;
		TextView appDevName;

	}

}

package com.example.todofragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.example.todofragments.ToDoFragment.onToDoFragmentChangeListener;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailsFragment extends Fragment {
	public onItemDetailsFragmentChangeListener mListener;
	TextView itemName;
	String user, notes;
	int pos;
	String objectId;
	List<String> list;

	public ItemDetailsFragment(String user, String notes, int pos,
			List<String> notesList, String objId) {
		this.notes = notes;
		this.user = user;
		this.pos = pos;
		this.list = notesList;
		this.objectId = objId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_details, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		itemName = (TextView) getActivity().findViewById(R.id.itemName);
		itemName.setText(notes);

		getActivity().findViewById(R.id.deleteButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						deleteItem();

					}
				});

	}

	public void deleteItem() {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");

		// Retrieve the object by id
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject obj, ParseException e) {
				if (e == null) {

					obj.remove("Notes");
					list.remove(pos);
					obj.put("Notes", list);
					obj.saveInBackground();
					getActivity().getFragmentManager().popBackStack();
				} else {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (onItemDetailsFragmentChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "should implement OnToDoFragmentChangeListener");
		}

	}

	public interface onItemDetailsFragmentChangeListener {
		void gotoToDo(String user, boolean delete, int pos);

	}
}

package com.example.todofragments;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ToDoFragment extends Fragment {

	public onToDoFragmentChangeListener mListener;
	ListView listView;
	LinkedList<String> notes;
	ArrayAdapter<String> adapter;
	String user;
	List<String> notesFromParse;
	String objectId;
	EditText dialogText;
	boolean delete;
	int pos;

	public ToDoFragment(String email, boolean delete, int pos) {
		this.user = email;
		this.delete = delete;
		this.pos = pos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.fragment_todo, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			mListener = (onToDoFragmentChangeListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "should implement OnToDoFragmentChangeListener");
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		listView = (ListView) getActivity().findViewById(R.id.listView);

		notes = new LinkedList<String>();

		if (user.length() != 0) {

			getDataFromParse();

			adapter = new ArrayAdapter<>(getActivity(),
					android.R.layout.simple_list_item_1, notes);

			adapter.setNotifyOnChange(true);
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					mListener.gotoItemDetails(user, notes.get(position),
							position, notes,objectId);

				}

			});
		}

	}

	public void getDataFromParse() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");
		query.whereEqualTo("Email", user);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (object == null) {
					Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				} else {
					notesFromParse = object.getList("Notes");
					objectId = object.getObjectId();
					if (notesFromParse != null && notesFromParse.size() != 0) {
						for (String s : notesFromParse) {
							notes.add(s);
							adapter.notifyDataSetChanged();
						}
					}

				}
			}
		});

	}

	public void addItemDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final View layout = View.inflate(getActivity(), R.layout.custom_dialog,
				null);

		dialogText = ((EditText) layout.findViewById(R.id.dialogText));
		builder.setView(layout);

		builder.setTitle("Add an item")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if (dialogText != null
								&& dialogText.getText().toString().length() != 0) {

							updateItemList();

						} else {
							Toast.makeText(getActivity(),
									"Please complete the text",
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

	public void updateItemList() {
		notes.add(dialogText.getText().toString());
		adapter.notifyDataSetChanged();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ToDo");

		// Retrieve the object by id
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject obj, ParseException e) {
				if (e == null) {

					obj.add("Notes", dialogText.getText().toString());
					obj.saveInBackground();
				} else {
					e.printStackTrace();
				}
			}
		});

	}

	public interface onToDoFragmentChangeListener {

		void gotoItemDetails(String email, String notes, int pos, List<String> notesList, String objId);
	}
}

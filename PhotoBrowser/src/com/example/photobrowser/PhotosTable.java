package com.example.photobrowser;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PhotosTable {
	static final String TABLENAME = "photo";
	static final String COLUMN_PHOTO_ID = "photoID";
	static final String COLUMN_PHOTO_NAME = "photoName";
	static final String COLUMN_PHOTO_URL = "photoURL";
	static final String COLUMN_OWNER_NAME = "ownerName";

	static public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABLENAME + " (");
		sb.append(COLUMN_PHOTO_ID + " integer primary key, ");
		sb.append(COLUMN_PHOTO_NAME + " text not null, ");
		sb.append(COLUMN_PHOTO_URL + " text not null, ");
		sb.append(COLUMN_OWNER_NAME + " text not null); ");

		try {
			db.execSQL(sb.toString());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	static public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
		PhotosTable.onCreate(db);
	}

}

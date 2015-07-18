package com.example.photobrowser;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PhotoDAO {
	private SQLiteDatabase db;

	public PhotoDAO(SQLiteDatabase db) {
		this.db = db;
	}

	public long save(Photo photo) {

		ContentValues values = new ContentValues();
		values.put(PhotosTable.COLUMN_PHOTO_ID, photo.get_id());
		values.put(PhotosTable.COLUMN_PHOTO_NAME, photo.getTitle());
		values.put(PhotosTable.COLUMN_PHOTO_URL, photo.getUrl());
		values.put(PhotosTable.COLUMN_OWNER_NAME, photo.getName());

		return db.insert(PhotosTable.TABLENAME, null, values);

	}

	public boolean update(Photo photo) {
		ContentValues values = new ContentValues();

		values.put(PhotosTable.COLUMN_PHOTO_NAME, photo.getName());
		values.put(PhotosTable.COLUMN_PHOTO_URL, photo.getUrl());
		values.put(PhotosTable.COLUMN_OWNER_NAME, photo.getName());
		return db.update(PhotosTable.TABLENAME, values,
				PhotosTable.COLUMN_PHOTO_ID + "=?",
				new String[] { photo.get_id() + "" }) > 0;
	}

	public boolean delete(Photo photo) {
		return db.delete(PhotosTable.TABLENAME, PhotosTable.COLUMN_PHOTO_ID
				+ "=?", new String[] { photo.get_id() + "" }) > 0;
	}

	public Photo get(long id) {

		Photo photo = null;

		Cursor c = db.query(true, PhotosTable.TABLENAME, new String[] {
				PhotosTable.COLUMN_PHOTO_ID, PhotosTable.COLUMN_PHOTO_NAME,
				PhotosTable.COLUMN_PHOTO_URL, PhotosTable.COLUMN_OWNER_NAME },
				PhotosTable.COLUMN_PHOTO_ID + "=?", new String[] { id + "" },
				null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			photo = buildPhotoFromCursor(c);
			if (!c.isClosed()) {
				c.close();
			}
		}

		return photo;
	}

	public List<Photo> getAll() {

		List<Photo> photos = new ArrayList<Photo>();

		Cursor c = db.query(PhotosTable.TABLENAME, new String[] {
				PhotosTable.COLUMN_PHOTO_ID, PhotosTable.COLUMN_PHOTO_NAME,
				PhotosTable.COLUMN_PHOTO_URL, PhotosTable.COLUMN_OWNER_NAME },
				null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			do {
				Photo photo = buildPhotoFromCursor(c);
				if (photo != null) {
					photos.add(photo);
				}

			} while (c.moveToNext());

			if (!c.isClosed()) {
				c.close();
			}
		}

		return photos;
	}

	private Photo buildPhotoFromCursor(Cursor c) {
		Photo photo = null;
		if (c != null) {
			photo = new Photo();
			photo.set_id(c.getInt(0));
			photo.setTitle(c.getString(1));
			photo.setUrl(c.getString(2));
			photo.setName(c.getString(3));
		}

		return photo;
	}

}

package com.example.photobrowser;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDataManager {
	private Context mContext;
	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private PhotoDAO photoDAO;

	public DatabaseDataManager(Context mContext) {
		this.mContext = mContext;
		dbOpenHelper = new DatabaseOpenHelper(this.mContext);
		db = dbOpenHelper.getWritableDatabase();
		photoDAO = new PhotoDAO(db);
	}
	
	public void close(){
		if(db != null){
			db.close();
		}
	}

	public PhotoDAO getPhotoDAO() {
		return this.photoDAO;
	}

	public long savePhoto(Photo photo) {
		return this.photoDAO.save(photo);
	}

	public boolean updatePhoto(Photo photo) {
		return this.photoDAO.update(photo);
	}

	public boolean deletePhoto(Photo photo) {
		return this.photoDAO.delete(photo);
	}

	public Photo getPhoto(long id) {
		return this.photoDAO.get(id);
	}

	public List<Photo> getAllPhotos() {
		return this.photoDAO.getAll();
	}

}

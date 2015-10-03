package com.davidguerrero.asteroids;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "puntuaciones";

	// Puntuaciones table name
	private static final String TABLE_PUNTUACIONES = "puntos";

	// Puntuaciones Table Columns names

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_RECORD = "record";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PUNTUACIONES_TABLE = "CREATE TABLE " + TABLE_PUNTUACIONES
				+ "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_NAME + " TEXT," + KEY_RECORD + " TEXT" + ")";
		db.execSQL(CREATE_PUNTUACIONES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUNTUACIONES);

		// Create tables again
		onCreate(db);
	}

	// Adding new contact
	public void addPuntuacion(Puntuacion puntuacion) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, puntuacion.getNombre());
		values.put(KEY_RECORD, puntuacion.getPuntos());

		// Inserting Row
		db.insert(TABLE_PUNTUACIONES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	public Puntuacion getPuntuacion(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PUNTUACIONES, new String[] { KEY_NAME,
				KEY_RECORD }, KEY_NAME + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Puntuacion puntuacion = new Puntuacion(cursor.getString(0),
				cursor.getString(1));
		// return contact
		return puntuacion;
	}

	// Getting All Contacts
	public List<Puntuacion> getAllContacts() {
		List<Puntuacion> puntuacionesList = new ArrayList<Puntuacion>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PUNTUACIONES + " order by " + KEY_RECORD + " *1 DESC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Puntuacion puntuacion = new Puntuacion();
				puntuacion.setPuntos(cursor.getString(2));
				puntuacion.setNombre(cursor.getString(1));
				// Adding contact to list
				puntuacionesList.add(puntuacion);
			} while (cursor.moveToNext());
		}

		// return contact list
		return puntuacionesList;
	}

	// Getting contacts Count
	public int getPuntuacionesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PUNTUACIONES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public int updatePuntuacion(Puntuacion puntuacion) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, puntuacion.getNombre());
		values.put(KEY_RECORD, puntuacion.getPuntos());

		// updating row
		return db.update(TABLE_PUNTUACIONES, values, KEY_NAME + " = ?",
				new String[] { String.valueOf(puntuacion.getNombre()) });
	}

	// Deleting single contact
	public void deletePuntuacion(Puntuacion puntuacion) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PUNTUACIONES, KEY_NAME + " = ?",
				new String[] { String.valueOf(puntuacion.getNombre()) });
		db.close();
	}

}

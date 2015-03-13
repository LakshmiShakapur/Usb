package com.example.tab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Readadapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_Key = "key";

	private static final String DATABASE_TABLE = "Read_table";
	private Context context;
	private SQLiteDatabase database;
	private Readhelper ret_dbhelp;

	public Readadapter(Context context) {
		this.context = context;
	}

	public Readadapter open() throws SQLException {
		ret_dbhelp = new Readhelper(context);
		database = ret_dbhelp.getWritableDatabase();
		return this;
	}

	public void close() {
		ret_dbhelp.close();
	}

	/** inserts values into database */
	public long insertData(String key) {
		ContentValues initialValues = createContentValues(key);
		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	public Cursor fetch_retdb(long rowId) throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_Key }, KEY_ROWID + "=" + rowId, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean updateDataUser(int _id, String key) {
		try {
			database = ret_dbhelp.getWritableDatabase();
			String query = "UPDATE " + DATABASE_TABLE + " SET " + KEY_Key
					+ " = '" + key + "' WHERE " + KEY_ROWID + " =" + _id + ";";
			Log.i("query", query);
			database.execSQL(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private ContentValues createContentValues(String key) {
		// TODO Auto-generated method stub

		ContentValues values = new ContentValues();

		values.put(KEY_Key, key);
		return values;
	}

}

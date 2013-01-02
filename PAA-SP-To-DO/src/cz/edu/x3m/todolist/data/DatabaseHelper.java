package cz.edu.x3m.todolist.data;


import cz.edu.x3m.todolist.data.sql.SQLColumn;
import cz.edu.x3m.todolist.data.sql.SQLTable;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {


	public static final String DATABASE_NAME = "notepad";
	public static final int DATABASE_VERSION = 4;
	private boolean firstRun = false;

	public static final SQLTable TABLE_GROUPS = new SQLTable ("groups", new SQLColumn ("_id",
				"INTEGER", true), new SQLColumn ("title", "TEXT", false, true), new SQLColumn (
				"description", "TEXT", false, false));

	public static final SQLTable TABLE_TASKS = new SQLTable ("tasks", new SQLColumn ("_id",
				"INTEGER", true), new SQLColumn ("gid", "INTEGER", false, true), new SQLColumn (
				"title", "TEXT", false, true), new SQLColumn ("description", "TEXT", false, false),
				new SQLColumn ("status", "INTEGER", false, true));


	public DatabaseHelper (Context context) {
		super (context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	public static String printCursor (Cursor c) {
		StringBuilder result = new StringBuilder ("Data: \n");
		int size = c.getCount ();
		String [] names = c.getColumnNames ();
		for (int i = 0; i < size; i++) {
			c.moveToNext ();

			for (int j = 0; j < names.length; j++) {
				result.append (names[j]);
				result.append (": ");
				result.append (c.getString (j));
				result.append (", ");
			}
			result.append ("\n");
		}
		return result.toString ();
	}


	public static Group [] toGroupArray (Cursor c) {
		Group [] result = new Group [c.getCount ()];
		for (int i = 0, l = result.length; i < l; i++) {
			c.moveToNext ();
			result[i] = new Group (c.getLong (0), c.getString (1), c.getString (2));
		}
		return result;
	}


	public static Task [] toTaskArray (Cursor c) {
		Task [] result = new Task [c.getCount ()];
		for (int i = 0, l = result.length; i < l; i++) {
			c.moveToNext ();
			result[i] = new Task (c.getLong (0), c.getLong (1), c.getString (2), c.getString (3),
						c.getLong (4));
		}
		return result;
	}


	public static Group toGroup (Cursor c) {
		if (c.getCount () == 0)
			return null;

		c.moveToNext ();
		return new Group (c.getLong (0), c.getString (1), c.getString (2));
	}


	public static Task toTask (Cursor c) {
		if (c.getCount () == 0)
			return null;

		c.moveToNext ();
		return new Task (c.getLong (0), c.getLong (1), c.getString (2), c.getString (3),
					c.getLong (4));
	}


	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL (TABLE_GROUPS.createTable ());
		db.execSQL (TABLE_TASKS.createTable ());

		/*Toast.makeText (ToDoListActivity.instance,
						TABLE_GROUPS.createTable () + "\n\n\n" + TABLE_TASKS.createTable (),
						Toast.LENGTH_LONG).show ();
		Toast.makeText (ToDoListActivity.instance,
						TABLE_GROUPS.createTable () + "\n\n\n" + TABLE_TASKS.createTable (),
						Toast.LENGTH_LONG).show ();*/
		firstRun = true;
	}


	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL (TABLE_GROUPS.deleteTable ());
		db.execSQL (TABLE_TASKS.deleteTable ());
		onCreate (db);
	}


	public boolean isFirstRun () {
		return firstRun;
	}


	public void setFirstRun (boolean firstRun) {
		this.firstRun = firstRun;
	}


}

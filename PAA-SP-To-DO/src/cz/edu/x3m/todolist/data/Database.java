package cz.edu.x3m.todolist.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Database {

	public static final String BUNDLE_ID = "bundleID";

	// #
	public final DatabaseHelper openHelper;
	private SQLiteDatabase database;

	public Database(DatabaseHelper databaseHelper, DatabaseListener listener) {
		this.openHelper = databaseHelper;
		this.listener = listener;
	}

	private final DatabaseListener listener;
	private boolean fireEvents = true;

	/*
	 * --------------------------------------------------------------------------
	 * ------SELECT--------------------------------------------------------------
	 * --------------------------------------------------------------------------
	 */

	public Group[] getGroups() {
		return DatabaseHelper.toGroupArray(openHelper.getReadableDatabase().query(DatabaseHelper.TABLE_GROUPS.NAME,
				DatabaseHelper.TABLE_GROUPS.COLUMNS_NAMES, null, null, null, null, null));
	}

	public Task[] getTasks(long gid) {
		return DatabaseHelper.toTaskArray(openHelper.getReadableDatabase().query(DatabaseHelper.TABLE_TASKS.NAME,
				DatabaseHelper.TABLE_TASKS.COLUMNS_NAMES, "gid = ?", new String[] { gid + "" }, null, null, null));
	}

	public Task getTask(long id) {
		return DatabaseHelper.toTask(openHelper.getReadableDatabase().query(DatabaseHelper.TABLE_TASKS.NAME,
				DatabaseHelper.TABLE_TASKS.COLUMNS_NAMES, "_id = ?", new String[] { String.valueOf(id) }, null, null,
				null));
	}

	public Group getGroup(long id) {
		return DatabaseHelper.toGroup(openHelper.getReadableDatabase().query(DatabaseHelper.TABLE_GROUPS.NAME,
				DatabaseHelper.TABLE_GROUPS.COLUMNS_NAMES, "_id = ?", new String[] { String.valueOf(id) }, null, null,
				null));
	}

	/*
	 * --------------------------------------------------------------------------
	 * ------INSERT--------------------------------------------------------------
	 * --------------------------------------------------------------------------
	 */

	public long insertGroup(Group group) {
		database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", group.title);
		values.put("description", group.description);
		long id = database.insert(DatabaseHelper.TABLE_GROUPS.NAME, null, values);
		database.close();
		onGroupListChanged();
		return id;
	}

	public long insertTask(Task task) {
		database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", task.title);
		values.put("description", task.description);
		values.put("status", task.status);
		values.put("gid", task.gid);
		long id = database.insertWithOnConflict(DatabaseHelper.TABLE_TASKS.NAME, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		database.close();
		onTaskListChanged();
		return id;
	}

	public void insertDefault() {

		long id;
		fireEvents = false;

		id = insertGroup(new Group("Hlavní skupina", "Ty nejdùležitìjší úkoly"));
		insertTask(new Task(id, "Váš první úkol!", "V každé skupinì mùže být mnoho úkolù. Kliknutím na tento text, oznaèíte úkol za splnìný"));
		insertTask(new Task(id, "Váš druhý úkol!", "Smazání nebo jinou úpravu úkolu, provedete dlouhým kliknutím"));
		
		
		id = insertGroup(new Group("Prázdná skupina", "Nic moc"));
		// # nothing

		id = insertGroup(new Group("Další­ skupina", ""));
		insertTask(new Task(id, "První úkol", "Popis prvního úkolu"));
		insertTask(new Task(id, "Druhý úkol", "Popis druhéhoúkolu"));
		insertTask(new Task(id, "Tøetí splnìný úkol", "Už je hotov", 1));
		fireEvents = true;
	}

	/*
	 * --------------------------------------------------------------------------
	 * ------DELETE--------------------------------------------------------------
	 * --------------------------------------------------------------------------
	 */

	public int deleteGroup(long id) {
		database = openHelper.getWritableDatabase();
		int del = database.delete(DatabaseHelper.TABLE_GROUPS.NAME, "_id = ?", new String[] { String.valueOf(id) });
		database.delete(DatabaseHelper.TABLE_TASKS.NAME, "gid = ?", new String[] { String.valueOf(id) });

		database.close();
		onGroupListChanged();
		return del;
	}

	public int deleteGroups() {
		database = openHelper.getWritableDatabase();
		int del = database.delete(DatabaseHelper.TABLE_GROUPS.NAME, null, null);
		database.delete(DatabaseHelper.TABLE_TASKS.NAME, null, null);

		database.close();
		onGroupListChanged();
		return del;
	}

	public int deleteTask(long id) {
		database = openHelper.getWritableDatabase();
		int del = database.delete(DatabaseHelper.TABLE_TASKS.NAME, "_id = ?", new String[] { String.valueOf(id) });

		database.close();
		onTaskListChanged();
		return del;
	}

	public int deleteTasks(long id) {
		database = openHelper.getWritableDatabase();
		int del = database.delete(DatabaseHelper.TABLE_TASKS.NAME, "gid = ?", new String[] { String.valueOf(id) });

		database.close();
		onTaskListChanged();
		return del;
	}

	/*
	 * --------------------------------------------------------------------------
	 * ------EDIT----------------------------------------------------------------
	 * --------------------------------------------------------------------------
	 */

	public int editGroup(long id, Group group) {
		database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", group.title);
		values.put("description", group.description);
		int affected = database.update(DatabaseHelper.TABLE_GROUPS.NAME, values, "_id = ?",
				new String[] { String.valueOf(id) });
		database.close();
		onGroupItemChanged(group);
		return affected;
	}

	public int editTask(long id, Task task) {
		database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", task.title);
		values.put("description", task.description);
		values.put("gid", task.gid);
		values.put("status", task.status);
		int affected = database.update(DatabaseHelper.TABLE_TASKS.NAME, values, "_id = ?",
				new String[] { String.valueOf(id) });
		database.close();
		onTaskListChanged();
		return affected;
	}

	public int switchTaskStatus(long id) {
		Task task = getTask(id);
		System.out.println(task.info());
		database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("status", task.status == 0 ? 1 : 0);
		int affected = database.update(DatabaseHelper.TABLE_TASKS.NAME, values, "_id = ?",
				new String[] { String.valueOf(id) });
		database.close();
		onTaskItemChanged(task);
		return affected;
	}

	public int setTaskStatus(long id, long status) {
		database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("status", status);
		int affected = database.update(DatabaseHelper.TABLE_TASKS.NAME, values, "_id = ?",
				new String[] { String.valueOf(id) });
		database.close();
		onTaskItemChanged(null);
		return affected;
	}

	private void onTaskItemChanged(Task task) {
		if (!fireEvents || listener == null)
			return;
		listener.onTaskItemChanged(task);
	}

	private void onTaskListChanged() {
		if (!fireEvents || listener == null)
			return;
		listener.onTaskListChanged();
	}

	private void onGroupItemChanged(Group group) {
		if (!fireEvents || listener == null)
			return;
		listener.onGroupItemChanged(group);
	}

	private void onGroupListChanged() {
		if (!fireEvents || listener == null)
			return;
		listener.onGroupListChanged();
	}

}

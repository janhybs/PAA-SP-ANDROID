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

	/**
	 * @return array of all groups or empty array
	 */
	public Group[] getGroups() {
		return DatabaseHelper.toGroupArray(openHelper.getReadableDatabase().query(DatabaseHelper.TABLE_GROUPS.NAME,
				DatabaseHelper.TABLE_GROUPS.COLUMNS_NAMES, null, null, null, null, null));
	}

	/**
	 * @return all tasks in specific group
	 */
	public Task[] getTasks(long gid) {
		return DatabaseHelper.toTaskArray(openHelper.getReadableDatabase().query(DatabaseHelper.TABLE_TASKS.NAME,
				DatabaseHelper.TABLE_TASKS.COLUMNS_NAMES, "gid = ?", new String[] { gid + "" }, null, null, null));
	}

	/**
	 * @return certain task specified by id
	 */
	public Task getTask(long id) {
		return DatabaseHelper.toTask(openHelper.getReadableDatabase().query(DatabaseHelper.TABLE_TASKS.NAME,
				DatabaseHelper.TABLE_TASKS.COLUMNS_NAMES, "_id = ?", new String[] { String.valueOf(id) }, null, null,
				null));
	}

	/**
	 * @return certain group specified by id
	 */
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

	/**
	 * @return insert id (-1 on error)
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

	/**
	 * Insert task (may overwrite)
	 * 
	 * @return insert id
	 */
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

	/**
	 * Insert default sequence (groups and tasks)
	 */
	public void insertDefault() {

		long id;
		fireEvents = false;

		id = insertGroup(new Group("Hlavn� skupina", "Ty nejd�le�it�j�� �koly"));
		insertTask(new Task(id, "V� prvn� �kol!",
				"V ka�d� skupin� m��e b�t mnoho �kol�. Kliknut�m na tento text, ozna��te �kol za spln�n�"));
		insertTask(new Task(id, "V� druh� �kol!", "Smaz�n� nebo jinou �pravu �kolu, provedete dlouh�m kliknut�m"));

		id = insertGroup(new Group("Pr�zdn� skupina", "Nic moc"));
		// # nothing

		id = insertGroup(new Group("Dal�� skupina", ""));
		insertTask(new Task(id, "Prvn� �kol", "Popis prvn�ho �kolu"));
		insertTask(new Task(id, "Druh� �kol", "Popis druh�ho�kolu"));
		insertTask(new Task(id, "T�et� spln�n� �kol", "U� je hotov", 1));
		fireEvents = true;
	}

	/*
	 * --------------------------------------------------------------------------
	 * ------DELETE--------------------------------------------------------------
	 * --------------------------------------------------------------------------
	 */

	/**
	 * @return deleted group count (0|1)
	 */
	public int deleteGroup(long id) {
		database = openHelper.getWritableDatabase();
		int del = database.delete(DatabaseHelper.TABLE_GROUPS.NAME, "_id = ?", new String[] { String.valueOf(id) });
		database.delete(DatabaseHelper.TABLE_TASKS.NAME, "gid = ?", new String[] { String.valueOf(id) });

		database.close();
		onGroupListChanged();
		return del;
	}

	/**
	 * @return group count
	 */
	public int deleteGroups() {
		database = openHelper.getWritableDatabase();
		int del = database.delete(DatabaseHelper.TABLE_GROUPS.NAME, null, null);
		database.delete(DatabaseHelper.TABLE_TASKS.NAME, null, null);

		database.close();
		onGroupListChanged();
		return del;
	}

	/**
	 * @return deleted task count
	 */
	public int deleteTask(long id) {
		database = openHelper.getWritableDatabase();
		int del = database.delete(DatabaseHelper.TABLE_TASKS.NAME, "_id = ?", new String[] { String.valueOf(id) });

		database.close();
		onTaskListChanged();
		return del;
	}

	/**
	 * Delete all task from specific group
	 * 
	 * @return deleted task count
	 */
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

	/**
	 * @return affected rows (0|1)
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

	/**
	 * @return affected rows (0|1)
	 */
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

	/**
	 * @return affected rows (0|1), should be always 1
	 */
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

	/**
	 * @return affected rows (0|1)
	 */
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

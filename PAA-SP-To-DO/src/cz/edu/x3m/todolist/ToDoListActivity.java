package cz.edu.x3m.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import cz.edu.x3m.todolist.adapters.GroupAdapter;
import cz.edu.x3m.todolist.adapters.TaskAdapter;
import cz.edu.x3m.todolist.comparators.TaskComparator;
import cz.edu.x3m.todolist.data.Database;
import cz.edu.x3m.todolist.data.DatabaseHelper;
import cz.edu.x3m.todolist.data.DatabaseListener;
import cz.edu.x3m.todolist.data.Group;
import cz.edu.x3m.todolist.data.Task;
import cz.edu.x3m.todolist.dialogs.GroupSettings;
import cz.edu.x3m.todolist.dialogs.TaskSettings;
import cz.edu.x3m.todolist.listeners.OnGestureDetectorListener;
import cz.edu.x3m.todolist.manipulation.AddGroupActivity;
import cz.edu.x3m.todolist.manipulation.AddTaskActivity;
import cz.edu.x3m.todolist.utils.InfoActivity;

public class ToDoListActivity extends Activity implements DatabaseListener {

	public static ToDoListActivity instance;

	public ToDoListActivity() {
		ToDoListActivity.instance = this;
	}

	public Group selectedGroup;
	public Database database;

	public GroupAdapter groupAdapter;
	public TaskAdapter taskAdapter;

	private Spinner groupSpinner;
	private ListView taskListView;

	private GroupListener groupListener;
	private TaskListener taskListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		database = new Database(new DatabaseHelper(this), this);

		showGroups();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_layout, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item == null ? featureId : item.getItemId()) {
		case R.id.menu_add_group:
			Intent addGroupIntent = new Intent(this, AddGroupActivity.class);
			startActivity(addGroupIntent);
			break;

		case R.id.menu_add_task:
			Intent addTaskIntent = new Intent(this, AddTaskActivity.class);
			addTaskIntent.putExtra(Database.BUNDLE_ID, selectedGroup == null ? -1 : selectedGroup.id);
			startActivity(addTaskIntent);
			break;

		case R.id.menu_about:
			Intent infoIntent = new Intent(this, InfoActivity.class);
			startActivity(infoIntent);
			break;

		}
		return super.onMenuItemSelected(featureId, item);
	}

	private Group[] getGroups() {
		Group[] groups = database.getGroups();
		if (groups.length == 0) {
			database.insertDefault();
			groups = database.getGroups();
		}
		return groups;
	}

	private void showGroups() {
		groupSpinner = (Spinner) findViewById(R.id.group_spinner);
		groupAdapter = new GroupAdapter(this, R.id.titleTV, getGroups());
		groupListener = new GroupListener();

		groupSpinner.setPromptId(R.string.choose_group);
		groupSpinner.setAdapter(groupAdapter);

		groupSpinner.setOnItemSelectedListener(groupListener);
		groupSpinner.setOnLongClickListener(groupListener);
	}

	private void showTasks(Group group) {
		taskListView = (ListView) findViewById(R.id.task_list);
		taskAdapter = new TaskAdapter(this, database.getTasks(group.id));
		taskAdapter.sort(new TaskComparator());
		taskListener = new TaskListener();

		taskListView.setAdapter(taskAdapter);
		taskListView.setOnItemClickListener(taskListener);
		taskListView.setOnItemLongClickListener(taskListener);
		taskListView.setEmptyView(findViewById(R.id.empty));

		final GestureDetector detector = new GestureDetector(this, new OnGestureDetectorListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				Task task = taskAdapter.getItem(taskListView.pointToPosition(Math.round(e1.getX()),
						Math.round(e1.getY())));
				TaskSettings.show(ToDoListActivity.instance, task);
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});

		taskListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				detector.onTouchEvent(event);
				return false;
			}
		});

	}

	public void onEmptyClick(View v) {
		Intent addTaskIntent = new Intent(this, AddTaskActivity.class);
		addTaskIntent.putExtra(Database.BUNDLE_ID, selectedGroup == null ? -1 : selectedGroup.id);
		startActivity(addTaskIntent);
	}

	private void onTaskItemClick(int position) {
		Task task = taskAdapter.getItem(position);
		task.status = task.status == 0 ? 1 : 0;
		database.switchTaskStatus(task.id);
	}

	private void onTaskItemLongClick(int position) {
		Task task = taskAdapter.getItem(position);
		TaskSettings.show(this, task);
	}

	public void setSelectedGroup(Group selectedGroup) {
		this.selectedGroup = selectedGroup;
		showTasks(selectedGroup);
	}

	@Override
	public void onGroupListChanged() {
		showGroups();
	}

	@Override
	public void onTaskListChanged() {
		showTasks(selectedGroup);
	}

	@Override
	public void onGroupItemChanged(Group group) {
		showGroups();
	}

	@Override
	public void onTaskItemChanged(Task task) {
		taskAdapter.notifyDataSetChanged();
	}
	

	class TaskListener implements OnItemClickListener, OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> listView, View row, int pos, long id) {
			onTaskItemLongClick(pos);
			return true;
		}

		@Override
		public void onItemClick(AdapterView<?> listView, View row, int pos, long id) {
			onTaskItemClick(pos);
			return;
		}

	}

	class GroupListener implements OnItemSelectedListener, OnLongClickListener {

		@Override
		public void onItemSelected(AdapterView<?> listView, View view, int pos, long id) {
			setSelectedGroup(groupAdapter.getItem(pos));
			return;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			onMenuItemSelected(R.id.menu_add_group, null);
			return;
		}

		@Override
		public boolean onLongClick(View arg0) {
			GroupSettings.show(ToDoListActivity.instance, (Group) groupSpinner.getSelectedItem());
			return true;
		}

	}

}

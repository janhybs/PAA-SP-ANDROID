package cz.edu.x3m.todolist.dialogs;

import cz.edu.x3m.todolist.R;
import cz.edu.x3m.todolist.ToDoListActivity;
import cz.edu.x3m.todolist.data.Database;
import cz.edu.x3m.todolist.data.Task;
import cz.edu.x3m.todolist.manipulation.EditTaskActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class TaskSettings {

	public static void show(final ToDoListActivity todo, final Task task) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(todo);

		alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		alertDialogBuilder.setTitle(R.string.choose_action);
		alertDialogBuilder.setItems(R.array.task_setting, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {

				// # edit item
				case 0:
					Intent editTaskIntent = new Intent(todo, EditTaskActivity.class);
					editTaskIntent.putExtra(Database.BUNDLE_ID, task.id);
					todo.startActivity(editTaskIntent);
					break;

				// # delete item
				case 1:
					ConfirmDialog.showConfirmDialog(todo, R.string.dialog_delete_task, new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							todo.database.deleteTask(task.id);
						}
					});
					break;
				}
			}
		});

		alertDialogBuilder.create().show();
	}
}

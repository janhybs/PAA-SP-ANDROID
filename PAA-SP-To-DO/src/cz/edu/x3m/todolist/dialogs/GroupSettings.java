package cz.edu.x3m.todolist.dialogs;

import cz.edu.x3m.todolist.R;
import cz.edu.x3m.todolist.ToDoListActivity;
import cz.edu.x3m.todolist.data.Database;
import cz.edu.x3m.todolist.data.Group;
import cz.edu.x3m.todolist.manipulation.EditGroupActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.Toast;

public class GroupSettings {

	public static void show(final ToDoListActivity todo, final Group group) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(todo);

		alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		alertDialogBuilder.setTitle(R.string.choose_action);
		alertDialogBuilder.setItems(R.array.group_setting, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {

				// # edit group
				case 0:
					Intent editGroupIntent = new Intent(todo, EditGroupActivity.class);
					editGroupIntent.putExtra(Database.BUNDLE_ID, group.id);
					todo.startActivity(editGroupIntent);
					break;

				// # delete group
				case 1:
					ConfirmDialog.showConfirmDialog(todo, R.string.dialog_delete_group, new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							int result = todo.database.deleteGroup(group.id);
							Toast.makeText(todo,
									result == 1 ? R.string.operation_success : R.string.error_delete_group,
									Toast.LENGTH_LONG).show();

						}
					});
					break;
				}
			}
		});

		alertDialogBuilder.create().show();
	}
}

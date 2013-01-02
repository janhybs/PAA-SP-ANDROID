package cz.edu.x3m.todolist.manipulation;

import android.os.Bundle;
import android.widget.Toast;
import cz.edu.x3m.todolist.R;
import cz.edu.x3m.todolist.ToDoListActivity;
import cz.edu.x3m.todolist.data.Group;
import cz.edu.x3m.todolist.manipulation.iface.AbstractManipulation;

public class AddGroupActivity extends AbstractManipulation {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_group_layout);
		attachButtonListeners();
	}

	@Override
	protected boolean onConfirmClicked() {
		if (!super.onConfirmClicked())
			return false;

		Group group = new Group(getFormTitle(), getFormDescription());
		long result = ToDoListActivity.instance.database.insertGroup(group);

		Toast.makeText(this, result == -1 ? R.string.error_insert_group : R.string.operation_success, Toast.LENGTH_LONG)
				.show();
		finish();
		return true;
	}

}

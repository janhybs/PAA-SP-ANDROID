package cz.edu.x3m.todolist.manipulation;

import android.os.Bundle;
import android.widget.Toast;
import cz.edu.x3m.todolist.R;
import cz.edu.x3m.todolist.ToDoListActivity;
import cz.edu.x3m.todolist.data.Group;
import cz.edu.x3m.todolist.manipulation.iface.AbstractEditManipulation;

public class EditGroupActivity extends AbstractEditManipulation {

	private Group startGroup;
	private Group endGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_group_layout);

		fillForm();
		attachButtonListeners();
	}

	@Override
	public void fillForm() {
		startGroup = ToDoListActivity.instance.database.getGroup(editID);
		setFormTitle(startGroup.title);
		setFormDescription(startGroup.description);
	}

	@Override
	protected boolean onConfirmClicked() {
		if (!super.onConfirmClicked())
			return false;

		endGroup = new Group(getFormTitle(), getFormDescription());
		int result = ToDoListActivity.instance.database.editGroup(editID, endGroup);

		Toast.makeText(
				this,
				result == 0 && startGroup.compareTo(endGroup) != 0 ? R.string.error_edit_group
						: R.string.operation_success, Toast.LENGTH_LONG).show();
		finish();
		return true;
	}

}

package cz.edu.x3m.todolist.manipulation;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;
import cz.edu.x3m.todolist.R;
import cz.edu.x3m.todolist.ToDoListActivity;
import cz.edu.x3m.todolist.adapters.GroupAdapter;
import cz.edu.x3m.todolist.data.Group;
import cz.edu.x3m.todolist.data.Task;
import cz.edu.x3m.todolist.manipulation.iface.AbstractEditManipulation;
import cz.edu.x3m.todolist.manipulation.iface.ITaskManipulation;

public class EditTaskActivity extends AbstractEditManipulation implements ITaskManipulation {

	Task startTask;
	Task endTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_task_layout);

		Spinner spinner = (Spinner) findViewById(R.id.formGID);
		spinner.setPromptId(R.string.choose_group);
		spinner.setAdapter(new GroupAdapter(this, R.id.titleTV, ToDoListActivity.instance.database.getGroups()));

		fillForm();
		attachButtonListeners();
	}

	@Override
	protected boolean onConfirmClicked() {
		if (!super.onConfirmClicked())
			return false;

		endTask = new Task(getFormGID().id, getFormTitle(), getFormDescription(), getFormStatus() ? 1 : 0);
		int result = ToDoListActivity.instance.database.editTask(editID, endTask);

		Toast.makeText(
				this,
				result == 0 && startTask.compareTo(endTask) != 0 ? R.string.error_edit_group
						: R.string.operation_success, Toast.LENGTH_LONG).show();
		finish();
		return true;
	}

	@Override
	public void fillForm() {
		startTask = ToDoListActivity.instance.database.getTask(editID);
		Group group = ToDoListActivity.instance.database.getGroup(startTask.gid);

		setFormTitle(startTask.title);
		setFormDescription(startTask.description);
		setFormStatus(startTask.status != 0);
		setFormGID(group);
	}

	@Override
	public Group getFormGID() {
		Spinner spinner = (Spinner) findViewById(R.id.formGID);
		return (Group) spinner.getSelectedItem();
	}

	@Override
	public void setFormGID(long value) {
		Spinner spinner = (Spinner) findViewById(R.id.formGID);
		GroupAdapter adapter = (GroupAdapter) spinner.getAdapter();
		int position = adapter.findGroup(value);
		spinner.setSelection(position);
	}

	@Override
	public void setFormGID(Group value) {
		setFormGID(value.id);
	}

}

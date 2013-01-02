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

public class AddTaskActivity extends AbstractEditManipulation implements ITaskManipulation {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_task_layout);

		Spinner spinner = (Spinner) findViewById(R.id.formGID);
		spinner.setPromptId(R.string.choose_group);
		spinner.setAdapter(new GroupAdapter(this, R.id.titleTV, ToDoListActivity.instance.database.getGroups()));

		attachButtonListeners();
		fillForm();
	}

	@Override
	protected boolean onConfirmClicked() {
		if (!super.onConfirmClicked())
			return false;

		Task task = new Task(getFormGID().id, getFormTitle(), getFormDescription(), getFormStatus() ? 1 : 0);
		long result = ToDoListActivity.instance.database.insertTask(task);

		Toast.makeText(this, result == -1 ? R.string.error_insert_task : R.string.operation_success, Toast.LENGTH_LONG)
				.show();
		finish();
		return true;
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
		setFormGID(value == null ? -1 : value.id);
	}

	@Override
	public void fillForm() {
		Group prefferedGroup = ToDoListActivity.instance.database.getGroup(editID);
		if (prefferedGroup == null)
			return;

		setFormGID(prefferedGroup);

	}

}

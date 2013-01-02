package cz.edu.x3m.todolist.adapters;


import cz.edu.x3m.todolist.R;
import cz.edu.x3m.todolist.data.Task;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;



public class TaskAdapter extends ArrayAdapter <Task> {


	private Context context;
	public int layoutResourceId;
	public Task data[] = null;


	public TaskAdapter (Context context, Task [] data) {
		super (context, R.layout.task_list_item, data);
		this.layoutResourceId = R.layout.task_list_item;
		this.context = context;
		this.data = data;
	}


	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TaskHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater ();
			row = inflater.inflate (layoutResourceId, parent, false);

			holder = new TaskHolder (row);
			row.setTag (holder);
		} else {
			holder = (TaskHolder) row.getTag ();
		}

		Task task = data[position];
		holder.title.setText (task.title);
		holder.description.setText (task.description);
		holder.status.setChecked (task.status != 0);
		holder.row.setEnabled(task.status == 0);

		return row;
	}
	


	static class TaskHolder {


		CheckBox status;
		TextView title;
		TextView description;
		View row;


		public TaskHolder (View row) {
			this.status = (CheckBox) row.findViewById (R.id.statusCB);
			this.title = (TextView) row.findViewById (R.id.titleTV);
			this.description = (TextView) row.findViewById (R.id.descriptionTV);
			this.row = row;
		}
	}

}

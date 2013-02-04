package cz.edu.x3m.todolist.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cz.edu.x3m.todolist.R;
import cz.edu.x3m.todolist.data.Group;

public class GroupAdapter extends ArrayAdapter<Group> {

	private Context context;
	public int layoutResourceId;
	public Group data[] = null;

	public GroupAdapter(Context context, int textViewID, Group[] data) {
		super(context, R.layout.group_list_item, textViewID, data);
		this.layoutResourceId = R.layout.group_list_item;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		GroupHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new GroupHolder(row);
			row.setTag(holder);
		} else {
			holder = (GroupHolder) row.getTag();
		}

		Group task = data[position];
		holder.title.setText(task.title);
		holder.description.setText(task.description);

		return row;
	}

	/**
	 * Method finds group by id
	 * @param id
	 * @return array position
	 */
	public int findGroup(long id) {
		if (data == null || data.length == 0)
			return -1;

		for (int i = 0, l = data.length; i < l; i++) {
			if (data[i].id == id)
				return i;
		}
		return -1;
	}

	static class GroupHolder {

		TextView title;
		TextView description;
		View row;

		public GroupHolder(View row) {
			this.title = (TextView) row.findViewById(R.id.titleTV);
			this.description = (TextView) row.findViewById(R.id.descriptionTV);
			this.row = row;
		}
	}
}

package cz.edu.x3m.todolist.data;


public interface DatabaseListener {


	void onGroupListChanged ();


	void onTaskListChanged ();


	void onGroupItemChanged (Group group);


	void onTaskItemChanged (Task task);

}

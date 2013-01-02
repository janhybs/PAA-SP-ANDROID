package cz.edu.x3m.todolist.manipulation.iface;

import cz.edu.x3m.todolist.data.Group;




public interface ITaskManipulation {

	Group getFormGID ();
	void setFormGID (Group value);
	void setFormGID (long value);
}

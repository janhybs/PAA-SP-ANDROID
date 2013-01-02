package cz.edu.x3m.todolist.manipulation.iface;


import cz.edu.x3m.todolist.data.Database;
import android.os.Bundle;



public abstract class AbstractEditManipulation extends AbstractManipulation {


	protected long editID;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		editID = getIntent ().getLongExtra (Database.BUNDLE_ID, -1);
	}


	public abstract void fillForm ();
}

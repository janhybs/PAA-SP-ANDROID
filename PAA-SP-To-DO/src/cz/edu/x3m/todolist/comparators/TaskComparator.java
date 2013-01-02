package cz.edu.x3m.todolist.comparators;

import java.util.Comparator;

import cz.edu.x3m.todolist.data.Task;



public class TaskComparator implements Comparator <Task>{

	@Override
	public int compare (Task t0, Task t1) {
		long s0 = t0.status, s1 = t1.status;
		return s0 == s1 ? 0 : s0 == 1 ? 1 : -1;
	}
	

}

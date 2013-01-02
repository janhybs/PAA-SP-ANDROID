package cz.edu.x3m.todolist.data;


public class AbstractSQLType {


	public String title, description;
	public long id;


	public AbstractSQLType (String title, String description, long id) {
		super ();
		this.title = title;
		this.description = description;
		this.id = id;
	}


	public AbstractSQLType (String title, String description) {
		this (title, description, Long.MAX_VALUE);
	}


	@Override
	public String toString () {
		if (description == null || description.length () == 0)
			return title;
		else if (description.length () < 8)
			return String.format ("%s (%s)", title, description);
		return String.format ("%s (%s...)", title, description.substring (0, 8));
	}

}

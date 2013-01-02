package cz.edu.x3m.todolist.data;


public class Group extends AbstractSQLType implements Comparable <Group> {


	public Group (String title, String description) {
		super (title, description);
	}


	public Group (long id, String title, String description) {
		super (title, description, id);
	}

	@Override
	public int compareTo (Group that) {
		return (title + description).compareTo (that.title + that.description);
	}


	public String info () {
		return String.format (	"[TASK ID: '%d', TITLE: '%s', DES: '%s']",
								id,
								title,
								description != null ? description.length () > 32 ? description
											.subSequence (0, 32) : description : "-EMPTY-");
	}
}
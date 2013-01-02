package cz.edu.x3m.todolist.data;


public class Task extends AbstractSQLType implements Comparable <Task> {


	public long gid, status;


	public Task (long gid, String title, String description) {
		super (title, description);
		this.gid = gid;
		this.status = 0;
	}


	public Task (long gid, String title, String description, long status) {
		super (title, description);
		this.gid = gid;
		this.status = status;
	}


	public Task (long id, long gid, String title, String description, long status) {
		super (title, description, id);
		this.gid = gid;
		this.status = status;
	}


	public String info () {
		return String
					.format (	"[GROUP ID: '%d', GID: '%d', TITLE: '%s', STATUS: '%d', DES: '%s']",
								id,
								gid,
								title,
								status,
								description != null ? description.length () > 32 ? description
											.subSequence (0, 32) : description : "-EMPTY-");

	}


	@Override
	public int compareTo (Task t) {
		return status != t.status ? (status > t.status ? 1 : -1) : gid != t.gid ? (gid > t.gid ? 1
					: -1) : (title + description).compareTo (t.title + t.description);
	}
}
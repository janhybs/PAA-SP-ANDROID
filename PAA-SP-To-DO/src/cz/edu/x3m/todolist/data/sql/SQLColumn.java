package cz.edu.x3m.todolist.data.sql;


public class SQLColumn {


	public final String NAME;
	public final String TYPE;
	public final String PRIMARY_KEY;
	public final String NOT_NULL;


	public SQLColumn (String columnName, String type, Boolean isPrimary, Boolean isNotNull) {
		NAME = columnName;
		TYPE = type;
		PRIMARY_KEY = isPrimary ? "PRIMARY KEY" : "";
		NOT_NULL = isNotNull ? "NOT NULL" : "NULL";

	}


	public SQLColumn (String columnName, String type, Boolean isPrimary) {
		NAME = columnName;
		TYPE = type;
		PRIMARY_KEY = isPrimary ? "PRIMARY KEY AUTOINCREMENT" : "";
		NOT_NULL = "";
	}


	@Override
	public String toString () {
		return String.format ("%s %s %s %s", NAME, TYPE, PRIMARY_KEY, NOT_NULL);
	}


	public String toSQL () {
		return toString ();
	}


}

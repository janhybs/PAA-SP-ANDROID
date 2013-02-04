package cz.edu.x3m.todolist.data.sql;



public class SQLTable {

	public final String NAME;
	public final SQLColumn[] COLUMNS;
	public final String[] COLUMNS_NAMES;
	
	

	public SQLTable (String tableName, SQLColumn ...columns) {
		NAME = tableName;
		COLUMNS = columns;
		COLUMNS_NAMES = new String[COLUMNS.length];
		for (int i = 0; i < columns.length; i++)
			COLUMNS_NAMES[i] = COLUMNS[i].NAME;
	}
	
	
	@Override
	public String toString () {
		return createTable ();
	}
	
	/**
	 * Creates "create table" query
	 * @return string
	 */
	public String createTable () {
		StringBuilder result = new StringBuilder ("CREATE TABLE ");
		result.append (NAME);
		result.append (" (");
		for (int i = 0, l = COLUMNS.length; i < l; i++) {
			result.append (COLUMNS[i].toSQL ());
			if (i == l-1) break;
			else result.append (", ");
		}
		result.append (");");
		return result.toString ();
	}
	
	/**
	 * Creates "drop table" query
	 * @return string
	 */
	public String deleteTable () {
		return String.format ("DROP TABLE IF EXISTS %s", NAME);
	}
	
}

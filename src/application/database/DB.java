package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

	private static Connection connection = null;
	
	/**
	 * Creates a connection to the database. This method is called when the program starts
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://mysql02.it.ntnu.no:3306/niklaso_tdt4140?useSSL=false", "niklaso_tdt4140", "gruppe69");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks the state of the database connection
	 * 
	 * @return the state of the database connection
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Boolean connected() {
		try {
			return connection == null ? false : !connection.isClosed();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Closes the connection to the database. This method is called when the program terminates
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void disconnect() {
		try {
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used for generating a resultset based on a select-query
	 * 
	 * @param query is a select-query
	 * @return the resultset from the query passed as an argument
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ResultSet select(String query) {
		try {
			return connection.createStatement().executeQuery(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Used for queries that alter the content of the database, such as deletes or inserts
	 * 
	 * @param query is a query that alter content of the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void alter(String query) {
		try {
			connection.createStatement().execute(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// TODO allow the method to push the exception higher up, so the methods calling it can handle it. This will allow the application to give the user knowledge.
	
	/**
	 * Used for queries that alter the content of the database, such as deletes (or inserts)
	 * 
	 * @param query is a query that alter content of the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void delete(String query) {
		alter(query);
	}
	
	/**
	 * Used for queries that alter the content of the database, such as (deletes or) inserts
	 * 
	 * @param query is a query that alter content of the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void insert(String query) {
		alter(query);
	}

	/**
	 * This method shall never run outside DB.main(String[]), as it should only be used for
	 * initializing the database with an admin.
	 * <br><br>This is done so that the data in the database can be deleted before / during / after testing of
	 * the product without any real consequences, as this method assures that we can easily create a new admin.
	 * <br><br>The admin created will have following login details:
	 * <ul>
	 * <li>Username:   admin</li>
	 * <li>Password:   adminpass</li>
	 * <br>
	 *
	 * @author Niklas Sølvberg
	 */
	private static void initDatabase()
	{
		String query;
		query = "DELETE FROM `user` WHERE `username` = \"admin\";";
		delete(query);
		query = "INSERT INTO `user` VALUES (\"admin\", \"" + Hashing.generateHash("adminpass") + "\", \"A\");";
		insert(query);
	}
	
	private static void initoDB()
	{
		String query;
		query = "DELETE FROM `user` WHERE `username` = \"a\";";
		delete(query);
		query = "INSERT INTO `user` VALUES (\"a\", \"" + Hashing.generateHash("a") + "\", \"A\");";
		insert(query);
		System.out.println("Added admin a");
	}

	/**
	 * The only purpose of this main-method is to initialize the database with an admin in the user-table.
	 * <br><br>This method should never do anything else than run the initDatabase()-method.
	 * <br><ul>
	 * <li>Username:  admin</li>
	 * <li>Password:  adminpass</li>
	 * </ul>
	 *
	 * @param args
	 * @author Niklas Sølvberg
	 */
	public static void main(String[] args)
	{
		connect();
		initDatabase();
		initoDB();
		disconnect();
	}
	
}

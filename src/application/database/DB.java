package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

	private static Connection connection = null;
	
	public static void connect(String path, String user, String pass) {
		try {
			connection = DriverManager.getConnection(path, user, pass);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Boolean connected() {
		try {
			return connection == null ? false : !connection.isClosed();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void disconnect() {
		try {
			connection.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet select(String query) {
		try {
			return connection.createStatement().executeQuery(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void alter(String query) {
		try {
			connection.createStatement().execute(query);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

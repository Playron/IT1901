package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

	private static Connection connection = null;
	
	public static void connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://mysql02.it.ntnu.no:3306/niklaso_tdt4140?useSSL=false", "niklaso_tdt4140", "gruppe69");
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
	
	public static void delete(String query) {
		alter(query);
	}
	
	public static void insert(String query) {
		alter(query);
	}
	
}

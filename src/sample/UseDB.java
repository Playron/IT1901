package sample;

import java.sql.*;
public class UseDB {
	static ResultSet result = null;
	
	private static Connection connectDB() {
		
		Connection conn = null;
		try {
		    conn =
		       DriverManager.getConnection("jdbc:mysql://mysql.stud.ntnu.no/niklaso_tdt4140?&"
		       		+ "useSSL=false&useJDBCCompliantTimezoneShift=true&"
		       		+ "useLegacyDatetimeCode=false&serverTimezone=UTC","niklaso_tdt4140","gruppe69");

		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
	
	public static  void Write(String query){
		try {
			Connection myconn = connectDB();
			Statement statement = myconn.createStatement();
		    statement.execute(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static ResultSet Read(String query) {
		try {
			Connection myconn = connectDB();
			Statement statement = myconn.createStatement();
			result = statement.executeQuery(query);
		} catch (Exception e) {
		}
		return result;
	}
	public static void PrintTable(String table) throws SQLException {
		String query = "select* from "+table;
		ResultSet result = Read(query);
		int columns = result.getMetaData().getColumnCount();
		while(result.next()) {
			String string = "";
			for (int i = 1; i < columns+1; i++) {
				string+=result.getString(i)+",";
			}
			System.out.println(string);
		}	
	}
	public static void PrintSet(ResultSet result) throws SQLException {
		int columnCount = result.getMetaData().getColumnCount();
		while(result.next()) {
			String string = "";
			for (int i = 1; i < columnCount+1; i++) {
				string+=result.getString(i)+",";
			}
			System.out.println(string);
		}
		
	}
	public static void main(String[] args) {
	}
}
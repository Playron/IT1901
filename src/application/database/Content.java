package application.database;

import java.sql.ResultSet;

public class Content {

	public static void addContent(String header, String body) {
		String query = "INSERT INTO `post` (`poster`, `header`, `text`, `approved`) VALUES (\"" + CurrentUser.getUsername() + "\", \"" + header + "\", \"" + body + "\", false);";
		DB.insert(query);
	}
	
	public static ResultSet getPosts() {
		String query = "SELECT `poster`, `header`, `text` FROM `post`;";
		return DB.select(query);
	}
	
}

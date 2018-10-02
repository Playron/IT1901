package application.database;

import java.sql.ResultSet;

public class Content {

	public static void addContent(String header, String body, String state) {
		String query = "INSERT INTO `post` (`poster`, `header`, `text`, `state`) VALUES (\"" + CurrentUser.getUsername() + "\", \"" + header + "\", \"" + body + "\", \"" + state + "\");";
		DB.insert(query);
	}
	
	public static ResultSet getPosts() {
		String query = "SELECT `poster`, `header`, `text` FROM `post`;";
		return DB.select(query);
	}
	
}

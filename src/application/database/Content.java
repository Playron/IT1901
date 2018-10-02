package application.database;

public class Content {

	public static void addContent(String header, String body) {
		String query = "INSERT INTO `post` (`poster`, `header`, `text`, `approved`) VALUES (\"" + CurrentUser.getUsername() + "\", \"" + header + "\", \"" + body + "\", false);";
		DB.insert(query);
	}
	
}

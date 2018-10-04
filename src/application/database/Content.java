package application.database;

import java.sql.ResultSet;

public class Content {

	
	/**
	 * Adds a post to the database.
	 * 
	 * @param header is the header of the post
	 * @param body is the content of the post
	 * @param state is either "submitted" or "published"
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void addContent(String header, String body, String state, String editor) {
		String query = "INSERT INTO `post` (`poster`, `header`, `text`, `state`, `editor`) VALUES (\"" + CurrentUser.getUsername() + "\", \"" + header + "\", \"" + body + "\", \"" + state + "\", \"" + editor + "\");";
		DB.insert(query);
	}
	
	/**
	 * Updates a post. Currently only used when an editor has edited (and published) a post.
	 * 
	 * @param ID is the postID
	 * @param header is the header of the post
	 * @param body is the text content of the post
	 * @param state is the state of the post. This will in all current cases be 'submitted' before the method is called, and 'published' afterwards
	 * @param editor the user that edited the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void updateContent(int ID, String header, String body, String state, String editor) {
		String query = "UPDATE `post` SET `header` = \"" + header +"\", `text` = \"" + body + "\", `state` = \"" + state + "\", `editor` = \"" + editor + "\" WHERE `postID` = " + ID + ";";
		DB.alter(query);
	}
	
	/**
	 * Retrieves all posts from the database.
	 * 
	 * @return the resultset containing all posts in the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ResultSet getPosts() {
		String query = "SELECT * FROM `post`;";
		return DB.select(query);
	}
	
	/**
	 * Retrieves all posts from the database that is posted or edited by a specified user.
	 * 
	 * @param authorOrEditor is a string that limits the posts to the results where either the poster or the editor equals the param
	 * @return all posts where the param is equal to either the poster or the editor
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ResultSet getPosts(String authorOrEditor) {
		String query = "SELECT * FROM `post` WHERE (`poster` = \"" + authorOrEditor + "\") OR (`editor` = \"" + authorOrEditor + "\");";
		return DB.select(query);
	}
	
}

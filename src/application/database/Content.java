package application.database;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		String query;
		if (editor != null)
			query = "INSERT INTO `post` (`poster`, `header`, `text`, `state`, `editor`) VALUES (\"" + CurrentUser.getUsername() + "\", \"" + header + "\", \"" + body + "\", \"" + state + "\", \"" + editor + "\");";
		else
			query = "INSERT INTO `post` (`poster`, `header`, `text`, `state`, `editor`) VALUES (\"" + CurrentUser.getUsername() + "\", \"" + header + "\", \"" + body + "\", \"" + state + "\", null);";
		DB.insert(query);
	}

	/**
	 * Updates a post. Currently only used when an editor has edited (and published) a post.
	 *
	 * @param ID     is the postID
	 * @param header is the header of the post
	 * @param body   is the text content of the post
	 * @param state  is the state of the post. This will in all current cases be 'submitted' before the method is called, and 'published' afterwards
	 * @param editor the user that edited the post
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
	 * @author Niklas Sølvberg
	 */
	public static ResultSet getPosts(String authorOrEditor)
	{
		String query = "SELECT * FROM `post` WHERE (`poster` = \"" + authorOrEditor + "\") OR (`editor` = \"" + authorOrEditor + "\");";
		return DB.select(query);
	}

	/**
	 * Retrieves information about all users in the database.
	 * <br><br>This method should only be called if the user is an admin.
	 *
	 * @return all users in the database
	 * @author Niklas Sølvberg
	 */
	public static ResultSet getUsers()
	{
		if (!CurrentUser.hasAdminRights())
			throw new IllegalStateException("This method can only be called if the current user is an admin.");
		String query = "SELECT * FROM `user`;";
		return DB.select(query);
	}

	/**
	 * Updates an existing user's usertype / access level.
	 * <br><br>This method should never be called unless the current user is an admin.
	 *
	 * @param username is the username of the user that is being updated
	 * @param usertype is the usertype / access level of the user that is being updated
	 * @author Niklas Sølvberg
	 */
	public static void updateUser(String username, String accessLevel)
	{
		Character usertype = null;
		switch (accessLevel) {
			case "User": usertype = 'U'; break;
			case "Author": usertype = 'F'; break;
			case "Copy Editor": usertype = 'C'; break;
			case "Executive Editor": usertype = 'E'; break;
			case "Admin": usertype = 'A'; break;
		}
		String query = "UPDATE `user` SET `usertype` = \"" + usertype + "\" WHERE `username` = \"" + username + "\";";
		DB.alter(query);
	}
	
	/**
	 * Creates a user with the passed arguments.
	 *
	 * @param username the username the user chose at registration
	 * @param password the password the user chose at registration
	 * @author Niklas Sølvberg
	 */
	public static void createUser(String username, String password)
	{
		String hash = Hashing.generateHash(password);
		String query = "INSERT INTO `user` VALUES (\"" + username + "\", \"" + hash + "\", \"U\");";
		try
		{
			ResultSet r = DB.select("SELECT * FROM `user`;");
			while (r.next())
				if (r.getString("username").equals(username))
					throw new IllegalArgumentException("");
			DB.insert(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the `assignedto`-value of the specified post to be the username of the currently logged in user.
	 * 
	 * @param postID is the ID of the post that is being assigned to the currently logged in user
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void updateAssignedToMyself(int postID) {
		String query = "UPDATE `post` SET `assignedto` = \"" + CurrentUser.getUsername() + "\" WHERE `postID` = " + postID + ";";
		DB.alter(query);
	}
	
	/**
	 * Adds an entry to the accessrequest-table (or updating if the user has an entry already)
	 * 
	 * @param accessLevelRequested is the new accesslevel the user requests
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void requestAccessLevel(char accessLevelRequested) {
		if (CurrentUser.getUsername() == null)
			throw new IllegalStateException("Should not be able to request new accesslevel while browsing as an unregistered user.");
		boolean olderRequestExists = false;
		String query = "SELECT * FROM `accessrequest`;";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				if (r.getString("username").equals(CurrentUser.getUsername()))
					olderRequestExists = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		if (olderRequestExists)
			query = "UPDATE `accessrequest` SET `accesslevel` = \"" + accessLevelRequested + "\" WHERE `username` = \"" + CurrentUser.getUsername() + "\";";
		else
			query = "INSERT INTO `accessrequest` VALUES (\"" + CurrentUser.getUsername() + "\", \"" + accessLevelRequested + "\");";
		DB.alter(query);
	}
	
	/**
	 * Retrieves information about all accesslevel requests in the database
	 * 
	 * @return the resultset containing all entries in the accessrequest-table
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ResultSet getAccessLevelRequests() {
		String query = "SELECT * FROM `accessrequest`;";
		return DB.select(query);
	}
	
	/**
	 * Retrieves the accesslevel of the given user
	 * 
	 * @param username of the user you want to know the accesslevel of
	 * @return the accesslevel of the user
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Character getAccessLevel(String username) {
		String query = "SELECT * FROM `user` WHERE `username` = \"" + username + "\";";
		ResultSet r = DB.select(query);
		try {
			while (r.next())
				return (Character) r.getString("usertype").charAt(0);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * Updates the usertype of the user specified by username
	 * 
	 * @param username is the user that has been granted a new accesslevel
	 * @param usertype is the accesslevel that was granted to the user
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void updateAccessLevel(String username, Character usertype) {
		String query = "UPDATE `user` SET `usertype` = \"" + usertype + "\" WHERE `username` = \"" + username + "\";";
		DB.alter(query);
	}
	
	/**
	 * Deletes the accesslevel request of the user specified by username
	 * 
	 * @param username is the user which no longer have an active request
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void deleteAccessLevelRequest(String username) {
		String query = "DELETE FROM `accessrequest` WHERE `username` = \"" + username + "\";";
		DB.delete(query);
	}
	
}

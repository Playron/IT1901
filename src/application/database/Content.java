package application.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.logic.Post;
import javafx.collections.ObservableList;

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
	 * @param complete refers to if the post is complete or incomplete. If the post has never been edited, it is null
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void updateContent(int ID, String header, String body, String state, String editor, boolean complete) {
		String query;
		if (complete)
			query = "UPDATE `post` SET `header` = \"" + header +"\", `text` = \"" + body + "\", `state` = \"" + state + "\", `editor` = \"" + editor + "\", `complete` = 1 WHERE `postID` = " + ID + ";";
		else
			query = "UPDATE `post` SET `header` = \"" + header +"\", `text` = \"" + body + "\", `state` = \"" + state + "\", `editor` = \"" + editor + "\", `complete` = 0 WHERE `postID` = " + ID + ";";
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
	public static ResultSet getPosts(String authorEditorCategory)
	{
		String query;
		if (getCategoryID(authorEditorCategory) == null)
			query = "SELECT * FROM `post` WHERE `poster` = \"" + authorEditorCategory + "\" OR `editor` = \"" + authorEditorCategory + "\";";
		else
			query = "(SELECT * FROM `post` WHERE `poster` = \"" + authorEditorCategory + "\" OR `editor` = \"" + authorEditorCategory + "\") UNION (SELECT `postID`, `poster`, `editor`, `header`, `text`, `state`, `assignedto`, `complete` FROM `post` NATURAL JOIN `postcategories` WHERE `postCategory` = " + getCategoryID(authorEditorCategory) + ");";
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
	 * Updates the `assignedto`-value of the specified post to be the username of the specified user.
	 * 
	 * @param postID is the ID of the post that is being assigned to the specified user
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void updateAssignedTo(String username, int postID) {
		String query = "UPDATE `post` SET `assignedto` = \"" + username + "\" WHERE `postID` = " + postID + ";";
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
	
	/**
	 * Retrieves information about the "complete"-value of a post
	 * 
	 * @param postID is the ID of the post we want to know is complete or not
	 * @return if the post is complete or not
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Boolean isComplete(int postID) {
		String query = "SELECT * FROM `post` WHERE `postID` = " + postID + ";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				return r.getBoolean("complete");
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * Retrieves all categories from the database
	 * 
	 * @return all categories as a list of String-objects
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<String> getCategories() {
		ArrayList<String> categories = new ArrayList<String>();
		String query = "SELECT * FROM `categories`;";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				categories.add(r.getString("categoryName"));
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return categories;
	}
	
	/**
	 * @param categoryName is the name of the category we want the ID of
	 * @return the ID of the category
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Integer getCategoryID(String categoryName) {
		String query = "SELECT * FROM `categories`;";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				if (r.getString("categoryName").equals(categoryName))
					return r.getInt("categoryID");
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * @param categories is a list with all categories the user want to add to the post (while creating the post)
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void addPostCategories(ObservableList<String> categories) {
		if (categories.size() == 0)
			return;
		int postID = 0;
		String query = "SELECT MAX(`postID`) FROM `post`;";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				postID = r.getInt(1);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		if (postID == 0)
			throw new IllegalStateException("The postID can not be 0.");
		for (String string : categories) {
			query = "INSERT INTO `postcategories` VALUES (" + postID + ", " + getCategoryID(string) + ");";
			DB.insert(query);
		}
	}
	
	/**
	 * @param subscribed is the user that is being subscribed to
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void addSubscription(String subscribed) {
		String query = "INSERT INTO `subscription` VALUES (\"" + CurrentUser.getUsername() + "\", \"" + subscribed + "\");";
		DB.insert(query);
	}
	
	/**
	 * @param categoryID is the ID of the category that is being subscribed to
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void addCategorySubscription(int categoryID) {
		String query = "INSERT INTO `categorysubscription` VALUES (\"" + CurrentUser.getUsername() + "\", " + categoryID + ");";
		DB.insert(query);
	}
	
	/**
	 * @param user is the user we want to check if the currently logged in user is subscribed to
	 * @return if the current user is subscribed to user
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Boolean isSubscribedTo(String user) {
		String query = "SELECT COUNT(*) FROM `subscription` WHERE `subscriber` = \"" + CurrentUser.getUsername() + "\" AND `subscribed` = \"" + user + "\";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				return r.getBoolean(1);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * @param categoryID is the category we want to check if the currently logged in user is subscribed to
	 * @return if the current user is subscribed to category
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Boolean isSubscribedTo(int categoryID) {
		String query = "SELECT COUNT(*) FROM `categorysubscription` WHERE `subscriber` = \"" + CurrentUser.getUsername() + "\" AND `categoryID` = " + categoryID + ";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				return r.getBoolean(1);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * @param username is the user we want to check if exists
	 * @return if he user exists
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Boolean userIsSubscribable(String username) {
		if (CurrentUser.getUsername().equals(username))
			return false;
		String query = "SELECT COUNT(*) FROM `user` WHERE `username` = \"" + username + "\";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				return r.getBoolean(1);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	/**
	 * @return a list of all users the currently logged in user subscribes to
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<String> subscribedUsers() {
		ArrayList<String> users = new ArrayList<String>();
		String query = "SELECT * FROM `subscription` WHERE `subscriber` = \"" + CurrentUser.getUsername() + "\";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				users.add(r.getString("subscribed"));
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return users;
	}
	
	/**
	 * @return a list of all categories the currently logged in user subscribes to
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Integer> subscribedCategories() {
		ArrayList<Integer> categories = new ArrayList<Integer>();
		String query = "SELECT * FROM `categorysubscription` WHERE `subscriber` = \"" + CurrentUser.getUsername() + "\";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				categories.add(r.getInt("categoryID"));
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return categories;
	}
	
	public static Boolean checkCategories(int postID) {
		ArrayList<Integer> categories = new ArrayList<Integer>();
		String query = "SELECT * FROM `postcategories` WHERE `postID` = " + postID + ";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				categories.add(r.getInt("postCategory"));
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		for (Integer i : subscribedCategories())
			if (categories.contains(i))
				return true;
		return false;
	}
	
	/**
	 * @param username is the user we want to check if has copy editor rights
	 * @return if the user has copy editor rights
	 * 
	 * @author Niklas Sølvberg
	 */
	public static boolean userIsEditor(String username) {
		String query = "SELECT * FROM `user` WHERE `username` = \"" + username + "\";";
		try {
			ResultSet r = DB.select(query);
			while (r.next())
				if (r.getString("usertype").equals("C") || r.getString("usertype").equals("E") || r.getString("usertype").equals("A"))
					return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**	
	 * Creates a user with the passed arguments.
	 *
	 * @param comment is the comment to the respective post
	 * @param post is the PostId
	 * 
	 * @author Per Haagensen
	 */
	public static void addComment(String comment, Post post) {
		String query;
		query = "INSERT INTO `comment` (`commenter`, `text`, `post`) VALUES (\"" + CurrentUser.getUsername() + "\", \"" + comment + "\", \"" + post.getID() + "\");";
		DB.insert(query);
	}
	
	/**
	 * Creates a user with the passed arguments.
	 *
	 * @return SQL query that returns for comments to a specific post
	 * @param post is the post.postid
	 * 
	 * @author Per Haagensen
	 */
	public static void getPostComment(Post post) {
		String query;
		query = "SELECT * FROM `comment` WHERE `post` = " + post.getID() + ";";
		DB.insert(query);
	}
	
}

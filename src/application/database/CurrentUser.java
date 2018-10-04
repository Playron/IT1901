package application.database;

/**
 * Valid usertypes are:
 * <ol>
 * <li>'A' - Admin</li>
 * <li>'E' - Editor</li>
 * <li>'U' - Author / Regular user</li>
 * <li>null - Unregistered user</li>
 * </ol>
 * 
 * @author Niklas Sølvberg
 */
public class CurrentUser {

	private static String username= null;
	private static Character usertype = null;
	
	/**
	 * Should be called when a user is logging in
	 * 
	 * @param username is the username of the user that is logging in
	 * @param usertype is the usertype of the user that is logging in
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void setCurrentUser(String username, Character usertype) {
		if (!(usertype == null || usertype.equals('A') || usertype.equals('U') || usertype.equals('E')))
			throw new IllegalStateException("Invalid usertype");
		if (username == null) {
			CurrentUser.username = null;
			CurrentUser.usertype = null;
		}
		else {
			CurrentUser.username = username;
			CurrentUser.usertype = usertype;
		}
	}
	
	/**
	 * Should be called when a user is logging out
	 * 
	 * @author Niklas Sølvberg
	 */
	public static void logOutCurrentUser() {
		username = null;
		usertype = null;
	}
	
	/**
	 * @return the username of the currently logged in user
	 * 
	 * @author Niklas Sølvberg
	 */
	public static String getUsername() {
		return username;
	}
	
	/**
	 * @return the usertype of the currently logged in user
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Character getAccessLevel() {
		return usertype;
	}
	
	/**
	 * @return if the currently logged in user has the rights of an admin
	 * 
	 * @author Niklas Sølvberg
	 */
	public static boolean hasAdminRights() {
		if (isRegistered())
			return usertype.equals('A');
		return false;
	}
	
	/**
	 * @return if the currently logged in user has the rights of a regular user / author
	 * 
	 * @author Niklas Sølvberg
	 */
	public static boolean isRegistered() {
		return usertype != null;
	}
	
	/**
	 * @return if the currently logged in user has the rights of an editor
	 * 
	 * @author Niklas Sølvberg
	 */
	public static boolean hasEditorRights() {
		if (isRegistered())
			return usertype.equals('E');
		return false;
	}
	
}

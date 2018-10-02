package application.database;

public class CurrentUser {

//	Valid usertypes are:
//		'A' - Admin
//		'E' - Editor
//		'U' - Author / Regular user
//		null - Unregistered user
//
//	All usertypes have the rights of the usertypes beneath itself
	
	private static String username= null;
	private static Character usertype = null;
	
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
	
	public static void logOutCurrentUser() {
		username = null;
		usertype = null;
	}
	
	public static String getUsername() {
		return username;
	}
	
	public static Character getAccessLevel() {
		return usertype;
	}
	
	public static boolean hasAdminRights() {
		return usertype.equals('A');
	}
	
	public static boolean isRegistered() {
		return usertype != null;
	}
	
	public static boolean hasEditorRights() {
		return usertype.equals('E');
	}
	
}

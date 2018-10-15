package application.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login
{

	/**
	 * Returns the hash stored in the database that is connected to the username parameter
	 *
	 * @param username is the username that is typed at the login screen
	 * @return the stored hash connected to the param username
	 * @author Niklas Sølvberg
	 */
	public static String getStoredPassword(String username)
	{
		String query = "SELECT * FROM `user` WHERE `username` = \"" + username + "\";";
		ResultSet r = DB.select(query);
		try
		{
			while (r.next())
				return r.getString("password");
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			return null;
		}
		return null;
	}

	/**
	 * Checks if the username and password combination is valid
	 *
	 * @param username is the typed username
	 * @param password is the typed password
	 * @return the validity of the login details
	 * @author Niklas Sølvberg
	 */
	public static boolean isValidLogin(String username, String password)
	{
		try
		{
			String hash = getStoredPassword(username);
			return Hashing.validPassword(password, hash);
		} catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * This method should be called after the user has verified his login details
	 *
	 * @param username is the username of the user that successfully logged in
	 * @author Niklas Sølvberg
	 */
	public static void login(String username)
	{
		String query = "SELECT * FROM `user` WHERE `username` = \"" + username + "\";";
		ResultSet r = DB.select(query);
		try
		{
			while (r.next())
				CurrentUser.setCurrentUser(username, r.getString("usertype").charAt(0));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Should be called when a user is logging out
	 *
	 * @author Niklas Sølvberg
	 */
	public static void logout()
	{
		CurrentUser.logOutCurrentUser();
	}
	
}

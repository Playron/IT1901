package application.logic;

public class User
{

	private String username;
	private char usertype;

	/**
	 * Constructs a user-object
	 *
	 * @param username is the user's username
	 * @param usertype is the user's usertype / access level
	 * @author Niklas Sølvberg
	 */
	public User(String username, char usertype)
	{
		this.username = username;
		this.usertype = usertype;
	}

	/**
	 * @return the username
	 * @author Niklas Sølvberg
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Identical to getAccessLevel()
	 *
	 * @return the usertype / access level
	 * @author Niklas Sølvberg
	 */
	public char getUsertype()
	{
		return usertype;
	}
	
	/**
	 * Identical to getUsertype()
	 *
	 * @return the usertype / access level
	 * @author Niklas Sølvberg
	 */
	public char getAccessLevel()
	{
		return usertype;
	}
	
}

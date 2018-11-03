package application.logic;

public class User
{

	private String username;
	private String usertype;
	private String requestedUsertype;

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
		this.usertype = Usertype.asString(usertype);
		requestedUsertype = null;
	}
	
	/**
	 * Constructs a user-object
	 *
	 * @param username is the user's username
	 * @param usertype is the user's usertype / access level
	 * @author Niklas Sølvberg
	 */
	public User(String username, char usertype, char requestedUsertype)
	{
		this.username = username;
		this.usertype = Usertype.asString(usertype);
		this.requestedUsertype = Usertype.asString(requestedUsertype);
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
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getUsertype()
	{
		return usertype;
	}
	
	/**
	 * Identical to getUsertype()
	 *
	 * @return the usertype / access level
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getAccessLevel()
	{
		return usertype;
	}
	
	/**
	 * 
	 *
	 * @return the requested accesslevel
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getRequestedAccessLevel()
	{
		return requestedUsertype;
	}
	
}

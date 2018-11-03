package application.logic;

import java.util.ArrayList;

public class Usertype
{

	/**
	 * @return a list of all possible usertypes / access levels
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<String> usertypes()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("User");
		list.add("Author");
		list.add("Copy Editor");
		list.add("Executive Editor");
		list.add("Admin");
		return list;
	}
	
	/**
	 * @param usertype is an accesslevel represented as a Character
	 * @return the accesslevel represented as a String
	 * 
	 * @author Niklas Sølvberg
	 */
	public static String asString(Character usertype) {
		switch (usertype) {
			case 'U': return "User";
			case 'F': return "Author";
			case 'C': return "Copy Editor";
			case 'E': return "Executive Editor";
			case 'A': return "Admin";
			default: return null;
		}
	}
	
	/**
	 * @param usertype is an accesslevel represented as a String
	 * @return the accesslevel represented as a Character
	 * 
	 * @author Niklas Sølvberg
	 */
	public static Character asChar(String usertype) {
		switch (usertype) {
			case "User": return 'U';
			case "Author": return 'F';
			case "Copy Editor": return 'C';
			case "Executive Editor": return 'E';
			case "Admin": return 'A';
			default: return null;
		}
	}
	
	/**
	 * @param usertype is the usertype represented as a Character
	 * @return the usertype as it would be written down in a sentence. 'A' becomes "an admin" ...
	 * 
	 * @author Niklas Sølvberg
	 */
	public static String accessLevelInSentence(Character usertype) {
		switch (usertype) {
			case 'U': return "a user";
			case 'F': return "an author";
			case 'C': return "a copy editor";
			case 'E': return "an executive editor";
			case 'A': return "an admin";
			default: return "UNDEFINED USER TYPE";
		}
	}
	
}

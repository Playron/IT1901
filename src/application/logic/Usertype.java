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
	
}

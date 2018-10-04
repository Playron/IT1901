package application.logic;

import java.util.ArrayList;

public class Usertype {

	/**
	 * @return a list of all possible usertypes / access levels
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Character> usertypes() {
		ArrayList<Character> list = new ArrayList<Character>();
		list.add('A');
		list.add('E');
		list.add('U');
		return list;
	}
	
}

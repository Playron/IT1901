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
		list.add('U');
		list.add('E');
		list.add('A');
		return list;
	}
	
}

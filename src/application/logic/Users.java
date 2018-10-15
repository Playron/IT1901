package application.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import application.database.Content;
import application.database.CurrentUser;

import javafx.scene.control.Label;

public class Users
{

	/**
	 * @return all users that is stored in the database and sorts them by usertype / access level and username
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<User> getUsers()
	{
		ArrayList<User> users = new ArrayList<User>();
		ResultSet r = Content.getUsers();
		try
		{
			while (r.next())
				users.add(new User(r.getString("username"), r.getString("usertype").charAt(0)));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		if (users.size() > 1)
		{
			Collections.sort(users, new Comparator<User>()
			{
				@Override
				public int compare(User o1, User o2)
				{
					return o1.getUsername().compareTo(o2.getUsername());
				}
			});
			Collections.sort(users, new Comparator<User>()
			{
				@Override
				public int compare(User o1, User o2)
				{
					return (o1.getAccessLevel() > o2.getAccessLevel() ? 1 : -1);
				}
			});
		}
		return users;
	}

	/**
	 * @return label representations of all users that is stored in the database sorted by usertype / access level and username
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getLabels()
	{
		ArrayList<Label> labels = new ArrayList<Label>();
		for (User user : getUsers())
		{
			String line = "     " + user.getUsername();
			int l = line.length();
			for (int i = 0; i < 50 - l; i++)
				line += " ";
			line += user.getUsertype() + "\n-------------------------------------------------------------";
			labels.add(new Label(line));
		}
		return labels;
	}

	/**
	 * @return all users that is stored in the database except for the current user, and sorts them by usertype / access level and username
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<User> getUsersNotCurrent()
	{
		ArrayList<User> users = new ArrayList<User>();
		ResultSet r = Content.getUsers();
		try
		{
			while (r.next())
				if (!r.getString("username").equals(CurrentUser.getUsername()))
					users.add(new User(r.getString("username"), r.getString("usertype").charAt(0)));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		if (users.size() > 1)
		{
			Collections.sort(users, new Comparator<User>()
			{
				@Override
				public int compare(User o1, User o2)
				{
					return o1.getUsername().compareTo(o2.getUsername());
				}
			});
			Collections.sort(users, new Comparator<User>()
			{
				@Override
				public int compare(User o1, User o2)
				{
					return (o1.getAccessLevel() > o2.getAccessLevel() ? 1 : -1);
				}
			});
		}
		return users;
	}

	/**
	 * @return label representations of all users that is stored in the database except for the current user, sorted by usertype / access level and username
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getLabelsNotCurrent()
	{
		ArrayList<Label> labels = new ArrayList<Label>();
		for (User user : getUsersNotCurrent())
		{
			String line = "     " + user.getUsername();
			int l = line.length();
			for (int i = 0; i < 50 - l; i++)
				line += " ";
			line += user.getUsertype() + "\n-------------------------------------------------------------";
			labels.add(new Label(line));
		}
		return labels;
	}
	
}

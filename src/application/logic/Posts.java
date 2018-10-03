package application.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.database.Content;
import javafx.scene.control.Label;

public class Posts {

	/**
	 * Creates a list of post-objects, containing all posts from the database
	 * 
	 * @return a list of all posts from the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts();
		try {
			while (r.next())
				posts.add(new Post(r.getString("header"), r.getString("text"), r.getString("poster")));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return posts;
	}
	
	/**
	 * Creates a list of post-objects, containing all submitted posts from the database
	 * 
	 * @return a list of all posts from the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getSubmittedPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts();
		try {
			while (r.next() && r.getString("state").equals("submitted"))
				posts.add(new Post(r.getString("header"), r.getString("text"), r.getString("poster")));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return posts;
	}
	
	/**
	 * Creates a list of post-objects, containing all published posts from the database
	 * 
	 * @return a list of all posts from the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getPublishedPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts();
		try {
			while (r.next() && r.getString("state").equals("published"))
				posts.add(new Post(r.getString("header"), r.getString("text"), r.getString("poster")));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return posts;
	}
	
	/**
	 * Creates a list of labels, representing all posts from the database
	 * 
	 * @return a list of all posts from the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getLabels() {
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Post post : getPosts()) {
			String[] wordList = post.getBody().split(" ");
			String body = "";
			int i = 0;
			for (int j = 0; j < wordList.length; j++) {
				if (i > 100) {
					body += "\n";
					i = 0;
				}
				i += wordList[j].length() + 1;
				body += wordList[j] + " ";
			}
			labels.add(new Label("____________________________________________________________________________________________________\n____________________________________________________________________________________________________\n\n\n" + post.getHeader() + ", by " + post.getPoster() + "\n--------------------------------------------------\n" + body));
		}
		return labels;
	}
	
	/**
	 * Creates a list of labels, representing all submitted posts from the database
	 * 
	 * @return a list of all posts from the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getSubmittedLabels() {
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Post post : getSubmittedPosts()) {
			String[] wordList = post.getBody().split(" ");
			String body = "";
			int i = 0;
			for (int j = 0; j < wordList.length; j++) {
				if (i > 100) {
					body += "\n";
					i = 0;
				}
				i += wordList[j].length() + 1;
				body += wordList[j] + " ";
			}
			labels.add(new Label("____________________________________________________________________________________________________\n____________________________________________________________________________________________________\n\n\n" + post.getHeader() + ", by " + post.getPoster() + "\n--------------------------------------------------\n" + body));
		}
		return labels;
	}
	
	/**
	 * Creates a list of labels, representing all published posts from the database
	 * 
	 * @return a list of all posts from the database
	 * 
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getPublishedLabels() {
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Post post : getPublishedPosts()) {
			String[] wordList = post.getBody().split(" ");
			String body = "";
			int i = 0;
			for (int j = 0; j < wordList.length; j++) {
				if (i > 100) {
					body += "\n";
					i = 0;
				}
				i += wordList[j].length() + 1;
				body += wordList[j] + " ";
			}
			labels.add(new Label("____________________________________________________________________________________________________\n____________________________________________________________________________________________________\n\n\n" + post.getHeader() + ", by " + post.getPoster() + "\n--------------------------------------------------\n" + body));
		}
		return labels;
	}
	
}

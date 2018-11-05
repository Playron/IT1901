package application.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;

import application.database.Content;

import javafx.scene.control.Label;

public class Posts {

	/**
	 * Creates a list of post-objects, containing all posts from the database
	 * <br><br>Update 03.10.2018: Now sorts the posts from newest to oldest
	 *
	 * @return a list of all posts from the database
	 *
	 * @author Niklas Sølvberg
	 * @author Torleif Hensvold
	 */
	public static ArrayList<Post> getPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts();
		try {
			while (r.next())
				posts.add(new Post(r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Collections.reverse(posts);
		return posts;
	}

	/**
	 * Creates a list of post-objects, containing all submitted posts from the database
	 * <br><br>Update 03.10.2018: Now sorts the posts from newest to oldest
	 *
	 * @return a list of all submitted posts from the database
	 *
	 * @author Niklas Sølvberg
	 * @author Torleif Hensvold
	 */
	public static ArrayList<Post> getSubmittedPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts();
		try {
			while (r.next())
				if (r.getString("state").equals("submitted"))
					posts.add(new Post(r.getString("assignedto"), r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Collections.reverse(posts);
		return posts;
	}

	/**
	 * Creates a list of post-objects, containing all published posts from the database
	 * <br><br>Update 03.10.2018: Now sorts the posts from newest to oldest
	 *
	 * @return a list of all published posts from the database
	 *
	 * @author Niklas Sølvberg
	 * @author Torleif Hensvold
	 */
	public static ArrayList<Post> getPublishedPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts();
		try {
			while (r.next())
				if (r.getString("state").equals("published"))
					posts.add(new Post(r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Collections.reverse(posts);
		return posts;
	}

	/**
	 * Creates a list of post-objects, containing a selection of all posts from the database
	 *
	 * @param authorOrEditor is a string that limits the posts to the results where either the poster or the editor equals the param
	 * @return a list of all posts where either the editor or the poster is equal to the param
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getPosts(String authorOrEditor)
	{
		if (authorOrEditor.equals(""))
			return getPosts();
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts(authorOrEditor);
		try
		{
			while (r.next())
				if (r.getString("state").equals("published"))
					posts.add(new Post(r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Collections.reverse(posts);
		return posts;
	}

	/**
	 * Creates a list of post-objects, containing a selection of submitted posts from the database
	 *
	 * @param authorOrEditor is a string that limits the posts to the results where either the poster or the editor equals the param
	 * @return a list of all submitted posts where either the editor or the poster is equal to the param
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getSubmittedPosts(String authorOrEditor)
	{
		if (authorOrEditor.equals(""))
			return getSubmittedPosts();
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts(authorOrEditor);
		try
		{
			while (r.next())
				if (r.getString("state").equals("submitted"))
					posts.add(new Post(r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Collections.reverse(posts);
		return posts;
	}

	/**
	 * Creates a list of post-objects, containing a selection of published posts from the database
	 *
	 * @param authorOrEditor is a string that limits the posts to the results where either the poster or the editor equals the param
	 * @return a list of all published posts where either the editor or the poster is equal to the param
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getPublishedPosts(String authorOrEditor)
	{
		if (authorOrEditor.equals(""))
			return getPublishedPosts();
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts(authorOrEditor);
		try
		{
			while (r.next())
				if (r.getString("state").equals("published"))
					posts.add(new Post(r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		Collections.reverse(posts);
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

	/**
	 * Creates a list of labels, representing a selection of all posts from the database
	 *
	 * @return a list of all posts from the database where either the editor or the poster is equal to authorOrEditor
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getLabels(String authorOrEditor)
	{
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Post post : getPosts(authorOrEditor))
		{
			String[] wordList = post.getBody().split(" ");
			String body = "";
			int i = 0;
			for (int j = 0; j < wordList.length; j++)
			{
				if (i > 100)
				{
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
	 * Creates a list of labels, representing a selection of all submitted posts from the database
	 *
	 * @return a list of all submitted posts from the database where either the editor or the poster is equal to authorOrEditor
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getSubmittedLabels(String authorOrEditor)
	{
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Post post : getSubmittedPosts(authorOrEditor))
		{
			String[] wordList = post.getBody().split(" ");
			String body = "";
			int i = 0;
			for (int j = 0; j < wordList.length; j++)
			{
				if (i > 100)
				{
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
	 * Creates a list of labels, representing a selection of published posts from the database
	 *
	 * @return a list of all published posts from the database where either the editor or the poster is equal to authorOrEditor
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getPublishedLabels(String authorOrEditor)
	{
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Post post : getPublishedPosts(authorOrEditor))
		{
			String[] wordList = post.getBody().split(" ");
			String body = "";
			int i = 0;
			for (int j = 0; j < wordList.length; j++)
			{
				if (i > 100)
				{
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
	 * Creates a list of post-objects, containing all published, subscribed posts from the database
	 *
	 * @return a list of all published, subscribed posts from the database
	 *
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getSubscribedPosts() {
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts();
		try {
			while (r.next())
				if (Content.subscribedUsers().contains(r.getString("poster")) || Content.subscribedUsers().contains(r.getString("editor")) || Content.checkCategories(r.getInt("postID")))
					posts.add(new Post(r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		Collections.reverse(posts);
		return posts;
	}
	
	/**
	 * Creates a list of post-objects, containing a selection of published, subscribed posts from the database
	 *
	 * @param authorOrEditor is a string that limits the posts to the results where either the poster or the editor equals the param
	 * @return a list of all published, subscribed posts where either the editor or the poster is equal to the param
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Post> getSubscribedPosts(String authorOrEditor) {
		if (authorOrEditor.equals(""))
			return getSubscribedPosts();
		ArrayList<Post> posts = new ArrayList<Post>();
		ResultSet r = Content.getPosts(authorOrEditor);
		try {
			while (r.next())
				posts.add(new Post(r.getInt("postID"), r.getString("header"), r.getString("text"), r.getString("poster"), r.getString("editor")));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		Collections.reverse(posts);
		return posts;
	}
	
	/**
	 * Creates a list of labels, representing a selection of published posts from the database
	 *
	 * @return a list of all published posts from the database where either the editor or the poster is equal to authorOrEditor
	 * @author Niklas Sølvberg
	 */
	public static ArrayList<Label> getSubscribedLabels(String authorOrEditor)
	{
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Post post : getSubscribedPosts(authorOrEditor))
		{
			labels.add(getPostLabel(post));
		}
		return labels;
	}
	
	
	public static Label getPostLabel(Post post)
	{
		String[] wordList = post.getBody().split(" ");
		String body = "";
		int i = 0;
		for (int j = 0; j < wordList.length; j++)
		{
			if (i > 100)
			{
				body += "\n";
				i = 0;
			}
			i += wordList[j].length() + 1;
			body += wordList[j] + " ";
		}
		return new Label("____________________________________________________________________________________________________\n____________________________________________________________________________________________________\n\n\n" + post.getHeader() + ", by " + post.getPoster() + "\n--------------------------------------------------\n" + body);
	}
	
}

package application.logic;

public class Post {

	private String header;
	private String body;
	private String poster;
	private String editor;
	private int ID;
	private String assigned;
	
	/**
	 * Constructs post-objects
	 * 
	 * @param ID is the ID of a post
	 * @param header is the header of a post
	 * @param body is the content of a post
	 * @param poster is the user that created a post
	 * 
	 * @author Niklas Sølvberg
	 */
	public Post(int ID, String header, String body, String poster) {
		this.ID = ID;
		this.header = header;
		this.body = body;
		this.poster = poster;
		editor = null;
		assigned = null;
	}
	
	/**
	 * Constructs post-objects
	 * 
	 * @param ID is the ID of a post
	 * @param header is the header of a post
	 * @param body is the content of a post
	 * @param poster is the user that created a post
	 * @param editor is the editor of a post
	 * 
	 * @author Niklas Sølvberg
	 */
	public Post(int ID, String header, String body, String poster, String editor) {
		this.ID = ID;
		this.header = header;
		this.body = body;
		this.poster = poster;
		this.editor = editor;
		assigned = null;
	}
	
	/**
	 * Constructs post-objects
	 * 
	 * @param assigned is the assigned editor of a post
	 * @param ID is the ID of a post
	 * @param header is the header of a post
	 * @param body is the content of a post
	 * @param poster is the user that created a post
	 * 
	 * @author Niklas Sølvberg
	 */
	public Post(String assigned, int ID, String header, String body, String poster) {
		this.ID = ID;
		this.header = header;
		this.body = body;
		this.poster = poster;
		editor = null;
		this.assigned = assigned;
	}
	
	/**
	 * Constructs post-objects
	 * 
	 * @param assigned is the assigned editor of a post
	 * @param ID is the ID of a post
	 * @param header is the header of a post
	 * @param body is the content of a post
	 * @param poster is the user that created a post
	 * @param editor is the editor of a post
	 * 
	 * @author Niklas Sølvberg
	 */
	public Post(String assigned, int ID, String header, String body, String poster, String editor) {
		this.ID = ID;
		this.header = header;
		this.body = body;
		this.poster = poster;
		this.editor = editor;
		this.assigned = assigned;
	}
	
	/**
	 * @return the header of the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getHeader() {
		return header;
	}
	
	/**
	 * @return the content of the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * @return the user that created the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getPoster() {
		return poster;
	}
	
	/**
	 * @return the user that edited the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getEditor() {
		return editor;
	}

	/**
	 * Sets the editor of the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	/**
	 * @return the ID of the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * @return the assigned editor of the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public String getAssigned() {
		if (assigned == null)
			return null;
		if (assigned.equals(""))
			return null;
		return assigned;
	}
	
	/**
	 * @param assigned is the new assigned editor of the post
	 * 
	 * @author Niklas Sølvberg
	 */
	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}
	
	/**
	 * @return if the post is assigned to anyone
	 * 
	 * @author Niklas Sølvberg
	 */
	public boolean isAssigned() {
		if (assigned == null)
			return false;
		if (assigned.equals(""))
			return false;
		return true;
	}
	
}

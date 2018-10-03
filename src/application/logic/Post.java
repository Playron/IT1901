package application.logic;

public class Post {

	private String header;
	private String body;
	private String poster;
	
	/**
	 * Constructs post-objects
	 * 
	 * @param header is the header of a post
	 * @param body is the content of a post
	 * @param poster is the user that created a post
	 * 
	 * @author Niklas Sølvberg
	 */
	public Post(String header, String body, String poster) {
		this.header = header;
		this.body = body;
		this.poster = poster;
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
	
}

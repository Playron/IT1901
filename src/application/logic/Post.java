package application.logic;

public class Post {

	private String header;
	private String body;
	private String poster;
	
	public Post(String header, String body, String poster) {
		this.header = header;
		this.body = body;
		this.poster = poster;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getPoster() {
		return poster;
	}
	
}

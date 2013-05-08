package ch.ethz.inf.dbproject.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that represents a user comment.
 */
public class Comment {
	
	private final int id;
	private final String content;
	private final Date commentDate;
	private final String username;
	private final int userId;
	
	public Comment(final int id, final String content, final Date date, final String username,final int userId) {
		this.id = id;
		this.content = content;
		this.commentDate = date;
		this.username = username;
		this.userId = userId;
	}
	
	public Comment(final String content, final User uid) {
		this.content = content;
		this.username = uid.getUsername();
		this.userId = uid.getId();
		this.id = 0;
		this.commentDate = null;
	}
	
	public Comment(ResultSet rs) throws SQLException {
		this.commentDate = rs.getDate("commentDate");
		this.content = rs.getString("content");
		this.id = rs.getInt("id");
		this.username = rs.getString("username");
		this.userId = rs.getInt("uid");
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public String getUsername() {
		return username;
	}

	public int getUserId() {
		return userId;
	}

}

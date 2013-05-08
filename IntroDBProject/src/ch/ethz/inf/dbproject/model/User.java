package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that represents a registered in user.
 */
public final class User {

	private final int id;
	private final String username;
	private final String email;
	private final String pw;
	
	public User(final int id, final String username, final String email,final String pw) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.pw = pw;
	}
	
	public User(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.username = rs.getString("username");
		this.email = rs.getString("email");
		this.pw = rs.getString("pw");
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPw() {
		return pw;
	}
}

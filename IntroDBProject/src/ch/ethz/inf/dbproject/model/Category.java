package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Object that represents a category of project (i.e. Art, Music...) 
 */
public final class Category {

	/**
	 * TODO All properties need to be added here 
	 */	
	private final String name;

	public Category(final String name) {
		super();
		this.name = name;
	}
	
	public Category(ResultSet rs) throws SQLException {
		this.name = rs.getString("catName");
	}

	public final String getName() {
		return name;
	}
	
}

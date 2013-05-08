package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a City 
 */
public final class City {

	private final String name;

	public City(final String name) {
		super();
		this.name = name;
	}

	public City(ResultSet rs) throws SQLException{
		this.name = rs.getString("cityName");
	}

	public final String getName() {
		return name;
	}	
}
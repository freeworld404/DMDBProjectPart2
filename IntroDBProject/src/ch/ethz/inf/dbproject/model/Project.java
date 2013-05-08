package ch.ethz.inf.dbproject.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public final class Project {
	
	/**
	 * TODO The properties of the project should be added here
	 */
	private final int id;
	private final String title;
	private final String shortDescription;
	private final String description;
	private final int fundProgress;
	private final int baseGoal;
	private final String username;
	private final int daysLeft;
	private final Date startDate;
	private final Date endDate;
	private final String category;
	private final String city;
	/**
	 * Construct a new project.
	 * 
	 * @param name		The name of the project
	 */
	public Project(	final int id, final String title, final String description,final int fundProgress, final int baseGoal, final Timestamp createDate, final Date startDate, final Date endDate, final User uid, final Category catId,final City cityId) {
		this.id = id;
		this.title = title;
		this.description = description;
		if (description.length() > 140) {
			this.shortDescription = description.substring(0, 140);
		} else {
			this.shortDescription = this.description;
		}
		this.fundProgress = fundProgress;
		this.baseGoal = baseGoal;
		this.startDate = startDate;
		this.endDate = endDate;
		this.username = uid.getUsername();
		this.category = catId.getName();
		this.city = cityId.getName();
		this.daysLeft = endDate.compareTo(startDate);
	}
	
	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public String getCity() {
		return city;
	}

	public Project(	final ResultSet rs) throws SQLException {
		// TODO These need to be adapted to your schema
		// TODO Extra properties need to be added
		this.id = rs.getInt("id");
		this.title = rs.getString("title");
		this.description = rs.getString("description");
		if (description.length() > 140) {
			this.shortDescription = description.substring(0, 140);
		} else {
			this.shortDescription = description;
		}
		this.fundProgress = rs.getInt("fundProgress");
		this.baseGoal = rs.getInt("baseGoal");
		this.startDate = rs.getDate("startDate");
		this.endDate = rs.getDate("endDate");
		this.username = rs.getString("username");
		this.category = rs.getString("catName");
		this.city = rs.getString("cityName");
		this.daysLeft = this.endDate.compareTo(startDate);
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public int getBaseGoal() {
		return baseGoal;
	}

	public String getUsername() {
		return username;
	}

	public int getDaysLeft() {
		return daysLeft;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	public int getFundProgress() {
		return fundProgress;
	}
}
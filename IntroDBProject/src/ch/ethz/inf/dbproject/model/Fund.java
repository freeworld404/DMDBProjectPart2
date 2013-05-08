package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Fund {
	
	private final int id;
	private final Timestamp fundDate;
	private final int fundingLevelAmount;
	private final String username;
	
	
	public Fund(final int id,final Timestamp fundDate, final int amount,final String username){
		this.id = id;
		this.fundDate = fundDate;
		this.fundingLevelAmount = amount;
		this.username = username;
	}

	public Fund(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.fundDate = rs.getTimestamp("fundDate");
		this.fundingLevelAmount = rs.getInt("amount");
		this.username = rs.getString("username");
	}

	public int getId() {
		return id;
	}

	public Timestamp getFundDate() {
		return fundDate;
	}

	public int getFundingLevelAmount() {
		return fundingLevelAmount;
	}

	public String getUsername() {
		return username;
	}
}

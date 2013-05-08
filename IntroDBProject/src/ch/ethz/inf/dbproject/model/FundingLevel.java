package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FundingLevel {
	
	private final int id;
	private final int amount;
	private final String reward;
	
	public FundingLevel(final int id, final int amount,final String reward) {
		this.id = id;
		this.amount = amount;
		this.reward = reward;
	}

	public FundingLevel(ResultSet rs) throws SQLException {
		this.id = rs.getInt("id");
		this.amount = rs.getInt("amount");
		this.reward = rs.getString("reward");
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public String getReward() {
		return reward;
	}
}

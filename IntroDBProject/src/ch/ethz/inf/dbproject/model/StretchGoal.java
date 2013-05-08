package ch.ethz.inf.dbproject.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StretchGoal {
	
	//private final int id;
	private final int amount;
	private final String reward;
	private final int pid;
	
	public StretchGoal(final int id, final int amount, final String reward, final int pid){
		//this.id = id;
		this.amount = amount;
		this.reward = reward;
		this.pid = pid;
	}

	public StretchGoal(	final ResultSet rs) throws SQLException {
		//this.id = 0;//rs.getInt("id");
		this.pid = rs.getInt("pid");
		this.amount = rs.getInt("amount");
		this.reward = rs.getString("reward");
	}
	/*
	public int getId() {
		return id;
	}
	*/
	public int getAmount() {
		return amount;
	}

	public String getReward() {
		return reward;
	}
	
	public int getPid() {
		return pid;
	}
}

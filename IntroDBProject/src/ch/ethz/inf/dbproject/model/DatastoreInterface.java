package ch.ethz.inf.dbproject.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.dbproject.database.MySQLConnection;

/**
 * This class should be the interface between the web application
 * and the database. Keeping all the data-access methods here
 * will be very helpful for part 2 of the project.
 */
public final class DatastoreInterface {

	private Connection sqlConnection;

	private final String fieldsProject = "p.id,title,description,IFNULL(sum(amount),0) as fundProgress," +
			                             "baseGoal,username,startDate,endDate,catName,cityName";
	private final String tablesProject = "( fund f left outer join fundingLevel fl on f.flid=fl.id ) " +
					                     "right outer join project p on (fl.pid=p.id), " +
					                     "user u,category cat,city c ";
	private final String baseWhereProject = "p.uid=u.id and p.catId=cat.id and p.cityId=c.id";
	
	public DatastoreInterface() {
		this.sqlConnection = MySQLConnection.getInstance().getConnection();
	}
	
	public final Project getProjectById(final int id) {
		Project result = null;
		
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject + " and p.id="+id+" GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			rs.first();
			result = new Project(rs);
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
		return result;
		
	}
	
	public final List<Project> getAllProjects() {
		final List<Project> projects = new ArrayList<Project>();		
		/* select 
		 * p.id,title,description,IFNULL(sum(amount),0) as fundProgress,baseGoal,
		 * startDate,endDate,username,cityName,catName 
		 * from 
		 *   ( fund f left outer join fundingLevel fl on f.flid=fl.id ) 
		 *  right outer join project p on (fl.pid=p.id),
		 *  user u,city c, category cat
		 *  where p.uid=u.id and p.catId=cat.id and p.cityId=c.id group by p.id;
		 */
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject + " GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public User getUserById(int id) {
		User result = null;	
		//Code example for DB access
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT id,username,email,pw FROM user u where id="+id);
			rs.first();
			result = new User(rs);
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return result;
	}

	public Category getCategoryById(int id) {
		Category result = null;	
		//Code example for DB access
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT catName FROM category where id="+id);
			rs.first();
			result = new Category(rs);
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return result;
	}

	public City getCityById(int id) {
		City result = null;	
		//Code example for DB access
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT cityName FROM city where id="+id);
			rs.first();
			result = new City(rs);
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return result;
	}
	
	public final List<StretchGoal> getStretchGoalByProjectId(final int id) {
		final List<StretchGoal> result = new ArrayList<StretchGoal>();
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			String sql_query = "SELECT pid, amount, reward FROM project p, stretchGoal s where p.id=s.pid and s.pid="+id;
			final ResultSet rs = stmt.executeQuery(sql_query);
			while(rs.next()) {
				result.add(new StretchGoal(rs));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("In StretchGoal");
			System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
			e.printStackTrace();
		}
		return result;
		
	}
	
	public void addCommentForProject(Integer id, Comment commentObj) {
		// id = Project id 
		try {
			final String sql = "INSERT INTO comments (pid, content, uid) values (?,?,?)";
			final java.sql.PreparedStatement stmt = sqlConnection.prepareStatement(sql);
			stmt.setInt(1,id);
			stmt.setString(2, commentObj.getContent());
			stmt.setInt(3, commentObj.getUserId());
			stmt.executeUpdate();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();			
		}
	}

	public User getUserByUsername(String username) {
		User result = null;	
		//Code example for DB access
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT id,username,email,pw FROM user where username='"+username+"'");
			rs.first();
			result = new User(rs);
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return result;
	}

	public List<Project> getProjectsByCategory(String category) {
		final List<Project> projects = new ArrayList<Project>();		
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" and catName='"+category+"'" +" GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public List<Project> getMostPopularProjects() {
		final List<Project> projects = new ArrayList<Project>();		
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + ", count(*) as fund_givers " +
					"FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" GROUP BY p.id ORDER BY fund_givers DESC LIMIT 10";
			final ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public List<Project> getMostFundedProjects() {
		final List<Project> projects = new ArrayList<Project>();		
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" GROUP BY p.id ORDER BY fundProgress DESC LIMIT 10";
			final ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public List<Project> getSoonEndingProjects() {
		final List<Project> projects = new ArrayList<Project>();		
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" and DATEDIFF(p.endDate,CURDATE()) < 5000 GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public List<Comment> getCommentsOfProject(Integer id) {
		final List<Comment> comments = new ArrayList<Comment>();		
		//Code example for DB access
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT c.id,username,uid,content,commentDate from comments c,user u where c.uid=u.id and c.pid="+id);
			while (rs.next()) {
				comments.add(new Comment(rs));
			}
			rs.close();
			stmt.close();			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return comments;
	}

	public List<Project> searchByName(String name) {
		final List<Project> projects = new ArrayList<Project>();
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" and p.title like '%"+name+"%' GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public List<Project> searchByCategory(String name) {
		final List<Project> projects = new ArrayList<Project>();		
		//Code example for DB access
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" and cat.catName like '%"+name+"%' GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public List<Project> searchByCity(String name) {
		final List<Project> projects = new ArrayList<Project>();		
		//Code example for DB access
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" and c.cityName like '%"+name+"%' GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

	public List<Fund> getFundsOfProject(int pid) {
		final List<Fund> funds = new ArrayList<Fund>();
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT f.id,fundDate,amount,username from fund f,project p, user u, fundingLevel fl where f.uid=u.id and f.flid=fl.id and fl.pid=p.id and p.id="+pid);
			while (rs.next()) {
				funds.add(new Fund(rs));
			}
			rs.close();
			stmt.close();			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return funds;
	}

	public List<FundingLevel> getFundingLevelsOfProject(Integer id) {
		final List<FundingLevel> fundLevels = new ArrayList<FundingLevel>();
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT id,amount,reward from fundingLevel fl where fl.pid="+id);
			while (rs.next()) {
				fundLevels.add(new FundingLevel(rs));
			}
			rs.close();
			stmt.close();			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return fundLevels;
	}

	public void fundProject( int uid, int flid) {
		try {
			final String sql = "INSERT INTO fund (uid, flid) values (?,?)";
			final java.sql.PreparedStatement stmt = sqlConnection.prepareStatement(sql);
			stmt.setInt(1,uid);
			stmt.setInt(2, flid);
			stmt.executeUpdate();
		} catch (final SQLException ex) {			
			ex.printStackTrace();			
		}
	}

	public int insertCity (String city) {
		int cityId = 0;
		try {
			//Check if city exists and insert if not
			final Statement getInfo = this.sqlConnection.createStatement();
			final ResultSet rs = getInfo.executeQuery("SELECT cityName, id from city where cityName='"+city+"'");
			
			//If city doesn't exist, add it
			if (!rs.isBeforeFirst()) {
				final String sqlCity = "INSERT INTO city (cityName) values (?)";
				final java.sql.PreparedStatement stmt = sqlConnection.prepareStatement(sqlCity);
				stmt.setString(1, city);
				stmt.executeUpdate();
				stmt.close();
			}
			
			//Select city and get Id
			final Statement getInfo2 = this.sqlConnection.createStatement();
			final ResultSet rs2 = getInfo2.executeQuery("SELECT cityName, id from city where cityName='"+city+"'");
			rs2.first();
			cityId = rs2.getInt("id");
			
			rs.close();
			rs2.close();
			getInfo2.close();
			getInfo.close();		
		} catch (final SQLException ex) {			
			ex.printStackTrace();			
		}
		return cityId;
	}

	public int projectIdByTitle (String title) {
		int result = 0;	
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT p.id FROM project p where p.title='"+title+"'");
			rs.first();
			result = rs.getInt("id");
			rs.close();
			stmt.close();
		
		} catch (final SQLException ex) {			
			ex.printStackTrace();			
		}
		return result;
	}
	
	public void createProject(int uid, String title, String description,
			String baseGoal, String stretchGoal, String stretchReward,
			String startDay, String startMonth, String startYear,
			String endDay, String endMonth, String endYear, String city,
			String categoryId) {
		
		try {
			int cityId = insertCity(city);
			int catId = Integer.parseInt(categoryId);
			int baseGoalInt = Integer.parseInt(baseGoal);
			int stretchGoalInt = Integer.parseInt(stretchGoal);
			
			final String sql = "INSERT INTO project (title, description, baseGoal, startDate, endDate, uid, catId, cityId) values (?,?,?,?,?,?,?,?)";
			final java.sql.PreparedStatement stmt = sqlConnection.prepareStatement(sql);
			stmt.setString(1, title);
			stmt.setString(2, description);
			stmt.setInt(3, baseGoalInt);
			stmt.setString(4, startYear+"-"+startMonth+"-"+startDay);
			stmt.setString(5, endYear+"-"+endMonth+"-"+endDay);
			stmt.setInt(6, uid);
			stmt.setInt(7, catId);
			stmt.setInt(8, cityId);
			stmt.executeUpdate();
			stmt.close();
			
			int pid = projectIdByTitle(title);
			final String stretchGoalEntry = "INSERT INTO stretchGoal (amount, reward, pid) values (?,?,?)";
			final java.sql.PreparedStatement stretchGoalstmt = sqlConnection.prepareStatement(stretchGoalEntry);
			stretchGoalstmt.setInt(1, stretchGoalInt);
			stretchGoalstmt.setString(2, stretchReward);
			stretchGoalstmt.setInt(3, pid);
			stretchGoalstmt.executeUpdate();
			stretchGoalstmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();			
		}
	}
	
	public User getOwnerOfProject(Integer id) {
		User result = null;	
		//Code example for DB access
		try {
			
			final Statement stmt = this.sqlConnection.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT u.id,username,email,pw FROM user u,project p where p.uid=u.id and p.id='"+id+"'");
			rs.first();
			result = new User(rs);
			rs.close();
			stmt.close();
			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return result;
	}

	public void newFundingLevelForProject(Integer id, int amount, String reward) {
		try {
			final String sql = "INSERT INTO fundingLevel (amount,reward,pid) values (?,?,?)";
			final java.sql.PreparedStatement stmt = sqlConnection.prepareStatement(sql);
			stmt.setInt(1,amount);
			stmt.setString(2, reward);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (final SQLException ex) {			
			ex.printStackTrace();			
		}
	}
	
	public void newStretchGoalForProject (Integer id, int amount, String reward) {
		try {
			final String sql = "INSERT INTO stretchGoal (amount,reward,pid) values (?,?,?)";
			final java.sql.PreparedStatement stmt = sqlConnection.prepareStatement(sql);
			stmt.setInt(1, amount);
			stmt.setString(2, reward);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (final SQLException ex) {			
			ex.printStackTrace();			
		}
	}

	public void createUser(User user) {
		try {
			final String sql = "insert into user (email, username, pw) values (?,?,?)";
			final java.sql.PreparedStatement stmt = sqlConnection.prepareStatement(sql);
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getUsername());
			stmt.setString(3, user.getPw());
			stmt.executeUpdate();
		} catch (final SQLException ex) {			
			ex.printStackTrace();
		}
	}

	public List<Project> getSupportedProjects(User user) {
		final List<Project> projects = new ArrayList<Project>();		
		//Code example for DB access
		try {
			final Statement stmt = this.sqlConnection.createStatement();
			String query = "SELECT " + fieldsProject + " FROM  " + tablesProject + " " +
					"WHERE " +baseWhereProject +" and f.uid="+user.getId()+" GROUP BY p.id";
			final ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				projects.add(new Project(rs));
			}
			rs.close();
			stmt.close();			
		} catch (final SQLException ex) {			
			ex.printStackTrace();
			return null;			
		}
		return projects;
	}

}

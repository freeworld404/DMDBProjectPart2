package ch.ethz.inf.dbproject;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.Comment;
import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Fund;
import ch.ethz.inf.dbproject.model.FundingLevel;
import ch.ethz.inf.dbproject.model.Project;
import ch.ethz.inf.dbproject.model.StretchGoal;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Project Details Page
 */
@WebServlet(description = "Displays a specific project.", urlPatterns = { "/Project" })
public final class ProjectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProjectServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		final String idString = request.getParameter("id");

		if (idString == null) {
			this.getServletContext().getRequestDispatcher("/Projects").forward(request, response);
		}

		// TEST
		request.setAttribute("id", idString);
		session.setAttribute("id", idString);
		
		try {

			final Integer id = Integer.parseInt(idString);
			
			/***
			 * Check if we new to add a comment
			 */
			final String action = request.getParameter("action");
			if (action != null && action.trim().equals("add_comment")) {
				String username = UserManagement.getCurrentlyLoggedInUser(session).getUsername();
				String comment = request.getParameter("comment");
				Comment commentObj = new Comment(comment,dbInterface.getUserByUsername(username));
				this.dbInterface.addCommentForProject(id, commentObj);
			} else if (action != null && action.equals("fund")) {
				int uid = UserManagement.getCurrentlyLoggedInUser(session).getId();
				int flid = Integer.parseInt(request.getParameter("flid"));
				this.dbInterface.fundProject(uid,flid);
			} else if (action != null && action.equals("newFundingLevel")) {
				int amount = Integer.parseInt(request.getParameter("amount"));
				String reward = request.getParameter("reward");
				this.dbInterface.newFundingLevelForProject(id,amount,reward);
			} else if (action != null && action.equals("newStretchGoal")) {
				int stretchGoalamount = Integer.parseInt(request.getParameter("amount"));
				String stretchGoalreward = request.getParameter("reward");
				this.dbInterface.newStretchGoalForProject(id, stretchGoalamount, stretchGoalreward);
			}
			

			final Project project = this.dbInterface.getProjectById(id);
			
			/*******************************************************
			 * Construct a table to present all properties of a project
			 *******************************************************/
			final BeanTableHelper<Project> table = new BeanTableHelper<Project>(
					"projects" 		/* The table html id property */,
					"projectTable" /* The table html class property */,
					Project.class 	/* The class of the objects (rows) that will be displayed */
			);
			
			// Add columns to the new table

			/*
			 * Column 1: The name of the item (This will probably have to be changed)
			 */
			table.addBeanColumn("Project Name", "title");
			table.addBeanColumn("Description", "description");
			table.addBeanColumn("Funding Progress", "fundProgress");
			table.addBeanColumn("Base Goal", "baseGoal");
			table.addBeanColumn("Creator", "username");
			table.addBeanColumn("Start Date", "startDate");
			table.addBeanColumn("End Date", "endDate");
			table.addBeanColumn("City", "city");
			table.addBeanColumn("Category", "category");

			table.addObject(project);
			table.setVertical(true);			

			session.setAttribute("projectTable", table);	
			
			final BeanTableHelper<StretchGoal> stretchGoalTable = new BeanTableHelper<StretchGoal>(
					"stretchGoals" 		, // The table html id property
					"stretchGoalTable" , // The table html class property 
					StretchGoal.class  // The class of the objects (rows) that will be displayed
			);
			
			stretchGoalTable.addBeanColumn("Stretch Goal", "amount");
			stretchGoalTable.addBeanColumn("Stretch Goal Reward", "reward");
			
			final List<StretchGoal> stretchGoal = this.dbInterface.getStretchGoalByProjectId(id);
			stretchGoalTable.addObjects(stretchGoal);

			
			session.setAttribute("stretchGoalTable", stretchGoalTable);
			
			//Add new Stretch Goals
			if (UserManagement.getCurrentlyLoggedInUser(session) != null) {
				String newStretchGoal = "";
			
				if (UserManagement.getCurrentlyLoggedInUser(session).getId() == dbInterface.getOwnerOfProject(id).getId()) {
					newStretchGoal = "<form action=\"Project\" method=\"get\">";
					newStretchGoal += "<input type=\"hidden\" name=\"action\" value=\"newStretchGoal\" />";
					newStretchGoal += "<input type=\"hidden\" name=\"id\" value=\""+id+"\">";
					newStretchGoal += "<table><tr><th>Stretch Goal:</th><td><input type=\"amount\" name=\"amount\" value=\"1\" /> </td></tr>";
					newStretchGoal += "<tr><th>Reward:</th><td><input type=\"text\" name=\"reward\" value=\"\" /></td></tr>";
					newStretchGoal += "<tr><th colspan=\"2\"><input type=\"submit\" value=\"New Stretch Goal\" /></th></tr></table></form>";
				}
				session.setAttribute("newStretchGoal",newStretchGoal);
			}
			
			//Add new Funding Levels
			if (UserManagement.getCurrentlyLoggedInUser(session) != null) {
				String newFundingLevel = "";
			
				if (UserManagement.getCurrentlyLoggedInUser(session).getId() == dbInterface.getOwnerOfProject(id).getId()) {
					newFundingLevel = "<form action=\"Project\" method=\"get\">";
					newFundingLevel += "<input type=\"hidden\" name=\"action\" value=\"newFundingLevel\" />";
					newFundingLevel += "<input type=\"hidden\" name=\"id\" value=\""+id+"\">";
					newFundingLevel += "<table><tr><th>Amount:</th><td><input type=\"amount\" name=\"amount\" value=\"1\" /> </td></tr>";
					newFundingLevel += "<tr><th>Reward:</th><td><input type=\"text\" name=\"reward\" value=\"\" /></td></tr>";
					newFundingLevel += "<tr><th colspan=\"2\"><input type=\"submit\" value=\"new Funding Level\" /></th></tr></table></form>";
				}
				session.setAttribute("newFundingLevel",newFundingLevel);
			}
			
			/*******************************************************
			 * Construct a table for all payment amounts
			 *******************************************************/
			//TODO implement this
			//List<Amount> payments = this.dbInterface.getAmountsOfProject(id);
			//Create a table to display the amounts the same way as above
			//The table needs to have a link column the allows a registered user to fund that amount
			//We need to catch the click a store the funding in the database
			//session.setAttribute("amountTable", table);
			
			final BeanTableHelper<Fund> fundsTable = new BeanTableHelper<Fund>(
					"funds" 		/* The table html id property */,
					"fundsTable" /* The table html class property */,
					Fund.class 	/* The class of the objects (rows) that will be displayed */
			);

			fundsTable.addBeanColumn("Date", "fundDate");
			fundsTable.addBeanColumn("User", "username");
			fundsTable.addBeanColumn("Amount", "fundingLevelAmount");

			List<Fund> funds = this.dbInterface.getFundsOfProject(id);
			fundsTable.addObjects(funds);

			session.setAttribute("fundsTable",fundsTable);

			final BeanTableHelper<FundingLevel> fundsLevelTable = new BeanTableHelper<FundingLevel>(
					"fundingLevel" 		/* The table html id property */,
					"fundingLevelTable" /* The table html class property */,
					FundingLevel.class 	/* The class of the objects (rows) that will be displayed */
			);

			fundsLevelTable.addBeanColumn("Amount", "amount");
			fundsLevelTable.addBeanColumn("Reward", "reward");
			if ((User) session.getAttribute(UserManagement.SESSION_USER) != null) {
				fundsLevelTable.addLinkColumn("", "fund", "Project?id="+id+"&action=fund&flid=", "id");
			}
			

			List<FundingLevel> fundingLevels = this.dbInterface.getFundingLevelsOfProject(id);
			fundsLevelTable.addObjects(fundingLevels);

			session.setAttribute("fundingLevelTable",fundsLevelTable);
			
			/*******************************************************
			 * Construct a table to present all comments
			 *******************************************************/
			List<Comment> comments = this.dbInterface.getCommentsOfProject(id);
			//Create a table to display the comments the same way as above
			final BeanTableHelper<Comment> commentsTable = new BeanTableHelper<Comment>(
					"comments" 		/* The table html id property */,
					"commentTable" /* The table html class property */,
					Comment.class 	/* The class of the objects (rows) that will be displayed */
			);

			// Add columns to the new table
			commentsTable.addBeanColumn("User", "username");
			commentsTable.addBeanColumn("", "content");
			commentsTable.addObjects(comments);
			session.setAttribute("commentTable", commentsTable);
			
		} catch (final Exception ex) {
			ex.printStackTrace();
			this.getServletContext().getRequestDispatcher("/Projects.jsp").forward(request, response);
		}

		this.getServletContext().getRequestDispatcher("/Project.jsp").forward(request, response);
	}
}
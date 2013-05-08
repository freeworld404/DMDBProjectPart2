package ch.ethz.inf.dbproject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.ethz.inf.dbproject.model.DatastoreInterface;
import ch.ethz.inf.dbproject.model.Project;
import ch.ethz.inf.dbproject.model.User;
import ch.ethz.inf.dbproject.util.UserManagement;
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

@WebServlet(description = "Page that displays the user login / logout options.", urlPatterns = { "/User" })
public final class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	public final static String SESSION_USER_LOGGED_IN = "userLoggedIn";
	public final static String SESSION_USER_DETAILS = "userDetails";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		final HttpSession session = request.getSession(true);
		User loggedUser = UserManagement.getCurrentlyLoggedInUser(session);

		// TODO display registration

		final String action = request.getParameter("action");
		if (action != null && action.trim().equals("login") && loggedUser == null) {

			final String username = request.getParameter("username");
			// Note: It is really not safe to use HTML get method to send passwords.
			// However for this project, security is not a requirement.
			final String password = request.getParameter("password");

			User login_user = dbInterface.getUserByUsername(username);
			
			if (login_user != null && login_user.getPw().equals(password)) {
				loggedUser = login_user;
				session.setAttribute("LOGGED_IN_USER", login_user);
				session.setAttribute(SESSION_USER_DETAILS, username);
			}

		}
		else if (action != null && action.trim().equals("createProject") && loggedUser != null) {
			//Create a project
			final String title = request.getParameter("title");
			final String description = request.getParameter("description");
			final String baseGoal = request.getParameter("baseGoal");
			final String stretchGoal = request.getParameter("stretchGoal");
			final String stretchReward = request.getParameter("stretchReward");
			final String startDay = request.getParameter("startDay");
			final String startMonth = request.getParameter("startMonth");
			final String startYear = request.getParameter("startYear");
			final String endDay = request.getParameter("endDay");
			final String endMonth = request.getParameter("endMonth");
			final String endYear = request.getParameter("endYear");
			final String city = request.getParameter("city");
			final String categoryId = request.getParameter("categoryId");
			final int userId = loggedUser.getId();
			
			dbInterface.createProject(userId, title, description, baseGoal, stretchGoal, 
					stretchReward, startDay, startMonth, startYear, endDay, endMonth, 
					endYear, city, categoryId);
		} else if (action != null && action.trim().equals("register") && loggedUser == null) {
			final String username = request.getParameter("regusername");
			// Note: It is really not safe to use HTML get method to send passwords.
			// However for this project, security is not a requirement.
			final String password = request.getParameter("regpassword");
			final String email = request.getParameter("regemail");
			User user = dbInterface.getUserByUsername(username);
			if (user == null) {
				User newuser = new User(0, username, email, password);
				dbInterface.createUser(newuser);
			}
		}

		
		
		if (loggedUser == null) {
			// Not logged in!
			session.setAttribute(SESSION_USER_LOGGED_IN, false);
		} else {
			// Logged in
			final BeanTableHelper<User> userDetails = new BeanTableHelper<User>("userDetails", "userDetails", User.class);
			userDetails.addBeanColumn("Username", "username");
			userDetails.addBeanColumn("E-Mail", "email");
			userDetails.addObject(loggedUser);
			session.setAttribute(SESSION_USER_LOGGED_IN, true);
			session.setAttribute(SESSION_USER_DETAILS, userDetails);
			final BeanTableHelper<Project> supportedProjectsTable = new BeanTableHelper<Project>("supportedProjectsTable", "supportedProjectsTable", Project.class);
			supportedProjectsTable.addBeanColumn("Project Name", "title");
			supportedProjectsTable.addBeanColumn("Description","shortDescription");
			supportedProjectsTable.addBeanColumn("Base Goal", "baseGoal");
			supportedProjectsTable.addBeanColumn("Creator", "username");
			supportedProjectsTable.addBeanColumn("End Date", "endDate");
			supportedProjectsTable.addLinkColumn(""	/* The header. We will leave it empty */,
					"View Project" 	/* What should be displayed in every row */,
					"Project?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
					"id" 			/* For every project displayed, the ID will be retrieved and will be attached to the url base above */);

			supportedProjectsTable.addObjects(dbInterface.getSupportedProjects(loggedUser));
			session.setAttribute("supportedProjectsTable", supportedProjectsTable);
		}

// Finally, proceed to the User.jsp page which will renden the profile
		this.getServletContext().getRequestDispatcher("/User.jsp").forward(request, response);

	}

}

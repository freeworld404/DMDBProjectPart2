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
import ch.ethz.inf.dbproject.util.html.BeanTableHelper;

/**
 * Servlet implementation class of Project list page
 */
@WebServlet(description = "Page that displays the projects based on different filtering criteria.", urlPatterns = { "/Projects" })
public final class ProjectsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final DatastoreInterface dbInterface = new DatastoreInterface();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProjectsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected final void doGet(final HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {

		final HttpSession session = request.getSession(true);

		/*******************************************************
		 * Construct a table to present all our results
		 *******************************************************/
		final BeanTableHelper<Project> table = new BeanTableHelper<Project>(
				"projects" 		/* The table html id property */,
				"projectsTable" /* The table html class property */,
				Project.class 	/* The class of the objects (rows) that will bedisplayed */
		);

		// Add columns to the new table

		/*
		 * Column 1: The name of the item (This will probably have to be changed)
		 */
		table.addBeanColumn("Project Name", "title");
		table.addBeanColumn("Description","shortDescription");
		table.addBeanColumn("Funding Progres", "fundProgress");
		table.addBeanColumn("Base Goal", "baseGoal");
		table.addBeanColumn("Creator", "username");
		table.addBeanColumn("End Date", "endDate");

		/*
		 * Column 4: This is a special column. It adds a link to view the
		 * Project. We need to pass the project identifier to the url.
		 */
		table.addLinkColumn(""	/* The header. We will leave it empty */,
				"View Project" 	/* What should be displayed in every row */,
				"Project?id=" 	/* This is the base url. The final url will be composed from the concatenation of this and the parameter below */, 
				"id" 			/* For every project displayed, the ID will be retrieved and will be attached to the url base above */);

		// Pass the table to the session. This will allow the respective jsp page to display the table.
		session.setAttribute("projects", table);

		// The filter parameter defines what to show on the Projects page
		final String filter = request.getParameter("filter");
		final String category = request.getParameter("category");
		if (filter == null && category == null) {
			table.addObjects(this.dbInterface.getAllProjects());
		} else if (category != null) {
			table.addObjects(this.dbInterface.getProjectsByCategory(category));
		} else if (filter != null) {		
			if(filter.equals("popular")) {
				table.addObjects(this.dbInterface.getMostPopularProjects());
			} else if (filter.equals("funded")) {
				table.addObjects(this.dbInterface.getMostFundedProjects());
			} else if (filter.equals("ending")) {
				table.addObjects(this.dbInterface.getSoonEndingProjects());
			}
		} else {
			throw new RuntimeException("Code should not be reachable!");
		}

		// Finally, proceed to the Projects.jsp page which will render the Projects
		this.getServletContext().getRequestDispatcher("/Projects.jsp").forward(request, response);
	}
}
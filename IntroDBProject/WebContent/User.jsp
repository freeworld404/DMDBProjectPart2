<%@page import="ch.ethz.inf.dbproject.UserServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h2>Your Account</h2>

<% 
if ((Boolean) session.getAttribute(UserServlet.SESSION_USER_LOGGED_IN)) {
	// User is logged in. Display the details:
%>
	
<%= session.getAttribute(UserServlet.SESSION_USER_DETAILS) %>
<br/>
<h3>Supported Projects</h3>
<%= session.getAttribute("supportedProjectsTable") %>
<h3>Create Project:</h3>
<form action="User" method="get">
<input type="hidden" name="action" value="createProject" />
<table>
	<tr>
		<th>Project Title:</th>
		<td><input type="text" name="title" value="" /></td>
	</tr>
	<tr>
		<th>Description:</th>
		<td><input type="text" name="description" value="" /></td>
	</tr>
	<tr>
		<th>Base Goal:</th>
		<td><input type="text" name="baseGoal" value="" /></td>
	</tr>
	<tr>
		<th>Stretch Goal:</th>
		<td><input type="text" name="stretchGoal" value="" /></td>
	</tr>
	<tr>
		<th>Stretch Reward:</th>
		<td><input type="text" name="stretchReward" value="" /></td>
	</tr>
	<tr>
	<th>Start Date:</th>
		<td>
			<input type="text" name="startDay" value="DD" size="2" maxlength="2" />
			<input type="text" name="startMonth" value="MM" size="2" maxlength="2"/>
			<input type="text" name="startYear" value="YYYY" size="4" maxlength="4"/>
		</td>
	</tr>
	<tr>
	<th>End Date:</th>
		<td>
			<input type="text" name="endDay" value="DD" size="2" maxlength="2" />
			<input type="text" name="endMonth" value="MM" size="2" maxlength="2"/>
			<input type="text" name="endYear" value="YYYY" size="4" maxlength="4"/>
		</td>
	</tr>

	<tr>
	<th>City:</th>
		<td><input type="text" name="city" value="" /></td>
	</tr>
	<tr>
	<th>Category:</th>
		<td>
			<select name="categoryId">
				<option value="1">Super heroes</option>
				<option value="2">Super villains</option>
				<option value="3">Tech</option>
			</select>
		</td>
	</tr>
	<tr>
		<th colspan="2">
			<input type="submit" value="Create Project" />
		</th>
	</tr>
</table>
</form>

<%
//TODO: Display funded projects
session.getAttribute("fundedProjectsTable");
//TODO: Add possibility to create new projects (requires a form) 
//session.getAttribute("createProjectTable");

} else {
	// User not logged in. Display the login form.
%>

	<form action="User" method="get">
	<input type="hidden" name="action" value="login" />
	<table>
		<tr>
			<th>Username</th>
			<td><input type="text" name="username" value="" /></td>
		</tr>
		<tr>
			<th>Password</th>
			<td><input type="password" name="password" value="" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input type="submit" value="Login" />
			</th>
		</tr>
	</table>
	</form>
	
	<form action="User" method="get">
	<input type="hidden" name="action" value="register" />
	<table>
		<tr>
			<th>Username</th>
			<td><input type="text" name="regusername" value="" /></td>
		</tr>
		<tr>
			<th>E-Mail</th>
			<td><input type="email" name="regemail"/></td>
		</tr>
		<tr>
			<th>Password</th>
			<td><input type="password" name="regpassword" value="" /></td>
		</tr>
		<tr>
			<th colspan="2">
				<input type="submit" value="Register" />
			</th>
		</tr>
	</table>
	</form>
	
<%
}
%>

<%@ include file="Footer.jsp" %>
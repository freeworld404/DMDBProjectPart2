<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="Header.jsp"%>
<% final User user = (User) session.getAttribute(UserManagement.SESSION_USER); %>

<h1>Project Detail</h1>

<table>
  <tr>
    <td><%=session.getAttribute("projectTable")%></td>
    <% if (user != null) { %>
    <td><%=session.getAttribute("newFundingLevel")%></td>
    <td><%=session.getAttribute("newStretchGoal")%></td>
    <%} %>
  </tr>
</table>

<h2>Stretch Goals &amp; rewards</h2>
<%=session.getAttribute("stretchGoalTable")%>

<h2>Help us and become a part of The Awesomes!</h2>

<%=session.getAttribute("fundingLevelTable")%>

<h2>Funders (aka. The Awesomes)</h2>

<%=session.getAttribute("fundsTable")%>


<%
if (user != null) {
	// User is logged in. He can add a comment
%>	
	<br/>
	<form action="Project" method="get">
		<input type="hidden" name="action" value="add_comment" />
		<input type="hidden" name="user_id" value="<%= user.getId() %>" />
		<input type="hidden" name="id" value="<%= session.getAttribute("id") %>" />
		Add Comment
		<br />
		<textarea rows="4" cols="50" name="comment"></textarea>
		<br />
		<input type="submit" value="Submit" />
	</form>
<%
}
%>

<h2>Comments</h2>

<%=session.getAttribute("commentTable")%>

<%@ include file="Footer.jsp"%>
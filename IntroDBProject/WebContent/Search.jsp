<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Search</h1>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="name" />
	Search By Name:
	<input type="text" name="name" />
	<input type="submit" value="Search" title="Search by Name" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="category" />
	Search By Category:
	<input type="text" name="category" />
	<input type="submit" value="Search" title="Search by Category" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="city" />
	Search By City:
	<input type="text" name="city" />
	<input type="submit" value="Search" title="Search by City" />
</div>
</form>

<hr/>
<%=session.getAttribute("results")%>

<hr/>

<%@ include file="Footer.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Projects</h1>

<hr/>

<%= session.getAttribute("projects") %>

<hr/>

<%@ include file="Footer.jsp" %>
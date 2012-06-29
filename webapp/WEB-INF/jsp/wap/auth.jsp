<%@ page import="java.util.Enumeration,
                 org.apache.log4j.Logger"%><?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<wml>
<card id="top" title="Login">
<%= request.getCookies()[0].getName() %>
Auth: <%= request.getParameter("userId") %> <%= request.getParameter("password") %>
<% response.addCookie(new Cookie("steve", "bate")); %>
</card>
</wml>
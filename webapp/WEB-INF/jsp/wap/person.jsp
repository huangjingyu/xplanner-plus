<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%@ page import="java.text.NumberFormat,
                 java.text.DecimalFormat"%>
<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<db:useBean id="person" type="net.sf.xplanner.domain.Person"
	oid='<%= new Integer(request.getParameter("oid")) %>'/>
<wml>
<card id="details" title="Person Details">
<do type="accept" label="Back">
  <prev/>
</do>
<do type="accept" label="All Projects">
  <go href="/xplanner/do/mobile/view/projects"/>
</do>
<p><small>
<b>Name:</b> <bean:write name="person" property="name"/><br/>
<b>Initials:</b> <bean:write name="person" property="initials"/><br/>
<logic:notEmpty name="person" property="phone">
<b>Phone:</b> <a href="wtai://wp/mc;<%= person.getPhone().replaceAll("-","") %>"><bean:write name="person" property="phone"/></a><br/>
</logic:notEmpty>
<logic:notEmpty name="person" property="email">
<b>Email:</b> <bean:write name="person" property="email"/><br/>
</logic:notEmpty>
</small>
</p>
</card>
</wml>
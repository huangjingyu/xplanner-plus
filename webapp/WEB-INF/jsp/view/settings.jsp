<%@page import="net.sf.xplanner.domain.Setting"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.security.auth.SystemAuthorizer,
                 com.technoetic.xplanner.security.SecurityHelper,
                 java.util.Date,
                 org.apache.commons.collections.CollectionUtils,
                 org.apache.commons.collections.Predicate,
                 net.sf.xplanner.domain.Iteration,
                 net.sf.xplanner.domain.Project,
                 com.technoetic.xplanner.security.AuthenticationException,
                 org.apache.struts.Globals,
                 com.technoetic.xplanner.tags.displaytag.RowDecorator,
                 com.technoetic.xplanner.tags.displaytag.Row,
                 org.apache.commons.beanutils.PropertyUtils"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>

<xplanner:content>
<xplanner:contentTitle titleKey="projects.title"/>

<db:useBeans id="settings" type="net.sf.xplanner.domain.Setting" />
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>

<span class="title">
<h1>Settings
<bean:message key="projects.title"/>
</h1>
</span>
${settings}

<%--
<bean:size id="projectCount" name="projects"/>
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
 --%>  
 <xplanner:writableTable wholeCollection='${settings}' permissions="admin.edit,sysadmin.delete"
    name="pageScope.settings" id="objecttable" styleClass="objecttable" requestURI="">
    <% final Setting setting = (Setting)objecttable; pageContext.setAttribute("setting", objecttable); %>
  <dt:column title='<%= messages.getMessage("objects.tableheading.id") %>'>
  <xplanner:link href="setting" paramId="oid" paramName="setting" paramProperty="id">
    <bean:write name="setting" property="id"/></xplanner:link></dt:column>
  <dt:column title='<%= messages.getMessage("projects.tableheading.name") %>' sortable="true" sortProperty="name">
    <xplanner:link styleClass="name"  href="setting" paramId="oid" paramName="setting" paramProperty="id">
    <bean:write name="setting" property="name"/></xplanner:link></dt:column>
</xplanner:writableTable>

<span class="links">
<xplanner:isUserAuthorized resourceType="system.project" permission="create.project">
    <xplanner:link page="/do/edit/setting"><bean:message key="settings.link.add_setting"/></xplanner:link> 
</xplanner:isUserAuthorized>
</span>
<tiles:put name="globalActions" direct="true">
	<xplanner:authenticatedUser id="person"/>
	<xplanner:link page="/do/view/people"><bean:message key="projects.link.people"/></xplanner:link> 
	<xplanner:link page="/do/view/aggregateTimesheet"><bean:message key="projects.link.aggregate.timesheet"/></xplanner:link>
</tiles:put>

</xplanner:content>

<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.security.SecurityHelper,
                 com.technoetic.xplanner.security.auth.SystemAuthorizer" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<xplanner:content definition="viewLayout">
<bean:define id="person" name="person" scope="request" type="net.sf.xplanner.domain.Person" />
<db:useBeans id="projects" type="net.sf.xplanner.domain.Project" where="hidden = false"/>

<xplanner:contentTitle title="XPlanner {0} {1}">
    <xplanner:contentTitleArg><bean:message key="person.prefix"/></xplanner:contentTitleArg>
    <xplanner:contentTitleArg>${person.name}</xplanner:contentTitleArg>
</xplanner:contentTitle>

<span class="title"><h1><bean:message key="person.label.name"/> <a href="">${person.name}</a> </h1></span>

<%-- Active Tasks --%>
<jsp:include page="personTaskTableFragment.jsp">
    <jsp:param name="collectionName" value="currentActiveTasksForPerson" />
    <jsp:param name="label" value="person.label.active_tasks" />
    <jsp:param name="label.none" value="person.label.active_tasks.none" />
</jsp:include>

<%-- Planned Tasks --%> 
<jsp:include page="personTaskTableFragment.jsp">
    <jsp:param name="collectionName" value="currentPendingTasksForPerson" />
    <jsp:param name="label" value="person.label.planned_tasks" />
    <jsp:param name="label.none" value="person.label.planned_tasks.none" />
</jsp:include>

<%-- Completed Tasks --%> 
<jsp:include page="personTaskTableFragment.jsp">
    <jsp:param name="collectionName" value="currentCompletedTasksForPerson" />
    <jsp:param name="label" value="person.label.completed_tasks" />
    <jsp:param name="label.none" value="person.label.completed_tasks.none" />
</jsp:include>

<%-- Customer Stories --%> 
<jsp:include page="personStoryTableFragment.jsp">
    <jsp:param name="collectionName" value="storiesForCustomer" />
    <jsp:param name="label" value="person.label.stories.customer" />
    <jsp:param name="label.none" value="person.label.stories.customer.none" />
</jsp:include>

<%-- Tracker Stories --%>
<jsp:include page="personStoryTableFragment.jsp">
    <jsp:param name="collectionName" value="storiesForTracker" />
    <jsp:param name="label" value="person.label.stories.tracker" />
    <jsp:param name="label.none" value="person.label.stories.tracker.none" />
</jsp:include>

<%-- Future Tasks --%>
<jsp:include page="personTaskTableFragment.jsp">
    <jsp:param name="collectionName" value="futureTasksForPerson" />
    <jsp:param name="label" value="person.label.future_tasks" />
    <jsp:param name="label.none" value="person.label.future_tasks.none" />
</jsp:include>
</xplanner:content>
<%-- 

<xplanner:contentTitle title="XPlanner {0} {1}">
    <xplanner:contentTitleArg><bean:message key="person.prefix"/></xplanner:contentTitleArg>
    <xplanner:contentTitleArg>${person.name}</xplanner:contentTitleArg>
</xplanner:contentTitle>

<span class="links">
<xplanner:actionButtons id="action" name="person">
<%
  final int remoteUserId = SecurityHelper.getRemoteUserId(request);
  if (remoteUserId == person.getId() ||
      SystemAuthorizer.get().hasPermissionForSomeProject(projects,
          remoteUserId, "system.person", person.getId(), "admin.edit")) {
%>
                        <xplanner:link page='<%=action.getTargetPage()%>' paramId="oid"
                            paramName="person" paramProperty="id" includeProjectId="false">
                            <bean:message key='<%=action.getTitleKey()%>'/>
                        </xplanner:link> 
                    <% } %>
</xplanner:actionButtons>
<xplanner:link page="/do/view/people"><bean:message key="projects.link.people"/></xplanner:link> 
<xplanner:link page="/do/view/timesheet" paramId="oid" paramName="person" paramProperty="id">
<bean:message key="person.link.timesheet"/></xplanner:link>
 <jsp:include page="exportLinks.jsp"/>
</span>

<xplanner:content>
    <db:useBean id="person" type="net.sf.xplanner.domain.Person" />
    <db:useBeans id="projects" type="net.sf.xplanner.domain.Project" where="hidden = false"/>
    <xplanner:contentTitle title="XPlanner {0} {1}">
        <xplanner:contentTitleArg><bean:message key="person.prefix"/></xplanner:contentTitleArg>
        <xplanner:contentTitleArg>${person.name}</xplanner:contentTitleArg>
    </xplanner:contentTitle>

<span class="title"><h1><bean:message key="person.label.name"/> <a href="">${person.name}</a> </h1></span>














</xplanner:content>
 --%>
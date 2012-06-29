<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.security.SecurityHelper,
                 com.technoetic.xplanner.security.auth.SystemAuthorizer,
                 org.apache.struts.Globals,
                 net.sf.xplanner.domain.Person,
                 java.util.*"%>
<%@ page import="com.technoetic.xplanner.security.auth.PermissionHelper"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>


<xplanner:content>
<xplanner:contentTitle titleKey="people.title"/>

<script src="<html:rewrite page="/js/tooltip.js"/>" type="text/javascript"></script>

<%--TODO remove duplication of projectId extraction--%>
<bean:parameter id="projectId" name="projectId" value="0"/>

<xplanner:isUserAuthorized permission="admin.hide" resourceType="system.person"  >
    <db:useBeans id="people" type="net.sf.xplanner.domain.Person" order="is_hidden,name asc"/>
</xplanner:isUserAuthorized>
<xplanner:isUserAuthorized permission="admin.hide" negate="true" resourceType="system.person">
    <db:useBeans id="people" type="net.sf.xplanner.domain.Person"
        where="hidden = false" order="name"/>
</xplanner:isUserAuthorized>
<db:useBeans id="projects" type="net.sf.xplanner.domain.Project" where="hidden = false"/>

<logic:greaterThan name="projectId" value="0" >
    <db:useBean id="project" type="net.sf.xplanner.domain.Project"
        oid='<%= new Integer(projectId) %>'/>
    <p class="title"><bean:message key="people.project.title" arg0='<%= project.getName() %>' /></p>
</logic:greaterThan>
<logic:equal name="projectId" value="0" >
    <p class="title"><h1><bean:message key="people.title"/></h1></p>
</logic:equal>


<p>
<bean:size id="peopleCount" name="people"/>
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>

<%
  Collection people = (Collection) pageContext.getAttribute("people");
  Collection peopleToShow = PermissionHelper.getPeopleWithProjectRole(projectId, people);
  request.setAttribute("peopleToShow", peopleToShow);
%>

  <logic:greaterThan name="peopleCount" value="0">
<%--<dt:table name="pageScope.people" id="objecttable" styleClass="objecttable" requestURI="">--%>
<xplanner:writableTable name="peopleToShow" id="objecttable" styleClass="objecttable" requestURI="" defaultsort="3" defaultorder="ascending"  >
  <dt:setProperty name="paging.banner.placement" value="bottom" />

  <% final Person person = (Person)objecttable; pageContext.setAttribute("person", person);
  %>
 <%--               <%
                     final int remoteUserId = SecurityHelper.getRemoteUserId(request);
                     int projectOid = Integer.parseInt(projectId);
                     boolean isAuthorized = SystemAuthorizer.get().hasPermission(
                             projectOid, person.getId(), "system.project", projectOid, "read%");
                     if (projectOid == 0 || isAuthorized) {
                 %>
--%>
<xplanner:actionButtonsColumn id="action" name="person" title='<%= messages.getMessage("people.tableheading.actions") %>'>
<%
  // DEBT replace with the isUserAuthorizedTag
  if (SecurityHelper.getRemoteUserId(request) == person.getId() ||
      SystemAuthorizer.get().hasPermissionForSomeProject(projects,
          SecurityHelper.getRemoteUserId(request), "system.person", person.getId(), "admin.edit")) {
%>
                        <xplanner:link styleClass="image" page='<%=action.getTargetPage()%>' paramId="oid"
                            paramName="person" paramProperty="id" includeProjectId="false">
                            <html:img styleClass="${action.name}" page='<%=action.getIconPath()%>' alt='<%=action.getName()%>' border="0"/>
                        </xplanner:link>
                    <% } %>
                    &nbsp;
</xplanner:actionButtonsColumn>
  <dt:column sortable="true" sortProperty="userId" title='<%= messages.getMessage("people.tableheading.userid") %>'>
    <xplanner:link page="/do/view/person" paramId="oid" paramName="person" paramProperty="id">
    ${person.userId}</xplanner:link>
  </dt:column>
  <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("people.tableheading.name") %>'>
    <xplanner:link page="/do/view/person" paramId="oid" paramName="person" paramProperty="id">
    ${person.name}</xplanner:link>
  </dt:column>
  <dt:column sortable="true" sortProperty="initials" title='<%= messages.getMessage("people.tableheading.initials") %>'>
    <p>${person.initials}</p>
  </dt:column>
  <dt:column title='<%= messages.getMessage("people.tableheading.phone") %>'>
    <p>${person.phone}</p>
  </dt:column>
  <dt:column sortable="true" sortProperty="email" title='<%= messages.getMessage("people.tableheading.email") %>'>
    <a href="mailto:${person.email}">${person.email}</a>
  </dt:column>
          <%--      <%
                     }
                %>--%>
</xplanner:writableTable>
</logic:greaterThan>
</p>
<span class="links">
<xplanner:isUserAuthorized resourceType="system.person" permission="create.person">
    <xplanner:link page="/do/edit/person" includeProjectId="false" >
        <bean:message key="people.link.add_person"/>
    </xplanner:link>  <xplanner:link page="/do/import/people" includeProjectId="false" >
        <bean:message key="people.link.import_people"/>
    </xplanner:link>
</xplanner:isUserAuthorized>
</span>

	<script>
		tooltip("<%= messages.getMessage("action.edit.person")%>", "edit", $('img'));
	</script>

</xplanner:content>


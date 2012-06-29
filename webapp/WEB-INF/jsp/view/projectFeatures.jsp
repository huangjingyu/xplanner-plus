<%@page import="org.hibernate.Hibernate"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.format.*,java.util.*,
                 com.technoetic.xplanner.actions.AbstractAction,
                 org.apache.commons.lang.StringEscapeUtils,
                 com.technoetic.xplanner.util.StringUtilities" %>
<%@ page import="com.technoetic.xplanner.views.IterationStoriesPage"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<db:useBean id="project" type="net.sf.xplanner.domain.Project" scope="request" />
<xplanner:content definition="tiles:view" >
    <tiles:put name="beanName">project</tiles:put>
    <tiles:put name="titlePrefix">project.allfeatures.prefix</tiles:put>

    <logic:notEmpty name="project" property="description">
        <div class="description">
            <xplanner:twiki name="project" property="description"/>
        </div>
    </logic:notEmpty>


<bean:size id="iterationCount" name="project" property="iterations"/>

<logic:greaterThan name="iterationCount" value="0">
<p>
<table id="objecttable" class="objecttable" cellspacing="2">
<tr>
<th class="objecttable"><bean:message key="person.tableheading.iteration"/></th>
<th class="objecttable"><bean:message key="person.tableheading.story"/></th>
<th class="objecttable"><bean:message key='<%=IterationStoriesPage.STORIES_PRIORITY_COLUMN%>'/></th>
<th class="objecttable"><bean:message key="person.tableheading.feature"/></th>
<%-- This should really be at the feature level, but it's too messy --%>
<xplanner:isUserAuthorizedForAny name="project" property="iterations" permissions="edit,delete">
    <th class="objecttable"><bean:message key="stories.tableheading.actions"/></th>
</xplanner:isUserAuthorizedForAny>
</tr>
    <logic:iterate name="project" property="iterations" id="iteration" type="net.sf.xplanner.domain.Iteration">
        <logic:iterate name="iteration" property="userStories" id="story" type="net.sf.xplanner.domain.UserStory">
          <logic:iterate name="story" property="features" id="feature" type="net.sf.xplanner.domain.Feature">
             <tr>
             <td><html:link page="/do/view/iteration" paramId="oid" paramName="iteration" paramProperty="id">
                        ${iteration.name}</html:link></td>
             <td><html:link page="/do/view/userstory" paramId="oid" paramName="story" paramProperty="id">
                        ${story.name}</html:link></td>
            <td class="objecttable">${story.priority}</td>
            <td><html:link page="/do/view/feature" paramId="oid" paramName="feature" paramProperty="id">
                ${feature.name}</html:link></td>
            <xplanner:isUserAuthorizedForAny name="iteration" property="userStories" permissions="edit,delete">
                <td nowrap class="objecttable" align="center">
                    <xplanner:isUserAuthorized name="feature" permission="edit">
                        <xplanner:link page="/do/edit/feature" paramId="oid" paramName="feature" paramProperty="id" fkey='<%= story.getId() %>'>
                            <html:img page="/images/edit.gif" alt="edit" border="0"/></xplanner:link>
                    </xplanner:isUserAuthorized>
                    <xplanner:isUserAuthorized name="feature" permission="edit">
                          <xplanner:link page="/do/delete/feature" paramId="oid"
                                paramName="feature" paramProperty="id" onclick='<%="return confirm('Do you want to delete feature \\\\'" + StringUtilities.replaceQuotationMarks(StringEscapeUtils.escapeJavaScript(feature.getName())) + "\\\\'?')"%>'>
                            <html:img page="/images/delete.gif" alt="delete" border="0"/></xplanner:link>
                    </xplanner:isUserAuthorized>
                </td>
            </xplanner:isUserAuthorizedForAny>
            </tr>
          </logic:iterate>
        </logic:iterate>
      </logic:iterate>
</table>
</p>
</logic:greaterThan>

<logic:equal name="storyCount" value="0">
<p class="highlighted_message"><bean:message key="stories.none"/></p>
</logic:equal>

<tiles:put name="actions" direct="true" >
<xplanner:isUserAuthorized name="project" permission="admin.edit" >
	<xplanner:link page="/do/edit/project" paramId="oid" paramName="project" paramProperty="id">
	    <bean:message key="project.link.edit"/></xplanner:link> |
</xplanner:isUserAuthorized>
<xplanner:isUserAuthorized resourceType="system.project.iteration" permission="create" >
    <xplanner:link page="/do/edit/iteration">
        <bean:message key="project.link.create_iteration"/></xplanner:link> |
</xplanner:isUserAuthorized>
<xplanner:link page="/do/view/project/features" paramId="oid" paramName="project" paramProperty="id">
  <bean:message key="iteration.link.all_features"/>
</xplanner:link> |
<xplanner:isUserAuthorized resourceType="system.person" permission="read">
    <xplanner:link page="/do/view/people"><bean:message key="projects.link.people"/></xplanner:link> |
</xplanner:isUserAuthorized>
<jsp:include page="exportLinks.jsp"/> |
 <xplanner:link page="/do/view/history">
    <bean:message key="history.link"/>
    <xplanner:linkParam id="oid" name="project" property="id"/>
    <xplanner:linkParam id='<%= AbstractAction.TYPE_KEY %>' value='<%=Hibernate.getClass(project).getName() %>'/>
    <xplanner:linkParam id="container" value="true"/>
 </xplanner:link>

</tiles:put>

</xplanner:content>

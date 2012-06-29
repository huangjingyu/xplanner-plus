<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.format.*,java.util.*,
                 org.apache.commons.lang.StringEscapeUtils,
                 com.technoetic.xplanner.util.StringUtilities" %>
<%@ page import="com.technoetic.xplanner.views.IterationStoriesPage"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<db:useBean id="iteration" type="net.sf.xplanner.domain.Iteration" scope="request" />
<xplanner:content definition="tiles:view" >
<%@include file="../layout/iterationTileParams.jsp" %>

<tiles:put name="titlePrefix">iteration.allfeatures.prefix</tiles:put>

<bean:size id="storyCount" name="iteration" property="userStories"/>
<bean:define id="stories" name="iteration" property="userStories"/>

<logic:greaterThan name="storyCount" value="0">
<p>
<div class="table_title"><bean:message key="iteration.label.hours"/>
    <bean:message key="iteration.label.estimated"/>
        <%= DecimalFormat.format(pageContext, iteration.getEstimatedHours()) %>,
    <bean:message key="iteration.label.actual"/>
        <%= DecimalFormat.format(pageContext, iteration.getCachedActualHours()) %>,
    <bean:message key="iteration.label.remaining"/>
        <%= DecimalFormat.format(pageContext, iteration.getTaskRemainingHours()) %>
</div><br/>
<table id="objecttable" class="objecttable" cellspacing="2">
<tr>
<th class="objecttable"><bean:message key="person.tableheading.story"/></th>
<th class="objecttable"><bean:message key='<%=IterationStoriesPage.STORIES_PRIORITY_COLUMN%>'/></th>
<th class="objecttable"><bean:message key="person.tableheading.feature"/></th>
<%-- This should really be at the feature level, but it's too messy --%>
<xplanner:isUserAuthorizedForAny name="iteration" property="userStories" permissions="edit,delete">
    <th class="objecttable"><bean:message key="stories.tableheading.actions"/></th>
</xplanner:isUserAuthorizedForAny>
</tr>
	<logic:iterate name="iteration" property="userStories" id="story" type="net.sf.xplanner.domain.UserStory">
	  <logic:iterate name="story" property="features" id="feature" type="com.technoetic.xplanner.domain.Feature">
         <tr>
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
</table>
</p>
</logic:greaterThan>

<logic:equal name="storyCount" value="0">
<p class="highlighted_message"><bean:message key="stories.none"/></p>
</logic:equal>

</xplanner:content>

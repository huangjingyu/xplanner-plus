<%@page import="org.hibernate.Hibernate"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
 <%@ page import="com.technoetic.xplanner.format.*,
                  com.technoetic.xplanner.actions.AbstractAction,
                  org.apache.struts.Globals,
                  com.technoetic.xplanner.XPlannerProperties" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>

<db:useBean id="story" type="net.sf.xplanner.domain.UserStory" scope="request" />

<%
    double visualEstimation = (story.getCachedActualHours() == 0.0 && story.isCompleted()) ?
        0.0 : story.getTaskBasedRemainingHours() + story.getCachedActualHours();
%>
<xplanner:content definition="tiles:view">
<script src="<html:rewrite page="/js/tooltip.js"/>" type="text/javascript"></script>
<tiles:put name="beanName">story</tiles:put>
<tiles:put name="titlePrefix">story.prefix</tiles:put>
<tiles:put name="showTitle">false</tiles:put>

<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
<tiles:put name="progress" direct="true">
<div class="iterationCard">
  <div class="iterationCardHeader">
    <strong><bean:message key="story.prefix"/>
			   <span class="object_name"><%=story.getName()%>
               [id=<%=story.getId()%>]</span>
		</strong>
			<xplanner:progressBar
        actual='<%= story.getCachedActualHours() %>'
        estimate='<%= visualEstimation %>'
        complete='<%= story.isCompleted() %>'
        width="150" height="16"/>
	</div>
	<logic:notEmpty name="story" property="description">
		<div class="description">
	    <xplanner:twiki name="story" property="description"/>
	  </div>
	  <hr class="delimeter"/>
  </logic:notEmpty>
  <table class="info">
    <tr>
      <th>Priority:</th>
      <td>
        <bean:write name="story" property="priority"/>
      </td>
      <th nowrap><bean:message key="story.label.estimated_hours"/> </th>
      <td><%= DecimalFormat.format(pageContext, story.getEstimatedHours()) %>
                (<%= DecimalFormat.format(pageContext, story.getTaskBasedEstimatedOriginalHours()) %>)</td>
    </tr>
    <tr>
	    <logic:notEmpty name="story" property="customer">
      <th>
		    <bean:message key="story.label.customer"/>
		  </th>
		  <td>
        <xplanner:link page="/do/view/person" paramId="oid" paramName="story" paramProperty="customer.id">
				<bean:write name="story" property="customer.name"/></xplanner:link>
		  </td>
	    </logic:notEmpty>
		  <logic:empty name="story" property="customer">
		    <td colspan="2">&nbsp;</td>
		  </logic:empty>
		  <th nowrap><bean:message key="story.label.actual_hours"/> </th>
      <td><%= DecimalFormat.format(pageContext, story.getCachedActualHours()) %></td>
    </tr>
    <logic:notEqual name="story" property="trackerId" value="0">
    <tr>
      <th>
        <bean:message key="story.label.tracker"/>
      </th>
      <td>
         <db:useBean id="tracker" type="net.sf.xplanner.domain.Person"
             oid='<%= new Integer(story.getTrackerId()) %>'/>
         <xplanner:link page="/do/view/person" paramId="oid" paramName="tracker" paramProperty="id">
         ${tracker.name}</xplanner:link>
       </td>
       <th nowrap><bean:message key="story.label.remaining_hours"/> </th>
       <td><%= DecimalFormat.format(pageContext, story.getTaskBasedRemainingHours()) %></td>
    </tr>
    </logic:notEqual>
    <tr>
      <th nowrap><bean:message key="story.label.last_update"/></th>
      <td ><%= DateTimeFormat.format(pageContext, story.getLastUpdateTime()) %></td>
      <th nowrap><bean:message key="story.label.disposition"/></th>
      <td><%=messages.getMessage(story.getDisposition().getNameKey())%></td>
    </tr>
		  <%-- Estimated and Actual Hours table --%>
      <tr>
		</tr>
      <tr>
        <th> </th>
        <td> </td>
        <th nowrap><bean:message key="story.label.status"/> </th>
  		  <td><%=messages.getMessage(story.getStatusEnum().getNameKey())%></td>
		</tr>
 </table>
</div>
</tiles:put>


<bean:size id="taskCount" name="story" property="tasks"/>

<logic:greaterThan name="taskCount" value="0">
<xplanner:writableTable  wholeCollection='<%=story.getTasks() %>' permissions="edit,delete"
    id="objecttable" defaultsort="3" defaultorder="ascending" name="requestScope.story" property="tasks" styleClass="objecttable" cellspacing="0"
    requestURI="" decorator="com.technoetic.xplanner.tags.displaytag.TaskDecorator">
    <bean:define id="task" name="objecttable" type="net.sf.xplanner.domain.Task"/>
     <xplanner:actionButtonsColumn
        name="task" id="action" title='<%= messages.getMessage("tasks.tableheading.actions") %>' >
    <xplanner:isUserAuthorized name="task" permission='<%=action.getPermission()%>'>
      <xplanner:link styleClass="image" page='<%=action.getTargetPage()%>' paramId="oid" onclick='<%=action.getOnclick()%>' paramName='<%=action.getDomainType()%>' paramProperty="id">
      <html:img styleClass="${action.name}"  page='<%=action.getIconPath()%>' alt='<%=action.getName()%>' border="0"/></xplanner:link>
  </xplanner:isUserAuthorized>
  </xplanner:actionButtonsColumn>
    <dt:column title='<%= messages.getMessage("objects.tableheading.id") %>'>
        <html:link page="/do/view/task" paramId="oid" paramName="task" paramProperty="id">
	        ${task.id}</html:link>
    </dt:column>
    <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("tasks.tableheading.name")%>'>
        <html:link styleClass="name" page="/do/view/task" paramId="oid" paramName="task" paramProperty="id">
	        ${task.name}</html:link>
    </dt:column>
	<dt:column align="center" sortable="true" title='<%= messages.getMessage("tasks.tableheading.type")%>'>
        ${task.type}
    </dt:column>
    <dt:column sortable="true" sortProperty="percentCompleted" title='<%= messages.getMessage("tasks.tableheading.progress")%>'>
        <xplanner:progressBar
                actual='<%= task.getActualHours() %>'
                estimate='<%= task.getActualHours() + task.getRemainingHours() %>'
                complete='<%= task.isCompleted()%>' height="13"
                width="100%" />
    </dt:column>
    <dt:column sortable="true" sortProperty="acceptorName" title='<%= messages.getMessage("tasks.tableheading.acceptor")%>'>
        <logic:notEqual name="task" property="acceptorId" value="0">
            <db:useBean
                id="acceptor" type="net.sf.xplanner.domain.Person"
                oid='<%= new Integer(task.getAcceptorId()) %>'/>
            <xplanner:link page="/do/view/person" paramId="oid" paramName="acceptor" paramProperty="id">
		        ${acceptor.initials}</xplanner:link>
        </logic:notEqual><logic:equal name="task" property="acceptorId" value="0">&nbsp;</logic:equal>
    </dt:column>
    <dt:column sortable="true" sortProperty="estimatedOriginalHours" title='<%= messages.getMessage("tasks.tableheading.estimate.original") %>' align="right">
        <%= DecimalFormat.format(pageContext, task.getEstimatedOriginalHours()) %>
    </dt:column>
    <dt:column align="right" sortProperty="estimatedHours" sortable="true" title='<%= messages.getMessage("tasks.tableheading.estimated_hours")%>'>
        <%= DecimalFormat.format(pageContext, task.getEstimatedHours()) %>
    </dt:column>
    <%
      if (!"image".equals(new XPlannerProperties().getProperty("xplanner.progressbar.impl"))) { %>
      <dt:column align="right" sortProperty="actualHours" sortable="true" title='<%= messages.getMessage("tasks.tableheading.actual_hours")%>'>
          <%= DecimalFormat.format(pageContext, task.getActualHours()) %>
      </dt:column>
    <%}%>
    <dt:column align="right" sortProperty="remainingHours" sortable="true" title='<%= messages.getMessage("tasks.tableheading.remaining_hours")%>'>
        <%= DecimalFormat.format(pageContext, task.getRemainingHours()) %>
    </dt:column>
    <dt:column align="center" sortable="true" title='<%= messages.getMessage("tasks.tableheading.disposition")%>'>
        <%=messages.getMessage(task.getDispositionNameKey())%>
    </dt:column>
</xplanner:writableTable>
<br/>
</logic:greaterThan>

<logic:equal name="taskCount" value="0">
  <p class="highlighted_message"><bean:message key="story.no_tasks"/></p>
</logic:equal>

<%--    FEATURE:--%>
<%--<bean:size id="featureCount" name="userstory" property="features"/>--%>
<%--<logic:greaterThan name="featureCount" value="0">--%>
<%--<p>--%>
<%--<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>--%>
<%--<dt:table id="objecttable" defaultsort="1" defaultorder="ascending" name="requestScope.userstory" property="features" styleClass="objecttable" cellspacing="0"--%>
<%--    requestURI="" decorator="com.technoetic.xplanner.tags.displaytag.TaskDecorator">--%>
<%--    <bean:define id="feature" name="objecttable" type="com.technoetic.xplanner.domain.Feature"/>--%>
<%--    <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("features.tableheading.name")%>'>--%>
<%--        <html:link page="/do/view/feature" paramId="oid" paramName="feature" paramProperty="id">--%>
<%--	        ${feature.name}</html:link>--%>
<%--    </dt:column>--%>
<%--    <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("features.tableheading.description")%>'>--%>
<%--        <html:link page="/do/view/feature" paramId="oid" paramName="feature" paramProperty="id">--%>
<%--	        ${feature.description}</html:link>--%>
<%--    </dt:column>--%>
<%--	<xplanner:isUserAuthorizedForAny name="userstory" property="features" permissions="edit,delete" >--%>
<%--        <dt:column title='<%= messages.getMessage("features.tableheading.action")%>'>--%>
<%--            &nbsp;--%>
<%--            <xplanner:isUserAuthorized name="feature" permission="edit" >--%>
<%--                <xplanner:link page="/do/edit/feature" paramId="oid" paramName="feature" paramProperty="id">--%>
<%--                    <html:img page="/images/edit.gif" alt="edit" border="0"/></xplanner:link>--%>
<%--            </xplanner:isUserAuthorized>--%>
<%--            <xplanner:isUserAuthorized name="feature" permission="delete" >--%>
<%--                <xplanner:link page="/do/delete/feature" paramId="oid"--%>
<%--                        paramName="feature" paramProperty="id" onclick='<%="return confirm('Do you want to delete feature \\\\'" + StringUtilities.replaceQuotationMarks(StringEscapeUtils.escapeJavaScript(feature.getName())) + "\\\\'?')"%>'>--%>
<%--                    <html:img page="/images/delete.gif" alt="delete" border="0"/></xplanner:link>--%>
<%--            </xplanner:isUserAuthorized>--%>
<%--            &nbsp;--%>
<%--        </dt:column>--%>
<%--    </xplanner:isUserAuthorizedForAny>--%>
<%--</dt:table>--%>
<%--</p>--%>
<%--</logic:greaterThan>--%>
<%--<logic:equal name="featureCount" value="0">--%>
<%--  <p class="highlighted_message"><bean:message key="story.no_features"/></p>--%>
<%--</blockquote>--%>
<%--</logic:equal>--%>
<!-- /features -->

<tiles:put name="actions" direct="true">

<xplanner:actionButtons
        name="story" id="action">
    <xplanner:isUserAuthorized name="story" permission='<%=action.getPermission()%>'>
      <xplanner:link page='<%=action.getTargetPage()%>' paramId="oid" onclick='<%=action.getOnclick()%>' fkey='<%=story.getIteration().getId()%>'
           paramName='<%=action.getDomainType()%>' paramProperty="id" useReturnto='<%=action.useReturnTo()%>'>
         <bean:message key='<%=action.getTitleKey()%>'/>
      </xplanner:link> 
  </xplanner:isUserAuthorized>
</xplanner:actionButtons>
<xplanner:isUserAuthorized name="story" permission="edit">
<xplanner:link page="/do/edit/task">
   <bean:message key="story.link.create_task"/>
</xplanner:link> 

</xplanner:isUserAuthorized>
</tiles:put>

<tiles:put name="globalActions" direct="true">
 <jsp:include page="exportLinks.jsp"/>
 <xplanner:link page="/do/view/history">
    <bean:message key="history.link"/>
    <xplanner:linkParam id="oid" name="story" property="id"/>
    <xplanner:linkParam id='<%= AbstractAction.TYPE_KEY %>' value='<%= Hibernate.getClass(story).getName() %>'/>
 </xplanner:link>
</tiles:put>

<script>
		tooltip("<%= messages.getMessage("action.edit.task")%>", "edit", $('img'));
		tooltip("<%= messages.getMessage("action.delete.task")%>", "delete", $('img'));
		tooltip("<%= messages.getMessage("action.movecontinue.task")%>", "movecontinue", $('img'));
		tooltip("<%= messages.getMessage("action.edittime.task")%>", "edittime", $('img'));
		</script>

</xplanner:content>

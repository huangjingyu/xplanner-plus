<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.Globals,
                 com.technoetic.xplanner.domain.StoryDisposition,
                 com.technoetic.xplanner.format.DecimalFormat,
                 com.technoetic.xplanner.views.IterationStoriesPage"%>
<%@ page import="net.sf.xplanner.domain.UserStory"%>
<%@ page import="com.technoetic.xplanner.views.IterationAccuracyPage"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>

<db:useBean id="iteration" type="net.sf.xplanner.domain.Iteration" scope="request" />
<xplanner:content definition="tiles:view" >

<%@include file="../layout/iterationTileParams.jsp" %>

<% if (iteration.getUserStories().size() > 0) { %>
<jsp:useBean id="pieChartPostprocessor" class="com.technoetic.xplanner.charts.PieChartPostProcessor" scope="request"/>
<div class="accuracy">
<table class="objecttable currentStatus" id="currentStatus">
	<thead>
    <tr>  
            <th title='<bean:message key="iteration.labeltooltip.current_status"/>'><bean:message key="iteration.label.current_status"/></th>
<th title='<bean:message key="iteration.label.task.current_status.tooltip"/>'><bean:message key="iteration.label.task"/></th>
    </tr>
	</thead>
  <tbody>
    <tr>
      <td><bean:message key="iteration.label.estimated_original_hours"/></td>
      <td title='<bean:message key="iteration.label.originalEstimate.tooltip"/>'><%= DecimalFormat.format(pageContext, iteration.getTaskEstimatedOriginalHours()) %></td>
    </tr>
    <tr>
      <td><bean:message key="iteration.label.estimated_hours"/></td>
      <td title="<bean:message key="iteration.label.currentEstimate.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getEstimatedHours()) %></td>
    </tr><tr>
      <td><bean:message key="iteration.label.completed"/></td>
      <td title="<bean:message key="iteration.label.currentCompleted.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskActualHours()) %></td>
    </tr>
    <tr>
      <td><bean:message key="iteration.label.remaining"/></td>
      <td title="<bean:message key="iteration.label.currentRemaining.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskRemainingHours()) %></td>
    </tr>
    <tr>
      <td><bean:message key="iteration.label.overestimated"/></td>
      <td title="<bean:message key="iteration.label.currentOverestimated.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskOverestimatedHours()) %></td>
    </tr>
    <tr>
      <td><bean:message key="iteration.label.underestimated"/></td>
      <td title="<bean:message key="iteration.label.currentUnderestimated.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskUnderestimatedHours()) %></td>
    </tr>
    <tr>
      <td><bean:message key="iteration.label.added"/></td>
      <td title="<bean:message key="iteration.label.currentAdded.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskAddedHours()) %></td>
    </tr>
    <tr>
      <td><bean:message key="iteration.label.postponed"/></td>
      <td title="<bean:message key="iteration.label.currentPostponed.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskPostponedHours()) %></td>
    </tr>
  </tbody>
</table>

<table id="<%= IterationAccuracyPage.ITERATION_STATUS_TABLE_ID %>" class="objecttable status">
  <thead>
	  <tr>
	    <th  title='<bean:message key="iteration.labeltooltip.summary"/>'><bean:message key="iteration.label.summary"/></th>
	    <th title='<bean:message key="iteration.label.story.tooltip"/>'><bean:message key="iteration.label.story"/></th>
	    <th title='<bean:message key="iteration.label.task.summary.tooltip"/>'><bean:message key="iteration.label.task"/></th>
	  </tr>
  </thead>
  <tbody>
	  <tr>
	    <td><bean:message key="iteration.label.original"/></td>
	    <td title="<bean:message key="iteration.label.original.story.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getStoryOriginalHours()) %></td>
	    <td title="<bean:message key="iteration.label.original.task.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskOriginalHours()) %></td>
	  </tr>
	  <tr>
	    <td><bean:message key="iteration.label.added"/></td>
	    <td title="<bean:message key="iteration.label.added.story.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getStoryAddedHours()) %></td>
	    <td title="<bean:message key="iteration.label.added.task.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskAddedHours()) %></td>
	  </tr>
	  <tr>
	    <td><bean:message key="iteration.label.postponed"/></td>
	    <td title="<bean:message key="iteration.label.postponed.story.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getStoryPostponedHours()) %></td>
	    <td title="<bean:message key="iteration.label.postponed.task.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskPostponedHours()) %></td>
	  </tr>
	  <tr>
	    <td><bean:message key="iteration.label.total"/></td>
	    <td title="<bean:message key="iteration.label.total.story.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getStoryTotalHours()) %></td>
	    <td title="<bean:message key="iteration.label.total.task.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskTotalHours()) %></td>
	  </tr>
	  <tr>
	    <td><bean:message key="iteration.label.completed"/></td>
	    <td title="<bean:message key="iteration.label.completed.story.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getStoryCompletedHours()) %></td>
	    <td title="<bean:message key="iteration.label.completed.task.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskCompletedHours()) %></td>
	  </tr>
	  <tr>
	    <td><bean:message key="iteration.label.remaining"/></td>
	    <td title="<bean:message key="iteration.label.remaining.story.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getStoryRemainingHours()) %></td>
	    <td title="<bean:message key="iteration.label.remaining.task.tooltip"/>"><%= DecimalFormat.format(pageContext, iteration.getTaskCompletedRemainingHours()) %></td>
	  </tr>
  </tbody>
</table>
</div>
<br>
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
<dt:table id="objecttable" defaultsort="1"  defaultorder="ascending" name="requestScope.iteration" property="userStories"
          styleClass="objecttable" cellspacing="0"
          requestURI="" decorator="com.technoetic.xplanner.tags.displaytag.UserStoryDecorator">
    <bean:define id="story" name="objecttable" type="net.sf.xplanner.domain.UserStory"/>
  <tr>
    <dt:column sortable="true" sortProperty="orderNo" title='<%= messages.getMessage("stories.tableheading.order")%>'
            htmlTitle='<%= messages.getMessage("stories.headertooltip.order")%>'>
            ${story.orderNo}
    </dt:column>
    <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("stories.tableheading.name")%>'
            htmlTitle='<%= messages.getMessage("stories.headertooltip.name")%>'>
        <html:link page="/do/view/userstory" paramId="oid" paramName="story" paramProperty="id">
            ${story.name}</html:link>
    </dt:column>
    <dt:column sortable="true" sortProperty="priority" title='<%= messages.getMessage(IterationStoriesPage.STORIES_PRIORITY_COLUMN)%>'
            htmlTitle='<%= messages.getMessage("stories.headertooltip.priority")%>'>
        ${story.priority}
    </dt:column>
    <%--<dt:column sortable="true" sortProperty="estimatedOriginalHours" align="right" title='<%= messages.getMessage(IterationStoriesPage.STORIES_ESTIMATED_ORIGINAL_HOURS_COLUMN)%>'--%>
            <%--htmlTitle='<%= messages.getMessage("stories.headertooltip.estimated_original_hours")%>'>--%>
        <%--<%= DecimalFormat.format(pageContext, story.getEstimatedOriginalHours()) %>--%>
    <%--</dt:column>--%>
      <dt:column sortable="true" sortProperty="iterationStartEstimatedHours" align="right" title='<%= messages.getMessage(IterationStoriesPage.STORIES_ITERATION_START_ESTIMATE_HOURS_COLUMN)%>'
              htmlTitle='<%= messages.getMessage("stories.headertooltip.iteration_start_estimate_hours")%>'>
        <%= DecimalFormat.format(pageContext, StoryDisposition.ADDED.equals(story.getDisposition()) ? story.getTaskEstimatedOriginalHours() : story.getEstimatedOriginalHours()) %>
      </dt:column>
    <dt:column sortable="true" sortProperty='<%=UserStory.ESTIMATED_HOURS%>' align="right" title='<%= messages.getMessage(IterationStoriesPage.STORIES_ESTIMATED_HOURS_COLUMN)%>'
            htmlTitle='<%= messages.getMessage("stories.headertooltip.estimated_hours")%>'>
        <%= story.isCompleted() ? "" : DecimalFormat.format(pageContext, story.getEstimatedHours()) %>
    </dt:column>
    <dt:column sortable="true" sortProperty="actualHours" align="right" title='<%= messages.getMessage(IterationStoriesPage.STORIES_ACTUAL_HOURS_COLUMN)%>'
            htmlTitle='<%= messages.getMessage("stories.headertooltip.actual_hours")%>'>
        <%= DecimalFormat.format(pageContext, story.getCachedActualHours()) %>
    </dt:column>
    <dt:column sortable="true" sortProperty="originalEstimateAndActualPercentDifference"
                               property="originalEstimateAndActualPercentDifferenceFormatted"
               align="right" title='<%= messages.getMessage("stories.tableheading.percent_diff")%>'
               htmlTitle='<%= messages.getMessage("stories.headertooltip.percent_diff")%>'/>
    <dt:column align="center" sortable="true" title='<%= messages.getMessage("stories.tableheading.disposition")%>'
         htmlTitle='<%= messages.getMessage("stories.headertooltip.disposition")%>'>
        <%=messages.getMessage(story.getDisposition().getAbbreviationKey())%>
    </dt:column>
    <logic:equal name="iteration" property="current" value="true" >
        <dt:column sortable="true" sortProperty="originalEstimateToCurrentEstimateScore"
                   title='<%= messages.getMessage("iteration.accuracy.orig_to_est")%>'
                   htmlTitle='<%= messages.getMessage("iteration.accuracy.headertooltip.orig_to_est")%>'>
            <xplanner:progressBar
                  actual='<%= story.getEstimatedHours() %>'
                  estimate='<%= story.getTaskBasedEstimatedOriginalHours() %>'
                  complete='<%= story.isCompleted() %>'
                  width="120" height="8"/>
        </dt:column>
    </logic:equal>
    <dt:column sortable="true" sortProperty="actualToOriginalEstimateScore"
               title='<%= messages.getMessage("iteration.accuracy.actual_to_orig")%>'
               htmlTitle='<%= messages.getMessage("iteration.accuracy.headertooltip.actual_to_orig")%>'>
        <xplanner:progressBar
                  actual='<%= story.getCachedActualHours() %>'
                  estimate='<%= story.getTaskBasedEstimatedOriginalHours() %>'
                  complete='<%= story.isCompleted() %>'
                  width="120" height="8"/>
    </dt:column>
    <dt:column sortable="true" sortProperty="actualToEstimateScore"
               title='<%= messages.getMessage("iteration.accuracy.actual_to_est")%>'
               htmlTitle='<%= messages.getMessage("iteration.accuracy.headertooltip.actual_to_est")%>'>
        <xplanner:progressBar
                  actual='<%= story.getCachedActualHours() %>'
                  estimate='<%= story.getEstimatedHours() %>'
                  complete='<%= story.isCompleted() %>'
                  width="120" height="8"/>
    </dt:column>
</dt:table>

<% } %>

<% if (iteration.getUserStories().size() == 0) { %>
  <p class="highlighted_message"><bean:message key="stories.none"/></p>
<% } %>
<br/>
</xplanner:content>

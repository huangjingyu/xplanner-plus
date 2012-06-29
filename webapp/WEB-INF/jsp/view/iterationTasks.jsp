<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.format.*,
                 org.apache.struts.Globals,
                 com.technoetic.xplanner.domain.ext.*,
                 com.technoetic.xplanner.tags.displaytag.*,
                 com.technoetic.xplanner.domain.*,
                 com.technoetic.xplanner.views.*,
                 net.sf.xplanner.domain.*,
                 com.technoetic.xplanner.views.IconConstants" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>


<db:useBean id="iteration" type="net.sf.xplanner.domain.Iteration" scope="request" />
<xplanner:content definition="tiles:view" >
<%@include file="../layout/iterationTileParams.jsp" %>

<tiles:put name="titlePrefix">iteration.alltasks.prefix</tiles:put>

<bean:size id="storyCount" name="iteration" property="userStories"/>
<bean:define id="stories" name="iteration" property="userStories"/>


<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
<logic:greaterThan name="storyCount" value="0">
  <div class="title" >
  	 <h1>	
  	 <bean:message key="iteration.label.hours"/>
     <bean:message key="iteration.label.estimated"/>
          <%= DecimalFormat.format(pageContext, iteration.getEstimatedHours()) %>,
     <bean:message key="iteration.label.actual"/>
          <%= DecimalFormat.format(pageContext, iteration.getCachedActualHours()) %>,
     <bean:message key="iteration.label.remaining"/>
          <%= DecimalFormat.format(pageContext, iteration.getTaskRemainingHours()) %>
</h1>
  </div>

  <%  IterationTasks iterationTasks = new IterationTasks(iteration);
      request.setAttribute("iterationTasks", iterationTasks);
  %>

  <xplanner:writableTable wholeCollection='<%=iteration.getUserStories() %>'
                          permissions="edit,delete"
                          sort="list"
                          defaultsort="7"
                          rowDecorator='<%= new TaskRowDecorator() %>'
                          id="objecttable"
                          name="iterationTasks"
                          property="iterationTasks"
                          styleClass="objecttable"
                          requestURI=""
                          decorator="com.technoetic.xplanner.tags.displaytag.AllTasksTableDecorator">
    <% Task task = (Task)objecttable;
       pageContext.setAttribute("task", task);
       UserStory story = null;
       if (task != null) {
          story = task.getUserStory();
          pageContext.setAttribute("story", story);
       }
    %>
    <dt:setProperty name="paging.banner.placement" value="bottom" />
    <xplanner:actionButtonsColumn
        name="task" id="action" title='<%= messages.getMessage("stories.tableheading.actions") %>' >
      <xplanner:isUserAuthorized name="task" permission='<%=action.getPermission()%>'>
        <xplanner:link styleClass="image" page='<%=action.getTargetPage()%>' paramId="oid" onclick='<%=action.getOnclick()%>'
                  paramName='<%=action.getDomainType()%>' paramProperty="id" fkey='<%=story.getId()%>' >
        <html:img page='<%=action.getIconPath()%>' alt='<%=action.getName()%>' border="0"/></xplanner:link>
       </xplanner:isUserAuthorized>
    </xplanner:actionButtonsColumn>
    <dt:column sortable="true" sortProperty="story.orderNo" title='<%= messages.getMessage(IterationStoriesPage.STORIES_ORDER_COLUMN) %>'>
      ${story.orderNo}
    </dt:column>
    <dt:column sortable="true" sortProperty="story" title='<%= messages.getMessage("person.tableheading.story") %>' group="1">
      <html:link page="/do/view/userstory" paramId="oid" paramName="story" paramProperty="id">
        ${story.name}
      </html:link>
    </dt:column>
    <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("person.tableheading.task") %>'>
      <html:link page="/do/view/task" paramId="oid" paramName="task" paramProperty="id">
        ${task.name}
      </html:link>
    </dt:column>
    <dt:column title='<%= messages.getMessage("objects.tableheading.id") %>'>
      <html:link page="/do/view/task" paramId="oid" paramName="task" paramProperty="id">
  	      ${task.id}</html:link>
    </dt:column>
    <dt:column sortable="true" sortProperty="acceptorId" title='<%= messages.getMessage("tasks.tableheading.acceptor") %>'>
      <logic:notEqual name="task" property="acceptorId" value="0">
        <db:useBean id="acceptor" type="net.sf.xplanner.domain.Person"
                    oid='<%= new Integer(task.getAcceptorId()) %>'/>
        <xplanner:link page="/do/view/person" paramId="oid" paramName="acceptor" paramProperty="id">
          ${acceptor.initials}</xplanner:link>
      </logic:notEqual>
      <logic:equal name="task" property="acceptorId" value="0">&nbsp;</logic:equal>
    </dt:column>
    <% String image = IconConstants.STATUS_OPEN_ICON;
       String toolTip = "";
       if (task != null) {
          if (task.getStatus() == TaskStatus.STARTED) {
             image = IconConstants.STATUS_STARTED_ICON;
             toolTip = "Started";
          }
          else if (task.getStatus() == TaskStatus.COMPLETED) {
             image = IconConstants.STATUS_COMPLETED_ICON;
             toolTip = "Completed";
          }
       }
    %>
    <dt:column sortable="true"  sortProperty="statusOrder" title='<%= messages.getMessage("tasks.tableheading.status") %>' align="center">
    	<%if(toolTip!=""){ %>
    	<html:img page='<%=image%>'  />
    	<%}else{ %>
    	<html:img page='<%=image%>' />
      <%} %>
    </dt:column>
    <dt:column sortable="true" sortProperty="estimatedOriginalHours" title='<%= messages.getMessage("tasks.tableheading.estimate.original") %>' align="right">
      <%= DecimalFormat.format(pageContext, task.getEstimatedOriginalHours()) %>
    </dt:column>
    <dt:column sortable="true" sortProperty="estimatedHours" title='<%= messages.getMessage("tasks.tableheading.estimate") %>' align="right">
      <%= DecimalFormat.format(pageContext, task.getEstimatedHours()) %>
    </dt:column>
    <dt:column sortable="true" sortProperty="actualHours" title='<%= messages.getMessage("tasks.tableheading.actual") %>' align="right">
      <%= DecimalFormat.format(pageContext, task.getActualHours()) %>
    </dt:column>
    <dt:column sortable="true" sortProperty="remainingHours" title='<%= messages.getMessage("tasks.tableheading.remaining") %>' align="right">
      <%= task.isCompleted() ? "" : DecimalFormat.format(pageContext, task.getRemainingHours()) %>
    </dt:column>
    <dt:column sortable="true" sortProperty="disposition" title='<%= messages.getMessage("tasks.tableheading.disposition") %>'>
      <%= messages.getMessage(task.getDispositionNameKey()) %>
    </dt:column>
    <dt:column sortable="true" property="type" title='<%= messages.getMessage("tasks.tableheading.type") %>'/>
  </xplanner:writableTable>

    
      <table class="infoTable" border="0">
      <tr>
        <td><bean:message key="iteration.legend.task_started"/></td>
        <td>
           <html:img page='<%=IconConstants.STATUS_STARTED_ICON%>'/>
<%--          <table cellpadding="0" cellspacing="0" border="1">--%>
<%--              <tr><td height="8" class="task_started" width="40">&nbsp;</td></tr>--%>
<%--          </table>--%>
        </td>
        <td><bean:message key="iteration.legend.task_completed"/></td>
        <td>
           <html:img page='<%=IconConstants.STATUS_COMPLETED_ICON%>'/>
<%--           <table cellpadding="0" cellspacing="0" border="1">--%>
<%--              <tr> <td height="8" class="task_completed" width="40">&nbsp;</td> </tr>--%>
<%--           </table>--%>
        </td>

        <td><bean:message key="iteration.legend.task_unassigned"/></td>
        <td><table cellpadding="0" cellspacing="0" border="1">
              <tr>
                  <td height="8" class="task_unassigned" width="40">&nbsp;</td>
              </tr></table></td>
        <td><bean:message key="iteration.legend.task_unestimated"/></td>
        <td><table cellpadding="0" cellspacing="0" border="1">
              <tr>
                  <td height="8" class="task_unestimated" width="40">&nbsp;</td>
              </tr></table></td>
      </tr>
      </table>
   
    </logic:greaterThan>


<logic:equal name="storyCount" value="0">
<p class="highlighted_message"><bean:message key="stories.none"/></p>
</logic:equal>

</xplanner:content>

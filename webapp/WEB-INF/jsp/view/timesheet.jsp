<%@ page import="com.technoetic.xplanner.format.DecimalFormat"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" prefix="cewolf" %>
<%@ taglib tagdir="/WEB-INF/tags"  prefix="tags" %>

<xplanner:content>
    <xplanner:contentTitle title="XPlanner {0} {1}">
        <xplanner:contentTitleArg><bean:message key="timesheet.prefix"/></xplanner:contentTitleArg>
        <xplanner:contentTitleArg><bean:message key="timesheet.personName"/></xplanner:contentTitleArg>
    </xplanner:contentTitle>

<bean:parameter id="oid" name="oid"/>
<jsp:useBean id="labelRotation" class="de.laures.cewolf.cpp.RotatedAxisLabels" scope="request"/>
<jsp:useBean id="dataPointShapes" class="com.technoetic.xplanner.charts.CategoryPostProcessor" scope="request"/>
<jsp:useBean id="borderColor" class="com.technoetic.xplanner.charts.BorderPostProcessor" scope="request"/>

<script type="text/javascript" src="<html:rewrite page="/calendar/calendar.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/calendar/calendarHelper.js"/>"></script>
<script type="text/javascript" src="<html:rewrite page="/calendar/calendar-i18n.jsp"/>"></script>

<html:form method="post" action="view/timesheet">
<html:hidden name="timesheetForm" property="oid"/>
<p class="title"><bean:write name="timesheetForm" property="timesheet.personName"/></p>
<p>
  <b><bean:message key="timesheet.label.period_start"/></b>
  &nbsp; <html:text styleId="startDate" name="timesheetForm" property="startDateString" size="12" maxlength="10"/>
  &nbsp; <input type="image" src="<html:rewrite page="/images/calendar.gif" />" align="top"
                name="setStartDate" value="<bean:message key="timesheet.editor.set_date"/>"
                onclick="return showCalendar('startDate', '<bean:message key="format.date"/>');"/>
  &nbsp; <b><bean:message key="timesheet.label.period_end"/></b>
  &nbsp; <html:text styleId="endDate" name="timesheetForm" property="endDateString" size="12" maxlength="10"/>
  &nbsp; <input type="image" src="<html:rewrite page="/images/calendar.gif" />" align="top"
              name="setEndDate" value="<bean:message key="timesheet.editor.set_date"/>"
              onclick="return showCalendar('endDate', '<bean:message key="format.date"/>');"/>
</p>

<!-- Summary Timesheet -->
<p class="title"><bean:message key="timesheet.label.summary"/></p>
<table class="objecttable">
	  <tr class="objecttable">
		<th class="objecttable"><bean:message key="timesheet.tableheading.project"/></th>
		<th class="objecttable"><bean:message key="timesheet.tableheading.iteration"/></th>
		<th class="objecttable"><bean:message key="timesheet.tableheading.story"/></th>
		<th class="objecttable"><bean:message key="timesheet.tableheading.hours"/></th>
	  </tr>
          <logic:iterate id="entry" name="timesheetForm" property="timesheet.entries" indexId="entryIdx">
                <tr class="objecttable">
                   <td class="objecttable">
                       <html:link page="/do/view/project" paramId="oid" paramName="entry" paramProperty="projectId">
                           <bean:write name="entry" property="projectName"/>
                       </html:link>
                   </td>
                   <td class="objecttable">
                       <html:link page="/do/view/iteration" paramId="oid" paramName="entry" paramProperty="iterationId">
                           <bean:write name="entry" property="iterationName"/>
                       </html:link>
                   </td>
                   <td class="objecttable">
                       <html:link page="/do/view/userstory" paramId="oid" paramName="entry" paramProperty="storyId">
                           <bean:write name="entry" property="storyName"/>
                       </html:link>
                   </td>
                   <td class="objecttable"><bean:write name="entry" property="totalDuration"/></td>
                </tr>
          </logic:iterate>
               <tr class="objecttable_buttons">
                   <td class="objecttable_buttons" colspan="3"><b><bean:message key="timesheet.label.total"/></b></td>
                   <td class="objecttable_buttons"><b><bean:write name="timesheetForm" property="timesheet.total"/></b></td>
               </tr>
</table>

<!-- Daily Timesheet -->
<%-- todo Improve daily timesheet formatting -- too wide --%>
<bean:define id="timesheet" name="timesheetForm" property="timesheet" type="com.technoetic.xplanner.domain.virtual.Timesheet"/>
<p class="title"><bean:message key="timesheet.label.daily"/></p>

<table class="objecttable" cellpadding="4">
  <tr class="objecttable">
  <logic:iterate id="entry" name="timesheetForm" property="timesheet.dailyEntries" indexId="entryIdx">
        <th align="center" class="objecttable"><bean:write name="entry" property="entryDateShort"/></th>
  </logic:iterate>
        <th align="center" class="objecttable"><bean:message key="timesheet.label.total"/></th>
  </tr>
   <tr class="objecttable">
       <logic:iterate id="entry" name="timesheetForm" property="timesheet.dailyEntries"
                indexId="entryIdx" type="com.technoetic.xplanner.domain.virtual.DailyTimesheetEntry">
           <td align="center" class="objecttable">
               <%= DecimalFormat.format(request, entry.getTotalDuration().doubleValue(), " -- ") %>
           </td>
       </logic:iterate>
           <td align="center" class="objecttable">
               <b><%= DecimalFormat.format(request, timesheet.getTotal().doubleValue()) %></b>
           </td>
    </tr>
</table>

<html:submit/>
</html:form>

<bean:define id="timesheetForm" name="timesheetForm" type="com.technoetic.xplanner.forms.PersonTimesheetForm"/>

<jsp:useBean id="personTimesheetProjectData" class="com.technoetic.xplanner.charts.timesheet.PersonTimesheetProjectData" scope="request">
  <jsp:setProperty name="personTimesheetProjectData" property="timesheet" value='<%= timesheetForm.getTimesheet() %>' />
</jsp:useBean>

<cewolf:chart id="personTimesheetProjectChart" type="pie" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="personTimesheetProjectData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="borderColor" />
</cewolf:chart>

<jsp:useBean id="personTimesheetIterationData" class="com.technoetic.xplanner.charts.timesheet.PersonTimesheetIterationData" scope="request">
  <jsp:setProperty name="personTimesheetIterationData" property="timesheet" value='<%= timesheetForm.getTimesheet() %>' />
</jsp:useBean>

<cewolf:chart id="personTimesheetIterationChart" type="pie" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="personTimesheetIterationData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="borderColor" />
</cewolf:chart>


<jsp:useBean id="personTimesheetStoryData" class="com.technoetic.xplanner.charts.timesheet.PersonTimesheetStoryData" scope="request">
  <jsp:setProperty name="personTimesheetStoryData" property="timesheet" value='<%= timesheetForm.getTimesheet() %>' />
</jsp:useBean>

<cewolf:chart id="personTimesheetStoryChart" type="pie" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="personTimesheetStoryData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="borderColor" />
</cewolf:chart>


<table cellpadding="1" class="objecttable">
  <tr>
    <th width="100%" align="center" class="objecttable_heading">
      <bean:message key="timesheet.statistics.hours_by_project"/>
      ( <bean:write name="timesheetForm" property="timesheet.personName"/>:
      <bean:write name="timesheetForm" property="startDateString"/>
      <bean:message key="timesheet.label.to"/>
      <bean:write name="timesheetForm" property="endDateString"/> )
    </th>
  </tr>
  <tr>
     <td width="100%" align="center" valign="top">
       <cewolf:img chartid="personTimesheetProjectChart" renderer="../../cewolf" width="600" height="307"/>
     </td>
  </tr>
  <tr>
    <th width="100%" align="center" class="objecttable_heading">
      <bean:message key="timesheet.statistics.hours_by_iteration"/>
      ( <bean:write name="timesheetForm" property="timesheet.personName"/>:
      <bean:write name="timesheetForm" property="startDateString"/>
      <bean:message key="timesheet.label.to"/>
      <bean:write name="timesheetForm" property="endDateString"/> )
    </th>
  </tr>
  <tr>
    <td width="100%" align="center" valign="top">
      <cewolf:img chartid="personTimesheetIterationChart" renderer="../../cewolf" width="600" height="307"/>
    </td>
  </tr>
  <tr>
    <th width="100%" align="center" class="objecttable_heading">
      <bean:message key="timesheet.statistics.hours_by_story"/>
      ( <bean:write name="timesheetForm" property="timesheet.personName"/>:
      <bean:write name="timesheetForm" property="startDateString"/>
      <bean:message key="timesheet.label.to"/>
      <bean:write name="timesheetForm" property="endDateString"/> )
    </th>
  </tr>
  <tr>
    <td width="100%" align="center" valign="top">
      <cewolf:img chartid="personTimesheetStoryChart" renderer="../../cewolf" width="600" height="307"/>
    </td>
  </tr>
</table>

<span class="links">
<xplanner:link page="/do/view/people"><bean:message key="projects.link.people"/></xplanner:link>
</span>

</xplanner:content>

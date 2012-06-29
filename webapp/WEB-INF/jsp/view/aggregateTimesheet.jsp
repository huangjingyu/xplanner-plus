<%@ page
	import="org.apache.struts.Globals,com.technoetic.xplanner.domain.virtual.TimesheetEntry,com.technoetic.xplanner.forms.AggregateTimesheetForm"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner"%>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db"%>
<%@ taglib uri="http://cewolf.sourceforge.net/taglib/cewolf.tld"
	prefix="cewolf"%>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt"%>

<xplanner:content>
	<xplanner:contentTitle title="XPlanner {0}">
		<xplanner:contentTitleArg>
			<bean:message key="timesheet.aggregate.title" />
		</xplanner:contentTitleArg>
	</xplanner:contentTitle>

	<jsp:useBean id="labelRotation"
		class="de.laures.cewolf.cpp.RotatedAxisLabels" scope="request" />
	<jsp:useBean id="dataPointShapes"
		class="com.technoetic.xplanner.charts.CategoryPostProcessor"
		scope="request" />
	<jsp:useBean id="borderColor"
		class="com.technoetic.xplanner.charts.BorderPostProcessor"
		scope="request" />

	<script type="text/javascript"
		src="<html:rewrite page="/calendar/calendar.js"/>"></script>
	<script type="text/javascript"
		src="<html:rewrite page="/calendar/calendarHelper.js"/>"></script>
	<script type="text/javascript"
		src="<html:rewrite page="/calendar/calendar-i18n.jsp"/>"></script>

	<html:form method="post" action="view/aggregateTimesheet">
		<h1 class="title"><bean:message key="timesheet.aggregate.title" /></h1>
		
		<div id="editObject">
		<table id="timesheet" border="0" width="400">

			<tr margin="0" padding="0">
				<td border="0" width="150"><b><bean:message
					key="timesheet.aggregate.label.people" /></b></td>
				<td border="0"><b><bean:message
					key="timesheet.label.period_start" /></b></td>
			</tr>



			<tr border="0">
				<td margin="0" padding="0" border="0" width="150"
					id="selectedPeople" valign="top">
				<div class="borderedInput ">
				<div><html:select name="aggregateTimesheetForm"
					property="selectedPeople" multiple="true" size="7">
					<html:optionsCollection property="allPeople" value="id"
						label="name" />
				</html:select></div>
				</div>
				</td>

				<td margin="0" padding="0" valign="top">
				<div class="borderedInput dateInput">
				<div><html:text styleId="startDate"
					name="aggregateTimesheetForm" property="startDateString" size="12"
					maxlength="10" /> <input type="image"
					src="<html:rewrite page="/images/calendar.gif" />" align="top"
					name="setStartDate"
					value="<bean:message key="timesheet.editor.set_date"/>"
					onclick="return showCalendar('startDate', '<bean:message key="format.date"/>');" />
				</div>
				</div>

				<b><bean:message key="timesheet.label.period_end" /></b>
				<div class="borderedInput dateInput">
				<div><html:text styleId="endDate"
					name="aggregateTimesheetForm" property="endDateString" size="12"
					maxlength="10" /> <input type="image"
					src="<html:rewrite page="/images/calendar.gif" />" align="top"
					name="setEndDate"
					value="<bean:message key="timesheet.editor.set_date"/>"
					onclick="return showCalendar('endDate', '<bean:message key="format.date"/>');" />
				</div>
				</div>
				<br />
				<div class="objecttable_buttons"><html:submit /></div>

				</td>
			</tr>
		</table>
		</div>

		<%-- Timesheet --%>
		<%
			request.setAttribute("aggregateTimesheetForm", session
							.getAttribute("aggregateTimesheetForm"));
		%>
		<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>'
			type="org.apache.struts.util.MessageResources" />
		<p class="stop"></p><dt:table id="entry" name="aggregateTimesheetForm"
			property="timesheet.entries" styleClass="objecttable" requestURI="">

			<dt:column sortable="true" sortProperty="projectName"
				title='<%= messages.getMessage("timesheet.tableheading.project") %>'>
				<html:link page="/do/view/project" paramId="oid" paramName="entry"
					paramProperty="projectId">
					<bean:write name="entry" property="projectName" />
				</html:link>
			</dt:column>
			<dt:column sortable="true" sortProperty="iterationName"
				title='<%= messages.getMessage("timesheet.tableheading.iteration") %>'>
				<html:link page="/do/view/iteration" paramId="oid" paramName="entry"
					paramProperty="iterationId">
					<bean:write name="entry" property="iterationName" />
				</html:link>
			</dt:column>
			<dt:column sortable="true" sortProperty="storyName"
				title='<%= messages.getMessage("timesheet.tableheading.story") %>'>
				<html:link page="/do/view/userstory" paramId="oid" paramName="entry"
					paramProperty="storyId">
					<bean:write name="entry" property="storyName" />
				</html:link>
			</dt:column>
			<dt:column sortable="true" sortProperty="totalDuration" align="right"
				title='<%= messages.getMessage("timesheet.tableheading.hours") %>'>
				<bean:write name="entry" property="totalDuration" />
			</dt:column>
		</dt:table>
		<div class="strip"><b>Total Hours:&nbsp;&nbsp;&nbsp;&nbsp;<bean:write
			name="aggregateTimesheetForm" property="timesheet.total" />&nbsp;&nbsp;</b>
		</div>



	</html:form>

	<!-- Charts -->

	<bean:define id="aggregateTimesheetForm" name="aggregateTimesheetForm"
		type="com.technoetic.xplanner.forms.AggregateTimesheetForm" />
	<jsp:useBean id="timesheetProjectData"
		class="com.technoetic.xplanner.charts.timesheet.PersonTimesheetProjectData"
		scope="request">
		<jsp:setProperty name="timesheetProjectData" property="timesheet"
			value='<%= aggregateTimesheetForm.getTimesheet() %>' />
	</jsp:useBean> 
	<cewolf:chart id="timesheetProjectChart" type="pie" showlegend="false"
		background="#000000">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="timesheetProjectData" />
		</cewolf:data>
		<cewolf:chartpostprocessor id="borderColor" />
	</cewolf:chart>
	<jsp:useBean id="timesheetIterationData"
		class="com.technoetic.xplanner.charts.timesheet.PersonTimesheetIterationData"
		scope="request">
		<jsp:setProperty name="timesheetIterationData" property="timesheet"
			value='<%= aggregateTimesheetForm.getTimesheet() %>' />
	</jsp:useBean>

	<cewolf:chart id="timesheetIterationChart" type="pie"
		showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="timesheetIterationData" />
		</cewolf:data>
		<cewolf:chartpostprocessor id="borderColor" />
	</cewolf:chart>


	<jsp:useBean id="timesheetStoryData"
		class="com.technoetic.xplanner.charts.timesheet.PersonTimesheetStoryData"
		scope="request">
		<jsp:setProperty name="timesheetStoryData" property="timesheet"
			value='<%= aggregateTimesheetForm.getTimesheet() %>' />
	</jsp:useBean>

	<cewolf:chart id="timesheetStoryChart" type="pie" showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="timesheetStoryData" />
		</cewolf:data>
		<cewolf:chartpostprocessor id="borderColor" />
	</cewolf:chart>


	<table cellpadding="1" class="objecttable charts">
		<tr>
			<th width="100%" align="center" class="table_label">
			<br/><bean:message
				key="timesheet.statistics.hours_by_project" /> ( <bean:write
				name="aggregateTimesheetForm" property="startDateString" /> <bean:message
				key="timesheet.label.to" /> <bean:write
				name="aggregateTimesheetForm" property="endDateString" /> )</th>
		</tr>
		<tr>
			<td width="100%" align="center" valign="top"><cewolf:img
				chartid="timesheetProjectChart" renderer="../../cewolf" width="600"
				height="307" /></td>
		</tr>
		<tr>
			<th width="100%" align="center" class="table_label">
			<br/>
			<bean:message
				key="timesheet.statistics.hours_by_iteration" /> ( <bean:write
				name="aggregateTimesheetForm" property="startDateString" /> <bean:message
				key="timesheet.label.to" /> <bean:write
				name="aggregateTimesheetForm" property="endDateString" /> )</th>
		</tr>
		<tr>
			<td width="100%" align="center" valign="top"><cewolf:img
				chartid="timesheetIterationChart" renderer="../../cewolf"
				width="600" height="307" /></td>
		</tr>
		<tr>
			<th width="100%" align="center" class="table_label">
			<br/><bean:message
				key="timesheet.statistics.hours_by_story" /> ( <bean:write
				name="aggregateTimesheetForm" property="startDateString" /> <bean:message
				key="timesheet.label.to" /> <bean:write
				name="aggregateTimesheetForm" property="endDateString" /> )</th>
		</tr>
		<tr>
			<td width="100%" align="center" valign="top"><cewolf:img
				chartid="timesheetStoryChart" renderer="../../cewolf" width="600"
				height="307" /></td>
		</tr>
	</table>

	<span class="links"> <xplanner:link page="/do/view/people">
		<bean:message key="projects.link.people" />
	</xplanner:link> </span>

</xplanner:content>

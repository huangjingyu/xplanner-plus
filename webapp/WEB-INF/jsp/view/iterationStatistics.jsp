<%
  XPlannerProperties result = new XPlannerProperties();
%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.db.hibernate.ThreadSession,
                 com.technoetic.xplanner.XPlannerProperties,
                 java.util.Properties"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" prefix="cewolf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<db:useBean id="iteration" type="net.sf.xplanner.domain.Iteration" scope="request" />
<xplanner:content definition="tiles:view" >

<script src="<html:rewrite page="/js/dojo/dojo.xd.js"/>" type="text/javascript" djConfig="parseOnLoad:true"></script>

<%@include file="../layout/iterationTileParams.jsp" %>

<jsp:useBean id="labelRotation" class="de.laures.cewolf.cpp.RotatedAxisLabels" scope="request"/>
<jsp:useBean id="dataPointShapes" class="com.technoetic.xplanner.charts.CategoryPostProcessor" scope="request"/>
<jsp:useBean id="borderColor" class="com.technoetic.xplanner.charts.BorderPostProcessor" scope="request"/>
<jsp:useBean id="pieCustomizer" class="com.technoetic.xplanner.charts.PieChartPostProcessor" scope="request"/>

<jsp:useBean id="iterationStatistics" class="com.technoetic.xplanner.db.IterationStatisticsQuery" scope="request">
  <jsp:setProperty name="iterationStatistics" property="iterationId" param="oid" />
  <jsp:setProperty name="iterationStatistics" property="request" value='<%= request %>' />
</jsp:useBean>

<jsp:useBean id="taskTypeData" class="com.technoetic.xplanner.charts.TaskTypeData" scope="request">
  <jsp:setProperty name="taskTypeData" property="statistics" value='<%= iterationStatistics %>' />
</jsp:useBean>
<%-- 	
<xplanner:isUserAuthorized resourceType="system" permission="sysadmin.promote">
	<c:set var="linkForDataRegeration"><html:rewrite page="/do/edit/dataSample"/></c:set>
	<xplanner:link useReturnto="true"  href="${linkForDataRegeration}">Regenerate charts data now</xplanner:link>
	This may take long time!!!
</xplanner:isUserAuthorized>
--%>
	

<cewolf:chart id="taskTypeChart" type="pie3d" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="taskTypeData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="pieCustomizer" />
</cewolf:chart>

<jsp:useBean id="taskDispositionData" class="com.technoetic.xplanner.charts.TaskDispositionData" scope="request">
  <jsp:setProperty name="taskDispositionData" property="statistics" value='<%= iterationStatistics %>' />
</jsp:useBean>

<cewolf:chart id="taskDispositionChart" type="pie3d" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="taskDispositionData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="pieCustomizer" />
</cewolf:chart>

<jsp:useBean id="taskTypeEstimatedHoursData" class="com.technoetic.xplanner.charts.TaskTypeEstimatedHoursData" scope="request">
  <jsp:setProperty name="taskTypeEstimatedHoursData" property="statistics" value='<%= iterationStatistics %>' />
</jsp:useBean>

<cewolf:chart id="taskTypeEstimatedHoursChart" type="pie3d" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="taskTypeEstimatedHoursData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="pieCustomizer" />
</cewolf:chart>

<jsp:useBean id="taskDispositionEstimatedHoursData" class="com.technoetic.xplanner.charts.TaskDispositionEstimatedHoursData" scope="request">
  <jsp:setProperty name="taskDispositionEstimatedHoursData" property="statistics" value='<%= iterationStatistics %>' />
</jsp:useBean>

<cewolf:chart id="taskDispositionEstimatedHoursChart" type="pie3d" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="taskDispositionEstimatedHoursData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="pieCustomizer" />
</cewolf:chart>

<jsp:useBean id="taskTypeActualHoursData" class="com.technoetic.xplanner.charts.TaskTypeActualHoursData" scope="request">
  <jsp:setProperty name="taskTypeActualHoursData" property="statistics" value='<%= iterationStatistics %>' />
</jsp:useBean>

<cewolf:chart id="taskTypeActualHoursChart" type="pie3d" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="taskTypeActualHoursData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="pieCustomizer" />
</cewolf:chart>

<jsp:useBean id="taskDispositionActualHoursData" class="com.technoetic.xplanner.charts.TaskDispositionActualHoursData" scope="request">
  <jsp:setProperty name="taskDispositionActualHoursData" property="statistics" value='<%= iterationStatistics %>' />
</jsp:useBean>

<cewolf:chart id="taskDispositionActualHoursChart" type="pie3d" showlegend="false">
  <cewolf:colorpaint color="#FFFFFF"/>
  <cewolf:data>
    <cewolf:producer id="taskDispositionActualHoursData" />
  </cewolf:data>
  <cewolf:chartpostprocessor id="pieCustomizer" />
</cewolf:chart>

<%
  Properties p = new XPlannerProperties().get();
    int bigChartWidth = Integer.parseInt(p.getProperty("xplanner.effort.chart.width","800"))+10;
    int bigChartHeight = Integer.parseInt(p.getProperty("xplanner.effort.chart.height","400"))+10;
    int smallChartWidth = bigChartWidth/2;
    int smallChartHeight = bigChartHeight/2;
%>
<p>
<table cellpadding="1" align="center" width="50%" class="objecttable charts">
   <xplanner:propertyEqual key="xplanner.effort.chart.velocity" value="displayed">
      <tr>
        <th  colspan="2" align="center">
          <bean:message key="iteration.statistics.velocity.label"/>
        </th>
      </tr>
      <tr>
        <td colspan="2" align="center">
            <jsp:useBean id="velocityData" class="com.technoetic.xplanner.charts.TaskVelocityData" scope="request">
              <jsp:setProperty name="velocityData" property="statistics" value='<%= iterationStatistics %>' />
            </jsp:useBean>
            <% String velocityXAxisLabel = iterationStatistics.getResourceString("iteration.statistics.velocity.xaxis"); %>
            <% String velocityYAxisLabel = iterationStatistics.getResourceString("iteration.statistics.velocity.yaxis"); %>
            <cewolf:chart id="velocityChart" type="line"
                xaxislabel='<%= velocityXAxisLabel %>' yaxislabel='<%= velocityYAxisLabel %>'>
              <cewolf:colorpaint color="#FFFFFF"/>
              <cewolf:data>
                <cewolf:producer id="velocityData"/>
              </cewolf:data>
              <cewolf:chartpostprocessor id="dataPointShapes" />
              <cewolf:chartpostprocessor id="labelRotation" >
                <cewolf:param name="rotate_at" value='<%= new Integer(1) %>' />
              </cewolf:chartpostprocessor>
            </cewolf:chart>
            <cewolf:img chartid="velocityChart" renderer='<%="/cewolf" %>'
                    width='<%= bigChartWidth %>' height='<%= bigChartHeight %>'>
                <cewolf:map tooltipgeneratorid="velocityData"/>
            </cewolf:img>
        </td>
      </tr>
   </xplanner:propertyEqual>
<br/>
   <xplanner:propertyEqual key="xplanner.effort.chart.progress" value="displayed">
      <tr>
        <th class="table_label" colspan="2" align="center">
          <bean:message key="iteration.statistics.progress.label"/>
        </th>
      </tr>
      <tr>
        <td colspan="2" align="center">
            <jsp:useBean id="velocityData2" class="com.technoetic.xplanner.charts.DataSampleData" scope="request">
              <jsp:setProperty name="velocityData2" property="iteration" value='<%= iteration %>' />
              <jsp:setProperty name="velocityData2" property="aspects" value="estimatedHours,actualHours" />
              <jsp:setProperty name="velocityData2" property="categories"
                value='<%= iterationStatistics.getResourceString("iteration.statistics.progress.series_estimated")
                            + "," +
                            iterationStatistics.getResourceString("iteration.statistics.progress.series_actual") %>'/>
              <jsp:setProperty name="velocityData2" property="includeWeekends"
                               value='<%=result.getProperty("xplanner.effort.chart.include.weekends", true)%>' />
            </jsp:useBean>
            <% String progressXAxisLabel = iterationStatistics.getResourceString("iteration.statistics.progress.xaxis"); %>
            <% String progressYAxisLabel = iterationStatistics.getResourceString("iteration.statistics.progress.yaxis"); %>
            <cewolf:chart id="velocityChart2" type="line"
                xaxislabel='<%= progressXAxisLabel %>' yaxislabel='<%= progressYAxisLabel %>'>
              <cewolf:colorpaint color="#FFFFFF"/>
              <cewolf:data>
                <cewolf:producer id="velocityData2"/>
              </cewolf:data>
              <cewolf:chartpostprocessor id="dataPointShapes" />
              <cewolf:chartpostprocessor id="labelRotation" >
                <cewolf:param name="rotate_at" value='<%= new Integer(1) %>' />
              </cewolf:chartpostprocessor>
            </cewolf:chart>
            <cewolf:img chartid="velocityChart2" renderer='<%= "/cewolf" %>'
                width='<%= bigChartWidth %>' height='<%= bigChartHeight %>'>
            </cewolf:img>
        </td>
      </tr>
  </xplanner:propertyEqual>

   <xplanner:propertyEqual key="xplanner.effort.chart.burndown" value="displayed">
      <tr>
        <th class="table_label" colspan="2" align="center">
          <br/>
          <bean:message key="iteration.statistics.burndown.label"/>
        </th>
      </tr>
      <tr>
        <td colspan="2" align="center">
            <jsp:useBean id="burnData" class="com.technoetic.xplanner.charts.DataSampleData" scope="request">
              <jsp:setProperty name="burnData" property="iteration" value='<%= iteration %>' />
              <jsp:setProperty name="burnData" property="aspects" value="remainingHours" />
              <jsp:setProperty name="burnData" property="categories" value="Remaining" />
              <jsp:setProperty name="burnData" property="includeWeekends"
                               value='<%=result.getProperty("xplanner.effort.chart.include.weekends", true)%>' />
            </jsp:useBean>
            <% String burndownXAxisLabel = iterationStatistics.getResourceString("iteration.statistics.burndown.xaxis"); %>
            <% String burndownYAxisLabel = iterationStatistics.getResourceString("iteration.statistics.burndown.yaxis"); %>
            <cewolf:chart id="burnDownChart" type="line"
                xaxislabel='<%= burndownXAxisLabel %>' yaxislabel='<%= burndownYAxisLabel %>'>
              <cewolf:colorpaint color="#FFFFFF"/>
              <cewolf:data>
                <cewolf:producer id="burnData"/>
              </cewolf:data>
              <cewolf:chartpostprocessor id="dataPointShapes" />
              <cewolf:chartpostprocessor id="labelRotation" >
                <cewolf:param name="rotate_at" value='<%= new Integer(1) %>' />
              </cewolf:chartpostprocessor>
            </cewolf:chart>
            <cewolf:img chartid="burnDownChart" renderer='<%= "/cewolf" %>'
                width='<%= bigChartWidth %>' height='<%= bigChartHeight %>'>
            <%--    <cewolf:map tooltipgeneratorid="burnData"/>--%>
            </cewolf:img>
        </td>
      </tr>
  </xplanner:propertyEqual>

  <tr>
    <th class="table_label" width="50%" align="center">
       <br/>
      <bean:message key="iteration.statistics.all_type"/>
    </th>
    <th class="table_label" width="50%" align="center">
       <br/>
      <bean:message key="iteration.statistics.all_disposition"/>
    </th>
  </tr>
  <tr>
    <td width="50%" align="left" valign="top"> 
	 <cewolf:img chartid="taskTypeChart" renderer="../../../cewolf"
        width='<%= smallChartWidth %>' height='<%= smallChartHeight %>'/>
    </td>
    <td width="50%" align="left" valign="top">	
	  <cewolf:img chartid="taskDispositionChart" renderer="../../../cewolf"
        width='<%= smallChartWidth %>' height='<%= smallChartHeight %>'/>
    </td>
  </tr>
  <tr>
    <th class="table_label" width="50%" align="center">
       <br/>
      <bean:message key="iteration.statistics.completed_estimated_type"/>
    </th>
    <th class="table_label" width="50%" align="center">
       <br/>
      <bean:message key="iteration.statistics.completed_estimated_disposition"/>
    </th>
  </tr>
  <tr>
    <td width="50%" align="left" valign="top">

    
	 
	 <cewolf:img chartid="taskTypeEstimatedHoursChart" renderer="../../../cewolf"
        width='<%= smallChartWidth %>' height='<%= smallChartHeight %>'/> 
    </td>
    <td width="50%" align="left" valign="top">
	
      <cewolf:img chartid="taskDispositionEstimatedHoursChart" renderer="../../../cewolf"
        width='<%= smallChartWidth %>' height='<%= smallChartHeight %>'/>
    </td>
  </tr>
  <tr>
    <th class="table_label" width="50%" align="center">
 <br/>     
 <bean:message key="iteration.statistics.completed_actual_type"/>
    </th>
    <th class="table_label" width="50%" align="center">
 <br/>    
  <bean:message key="iteration.statistics.completed_actual_disposition"/>
    </th>
  </tr>
  <tr>
    <td width="50%" align="left" valign="top">

      <cewolf:img chartid="taskTypeActualHoursChart" renderer="../../../cewolf"
        width='<%= smallChartWidth %>' height='<%= smallChartHeight %>'/>
    </td>
    <td width="50%" align="left" valign="top">
    

      <cewolf:img  chartid="taskDispositionActualHoursChart" renderer="../../../cewolf"
        width='<%= smallChartWidth %>' height='<%= smallChartHeight %>'/>
    </td>
  </tr>
</table>
</p>
 <br/>
</xplanner:content>

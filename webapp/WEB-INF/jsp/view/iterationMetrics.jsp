<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.Globals,com.technoetic.xplanner.format.DecimalFormat,
                 com.technoetic.xplanner.metrics.DeveloperMetrics,
                 com.technoetic.xplanner.metrics.IterationMetrics" %>
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
<tiles:put name="titlePrefix">iteration.metrics.prefix</tiles:put>

<jsp:useBean id="metrics" class="com.technoetic.xplanner.metrics.IterationMetrics" scope="request">
</jsp:useBean>

<div class="title">
  	<h1>
  	<bean:message key="iteration.metrics.total_hours"/> <%= DecimalFormat.format(pageContext, metrics.getTotalHours()) %>
	</h1>	
</div>

<div class="title">
  	<h1>
  <bean:message key="iteration.metrics.avr_pair"/> <%= DecimalFormat.format(pageContext, metrics.getTotalPairedPercentage()) %>%
</h1></div>

<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
<p class="highlighted_message">
  <dt:table id="totalTable" htmlId="totalTable" uid="totalTable" name="metrics" defaultsort="1" property="developerTotalTime" styleClass="objecttable" requestURI="">
</p>
    <% final DeveloperMetrics developerMetrics = (DeveloperMetrics)totalTable;
       if(developerMetrics != null) {
            pageContext.setAttribute("developerMetrics", developerMetrics);
       }%>

    <dt:setProperty name="paging.banner.placement" value="bottom" />
      <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("iteration.metrics.tableheading.developer") %>'
        htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.developer")%>' >
        <% if (!developerMetrics.getName().equals(IterationMetrics.UNASSIGNED_NAME) ) { %>
            <xplanner:link page="/do/view/person" paramId="oid" paramName="developerMetrics" paramProperty="id">
	        ${developerMetrics.name}</xplanner:link>
         <% } else { %>
            ${developerMetrics.name}
         <% } %>
      </dt:column>
      <dt:column align="right" nowrap="true" sortable="true" sortProperty="hours" title='<%= messages.getMessage("iteration.metrics.tableheading.total") %>'
               htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.total_worked")%>' >
              <%= DecimalFormat.format(pageContext, developerMetrics.getHours(), "&nbsp;") %>
      </dt:column>
      <dt:column align="right" nowrap="true" sortable="true" sortProperty="hours" title='<%= messages.getMessage("iteration.metrics.tableheading.paired_hours") %>'
              htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.paired.hours")%>' >
              <%= DecimalFormat.format(pageContext, developerMetrics.getPairedHours(),"&nbsp;") %>
      </dt:column>
      <dt:column align="right" nowrap="true" sortable="true" sortProperty="hours" title='<%= messages.getMessage("iteration.metrics.tableheading.solo_hours") %>'
              htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.solo.hours")%>' >
              <%= DecimalFormat.format(pageContext, developerMetrics.getUnpairedHours(),"&nbsp;") %>
      </dt:column>
      <dt:column nowrap="true" sortable="true" sortProperty="hours" title='<%= messages.getMessage("iteration.metrics.tableheading.paired_outof_total") %>'
            htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.paired_outof_total") %>' >
              <table id="metrics" cellpadding="0">
                     <%
                             long maxWidth = 400;
                             long pairedWidth = Math.round(developerMetrics.getPairedHours()/metrics.getMaxTotalTime()*maxWidth);
                             long unpairedWidth = Math.round((developerMetrics.getHours()-developerMetrics.getPairedHours())/
                                 metrics.getMaxTotalTime()*maxWidth);
                     %>
                     <%if (pairedWidth > 0) { %>
                            <td class="pairedHours" width='<%= pairedWidth %>' ></td>
                                       <% } %>
                           <% if (unpairedWidth > 0) { %>
                           <td class="unpairedHours" width='<%= unpairedWidth %>' ></td>
                     <% } %>
              </table>
      </dt:column>
    </dt:table>

    <table class="infoTable" border="0">
      <tr>
	      <td><bean:message key="iteration.metrics.paired_label"/></td>
	      <td><table cellpadding="0" cellspacing="0" border="1"><tr><td height="8" bgcolor="#A0A0A0" width="40"></td></tr></table></td>
	      <td><bean:message key="iteration.metrics.unpaired_label"/></td>
	      <td><table border="1" cellpadding="0" cellspacing="0"><tr><td height="8" bgcolor="#C0C0C0" width="40"></td></tr></table></td>

    </table>


<!-- ----------------------------------------------------- -->

<div class="title">
  	<h1><bean:message key="iteration.metrics.accepted_hours"/>
</h1></div>
<p class="highlighted_message">
  <dt:table id="acceptedTable" htmlId="acceptedTable" uid="acceptedTable" defaultsort="1" name="metrics" property="developerAcceptedTime" styleClass="objecttable" requestURI="">
</p>
    <% final DeveloperMetrics developerMetrics = (DeveloperMetrics)acceptedTable;
       if(developerMetrics != null){
            pageContext.setAttribute("developerMetrics", developerMetrics);
       }%>

    <dt:setProperty name="paging.banner.placement" value="bottom" />

      <dt:column sortable="true" htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.developer") %>'
                sortProperty="name" title='<%= messages.getMessage("iteration.metrics.tableheading.developer") %>'>
        <logic:notEqual name="developerMetrics" property="acceptedHours" value="0">
        <% if (!developerMetrics.getName().equals(IterationMetrics.UNASSIGNED_NAME) ) { %>
            <xplanner:link page="/do/view/person" paramId="oid" paramName="developerMetrics" paramProperty="id">
	        ${developerMetrics.name}</xplanner:link>
         <% } else { %>
            ${developerMetrics.name}
         <% } %>
        </logic:notEqual>
      </dt:column>
      <dt:column align="right" sortable="true" sortProperty="acceptedHours" title='<%= messages.getMessage("iteration.metrics.tableheading.total") %>'
         htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.total_accepted")%>' >
        <logic:notEqual name="developerMetrics" property="acceptedHours" value="0">
                <%= DecimalFormat.format(pageContext, developerMetrics.getAcceptedStoryHours() +
                                                      developerMetrics.getAcceptedTaskHours(), "&nbsp;") %>
        </logic:notEqual>
      </dt:column>
      <dt:column align="right" sortable="true" sortProperty="acceptedHours" title='<%= messages.getMessage("iteration.metrics.tableheading.story") %>'
        htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.story")%>' >
        <logic:notEqual name="developerMetrics" property="acceptedHours" value="0">
                <%= DecimalFormat.format(pageContext, developerMetrics.getAcceptedStoryHours(), "&nbsp;") %>
        </logic:notEqual>
      </dt:column>
      <dt:column align="right" sortable="true" sortProperty="acceptedHours" title='<%= messages.getMessage("iteration.metrics.tableheading.task") %>'
         htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.task")%>' >
        <logic:notEqual name="developerMetrics" property="acceptedHours" value="0">
                <%= DecimalFormat.format(pageContext, developerMetrics.getAcceptedTaskHours(), "&nbsp;") %>
        </logic:notEqual>
      </dt:column>
      <dt:column align="right" sortable="true" sortProperty="ownTaskHours" title='<%= messages.getMessage("iteration.metrics.tableheading.worked_hours") %>'
        htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.worked_hours")%>' >
        <logic:notEqual name="developerMetrics" property="acceptedHours" value="0">
                <%= DecimalFormat.format(pageContext, developerMetrics.getOwnTaskHours(),"&nbsp;") %>
        </logic:notEqual>
      </dt:column>
      <dt:column align="right" sortable="true" sortProperty="remainingTaskHours" title='<%= messages.getMessage("iteration.metrics.tableheading.remaining_hours") %>'
        htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.remaining_hours")%>' >
        <logic:notEqual name="developerMetrics" property="remainingTaskHours" value="0">
                <%= DecimalFormat.format(pageContext, developerMetrics.getRemainingTaskHours(),"&nbsp;") %>
        </logic:notEqual>
      </dt:column>
     <dt:column sortable="true" sortProperty="ownTaskHours" title='<%= messages.getMessage("iteration.metrics.tableheading.worked_outof_accepted") %>'
        htmlTitle='<%= messages.getMessage("iteration.metrics.headertooltip.worked_outof_accepted") %>'>
        <logic:notEqual name="developerMetrics" property="acceptedHours" value="0">
        <table id="metrics1" >
             <%
                 long maxWidth = 400;
                 long acceptedHoursWithNoWorkedWidth = Math.round(developerMetrics.getRemainingTaskHours()/metrics.getMaxAcceptedTime()*maxWidth);
                 long ownTaskWorkedHoursWidth =  Math.round(developerMetrics.getOwnTaskHours()/metrics.getMaxAcceptedTime()*maxWidth);
             %>
                 <%if (ownTaskWorkedHoursWidth > 0) { %>
                 <td class="pairedHours" width='<%= ownTaskWorkedHoursWidth %>' bgcolor="#A0A0A0"></td>
                 <% } %>
                 <% if (acceptedHoursWithNoWorkedWidth > 0) { %>
                 <td class="unpairedHours" align="right" width='<%= acceptedHoursWithNoWorkedWidth %>' bgcolor="#C0C0C0"></td>
                 <% } %>
                 </table>
        </logic:notEqual>
      </dt:column>
    </dt:table>
    <table class="infoTable" border="0">
      <tr>
	      <td><bean:message key="iteration.metrics.worked_label"/></td>
	      <td><table cellpadding="0" cellspacing="0" border="1"><tr><td height="8" bgcolor="#A0A0A0" width="40"></td></tr></table></td>
	      <td><bean:message key="iteration.metrics.accepted_label"/></td>
	      <td><table border="1" cellpadding="0" cellspacing="0"><tr><td height="8" bgcolor="#C0C0C0" width="40"></td></tr></table></td>
    </table>

</xplanner:content>

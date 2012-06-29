<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>


<%@page import="com.technoetic.xplanner.format.DecimalFormat"%><tiles:put name="beanName">iteration</tiles:put>
<tiles:put name="titlePrefix">iteration.prefix</tiles:put>
<tiles:put name="titleSuffix"></tiles:put>
<tiles:put name="actions" direct="true"><jsp:include page="iteration/links.jsp" flush="true"/> </tiles:put>
<tiles:put name="globalActions" direct="true"><jsp:include page="iteration/globalLinks.jsp" flush="true"/> </tiles:put>
<tiles:put name="progress" direct="true">
<div class="iterationCard">
  <div class="iterationCardHeader">
	  <strong>(<xplanner:formatDate formatKey="format.date" name="iteration" property="startDate"/>
	    <bean:message key="iteration.to"/>
	    <xplanner:formatDate formatKey="format.date" name="iteration" property="endDate"/>)</strong>
	  <jsp:include page="iteration/progress.jsp" flush="true"/>
  </div>
  <logic:notEmpty name="iteration" property="description">
    <div class="description">
      <xplanner:twiki name="iteration" property="description"/>
    </div>
    <hr class="delimeter"/>
  </logic:notEmpty>
  <bean:size id="storyCount" name="iteration" property="userStories"/>
  <logic:greaterThan name="storyCount" value="0" >
    <div class="tableTitle">
      <bean:message key="iteration.label.hours"/>
      <bean:message key="iteration.label.estimated"/>
        <%= DecimalFormat.format(pageContext, iteration.getEstimatedHours()) %>,
      <bean:message key="iteration.label.actual"/>
        <%= DecimalFormat.format(pageContext, iteration.getCachedActualHours()) %>,
      <bean:message key="iteration.label.remaining"/>
        <%= DecimalFormat.format(pageContext, iteration.getTaskRemainingHours()) %>
    </div>
  </logic:greaterThan>
</div>
</tiles:put>

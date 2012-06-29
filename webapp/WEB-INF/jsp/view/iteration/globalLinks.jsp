<%@page import="org.hibernate.Hibernate"%>
<%@ page import="com.technoetic.xplanner.actions.AbstractAction,
                 com.technoetic.xplanner.domain.IterationStatus,
                 com.technoetic.xplanner.views.*,
                 com.technoetic.xplanner.XPlannerProperties"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<bean:define id="iteration" name="iteration" type="net.sf.xplanner.domain.Iteration"/>
<c:if test="${iteration.active}">
	<xplanner:link page="/do/view/dashboard" paramId="iterationId" paramName="iteration" paramProperty="id">
	  <bean:message key="iteration.link.dashboard"/>
	</xplanner:link>
</c:if>

  <!-- DEBT(DATADRIVEN) Move export links to the domain meta data repository-->
  <jsp:include page="../exportLinks.jsp"/> 
<xplanner:link page="/do/view/iteration/tasks" paramId="oid" paramName="iteration" paramProperty="id">
  <bean:message key="iteration.link.all_tasks"/>
</xplanner:link> 
<%--FEATURES:--%>
<%--<xplanner:link page="/do/view/iteration/features" paramId="oid" paramName="iteration" paramProperty="id">--%>
<%--  <bean:message key="iteration.link.all_features"/>--%>
<%--</xplanner:link> |--%>
<xplanner:link page="/do/view/iteration/metrics" paramId="oid" paramName="iteration" paramProperty="id">
  <bean:message key="iteration.link.metrics"/>
</xplanner:link> 

<%
  if ("on".equals(new XPlannerProperties().getProperty("xplanner.statistics"))) { %>
<xplanner:link page="/do/view/iteration/statistics" paramId="oid" paramName="iteration" paramProperty="id">
  <bean:message key="iteration.link.statistics"/></xplanner:link> 
<% } %>
<xplanner:link page="/do/view/iteration/accuracy" paramId="oid" paramName="iteration" paramProperty="id">
    <bean:message key="iteration.link.accuracy"/></xplanner:link> 

 <xplanner:link page="/do/view/history">
    <bean:message key="history.link"/>
    <xplanner:linkParam id="oid" name="iteration" property="id"/>
    <xplanner:linkParam id='<%= AbstractAction.TYPE_KEY %>' value='<%= Hibernate.getClass(iteration).getName() %>'/>
 </xplanner:link>

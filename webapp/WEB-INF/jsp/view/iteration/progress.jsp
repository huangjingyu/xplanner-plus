<%@ page import="com.technoetic.xplanner.actions.AbstractAction,
                net.sf.xplanner.domain.Iteration,
                 com.technoetic.xplanner.domain.IterationStatus"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>


  <bean:define id="iteration" name="iteration" type="net.sf.xplanner.domain.Iteration"/>
  	    <xplanner:progressBar
                  actual='<%= iteration.getCachedActualHours() %>'
                  estimate='<%= iteration.getEstimatedHours() %>'
                  complete="false"
                  width="150px" height="8"/>

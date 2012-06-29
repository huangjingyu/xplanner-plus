<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ page import="org.hibernate.UnresolvableObjectException,
                 org.apache.struts.Globals
                 "%><%@ page import="com.technoetic.xplanner.format.AbstractFormat"%><%@ page import="com.technoetic.xplanner.domain.repository.ObjectNotFoundException"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<xplanner:content>

    <xplanner:contentTitle titleKey="error.title"/>
    <span class="title"><bean:message key="error.title"/></span><p/>
    <span class="error_message">
  <%
  if (exception == null) {
    exception = (Exception) request.getAttribute("exception");
  }
  if (exception == null) {
    exception = (Throwable) request.getAttribute(Globals.EXCEPTION_KEY);
  }
  if (exception instanceof UnresolvableObjectException) {
%>
   <bean:message key="error.objectNotFound" arg0='<%= ((UnresolvableObjectException)exception).getIdentifier().toString() %>'/></span>
<% } else if (exception instanceof ObjectNotFoundException) {%>
   <bean:message key="error.objectNotFound" arg0='<%= request.getParameter("oid") %>'/></span>
<% } else {%>
     <html:messages id="message">
       <%=message%><br/>
     </html:messages>
<% } %>
</span>
</xplanner:content>
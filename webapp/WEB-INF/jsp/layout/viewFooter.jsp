<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<bean:define id="object" name="object" type="net.sf.xplanner.domain.DomainObject"/>
<jsp:include page="/WEB-INF/jsp/view/notes.jsp" flush="true">
  <jsp:param name="oid" value='<%= Integer.toString(object.getId()) %>'/>
</jsp:include>


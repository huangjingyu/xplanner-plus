<%@page import="org.hibernate.Hibernate"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.format.*,
                 com.technoetic.xplanner.actions.AbstractAction" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.technoetic.xplanner.actions.UpdateTimeAction" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<db:useBean id="feature" type="com.technoetic.xplanner.domain.Feature" scope="request"/>
<xplanner:content definition="tiles:view">

<tiles:put name="beanName">feature</tiles:put>
<tiles:put name="titlePrefix">feature.prefix</tiles:put>
<tiles:put name="showTitle">false</tiles:put>

<p>
<table width="60%" border="0" class="card"
		background="<html:rewrite page="/images/cardbg.gif"/>">
<tr>
  <td>
    <table width="100%" cellspacing="0">
      <tr>
        <td nowrap>
          <p><span class="title"><bean:message key="feature.prefix"/> </span>
		     <span class="object_name">${feature.name}</span></p>
        </td>
      </tr>
	  <tr><td colspan="2" height="16">&nbsp;</td></tr>
      <tr>
        <td colspan="2">
			<xplanner:twiki name="feature" property="description"/>&nbsp;
        </td>
      </tr>
	  <tr><td colspan="2" height="16">&nbsp;</td></tr>
    </table>
  </td>
</tr>
</table>
</p>
<tiles:put name="actions" direct="true">
<xplanner:isUserAuthorized name="feature" permission="edit">
    <xplanner:link page="/do/edit/feature"
        paramId="oid" paramName="feature" paramProperty="id"
        fkey='<%=feature.getUserStory().getId()%>'>
      <bean:message key="feature.link.edit"/>
    </xplanner:link>
</xplanner:isUserAuthorized> |
 <xplanner:link page="/do/view/history">
    <bean:message key="history.link"/>
    <xplanner:linkParam id="oid" name="feature" property="id"/>
    <xplanner:linkParam id='<%= AbstractAction.TYPE_KEY %>' value='<%= Hibernate.getClass(feature).getName() %>'/>
 </xplanner:link>
</tiles:put>
</xplanner:content>

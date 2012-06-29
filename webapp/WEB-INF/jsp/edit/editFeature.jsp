<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<xplanner:content definition="tiles:edit" >

<bean:define id="feature" name="edit/feature" type="com.technoetic.xplanner.forms.FeatureEditorForm" />

<logic:present parameter="oid">
    <tiles:put name="editMessage">feature.editor.edit_prefix</tiles:put>
    <tiles:put name="titlePrefix">feature.editor.edit_prefix</tiles:put>
    <tiles:put name="titleSuffix"><%=feature.getName()%></tiles:put>
    <tiles:put name="showTitleSuffix">true</tiles:put>
    <tiles:put name="objectType">com.technoetic.xplanner.domain.Feature</tiles:put>
</logic:present>
<logic:notPresent parameter="oid">
    <tiles:put name="editMessage">feature.editor.create</tiles:put>
    <tiles:put name="titlePrefix">feature.editor.create</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.UserStory</tiles:put>
</logic:notPresent>

<%-- rewrite is necessary for cookie-less sessions --%>
<form method="post" action="<html:rewrite page="/do/edit/feature"/>" name="featureEditor">
  <logic:present parameter="oid">
    <bean:parameter id="oid" name="oid"/>
    <input type="hidden" name="oid" value='<%=oid%>'>
  </logic:present>
  <bean:parameter id="returnto" name="returnto"/>
  <input type="hidden" name="returnto" value='<%=returnto%>'>
  <bean:parameter id="fkey" name="fkey"/>
  <input type="hidden" name="fkey" value='<%=fkey%>'>
  <input type="hidden" name="storyId" value='<%=fkey%>'>
  <input type="hidden" name="id" value='<%= feature.getId() %>'>
  <table cellpadding="4" bgcolor="#C0C0C0" class="editor">
	  <tr>
		<td nowrap valign="top"><b><bean:message key="feature.editor.name"/></b></td>
		<td><html:text name="feature" property="name" size="40"/></td>
	  </tr>
	  <tr>
		<td nowrap valign="top"><b><bean:message key="feature.editor.description"/></b></td>
                <td align="right">
                  <%@ include file="/WEB-INF/jsp/common/formattingHelpLink.jsp" %>
                </td>
	  </tr>
	  <tr>
		<td colspan="2">
		  <html:textarea name="feature" property="description" cols="60" rows="10"/>
		</td>
	  </tr>
	  <tr>
		<td colspan="2" valign="top">
		  <logic:present parameter="oid">
              <input type="hidden" name="action"
                value='<%= com.technoetic.xplanner.actions.EditObjectAction.UPDATE_ACTION %>'>
			  <input type="submit" value="<bean:message key="form.update"/>">
		  </logic:present>
		  <logic:notPresent parameter="oid">
              <input type="hidden" name="action"
                value='<%= com.technoetic.xplanner.actions.EditObjectAction.CREATE_ACTION %>'>
			  <input type="submit" value="<bean:message key="form.create"/>">
		  </logic:notPresent>
		  <input type="reset" value="<bean:message key="form.reset"/>">
		</td>
	  </tr>
  </table>
</form>

  <script language="JavaScript">
    document.forms["featureEditor"].name.focus();
  </script>



</xplanner:content>

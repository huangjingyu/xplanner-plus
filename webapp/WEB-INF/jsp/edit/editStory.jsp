<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.Globals,
                 com.technoetic.xplanner.format.DecimalFormat,
                 com.technoetic.xplanner.domain.StoryDisposition,
                 com.technoetic.xplanner.domain.StoryStatus"%>
<%@ page import="net.sf.xplanner.domain.UserStory"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>

<xplanner:content definition="tiles:edit" >
<bean:define id="story" name="edit/userstory" type="com.technoetic.xplanner.forms.UserStoryEditorForm" />

<logic:present parameter="oid">
    <tiles:put name="editMessage">story.editor.edit_prefix</tiles:put>
    <tiles:put name="titlePrefix">story.editor.edit_prefix</tiles:put>
    <tiles:put name="titleSuffix"><%=story.getName()%></tiles:put>
    <tiles:put name="showTitleSuffix">true</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.UserStory</tiles:put>
</logic:present>
<logic:notPresent parameter="oid">
    <tiles:put name="editMessage">story.editor.create</tiles:put>
    <tiles:put name="titlePrefix">story.editor.create</tiles:put>
    <tiles:put name="objectType">net.sf.xplanner.domain.Iteration</tiles:put>
</logic:notPresent>

<bean:define id="messages" name='<%= Globals.MESSAGES_KEY %>' scope="application"
   type="org.apache.struts.util.MessageResources" />
<%-- rewrite is necessary for cookie-less sessions --%>
<form method="post" action="<html:rewrite page="/do/edit/userstory"/>" name="storyEditor">
  <div id="editObject" >
	<logic:present parameter="oid">
	    <bean:parameter id="oid" name="oid"/>
	    <input type="hidden" name="oid" value='<%=oid%>'>
	</logic:present>
	<bean:parameter id="fkey" name="fkey"/>
	<bean:parameter id="returnto" name="returnto"/>
    <input type="hidden" name="iterationId" value='<%=fkey%>'/>
    <input type="hidden" name="id" value='<%= story.getId() %>'>
	<input type="hidden" name="returnto" value='<%=returnto%>'>
	<input type="hidden" name="fkey" value='<%=fkey%>'>

	<table class="editor">
	  <tr>
		<th nowrap valign="top"><b><bean:message key="story.editor.name"/> </b></th>
		<td class="secondColumn"><div class="borderedInput"><div><html:text name="story" property="name" size="40"/></div></div></td>
	  </tr>
     <tr>
     <th nowrap valign="top"><b><bean:message key="story.editor.disposition"/> </b></th>
     <td><div class="borderedInput"><div><html:select name="story" property="dispositionName">
       <html:option value='<%= StoryDisposition.PLANNED.getName()%>'>
         <bean:message key='<%= StoryDisposition.PLANNED.getNameKey() %>'/>
        </html:option>
		<html:option value='<%= StoryDisposition.CARRIED_OVER.getName()  %>'>
         <bean:message key='<%= StoryDisposition.CARRIED_OVER.getNameKey() %>'/>
        </html:option>
		<html:option value='<%= StoryDisposition.ADDED.getName() %>'>
           <bean:message key='<%= StoryDisposition.ADDED.getNameKey() %>'/>
        </html:option>
 </html:select></div></div>
 </td>
 </tr>
 <tr>
 <th nowrap valign="top"><b><bean:message key="story.editor.customer"/> </b></th>
		<td><div class="borderedInput"><div>
		  <html:select property="customerId" name="story">
			<html:option value="0"><bean:message key="person.not_assigned"/></html:option>
            <xplanner:personOptions/>
		  </html:select></div></div>
		</td>
	  </tr>
	  <tr>
		<th nowrap valign="top"><b><bean:message key="story.editor.tracker"/> </b></th>
		<td><div class="borderedInput"><div>
		  <html:select property="trackerId" name="story">
			<html:option value="0"><bean:message key="person.not_assigned"/></html:option>
            <xplanner:personOptions/>
		  </html:select></div></div>
		</td>
	  </tr>
     <tr>
      <th nowrap valign="top"><b><bean:message key="story.editor.status"/> </b></th>
      <td><div class="borderedInput"><div>
       <html:select name="story" property="statusName">
          <html:option value='<%= StoryStatus.DRAFT.getName() %>'>
            <bean:message key='<%= StoryStatus.DRAFT.getNameKey() %>'/>
          </html:option>
          <html:option value='<%= StoryStatus.DEFINED.getName() %>'>
            <bean:message key='<%= StoryStatus.DEFINED.getNameKey() %>'/>
          </html:option>
          <html:option value='<%= StoryStatus.ESTIMATED.getName() %>'>
           <bean:message key='<%= StoryStatus.ESTIMATED.getNameKey() %>'/>
          </html:option>
         <html:option value='<%= StoryStatus.PLANNED.getName()  %>' >
          <bean:message key='<%= StoryStatus.PLANNED.getNameKey() %>'/>
          </html:option>
          <html:option value='<%= StoryStatus.IMPLEMENTED.getName() %>'>
           <bean:message key='<%= StoryStatus.IMPLEMENTED.getNameKey() %>'/>
          </html:option>
          <html:option value='<%= StoryStatus.VERIFIED.getName() %>'>
           <bean:message key='<%= StoryStatus.VERIFIED.getNameKey() %>'/>
          </html:option>
          <html:option value='<%= StoryStatus.ACCEPTED.getName() %>'>
           <bean:message key='<%= StoryStatus.ACCEPTED.getNameKey() %>'/>
          </html:option>
       </html:select></div></div>
      </td>
     </tr>
	  <tr>
		<th valign="top"><b><bean:message key="story.editor.priority"/></b></th>
		<td>
			<div class="borderedInput"><div><html:text name="story" property="priority" size="40"/></div></div>
		</td>
	  <tr>
    <tr>
    <th valign="top"><b><bean:message key="story.editor.order"/></b></th>
    <td>
      <div class="borderedInput"><div><html:text name="story" property="orderNo" size="40"/></div></div>
    </td>
    <tr>
	    <th nowrap valign="top">
        <b><bean:message key="story.editor.estimated_hours"/> </b>
	    </th>
			<td>
				<logic:greaterThan name="story" property="taskBasedEstimatedHours" value="0">
				    <%= DecimalFormat.format(pageContext, story.getTaskBasedEstimatedHours()) %>
				</logic:greaterThan>
				<logic:equal name="story" property="taskBasedEstimatedHours" value="0">
				    <div class="borderedInput"><div><html:text name="story" property='<%=UserStory.ESTIMATED_HOURS%>' size="5"/></div></div>
				</logic:equal>
      </td>
	  </tr>
	  <tr>
		<td colspan="2" valign="top">
			<table width="100%" border="0"><tr>
				<td><b><bean:message key="task.editor.description"/></b> (<bean:message key="form.maxtext"/>)</td>
             <td align="right">
             <%@ include file="/WEB-INF/jsp/common/formattingHelpLink.jsp" %>
              </td>
			</tr></table>
		</td>
	  </tr>
	  <tr>
		  <td colspan="2"><div class="borderedInput"><div><html:textarea name="story" property="description" cols="60" rows="10" /></div></div></td>
	  </tr>
	  <tr>
		<td nowrap valign="top" colspan="2" class="objecttable_buttons">
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
  </div>
</form>

  <script language="JavaScript">
    document.forms["storyEditor"].name.focus();
  </script>


</xplanner:content>

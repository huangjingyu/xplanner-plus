<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.forms.IterationStatusEditorForm" %><%@ page import="com.technoetic.xplanner.actions.UpdateTimeAction"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<xplanner:content>
  <bean:define id="iteration" name="edit/iteration" type="net.sf.xplanner.domain.Iteration" />
  <bean:define id="startedIterationsNr" name="startedIterationsNr" type="java.lang.Integer" />
<xplanner:contentTitle>
    XPlanner -
        <bean:message key="iteration.status.editor.start_prefix"/>
        ${iteration.name}
</xplanner:contentTitle>

<xplanner:navigation oid='<%= iteration.getId() %>'
	type="net.sf.xplanner.domain.Iteration" inclusive="true"/>

<html:errors/>

<p>
    <span class="title"><h1><bean:message key="iteration.status.editor.start_prefix"/> </span>
        <a href="">${iteration.name}</a></h1>
</p>

<%--  TODO Extract the following form to a OkCancelDialog. Popup w/o adorment might be better--%>
<%--  FIXME use a base Iteration form (refactor common out of others)--%>
<%--TODO change changeStatus to start everywhere--%>

  <form method="post" action="<html:rewrite page="/do/start/iteration"/>"  name="changeStatus">
     <bean:parameter id="returnto" name="returnto"/>
    <input type="hidden" name="oid" value="${iteration.id}" />
    <input type="hidden" name="fkey" value='<%= request.getParameter("fkey") %>' />
    <input type="hidden" name="returnto" value='<%=returnto%>' />
    <input type="hidden" name="operation" value="start" />
    <input type="hidden" name="iterationStartConfirmed" value="true" />
    <input type="hidden" name="<%=IterationStatusEditorForm.SAVE_TIME_ATTR%>"
      value="<%=request.getAttribute(IterationStatusEditorForm.SAVE_TIME_ATTR)%>" />

 <div id="editObject" >
    <table border="0">
      <% if(UpdateTimeAction.isFromUpdateTime(request)) { %>
      <tr>
        <td><bean:message key="iteration.status.editor.message_1"/><br/></td>
      </tr>
      <%}%>
      <%if(startedIterationsNr.intValue() > 0){%>
        <tr>
          <td>
            <bean:message key="iteration.status.editor.message_2" arg0='<%=""+startedIterationsNr.intValue()%>' />
            <br/>
            <input id="closeIterations" name="closeIterations" type="checkbox" value="on" checked="checked"><bean:message key="iteration.status.editor.message_3"/>
            <br/>
          </td>
        </tr>
      <%}%>
      <tr>
        <td><br/><bean:message key="iteration.status.editor.message_4"/></td>
      </tr>

    </table>

    <table class="editor">
    <tr class="objecttable_buttons"> <td >
<%-- TODO Investigate ways to use struts so we don't need javascript. Rational: ATs won't run javascript--%>
<%--TODO extract start into constant and use the same in the action class--%>
    <input type="submit" name="start" id="start" value="<bean:message key="ok"/>" />
    <input type="button" id="cancel" name="cancel" value="<bean:message key="cancel"/>"
           onclick="document.location='<%=request.getContextPath()+returnto%>';"/>
    </td> </tr>
    </table>
</div>
  <script language="JavaScript">
    document.forms['changeStatus'].start.focus();
  </script>



</xplanner:content>

<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.security.auth.SystemAuthorizer,
                 com.technoetic.xplanner.security.SecurityHelper,
                 com.technoetic.xplanner.db.hibernate.ThreadSession,
                 java.util.Date,
                 java.util.Iterator,
                 org.apache.commons.collections.CollectionUtils,
                 org.apache.commons.collections.Predicate,
                 net.sf.xplanner.domain.Iteration,
                 net.sf.xplanner.domain.Project,
                 com.technoetic.xplanner.security.AuthenticationException"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>


<xplanner:content>
<xplanner:contentTitle titleKey="rights.title"/>


<style>
.page_body {
  border-top: solid 2px #c0c0c0;
  padding-top: 0.2em;
}
</style>
<db:useBean id="project" type="net.sf.xplanner.domain.Project" oid='<%= new Integer(request.getParameter(\"oid\")) %>' />
<bean:define id="roles" name="edit/roles" type="com.technoetic.xplanner.forms.RoleEditorForm" />
 
<xplanner:contentTitle>
    XPlanner - Projet:${project.name}
   
        
    
</xplanner:contentTitle>

<xplanner:navigation back="true"/>

<html:errors/>

<span class="title"><bean:message key="project.prefix"/></span>
<span class="object_name">${project.name}</span>

<xplanner:outline/>

<%-- rewrite is necessary for cookie-less sessions --%>
<form method="post" action="<html:rewrite page="/do/edit/roles"/>">
    <xplanner:isUserAuthorized name="project" permission="admin.edit.role">

 <bean:parameter id="returnto" name="returnto"/>
 <bean:parameter id="oid" name="oid"/>
    <input type="hidden" name="oid" value='<%=oid%>'>
 <input type="hidden" name="returnto" value='<%=returnto%>'>
 <input type="hidden" name="id" value='<%= project.getId() %>'>
  <table class="objecttable" border="0" bgcolor="#f0f0f0">
	  <tr>
                <th><bean:message key="roles.tableheading.persons"/></th>
                <th><bean:message key="roles.tableheading.role"/></th>
            </tr>
<db:useBeans id="persons" type="net.sf.xplanner.domain.Person" where="hidden = false" order="name" />
<logic:iterate id="personne" indexId="index" name="persons" type="net.sf.xplanner.domain.Person">
              <tr>  
               <td><bean:write name="personne" property="name"/></td>
                <td>
                    <input type="hidden" name="personId[<%=index%>]" value='<%=personne.getId()%>'>
                    <select name="personRole[<%=index%>]">
                        <option value="viewer"
                            <%= roles.isRoleSelected("viewer", personne.getId()) %>><bean:message key="person.editor.role.viewer"/>
                        </option>
                        <option value="editor"
                            <%= roles.isRoleSelected("editor", personne.getId()) %>><bean:message key="person.editor.role.editor"/>
                        </option>
                        <option value="admin"
                            <%= roles.isRoleSelected("admin", personne.getId()) %>><bean:message key="person.editor.role.admin"/>
                        </option>
                    </select>
            </tr>
 </logic:iterate>
</table>
 <table>
    <tr>
        <td>
   <input type="hidden" name="action"
                value='<%= com.technoetic.xplanner.actions.EditObjectAction.UPDATE_ACTION %>'>
   <input type="submit" value="<bean:message key="form.update"/>">
        </td>
        <td>
         <input type="reset" value="<bean:message key="form.reset"/>">
         </td>
     </tr>
  </table>
    </xplanner:isUserAuthorized>

</xplanner:content>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.security.SecurityHelper,
                 com.technoetic.xplanner.security.auth.SystemAuthorizer,
                 com.technoetic.xplanner.db.hibernate.ThreadSession,
                 org.apache.commons.lang.StringEscapeUtils,
                 com.technoetic.xplanner.util.StringUtilities"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>

<bean:parameter id="oid" name="oid"/>
<db:useBeans id="notes" type="net.sf.xplanner.domain.Note"
    where='<%= "attachedToId=" + oid %>' order='<%="submissionTime" %>'/>

<%-- viewers have no need to see the notes bar if there are no notes. --%>
<bean:define id="domainContext" name="domainContext" type="com.technoetic.xplanner.tags.DomainContext"/>
<%
    boolean isNoteEditor = SystemAuthorizer.get().hasPermission(
            domainContext.getProjectId(), SecurityHelper.getRemoteUserId(request), "note", 0, "edit");
%>
<% if (isNoteEditor || notes.size() > 0) { %>
<table class="noteSectionHeader">
  <tr>
    <td><span class="noteSectionLabel"><bean:message key="notes.label.notes"/></span></td>
    <td nowrap>
      <div align="right">
        <% if (isNoteEditor) { %>
            <span class="noteSectionLinks">
		    <xplanner:link page="/do/edit/note" paramId="attachedToId" paramName="oid">
		        <bean:message key="note.create"/>
            </xplanner:link>
            </span>
        <% } %>
	  </div>
    </td>
  </tr>
</table>
<% } %>

<bean:size id="notesCount" name="notes"/>
<logic:greaterThan name="notesCount" value="0" >
    <logic:iterate name="notes" scope="page" id="note" indexId="n"
                   type="net.sf.xplanner.domain.Note">
        <table class="notesTable">
          <tr>
            <td class="noteHeader">
            <div>
                <xplanner:isUserAuthorized name="note" permission="edit"
                    principalId='<%=SecurityHelper.getRemoteUserId(request)%>'
                    allowedUser='<%= note.getAuthorId() %>'>
                        <xplanner:link page="/do/edit/note">
                            <html:img page="/images/edit.png" alt="edit" border="0"/>
                            <xplanner:linkParam id="oid" name="note" property="id"/>
                            <xplanner:linkParam id="attachedToId" name="note" property="attachedToId"/>
                        </xplanner:link>
                        <xplanner:link page="/do/delete/note" onclick='<%="return confirm(\'Do you want to delete note " + StringUtilities.replaceQuotationMarks(StringEscapeUtils.escapeJavaScript(note.getSubject())) + "?\')"%>'>
                            <html:img page="/images/delete.png" alt="delete" border="0"/>
                            <xplanner:linkParam id="oid" name="note" property="id"/>
                        </xplanner:link>
              </xplanner:isUserAuthorized>
            </div>
            <div>
              <ul>
                <li><strong><bean:message key="notes.label.subject"/></strong>
                    ${note.subject}
                </li>
                <li>
                <db:useBean id="author" type="net.sf.xplanner.domain.Person"
                                  oid='<%= new Integer(note.getAuthorId()) %>'/>
                  <strong><bean:message key="notes.label.author"/> </strong>
                                    <html:link page="/do/view/person" paramId="oid" paramName="author" paramProperty="id">
                    ${author.name}</html:link>
                </li>
                <li>
                  <strong><bean:message key="notes.label.date"/> </strong>
                  <xplanner:formatDate formatKey="format.datetime" name="note" property="submissionTime"/>
                </li>
                <logic:present name="note" property="file">
                    <logic:notEqual name="note" property="file.name" value="">
                      <li>
                        <strong><bean:message key="notes.label.attachment"/> </strong>
                          <html:link page="/do/download/attachment" paramId="oid" paramName="note" paramProperty="id">
                            <bean:write name="note" property="file.name"/>
                          </html:link>
			                </li>
			                <li>
			                 <strong><bean:message key="notes.label.attachment.count"/> </strong>
                       <%= note.getAttachmentCount() %>
			                </li>
                    </logic:notEqual>
                </logic:present>
              </ul>
            </div>
            </td>
          <tr>
            <td class="noteBody"><div><xplanner:twiki name="note" property="body"/></div></td>
          </tr>
        </table>
    </logic:iterate>

</logic:greaterThan>


<%-- EXPERIMENTAL - This is a sample presentation for the file manager functionality --%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<xplanner:content>
<xplanner:contentTitle>XPlanner File Manager</xplanner:contentTitle>
    <tags:breadcrumb />

<div style="margin-bottom: 0em; padding-bottom: 0em">
    <img src="<html:rewrite page="/images/folder.gif"/>"/>
    <html:link page="/do/view/directory?action=list"
        paramId="directoryId" paramName="root" paramProperty="id" >/</html:link><br/>
    <logic:present name="directory" property="parent">
        <img src="<html:rewrite page="/images/folder.gif"/>"/>
        <html:link page="/do/view/directory?action=list"
                paramId="directoryId" paramName="directory" paramProperty="parent.id" >..</html:link><br/>
    </logic:present>
    <logic:present name="directory" property="parent">
        <img src="<html:rewrite page="/images/folder.gif"/>"/>
        <html:link page="/do/view/directory?action=list"
            paramId="directoryId" paramName="directory" paramProperty="id" >
                <bean:write name="directory" property="name"/>
        </html:link>
   </logic:present>
</div>

<div style="margin-left: 0.5em;">
    <table border="0">
    <logic:iterate id="subdirectory" name="directory" property="subdirectories">
    <tr><td>
    <img src="<html:rewrite page="/images/folder.gif"/>"/>
         <html:link page="/do/view/directory?action=list"
                paramId="directoryId" paramName="subdirectory" paramProperty="id" >
            <bean:write name="subdirectory" property="name"/>
         </html:link>
    </td>
        <td>
        <xplanner:link page="/do/view/directory">
            <xplanner:linkParam id="action" value="rmdir"/>
            <xplanner:linkParam id="directoryId" name="subdirectory" property="id"/>
            <img border="0" src="<html:rewrite page="/images/delete.gif"/>"/></xplanner:link>
        </td>
    </tr>
    </logic:iterate>
    <logic:iterate id="file" name="directory" property="files">
    <tr><td style="padding-right: 2em;">
    <img src="<html:rewrite page="/images/file.gif"/>"/>
        <html:link page="/do/view/directory?action=download"
            paramId="fileId" paramName="file" paramProperty="id" >
                <bean:write name="file" property="name"/>
        </html:link>
        </td>
        <td>
        <xplanner:link page="/do/view/directory">
            <xplanner:linkParam id="action" value="delete"/>
            <xplanner:linkParam id="fileId" name="file" property="id"/>
            <xplanner:linkParam id="directoryId" name="directory" property="id"/>
            <img border="0" src="<html:rewrite page="/images/delete.gif"/>"/></xplanner:link>
        </td>
    </tr>
    </logic:iterate>
    </table>
    </div>
    <hr/>
    <%-- do-before-release clean up action names for file manager --%>
    <html:form action="/view/directory">
        <html:hidden property="action" value="mkdir"/>
        <html:hidden property="directoryId"/>
        <html:text property="name"/>
        <html:submit value="Create Directory"/>
    </html:form>
    <div id="upload">
    <html:form action="/view/directory" enctype="multipart/form-data">
        <html:hidden property="action" value="upload"/>
        <html:hidden property="directoryId"/>
        <html:file property="formFile"/>
        <html:submit value="Upload"/>
    </html:form>
    </div>

</xplanner:content>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="com.technoetic.xplanner.domain.Integration,
                 com.technoetic.xplanner.format.DecimalFormat,
                 org.apache.struts.Globals" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>

<bean:parameter id="projectId" name="projectId"/>

<xplanner:content>
    <xplanner:contentTitle titleKey="integrations.title"/>
    <xplanner:navigation oid='<%= Integer.parseInt(projectId) %>' type="net.sf.xplanner.domain.Project"/>

<span class="title"><bean:message key="integrations.title"/></span>

<html:errors/>


<db:useBeans id="currentIntegrations" where="state = ? and projectId = ?"
        type="com.technoetic.xplanner.domain.Integration">
    <db:useBeansParameter value='<%= new Character(Integration.ACTIVE) %>' type="char"/>
    <db:useBeansParameter value='<%= projectId %>' type="string"/>
</db:useBeans>

<db:useBeans id="pendingIntegrations" where="state = ? and projectId = ?"
        type="com.technoetic.xplanner.domain.Integration">
    <db:useBeansParameter value='<%= new Character(Integration.PENDING) %>' type="char"/>
    <db:useBeansParameter value='<%= projectId %>' type="string"/>
</db:useBeans>

<html:form action="edit/integrations" method="post">
  <input type="hidden" name="projectId" value='<%= projectId %>'/>
<%
Integration currentIntegration = null;
if (currentIntegrations.size() > 0) {
    currentIntegration = (Integration)currentIntegrations.iterator().next();
    pageContext.setAttribute("currentIntegration", currentIntegration);
}
%>

<logic:present name="currentIntegration">
    <table cellpadding="1" class="description">
        <tr>
            <td>
                <db:useBean id="person" type="net.sf.xplanner.domain.Person"
                    oid='<%= new Integer(currentIntegration.getPersonId()) %>'/>
                <p><strong><bean:message key="integrations.current_integrator"/>:</strong>
                    <xplanner:link page="/do/view/person" paramId="oid" paramName="person" paramProperty="id">
                        <bean:write name="person" property="name"/>
                    </xplanner:link>
                    <br>
                    <strong><bean:message key="integrations.started_at"/>:</strong>
                    <xplanner:formatDate  formatKey="format.datetime"
                        name="currentIntegration" property="whenStarted"/>
                </p>
                <p><bean:write name="currentIntegration" property="comment"/></p>
                <p>
                    <xplanner:isUserAuthorized resourceType="system.project" permission="integrate">
                      <input type="submit" name="action.finish" value="Finished">
                      <input type="submit" name="action.cancel" value="Cancel">
                    </xplanner:isUserAuthorized>
                </p>
            </td>
            <td valign="top">
                <p><img src="<html:rewrite page="/images/madhack.gif" />" width="48" height="48"></p>
            </td>
        </tr>
    </table>
</logic:present>

<logic:notPresent name="currentIntegration">
    <table class="description">
        <tr>
            <td>
                <strong>No current integration.</strong>
            </td>
        </tr>
    </table>
</logic:notPresent>
<br/>

<% if (pendingIntegrations.size() > 0 || currentIntegrations.size() > 0) { %>
    <p><strong><bean:message key="integrations.waiting_line"/>:</strong></p>
<% } %>

<% if (pendingIntegrations.size() > 0) { %>
    <table border="0" cellpadding="1" cellspacing="1" class="objecttable">
      <tr>
        <th><bean:message key="integrations.tableheading.who"/></th>
        <th><bean:message key="integrations.tableheading.since"/></th>
        <th><bean:message key="integrations.tableheading.what"/></th>
        <xplanner:isUserAuthorized resourceType="system.project" permission="integrate">
            <th><bean:message key="integrations.tableheading.actions"/></th>
        </xplanner:isUserAuthorized>
      </tr>
      <logic:iterate id="integration" collection='<%= pendingIntegrations %>' indexId="n"
                type="com.technoetic.xplanner.domain.Integration" >
            <db:useBean id="person" type="net.sf.xplanner.domain.Person"
                oid='<%= new Integer(integration.getPersonId()) %>'/>
            <tr>
                <td align="center">
                    <xplanner:link page="/do/view/person" paramId="oid" paramName="person" paramProperty="id">
                        <bean:write name="person" property="name"/>
                    </xplanner:link>
                </td>
                <td  align="center">
                    <xplanner:formatDate  formatKey="format.datetime" name="integration" property="whenRequested"/>
                </td>
                <td>
                    <bean:write name="integration" property="comment"/>
                </td>
                <xplanner:isUserAuthorized resourceType="system.project" permission="integrate">
                    <td bgcolor="#f0f0f0" align="center">
                        <% if (n.equals(new Integer(0)) && currentIntegrations.size() == 0) { %>
                            <input type="submit" name="action.start" value="Start"/>
                        <% } %>
                        <input type="submit"
                            name="action.leave.<bean:write name="integration" property="id"/>"
                            value="Leave Line"/>
                    </td>
                </xplanner:isUserAuthorized>
            </tr>
      </logic:iterate>
    </table>
    <br/>
<% } %>

<xplanner:isUserAuthorized resourceType="system.project" permission="integrate">
    <table border="0" cellpadding="1" cellspacing="1" class="objecttable">
      <tr>
        <td bgcolor="#f0f0f0" align="center">
              <xplanner:authenticatedUser id="user"/>
              <%
                    Integration defaultIntegration = new Integration();
                    defaultIntegration.setPersonId(user != null ? user.getId() : 0);
                    pageContext.setAttribute("defaultIntegration", defaultIntegration);
              %>
              <bean:message key="integrations.tableheading.who"/>: <html:select name="defaultIntegration" property="personId">
                <html:option value="0">&nbsp;</html:option>
                <xplanner:personOptions/>
              </html:select>
              <bean:message key="integrations.tableheading.what"/>: <input type="text" name="comment" size="40"/>
              <input type="submit" name="action.join"
                value="<%= currentIntegrations.size() == 0 && pendingIntegrations.size() == 0 ?
                    "Start" : "Join line" %>">
        </td>
      </tr>
    </table>
</xplanner:isUserAuthorized>

<db:useBeans id="recentIntegrations"
        type="com.technoetic.xplanner.domain.Integration"
        where="state in (?, ?) and projectId = ?" order="when_complete desc" size="20">
    <db:useBeansParameter value='<%= new Character(Integration.FINISHED) %>' type="char"/>
    <db:useBeansParameter value='<%= new Character(Integration.CANCELED) %>' type="char"/>
    <db:useBeansParameter value='<%= projectId %>' type="string"/>
</db:useBeans>


<p><strong><bean:message key="integrations.recent_integrations"/>:</strong></p>

<bean:size id="recentIntegrationCount" name="recentIntegrations" />

<logic:greaterThan name="recentIntegrationCount" value="0" >
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>
<dt:table id="objecttable" list='<%= recentIntegrations %>' border="0" styleClass="objecttable"
        requestURI="" defaultorder="descending" defaultsort="3" length="20" >
<%--    <dt:setProperty name="paging.banner.placement" value="bottom" />--%>
    <bean:define id="integration" name="objecttable" type="com.technoetic.xplanner.domain.Integration"/>
    <dt:column sortable="true" title='<%= messages.getMessage("integrations.tableheading.who")%>'>
        <db:useBean id="person" type="net.sf.xplanner.domain.Person"
            oid='<%= new Integer(integration.getPersonId()) %>'/>
        <xplanner:link page="/do/view/person" paramId="oid" paramName="person" paramProperty="id">
            <bean:write name="person" property="name"/>
        </xplanner:link>
    </dt:column>
    <dt:column sortable="true" align="center" title='<%= messages.getMessage("integrations.tableheading.start")%>'>
        <xplanner:formatDate  formatKey="format.datetime" name="integration" property="whenStarted"/>
    </dt:column>
    <dt:column sortable="true" align="center" title='<%= messages.getMessage("integrations.tableheading.finish")%>'>
        <xplanner:formatDate  formatKey="format.datetime" name="integration" property="whenComplete"/>
    </dt:column>
    <dt:column sortable="true" align="center" title='<%= messages.getMessage("integrations.tableheading.duration")%>'>
        <%= DecimalFormat.format(pageContext, integration.getDuration()) %>
    </dt:column>
    <dt:column sortable="true" align="center" title='<%= messages.getMessage("integrations.tableheading.state")%>'>
        <logic:equal name="integration" property="state"
                value='<%= ""+Integration.FINISHED %>'>
            <bean:message key="integrations.state.completed"/>
        </logic:equal>
        <logic:equal name="integration" property="state"
                value='<%= ""+Integration.CANCELED %>'>
            <bean:message key="integrations.state.canceled"/>
        </logic:equal>
    </dt:column>
    <dt:column sortable="true" title='<%= messages.getMessage("integrations.tableheading.what")%>'>
        <bean:write name="integration" property="comment"/>
    </dt:column>
</dt:table>
</logic:greaterThan>
</html:form>

</xplanner:content>

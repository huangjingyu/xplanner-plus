
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.Globals,
                 org.apache.struts.action.ActionMessages"%>
<%@ page import="com.technoetic.xplanner.actions.AuthenticationAction"%>
<%@ page import="com.technoetic.xplanner.XPlannerProperties"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<xplanner:content>
<xplanner:contentTitle titleKey="login.title"/>
<bean:define id="navigationHidden" value="true" toScope="request"/>
<html:form method="post" action="/login" styleClass="loginForm" styleId="loginForm">
 <div id="login">
  <table>
    <tr>
      <td class="label"><label for="userId"><bean:message key="login.label.user_id"/>:</label></td>
      <td class="userName"><div class="borderedInput"><div><input type="text" name="userId" /></div></div></td>
    </tr>
    <tr>
      <td><label for="password"><bean:message key="login.label.password"/>:</label></td>
      <td class="password"><div class="borderedInput"><div><input type="password" name="password" /></div></div></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td class="leftAligned rememberMe"><input type="checkbox" name="remember" checked="checked" value="Y" /><label for="remember"><bean:message key="login.remember"/></label></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td class="leftAligned submit"><input type="submit" name="action" value="<bean:message key="login.label"/>"/>
      <%
        String loginInstructionsUrl = new XPlannerProperties().getProperty("login.instructions.url");
        if (StringUtils.isNotEmpty(loginInstructionsUrl)) {
      %>
        <div class="help"><bean:message key="login.instructions" arg0="<%=loginInstructionsUrl%>"/></div>
      <%}%></td>
    </tr>
  </table>
  </div>
</html:form>

<script>
    document.forms[0].userId.focus();
</script>

<%--TODO Move font to stylesheet and use styled DIV or SPAN with ID for testability of error condition--%>
<html:messages id="message" message="true" property='<%=ActionMessages.GLOBAL_MESSAGE%>' >
    <div class="errorMessage">
        <%= message%>
    </div>
</html:messages>
<html:messages id="message" message="true" property='<%=AuthenticationAction.MODULE_MESSAGES_KEY%>' >
    <div class="errorMessage">
        <%= message%>
    </div>
</html:messages>
  <%--
<bean:define id="messages" name='<%= Globals.MESSAGES_KEY %>' type="org.apache.struts.util.MessageResources" />
   <logic:iterate id="property" name="login/authenticate" property="loginModuleNames">
    <%String moduleName = (String)property;%>
    <html:messages id="message" message="true" property='<%=moduleName%>' >
        <font color="red"><bean:message key="authentication.module.name" arg0='<%=moduleName%>' />: <%= message%></font><br/>
     </html:messages>
</logic:iterate>
--%>
</xplanner:content>
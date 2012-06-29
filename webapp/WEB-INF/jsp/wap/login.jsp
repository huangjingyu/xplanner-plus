<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<%@ page contentType="text/vnd.wap.wml" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<db:useBeans id="projects" type="net.sf.xplanner.domain.Project" where="hidden = false" />
<wml>
<card id="top" title="Login">
<do type="accept" label="Login">
  <go href="/xplanner/do/mobile/login">
    <postfield name="action" value="authenticate"/>
    <postfield name="userId" value="$userId"/>
    <postfield name="password" value="$password"/>
    <postfield name="remember" value="$remember"/>
  </go>
</do>
<p>
User ID: <input type="text" name="userId"/><br/>
Password: <input type="password" name="password"/><br/>
<input type="checkbox" name="remember" value="Y"/> Remember me?<br/>
<%--<a href="/xplanner/wap/auth.jsp" title="Login">Login</a>--%>
</p>
</card>
</wml>
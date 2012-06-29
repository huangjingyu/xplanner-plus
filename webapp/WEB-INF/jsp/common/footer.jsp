<%@ page import="com.technoetic.xplanner.security.SecurityHelper,
                 com.technoetic.xplanner.XPlannerProperties"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>



<div class="page_footer">
<span><html:img page="/images/wider.png" styleClass="right deleteImg" />
switch width of Xplanner+ page 
</span>
			

 <a href="http://xplanner-plus.sourceforge.net/" target="xplanner_home">
     <%
       XPlannerProperties properties = new XPlannerProperties();
         String version = properties.getProperty(XPlannerProperties.XPLANNER_VERSION_KEY);
         String revision = properties.getProperty(XPlannerProperties.XPLANNER_BUILD_REVISION_KEY);
         String date = properties.getProperty(XPlannerProperties.XPLANNER_BUILD_DATE_KEY);
         String appUrl = properties.getProperty(XPlannerProperties.APPLICATION_URL_KEY);
     %>
     <bean:message key="app.label.version" arg0='<%=version%>' arg1='<%=date%>' arg2='<%=revision%>'/>
 </a>
</div>



<div class="below_page_footer">
<span>
 <%
   String productionSupportEmail = properties.getProperty(XPlannerProperties.SUPPORT_PRODUCTION_EMAIL_KEY);
   String issueLink = properties.getProperty(XPlannerProperties.SUPPORT_ISSUE_URL_KEY);
   String logoToSF = properties.getProperty("sf.net.logo");
  %>
 <bean:message key="footer.message" arg0="<%=productionSupportEmail%>" arg1="<%=issueLink%>" arg2='<%=appUrl+"/do/systemInfo"%>'/>
 <%=logoToSF%>
</span>
</div>

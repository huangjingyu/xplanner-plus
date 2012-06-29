<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.io.PrintWriter,
                 java.util.Properties,
                 java.text.MessageFormat,
                 org.apache.log4j.Logger,
                 org.apache.struts.Globals,
                 org.springframework.web.context.WebApplicationContext,
                 org.springframework.web.context.support.WebApplicationContextUtils,
                 com.technoetic.xplanner.SystemInfo,
                 com.technoetic.xplanner.XPlannerProperties,
                 com.technoetic.xplanner.tags.util.BoxedListTag
                 "%><%@ page import="java.util.TreeMap"%><%@ page import="org.apache.commons.lang.StringUtils"%><%@ page import="com.technoetic.xplanner.util.LocaleUtil"%><%@ page import="com.technoetic.xplanner.actions.SystemInfoAction"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>

<%
  boolean isSystemInfo = request.getAttribute(SystemInfoAction.IS_SYSTEM_INFO_KEY)!=null;
  String titleKey = isSystemInfo?"system.info.title":"error.title";
  WebApplicationContext applicationContext =
      WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
  SystemInfo systemInfo = (SystemInfo) applicationContext.getBean("systemInfo");
  Properties properties = (Properties) applicationContext.getBean("properties");
%>
<xplanner:content>

    <xplanner:contentTitle titleKey='<%=titleKey%>'/>
    <%--<xplanner:navigation back="true"/>--%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <style>
      .sectionTitle {
        font-size: 14px;
        font-weight: bold;
        margin: 0 0 10px 10px;
      }

      .warning {
        background-color: lightcoral;
        border: 1 #bbb solid;
        font-weight: bold;
        text-align:center;
      }
   </style>
        <tr>
            <td valign="top">
            <span class="title"><bean:message key='<%=titleKey%>'/></span>
            <p>
<%
  if (!isSystemInfo) {
  if (exception == null) {
    exception = (Exception) request.getAttribute("exception");
  }
  if (exception == null) {
    exception = (Throwable) request.getAttribute(Globals.EXCEPTION_KEY);
  }
  if (exception != null && exception.getMessage() != null) {
    String message = exception.getMessage();
    String[] lines = StringUtils.split(message);
    int lineCount = Math.min(lines.length, 3);
    String[] firstNLines = new String[lineCount];
    for (int i = 0; i < lineCount; i++) firstNLines[i] = lines[i];
    message = StringUtils.join(firstNLines, '\n');
    %>
      <span class="error_message">
      <%= message %>
    <%} else {%>
              <bean:message key="error.unknownError"/>
    <%}%>
              <br/><bean:message key="error.moreInfo"/>
            </span>
            </p>
       <%

         String productionSupportEmail = properties.getProperty(XPlannerProperties.SUPPORT_PRODUCTION_EMAIL_KEY);
         String issueLink = properties.getProperty(XPlannerProperties.SUPPORT_ISSUE_URL_KEY);
         String filingInfoFormat = properties.getProperty(XPlannerProperties.ERROR_FILING_INFO_KEY);
         String filingInfo = new MessageFormat(filingInfoFormat).format(new String[] {productionSupportEmail, issueLink});
       %>
        <p> <%=filingInfo%> </p>

        <hr/><br/>

        <%
          if (exception != null) {
        %>
        <xplanner:box title="Cause"><pre><%= exception.toString() %></pre></xplanner:box>
        <xplanner:box title="Stack Trace"><pre><% exception.printStackTrace(new PrintWriter(out)); %></pre></xplanner:box>
        <%
          }
 }//if (!isSystemInfo)
        %>
        <div class="sectionTitle">System Environment</div>
        <xplanner:boxedList title="Build" keyValues="<%=systemInfo.getBuildInfo()%>"/>
        <xplanner:boxedList title="Database" keyValues="<%=systemInfo.getDatabaseInfo()%>"/>
        <xplanner:boxedList title="App Server" keyValues="<%=systemInfo.getAppServerInfo()%>"/>
        <xplanner:boxedList title="Memory" keyValues="<%=systemInfo.getJVMStatistics()%>"/>
        <xplanner:boxedList title="System" keyValues="<%=systemInfo.getSystemProperties()%>"/>

        <div class="sectionTitle">Request Information</div>
        <%
          try {
        %>
          <xplanner:boxedList title="General">
            <li><%= BoxedListTag.renderProperty("Referer URL",request.getHeader("Referer") != null ? request.getHeader("Referer") : "Unknown" )%></li>
            <li><%= BoxedListTag.renderProperty("Locale", LocaleUtil.getLocale(request.getSession(true)).toString()) %></li>
            <li><%= BoxedListTag.renderProperty("URL", request.getRequestURL()) %></li>
            <ul>
               <li><%= BoxedListTag.renderProperty("Scheme", request.getScheme()) %></li>
               <li><%= BoxedListTag.renderProperty("Server", request.getServerName()) %></li>
               <li><%= BoxedListTag.renderProperty("Port", ""+request.getServerPort()) %></li>
               <li><%= BoxedListTag.renderProperty("URI", request.getRequestURI()) %></li>
              <ul>
                 <li><%= BoxedListTag.renderProperty("Context Path", request.getContextPath()) %></li>
                 <li><%= BoxedListTag.renderProperty("Servlet Path", request.getServletPath()) %></li>
                 <li><%= BoxedListTag.renderProperty("Path Info", request.getPathInfo()) %></li>
                 <li><%= BoxedListTag.renderProperty("Query String", request.getQueryString()) %></li>
            </ul></ul>
            </xplanner:boxedList>
            <xplanner:boxedList title="Parameters" keyValues="<%=new TreeMap(request.getParameterMap())%>"/>
            <xplanner:boxedList title="Attributes" keyValues="<%=BoxedListTag.getRequestAttributeMap(request)%>"/>
        <%
          }
          catch (Throwable t) {
            out.println("Error rendering logging information");
            t.printStackTrace(new PrintWriter(out));
          }
        %>
      </td>
   </tr>
</table>

<%
  Logger.getLogger(request.getRequestURI()).error("JSP error", exception);
%>
</xplanner:content>



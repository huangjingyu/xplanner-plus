<%@ page import="com.technoetic.xplanner.tags.TilesHelper"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page errorPage="/WEB-INF/jsp/common/unexpectedError.jsp"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="com.technoetic.xplanner.XPlannerProperties"%><html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">


<%
    String beanName = (String)TilesHelper.getAttribute("beanName",pageContext);
    String titlePrefix = (String)TilesHelper.getAttribute("titlePrefix",pageContext);
    boolean isScreenMode = !"print".equals(TilesHelper.getAttribute("displayMode",pageContext));
    XPlannerProperties properties = new XPlannerProperties();
    String appUrl = properties.getProperty(XPlannerProperties.APPLICATION_URL_KEY);
%>
<bean:define id="object" name='<%=beanName%>' toScope="request" type="com.technoetic.xplanner.domain.Nameable" />

<head>
	<c:set var="titleText"><bean:message key='<%=titlePrefix%>'/></c:set>
    <title>XPlanner ${titleText} <%=object==null?"":object.getName()%></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" media="screen" href="<html:rewrite page="/css/reset-min.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page='/ui/jquery-ui-1.8.11.custom.css'/>" />
    <link rel="stylesheet" media="screen" href="<html:rewrite page="/css/base.css"/>" />
	<link rel="shortcut icon"  type="image/x-icon" href="<html:rewrite page="/images/favicon2.ico"/>" />
	

<!--[if IE 7]>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page="/css/ie7.css"/>" />
<![endif]-->

	<script type="text/javascript" src="<html:rewrite page='/js/jquery-1.5.1.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/js/jquery-ui-1.8.11.custom.min.js'/>"></script>
    <script type="text/javascript" src="<html:rewrite page="/js/global.js"/>"></script>

    <!--<tab:tabConfig />-->
<meta property="og:title" content="XPlanner ${titleText} <%=object==null?"":object.getName()%>" />
<meta property="og:type" content="${object.name}" />
<meta property="og:url" content="http://sharethis.com" />
<meta property="og:image" content="http://sharethis.com/images/logo.jpg" />
<meta property="og:description" content="${object.name}" />
<meta property="og:site_name" content="${object.name}" />

</head>

<body>
  <div class="page">
    <% if (isScreenMode) { %>
    <div class="page_header"><tiles:insert attribute="header"/>
    <% } %>

    <tags:breadcrumb inclusive="true" />

        <% if ("true".equals(TilesHelper.getAttribute("showTitle",pageContext))) { %>
           <!-- 
           <span class="object_name"><%=object==null?"":object.getName()%>
           [id=<%=object==null?"":""+object.getId()%>]</span> -->
        <% } %>
        
    </div>
    <html:errors/>
    <div class="page_body">
    <span class="title">
    <h1>
    	${titleText}&#160;<a href="#" >${object.name}</a>
		    <a class="tw" href="http://twitter.com/home?status=Working on ${titleText} ${object.name}" target="_blank" >
			<html:img page="/images/twitter.png" styleClass="right deleteImg" />
			</a>
			
			<c:set var="pageUrl"><%=appUrl%><%=request.getAttribute("javax.servlet.forward.request_uri")%>?<%=request.getAttribute("javax.servlet.forward.query_string")%></c:set>
			<c:url var="shareUrl" value="http://www.facebook.com/dialog/feed">
				<c:param name="app_id" value="198634833492606" />
				<c:param name="link" value="${pageUrl}" />
				<c:param name="picture" value="http://xplanner-plus.sourceforge.net/images/logo.png" />
				<c:param name="name" value="Xplanner ${titleText}" />
				<c:param name="caption" value="${titleText}" />
				<c:param name="description" value="${object.name}" />
				<c:param name="message" value="Working on" />
				<c:param name="redirect_uri" value="http://xplannerplus.org/" />
			</c:url>
			<a target="_blank" href="${shareUrl}"><html:img page="/images/facebook.png" styleClass="right deleteImg" /></a>
	</h1>
        
  
   <!-- a class="in" target="_blank" href="http://www.linkedin.com/shareArticle?mini=true&url=http://www.xplannerplus.org/index.html&summary=Check out XPlanner plus: open source (free) project planning and bug tracking tool for agile teams">
   <html:img page="/images/linkedin.png" styleClass="right deleteImg" />
   </a>
   <span><script src="http://connect.facebook.net/en_US/all.js#xfbml=1"></script><fb:like href="http://www.xplannerplus.org/index.html" layout="button_count" show_faces="false" width="45" font=""></fb:like></span -->
     


        <tiles:getAsString name="titleSuffix"/>





      </span>
      
      <tiles:insert attribute="progress" />
      <% if (isScreenMode) { %>
	     <span class="links"> <tiles:insert attribute="actions" /></span>
	    <%}%>
      <tiles:insert attribute="body"/>
  <% if (object != null) {
        %>
        <jsp:include page="/WEB-INF/jsp/view/notes.jsp" flush="true">
            <jsp:param name="oid" value='<%= Integer.toString(object.getId()) %>'/>
        </jsp:include>
        <%
          }
    %>
    </div>
    <%
      if (isScreenMode) {
    %>
        <jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
    <% } %>
  </div>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<head>
   <meta name="description" content="XPlanner+ is an open source, free, project planning and tracking tool for agile XP and scrum teams." />
    
   <meta name="keywords" content="XPlanner Plus, XPlanner Plus demo, XPlanner-Plus demo, XPlanner+ free demo, Open Source Scrum tool, Free Project Management tool, Free Scrum tool, XPlanner+ demo, XPlanner+ burn down chart." />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
    <title><tiles:getAsString name="title"/></title>
    <link rel="stylesheet" href="<html:rewrite page="/css/reset-min.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page="/calendar/calendar-blue.css"/>" />
    <link rel="stylesheet" href="<html:rewrite page='/ui/jquery-ui-1.8.11.custom.css'/>" />
    <link rel="stylesheet" href="<html:rewrite page="/css/base.css"/>" />
	<link rel="shortcut icon"  type="image/x-icon" href="<html:rewrite page="/images/favicon2.ico"/>" />
<!--[if IE 7]>
<link rel="stylesheet" type="text/css" media="all" href="<html:rewrite page="/css/ie7.css"/>" />
<![endif]-->
    
    <script type="text/javascript" src="<html:rewrite page='/js/jquery-1.5.1.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/js/jquery-ui-1.8.11.custom.min.js'/>"></script>
    <script src="<html:rewrite page="/toggle.js"/>" ></script>
    <!--[if IE lte 8]>
    <script src="<html:rewrite page="/js/nifty.js"/>"></script>
    <script >
      window.onload=function(){
        if(!NiftyCheck())
            return;
        Rounded("div#editObject","#FFFFFF","#5583AC",8,8);
      }
    </script>
    <![endif]-->
</head>
<body>
  <div class="page">
<script type="text/javascript" src="<html:rewrite page="/js/global.js"/>"></script>

    <div class="page_header"><tiles:insert attribute="header"/>
        <tags:breadcrumb />
    </div>
    <div class="page_body"><tiles:insert attribute="body"/></div>
    <tiles:insert attribute="footer"/>
  </div>
</body>
</html>

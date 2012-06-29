<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="com.technoetic.xplanner.security.SecurityHelper,
                 com.technoetic.xplanner.XPlannerProperties"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="<html:rewrite page="/js/tooltip.js"/>" type="text/javascript"></script>

<div class="header">
  <a href="<html:rewrite page="/"/>"><img src="<html:rewrite page="/images/logo.png"  />" width="296" height="47" /></a>
	<div class="search_box">
	    <xplanner:authenticatedUser id="person"/> 
	    <c:if test="${not empty person}">
	      <bean:message key="footer.label.user"/> <%= SecurityHelper.getUserPrincipal(request).getName() %>
	      &nbsp;
	      <xplanner:link href="${appUrl}/setting/me/status/${person.id}" useReturnto="true">
	        <bean:message key="navigation.me"/>
	      </xplanner:link>
           &nbsp;&nbsp;
	      <a href="<html:rewrite page="/do/logout"/>"><bean:message key="logout"/></a>
		    <form action="<html:rewrite action="/search/content" />" id="search_form" >
		    	<!--
		    	<bean:message key="contentsearch.label" />
		    	-->
		    	<div class="right"><input type="image" name="action" src="<html:rewrite page="/images/search.png"/>"  alt="<bean:message key="contentsearch.button.label" />" /></div>
		    	<div id="search" class="searchInput borderedInput right">
		    	  <div class="right">
		    	  	<!--html:img page="/images/select.png" styleClass="right" /-->
		    	  	<span class="right"><input class="inp" type="text" name="searchedContent" /></span>
		    	  	<html:img page="/images/search_icon.png" styleClass="right deleteImg" />
		    	  </div>
		    	</div>
		    	<input type="hidden" name="restrictToProjectId" value="${project.id}"/>
		    </form>
		   </c:if>
    </div>
    <div class=""></div>
</div>
<script>
		
		$(".deleteImg").mouseover(function(){
			
		})
		
		tooltip("search by word or ID", "inp", $('input'));
		$("input").focusin(function(){
		console.log("ss")
			$(".deleteImg").addClass();
			$(".inp").width(223)
		})
		
		$(".deleteImg").hover(function(){$(this).fadeOut(100);
		$(".inp").width(223)
		});
		
		
		
		
</script>



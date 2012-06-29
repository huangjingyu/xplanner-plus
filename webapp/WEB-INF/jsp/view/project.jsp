<%@page import="org.hibernate.Hibernate"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.Globals,
                 com.technoetic.xplanner.actions.AbstractAction,
                 com.technoetic.xplanner.format.DecimalFormat,
                 com.technoetic.xplanner.tags.displaytag.IterationRowDecorator"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>
<%@ taglib uri="http://xplannerplus.com/jstl/functions"  prefix="xfn" %>

<db:useBean id="project" type="net.sf.xplanner.domain.Project" scope="request"/>
<xplanner:content definition="tiles:view" >


      
	<script src="<html:rewrite page="/js/tooltip.js"/>" type="text/javascript"></script>

  <!-- DEBT: Should use "this" instead of project. This way nobody has to set it explicitly in the tile-->
   <tiles:put name="beanName">project</tiles:put>
   <tiles:put name="titlePrefix">project.prefix</tiles:put>

         

<logic:notEmpty name="project" property="description">
    <div id="objectDescription" class="description">
        <xplanner:twiki name="project" property="description"/>
    </div>
</logic:notEmpty>
<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>

<c:set var="iterations" value="${project.iterations}"/>

<c:if test="${fn:length(iterations) gt 0 }">
	<xplanner:writableTable wholeCollection='${iterations}' permissions="edit,delete"
        rowDecorator='<%= new IterationRowDecorator() %>' id="objecttable" name="pageScope.iterations"
        styleClass="objecttable" requestURI=""
        defaultorder="descending" defaultsort="5" pagesize="10" >
        <dt:setProperty name="paging.banner.placement" value="bottom" />
        <bean:define id="iteration" name="objecttable" type="net.sf.xplanner.domain.Iteration"/>
    <xplanner:actionButtonsColumn styleClass="image" name="iteration" id="action" title='<%= messages.getMessage("iterations.tableheading.actions") %>' >
    

  <xplanner:isUserAuthorized  name="iteration" permission='<%=action.getPermission()%>'>
        <xplanner:action actionRenderer='<%=action%>' targetBean='<%=iteration%>' fkey='<%=iteration.getProject().getId()%>'/>
      </xplanner:isUserAuthorized>
    </xplanner:actionButtonsColumn>


        <dt:column title='<%= messages.getMessage("objects.tableheading.id") %>'>
          <xplanner:link page="/do/view/iteration" paramId="oid" paramName="iteration" paramProperty="id">
            ${iteration.id}</xplanner:link></dt:column>
        <dt:column sortable="true" sortProperty="name" title='<%= messages.getMessage("iterations.tableheading.iteration") %>'>
          <xplanner:link styleClass="name" page="/do/view/iteration" paramId="oid" paramName="iteration" paramProperty="id">
            ${iteration.name}</xplanner:link></dt:column>
        <dt:column sortable="true" sortProperty="startDate" title='<%= messages.getMessage("iterations.tableheading.startDate") %>'>
            <p><xplanner:formatDate formatKey="format.date" name="iteration" property="startDate"/></p></dt:column>
        <dt:column sortable="true" sortProperty="endDate" title='<%= messages.getMessage("iterations.tableheading.endDate") %>'>
            <p><xplanner:formatDate formatKey="format.date" name="iteration" property="endDate"/></p></dt:column>
        <dt:column sortable="true" sortProperty="daysWorked" title='<%= messages.getMessage("iterations.tableheading.days_worked") %>' align="right">
            <p><%= DecimalFormat.format(pageContext, iteration.getDaysWorked()) %></p> </dt:column>
        <dt:column title='<%= messages.getMessage("iterations.tableheading.stories") %>' align="right" >
            <p><%= iteration.getUserStories().size() %></p> </dt:column>
	</xplanner:writableTable>
</c:if>

<logic:equal name="iterationCount" value="0" >
  <p class="highlighted_message"><bean:message key="iterations.none"/></p>
</logic:equal>


<tiles:put name="actions" direct="true" >
<xplanner:actionButtons
        name="project" id="action">
    <xplanner:isUserAuthorized name="project" permission='<%=action.getPermission()%>'>
      <xplanner:link  page='<%=action.getTargetPage()%>' paramId="oid" onclick='<%=action.getOnclick()%>'
           paramName='<%=action.getDomainType()%>' paramProperty="id" useReturnto='<%=action.useReturnTo()%>'>
           <bean:message key='<%=action.getTitleKey()%>'/></xplanner:link> 
  </xplanner:isUserAuthorized>
</xplanner:actionButtons> 
<xplanner:isUserAuthorized resourceType="system.project.iteration" permission="create" >
    <xplanner:link page="/do/edit/iteration">
        <bean:message key="project.link.create_iteration"/></xplanner:link> 
</xplanner:isUserAuthorized>
</tiles:put>


<tiles:put name="globalActions" direct="true">
  <xplanner:isUserAuthorized resourceType="system.person" permission="read">
    <xplanner:link page="/do/view/people"><bean:message key="projects.link.people"/></xplanner:link> 
  </xplanner:isUserAuthorized>
  <jsp:include page="exportLinks.jsp"/>&nbsp;
  <xplanner:link page="/do/view/history">
    <bean:message key="history.link"/>
    <xplanner:linkParam id="oid" name="project" property="id"/>
    <xplanner:linkParam id='<%= AbstractAction.TYPE_KEY %>' value='<%= Hibernate.getClass(project).getName() %>'/>
    <xplanner:linkParam id="container" value="true"/>
  </xplanner:link>
</tiles:put>
	<script>
		tooltip("<%= messages.getMessage("action.edit.iteration")%>", "edit", $('img'));
		tooltip("<%= messages.getMessage("action.delete.iteration")%>", "delete", $('img'));
		
</script>

</xplanner:content>

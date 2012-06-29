<%@ page import="com.technoetic.xplanner.actions.AbstractAction,
                 com.technoetic.xplanner.domain.IterationStatus,
                 com.technoetic.xplanner.views.*,
                 com.technoetic.xplanner.XPlannerProperties"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<bean:define id="iteration" name="iteration" type="net.sf.xplanner.domain.Iteration"/>
<span class="links">
<xplanner:isUserAuthorized name="iteration" permission="edit" >
     <xplanner:actionButtons name="iteration" id="action">
      <xplanner:isUserAuthorized name="iteration" permission='<%=action.getPermission()%>'>
         <xplanner:action actionRenderer='<%=action%>' targetBean='<%=iteration%>' fkey='<%=iteration.getProject().getId()%>'/> 
        <%--
         <xplanner:link page='<%=action.getTargetPage()%>' paramId="oid" onclick='<%=action.getOnclick()%>'
           paramName='<%=action.getDomainType()%>' paramProperty="id" useReturnto='<%=action.useReturnTo()%>' fkey='<%=iteration.getProjectId()%>'>
           <bean:message key='<%=action.getTitleKey()%>'/>
         </xplanner:link> |
         --%>
      </xplanner:isUserAuthorized>
   </xplanner:actionButtons>
</xplanner:isUserAuthorized>
<xplanner:link page="/do/view/iteration" paramId="oid" paramName="iteration" paramProperty="id">
  <bean:message key="iteration.link.stories"/>
</xplanner:link> 
<xplanner:link page="/do/view/iteration/tasks" paramId="oid" paramName="iteration" paramProperty="id">
  <bean:message key="iteration.link.all_tasks"/>
</xplanner:link> 
<%--FEATURES:--%>
<%--<xplanner:link page="/do/view/iteration/features" paramId="oid" paramName="iteration" paramProperty="id">--%>
<%--  <bean:message key="iteration.link.all_features"/>--%>
<%--</xplanner:link> |--%>


</span>


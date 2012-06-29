<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="com.technoetic.xplanner.security.auth.SystemAuthorizer,
                 com.technoetic.xplanner.security.SecurityHelper,
                 java.util.Date,
                 org.apache.commons.collections.CollectionUtils,
                 org.apache.commons.collections.Predicate,
                 net.sf.xplanner.domain.Iteration,
                 net.sf.xplanner.domain.Project,
                 com.technoetic.xplanner.security.AuthenticationException,
                 org.apache.struts.Globals,
                 com.technoetic.xplanner.tags.displaytag.RowDecorator,
                 com.technoetic.xplanner.tags.displaytag.Row,
                 org.apache.commons.beanutils.PropertyUtils"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html"
	prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
	prefix="tiles"%>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner"%>
<%@ taglib uri="http://xplanner.org/xplanner-db-taglib" prefix="db"%>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt"%>

<xplanner:content>
	<xplanner:contentTitle titleKey="projects.title" />

	<script src="<html:rewrite page="/js/tooltip.js"/>" type="text/javascript"></script>

	<db:useBeans id="projects" type="net.sf.xplanner.domain.Project"
		order="is_hidden,name asc" />
	<%
    // todo Refactor this to a security helper class
    final HttpServletRequest r = request;
    CollectionUtils.filter(projects, new Predicate() {
        public boolean evaluate(Object o) {
            try {
                Project project = (Project)o;
                return SystemAuthorizer.get().hasPermission(project.getId(),
                            SecurityHelper.getRemoteUserId(r), project, "read");
            } catch (AuthenticationException e) {
                return false;
            }
        }
    });
%>


	<db:useBeans id="iterations" type="net.sf.xplanner.domain.Iteration"
		where="? between startDate and endDate">
		<db:useBeansParameter value='<%= new Date() %>' type="date" />
	</db:useBeans>


	<span class="title">
	<h1><bean:message key="projects.title" /></h1>
	</span>

	<bean:size id="projectCount" name="projects" />
	<bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>'
		type="org.apache.struts.util.MessageResources" />

	<%!
        private static class HiddenRowDecorator implements RowDecorator {
            public String getCssClasses(Row row) {
                try {
                    return PropertyUtils.getProperty(row.getObject(), "hidden").equals(Boolean.TRUE) ? "hidden" : null;
                } catch (Exception e) {
                    return null;
                }
            }
        }
    %>





	<logic:greaterThan name="projectCount" value="0">
		<xplanner:writableTable wholeCollection='<%= projects %>'
			permissions="admin.edit,sysadmin.delete" name="pageScope.projects"
			id="objecttable" styleClass="objecttable" requestURI="">
			<% final Project project = (Project)objecttable; pageContext.setAttribute("project", objecttable); %>
			<xplanner:actionButtonsColumn name="project" id="action"
				title='<%= messages.getMessage("iterations.tableheading.actions") %>'>
				<xplanner:isUserAuthorized name="project"
					permission='<%=action.getPermission()%>'>
					<xplanner:link styleClass="image" 
						page='<%=action.getTargetPage()%>' paramId="oid"
						onclick='<%=action.getOnclick()%>' paramName="project"
						paramProperty="id">


						<html:img page='<%=action.getIconPath()%>'
							alt='<%=action.getName()%>' border="0" styleClass="${action.name}" />
					</xplanner:link>
				</xplanner:isUserAuthorized>
			</xplanner:actionButtonsColumn>
			<dt:column
				title='<%= messages.getMessage("objects.tableheading.id") %>'>
				<xplanner:link href="project" paramId="oid" paramName="project"
					paramProperty="id">
					<bean:write name="project" property="id" />
				</xplanner:link>
			</dt:column>
			<dt:column
				title='<%= messages.getMessage("projects.tableheading.name") %>'
				sortable="true" sortProperty="name">
				<xplanner:link styleClass="name" href="project" paramId="oid"
					paramName="project" paramProperty="id">
					<bean:write name="project" property="name" />
				</xplanner:link>
			</dt:column>
			<dt:column
				title='<%= messages.getMessage("projects.tableheading.iteration") %>'
				sortable="true" sortProperty="name">
				<%
      pageContext.removeAttribute("iteration");
      Iteration iteration = (Iteration)CollectionUtils.find(iterations, new Predicate() {
          public boolean evaluate(Object o) {
              return ((Iteration)o).getProject() == project;
          }
      });
      if (iteration != null) {
          pageContext.setAttribute("iteration", iteration);
      }
    %>
				<logic:present name="iteration">
					<xplanner:link href="iteration" paramId="oid" paramName="iteration"
						paramProperty="id">
						<bean:write name="iteration" property="name" />
					</xplanner:link>
				</logic:present>
			</dt:column>
			<xplanner:isUserAuthorizedForAny name="projects" permissions="hide">
				<dt:column
					title='<%= messages.getMessage("projects.tableheading.ishidden") %>'
					sortable="true" align="center"><%= messages.getMessage(project.isHidden() ?
            "ishidden.yes" : "ishidden.no") %></dt:column>
			</xplanner:isUserAuthorizedForAny>

		</xplanner:writableTable>
	</logic:greaterThan>

	<logic:equal name="projectCount" value="0">
		<span class="highlighted_message"><bean:message
			key="projects.none" /></span>
	</logic:equal>


	<span class="links"> <xplanner:isUserAuthorized
		resourceType="system.project" permission="create.project">
		<xplanner:link page="/do/edit/project">
			<bean:message key="projects.link.add_project" />
		</xplanner:link>
	</xplanner:isUserAuthorized> </span>
	<tiles:put name="globalActions" direct="true">
		<xplanner:authenticatedUser id="person" />
		<xplanner:link page="/do/view/people">
			<bean:message key="projects.link.people" />
		</xplanner:link>
		<xplanner:link page="/do/view/aggregateTimesheet">
			<bean:message key="projects.link.aggregate.timesheet" />
		</xplanner:link>
	</tiles:put>
	<script>
		tooltip("<%= messages.getMessage("action.edit.project")%>", "edit", $('img'));
		tooltip("<%= messages.getMessage("action.delete.project")%>", "delete", $('img'));
		
</script>
</xplanner:content>

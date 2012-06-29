<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ page import="org.apache.struts.action.ActionMapping,
                 org.apache.struts.Globals,
                 com.technoetic.xplanner.tags.DomainContext,
                 java.util.ArrayList,
                 java.util.List,
                 java.util.Arrays,
                 org.apache.struts.config.ActionConfig,
                 com.technoetic.xplanner.XPlannerProperties"%><%@ page import="java.util.Properties"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%-- This should really be in the defaultLayout but it was causing problems with JWebUnit --%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<script language="JavaScript" src="<html:rewrite page="/overlib.js"/>"></script>
<%
    ActionMapping actionMapping = (ActionMapping)request.getAttribute(Globals.MAPPING_KEY);
    String name = actionMapping.getPath().substring(actionMapping.getPath().lastIndexOf("/")+1);
    DomainContext context = DomainContext.get(request);
    pageContext.setAttribute("object", context.getTargetObject());
    ArrayList formats = new ArrayList();
  XPlannerProperties properties = new XPlannerProperties();
  List availableFormats = Arrays.asList(properties.getProperty("xplanner.export.formats").split(","));
    for (int i = 0; i < availableFormats.size(); i++) {
        String format = (String)availableFormats.get(i);
        ActionConfig action = actionMapping.getModuleConfig().findActionConfig("/export/"+name+"/"+format);
        if (action != null) {
            formats.add(format);
        }
    }
%>

<a target=_top href="javascript:void(0);"
    onclick="return overlib('<%
        for (int i = 0; i < formats.size(); i++) {
            String format = (String)formats.get(i);
    %><xplanner:link removeQuotes="true" page='<%="/do/export/"+name+"/"+format %>'
        paramId="oid" paramName="object" paramProperty="id">
        <bean:message key='<%= "export."+format%>'/></xplanner:link> <%}%>', STICKY, CAPTION,'<bean:message key="export.label.formats"/>:',WIDTH,120);"
        onmouseout="return nd();"><bean:message key="export.prefix"/></a>
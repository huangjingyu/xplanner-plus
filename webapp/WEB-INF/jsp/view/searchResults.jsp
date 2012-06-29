<%@ page import="org.apache.struts.Globals"%>
<%@ page contentType = "text/html;charset=UTF-8"%>
<%@ taglib uri="http://xplanner.org/xplanner-taglib" prefix="xplanner" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://xplanner.org/displaytag.sf.net" prefix="dt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>


<xplanner:content>

<xplanner:contentTitle titleKey="search.results"/>

    <bean:define id="messages" name='<%=Globals.MESSAGES_KEY%>' type="org.apache.struts.util.MessageResources"/>

    <p class="title"><bean:message key="search.results"/> - <bean:write name="searchedContent"/></p>

    <logic:notEmpty name="searchResults" >
        <dt:table styleClass="objecttable"  name="searchResults" id="searchResult" requestURI="">
            <bean:define id="result" name="searchResult" type="com.technoetic.xplanner.domain.SearchResult"/>

          <dt:column property="resultType" title='<%= messages.getMessage("search.results.type")%>' sortable="true"/>

          <dt:column property="domainObjectId" title='<%= messages.getMessage("search.results.id")%>' sortable="true"/>

          <dt:column title='<%= messages.getMessage("search.results.title")%>' sortable="true">
          <% if ( "note". equals( result.getResultType())) { %>
              <html:link page='<%= "/do/view/" + result.getAttachedToDomainType()%>' paramId="oid" paramName="result" paramProperty="attachedToId">
                  ${result.title}
              </html:link>
          <% } else { %>
              <html:link page='<%= "/do/view/" + result.getResultType()%>' paramId="oid" paramName="result" paramProperty="domainObjectId">
                  ${result.title}
              </html:link>
          <% } %>
          </dt:column>

          <dt:column title='<%= messages.getMessage("search.results.match")%>' sortable="true">
              ${result.matchPrefix}<logic:equal name="result" property="matchInDescription" value="true"><strong></logic:equal>${result.matchingText}<logic:equal name="result" property="matchInDescription" value="true"></strong></logic:equal>${result.matchSuffix}
          </dt:column>
        </dt:table>

    </logic:notEmpty>

    <logic:empty name="searchResults">
        <bean:message key="empty.search.results"/>
    </logic:empty>

</xplanner:content>
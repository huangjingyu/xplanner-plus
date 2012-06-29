package com.technoetic.xplanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.Subject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import junit.extensions.FieldAccessor;
import junit.framework.Assert;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Role;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;

import com.kizna.servletunit.HttpServletRequestSimulator;
import com.kizna.servletunit.HttpServletResponseSimulator;
import com.kizna.servletunit.HttpSessionSimulator;
import com.kizna.servletunit.ServletConfigSimulator;
import com.kizna.servletunit.ServletContextSimulator;
import com.technoetic.mocks.hibernate.MockSession;
import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.mocks.servlets.jsp.MockJspWriter;
import com.technoetic.mocks.servlets.jsp.MockPageContext;
import com.technoetic.mocks.sql.MockConnection;
import com.technoetic.mocks.sql.MockPreparedStatement;
import com.technoetic.mocks.sql.MockResultSet;
import com.technoetic.mocks.sql.MockStatement;
import com.technoetic.mocks.struts.util.MockMessageResources;
import com.technoetic.mocks.struts.util.MockMessageResourcesFactory;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.DomainContext;

public class XPlannerTestSupport {
    public static final String DEFAULT_PERSON_USER_ID = "sombody";
    public static final int DEFAULT_PERSON_ID = 44;
    private Enumeration appenders;

    public static class XHttpServletResponseSimulator extends HttpServletResponseSimulator {
        private String redirect;
        private HashMap headers = new HashMap();
        private int status;

        public String encodeURL(String url) {
            return url;
        }

        public String getRedirect() {
            return redirect;
        }

        public void sendRedirect(String s) throws IOException {
            //super.sendRedirect(s);
            redirect = s;
        }

        public void setHeader(String name, String value) {
            headers.put(name, value);
        }

        public String getHeader(String name) {
            return (String) headers.get(name);
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getContentType() {
            return null;
        }

        public void setCharacterEncoding(String s) {

        }
    }

    public static class XServletContextSimulator extends ServletContextSimulator {
        private HashMap attributes = new HashMap();

        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }

        public Object getAttribute(String name) {
            return attributes.get(name);
        }

        public Set getResourcePaths(String s) {
            throw new UnsupportedOperationException();
        }
    }

    public static class XHttpServletRequestSimulator extends HttpServletRequestSimulator {
        private Locale locale;
        private String servletPath;
        private String contextPath;
        private String remoteAddr;
        private Cookie[] cookies;

        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        public Locale getLocale() {
            return locale;
        }

        public int getRemotePort() {
            return 0;
        }

        public String getLocalName() {
            return null;
        }

        public String getLocalAddr() {
            return null;
        }

        public int getLocalPort() {
            return 0;
        }

        public Map getParameterMap() {
            return parameters;
        }

        public String getServletPath() {
            return servletPath;
        }

        public void setServletPath(String servletPath) {
            this.servletPath = servletPath;
        }

        public String getContextPath() {
            return contextPath;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        public void setCookies(Cookie[] cookies) {
            this.cookies = cookies;
        }

        public Cookie[] getCookies() {
            return cookies;
        }

        public void setRemoteAddr(String remoteAddr) {
           this.remoteAddr = remoteAddr;
        }

        public String getRemoteAddr() {
           return remoteAddr;
        }

        public void setQueryString(String queryString) {
           this.queryString = queryString;
        }
    }

    public ActionMapping mapping;
    public ActionForm form;
    public ActionServlet actionServlet;
    public MockMessageResources resources;
    public MockPageContext pageContext;
    public MockJspWriter jspWriter;
    public XHttpServletRequestSimulator request;
    public XHttpServletResponseSimulator response;
    public HttpSessionSimulator servletSession;
    public ServletConfigSimulator servletConfig;
    public XServletContextSimulator servletContext;
    public MockPreparedStatement mockPreparedStatement;
    public MockStatement mockStatement;
    public MockResultSet mockResultSet;
    public MockConnection connection;
    public MockSessionFactory hibernateSessionFactory;
    public MockSession hibernateSession;

    public XPlannerTestSupport() throws Exception {
        hibernateSessionFactory = new MockSessionFactory();
        GlobalSessionFactory.set(hibernateSessionFactory);
        hibernateSession = new MockSession();
        hibernateSessionFactory.openSessionReturn = hibernateSession;
        connection = new MockConnection();
        hibernateSession.connectionReturn = connection;
        mockPreparedStatement = new MockPreparedStatement();
        connection.prepareStatementReturn = mockPreparedStatement;
        mockResultSet = new MockResultSet();
        mockPreparedStatement.executeQueryReturn = mockResultSet;
        mapping = new ActionMapping();
        request = new XHttpServletRequestSimulator();
        request.setLocale(Locale.getDefault());
        response = new XHttpServletResponseSimulator();
        servletSession = (HttpSessionSimulator) request.getSession();
        actionServlet = new ActionServlet();
        MockMessageResourcesFactory factory = new MockMessageResourcesFactory();
        resources = new MockMessageResources(factory, "");
        request.setAttribute(Globals.MESSAGES_KEY, resources);
        HibernateHelper.setSession(request, hibernateSession);
        pageContext = new MockPageContext();
        pageContext.getRequestReturn = request;
        pageContext.getResponseReturn = response;
        servletConfig = new ServletConfigSimulator();
        FieldAccessor.set(actionServlet, "config", servletConfig);
        pageContext.getServletConfigReturn = servletConfig;
        servletContext = new XServletContextSimulator();
        servletContext.setAttribute(Globals.MODULE_KEY, new ModuleConfigImpl(""));
        FieldAccessor.set(servletConfig, "context", servletContext);
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
        pageContext.getServletContextReturn = servletContext;
        pageContext.getSessionReturn = request.getSession();
        jspWriter = new MockJspWriter();
        pageContext.getOutReturn = jspWriter;
    }

    public void setForward(String name, String path) {
        ModuleConfig config = mapping.getModuleConfig();
        if (config == null) {
            config = new ModuleConfigImpl("");
            mapping.setModuleConfig(config);
        }
        ForwardConfig forwardConfig = config.findForwardConfig(name);
        if (forwardConfig == null) {
            config.addForwardConfig(new ActionForward(name, path, false));
        } else {
            forwardConfig.setPath(path);
        }
    }

    public ActionForward executeAction(Action action) throws Exception {
        return action.execute(mapping, form, request, response);
    }

    public Subject setUpSubjectInRole(String role) {
        return setUpSubject(DEFAULT_PERSON_USER_ID, new String[]{role});
    }

    public Subject setUpSubject(String userId, String[] roles) {
        Person person = new Person(userId);
        person.setId(DEFAULT_PERSON_ID);
        return setUpSubject(person, roles);
    }

    public Subject setUpSubject(Person person, String[] roles) {
        Subject subject = new Subject();
        subject.getPrincipals().add(new PersonPrincipal(person));
        for (int i = 0; i < roles.length; i++) {
            if (roles[i] != null) {
                subject.getPrincipals().add(new Role(roles[i]));
            }
        }
        SecurityHelper.setSubject(request, subject);
        return subject;
    }

    public void assertHistoryInObject(int objectId, String action, String description, int personId) {
        assertHistory(hibernateSession, objectId, action, description, personId);
    }

    public void assertHistory(MockSession hibernateSession,
                                      int objectId,
                                      String action,
                                      String description,
                                      int authorId) {

        List eventList = getHistoryList(hibernateSession, objectId, action);
        Assert.assertFalse("no historical events", eventList.size() == 0);
        if (eventList.size() > 0) {
            Iterator it = eventList.iterator();
            while (it.hasNext()) {
                History event = (History) it.next();
               if (matchesExpectedEvent(description, event, authorId)) {
                  assertEventDetails(description, event, authorId);
                  return;
               }
            }
           Assert.fail("Expected historical event not found");
        }
    }

   private boolean matchesExpectedEvent(String description, History event, int authorId) {
      return
            assertEventDescriptionMatches(description, event) &&
             authorId == event.getPersonId() &&
             event.getWhenHappened() != null &&
             !event.isNotified();
   }

   private boolean assertEventDescriptionMatches(String expectedDescription, History event) {
      return (event.getDescription() == null && expectedDescription == null) ||
             event.getDescription().equals(expectedDescription);
   }

   private void assertEventDetails(String description, History event, int authorId) {
      Assert.assertEquals("wrong description", description, event.getDescription());
      Assert.assertEquals("wrong authorId", authorId, event.getPersonId().intValue());
      Assert.assertNotNull("wrong date", event.getWhenHappened());
      Assert.assertFalse("wrong notified flag", event.isNotified());
   }

   public void assertNoHistory(MockSession hibernateSession) {
       List HistoryList = getHistoryList(hibernateSession, 0, null);
       Assert.assertFalse("unexpected historical event", HistoryList.size() == 0);
   }

    private List getHistoryList(MockSession hibernateSession, int targetObjectId, String action) {
        List HistoryList = new ArrayList();
        Iterator it = getObjectListWithType(hibernateSession, History.class).iterator();
        while (it.hasNext()) {
            History event = (History) it.next();
            if (targetObjectId == 0 || (event.getTargetId() == targetObjectId &&
                    StringUtils.equals(event.getAction(), action))) {
                HistoryList.add(event);
            }
        }
        return HistoryList;
    }


    public List getObjectListWithType(MockSession hibernateSession, Class objectClass) {
        List objectList = new ArrayList();
        for (int i = 0; i < hibernateSession.saveObjects.size(); i++) {
            if (objectClass.isAssignableFrom(hibernateSession.saveObjects.get(i).getClass())) {
                objectList.add(hibernateSession.saveObjects.get(i));
            }
        }
        return objectList;
    }

    public void setUpMockAppender() {
        appenders = Logger.getRootLogger().getAllAppenders();
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new AppenderSkeleton() {
            protected void append(LoggingEvent event) {
            }

            public void close() {
            }

            public boolean requiresLayout() {
                return false;
            }
        });
    }

    public void setUpDomainContext() {
        DomainContext context = new DomainContext();
        context.setProjectId(10);
        context.save(request);
    }

    public void tearDownMockAppender() {
        Logger rootLogger = Logger.getRootLogger();
        while (appenders.hasMoreElements()) {
            rootLogger.addAppender((Appender) appenders.nextElement());
        }
    }

    public static void dumpRequest(HttpServletRequest request) {
        Enumeration pnames = request.getParameterNames();
        while (pnames.hasMoreElements()) {
            String key = (String) pnames.nextElement();
            System.out.print(key + "=");
            String[] values = request.getParameterValues(key);
            if (values.length == 1) {
                System.out.println(values[0]);
            } else {
                System.out.println(StringUtils.join(values, ","));
            }
        }
    }

    public void assertDataSampleExist(MockSession hibernateSession, String aspect, int referenceId, double value, Date date) {
        List dataSampleList = getObjectListWithType(hibernateSession, DataSample.class);
        boolean isDataSample = false;
        Iterator it = dataSampleList.iterator();
        while (it.hasNext()) {
            DataSample ds = (DataSample) it.next();
            if (StringUtils.equals(ds.getAspect(), aspect) && ds.getReferenceId() == referenceId &&
                    ds.getValue() == value && areClose(date, ds.getSampleTime()))
                isDataSample = true;
        }
        Assert.assertTrue("no datasample: aspect=" + aspect + ", sampleDate=" + date +
                ", referenceId=" + referenceId + ", value=" + value, isDataSample);
    }

    private boolean areClose(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        cal.add(Calendar.HOUR, -2);
        Date lowerBorder = cal.getTime();
        cal.add(Calendar.HOUR, 4);
        Date upperBorder = cal.getTime();
        return date1.after(lowerBorder) && date1.before(upperBorder);
    }

    public void assertNoDataSample(MockSession hibernateSession, String aspect, int referenceId, Date date) {
        List dataSampleList = getObjectListWithType(hibernateSession, DataSample.class);
        boolean isDataSample = false;
        Iterator it = dataSampleList.iterator();
        while (it.hasNext()) {
            DataSample ds = (DataSample) it.next();
            if (StringUtils.equals(ds.getAspect(), aspect) && ds.getReferenceId() == referenceId &&
                    areClose(date, ds.getSampleTime()))
                isDataSample = true;
        }
        Assert.assertFalse("datasample: aspect=" + aspect + ", sampleDate=" + date +
                ", referenceId=" + referenceId + " exists", isDataSample);
    }

    public static String getAbsoluteTestURL(){
        XPlannerProperties properties = new XPlannerProperties();
        return properties.getProperty("xplanner.application.url");
    }

    public static String getRelativeTestURL(){
        XPlannerProperties properties = new XPlannerProperties();
        String baseUrl = properties.getProperty("xplanner.application.url");
//        Pattern pattern = Pattern.compile("^(?:\\w+://)?.*?(/.*?)/?$");
        Pattern pattern = Pattern.compile("^(?:\\w+://)?.*?((/.+?)?)/?$");
        Matcher matcher = pattern.matcher(baseUrl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}

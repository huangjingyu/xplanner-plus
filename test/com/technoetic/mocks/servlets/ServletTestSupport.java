package com.technoetic.mocks.servlets;

import java.util.HashMap;

import com.kizna.servletunit.HttpServletRequestSimulator;
import com.kizna.servletunit.HttpServletResponseSimulator;
import com.kizna.servletunit.HttpSessionSimulator;
import com.kizna.servletunit.ServletConfigSimulator;
import com.kizna.servletunit.ServletContextSimulator;
import com.technoetic.mocks.servlets.jsp.MockJspWriter;
import com.technoetic.mocks.servlets.jsp.MockPageContext;

public class ServletTestSupport {
    public static class XHttpServletResponseSimulator extends HttpServletResponseSimulator {
        public String encodeURL(String url) {
            return url;
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
    }

    public MockPageContext pageContext;
    public HttpServletRequestSimulator request;
    public HttpSessionSimulator session;
    public XHttpServletResponseSimulator response;
    public ServletConfigSimulator servletConfig;
    public XServletContextSimulator servletContext;
    public MockJspWriter mockJspWriter;

    public ServletTestSupport() {
        request = new HttpServletRequestSimulator();
        response = new XHttpServletResponseSimulator();
        session = (HttpSessionSimulator)request.getSession();
        pageContext = new MockPageContext();
        pageContext.getRequestReturn = request;
        pageContext.getResponseReturn = response;
        servletConfig = new ServletConfigSimulator();
        pageContext.getServletConfigReturn = servletConfig;
        servletContext = new XServletContextSimulator();
        pageContext.getServletContextReturn = servletContext;
        pageContext.getSessionReturn = request.getSession();
        mockJspWriter = new MockJspWriter();
        pageContext.getOutReturn = mockJspWriter;
    }

}

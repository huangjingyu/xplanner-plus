package com.technoetic.xplanner.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class RequestCharacterEncodingFilter implements Filter {

    private Logger log = Logger.getLogger(getClass());

    public static final String REQUEST_CHARACTER_ENCODING = "requestCharacterEncoding";

    private String encoding = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter(REQUEST_CHARACTER_ENCODING);
    }

    private String getInitParameter(FilterConfig filterConfig, String parameterName) throws ServletException {
        String value = filterConfig.getInitParameter(parameterName);
        if (StringUtils.isEmpty(value)) {
            throw new ServletException(getClass().getName() + ": " + parameterName + " is required");
        }
        return value;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }

    public void destroy() {
    }

}

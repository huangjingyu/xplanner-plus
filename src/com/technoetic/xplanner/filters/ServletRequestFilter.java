package com.technoetic.xplanner.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.RequestUtils;

/**
 * This filter saves the HTTP request for SOAP purposes. The SOAP adapters don't have
 * access to the HTTP execution context.
 */
public class ServletRequestFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                            FilterChain filterChain) throws IOException, ServletException {
        try {
        	HttpServletRequest servletRequest = (HttpServletRequest)request;
			ThreadServletRequest.set(servletRequest);
			StringBuffer requestURL = servletRequest.getRequestURL();
			if(StringUtils.isNotBlank(servletRequest.getQueryString())){
				requestURL.append("?").append(servletRequest.getQueryString());
			}
			request.setAttribute("currentPageUrl", requestURL);
			request.setAttribute("appPath", RequestUtils.absoluteURL(servletRequest, "/"));
            filterChain.doFilter(request, response);
        } finally {
            ThreadServletRequest.set(null);
        }
    }

    public void destroy() {
    }

}

package com.technoetic.xplanner.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class WebResourceCollection {
    private ArrayList urlPatterns = new ArrayList();
    private String name;

    public void addUrlPattern(String pattern) {
        urlPatterns.add(pattern);
    }

    public Collection getUrlPatterns() {
        return urlPatterns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean matches(HttpServletRequest request) {
        Iterator urlPatterns = getUrlPatterns().iterator();
        while (urlPatterns.hasNext()) {
            String urlPattern = (String)urlPatterns.next();
            if (isMatchingPathInfo(request, urlPattern)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchingPathInfo(HttpServletRequest request, String urlPattern) {
        String path = request.getServletPath() +
                (request.getPathInfo() == null ? "" : request.getPathInfo());
        return (urlPattern.equals("/") && (path.equals("/"))) ||
                (urlPattern.length() > 2 && urlPattern.startsWith("*.") && path != null && path.endsWith(urlPattern.substring(2))) ||
                (urlPattern.endsWith("*") && path != null && path.startsWith(urlPattern.substring(0, urlPattern.length() - 1))) ||
                (StringUtils.equals(urlPattern, path));
    }

}

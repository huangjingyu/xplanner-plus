package com.technoetic.xplanner.wiki;

import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.filters.ThreadServletRequest;

public class DomainObjectSchemeHandler implements SchemeHandler {
    private final Logger log = Logger.getLogger(getClass());
    private final String action;
    private static HashMap schemeClasses = new HashMap();

    static {
        schemeClasses.put("project", Project.class);
        schemeClasses.put("iteration", Iteration.class);
        schemeClasses.put("story", UserStory.class);
        schemeClasses.put("task", Task.class);
        schemeClasses.put("person", Person.class);
    }

    public DomainObjectSchemeHandler(String action) {
        this.action = action;
    }

    public String translate(Properties properties, String scheme, String location, String linkText) {
        Class domainClass = (Class)schemeClasses.get(scheme);
        Pattern pattern = Pattern.compile("^(\\d+)(.*)$");
        Matcher matcher = pattern.matcher(location);
        try {
            matcher.find();
            location = matcher.group(1);
            Object object = ThreadSession.get().load(domainClass, new Integer(location));
            if (linkText == null) {
                try {
                    linkText = scheme + ": "+ BeanUtils.getProperty(object, "name");
                } catch (Exception e) {
                   // ignored
                   linkText = scheme + ":" + location;
                }
            }
//FIXME: Why using a second group? What is it for?
            linkText += matcher.group(2);
            HttpServletRequest request = ThreadServletRequest.get();
            return "<a href='"+request.getContextPath()+"/do/view/"+
                    (action != null ? action : scheme)+"?oid="+location+"'>" + linkText + "</a>";
        } catch (Exception e) {
            return "["+scheme+": "+e.getMessage()+"]";
        }
    }
}

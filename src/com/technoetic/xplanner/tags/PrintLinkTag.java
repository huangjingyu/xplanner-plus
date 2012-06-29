/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 28, 2004
 * Time: 9:58:41 PM
 */
package com.technoetic.xplanner.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.displaytag.util.DefaultRequestHelper;
import org.displaytag.util.Href;
import org.displaytag.util.RequestHelper;

public class PrintLinkTag extends LinkTag {
    public static final String PRINT_PARAMETER_NAME = "print";

    public static boolean isInPrintMode(PageContext pageContext) {
        return pageContext.getRequest().getParameter( PRINT_PARAMETER_NAME ) != null;
    }

    
    public int doStartTag() throws JspException {
        RequestHelper helper = new DefaultRequestHelper((HttpServletRequest) pageContext.getRequest(),
                                                        (HttpServletResponse) pageContext.getResponse());
        Href basehref = helper.getHref();
        Href href = new Href("");
        href.setParameterMap(basehref.getParameterMap());
        href.addParameter(PrintLinkTag.PRINT_PARAMETER_NAME,"");
        setHref(href.toString());
        return super.doStartTag();
    }
}
package com.technoetic.xplanner.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

public class ProgressBarHtmlTag extends TagSupport implements ProgressBarTag  {
	private static final long serialVersionUID = 9020272510102623317L;
	private double actual;
    private double estimate;
    private boolean complete = false;
    private String width;
    private int height;

    public void setActual(double actual) {
        this.actual = actual;
    }

    public void setEstimate(double estimate) {
        this.estimate = estimate;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setWidth(int width) {
        this.width = String.valueOf(width);
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int doEndTag() throws JspException {
        try {
            double total = Math.max(actual, estimate);
            int workedPercent = 0;
            if (total == 0.0) {
                workedPercent = complete ? 100 : 0;
            } else {
                workedPercent = (int)(Math.round(Math.min(actual, estimate) / total * 100));
            }
            int unworkedPercent = 100 - workedPercent;

            pageContext.getOut().println("<table cellspacing=\"0\" cellpadding=\"0\" ");
            pageContext.getOut().println(" class=\"progressbar\"");
            if (StringUtils.isNotBlank(width)) {
                pageContext.getOut().println(" width=\"" + width + "\"");
            }
            if (height > 0) {
                //pageContext.getOut().println(" height=\"" + height + "\"");
            }
            pageContext.getOut().println(">");
            pageContext.getOut().println("<tr>");
            if (workedPercent > 0) {
                pageContext.getOut().println("<td class=\"");
                pageContext.getOut().print((complete ? "progressbar_completed" : "progressbar_uncompleted") + "\" width=\"" + workedPercent+ "%\" >");
                pageContext.getOut().println("&nbsp;</td>");
            }
            if (unworkedPercent > 0) {
                pageContext.getOut().println("<td");
                pageContext.getOut().print(" class=\"" +
                		(actual > estimate ? "progressbar_exceeded" : "progressbar_unworked") + "\" width=\"" + unworkedPercent +
                				"%\" >");
                pageContext.getOut().println("&nbsp;</td>");
            }
            pageContext.getOut().println("</tr>");
            pageContext.getOut().println("</table>");
        } catch (IOException ex) {
            throw new JspException("Caught IOException: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            throw new JspException("Caught NumberFormatException: " + ex.getMessage());
        }
        return EVAL_PAGE;
    }
}
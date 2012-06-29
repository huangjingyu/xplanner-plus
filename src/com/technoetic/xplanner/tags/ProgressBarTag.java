package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.Tag;

public interface ProgressBarTag extends Tag {
    void setActual(double actual);

    void setEstimate(double estimate);

    void setComplete(boolean complete);

    void setWidth(String width);

    void setHeight(int height);
}

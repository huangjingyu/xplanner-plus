package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.Tag;

public interface WritableTag extends Tag{
    boolean isWritable()
        throws Exception;
}

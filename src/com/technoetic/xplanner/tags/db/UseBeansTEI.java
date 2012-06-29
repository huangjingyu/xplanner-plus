package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class UseBeansTEI extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData data) {
        return new VariableInfo[]{
            new VariableInfo(
                    data.getAttributeString("id"),
                    "java.util.Collection",
                    true, VariableInfo.AT_END)
        };
    }
}

package com.technoetic.xplanner.wiki;

import java.util.Properties;

public interface WikiFormat {
    String ESCAPE_BRACKETS_KEY = "xplanner.escape.brackets";

    String format(String text);

    void setProperties(Properties  properties);
}

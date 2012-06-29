package com.technoetic.xplanner.wiki;

import java.util.Properties;

public class PropertySimpleSchemeHandler extends SimpleSchemeHandler
{
    private String key = null;
    private static final String NEW_TARGET = "_new";

    public PropertySimpleSchemeHandler(String key)
    {
        this.key = key;
    }

   protected String getTarget()
    {
        return NEW_TARGET;
    }

   protected String getPattern(Properties properties)
   {
      return properties.getProperty(key);
   }

}

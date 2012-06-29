package com.technoetic.xplanner.wiki;

import java.util.Properties;

public interface SchemeHandler {
   String translate(Properties properties, String scheme, String location, String linkText);
}

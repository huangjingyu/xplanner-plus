package com.technoetic.xplanner.wiki;

import java.util.Properties;

import org.apache.oro.text.perl.Perl5Util;

public class SimpleSchemeHandler implements SchemeHandler {
   private Perl5Util perl = new Perl5Util();
   private final String pattern;
   private static final String TARGET_TOP = "_top";

   public SimpleSchemeHandler() {
      this.pattern = null;
   }

   public SimpleSchemeHandler(String pattern) {
      this.pattern = pattern;
   }

   public String translate(Properties properties, String scheme, String location, String linkText) {
      String pattern = getPattern(properties);
      location = perl.substitute("s|(/)|\\\\$1|g", location);
      String url = perl.substitute("s/\\$1/" + location + "/go", pattern);
      return "<a href=\"" + url + "\" target=\"" + getTarget() + "\">" +
             (linkText != null ? linkText : (scheme + ":" + location)) + "</a>";
   }

   protected String getPattern(Properties properties) {
      return this.pattern;
   }

   protected String getTarget() {
      return TARGET_TOP;
   }

   public String toString() {
      return "[" + getClass().getName() + " pattern=" + pattern + "]";
   }
}

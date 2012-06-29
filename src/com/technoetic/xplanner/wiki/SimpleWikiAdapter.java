package com.technoetic.xplanner.wiki;

import com.technoetic.xplanner.XPlannerProperties;

public class SimpleWikiAdapter implements ExternalWikiAdapter {
    public String formatWikiWord(String wikiWord) {
       String url = new XPlannerProperties().getProperty(XPlannerProperties.WIKI_URL_KEY);
        if (url != null) {
            url = url.replaceAll("\\$1", wikiWord);
            return "<a href='" + url + "'>" + wikiWord + "</a>";
        } else {
            return wikiWord;
        }
    }
}

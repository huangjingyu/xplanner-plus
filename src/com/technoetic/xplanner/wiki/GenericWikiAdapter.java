package com.technoetic.xplanner.wiki;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.XPlannerProperties;

public class GenericWikiAdapter implements ExternalWikiAdapter {
    private Logger log = Logger.getLogger(getClass());
    private static HashSet existingTopics = new HashSet();
    private String existingTopicUrlPattern;
    private String newTopicUrlPattern;
    public String newTopicPattern;

    public GenericWikiAdapter() {
       XPlannerProperties properties = new XPlannerProperties();
        initialize(properties);
    }

    protected void initialize(XPlannerProperties properties) {
        existingTopicUrlPattern = properties.getProperty("twiki.wikiadapter.topic.url.existing");
        newTopicUrlPattern = properties.getProperty("twiki.wikiadapter.topic.url.new");
        newTopicPattern = properties.getProperty("twiki.wikiadapter.topic.newpattern");
    }

    public String formatWikiWord(String wikiWord) {
        return isTopicExisting(wikiWord)
                ? formatLinkToExistingTopic(wikiWord)
                : formatLinkToCreateTopic(wikiWord);
    }

    protected String formatLinkToCreateTopic(String wikiWord) {
        return wikiWord + "<a href='" + formatUrl(wikiWord, newTopicUrlPattern) + "'>?</a>";
    }

    protected String formatLinkToExistingTopic(String wikiWord) {
        return "<a href='" + formatUrl(wikiWord, existingTopicUrlPattern) + "'>" + wikiWord + "</a>";
    }

    protected String formatUrl(String wikiWord, String urlPattern) {
        return substituteWikiWord(wikiWord, urlPattern);
    }

    private String substituteWikiWord(String wikiWord, String text) {
        return text.replaceAll("\\$\\{word\\}", wikiWord);
    }

    private boolean isTopicExisting(String wikiWord) {
        // This is a somewhat inefficient, but general approach
        if (existingTopics.contains(wikiWord)) {
            return true;
        }
        try {
            URL url = new URL(formatUrl(wikiWord, existingTopicUrlPattern));
            InputStream page = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(page));
            try {
                String line = reader.readLine();
                while (line != null) {
                    if (line.matches(substituteWikiWord(wikiWord, newTopicPattern))) {
                        return false;
                    }
                    line = reader.readLine();
                }
            } finally {
                reader.close();
            }
        } catch (java.io.IOException e) {
            // ignored - return false
            return false;
        }
        existingTopics.add(wikiWord);
        return true;
    }

    public String getExistingTopicUrlPattern() {
        return existingTopicUrlPattern;
    }

    public String getNewTopicUrlPattern() {
        return newTopicUrlPattern;
    }

    public String getNewTopicPattern() {
        return newTopicPattern;
    }
}

package com.technoetic.xplanner.wiki;

import com.technoetic.xplanner.XPlannerProperties;

public class ExternalTwikiAdapter extends GenericWikiAdapter {
    private String defaultSubwiki;

    protected void initialize(XPlannerProperties properties) {
        super.initialize(properties);
        defaultSubwiki = properties.getProperty("twiki.wikiadapter.subwiki.default");
        if (defaultSubwiki == null) {
            defaultSubwiki = "Main";
        }
    }

    protected String formatLinkToCreateTopic(String wikiWord) {
        String subwiki = getSubWiki(wikiWord);
        String word = getWord(wikiWord);
        String url = substitute(subwiki, word, getNewTopicUrlPattern());
        return word + "<a href='" + url + "'>?</a>";
    }

    protected String formatUrl(String wikiWord, String urlPattern) {
        String subwiki = getSubWiki(wikiWord);
        String word = getWord(wikiWord);
        return substitute(subwiki, word, urlPattern);
    }

    private String substitute(String subwiki, String word, String urlPattern) {
        String url = urlPattern;
        url = url.replaceAll("\\$\\{subwiki\\}", subwiki + "/");
        url = url.replaceAll("\\$\\{word\\}", word);
        return url;
    }

    private String getSubWiki(String wikiWord) {
        int periodOffset = wikiWord.lastIndexOf(".");
        String subwiki;
        if (periodOffset != -1) {
            subwiki = wikiWord.substring(0, periodOffset);
        } else {
            subwiki = defaultSubwiki;
        }
        return subwiki;
    }

    private String getWord(String wikiWord) {
        int periodOffset = wikiWord.lastIndexOf(".");
        return periodOffset != -1
                ? wikiWord.substring(periodOffset + 1, wikiWord.length())
                : wikiWord;
    }

    protected String formatLinkToExistingTopic(String wikiWord) {
        String subwiki = getSubWiki(wikiWord);
        String word = getWord(wikiWord);
        String url = substitute(subwiki, word, getExistingTopicUrlPattern());
        return "<a href='" + url + "'>" + word + "</a>";
    }
}

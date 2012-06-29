package com.technoetic.xplanner.wiki;

/**
 * Interface for external wiki adapters.
 */
public interface ExternalWikiAdapter {
    /**
     * Format a wiki word. Typically, this will format a known word as a link to the
     * wiki page and create a <word>? type of link for unknown words. The unknown words
     * link to a page that allows definition of the word/topic.
     * @param wikiWord the word to be formatted
     * @return the formatted wiki word
     */
    String formatWikiWord(String wikiWord);
}

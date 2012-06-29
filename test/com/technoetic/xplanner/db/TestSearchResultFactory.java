package com.technoetic.xplanner.db;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Dec 15, 2004
 * Time: 3:02:37 PM
 */

import java.util.HashMap;

import junit.framework.TestCase;

import com.technoetic.xplanner.domain.SearchResult;

public class TestSearchResultFactory extends TestCase {
    SearchResultFactory helper;
    public static final String EXPECTED_SEARCH_CRITERIA = "Some";

    protected void setUp() throws Exception {
        super.setUp();
        helper = new SearchResultFactory(new HashMap());
    }

    public void testConvertObjectToSearchResult_NullDescription() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(
            new FakeNameable(0, "The title", ""), EXPECTED_SEARCH_CRITERIA);
        assertEquals("", searchResult.getMatchPrefix());
        assertEquals("", searchResult.getMatchingText());
        assertEquals("", searchResult.getMatchSuffix());
        assertFalse(searchResult.isMatchInDescription());
    }

    public void testConvertObjectToSearchResult_MatchInDescriptionFront() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(
            new FakeNameable(0, "The title", "Some Text"), EXPECTED_SEARCH_CRITERIA);
        assertTrue("match not detected", searchResult.isMatchInDescription());
        assertEquals("", searchResult.getMatchPrefix());
        assertEquals("Some", searchResult.getMatchingText());
        assertEquals(" Text", searchResult.getMatchSuffix());
    }

    public void testConvertObjectToSearchResult_MatchInDescriptionBack() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(
            new FakeNameable(0, "The title", "Some Text"), "text");
        assertTrue("match not detected", searchResult.isMatchInDescription());
        assertEquals("Some ", searchResult.getMatchPrefix());
        assertEquals("Text", searchResult.getMatchingText());
        assertEquals("", searchResult.getMatchSuffix());
    }

    public void testConvertObjectToSearchResult_MatchInDescriptionWithinWord() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(
            new FakeNameable(0, "The title", "Description"), "IPT");
        assertEquals("Descr", searchResult.getMatchPrefix());
        assertEquals("ipt", searchResult.getMatchingText());
        assertEquals("ion", searchResult.getMatchSuffix());
        assertTrue(searchResult.isMatchInDescription());
    }

    public void testConvertObjectToSearchResult_MatchInDescriptionLongPrefix() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(
            new FakeNameable(0, "The title", "Description with the word search in it"),
            "SEARCH");
        assertEquals("...scription with the word ", searchResult.getMatchPrefix());
        assertEquals("search", searchResult.getMatchingText());
        assertEquals(" in it", searchResult.getMatchSuffix());
        assertTrue(searchResult.isMatchInDescription());
    }

    public void testConvertObjectToSearchResult_MatchInDescriptionLongSuffix() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(
            new FakeNameable(0, "The title", "The word search is in the description"),
            "SEARCH");
        assertEquals("The word ", searchResult.getMatchPrefix());
        assertEquals("search", searchResult.getMatchingText());
        assertEquals(" is in the description", searchResult.getMatchSuffix());
    }

    public void testConvertObjectToSearchResult_MatchInDescriptionCaseDifferent() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(
            new FakeNameable(0, "The title", "The word SEARCH is in the description"),
            "search");
        assertEquals("The word ", searchResult.getMatchPrefix());
        assertEquals("SEARCH", searchResult.getMatchingText());
        assertEquals(" is in the description", searchResult.getMatchSuffix());
    }

    public void testConvertObjectToSearchResult_MatchInTitle() throws Exception {
        SearchResult searchResult = helper.convertObjectToSearchResult(new FakeNameable(0,
                                                                                        "The title",
                                                                                        "Description with\nno matching\nwords\nin it"),
                                                                       "emptyMatch");
        assertEquals("", searchResult.getMatchPrefix());
        assertEquals("Description with no matching words", searchResult.getMatchingText());
        assertEquals("", searchResult.getMatchSuffix());
    }

}
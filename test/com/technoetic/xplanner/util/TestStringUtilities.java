package com.technoetic.xplanner.util;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Dec 15, 2004
 * Time: 3:14:30 PM
 */

import junit.framework.TestCase;

public class TestStringUtilities extends TestCase {

    public void testGetShortPrefix_noSpace() throws Exception {
        String wholePrefix = "abc";
        assertPrefixMatches(wholePrefix, 1, "c");
        assertPrefixMatches(wholePrefix, 2, "bc");
        assertPrefixMatches(wholePrefix, 3, wholePrefix);
        assertPrefixMatches(wholePrefix, 4, wholePrefix);
    }

    public void testGetShortPrefix_withSpace() throws Exception {
        String wholePrefix = "a b c";
        assertPrefixMatches(wholePrefix, 1, "c");
        assertPrefixMatches(wholePrefix, 2, "b c");
        assertPrefixMatches(wholePrefix, 3, wholePrefix);
        assertPrefixMatches(wholePrefix, 4, wholePrefix);
        assertPrefixMatches("a   b", 4, "a   b");
    }

    public void testGetShortPrefix_withNewLine() throws Exception {
        String wholePrefix = "a\nb\nc";
        assertPrefixMatches(wholePrefix, 1, "c");
        assertPrefixMatches(wholePrefix, 2, "b\nc");
        assertPrefixMatches(wholePrefix, 3, wholePrefix);
        assertPrefixMatches(wholePrefix, 4, wholePrefix);
    }

    private void assertPrefixMatches(String wholePrefix, int maxPrefixLength,
                                     String expectedPrefix) {
        assertEquals(expectedPrefix, StringUtilities.getShortPrefix(wholePrefix, maxPrefixLength));
    }

    private void assertSuffixMatches(String wholeSuffix, int maxSuffixLength,
                                     String expectedSuffix) {
        assertEquals(expectedSuffix, StringUtilities.getShortSuffix(wholeSuffix, maxSuffixLength));
    }

    public void testGetShortSuffix_noSpace() throws Exception {
        String wholeSuffix = "abc";
        assertSuffixMatches(wholeSuffix, 1, "a");
        assertSuffixMatches(wholeSuffix, 2, "ab");
        assertSuffixMatches(wholeSuffix, 3, wholeSuffix);
        assertSuffixMatches(wholeSuffix, 4, wholeSuffix);
    }

    public void testGetShortSuffix_withSpace() throws Exception {
        String wholeSuffix = "a b c";
        assertSuffixMatches(wholeSuffix, 1, "a");
        assertSuffixMatches(wholeSuffix, 2, "a b");
        assertSuffixMatches(wholeSuffix, 3, wholeSuffix);
        assertSuffixMatches(wholeSuffix, 4, wholeSuffix);
    }

    public void testGetShortSuffix_withNewLine() throws Exception {
        String wholeSuffix = "a\nb\nc";
        assertSuffixMatches(wholeSuffix, 1, "a");
        assertSuffixMatches(wholeSuffix, 2, "a\nb");
        assertSuffixMatches(wholeSuffix, 3, wholeSuffix);
        assertSuffixMatches(wholeSuffix, 4, wholeSuffix);
    }
}
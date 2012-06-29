/*
 * The JUnit-addons Software License, Version 1.0
 *     (based on the Apache Software License, Version 1.1)
 *
 * Copyright (c) 2003 Vladimir R. Bossicard.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by Vladimir R.
 *        Bossicard as well as other contributors
 *        (http://junit-addons.sourceforge.net/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The name "JUnit-addons" must not be used to endorse or promote
 *    products derived from this software without prior written
 *    permission. For written permission, please contact
 *    vbossica@users.sourceforge.net.
 *
 * 5. Products derived from this software may not be called "JUnit-addons"
 *    nor may "JUnit-addons" appear in their names without prior written
 *    permission of the project managers.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ======================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals.  For more information on the JUnit-addons Project, please
 * see <http://junit-addons.sourceforge.net/>.
 */

package junitx.framework;

import junit.framework.AssertionFailedError;

/**
 * Thrown when an assert equals for Strings failed (class
 * <tt>junitx.framework.Assert</tt>).
 *
 * <h4>Usage</h4>
 * To use this new class, you'll have to invoke the <tt>assertEquals</tt> method
 * (for String objects) of the <tt>junitx.framework.Assert</tt> class:
 *
 * <pre>
 *    junitx.framework.Assert.assertEquals(message, expected, actual);
 * </pre>
 *
 * @version $Revision: 90 $ $Date: 2004-10-15 17:20:20 +0300 (Пт, 15 окт 2004) $
 * @author <a href="mailto:vbossica@users.sourceforge.net">Vladimir R. Bossicard</a>
 */
public class ComparisonFailure extends AssertionFailedError {

    private static final int MINIMAL_LENGTH = 10;

    private String expected;
    private String actual;
    private String message = "";

    /**
     * Constructs a <tt>ComparisonFailure</tt> object.
     */
    public ComparisonFailure(String message,
                             String expected,
                             String actual) {
        super(message);
        this.expected = expected;
        this.actual = actual;
        this.message = createMessage(super.getMessage(), this.expected, this.actual);
    }

    public String getMessage() {
        return this.message;
    }

    /**
     * Creates the message that is returned by the <tt>getMessage</tt> method.
     * The message is optimized so that its 'actual' part does either:
     * <ul>
     * <li>represent a word (delimited by stopper characters)
     * <li>contain 10 characters</li>
     * </ul>
     */
    protected static String createMessage(String message,
                                          String expected,
                                          String actual) {
        if ((expected == null || actual == null) ||
	    (actual.equals(expected))) {
            return format(message, expected, actual, null);
        }

        int end = Math.min(expected.length(), actual.length());
        int beginDiff = 0;
        while ((beginDiff < end) &&
                (expected.charAt(beginDiff) == actual.charAt(beginDiff))) {
            beginDiff++;
        }

        int endDiffExp = expected.length() - 1;
        int endDiffAct = actual.length() - 1;
        while ((endDiffExp >= beginDiff) &&
                (endDiffAct >= beginDiff) &&
                (expected.charAt(endDiffExp) == actual.charAt(endDiffAct))) {
            endDiffAct--;
            endDiffExp--;
        }

        /* the delta is the difference between the actual and expected values.
         * If a character was omitted in the actual string, a delta according
         * to the expected string is calculated */
        String delta = actual.substring(beginDiff, endDiffAct+1);
        if (delta.equals("")) {
            delta = expected.substring(beginDiff, endDiffExp+1);
        }

        /* Now that we have the the minimal and maximal positions, we have to
         * 'expand' the actual values' delta into both directions until it means
         * something (we have a minimal length or we have reached some
         * boundaries). */
        boolean optimized = false;
        while (!optimized) {
            if (endDiffAct - beginDiff < MINIMAL_LENGTH - 1) {
                if (beginDiff > 0 && beginDiff < actual.length() - 1 &&
                    !isStopper(actual.charAt(beginDiff))) {
                    beginDiff--;
                } else if ((endDiffAct >= 0) &&
                        (endDiffAct < actual.length()-1) &&
                        !isStopper(actual.charAt(endDiffAct))) {
                    endDiffAct++;
                    endDiffExp++;
                } else {
                    optimized = true;
                }
            } else {
                optimized = true;
            }
        }

        String expectedDisplay = expected.substring(beginDiff, endDiffExp + 1);
        String actualDisplay = actual.substring(beginDiff, endDiffAct + 1);

        // if the delta is obvious, delete it
        if (delta.equals(actualDisplay)) {
            delta = null;
        }

        if (beginDiff > 0) {
            expectedDisplay = "..." + expectedDisplay;
            actualDisplay = "..." + actualDisplay;
        }
        if (endDiffExp < expected.length() - 1) {
            expectedDisplay = expectedDisplay + "...";
        }
        if (endDiffAct < actual.length() - 1) {
            actualDisplay = actualDisplay + "...";
        }

        return format(message, expectedDisplay, actualDisplay, delta);
    }

    private static boolean isStopper(char input) {
        return (input == ' ') ||
                (input == ',') || (input == ';') ||
                (input == '(') || (input == ')') ||
                (input == '[') || (input == ']') ||
                (input == '{') || (input == '}');
    }

    private static String format(String message,
                                 String expected,
                                 String actual,
                                 String delta) {
        String formatted = "";
        if (message != null) {
            formatted = message + " ";
        }
        if (delta == null) {
            return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
        } else {
            return formatted + "expected:<" + expected + "> but was:<" + actual + "> ['" + delta + "']";
        }
    }

}


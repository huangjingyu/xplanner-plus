package com.technoetic.xplanner.easymock;

import org.apache.commons.lang.StringUtils;
import org.easymock.ArgumentsMatcher;

public class SameArgumentsMatcher implements ArgumentsMatcher {
    public boolean matches(Object[] expected, Object[] actual) {
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                return false;
            }
        }
        return true;
    }

    public String toString(Object[] arguments) {
        return StringUtils.join(arguments, ",");
    }
}

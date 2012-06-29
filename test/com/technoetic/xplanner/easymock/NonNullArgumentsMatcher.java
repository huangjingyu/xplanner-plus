package com.technoetic.xplanner.easymock;

import org.apache.commons.lang.StringUtils;
import org.easymock.ArgumentsMatcher;

class NonNullArgumentsMatcher implements ArgumentsMatcher {
    public boolean matches(Object[] expected, Object[] actual) {
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] == null) {
                return false;
            }
        }
        return true;
    }

    public String toString(Object[] parameters) {
        return StringUtils.join(parameters, ",");
    }
}

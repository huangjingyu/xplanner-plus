package com.technoetic.xplanner.actions;

import java.util.Iterator;

public class MockIterator implements Iterator {
    private boolean[] hasNextReturns;
    private int hasNextCallCnt;
    private boolean defaultVal;

    public MockIterator(boolean[] hasNextReturns) {
        this.hasNextReturns = hasNextReturns;
        this.hasNextCallCnt = 0;
        this.defaultVal = false;
    }

    public MockIterator(boolean defaultValue) {
        this.hasNextReturns = null;
        this.hasNextCallCnt = 0;
        this.defaultVal = defaultValue;
    }

    public void remove() {
    }

    public boolean hasNext() {
        if (hasNextReturns != null && hasNextCallCnt < hasNextReturns.length) {
            return hasNextReturns[hasNextCallCnt++];
        } else {
            return defaultVal;
        }
    }

    public Object next() {
        return null;
    }
}
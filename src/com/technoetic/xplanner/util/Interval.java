package com.technoetic.xplanner.util;

import java.io.Serializable;

/** The half-open interval [low, high) inclusive on the low end. This class is derived
 * from the Interval class developed at limegroup.org */
public class Interval implements Serializable, Comparable {

    private long low;
    private long high;

    /** @requires low<=high */
    public Interval(long low, long high) {
        if (high < low) {
            throw new IllegalArgumentException("high, low" + high + ", " + low);
        }
        this.low = low;
        this.high = high;
    }

    public Interval(long singleton) {
        this.low = singleton;
        this.high = singleton;
    }

    /**
     * Compares this to another interval by the 'low' element of the interval.
     * If the low elements are the same, then the high element is compared.
     */
    public int compareTo(Object o) {
        Interval other = (Interval)o;
        if (this.low != other.low) {
            return (int)(this.low - other.low);
        } else {
            return (int)(this.high - other.high);
        }
    }

    /**
     * True if this and other are adjacent, i.e. the high end of one equals the
     * low end of the other.
     */
    public boolean adjacent(Interval other) {
        return high == other.low || low == other.high;
    }

    /**
     * True if this and other overlap.
     */
    public boolean overlaps(Interval other) {
        return (this.low < other.high && other.low < this.high)
                || (other.low < this.high && this.low < other.high);
    }

    public String toString() {
        if (low == high) {
            return String.valueOf(low);
        } else {
            return String.valueOf(low) + "-" + String.valueOf(high);
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof Interval)) {
            return false;
        }
        Interval other = (Interval)o;
        return low == other.low && high == other.high;
    }

    public long getLow() {
        return low;
    }

    public long getHigh() {
        return high;
    }
}


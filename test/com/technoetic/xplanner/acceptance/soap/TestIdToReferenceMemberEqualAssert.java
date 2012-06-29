package com.technoetic.xplanner.acceptance.soap;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junitx.framework.PropertyAccessStrategy;

public class TestIdToReferenceMemberEqualAssert extends TestCase {
    IdToReferenceMemberEqualAssert equalsAssert = new IdToReferenceMemberEqualAssert();

    public void testNotInterestingProperty() throws Exception {
        assertFalse(equalsAssert.assertEquals("reference", new Object(), new Object(), new PropertyAccessStrategy()));
    }

    public void testIdPropertyMatchingEqualProperties() throws Exception {
        assertTrue(equalsAssert.assertEquals("referredId", new ReferenceIdOwner(1), new Referrer(new Referree(1)), new PropertyAccessStrategy()));
        assertTrue(equalsAssert.assertEquals("referredId", new Referrer(new Referree(1)), new ReferenceIdOwner(1), new PropertyAccessStrategy()));
    }

    public void testIdPropertyMatchingNotEqualProperties() throws Exception {
        try {
            equalsAssert.assertEquals("referredId", new ReferenceIdOwner(1), new Referrer(new Referree(2)), new PropertyAccessStrategy());
            fail("Did not throw an assertion");
        } catch (AssertionFailedError e) {
            return;
        }
        try {
            equalsAssert.assertEquals("referredId", new Referrer(new Referree(2)),new ReferenceIdOwner(1), new PropertyAccessStrategy());
            fail("Did not throw an assertion");
        } catch (AssertionFailedError e) {
            return;
        }
    }

    public void testIdPropertyNotMatchingProperties() throws Exception {
        assertFalse(equalsAssert.assertEquals("referredId", new ReferenceIdOwner(1), new Object(), new PropertyAccessStrategy()));
    }

    public class ReferenceIdOwner {
        int referredId;

        public ReferenceIdOwner(int referreeId) {this.referredId = referreeId; }

        public int getReferredId() {return referredId;}

        public void setReferredId(int referredId) {this.referredId = referredId; }

    }

    public class Referrer {
        Referree referred;

        public Referrer(Referree reference) { this.referred = reference; }

        public Referree getReferred() {return referred;}

        public void setReferred(Referree referred) {this.referred = referred; }
    }

    public class Referree {
        int id;

        public Referree(int id) {this.id = id; }

        public int getId() {return id;}

        public void setId(int id) {this.id = id; }
    }

}
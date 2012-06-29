
package junitx.framework;

import java.util.Calendar;

/**
 * To be used when equality of a Calendar depends only on its time and not its zone and other attributes of Calendar
 * that doesn't affect its time
 */
public class TimeOnlyCalendarMemberEqualAssert extends ValueMemberEqualAssert {
    public boolean assertValueEquals(String propertyName, Object expected, Object actual) {
        if (expected instanceof Calendar && actual instanceof Calendar) {
            Assert.assertEquals(propertyName, getMillis(expected), getMillis(actual));
            return true;
        }
        return false;
    }

    private long getMillis(Object expected) {
        return ((Calendar)expected).getTimeInMillis();
    }
}


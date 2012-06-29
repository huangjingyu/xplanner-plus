
package junitx.framework;

import java.util.Calendar;
import java.util.Date;

public class CalendarDateMemberEqualAssert extends ValueMemberEqualAssert {
    public boolean assertValueEquals(String propertyName, Object expected, Object actual) {
        if (expected instanceof Date && actual instanceof Calendar) {
            Assert.assertEquals(propertyName, getDate(expected), getDateFromCalendar(actual));
            return true;
        } else if (expected instanceof Calendar && actual instanceof Date) {
            Assert.assertEquals(propertyName, getDateFromCalendar(expected), getDate(actual));
            return true;
        }
        return false;
    }

    private Date getDate(Object expected) {
        return new Date(((Date)expected).getTime());
    }

    private Date getDateFromCalendar(Object actual) {
        return new Date(((Calendar) actual).getTimeInMillis());
    }


}
package junitx.framework;

public class ArrayMemberEqualAssert extends ValueMemberEqualAssert {
    public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
        if (expectedValue != null && expectedValue.getClass().isArray()) {
            if (expectedValue.getClass().getComponentType() == byte.class) {
                ArrayAssert.assertEquals(propertyName, (byte[]) expectedValue, (byte[]) actualValue);
            } else {
                ArrayAssert.assertEquals(propertyName, (Object[]) expectedValue, (Object[]) actualValue);
            }
            return true;
        }
        return false;
    }
}
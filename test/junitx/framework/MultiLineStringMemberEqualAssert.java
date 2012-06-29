package junitx.framework;

public class MultiLineStringMemberEqualAssert extends ValueMemberEqualAssert {
    public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
        if (expectedValue instanceof String && actualValue instanceof String) {
            String expected = stripNewlines((String) expectedValue);
            String actual = stripNewlines((String) actualValue);
            Assert.assertEquals(propertyName, expected, actual);
            return true;
        }
        return false;
    }
    private String stripNewlines(String value) {
        return value.replaceAll("[\r\n]", "");
    }
}
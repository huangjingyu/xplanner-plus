
package junitx.framework;


public abstract class ValueMemberEqualAssert implements MemberEqualAssert {
    public boolean assertEquals(String memberName,
                                Object expectedObject,
                                Object actualObject,
                                MemberAccessStrategy accessStrategy) {
        Object expectedValue = accessStrategy.getValidMemberValue(expectedObject, memberName);
        Object actualValue = accessStrategy.getValidMemberValue(actualObject, memberName);
        if (expectedValue == null || actualValue == null) {
            Assert.assertEquals(memberName, expectedValue, actualValue);
            return true;
        }
        return assertValueEquals(memberName, expectedValue, actualValue);
    }

    public abstract boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue);

}
package junitx.framework;

public class DoubleMemberEqualAssert extends ValueMemberEqualAssert {
    private double delta;

    public DoubleMemberEqualAssert() { this(0.009); }
    public DoubleMemberEqualAssert(double delta) { this.delta = delta; }

    public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
        if (expectedValue instanceof Double) {
            Assert.assertEquals(propertyName,
                                ((Double) expectedValue).doubleValue(),
                                ((Double) actualValue).doubleValue(),
                                delta);
            return true;
        }
        return false;
    }
}
package junitx.framework;

public interface MemberEqualAssert {
    boolean assertEquals(String propertyName, Object expectedObject, Object actualObject, MemberAccessStrategy accessStrategy);
}
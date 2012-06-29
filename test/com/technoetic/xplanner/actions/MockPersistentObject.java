package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.DummyDomainObject;

public class MockPersistentObject extends DummyDomainObject {
    private String name;
    private int integerVariable;
    private String stringVariable;
    private MockPersistentObject anotherVariable = null;

    public int getIntegerVariable() {
        return integerVariable;
    }

    public void setIntegerVariable(int i) {
        integerVariable = i;
    }

    public String getStringVariable() {
        return stringVariable;
    }

    public void setStringVariable(String s) {
        stringVariable = s;
    }

    public MockPersistentObject getAnotherVariable() {
        return anotherVariable;
    }

    public void setAnotherVariable(MockPersistentObject anotherVariable) {
        this.anotherVariable = anotherVariable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

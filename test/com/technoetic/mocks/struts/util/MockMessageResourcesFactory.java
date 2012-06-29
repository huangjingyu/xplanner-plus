package com.technoetic.mocks.struts.util;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

public class MockMessageResourcesFactory extends MessageResourcesFactory {
    public boolean getReturnNullCalled;
    public Boolean getReturnNullReturn;

    public boolean getReturnNull() {
        getReturnNullCalled = true;
        return getReturnNullReturn.booleanValue();
    }

    public boolean setReturnNullCalled;
    public boolean setReturnNullReturnNull;

    public void setReturnNull(boolean returnNull) {
        setReturnNullCalled = true;
        setReturnNullReturnNull = returnNull;
    }

    public boolean createResourcesCalled;
    public MessageResources createResourcesReturn;
    public String createResourcesConfig;

    public MessageResources createResources(String config) {
        createResourcesCalled = true;
        createResourcesConfig = config;
        return createResourcesReturn;
    }
}
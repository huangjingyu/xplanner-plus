package com.technoetic.mocks.struts.util;

import java.util.HashMap;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

public class MockMessageResources extends MessageResources {
    public MockMessageResources(MessageResourcesFactory factory, String config) {
        super(factory, config);
    }

    public HashMap resources = new HashMap();

    public boolean getMessageCalled;
    public Locale getMessageLocale;
    public String getMessageKey;
    public String getMessageReturn;

    public String getMessage(Locale locale, String key) {
        getMessageCalled = true;
        getMessageLocale = locale;
        getMessageKey = key;
        if (getMessageReturn == null) {
            return (String)resources.get(key);
        } else {
            return getMessageReturn;
        }
    }

    public void setMessage(String key, String msg) {
        resources.put(key, msg);
    }
}

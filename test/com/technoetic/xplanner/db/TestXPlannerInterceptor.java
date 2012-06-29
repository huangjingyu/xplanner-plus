package com.technoetic.xplanner.db;

import java.io.Serializable;
import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.XPlannerInterceptor;

public class TestXPlannerInterceptor extends TestCase {
    private XPlannerInterceptor interceptor;
    private Object mockEntity;
    private Serializable mockId;
    private Object[] mockCurrentState;
    private Object[] mockPreviousState;
    private String[] mockPropertyNames;
    private Type[] mockTypes;

    protected void setUp() throws Exception {
        super.setUp();
        mockEntity = new Object();
        mockId = new Serializable() {
        };
        mockCurrentState = new Object[2];
        mockPreviousState = new Object[2];
        mockTypes = new Type[]{Hibernate.STRING, Hibernate.STRING};
        interceptor = new XPlannerInterceptor();
    }

    public void testOnFlushDirtyUpdateLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "lastUpdateTime"};

        boolean result = interceptor.onFlushDirty(mockEntity, mockId, mockCurrentState,
                mockPreviousState, mockPropertyNames, mockTypes);

        assertTrue("wrong result", result);
        assertTrue("wrong value in state", mockCurrentState[1] instanceof Date);
    }

    public void testOnSaveUpdateLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "lastUpdateTime"};

        boolean result = interceptor.onSave(mockEntity, mockId, mockCurrentState, mockPropertyNames, mockTypes);

        assertTrue("wrong result", result);
        assertTrue("wrong value in state", mockCurrentState[1] instanceof Date);
    }

    public void testOnFlushDirtyUpdateNoLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "bar"};

        boolean result = interceptor.onFlushDirty(mockEntity, mockId, mockCurrentState,
                mockPreviousState, mockPropertyNames, mockTypes);

        assertFalse("wrong result", result);
        assertFalse("wrong value in state", mockCurrentState[1] instanceof Date);
    }

    public void testOnSaveUpdateNoLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "bar"};

        boolean result = interceptor.onSave(mockEntity, mockId, mockCurrentState, mockPropertyNames, mockTypes);

        assertFalse("wrong result", result);
        assertFalse("wrong value in state", mockCurrentState[1] instanceof Date);
    }
}

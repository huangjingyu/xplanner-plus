package com.technoetic.xplanner.easymock;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.classextension.EasyMock;

public class EasyMockHelper {
    private List<Object> localControls = new ArrayList<Object>();
	private List<Object> globalControls = new ArrayList<Object>();

    public final <T> T createLocalMock(Class<T> class1) {
    	T mock;
    	if(class1.isInterface()){
    		mock = createMock(class1);
    	}else{
    		mock = EasyMock.createMock(class1);
    	}
    	localControls.add(mock);
    	return mock;
	}

    public final <T> T createGlobalMock(Class<T> class1) {
    	T mock;
    	if(class1.isInterface()){
    		mock = createMock(class1);
    	}else{
    		mock = EasyMock.createMock(class1);
    	}
    	globalControls.add(mock);
    	return mock;
	}

    public final <T> T createNiceGlobalMock(Class<T> class1) {
    	T mock;
    	if(class1.isInterface()){
    		mock = createNiceMock(class1);
    	}else{
    		mock = EasyMock.createNiceMock(class1);
    	}
    	globalControls.add(mock);
    	return mock;
	}

    public void replayMocks() {
    	for (Object localControl : localControls) {
    		if(localControl.getClass().isInterface()){
    			replay(localControl);
    		}else{
    			EasyMock.replay(localControl);
    		}
		}
    	//Assert.assertTrue("No Global Mocks found!", globalControls.size() > 0);
    	for (Object globalControl : globalControls) {
    		if(globalControl.getClass().isInterface()){
    			replay(globalControl);
    		}else{
    			EasyMock.replay(globalControl);
    		}
		}
    }

    public void verifyMocks() {
    	for (Object localControl : localControls) {
    		if(localControl.getClass().isInterface()){
    			verify(localControl);
    		}else{
    			EasyMock.verify(localControl);
    		}
		}
    	//Assert.assertTrue("No Global Mocks found!", globalControls.size() > 0);
    	for (Object globalControl : globalControls) {
    		if(globalControl.getClass().isInterface()){
    			verify(globalControl);
    		}else{
    			EasyMock.verify(globalControl);
    		}
		}
    }

    public void resetMocks() {
    	for (Object localControl : localControls) {
    		if(localControl.getClass().isInterface()){
    			reset(localControl);
    		}else{
    			EasyMock.reset(localControl);
    		}
		}
    	Assert.assertTrue("No Global Mocks found!", globalControls.size() > 0);
    	for (Object globalControl : globalControls) {
    		if(globalControl.getClass().isInterface()){
    			reset(globalControl);
    		}else{
    			EasyMock.reset(globalControl);
    		}
		}
    }

}

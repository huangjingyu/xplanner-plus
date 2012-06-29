package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.domain.Integration;

public interface IntegrationListener {
    public static final int INTEGRATION_READY_EVENT = 1;

    public void onEvent(int eventType, Integration integration, HttpServletRequest request);
}
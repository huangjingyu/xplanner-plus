/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.module;

import java.util.Map;

import com.technoetic.xplanner.security.LoginModule;

public interface LoginModuleFactory {
   LoginModule newInstance(Map options) throws ConfigurationException;
}
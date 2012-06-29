/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.module;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.LoginModule;

public class LoginModuleLoader {
	public static final String LOGIN_MODULE_PROPERTY_PREFIX = "xplanner.security.login";
	public static final String LOGIN_MODULE_CLASS_KEY = LOGIN_MODULE_PROPERTY_PREFIX + "[{0}].module";
	public static final String LOGIN_MODULE_NAME_KEY = LOGIN_MODULE_PROPERTY_PREFIX + "[{0}].name";
	static final String LOGIN_OPTION_PREFIX = LOGIN_MODULE_PROPERTY_PREFIX + "[{0}].option.";
	private ApplicationContext applicationContext;

	public LoginModule[] loadLoginModules() throws ConfigurationException {
		XPlannerProperties properties = new XPlannerProperties();
		int idx = 0;
		String loginModuleClassName;
		List loginModuleList = new ArrayList();
		while (true) {
			loginModuleClassName = properties.getProperty(getKey(LOGIN_MODULE_CLASS_KEY, idx));
			if (loginModuleClassName == null)
				break;

			String loginModuleName = properties.getProperty(getKey(LOGIN_MODULE_NAME_KEY, idx));
			if (loginModuleName == null) {
				throw new ConfigurationException(
						LoginModule.MESSAGE_NO_MODULE_NAME_SPECIFIED_ERROR_KEY);
			}
			Map options = getOptions(properties, idx);
			loginModuleList.add(createModule(loginModuleClassName, loginModuleName, options));
			idx++;
		}
		return (LoginModule[]) loginModuleList.toArray(new LoginModule[] {});
	}

	private LoginModule createModule(String loginModuleClassName, String loginModuleName, Map options) {
		LoginModule loginModule;
		try {
			Object bean = applicationContext.getBean(loginModuleClassName);
			if (bean instanceof LoginModuleFactory) {
				LoginModuleFactory factory = (LoginModuleFactory) bean;
				loginModule = factory.newInstance(options);
			} else {
				loginModule = (LoginModule) bean;
			}
			loginModule.setName(loginModuleName);
			loginModule.setOptions(options);
		} catch (BeansException e) {
			throw new ConfigurationException(e);
		}
		return loginModule;
	}

	private HashMap getOptions(XPlannerProperties properties, int idx) {
		HashMap options = new HashMap();
		Iterator propertyNames = properties.getPropertyNames();
		while (propertyNames.hasNext()) {
			String name = (String) propertyNames.next();
			String optionName = MessageFormat.format(LOGIN_OPTION_PREFIX,
					new Integer[] { new Integer(idx) });
			if (name.startsWith(optionName)) {
				options.put(name.substring(optionName.length()), properties.getProperty(name));
			}
		}
		return options;
	}

	public static String[] getLoginModuleNames() {
		XPlannerProperties properties = new XPlannerProperties();
		List loginModuleNameList = new ArrayList();
		for (int i = 0; properties.getProperty(getKey(LOGIN_MODULE_NAME_KEY, i)) != null; i++) {
			loginModuleNameList.add(properties.getProperty(getKey(LOGIN_MODULE_NAME_KEY, i)));
		}
		return (String[]) loginModuleNameList.toArray(new String[] {});
	}

	private static String getKey(String propertyKey, int i) {
		return MessageFormat.format(propertyKey, new Integer[] { new Integer(i) });
	}

	@Required
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
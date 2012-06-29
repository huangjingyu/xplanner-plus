package net.sf.xplanner.struts;

import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

public class XPlannerMessageResources extends MessageResources {
	private static final long serialVersionUID = 1599914839332648038L;
	
	private MessageSource messageSource;

	
	public XPlannerMessageResources(MessageResourcesFactory factory, String config) {
		super(factory, config);
	}
	
	public XPlannerMessageResources() {
		super(null, null);
	}

	@Override
	public String getMessage(Locale locale, String key) {
		String keyToResolve = key;
		if(keyToResolve != null && keyToResolve.startsWith("org.apache.struts.taglib.bean")){
			keyToResolve = keyToResolve.replace("org.apache.struts.taglib.bean.","");
		}
		return messageSource.getMessage(keyToResolve, null, locale);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public boolean isPresent(Locale locale, String key) {
		try{
			return super.isPresent(locale, key);
		}catch (NoSuchMessageException e) {
			return false;
		}
	}
}

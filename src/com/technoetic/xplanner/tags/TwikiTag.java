package com.technoetic.xplanner.tags;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.RequestUtils;
import org.hibernate.SessionFactory;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.util.LogUtil;
import com.technoetic.xplanner.wiki.SchemeHandler;
import com.technoetic.xplanner.wiki.SimpleSchemeHandler;
import com.technoetic.xplanner.wiki.TwikiFormat;
import com.technoetic.xplanner.wiki.WikiFormat;

public class TwikiTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -1524094715817563540L;
	private static Logger LOG = LogUtil.getLogger();
	private String name;
	private String property;
	private static WikiFormat formatter;
	private HashMap schemeHandlers;

	final private String prefix = "twiki.scheme.";
	final private String handlerSuffix = ".handler";
	final private String wikiKey = prefix + "wiki";

	public int doEndTag() throws JspException {
		try {
			Object obj = RequestUtils.lookup(pageContext, name, null);
			String content = (String) PropertyUtils.getProperty(obj, property);
			Properties properties = new DomainSpecificPropertiesFactory(
					getRequestContext().getWebApplicationContext().getBean(SessionFactory.class),
					XPlannerProperties.getProperties())
					.createPropertiesFor(obj);
			WikiFormat formatter = getFormatter(properties);
			if (content != null) {
				formatter.setProperties(properties);
				pageContext.getOut()
						.print(formatter.format(content.toString()));
			}
		} catch (Exception ex) {
			throw new JspException(ex);
		}
		return EVAL_PAGE;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getProperty() {
		return property;
	}

	private synchronized WikiFormat getFormatter(Properties properties) {
		if (formatter == null) {
			// Read formatter class name from properties
			if (properties.getProperty("wiki.format") != null) {
				try {
					formatter = (WikiFormat) Class.forName(
							properties.getProperty("wiki.format"))
							.newInstance();
				} catch (Exception e) {
					LOG.error(
							"Cannot instantiate wiki format, using default: ",
							e);
					// Fall back to default
					formatter = new TwikiFormat();
				}
			} else {
				// Fall back to default
				formatter = new TwikiFormat();
			}
		}
		formatter.setProperties(properties);
		if (schemeHandlers == null || schemeHandlers.isEmpty()) {
			schemeHandlers = loadSchemeHandlers(properties);
		} else {
			final String translation = properties.getProperty(wikiKey);
			schemeHandlers.put(wikiKey.substring(prefix.length()),
					new SimpleSchemeHandler(translation));
		}
		formatter = new TwikiFormat(schemeHandlers);
		return formatter;
	}

	private HashMap loadSchemeHandlers(Properties properties) {
		HashMap schemeHandlers = new HashMap();
		Enumeration keys = properties.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if (key.startsWith(prefix)) {
				final String translation = properties.getProperty(key);
				if (key.endsWith(handlerSuffix)
						&& !key.equals(prefix + handlerSuffix.substring(1))) {
					try {
						String className = translation;
						String argument = null;
						int argumentOffset = translation.indexOf(";");
						if (argumentOffset != -1) {
							className = translation
									.substring(0, argumentOffset);
							argument = translation
									.substring(argumentOffset + 1);
						}
						Class handlerClass = Class.forName(className);
						SchemeHandler handler = null;
						try {
							Constructor argConstructor = handlerClass
									.getConstructor(new Class[] { String.class });
							if (argConstructor != null) {
								handler = (SchemeHandler) argConstructor
										.newInstance(new Object[] { argument });
							}
						} catch (Exception e) {
							handler = (SchemeHandler) handlerClass
									.newInstance();
						}
						String handlerKey = key.substring(prefix.length(),
								key.length() - handlerSuffix.length());
						schemeHandlers.put(handlerKey, handler);
					} catch (Exception e) {
						LOG.error("error", e);
					}
				} else {
					schemeHandlers.put(key.substring(prefix.length()),
							new SimpleSchemeHandler(translation));
				}
			}
		}
		return schemeHandlers;
	}

	@Override
	protected int doStartTagInternal() throws Exception {
		return 0;
	}
}
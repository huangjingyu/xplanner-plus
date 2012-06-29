package com.technoetic.xplanner.mail;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.util.HttpClient;

/**
 * User: Mateusz Prokopowicz
 * Date: May 19, 2005
 * Time: 4:15:39 PM
 */
public class EmailFormatterImpl implements EmailFormatter
{
   public static final String SUBJECT = "subject";
   public static final String TEMPLATE = "template";
   public static final String TITLE = "title";
   public static final String HEADER = "header";
   public static final String FOOTER = "footer";
   private VelocityEngine velocityEngine;
   private HttpClient httpClient;

   public void setVelocityEngine(VelocityEngine velocityEngine)
   {
      this.velocityEngine = velocityEngine;
   }

   public void setHttpClient(HttpClient httpClient)
   {
      this.httpClient = httpClient;
   }

   public String formatEmailEntry(String header,
                                     String footer,
                                     String storyLabel,
                                     String taskLabel,
                                     List bodyEntryList) throws Exception
   {
      Template template = velocityEngine.getTemplate("com/technoetic/xplanner/mail/velocity/email_notifications.vm");
      VelocityContext velocityContext = new VelocityContext();
      XPlannerProperties properties = new XPlannerProperties();
      String applicationUrl = properties.getProperty(XPlannerProperties.APPLICATION_URL_KEY);
      String style = httpClient.getPage(applicationUrl + "/css/email.css");
      velocityContext.put("hostUrl", applicationUrl);
      velocityContext.put("style", style);
      velocityContext.put("header", header);
      velocityContext.put("footer", footer);
      velocityContext.put("taskLabel", taskLabel);
      velocityContext.put("storyLabel", storyLabel);
      velocityContext.put("bodyEntries", bodyEntryList);
      Writer stringWriter = new StringWriter();
      template.merge(velocityContext, stringWriter);
      stringWriter.close();
      return stringWriter.toString();
   }

	public String formatEmailEntry(List bodyEntryList, Map<String, Object> params) throws Exception {
      Template template = velocityEngine.getTemplate((String)params.get(TEMPLATE));
	  Map<String,Object> context = new HashMap<String, Object>();
	  ResourceBundle bundle = ResourceBundle.getBundle("EmailResourceBundle");
	  for(String paramName:params.keySet()){
			if(paramName.equals(TITLE)||paramName.equals(HEADER)||paramName.equals(FOOTER)){
				String paramKey = (String)params.get(paramName);
				context.put(paramName, bundle.getString(paramKey));
			}else{
				context.put(paramName, params.get(paramName));
			}
	  }
	  VelocityContext velocityContext = new VelocityContext(context);
      XPlannerProperties properties = new XPlannerProperties();
      String applicationUrl = properties.getProperty(XPlannerProperties.APPLICATION_URL_KEY);
      String style = httpClient.getPage(applicationUrl + "/css/email.css");
	  velocityContext.put("style", style);
      velocityContext.put("hostUrl", applicationUrl);
	  Writer stringWriter = new StringWriter();
      template.merge(velocityContext, stringWriter);
      stringWriter.close();
      return stringWriter.toString();
	}
}

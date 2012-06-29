package com.technoetic.xplanner.mail;

import java.util.List;
import java.util.Map;

public interface EmailFormatter
{

	String formatEmailEntry(List bodyEntryList, Map<String, Object> params) throws Exception;
//   void setVelocityEngine(VelocityEngine velocityEngine);
//
//   void setHttpClient(HttpClient httpClient);

   String formatEmailEntry(String header,
                           String footer,
                           String storyLabel,
                           String taskLabel,
                           List bodyEntryList) throws Exception;
}

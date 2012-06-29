package com.technoetic.xplanner.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient
{
   public String getPage(String urlString)
      throws IOException
   {
      URL url = new URL(urlString);
      URLConnection connection = url.openConnection();
      connection.connect();
      InputStream stream = connection.getInputStream();
      byte[] readBuffer = new byte[256];
      StringBuffer page = new StringBuffer();
      int pos = stream.read(readBuffer, 0, 255);
      while (pos > 0)
      {
         page.append(new String(readBuffer, 0, pos));
         pos = stream.read(readBuffer, 0, 255);
      }
      return page.toString();
   }
}

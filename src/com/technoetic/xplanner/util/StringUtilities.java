package com.technoetic.xplanner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.opensymphony.util.TextUtils;

public class StringUtilities {
   public static String computeSuffix(String description, String boundaryText, int maxSuffixLength) {
      int index = description.toLowerCase().indexOf(boundaryText.toLowerCase());
      String wholeSuffix = description.substring(index + boundaryText.length());
      String matchSuffix = getShortSuffix(wholeSuffix, maxSuffixLength);
      if (matchSuffix.length() < wholeSuffix.length()) {
         matchSuffix += "...";
      }
      return matchSuffix;
   }

   public static String computePrefix(String description, String boundaryText, int maxSuffixLength) {
      int index = description.toLowerCase().indexOf(boundaryText.toLowerCase());
      String wholePrefix = description.substring(0, index);
      String matchPrefix = getShortPrefix(wholePrefix, maxSuffixLength);
      if (matchPrefix.length() < wholePrefix.length()) {
         matchPrefix = "..." + matchPrefix;
      }
      return matchPrefix;
   }

   public static String replaceQuotationMarks(String text) {
      if (text == null) return "";
      String newText = text.replaceAll("\"", "''");
      newText = newText.replaceAll("'", "\\\\'");
      return newText;
   }

   public static String getShortSuffix(String wholeSuffix, int maxSuffixLength) {
      int count = 0;
      int pos = 0;
      while (pos < wholeSuffix.length() && count < maxSuffixLength) {
         count = adjustCountBasedOnChar(wholeSuffix.charAt(pos), count);
         pos++;
      }
      return wholeSuffix.substring(0, pos);
   }

   private static int adjustCountBasedOnChar(char currentChar, int count) {
      if (currentChar != ' ' && currentChar != '\n') {
         count++;
      }
      return count;
   }

   public static String getShortPrefix(String wholePrefix, int maxPrefixLength) {
      int pos = wholePrefix.length() - 1;
      int count = 0;
      while (pos >= 0 && count < maxPrefixLength) {
         count = adjustCountBasedOnChar(wholePrefix.charAt(pos), count);
         pos--;
      }
      return wholePrefix.substring(pos + 1);
   }

   public static String getFirstNLines(String text, int desiredLineCount) {
      BufferedReader bufReader = new BufferedReader(new StringReader(text));
      StringBuffer stringBuffer = new StringBuffer();
      int i = 1;
      try {
         String str = bufReader.readLine();
         while (str != null && i <= desiredLineCount) {
            stringBuffer.append(str);
            if (i < desiredLineCount) {
               stringBuffer.append(" ");
            }
            i++;
            str = bufReader.readLine();
         }
      } catch (IOException e) {
         //Don't care
      }
      return stringBuffer.toString();
   }

   public static String htmlEncode(String s) {
      if (!TextUtils.stringSet(s)) return "";

      StringBuffer str = new StringBuffer();

      for (int j = 0; j < s.length(); j++) {
         str.append(htmlEncode(s.charAt(j)));
      }

      return str.toString();
   }

   public static String htmlEncode(char c) {
         switch (c) {
            case '"': return "&quot;";
            case '&': return "&amp;";
            case '<': return "&lt;";
            case '>': return "&gt;";
            default: return ""+c;
         }
   }

}

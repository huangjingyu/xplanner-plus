/*
 * Copyright (c) 2005, Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;
/**
 * Created by IntelliJ IDEA.
 * User: sg620641
 * Date: Dec 7, 2005
 * Time: 12:25:53 PM
 * To change this template use File | Settings | File Templates.
 */

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

public class TestDataSampleData extends TestCase {
   private DataSampleData dataSampleData;

   protected void setUp() throws Exception {
      dataSampleData = new DataSampleData();
   }

   public void testGetLatestDate() throws Exception {
      Date today = new Date();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(today);
      calendar.add(Calendar.DATE, -1);
      Date yesterday = calendar.getTime();
      calendar.setTime(today);
      calendar.add(Calendar.DATE, 1);
      Date tomorrow = calendar.getTime();
      assertEquals(today, dataSampleData.getLatestDate(yesterday, today));
      assertEquals(tomorrow, dataSampleData.getLatestDate(yesterday, tomorrow));
      assertEquals(tomorrow, dataSampleData.getLatestDate(tomorrow, today));
      java.sql.Date sqlDateToday = new java.sql.Date(today.getTime());
      java.sql.Timestamp timestampTomorrow = new Timestamp(tomorrow.getTime());
      assertEquals(timestampTomorrow, dataSampleData.getLatestDate(sqlDateToday, timestampTomorrow));
   }
}
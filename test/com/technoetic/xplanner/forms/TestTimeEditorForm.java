package com.technoetic.xplanner.forms;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.struts.action.ActionErrors;

public class TestTimeEditorForm extends AbstractEditorFormTestCase {
   private TimeEditorForm timeEditorForm;
   public static final long SECONDS = 1000;
   public static final long MINUTES = 60 * SECONDS;
   public static final long HOURS = 60 * MINUTES;
   public static final long DAYS = 24 * HOURS;

   public static final long START_TIME = new GregorianCalendar(2002, 2, 2, 0, 0).getTimeInMillis();
   public static final long END_TIME = START_TIME + 1 * DAYS;
   public static final String PERSON1 = "100";
   public static final String PERSON2 = "111";

   protected void setUp() throws Exception {
      form = timeEditorForm = new TimeEditorForm();
      super.setUp();
      timeEditorForm.setRowcount(1);
      timeEditorForm.setEntryId(0, "1");
      timeEditorForm.setDeleted(0, null);
      timeEditorForm.setStartTime(0, formatDateTime(START_TIME));
      timeEditorForm.setEndTime(0, formatDateTime(END_TIME));
      timeEditorForm.setReportDate(0, formatDate(START_TIME));
      timeEditorForm.setPerson1Id(0, PERSON1);
      timeEditorForm.setPerson2Id(0, "200");
      timeEditorForm.setDescription(0, "the description");
   }

   public String formatDateTime(long date) {
      return DATE_TIME_FORMAT.format(new Date(date));
   }

   public String formatDate(long date) {
      return DATE_FORMAT.format(new Date(date));
   }

   public void testReset() {
      timeEditorForm.setDuration(0, "5");

      timeEditorForm.reset(support.mapping, support.request);

      assertEquals(0, timeEditorForm.getRowcount());
      assertNull(timeEditorForm.getEntryId(0));
      assertNull(timeEditorForm.getDeleted(0));
      assertNull(timeEditorForm.getStartTime(0));
      assertNull(timeEditorForm.getEndTime(0));
      assertNull(timeEditorForm.getPerson1Id(0));
      assertNull(timeEditorForm.getPerson2Id(0));
      assertNull(timeEditorForm.getDuration(0));
   }

   public void testDescription() {
      assertEquals("Wrong description", "the description", timeEditorForm.getDescription(0));
   }

   public void testValidateFormOk() {
      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   public void testParseableDuration() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "2,1");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   public void testValidateBadStartDate() {
      timeEditorForm.setStartTime(0, "bogus");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.UNPARSABLE_TIME_ERROR_KEY, errors);
   }

   public void testValidateBadEndDate() {
      timeEditorForm.setEndTime(0, "bogus");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.UNPARSABLE_TIME_ERROR_KEY, errors);
   }

   public void testValidateMissingEndDate() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setEndTime(0, null);

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_TIME_ERROR_KEY, errors);
   }

   public void testValidateNegativeInterval() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setEndTime(0, formatDateTime(START_TIME - 1 * DAYS));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.NEGATIVE_INTERVAL_ERROR_KEY, errors);
   }

   public void testValidateOverlappingInterval() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, PERSON1);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * HOURS));
      timeEditorForm.setEndTime(1, formatDateTime(END_TIME - 3 * HOURS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.OVERLAPPING_INTERVAL_ERROR_KEY, errors);
   }

   public void testValidateOverlappingIntervalWithDifferentDevelopers() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, PERSON2);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * HOURS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   public void testValidateOverlappingIntervalWithDuration() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, PERSON1);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME - 1 * DAYS));
      timeEditorForm.setDuration(1, "48");
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.OVERLAPPING_INTERVAL_ERROR_KEY, errors);
   }

   public void testValidateOverlappingIntervalWithTwoNonpairedEntries() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson2Id(0, "0");
      timeEditorForm.setPerson1Id(1, PERSON2);
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * DAYS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   private void assertNoErrors(ActionErrors errors) {assertEquals("wrong # of expected errors", 0, errors.size());}

   public void testOutOfOrderTimeInterval() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(1, "1");
      timeEditorForm.setStartTime(1, formatDateTime(START_TIME + 1 * HOURS));
      timeEditorForm.setReportDate(1, formatDate(START_TIME));

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   public void testValidateMissingPerson1() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(0, "");
      timeEditorForm.setPerson2Id(0, "");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_PERSON_ERROR_KEY, errors);
   }

   public void testValidateMissingPerson2() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "5");
      timeEditorForm.setPerson1Id(0, "");
      timeEditorForm.setPerson2Id(0, "");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_PERSON_ERROR_KEY, errors);
   }

   public void testValidateSamePeople() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setPerson1Id(0, PERSON1);
      timeEditorForm.setPerson2Id(0, PERSON1);

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.SAME_PEOPLE_ERROR_KEY, errors);
   }

   public void testValidateOnlyEndTimeAndDuration() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_TIME_ERROR_KEY, errors);

   }

   public void testValidateIntervalAndDuration2() {
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
      assertEquals(formatDateTime(START_TIME + 3 * HOURS), timeEditorForm.getEndTime(0));
   }

   public void testValidateIntervalAndDuration3() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_TIME_ERROR_KEY, errors);
   }

   public void testValidateIntervalAndDurationNotLastRow() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   public void testValidateIntervalAndDurationAtLastRow() {
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.BOTH_INTERVAL_AND_DURATION_ERROR_KEY, errors);
   }

   public void testValidateDuration() {
      timeEditorForm.setStartTime(0, null);
      timeEditorForm.setEndTime(0, null);
      timeEditorForm.setDuration(0, "3");

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertNoErrors(errors);
   }

   public void testValidateMissingReportDate() {
      timeEditorForm.setRowcount(2);
      timeEditorForm.setReportDate(0, null);

      ActionErrors errors = timeEditorForm.validate(support.mapping, support.request);

      assertOneError(TimeEditorForm.MISSING_REPORT_DATE_ERROR_KEY, errors);
   }
}

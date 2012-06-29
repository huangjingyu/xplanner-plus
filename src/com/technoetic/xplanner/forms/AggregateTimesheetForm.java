package com.technoetic.xplanner.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.virtual.Timesheet;

public class AggregateTimesheetForm
        extends AbstractEditorForm {

    private java.util.Date startDate;
    private String startDateString;
    private java.util.Date endDate;
    private String endDateString;
    private SimpleDateFormat dateFormat;
    private Timesheet timesheet;
    private Collection allPeople;
    private String[] selectedPeople;

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (this.dateFormat == null) {
            String format = getResources(request).getMessage("format.date");
            dateFormat = new SimpleDateFormat(format);
        }

        Date startDate = null;
        if (isPresent(this.startDateString)) {
            try {
                startDate = dateFormat.parse(this.startDateString);
                this.setStartDate(startDate);
            } catch (ParseException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("timesheet.error.unparsable_date"));
                request.setAttribute(Globals.ERROR_KEY, errors);
                return errors;
            }
        }

        Date endDate = null;
        if (isPresent(this.endDateString)) {
            try {
                endDate = dateFormat.parse(this.endDateString);
                this.setEndDate(endDate);
            } catch (ParseException ex) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("timesheet.error.unparsable_date"));
                request.setAttribute(Globals.ERROR_KEY, errors);
                return errors;
            }
        }

        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.endDate = this.getWeekEndDate();
        this.startDate = this.getWeekStartDate();
        this.timesheet = new Timesheet(this.startDate, this.endDate);
        this.allPeople = new ArrayList();
        this.selectedPeople = new String[]{};
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public String getStartDateString() {
        if (dateFormat == null) {
            return this.getStartDate().toString();
        }
        return dateFormat.format(this.getStartDate());
    }

    public void setStartDateString(String start) {
        this.startDateString = start;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndDateString() {
        if (dateFormat == null) {
            return this.getEndDate().toString();
        }
        return dateFormat.format(this.getEndDate());
    }

    public void setEndDateString(String end) {
        this.endDateString = end;
    }

    public Timesheet getTimesheet() {
        return timesheet;
    }

    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    private Date getWeekEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 7 - weekday);
        return cal.getTime();
    }

    private Date getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, -weekday + 1);
        return cal.getTime();
    }

    public void setDateFormat(SimpleDateFormat format) {
        this.dateFormat = format;
    }

    public SimpleDateFormat getDateFormat() {
        return this.dateFormat;
    }

    public Collection getAllPeople() {
        return allPeople;
    }

    public void setAllPeople(Collection allPeople) {
        this.allPeople = allPeople;
    }

    public String[] getSelectedPeople() {
        return selectedPeople;
    }

    public void setSelectedPeople(String[] selectedPeople) {
        this.selectedPeople = selectedPeople;
    }
}
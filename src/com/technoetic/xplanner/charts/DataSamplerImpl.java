/*
 * Copyright (c) 2005, Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.xplanner.dao.DataSampleDao;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * Created by IntelliJ IDEA. User: sg620641 Date: Dec 9, 2005 Time: 4:07:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSamplerImpl extends HibernateDaoSupport implements DataSampler {
	protected final Logger LOG = Logger.getLogger(DataSamplerImpl.class);
	protected TimeGenerator timeGenerator;
	private HibernateOperations hibernateOperations;
	public static final String AUTOMATICALLY_EXTEND_END_DATE_PROP = "iteration.automatically.extend.endDate";
	private Properties properties;
	private DataSampleDao dataSampleDao;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	private void generateDataSamples(Iteration iteration, Date date) {
		saveSamples(date, iteration);
		extendIterationEndDateIfNeeded(iteration, date);
	}

	public void generateDataSamples(Iteration iteration) {
		Date todayMidnight = timeGenerator.getTodaysMidnight();
		Date tomorrowMidnight = TimeGenerator.shiftDate(todayMidnight,
				Calendar.DATE, 1);
		generateDataSamples(iteration, tomorrowMidnight);
	}

	public void generateOpeningDataSamples(Iteration iteration) {
		Date date = timeGenerator.getTodaysMidnight();
		generateDataSamples(iteration, date);
	}

	public void generateClosingDataSamples(Iteration iteration) {
		if (iteration.getEndDate().before(timeGenerator.getCurrentTime())) {
			Date todayMidnight = timeGenerator.getTodaysMidnight();
			generateDataSamples(iteration, todayMidnight);
		} else {
			generateDataSamples(iteration);
		}
	}

	public void setTimeGenerator(TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	public HibernateOperations getHibernateOperations() {
		if (hibernateOperations != null) {
			return hibernateOperations;
		}
		return getHibernateTemplate();
	}

	public void setHibernateOperations(HibernateOperations hibernateOperations) {
		this.hibernateOperations = hibernateOperations;
	}

	protected void extendIterationEndDateIfNeeded(Iteration iteration,
			Date midnight) {
		boolean automaticallyExtendEndDate = Boolean.valueOf(
				properties.getProperty(AUTOMATICALLY_EXTEND_END_DATE_PROP,
						"false")).booleanValue();
		if (automaticallyExtendEndDate
				&& (IterationStatus.ACTIVE.toInt() == iteration.getStatus())
				&& iteration.getEndDate().compareTo(midnight) < 0) {
			LOG.debug("Extend iteration end day to " + midnight);
			iteration.setEndDate(midnight);
			getHibernateOperations().save(iteration);
		}
	}

	protected void saveSamples(Date date, Iteration iteration) {
		saveSample(date, iteration, "estimatedHours", iteration
				.getEstimatedHours());
		saveSample(date, iteration, "actualHours", iteration
				.getCachedActualHours());
		saveSample(date, iteration, "remainingHours", iteration
				.getTaskRemainingHours());
	}

	protected void saveSample(Date date, Iteration iteration, String aspect,
			double value) {
		DataSample sample;

		List<DataSample> samples = dataSampleDao.getDataSamples(date, iteration, aspect);
		
		if (!samples.isEmpty()) {
			sample = samples.get(0);
			sample.setValue(value);
			dataSampleDao.save(sample);
			LOG.debug("update existing datasample");
		} else {
			sample = new DataSample(date, iteration.getId(), aspect, value);
			dataSampleDao.save(sample);
			LOG.debug("Generated a new sample:" + sample);
		}
	}

	public void setDataSampleDao(DataSampleDao dataSampleDao) {
		this.dataSampleDao = dataSampleDao;
	}

	
}

package com.technoetic.xplanner.charts;

import java.io.Serializable;
import java.util.Date;

import net.sf.xplanner.domain.DomainObject;

public class DataSample2 extends DomainObject implements Serializable {
    private Date sampleTime;
    private int referenceId;
    private String aspect;
    private double value;

    // For Hibernate
    DataSample2() {
    }

    public DataSample2(Date sampleTime, int referenceId, String aspect, double value) {
        this.sampleTime = sampleTime;
        this.referenceId = referenceId;
        this.aspect = aspect;
        this.value = value;
    }

    public Date getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Date sampleTime) {
        this.sampleTime = sampleTime;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
	public String toString() {
        return aspect + " of oid " + referenceId + " on " + sampleTime + " was " + value;
    }

    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataSample2)) return false;

        final DataSample2 dataSample = (DataSample2)o;

        if (referenceId != dataSample.referenceId) return false;
        if (value != dataSample.value) return false;
        if (aspect != null ? !aspect.equals(dataSample.aspect) : dataSample.aspect != null) return false;
        if (sampleTime != null ? !sampleTime.equals(dataSample.sampleTime) : dataSample.sampleTime != null) return false;

        return true;
    }

    @Override
	public int hashCode() {
        int result;
        long temp;
        result = (sampleTime != null ? sampleTime.hashCode() : 0);
        result = 29 * result + referenceId;
        result = 29 * result + (aspect != null ? aspect.hashCode() : 0);
        temp = Double.doubleToLongBits(value);
        result = 29 * result + (int)(temp ^ (temp >>> 32));
        return result;
    }
}

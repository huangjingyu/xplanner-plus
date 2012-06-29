package net.sf.xplanner.domain;


import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.Identifiable;

/**
*    XplannerPlus, agile planning software
*    @author Maksym_Chyrkov. 
*    Copyright (C) 2009  Maksym Chyrkov
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
* 	 
*/

@Entity
@Table(name = "datasample")
public class DataSample implements java.io.Serializable, Identifiable {
	public static final String SAMPLE_TIME="id.sampleTime";
    public static final String REFERENCE_ID="id.referenceId";
    public static final String ASPECT="id.aspect";
	private static final long serialVersionUID = 7180535099586780565L;
	private double value;
	private DataSampleId dataSampleId = new DataSampleId();
	
	public DataSample() {
	}

	public DataSample(Date midnight, int id, String aspect, double value) {
		setSampleTime(midnight);
		setReferenceId(id);
		setAspect(aspect);
		this.value = value;
	}

	@Column(name = "\"value\"", precision = 22, scale = 0)
	public double getValue() {
		return this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "sampleTime", column = @Column(name = "sampleTime", nullable = false)),
			@AttributeOverride(name = "referenceId", column = @Column(name = "referenceId", nullable = false)),
			@AttributeOverride(name = "aspect", column = @Column(name = "aspect", nullable = false)) })
	public DataSampleId getDataSampleId() {
		return dataSampleId;
	}

	public void setDataSampleId(DataSampleId dataSampleId) {
		this.dataSampleId = dataSampleId;
	}

	@Transient
	public Date getSampleTime() {
		return dataSampleId.getSampleTime();
	}

	public void setSampleTime(Date sampleTime) {
		this.dataSampleId.setSampleTime(sampleTime);
	}

	@Transient
	public int getReferenceId() {
		return dataSampleId.getReferenceId();
	}

	public void setReferenceId(int referenceId) {
		dataSampleId.setReferenceId(referenceId);
	}

	@Transient
	public String getAspect() {
		return dataSampleId.getAspect();
	}

	public void setAspect(String aspect) {
		dataSampleId.setAspect(aspect);
	}

	@Transient
	public int getId() {
		return 0;
	}
}

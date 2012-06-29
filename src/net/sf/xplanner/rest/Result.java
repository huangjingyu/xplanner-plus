package net.sf.xplanner.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
public class Result {
	private boolean isError;
	private int code;
	private String decription;
	
	public Result() {
		//Default constructor
	}
	
	public Result(boolean isError, int code, String decription) {
		this.isError = isError;
		this.code = code;
		this.decription = decription;
	}
	
	public boolean isError() {
		return isError;
	}
	public void setError(boolean isError) {
		this.isError = isError;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
}

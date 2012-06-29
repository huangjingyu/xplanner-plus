package net.sf.xplanner.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum IterationType {
	REGULAR(0), BACKLOG(1);
	private static List<IterationType> ALL_VALUES = Arrays.asList(values());
	private int id;
	
	private IterationType(int id){
		this.id = id;
	}
	
	public int getCode(){
		return id;
	}
	
	public String getMessageCode(){
		return "iteration.type." + toString().toLowerCase();
	}
	
	public static List<IterationType> getAllValues(){
		return ALL_VALUES;
	}
}

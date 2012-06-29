package net.sf.xplanner.functions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class Functions {

	public static List<Object> filter(List<Object> objects, String fieldName, Object fieldValue) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		if(objects == null || objects.size()==0 || StringUtils.isBlank(fieldName)|| fieldValue ==null){
			return objects;
		}
		int intValue = 0;
		List<Object> result = new ArrayList<Object>(objects.size());
		
		if(fieldValue instanceof Long){
			intValue = ((Long)fieldValue).intValue();
		}
		
		for (Object object : objects) {
			Object property = PropertyUtils.getProperty(object, fieldName);
			if(property instanceof Enum){
				if(fieldValue.equals(((Enum)property).name())){
					result.add(object);
				}
			}else if(fieldValue.equals(property)){
				result.add(object);
			}else if((fieldValue instanceof Long) && property.equals(intValue)){
				result.add(object);
			}
		}
		return result;
	}
}

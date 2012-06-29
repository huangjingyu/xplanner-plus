package net.sf.xplanner.hibernate;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;

public class ObjectSetter implements Setter{
	private static final long serialVersionUID = -1836514640217855044L;
	private final Getter getter;
	private final String property;
	private final boolean isNew;
	private Class domainClass;
	private Setter setter;

	public ObjectSetter(Getter getter, String property, boolean isNew) {
		this.getter = getter;
		domainClass = (Class) ((ParameterizedType) getter.getMethod().getGenericReturnType()).getActualTypeArguments()[0];
		ChainedPropertyAccessor propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] {
						PropertyAccessorFactory.getPropertyAccessor( domainClass, null ),
						PropertyAccessorFactory.getPropertyAccessor( "field" )
				}
		);
		setter = propertyAccessor.getSetter(domainClass, property);
		this.property = property;
		this.isNew = isNew;
		
	}

	@Override
	public Method getMethod() {
		return null;
	}

	@Override
	public String getMethodName() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Object target, Object value,
			SessionFactoryImplementor factory) throws HibernateException {
		Object object = getter.get(target);
		if(object instanceof List<?>) {
			List list = (List)object;
			if(isNew) {
				try {
					Object newInstance = domainClass.newInstance();
					setter.set(newInstance, value, factory);
					list.add(newInstance);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}else {
				Object object2 = list.get(list.size()-1);
				setter.set(object2, value, factory);
			}
		}
		
		
	}

}

package com.technoetic.xplanner.domain;

import java.lang.reflect.InvocationTargetException;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.DomainObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

//DEBT(METADATA) Rename this class to illustrate its responsability

/**
 * @resp Convert a DTO (Form, SOAP data object) attribute to a Domain object relationship and vise-a-versa
 */
public class RelationshipConvertor {
    private final String adapterProperty;
    private final String domainProperty;

    public RelationshipConvertor(String adapterProperty, String domainObjectProperty) {
        this.adapterProperty = adapterProperty;
        this.domainProperty = domainObjectProperty;
    }

    public String getAdapterProperty() {
        return adapterProperty;
    }

    public String getDomainProperty() {
        return domainProperty;
    }

    public void populateDomainObject(DomainObject destination, Object source, CommonDao<?> commonDao) throws HibernateException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException {
		if (PropertyUtils.isReadable(source, adapterProperty) && PropertyUtils.isWriteable(destination, domainProperty)) {
			Integer referredId = (Integer) PropertyUtils.getProperty(source, adapterProperty);
			Class destinationType = PropertyUtils.getPropertyType(destination, domainProperty);
			Object referred = findObjectById(commonDao, destinationType, referredId);
			PropertyUtils.setProperty(destination, domainProperty, referred);
		}
    }

    private Object findObjectById(CommonDao<?> commonDao, Class aClass, Integer id) throws HibernateException {
        if (id.intValue() == 0) return null;
        return commonDao.getById(aClass, id);
    }

    public void populateAdapter(Object adapter, DomainObject domainObject) throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {
        Object referred = PropertyUtils.getProperty(domainObject, domainProperty);
        Integer id = referred == null ?
                new Integer(0) : (Integer)PropertyUtils.getProperty(referred, "id");
        PropertyUtils.setProperty(adapter, adapterProperty, id);
    }

}
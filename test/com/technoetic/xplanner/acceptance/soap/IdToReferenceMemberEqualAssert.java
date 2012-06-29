package com.technoetic.xplanner.acceptance.soap;

import java.lang.reflect.InvocationTargetException;

import junitx.framework.Assert;
import junitx.framework.MemberAccessStrategy;
import junitx.framework.MemberEqualAssert;

import org.apache.commons.beanutils.PropertyUtils;

public class IdToReferenceMemberEqualAssert implements MemberEqualAssert {
    public boolean assertEquals(String propertyName,
                                Object expectedObject,
                                Object actualObject,
                                MemberAccessStrategy accessStrategy) {
        if (!propertyName.endsWith("Id")) return false;
        String nestedIdPropertyName = propertyName.substring(0, propertyName.length() - 2)+".id";
        try {
            Property expected = getProperty(expectedObject, propertyName, nestedIdPropertyName);
            Property actual = getProperty(actualObject, propertyName, nestedIdPropertyName);
            Assert.assertEquals(expected.name + "!="+actual.name, expected.value, actual.value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    private Property getProperty(Object object, String directIdPropertyName, String nestedIdPropertyName)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        try {
            return new Property(directIdPropertyName, PropertyUtils.getProperty(object, directIdPropertyName));
        } catch (Exception e) {
            return new Property(nestedIdPropertyName, PropertyUtils.getProperty(object, nestedIdPropertyName));
        }
    }

    class Property {
        public String name;
        public Object value;

        public Property(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

}
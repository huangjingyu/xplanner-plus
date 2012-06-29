/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.hsqldb.types.Types;

public abstract class CharacterEnumType implements UserType {
   private static final int[] SQL_TYPES = {Types.CHAR};

   public int[] sqlTypes() {
      return SQL_TYPES;
   }

   public abstract Class returnedClass();

   public boolean equals(Object x, Object y) throws HibernateException {
      return x == y;
   }

   public Object deepCopy(Object value) throws HibernateException {
      return value;
   }

   public boolean isMutable() {
      return false;
   }

   public Object nullSafeGet(ResultSet resultSet,
                             String[] names,
                             Object owner)
           throws HibernateException, SQLException {

     String name = resultSet.getString(names[0]);
     return resultSet.wasNull() ? null : getType(name);
   }

   protected abstract CharacterEnum getType(String code);

   public void nullSafeSet(PreparedStatement statement, Object value, int index)
         throws HibernateException, SQLException {
       if (value == null) {
           statement.setNull(index, Types.CHAR);
       } else {
           statement.setString(index, convert(value));
       }
   }

   private String convert(Object value)
   {
      CharacterEnum characterEnum = (CharacterEnum) value;

      return String.copyValueOf(new char[] {characterEnum.getCode()} );
   }

/* (non-Javadoc)
 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
 */
@Override
public Object assemble(Serializable serializable, Object obj)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
 */
@Override
public Serializable disassemble(Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
 */
@Override
public int hashCode(Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	return 0;
}

/* (non-Javadoc)
 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
 */
@Override
public Object replace(Object obj, Object obj1, Object obj2)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

}

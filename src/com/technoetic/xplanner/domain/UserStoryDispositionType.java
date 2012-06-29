package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

//FIXME: It's stub. Need rewriting.
//TODO delete this class and replace it with Disposition
public class UserStoryDispositionType implements UserType{
    private final int code;

    public static final String PLANNED_TEXT = "Planned";
    public static final String CARRIED_OVER_TEXT = "Carried Over";
    public static final String ADDED_TEXT = "Added";

    public static final String PLANNED_KEY = "planned";
    public static final String CARRIED_OVER_KEY = "carriedOver";
    public static final String ADDED_KEY = "added";

    public static final UserStoryDispositionType PLANNED = new UserStoryDispositionType(0);
    public static final UserStoryDispositionType CARRIED_OVER = new UserStoryDispositionType(1);
    public static final UserStoryDispositionType ADDED = new UserStoryDispositionType(2);

    public static final String[] KEYS = {PLANNED_KEY, CARRIED_OVER_KEY, ADDED_KEY};

    private UserStoryDispositionType (int code){
        this.code = code;
    }

    public int toInt() {
        return code;
    }

    public String getKey (){
        return KEYS[code];
    }

    public static UserStoryDispositionType fromKey(String key) {
        //TODO should be handled by iterating through the keys
        if (key == null)
            return null;
        else if (PLANNED_KEY.equals(key))
            return PLANNED;
        else if (CARRIED_OVER_KEY.equals(key))
            return CARRIED_OVER;
        else if (ADDED_KEY.equals(key))
            return ADDED;
        else
            throw new RuntimeException ("Unknown disposition key");
    }

    public static UserStoryDispositionType fromInt(int i){
       switch (i) {
           case 0: return PLANNED;
           case 1: return CARRIED_OVER;
           case 2: return ADDED;
           default: throw new RuntimeException ("Unknown disposition code");
       }
    }

    @Override
	public String toString() {
        return getKey();
    }

    public String getName() {
        return  getKey();
    }

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMutable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class returnedClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] sqlTypes() {
		// TODO Auto-generated method stub
		return null;
	}
}

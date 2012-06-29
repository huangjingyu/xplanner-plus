package com.technoetic.xplanner.db.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

public class XPlannerInterceptor implements Interceptor {
    private static String LAST_UPDATE_TIME = "lastUpdateTime";

    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return false;
    }

    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return setLastUpdateTime(propertyNames, currentState);
    }

    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return setLastUpdateTime(propertyNames, state);
    }

    private boolean setLastUpdateTime(String[] propertyNames, Object[] state) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (LAST_UPDATE_TIME.equals(propertyNames[i])) {
                state[i] = new Date();
                return true;
            }
        }
        return false;
    }

    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        // empty
    }

    public void preFlush(Iterator entities) {
        // empty
    }

    public void postFlush(Iterator entities) {
        // empty
    }

    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types) {
        return null;
    }

    public Object instantiate(Class clazz, Serializable id) throws CallbackException {
        return null;
    }

    public Boolean isUnsaved(Object entity) {
        return null;
    }

	@Override
	public void afterTransactionBegin(Transaction tx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getEntity(String entityName, Serializable id)
			throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName(Object object) throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object instantiate(String entityName, EntityMode entityMode,
			Serializable id) throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isTransient(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key)
			throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key)
			throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key)
			throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String onPrepareStatement(String sql) {
		// TODO Auto-generated method stub
		return sql;
	}

}

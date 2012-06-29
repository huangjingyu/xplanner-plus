package com.technoetic.mocks.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Set;

import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.TypeHelper;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.exception.SQLExceptionConverter;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;
import org.springframework.validation.DataBinder;

public class MockSessionFactory implements SessionFactory {

    public boolean openSessionCalled;
    public Session openSessionReturn;

    public Session openSession() {
        openSessionCalled = true;
        return openSessionReturn;
    }

    public boolean openSession2Called;
    public java.sql.Connection openSession2Connection;
    public Session openSession2Return;

    public Session openSession(java.sql.Connection connection) {
        openSession2Called = true;
        openSession2Connection = connection;
        return openSession2Return;
    }

    public boolean openSession3Called;
    public Session openSession3Return;
    public Interceptor openSession3Interceptor;

    public Session openSession(Interceptor interceptor) {
        openSession3Called = true;
        openSession3Interceptor = interceptor;
        return openSession3Return;
    }

    public boolean openSession4Called;
    public Session openSession4Return;
    public Interceptor openSession4Interceptor;
    public java.sql.Connection openSession4Connection;

    public Session openSession(java.sql.Connection connection, Interceptor interceptor) {
        openSession4Called = true;
        openSession4Connection = connection;
        openSession4Interceptor = interceptor;
        return openSession4Return;
    }


    public boolean openDatabinderCalled;
    public DataBinder openDatabinderReturn;
    public HibernateException openDatabinderHibernateException;

    public DataBinder openDatabinder() throws HibernateException {
        openDatabinderCalled = true;
        if (openDatabinderHibernateException != null) {
            throw openDatabinderHibernateException;
        }
        return openDatabinderReturn;
    }

    public boolean getClassMetadataCalled;
    public ClassMetadata getClassMetadataReturn;
    public HibernateException getClassMetadataHibernateException;
    public java.lang.Class getClassMetadataPersistentClass;

    public ClassMetadata getClassMetadata(java.lang.Class persistentClass) throws HibernateException {
        getClassMetadataCalled = true;
        getClassMetadataPersistentClass = persistentClass;
        if (getClassMetadataHibernateException != null) {
            throw getClassMetadataHibernateException;
        }
        return getClassMetadataReturn;
    }

    public boolean getCollectionMetadataCalled;
    public CollectionMetadata getCollectionMetadataReturn;
    public HibernateException getCollectionMetadataHibernateException;
    public java.lang.String getCollectionMetadataRoleName;

    public CollectionMetadata getCollectionMetadata(java.lang.String roleName) throws HibernateException {
        getCollectionMetadataCalled = true;
        getCollectionMetadataRoleName = roleName;
        if (getCollectionMetadataHibernateException != null) {
            throw getCollectionMetadataHibernateException;
        }
        return getCollectionMetadataReturn;
    }

    public boolean getAllClassMetadataCalled;
    public java.util.Map getAllClassMetadataReturn;
    public HibernateException getAllClassMetadataHibernateException;

    public java.util.Map getAllClassMetadata() throws HibernateException {
        getAllClassMetadataCalled = true;
        if (getAllClassMetadataHibernateException != null) {
            throw getAllClassMetadataHibernateException;
        }
        return getAllClassMetadataReturn;
    }

    public boolean getAllCollectionMetadataCalled;
    public java.util.Map getAllCollectionMetadataReturn;
    public HibernateException getAllCollectionMetadataHibernateException;

    public java.util.Map getAllCollectionMetadata() throws HibernateException {
        getAllCollectionMetadataCalled = true;
        if (getAllCollectionMetadataHibernateException != null) {
            throw getAllCollectionMetadataHibernateException;
        }
        return getAllCollectionMetadataReturn;
    }

    public boolean getReferenceCalled;
    public javax.naming.Reference getReferenceReturn;
    public javax.naming.NamingException getReferenceNamingException;

    public javax.naming.Reference getReference() throws javax.naming.NamingException {
        getReferenceCalled = true;
        if (getReferenceNamingException != null) {
            throw getReferenceNamingException;
        }
        return getReferenceReturn;
    }

    public void close() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evict(Class persistentClass) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evict(Class persistentClass, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictCollection(String roleName) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictQueries() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    public void evictQueries(String cacheRegion) throws HibernateException {
        throw new UnsupportedOperationException();
    }

  public SQLExceptionConverter getSQLExceptionConverter() {
    return null;
  }

@Override
public void evictEntity(String entityName) throws HibernateException {
	// TODO Auto-generated method stub
	
}

@Override
public void evictEntity(String entityName, Serializable id)
		throws HibernateException {
	// TODO Auto-generated method stub
	
}

@Override
public ClassMetadata getClassMetadata(String entityName)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Session getCurrentSession() throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Set getDefinedFilterNames() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public FilterDefinition getFilterDefinition(String filterName)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Statistics getStatistics() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean isClosed() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public StatelessSession openStatelessSession() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public StatelessSession openStatelessSession(Connection connection) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Cache getCache() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean containsFetchProfileDefinition(String name) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public TypeHelper getTypeHelper() {
	// TODO Auto-generated method stub
	return null;
}
}

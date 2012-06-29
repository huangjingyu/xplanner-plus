package com.technoetic.mocks.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.*;
import org.hibernate.classic.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

public class MockSession implements Session {
   public boolean flushCalled;
   public HibernateException flushHibernateException;

   public void flush() throws HibernateException {
      flushCalled = true;
      if (flushHibernateException != null) {
         throw flushHibernateException;
      }
   }

   public boolean connectionCalled;
   public Connection connectionReturn;
   public HibernateException connectionHibernateException;

   public Connection connection() throws HibernateException {
      connectionCalled = true;
      if (connectionHibernateException != null) {
         throw connectionHibernateException;
      }
      return connectionReturn;
   }

   public boolean disconnectCalled;
   public Connection disconnectReturn;
   public HibernateException disconnectHibernateException;

   public Connection disconnect() throws HibernateException {
      disconnectCalled = true;
      if (disconnectHibernateException != null) {
         throw disconnectHibernateException;
      }
      return disconnectReturn;
   }

   public boolean reconnectCalled;
   public HibernateException reconnectHibernateException;

   public void reconnect() throws HibernateException {
      reconnectCalled = true;
      if (reconnectHibernateException != null) {
         throw reconnectHibernateException;
      }
   }

   public boolean reconnect2Called;
   public HibernateException reconnect2HibernateException;
   public Connection reconnect2Connection;

   public void reconnect(Connection connection) throws HibernateException {
      reconnect2Called = true;
      reconnect2Connection = connection;
      if (reconnect2HibernateException != null) {
         throw reconnect2HibernateException;
      }
   }

   public boolean closeCalled;
   public Connection closeReturn;
   public HibernateException closeHibernateException;

   public Connection close() throws HibernateException {
      closeCalled = true;
      if (closeHibernateException != null) {
         throw closeHibernateException;
      }
      return closeReturn;
   }

   public boolean isOpenCalled;
   public Boolean isOpenReturn;

   public boolean isOpen() {
      isOpenCalled = true;
      return isOpenReturn.booleanValue();
   }

   public boolean isConnectedCalled;
   public Boolean isConnectedReturn;

   public boolean isConnected() {
      isConnectedCalled = true;
      return isConnectedReturn.booleanValue();
   }

   public boolean isDirty() throws HibernateException {
      return false;
   }

   public boolean getIdentifierCalled;
   public Serializable getIdentifierReturn;
   public HibernateException getIdentifierHibernateException;
   public Object getIdentifierObject;

   public Serializable getIdentifier(Object object) throws HibernateException {
      getIdentifierCalled = true;
      getIdentifierObject = object;
      if (getIdentifierHibernateException != null) {
         throw getIdentifierHibernateException;
      }
      return getIdentifierReturn;
   }

   public boolean loadCalled;
   public Object loadReturn;
   public HashMap loadReturnMap = new HashMap();
   public HibernateException loadHibernateException;
   public Class loadTheClass;
   public Serializable loadId;

   public Object load(Class theClass, Serializable id) throws HibernateException {
      loadCalled = true;
      loadTheClass = theClass;
      loadId = id;
      if (loadHibernateException != null) {
         throw loadHibernateException;
      }
      if (loadReturn != null) {
         return loadReturn;
      } else {
         return getLoadReturn(theClass, id);
      }
   }

   private Object getLoadReturn(Class theClass, Serializable id) {
      Object o = loadReturnMap.get(theClass);
      if (o instanceof HashMap) {
         return ((HashMap) o).get(id);
      }
      return o;
   }

   public void loadAddReturnByClassById(int id, Object o) {
      loadAddReturnByClassById(new Integer(id), o);
   }

   private void loadAddReturnByClassById(Serializable id, Object o) {
      if (loadReturnMap == null) {
         loadReturn = new HashMap();
      }
      Object objectForClass = loadReturnMap.get(o.getClass());
      if (objectForClass == null) {
         objectForClass = new HashMap();
         loadReturnMap.put(o.getClass(), objectForClass);
      }
      if (!(objectForClass instanceof HashMap)) {
         throw new AssertionFailedError(
               "loadReturn already contains a map of single object per class. Cannot add multiple object of same class");
      }
      ((HashMap) objectForClass).put(id, o);

   }

   public boolean load2Called;
   public Object load2Return;
   public HibernateException load2HibernateException;
   public Class load2TheClass;
   public Serializable load2Id;
   public LockMode load2LockMode;

   public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
      load2Called = true;
      load2TheClass = theClass;
      load2Id = id;
      load2LockMode = lockMode;
      if (load2HibernateException != null) {
         throw load2HibernateException;
      }
      return load2Return;
   }

   public boolean load3Called;
   public Object load3Return;
   public HibernateException load3HibernateException;
   public Serializable load3Id;
   public Object load3Object;

   public void load(Object object, Serializable id) throws HibernateException {
      load3Called = true;
      load3Object = object;
      load3Id = id;
      if (load3HibernateException != null) {
         throw load3HibernateException;
      }
   }

   public Object get(Class clazz, Serializable id) throws HibernateException {
      return load(clazz, id);
   }

   public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
      return load(clazz, id, lockMode);
   }

   public boolean saveCalled;
   public int saveCalledCount;
   public Serializable saveReturn;
   public HibernateException saveHibernateException;
   public Object saveObject;
   public ArrayList saveObjects = new ArrayList();
   public Object[] saveIds;
   public String saveIdProperty;

   public Serializable save(Object object) throws HibernateException {
      saveCalled = true;
      saveCalledCount++;
      saveObject = object;
      saveObjects.add(object);
      if (saveHibernateException != null) {
         throw saveHibernateException;
      }
      if (saveIdProperty != null) {
         try {
            saveReturn = (Integer) saveIds[saveCalledCount - 1];
            PropertyUtils.setProperty(object, saveIdProperty, saveReturn);
         } catch (Exception e) {
            throw new HibernateException(e);
         }
      }
      return saveReturn;
   }

   public boolean save2Called;
   public Serializable save2Return;
   public HibernateException save2HibernateException;
   public Object save2Object;
   public Serializable save2Id;

   public void save(Object object, Serializable id) throws HibernateException {
      save2Called = true;
      save2Object = object;
      save2Id = id;
      if (save2HibernateException != null) {
         throw save2HibernateException;
      }
   }

   public boolean saveOrUpdateCalled;
   public HibernateException saveOrUpdateHibernateException;
   public Object saveOrUpdateObject;

   public void saveOrUpdate(Object object) throws HibernateException {
      saveOrUpdateCalled = true;
      saveOrUpdateObject = object;
      if (saveOrUpdateHibernateException != null) {
         throw saveOrUpdateHibernateException;
      }
   }

   public boolean updateCalled;
   public HibernateException updateHibernateException;
   public Object updateObject;

   public void update(Object object) throws HibernateException {
      updateCalled = true;
      updateObject = object;
      if (updateHibernateException != null) {
         throw updateHibernateException;
      }
   }


   public boolean update2Called;
   public HibernateException update2HibernateException;
   public Object update2Object;
   public Serializable update2Id;

   public void update(Object object, Serializable id) throws HibernateException {
      update2Called = true;
      update2Object = object;
      update2Id = id;
      if (update2HibernateException != null) {
         throw update2HibernateException;
      }
   }

   public boolean deleteCalled;
   public HibernateException deleteHibernateException;
   public Object deleteObject;

   public void delete(Object object) throws HibernateException {
      deleteCalled = true;
      deleteObject = object;
      if (deleteHibernateException != null) {
         throw deleteHibernateException;
      }
   }

   public boolean findCalled;
   public List findReturn;
   public HibernateException findHibernateException;
   public String findQuery;

   public List find(String query) throws HibernateException {
      findCalled = true;
      findQuery = query;
      if (findHibernateException != null) {
         throw findHibernateException;
      }
      return findReturn;
   }

   public boolean find2Called;
   public List find2Return;
   public int find2CalledCount;
   public List find2Returns; // list of lists
   public boolean find2ReturnEmptyLists;
   public HibernateException find2HibernateException;
   public String find2Query;
   public Object find2Value;
   public Type find2Type;

   public List find(String query, Object value, Type type) throws HibernateException {
      find2Called = true;
      find2CalledCount++;
      find2Query = query;
      find2Value = value;
      find2Type = type;
      if (find2HibernateException != null) {
         throw find2HibernateException;
      }
      if (find2ReturnEmptyLists && (find2Returns == null || find2CalledCount > find2Returns.size())) {
         return Collections.EMPTY_LIST;
      }
      if (find2Returns != null) {
         return (List) find2Returns.get(find2CalledCount - 1);
      } else {
         return find2Return;
      }
   }

   public boolean find3Called;
   public List find3Return;
   public int find3CalledCount;
   public List find3Returns; // list of lists
   public boolean find3ReturnEmptyLists;
   public HibernateException find3HibernateException;
   public String find3Query;
   public Object[] find3Values;
   public Type[] find3Types;

   public List find(String query, Object[] values, Type[] types) throws HibernateException {
      find3Called = true;
      find3CalledCount++;
      find3Query = query;
      find3Values = values;
      find3Types = types;
      if (find3HibernateException != null) {
         throw findHibernateException;
      }
      if (find3ReturnEmptyLists && (find3Returns == null || find3CalledCount >= find3Returns.size())) {
         return Collections.EMPTY_LIST;
      }
      if (find3Returns != null) {
         return (List) find3Returns.get(find3CalledCount - 1);
      } else {
         return find3Return;
      }
   }

   public boolean iterateCalled;
   public Iterator iterateReturn;
   public HibernateException iterateHibernateException;
   public String iterateQuery;

   public Iterator iterate(String query) throws HibernateException {
      iterateCalled = true;
      iterateQuery = query;
      if (iterateHibernateException != null) {
         throw iterateHibernateException;
      }
      return iterateReturn;
   }

   public boolean iterate2Called;
   public Iterator iterate2Return;
   public HibernateException iterate2HibernateException;
   public Object iterate2Value;
   public Type iterate2Type;
   public String iterate2Query;

   public Iterator iterate(String query, Object value, Type type) throws HibernateException {
      iterate2Called = true;
      iterate2Query = query;
      iterate2Value = value;
      iterate2Type = type;
      if (iterateHibernateException != null) {
         throw iterate2HibernateException;
      }
      return iterate2Return;
   }


   public boolean iterate3Called;
   public Iterator iterate3Return;
   public HibernateException iterate3HibernateException;
   public Object[] iterate3Values;
   public Type[] iterate3Types;
   public String iterate3Query;

   public Iterator iterate(String query, Object[] values, Type[] types) throws HibernateException {
      iterate3Called = true;
      iterate3Query = query;
      iterate3Values = values;
      iterate3Types = types;
      if (iterate3HibernateException != null) {
         throw iterate3HibernateException;
      }
      return iterate3Return;
   }

   public boolean filterCalled;
   public Collection filterReturn;
   public HibernateException filterHibernateException;
   public Object filterCollection;
   public String filterFilter;

   public Collection filter(Object collection, String filter) throws HibernateException {
      filterCalled = true;
      filterCollection = collection;
      filterFilter = filter;
      if (filterHibernateException != null) {
         throw filterHibernateException;
      }
      return filterReturn;
   }


   public boolean filter2Called;
   public Collection filter2Return;
   public HibernateException filter2HibernateException;
   public Object filter2Collection;
   public String filter2Filter;
   public Object filter2Value;
   public Type filter2Type;

   public Collection filter(Object collection, String filter, Object value, Type type)
         throws HibernateException {
      filter2Called = true;
      filter2Collection = collection;
      filter2Filter = filter;
      filter2Value = value;
      filter2Type = type;
      if (filter2HibernateException != null) {
         throw filterHibernateException;
      }
      return filter2Return;
   }

   public boolean filter3Called;
   public Collection filter3Return;
   public HibernateException filter3HibernateException;
   public Object filter3Collection;
   public String filter3Filter;
   public Object[] filter3Values;
   public Type[] filter3Types;

   public Collection filter(Object collection, String filter, Object[] values, Type[] types)
         throws HibernateException {
      filter3Called = true;
      filter3Collection = collection;
      filter3Filter = filter;
      filter3Values = values;
      filter3Types = types;
      if (filterHibernateException != null) {
         throw filter3HibernateException;
      }
      return filter3Return;
   }

   public boolean delete2Called;
   public Integer delete2Return;
   public HibernateException delete2HibernateException;
   public String delete2Query;

   public int delete(String query) throws HibernateException {
      delete2Called = true;
      delete2Query = query;
      if (delete2HibernateException != null) {
         throw delete2HibernateException;
      }
      return delete2Return.intValue();
   }

   public boolean delete3Called;
   public Integer delete3Return;
   public HibernateException delete3HibernateException;
   public String delete3Query;
   public Object delete3Value;
   public Type delete3Type;

   public int delete(String query, Object value, Type type) throws HibernateException {
      delete3Called = true;
      delete3Query = query;
      delete3Value = value;
      delete3Type = type;
      if (delete3HibernateException != null) {
         throw delete3HibernateException;
      }
      return delete3Return.intValue();
   }


   public int delete4CalledCount;
   public boolean delete4Called;
   public Integer delete4Return;
   public HibernateException delete4HibernateException;
   public String delete4Query;
   public Object[] delete4Values;
   public Type[] delete4Types;

   public int delete(String query, Object[] values, Type[] types) throws HibernateException {
      delete4CalledCount++;
      delete4Called = true;
      delete4Query = query;
      delete4Values = values;
      delete4Types = types;
      if (delete4HibernateException != null) {
         throw delete4HibernateException;
      }
      return delete4Return.intValue();
   }

   public boolean lockCalled;
   public HibernateException lockHibernateException;
   public Object lockObject;
   public LockMode lockLockMode;

   public void lock(Object object, LockMode lockMode) throws HibernateException {
      lockCalled = true;
      lockObject = object;
      lockLockMode = lockMode;
      if (lockHibernateException != null) {
         throw lockHibernateException;
      }
   }

   public boolean getCurrentLockModeCalled;
   public LockMode getCurrentLockModeReturn;
   public HibernateException getCurrentLockModeHibernateException;
   public Object getCurrentLockModeObject;

   public LockMode getCurrentLockMode(Object object) throws HibernateException {
      getCurrentLockModeCalled = true;
      getCurrentLockModeObject = object;
      if (getCurrentLockModeHibernateException != null) {
         throw getCurrentLockModeHibernateException;
      }
      return getCurrentLockModeReturn;
   }

   public boolean beginTransactionCalled;
   public Transaction beginTransactionReturn;
   public HibernateException beginTransactionHibernateException;

   public Transaction beginTransaction() throws HibernateException {
      beginTransactionCalled = true;
      if (beginTransactionHibernateException != null) {
         throw beginTransactionHibernateException;
      }
      return beginTransactionReturn;
   }

   public boolean suspendFlushesCalled;

   public void suspendFlushes() {
      suspendFlushesCalled = true;
   }

   public boolean resumeFlushesCalled;

   public void resumeFlushes() {
      resumeFlushesCalled = true;
   }

   public boolean createQueryCalled;
   public Query createQueryReturn;
   public HibernateException createQueryHibernateException;
   public String createQueryQueryString;

   public Query createQuery(String queryString) throws HibernateException {
      createQueryCalled = true;
      createQueryQueryString = queryString;
      if (createQueryHibernateException != null) {
         throw createQueryHibernateException;
      }
      return createQueryReturn;
   }

   public boolean createFilterCalled;
   public Query createFilterReturn;
   public HibernateException createFilterHibernateException;
   public Object createFilterCollection;
   public String createFilterQueryString;

   public Query createFilter(Object collection, String queryString) throws HibernateException {
      createFilterCalled = true;
      createFilterCollection = collection;
      createFilterQueryString = queryString;
      if (createFilterHibernateException != null) {
         throw createFilterHibernateException;
      }
      return createFilterReturn;
   }

   public boolean getNamedQueryCalled;
   public Query getNamedQueryReturn;
   public HashMap getNamedQueryReturnMap = new HashMap();
   public HibernateException getNamedQueryHibernateException;
   public String getNamedQueryQueryName;

   public Query getNamedQuery(String queryName) throws HibernateException {
      getNamedQueryCalled = true;
      getNamedQueryQueryName = queryName;
      if (getNamedQueryHibernateException != null) {
         throw getNamedQueryHibernateException;
      }
      if (getNamedQueryReturn != null) return getNamedQueryReturn;

      return (Query) getNamedQueryReturnMap.get(queryName);
   }

   public boolean findIdentifiersCalled;
   public List findIdentifiersReturn;
   public HibernateException findIdentifiersHibernateException;
   public String findIdentifiersQuery;

   public List findIdentifiers(String query) throws HibernateException {
      findIdentifiersCalled = true;
      findIdentifiersQuery = query;
      if (findIdentifiersHibernateException != null) {
         throw findIdentifiersHibernateException;
      }
      return findIdentifiersReturn;
   }

   public boolean findIdentifiers2Called;
   public List findIdentifiers2Return;
   public HibernateException findIdentifiers2HibernateException;
   public String findIdentifiers2Query;
   public Object findIdentifiers2Value;
   public Type findIdentifiers2Type;

   public List findIdentifiers(String query, Object value, Type type) throws HibernateException {
      findIdentifiers2Called = true;
      findIdentifiers2Query = query;
      findIdentifiers2Value = value;
      findIdentifiers2Type = type;
      if (findIdentifiers2HibernateException != null) {
         throw findIdentifiers2HibernateException;
      }
      return findIdentifiers2Return;
   }

   public boolean findIdentifiers3Called;
   public List findIdentifiers3Return;
   public HibernateException findIdentifiers3HibernateException;
   public String findIdentifiers3Query;
   public Object[] findIdentifiers3Values;
   public Type[] findIdentifiers3Types;

   public List findIdentifiers(String query, Object[] values, Type[] types) throws HibernateException {
      findIdentifiers3Called = true;
      findIdentifiers3Query = query;
      findIdentifiers3Values = values;
      findIdentifiers3Types = types;
      if (findIdentifiers3HibernateException != null) {
         throw findIdentifiers3HibernateException;
      }
      return findIdentifiers3Return;
   }

   public void setFlushMode(FlushMode flushMode) {
      throw new UnsupportedOperationException();
   }

   public FlushMode getFlushMode() {
      throw new UnsupportedOperationException();
   }

   public SessionFactory getSessionFactory() {
      throw new UnsupportedOperationException();
   }

   public void cancelQuery() throws HibernateException {
      throw new UnsupportedOperationException();
   }

   public boolean contains(Object object) {
      throw new UnsupportedOperationException();
   }

   public void evict(Object object) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   public Object saveOrUpdateCopy(Object object) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   public Object saveOrUpdateCopy(Object object, Serializable id) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   public void refresh(Object object) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   public void refresh(Object object, LockMode lockMode) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   public Criteria createCriteria(Class persistentClass) {
      throw new UnsupportedOperationException();
   }

   public Query createSQLQuery(String sql, String returnAlias, Class returnClass) {
      throw new UnsupportedOperationException();
   }

   public Query createSQLQuery(String sql, String[] returnAliases, Class[] returnClasses) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#save(java.lang.String, java.lang.Object, java.io.Serializable)
 */
@Override
public void save(String s, Object obj, Serializable serializable)
		throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#saveOrUpdateCopy(java.lang.String, java.lang.Object)
 */
@Override
public Object saveOrUpdateCopy(String s, Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#saveOrUpdateCopy(java.lang.String, java.lang.Object, java.io.Serializable)
 */
@Override
public Object saveOrUpdateCopy(String s, Object obj, Serializable serializable)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#update(java.lang.String, java.lang.Object, java.io.Serializable)
 */
@Override
public void update(String s, Object obj, Serializable serializable)
		throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createCriteria(java.lang.String)
 */
@Override
public Criteria createCriteria(String s) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createCriteria(java.lang.Class, java.lang.String)
 */
@Override
public Criteria createCriteria(Class class1, String s) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createCriteria(java.lang.String, java.lang.String)
 */
@Override
public Criteria createCriteria(String s, String s1) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createSQLQuery(java.lang.String)
 */
@Override
public SQLQuery createSQLQuery(String s) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#delete(java.lang.String, java.lang.Object)
 */
@Override
public void delete(String s, Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#disableFilter(java.lang.String)
 */
@Override
public void disableFilter(String s) {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#enableFilter(java.lang.String)
 */
@Override
public Filter enableFilter(String s) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable)
 */
@Override
public Object get(String s, Serializable serializable)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable, org.hibernate.LockMode)
 */
@Override
public Object get(String s, Serializable serializable, LockMode lockmode)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getCacheMode()
 */
@Override
public CacheMode getCacheMode() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getEnabledFilter(java.lang.String)
 */
@Override
public Filter getEnabledFilter(String s) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getEntityMode()
 */
@Override
public EntityMode getEntityMode() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getEntityName(java.lang.Object)
 */
@Override
public String getEntityName(Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getSession(org.hibernate.EntityMode)
 */
@Override
public org.hibernate.Session getSession(EntityMode entitymode) {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getStatistics()
 */
@Override
public SessionStatistics getStatistics() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getTransaction()
 */
@Override
public Transaction getTransaction() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable)
 */
@Override
public Object load(String s, Serializable serializable)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable, org.hibernate.LockMode)
 */
@Override
public Object load(String s, Serializable serializable, LockMode lockmode)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#lock(java.lang.String, java.lang.Object, org.hibernate.LockMode)
 */
@Override
public void lock(String s, Object obj, LockMode lockmode)
		throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#merge(java.lang.Object)
 */
@Override
public Object merge(Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#merge(java.lang.String, java.lang.Object)
 */
@Override
public Object merge(String s, Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#persist(java.lang.Object)
 */
@Override
public void persist(Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#persist(java.lang.String, java.lang.Object)
 */
@Override
public void persist(String s, Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#replicate(java.lang.String, java.lang.Object, org.hibernate.ReplicationMode)
 */
@Override
public void replicate(String s, Object obj, ReplicationMode replicationmode)
		throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#save(java.lang.String, java.lang.Object)
 */
@Override
public Serializable save(String s, Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#saveOrUpdate(java.lang.String, java.lang.Object)
 */
@Override
public void saveOrUpdate(String s, Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#setCacheMode(org.hibernate.CacheMode)
 */
@Override
public void setCacheMode(CacheMode cachemode) {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#setReadOnly(java.lang.Object, boolean)
 */
@Override
public void setReadOnly(Object obj, boolean flag) {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.Session#update(java.lang.String, java.lang.Object)
 */
@Override
public void update(String s, Object obj) throws HibernateException {
	// TODO Auto-generated method stub
	
}

@Override
public void doWork(Work work) throws HibernateException {
	// TODO Auto-generated method stub
	
}

@Override
public boolean isDefaultReadOnly() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void setDefaultReadOnly(boolean readOnly) {
	// TODO Auto-generated method stub
	
}

@Override
public Object load(Class theClass, Serializable id, LockOptions lockOptions)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object load(String entityName, Serializable id, LockOptions lockOptions)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public LockRequest buildLockRequest(LockOptions lockOptions) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void refresh(Object object, LockOptions lockOptions)
		throws HibernateException {
	// TODO Auto-generated method stub
	
}

@Override
public Object get(Class clazz, Serializable id, LockOptions lockOptions)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Object get(String entityName, Serializable id, LockOptions lockOptions)
		throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean isReadOnly(Object entityOrProxy) {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean isFetchProfileEnabled(String name)
		throws UnknownProfileException {
	// TODO Auto-generated method stub
	return false;
}

@Override
public void enableFetchProfile(String name) throws UnknownProfileException {
	// TODO Auto-generated method stub
	
}

@Override
public void disableFetchProfile(String name) throws UnknownProfileException {
	// TODO Auto-generated method stub
	
}

@Override
public TypeHelper getTypeHelper() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public LobHelper getLobHelper() {
	// TODO Auto-generated method stub
	return null;
}

}

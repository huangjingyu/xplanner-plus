package com.technoetic.xplanner.db.hibernate;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.Reference;

import org.apache.log4j.Logger;
import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.TypeHelper;
import org.hibernate.classic.Session;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import com.technoetic.xplanner.util.LogUtil;
import com.thoughtworks.proxy.Invoker;
import com.thoughtworks.proxy.factory.StandardProxyFactory;
import com.thoughtworks.proxy.toys.echo.Echoing;

public class XPlannerSessionFactory implements SessionFactory {
   protected static final Logger LOG = LogUtil.getLogger();

    private final SessionFactory delegate;

    public XPlannerSessionFactory(SessionFactory delegate) {
        this.delegate = delegate;
    }

    public void close() throws HibernateException {
        delegate.close();
    }

    public void evict(Class persistentClass) throws HibernateException {
        delegate.evict(persistentClass);
    }

    public void evict(Class persistentClass, Serializable id) throws HibernateException {
        delegate.evict(persistentClass, id);
    }

    public void evictCollection(String roleName) throws HibernateException {
        delegate.evictCollection(roleName);
    }

    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        delegate.evictCollection(roleName, id);
    }

    public void evictQueries() throws HibernateException {
        delegate.evictQueries();
    }

    public void evictQueries(String cacheRegion) throws HibernateException {
        delegate.evictQueries(cacheRegion);
    }

    public Map getAllClassMetadata() throws HibernateException {
        return delegate.getAllClassMetadata();
    }

    public Map getAllCollectionMetadata() throws HibernateException {
        return delegate.getAllCollectionMetadata();
    }

    public ClassMetadata getClassMetadata(Class persistentClass) throws HibernateException {
        return delegate.getClassMetadata(persistentClass);
    }

    public CollectionMetadata getCollectionMetadata(String roleName) throws HibernateException {
        return delegate.getCollectionMetadata(roleName);
    }

    public Session openSession() throws HibernateException {
       Session session = delegate.openSession(new XPlannerInterceptor());
       return logAndWrapNewSessionIfDebug(session);
    }

   public Session openSession(Connection connection) {
      Session session = delegate.openSession(connection, new XPlannerInterceptor());
      return logAndWrapNewSessionIfDebug(session);
    }

    public Session openSession(Connection connection, Interceptor interceptor) {
       Session session = delegate.openSession(connection, interceptor);
       return logAndWrapNewSessionIfDebug(session);
    }

    public Session openSession(Interceptor interceptor) throws HibernateException {
       Session session = delegate.openSession(interceptor);
       return logAndWrapNewSessionIfDebug(session);
    }

   private Session logAndWrapNewSessionIfDebug(final Session session) {
      if (LOG.isDebugEnabled()) {
         PrintWriter out = new PrintWriter(new Log4JDebugLoggerWriter());
         return (Session) Echoing.object(Session.class, session, out, new StandardProxyFactory() {
            @Override
			public boolean canProxy(Class type) {
               return Session.class.isAssignableFrom(type);
            }

            @Override
			public Object createProxy(Class[] types, Invoker invoker) {
               return super.createProxy(types, new SessionCountingInvoker(session, invoker));
            }
         });
      }
      return session;
   }

   public Reference getReference() throws NamingException {
       return delegate.getReference();
   }

   private static int lastId = 0;
   private static int sessionCount = 0;

   class SessionCountingInvoker implements Invoker {
      private final Session session;
      private final int id;
      private final Invoker invoker;
      private final StackTraceElement[] creationStack;

      public SessionCountingInvoker(Session session, Invoker invoker) {
         this.session = session;
         this.invoker = invoker;
         this.creationStack = getStackTraceForMethod("openSession");
         this.id = ++lastId;
         LOG.debug("Session.new() -> " + ++sessionCount + " opened. " +
                   this + " was opened:\n" + getStackTraceString(creationStack));
      }

      @Override
	public String toString() {
         return "session #" + id + " (" + session + ")";
      }

      private StackTraceElement[] getStackTraceForMethod(String methodName) {
         StackTraceElement[] stackTrace = new Throwable().getStackTrace();
         stackTrace = pruneTopDownToOpenSessionFrame(stackTrace, methodName);
         stackTrace = pruneBottomUpToFirstXPlannerFrame(stackTrace);
         return stackTrace;
      }

      private StackTraceElement[] pruneBottomUpToFirstXPlannerFrame(StackTraceElement[] stackTrace) {
         int firstXPlannerFrameIndex = findFirstXPlannerFrameIndex(stackTrace);
         StackTraceElement[] prunedStackTrace = new StackTraceElement[firstXPlannerFrameIndex+1];
         System.arraycopy(stackTrace, 0, prunedStackTrace, 0, firstXPlannerFrameIndex+1);
         return prunedStackTrace;
      }

      private int findFirstXPlannerFrameIndex(StackTraceElement[] stackTrace) {
         for (int i = stackTrace.length - 1; i >= 0; i--) {
            StackTraceElement frame = stackTrace[i];
            if (frame.getClassName().indexOf("xplanner") != -1 ||
                frame.getClassName().indexOf("springframework") != -1) {
               return i;
            }
         }
         return stackTrace.length-1;
      }

      private StackTraceElement[] pruneTopDownToOpenSessionFrame(StackTraceElement[] stackTrace, String methodName) {
         int openSessionFrameIndex = findFirstTopFrameIndexForMethod(stackTrace, methodName);
         StackTraceElement[] prunedStackTrace = new StackTraceElement[stackTrace.length - openSessionFrameIndex];
         System.arraycopy(stackTrace, openSessionFrameIndex, prunedStackTrace, 0, prunedStackTrace.length);
         return prunedStackTrace;
      }

      private int findFirstTopFrameIndexForMethod(StackTraceElement[] stackTrace, String methodName) {
         for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement frame = stackTrace[i];
            if (methodName.equals(frame.getMethodName())) {
               return i;
            }
         }
         return 0;
      }

      private String getStackTraceString(StackTraceElement[] stackTrace) {
         StringBuffer buf = new StringBuffer();
         buf.append("\n");
         for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement frame = stackTrace[i];
            buf.append("\tat " + frame + "\n");
         }
         buf.append("\n");
         return buf.toString();
      }

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         Object result = null;
         boolean connected = session.isConnected();
         try {
            return invoker.invoke(proxy, method, args);
         } finally {
            if (method.getName().equals("close")) {
               if (connected) {
                  --sessionCount;
               }
               LOG.debug("Session.close() -> " + (!connected?"did not close session. ":" ") + sessionCount + " still opened. " +
                         this + " was closed:\n" + getStackTraceString(getStackTraceForMethod("close")));
            }
         }
      }

      @Override
	protected void finalize() throws Throwable {
         super.finalize();
         if (session.isOpen()) {
            LOG.debug("     ############# Session.finalize() -> " + this + " was not closed ###############\n" +
                      "Session was allocated from:\n" + getStackTraceString(creationStack));
         }
      }
   }

   private static class Log4JDebugLoggerWriter extends Writer {
      StringBuffer buf = new StringBuffer();

      @Override
	public void close() { }

      @Override
	public void flush() {
         LOG.debug(buf.toString());
         buf = new StringBuffer();
      }

      @Override
	public void write(char[] cbuf, int off, int len) {
         buf.append(cbuf, off, len);
      }
   }

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String)
 */
@Override
public void evictEntity(String s) throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#evictEntity(java.lang.String, java.io.Serializable)
 */
@Override
public void evictEntity(String s, Serializable serializable)
		throws HibernateException {
	// TODO Auto-generated method stub
	
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getClassMetadata(java.lang.String)
 */
@Override
public ClassMetadata getClassMetadata(String s) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getCurrentSession()
 */
@Override
public Session getCurrentSession() throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getDefinedFilterNames()
 */
@Override
public Set getDefinedFilterNames() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getFilterDefinition(java.lang.String)
 */
@Override
public FilterDefinition getFilterDefinition(String s) throws HibernateException {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#getStatistics()
 */
@Override
public Statistics getStatistics() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#isClosed()
 */
@Override
public boolean isClosed() {
	// TODO Auto-generated method stub
	return false;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#openStatelessSession()
 */
@Override
public StatelessSession openStatelessSession() {
	// TODO Auto-generated method stub
	return null;
}

/* (non-Javadoc)
 * @see org.hibernate.SessionFactory#openStatelessSession(java.sql.Connection)
 */
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
package com.technoetic.xplanner.db.hibernate;

import java.util.Iterator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.hibernate.cache.QueryCache;
import org.hibernate.cache.UpdateTimestampsCache;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

public class EHCacheHelper {
    private static Logger log = Logger.getLogger(EHCacheHelper.class);
    private static final int DEFAULT_MAX_CACHE_SIZE = 1000;
    private static final boolean DEFAULT_OVERFLOW_TO_DISK = false;
    private static boolean DEFAULT_ETERNAL = false;
    private static final int DEFAULT_TIME_TO_LIVE = 120;
    private static final int DEFAULT_TIME_TO_IDLE = 120;

    public static void configure(Configuration hibernateConfig) {
        Iterator classMappings = hibernateConfig.getClassMappings();
        try {
            while (classMappings.hasNext()) {
                PersistentClass persistentClass = (PersistentClass)classMappings.next();
                configureClassCache(persistentClass.getMappedClass());
            }
            configureClassCache(QueryCache.class);
            configureClassCache(UpdateTimestampsCache.class);
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private static void configureClassCache(Class theClass) throws CacheException {
        String name = theClass.getName();
        if (!CacheManager.getInstance().cacheExists(name)) {
            Cache ehcache = new Cache(name,
                    DEFAULT_MAX_CACHE_SIZE, DEFAULT_OVERFLOW_TO_DISK,
                    DEFAULT_ETERNAL, DEFAULT_TIME_TO_LIVE, DEFAULT_TIME_TO_IDLE);
            CacheManager.getInstance().addCache(ehcache);
        }
    }
}

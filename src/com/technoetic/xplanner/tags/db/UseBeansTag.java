package com.technoetic.xplanner.tags.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;

public class UseBeansTag extends BodyTagSupport {
    private final Logger log = Logger.getLogger(getClass());
    private static HashMap queryTranslations;

    private String id;
    private String qname;
    private String type;
    private String where;
    private String order;
    private String cache;
    private int size;
    private ArrayList parameterValues = new ArrayList();
    private ArrayList parameterTypes = new ArrayList();
    private HashMap namedParameterValues = new HashMap();
    private HashMap namedParameterTypes = new HashMap();

    public UseBeansTag() {
        initializeQueryTranslations();
    }

    private synchronized void initializeQueryTranslations() {
        if (queryTranslations == null) {
            queryTranslations = new HashMap();
           String translations = new XPlannerProperties().getProperty("hibernate.query.substitutions");
            String[] translation = translations.split("\\s*,\\s*");
            for (int i = 0; i < translation.length; i++) {
                String[] keyAndValue = translation[i].split("=");
                if (keyAndValue.length == 2) {
                    String value = keyAndValue[1];
                    if (value.startsWith("'")) {
                        value = value.replaceAll("^'|'$", "");
                    }
                    queryTranslations.put(keyAndValue[0], value);
                }
            }
        }
    }

    @Override
	public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    /*package*/
    void addParameter(Object value, Type type) {
        parameterValues.add(value);
        parameterTypes.add(type);
    }

    void addParameter(String name, Object value, Type type) {
        namedParameterValues.put(name, value);
        namedParameterTypes.put(name, type);
    }

    @Override
	public int doStartTag() {
        return EVAL_BODY_INCLUDE;
    }

    @Override
	public int doEndTag() throws JspException {
        try {
            Session session = getSession();
            try {
                List objects = null;
                if (qname == null) {
                    String hql = null;
                    if (getBodyContent() == null) {
                        hql = "from " + type;
                        if (where != null) {
                            hql += " where " + where;
                        }
                        if (order != null) {
                            hql += " order by " + order;
                        }
                    } else {
                        hql = getBodyContent().getString();
                    }
                    Query query = session.createQuery(hql);
                    objects = bindParametersAndExecute(query);
                } else {
                    objects = new ArrayList();
                    String[] queryNames = qname.split(",");
                    for (int i = 0; i < queryNames.length; i++) {
                        objects.addAll(bindParametersAndExecute(session.getNamedQuery(queryNames[i])));
                    }
                }
                pageContext.setAttribute(id, objects);
                if (log.isDebugEnabled()) {
                    log.debug("loaded beans: " + id + " " + objects);
                }
            } catch (Exception ex) {
                log.error("error", ex);
                throw new JspTagException(ex.toString());
            } finally {
                parameterTypes.clear();
                parameterValues.clear();
            }
        } catch (JspTagException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JspException(ex.toString());
        }
        return EVAL_PAGE;
    }

    private List bindParametersAndExecute(Query query) throws Exception {
        if (cache != null) {
            query.setCacheable(true);
            query.setCacheRegion(cache);
        }
        for (int i = 0; i < parameterValues.size(); i++) {
            query.setParameter(i, translate(parameterValues.get(i)), (Type)parameterTypes.get(i));
        }
        for (Iterator iterator = namedParameterValues.keySet().iterator(); iterator.hasNext();) {
            String key = (String)iterator.next();
            query.setParameter(key, translate(namedParameterValues.get(key)),
                    (Type)namedParameterTypes.get(key));
        }
        if (size > 0) {
            query.setMaxResults(size);
        }
        List objects = query.list();
        return objects;
    }

    private Object translate(Object value) {
        return queryTranslations.containsKey(value) ? queryTranslations.get(value) : value;
    }

    private Session getSession() throws Exception {
        return HibernateHelper.getSession(pageContext.getRequest());
    }

    @Override
	public void release() {
        id = null;
        qname = null;
        type = null;
        where = null;
        order = null;
        cache = null;
        size = 0;
        parameterValues = new ArrayList();
        parameterTypes = new ArrayList();
        namedParameterValues = new HashMap();
        namedParameterTypes = new HashMap();
        super.release();
    }
}
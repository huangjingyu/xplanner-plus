package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

public class UseBeanTag extends DatabaseTagSupport {
    public static final String DEFAULT_OID_PARAMETER = "oid";

    private final Logger log = Logger.getLogger(getClass());
    private String id;
    private String type;
    private Object oid;
    private String oidParameter;
    private String scope;

    @Override
	public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOid(Object oid) {
        this.oid = oid;
    }

    public void setOidParameter(String oidParameter) {
        this.oidParameter = oidParameter;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
	public int doEndTag() throws JspException {
        try {
           Session session = getSession();
            try {
                Class clazz = Class.forName(type);
                Object object = session.load(clazz, getObjectId());
                pageContext.setAttribute(id, object, getScope());
                if (log.isDebugEnabled()) {
                    log.debug("bean loaded: " + id + " " + object);
                }
            } catch (Exception ex) {
                log.error("error", ex);
                throw new JspTagException(ex.toString());
            }
        } catch (JspTagException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JspTagException(ex.toString());
        }
        return EVAL_PAGE;
    }

   private Integer getObjectId() {
      Integer objectId = null;
      if (oid instanceof Integer) {
          objectId = (Integer)oid;
      } else if (oid instanceof String) {
          objectId = new Integer((String)oid);
      } else if (oidParameter != null) {
          objectId = new Integer(pageContext.getRequest().getParameter(oidParameter));
      } else {
          String oid = pageContext.getRequest().getParameter(DEFAULT_OID_PARAMETER);
          if (oid != null) {
              objectId = new Integer(oid);
          }
      }
      return objectId;
   }

   private int getScope() {
       if ("application".equals(scope)) return PageContext.APPLICATION_SCOPE;
       if ("session".equals(scope)) return PageContext.SESSION_SCOPE;
       if ("request".equals(scope)) return PageContext.REQUEST_SCOPE;
       return PageContext.PAGE_SCOPE;
   }

    @Override
	public void release() {
        super.release();
        scope = null;
    }

	@Override
	protected int doStartTagInternal() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}

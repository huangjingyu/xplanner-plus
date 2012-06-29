package com.technoetic.xplanner.tags.displaytag;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.model.Row;
import org.displaytag.properties.TableProperties;
import org.displaytag.tags.TableTag;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.AuthorizationHelper;
import com.technoetic.xplanner.tags.WritableTag;


public class WritableTableTag extends TableTag implements WritableTag {
    private static final Logger log = Logger.getLogger(WritableTableTag.class);
    static final String IS_AUTHORIZED_FOR_ANY_PARAM_NAME = "isAuthorizedForAny";
   static
   {
      TableProperties.setUserProperties(new XPlannerProperties().get());
   }
    private String permissions;
    private Collection wholeCollection;

    /**
    * Optional decorator for row objects
    */
    private RowDecorator rowDecorator;

    public WritableTableTag() {
      super();
      //todo uncomment to enable export links as default
      //this.setExport(true);
    }

    public void setRowDecorator(Object rowDecorator) {
        if (rowDecorator instanceof String) {
            try {
                rowDecorator = Class.forName((String)rowDecorator).newInstance();
            } catch (Exception e) {
               log.error("Exception ", e);
            }
        }
        if (rowDecorator instanceof RowDecorator) {
            this.rowDecorator = (RowDecorator)rowDecorator;
        }
    }

    public Row setRowObjectForCellValues(Object iteratedObject, int rowNumber)
    {
        return new com.technoetic.xplanner.tags.displaytag.Row(
                            iteratedObject, rowNumber, rowDecorator);
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public void setWholeCollection(Collection wholeCollection) {
        this.wholeCollection = wholeCollection;
    }

    public boolean isWritable()
        throws Exception {
        Boolean isAuthorized = (Boolean) pageContext.getAttribute(IS_AUTHORIZED_FOR_ANY_PARAM_NAME);
        if (isAuthorized == null){
            isAuthorized = new Boolean(hasPermissionToAny());
            pageContext.setAttribute(IS_AUTHORIZED_FOR_ANY_PARAM_NAME, isAuthorized);
        }
        return isAuthorized.booleanValue();
    }

    private boolean hasPermissionToAny() throws AuthenticationException {
        if (StringUtils.isEmpty(permissions)){
            return true;
        }
        String[] permissionArray = permissions.split(",");
        boolean isAuthorized = AuthorizationHelper.hasPermissionToAny(permissionArray, wholeCollection, pageContext.getRequest());
        return isAuthorized;
    }
}

package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.PageHelper;

/**
 * User: Mateusz Prokopowicz
 * Date: Feb 15, 2005
 * Time: 11:08:59 AM
 */
public class AuthorizationHelper
{

    public static boolean hasPermissionToAny(String[] permissionArray,
                                             Collection objectCollection,
                                             ServletRequest request)
        throws AuthenticationException
    {
        return hasPermissionToAny(permissionArray, objectCollection, request, 0);
    }

    public static boolean hasPermissionToAny(String[] permissionArray,
                                             Collection objectCollection,
                                             ServletRequest request,
                                             int projectId)
        throws AuthenticationException
    {
        boolean isAuthorized = false;
        int remoteUserId = SecurityHelper.getRemoteUserId((HttpServletRequest) request);
        for (Iterator iterator = objectCollection.iterator(); !isAuthorized && iterator.hasNext();)
        {
            Object resource = iterator.next();
            projectId = PageHelper.getProjectId(resource, request);
            for (int i = 0; i < permissionArray.length; i++)
            {
                String permission = permissionArray[i];
                if (SystemAuthorizer.get().hasPermission(projectId,
                                                         remoteUserId,
                                                         resource, permission))
                {
                    isAuthorized = true;
                    break;
                }
            }
        }
        return isAuthorized;
    }

    public static boolean hasPermission(int projectId,
                                        int principalId,
                                        int resourceId,
                                        String resourceType,
                                        String permission,
                                        Object resource,
                                        ServletRequest request) throws AuthenticationException
    {
        boolean hasPermission;
        if (principalId == 0)
        {
            principalId = SecurityHelper.getRemoteUserId((HttpServletRequest) request);
        }
        if (resourceType != null)
        {
            hasPermission = !SystemAuthorizer.get().
                hasPermission(projectId, principalId, resourceType, resourceId, permission);
        }
        else
        {
            hasPermission = !SystemAuthorizer.get().
                hasPermission(projectId, principalId, resource, permission);
        }
        return hasPermission;
    }
}

package com.technoetic.xplanner.security.auth;

public class PermissionSetKey {
    int principalId;
    String resourceType;
    int resourceId;
    String permission;

    public PermissionSetKey(int principalId, String resourceType, int resourceId, String permission) {
        this.principalId = principalId;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.permission = permission;
    }

    public int getPrincipalId() {
        return principalId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getPermission() {
        return permission;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionSetKey)) return false;

        final PermissionSetKey permissionSetKey = (PermissionSetKey)o;

        if (principalId != permissionSetKey.principalId) return false;
        if (resourceId != permissionSetKey.resourceId) return false;
        if (permission != null ? !permission.equals(permissionSetKey.permission) : permissionSetKey.permission != null) return false;
        if (resourceType != null ? !resourceType.equals(permissionSetKey.resourceType) : permissionSetKey.resourceType != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = principalId;
        result = 29 * result + (resourceType != null ? resourceType.hashCode() : 0);
        result = 29 * result + resourceId;
        result = 29 * result + (permission != null ? permission.hashCode() : 0);
        return result;
    }

    public String toString() {
        return getClass().getName().substring(getClass().getName().lastIndexOf(".")+1) +
                "("+principalId+","+resourceType+","+resourceId+","+permission+")";
    }
}

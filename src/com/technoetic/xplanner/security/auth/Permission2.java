package com.technoetic.xplanner.security.auth;

import com.technoetic.xplanner.domain.Identifiable;

public class Permission2 implements Identifiable {
    private int id;
    private String resourceType;
    private int resourceId;
    private int principalId;
    private String name;
    private boolean positive = true;

    public Permission2() {
    }

    public Permission2(String resourceType, int resourceId, int personId, String permissionName) {
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.principalId = personId;
        this.name = permissionName;
    }

   public boolean isPositive() {
      return positive;
   }

   public void setPositive(boolean positive) {
      this.positive = positive;
   }

   public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(int principalId) {
        this.principalId = principalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String toString() {
        return getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)+"(" +
               "id="+id +", principalId=" +principalId +", resourceType='" +
               resourceType+"'"+", resourceId="+resourceId+", name='"+name +"'," + (positive?"positive":"negative")+" )";
    }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final Permission2 that = (Permission2) o;

    if (id != that.id) return false;
    if (positive != that.positive) return false;
    if (principalId != that.principalId) return false;
    if (resourceId != that.resourceId) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = id;
    result = 29 * result + (resourceType != null ? resourceType.hashCode() : 0);
    result = 29 * result + resourceId;
    result = 29 * result + principalId;
    result = 29 * result + (name != null ? name.hashCode() : 0);
    result = 29 * result + (positive ? 1 : 0);
    return result;
  }
}

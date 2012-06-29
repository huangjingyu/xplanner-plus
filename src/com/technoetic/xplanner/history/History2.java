package com.technoetic.xplanner.history;

import java.util.Date;

import com.technoetic.xplanner.domain.Identifiable;

public class History2 implements Identifiable{
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";
    public static final String DELETED = "deleted";
    public static final String REESTIMATED = "reestimated";
    public static final String ITERATION_STARTED = "started";
    public static final String ITERATION_CLOSED = "closed";
    public static final String MOVED = "moved";
    public static final String MOVED_IN = "moved in";
    public static final String MOVED_OUT = "moved out";
    public static final String CONTINUED = "continued";

    private Date when;
    private int targetObjectId;
    private int containerId;
    private String objectType;
    private String action;
    private String description;
    private int personId;
    private boolean notified;
    private int id;

   //For hibernate
    History2() {
    }

    public History2(int containerId, int targetObjectId, String objectType, String action, String description,
            int personId, Date when) {
        this.containerId = containerId;
        this.objectType = objectType;
        this.when = when;
        this.targetObjectId = targetObjectId;
        this.action = action;
        this.description = description;
        this.personId = personId;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public int getTargetObjectId() {
        return targetObjectId;
    }

    public void setTargetObjectId(int targetObjectId) {
        this.targetObjectId = targetObjectId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public String toString(){
        return "History(id="+this.getId()+
                              ", personId="+this.getPersonId()+
                              ", targetObjectId="+this.getTargetObjectId()+
                              ", objectType=" + objectType +
                              ", action="+this.getAction() +
                              ", containerId="+this.getContainerId() +
                              ", desc.='"+this.getDescription() + '\'' +
                              ", date="+this.getWhen()+
                              ", notified=" + notified +
                              ")";
    }

   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final History2 event = (History2) o;

      if (containerId != event.containerId) return false;
      if (id != event.id) return false;
      if (notified != event.notified) return false;
      if (personId != event.personId) return false;
      if (targetObjectId != event.targetObjectId) return false;
      if (action != null ? !action.equals(event.action) : event.action != null) return false;
      if (description != null ? !description.equals(event.description) : event.description != null) return false;
      if (objectType != null ? !objectType.equals(event.objectType) : event.objectType != null) return false;
      return !(when != null ? !when.equals(event.when) : event.when != null);

   }

   public int hashCode() {
      int result;
      result = (when != null ? when.hashCode() : 0);
      result = 29 * result + targetObjectId;
      result = 29 * result + containerId;
      result = 29 * result + (objectType != null ? objectType.hashCode() : 0);
      result = 29 * result + (action != null ? action.hashCode() : 0);
      result = 29 * result + (description != null ? description.hashCode() : 0);
      result = 29 * result + personId;
      result = 29 * result + (notified ? 1 : 0);
      result = 29 * result + id;
      return result;
   }
}

package com.technoetic.xplanner.domain;

import java.io.Serializable;
import java.security.Principal;

import net.sf.xplanner.domain.NamedObject;

public class Role2 extends NamedObject implements Principal, Serializable {
   public static final String SYSADMIN = "sysadmin";
   public static final String ADMIN = "admin";
   public static final String EDITOR = "editor";
   public static final String VIEWER = "viewer";
    private int left;
    private int right;

    Role2() {
        // for Hibernate
    }

    public Role2(String name) {
        setName(name);
    }

    public String toString() {
        return getName();
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

   public boolean isSysadmin() {
      return getName().equals(SYSADMIN);
   }

   public String getDescription() {return "";}
}

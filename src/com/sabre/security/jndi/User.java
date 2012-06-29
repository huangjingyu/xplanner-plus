package com.sabre.security.jndi;

import java.util.List;

public class User
{
    String username = null;
    String dn = null;
    String password = null;
    List roles = null;


    public User(String username,
         String dn,
         String password,
         List roles)
    {
        this.username = username;
        this.dn = dn;
        this.password = password;
        this.roles = roles;
    }

}

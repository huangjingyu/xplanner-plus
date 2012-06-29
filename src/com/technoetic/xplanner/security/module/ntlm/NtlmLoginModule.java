package com.technoetic.xplanner.security.module.ntlm;

import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;

import org.apache.log4j.Logger;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.LoginSupport;
import com.technoetic.xplanner.security.module.LoginSupportImpl;

public class NtlmLoginModule implements LoginModule
{
   private String domainController;
   private String domain;
   private String name;
   private transient Logger log = Logger.getLogger(getClass());
   transient LoginSupport support = new LoginSupportImpl();
   transient NtlmLoginHelper helper = new NtlmLoginHelperImpl();
   public static final String DOMAIN_KEY = "domain";
   public static final String CONTROLLER_KEY = "controller";

   public NtlmLoginModule(LoginSupport loginSupport, NtlmLoginHelper helper) {
      this.support = loginSupport;
      this.helper = helper;
   }

   public void setOptions(Map options)
    {
        domain = options.get(DOMAIN_KEY) != null ? (String) options.get(DOMAIN_KEY) : "YANDEX";
        domainController = options.get(CONTROLLER_KEY) != null ? (String) options.get(CONTROLLER_KEY) : domain;
        log.debug("initialized");
    }

    public Subject authenticate(String userId, String password) throws AuthenticationException
    {
        log.debug(ATTEMPTING_TO_AUTHENTICATE + this.getName() +" (" + userId + ")");
        try
        {
            helper.authenticate(userId, password, domainController, domain);

        }
        catch (SmbAuthException sae)
        {
            log.error("NT domain did not authenticate user " + userId);
            throw new AuthenticationException(MESSAGE_AUTHENTICATION_FAILED_KEY);
        }
        catch (SmbException se)
        {
            log.error("SmbException while authenticating " + userId, se);
            throw new AuthenticationException(MESSAGE_SERVER_ERROR_KEY);
        }
        catch (java.net.UnknownHostException e)
        {
            log.error("UnknownHostException while authenticating " + userId, e);
            throw new AuthenticationException(MESSAGE_SERVER_NOT_FOUND_KEY);
        }

        log.info("NT domain authenticated user " + userId);

       Subject subject = support.createSubject();
       log.debug("looking for user: " + userId);
       support.populateSubjectPrincipalFromDatabase(subject, userId);
       log.debug(AUTHENTICATION_SUCCESFULL + this.getName());
       return subject;
    }

    public boolean isCapableOfChangingPasswords()
    {
        return false;
    }

    public void changePassword(String userId, String password) throws AuthenticationException
    {
        throw new UnsupportedOperationException("change Password not implemented");
    }

    public void logout(HttpServletRequest request) throws AuthenticationException
    {
        request.getSession().invalidate();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}

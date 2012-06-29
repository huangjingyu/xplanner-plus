package com.technoetic.xplanner.security.module;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.util.Base64;

public class XPlannerLoginModule implements LoginModule {
   private transient final Logger log = Logger.getLogger(getClass());
   private final transient SecureRandom secureRandom = new SecureRandom();
   private String name;
   private final LoginSupport loginSupport;

   public XPlannerLoginModule(LoginSupport support) {
       loginSupport = support;
   }

   public void setOptions(Map options) {}

    public Subject authenticate(String userId, String password) throws AuthenticationException {
       log.debug(ATTEMPTING_TO_AUTHENTICATE + this.getName() +" (" + userId + ")");
       Subject subject = loginSupport.createSubject();
       Person person = loginSupport.populateSubjectPrincipalFromDatabase(subject, userId);
       if (!isPasswordMatched(person, password))
       {
          throw new AuthenticationException(MESSAGE_AUTHENTICATION_FAILED_KEY);
       }
       log.debug(AUTHENTICATION_SUCCESFULL + this.getName());
       return subject;
    }

    public boolean isCapableOfChangingPasswords() {
        return true;
    }

    boolean isPasswordMatched(Person person, String password) throws AuthenticationException {
        log.debug("evaluating password match for " + person.getUserId());
        String storedPassword = person.getPassword();
        if (storedPassword == null) {
            throw new AuthenticationException(MESSAGE_NULL_PASSWORD_KEY);
        }
        byte[] storedPasswordBytesWithSalt = Base64.decode(storedPassword.getBytes());

        if (storedPasswordBytesWithSalt.length < 12) {
            throw new AuthenticationException(MESSAGE_AUTHENTICATION_FAILED_KEY);
        }
        byte[] salt = new byte[12];
        System.arraycopy(storedPasswordBytesWithSalt, 0, salt, 0, 12);

       byte[] digestForGivenPassword = digestPassword(salt, password);
       byte[] digestForExistingPassword = new byte[storedPasswordBytesWithSalt.length - 12];
        System.arraycopy(storedPasswordBytesWithSalt, 12, digestForExistingPassword, 0, storedPasswordBytesWithSalt.length - 12);

        boolean isMatching = Arrays.equals(digestForGivenPassword, digestForExistingPassword);
        log.debug("passwords " + (isMatching ? "matched" : "did not match") + " for " + person.getUserId());
        return isMatching;
    }

   private byte[] digestPassword(byte[] salt, String password) throws AuthenticationException {
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(salt);
         md.update(password.getBytes("UTF8"));
         return md.digest();
      } catch (Exception e) {
         throw new AuthenticationException(MESSAGE_CONFIGURATION_ERROR_KEY, e);
      }
   }

   public void changePassword(String userId, String password) throws AuthenticationException {
       log.debug("changing password for " + userId);
       try {
           Session session = ThreadSession.get();
           session.connection().setAutoCommit(false);
           try {
              Person person = loginSupport.getPerson(userId);
               if (person != null) {
                   person.setPassword(encodePassword(password, null));
                   session.flush();
                   session.connection().commit();
               } else {
                   throw new AuthenticationException("couldn't find person.");
               }
           } catch (Throwable ex) {
               session.connection().rollback();
               log.error("error during password change.", ex);
               throw new AuthenticationException("server error.");
           }

       } catch (Exception e) {
           log.error("error", e);
           throw new AuthenticationException("server error.");
       }
   }

    public void logout(HttpServletRequest request) throws AuthenticationException {
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

   public String encodePassword(String password, byte[] salt) throws Exception {
       if (salt == null) {
           salt = new byte[12];
           secureRandom.nextBytes(salt);
       }

      byte[] digest = digestPassword(salt, password);
      byte[] storedPassword = new byte[digest.length + 12];

       System.arraycopy(salt, 0, storedPassword, 0, 12);
       System.arraycopy(digest, 0, storedPassword, 12, digest.length);

       return new String(Base64.encode(storedPassword));
   }

    // This main can be used to generate a hashed password by hand, if needed.
    public static void main(String[] args) {
        try {
            String password;
            if (args.length == 0) {
                password = "admin";
            } else {
                password = args[0];
            }
            System.out.println(new XPlannerLoginModule(new LoginSupportImpl()).encodePassword(password, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.sabre.security.jndi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.Subject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.sabre.security.jndi.util.Base64;
import com.sabre.security.jndi.util.HexUtils;

public class JNDIAuthenticatorImpl implements JNDIAuthenticator {

   /**
    * Debug level
    */
   private int debug = 10;
   /**
    * Logger.
    */
   public static final Logger log = Logger.getLogger(JNDIAuthenticatorImpl.class);
   /**
    * Should we search the entire subtree for matching users?
    */
   private boolean userSubtree;
   /**
    * Should we search the entire subtree for matching memberships?
    */
   private boolean roleSubtree;
   /**
    * Descriptive information about this Realm implementation.
    */
   protected static final String info = "org.apache.catalina.realm.JNDIRealm/1.0";
   /**
    * The JNDI context factory used to acquire our InitialContext.  By
    * default, assumes use of an LDAP server using the standard JNDI LDAP
    * provider.
    */
   protected String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
   /**
    * The type of authentication to use
    */
   private String authentication;
   /**
    * The connection username for the server we will contact.
    */
   private String connectionUser;
   /**
    * The connection password for the server we will contact.
    */
   private String connectionPassword;
   /**
    * The connection URL for the server we will contact.
    */
   private String connectionURL;
   /**
    * The protocol that will be used in the communication with the directory server.
    */
   private String protocol;
   /**
    * How should we handle referrals?  Microsoft Active Directory can't handle
    * the default case, so an application authenticating against AD must
    * set referrals to "follow".
    */
   private String referrals;
   /**
    * The base element for user searches.
    */
   private String userBase = "";
   /**
    * The message format used to search for a user, with "{0}" marking
    * the spot where the username goes.
    */
   private String userSearch;
   /**
    * The attribute name used to retrieve the user password.
    */
   private String userPassword;
   /**
    * The message format used to form the distinguished name of a
    * user, with "{0}" marking the spot where the specified username
    * goes.
    */
   private String userPattern;
   /**
    * The base element for role searches.
    */
   private String roleBase = "";
   /**
    * The name of an attribute in the user's entry containing
    * roles for that user
    */
   private String userRoleName;
   /**
    * The name of the attribute containing roles held elsewhere
    */
   private String roleName;
   /**
    * The directory context linking us to our directory server.
    */
   protected DirContext context;
   /**
    * The MessageDigest
    */
   private MessageDigest messageDigest;
   /**
    * The MessageFormat object associated with the current
    * <code>userSearch</code>.
    */
   protected MessageFormat userSearchFormat;
   /**
    * The MessageFormat object associated with the current
    * <code>userPattern</code>.
    */
   protected MessageFormat userPatternFormat;
   /**
    * The MessageFormat object associated with the current
    * <code>roleSearch</code>.
    */
   protected MessageFormat roleFormat;


   public void setDigest(String algorithm) throws NoSuchAlgorithmException {
      messageDigest = MessageDigest.getInstance(algorithm);
   }

   public void setDebug(String debug) {
      this.debug = Integer.parseInt(debug);
   }

   public void setUserSubtree(String userSubtree) {
      this.userSubtree = Boolean.getBoolean(userSubtree);
   }

   public void setRoleSubtree(String roleSubtree) {
      this.roleSubtree = Boolean.getBoolean(roleSubtree);
   }

   public void setContextFactory(String contextFactory) {
      this.contextFactory = contextFactory;
   }

   public void setAuthentication(String authentication) {
      this.authentication = authentication;
   }

   public void setConnectionUser(String connectionUser) {
      this.connectionUser = connectionUser;
   }

   public void setConnectionPassword(String connectionPassword) {
      this.connectionPassword = connectionPassword;
   }

   public void setConnectionURL(String connectionURL) {
      this.connectionURL = connectionURL;
   }

   public void setProtocol(String protocol) {
      this.protocol = protocol;
   }

   public void setReferrals(String referrals) {
      this.referrals = referrals;
   }

   public void setUserBase(String userBase) {
      this.userBase = userBase;
   }

   public void setUserSearch(String userSearch) {
      this.userSearch = userSearch;
      if (userSearch == null) {
         userSearchFormat = null;
      } else {
         userSearchFormat = new MessageFormat(userSearch);
      }
   }

   public void setUserPassword(String userPassword) {
      this.userPassword = userPassword;
   }

   public void setUserPattern(String userPattern) {
      this.userPattern = userPattern;
      if (userPattern == null) {
         userPatternFormat = null;
      } else {
         userPatternFormat = new MessageFormat(userPattern);
      }
   }

   public void setRoleBase(String roleBase) {
      this.roleBase = roleBase;
   }

   public void setUserRoleName(String userRoleName) {
      this.userRoleName = userRoleName;
   }

   public void setRoleName(String roleName) {
      this.roleName = roleName;
   }

   public void setRoleSearch(String roleSearch) {
      if (roleSearch == null) {
         roleFormat = null;
      } else {
         roleFormat = new MessageFormat(roleSearch);
      }
   }

   public void logMap(Map options) {
      for (Iterator iterator = options.keySet().iterator(); iterator.hasNext();) {
         Object key = iterator.next();
         log.debug("option: " + key + "=" + options.get(key));
      }
   }

   /**
    * Return the Principal associated with the specified username and
    * credentials, if there is one; otherwise return <code>null</code>.
    * <p/>
    * If there are any errors with the JDBC connection, executing
    * the query or anything we return null (don't authenticate). This
    * event is also logged, and the connection will be closed so that
    * a subsequent request will automatically re-open it.
    *
    * @param username    Username of the Principal to look up
    * @param credentials Password or other credentials to use in
    *                    authenticating this username
    */
   public synchronized Subject authenticate(String username, String credentials) throws AuthenticationException {

         openConnectionCheckCredentials(username, credentials);
         return getSubject(username, credentials);

   }

   private void openConnectionCheckCredentials(String username, String credentials)
         throws AuthenticationException {

      try{
         if (isAnonymousConnection()) {

            log.debug("Anonymous connection");
            open();
            checkCredentials(username, credentials);

         } else if (isFixedUserConnection()) {

            log.debug("Fixed user connection");
            open(connectionUser, connectionPassword);
            checkCredentials(username, credentials);

         } else {

            if(userPattern != null) {
                String formattedUsername = MessageFormat.format(userPattern, new Object[]{username});
                log.debug("Connection with " + formattedUsername + " user");
                open(formattedUsername, credentials);
            }

         }
      }finally {
         closeContext();
      }

   }

   public void setOptions(Map options) {
      try {
         BeanUtils.copyProperties(this, options);
         if (this.debug >= 2) {
            logMap(options);
         }
      }
      catch (Exception e) {
         // Don't care about not assignable fields
      }

   }

   private Subject getSubject(final String username, String credentials) throws AuthenticationException {
      if (username == null || username.equals("")
          || credentials == null || credentials.equals("")) {
            throw new AuthenticationException(JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
      }
      Subject subject = new Subject();
      Principal principal = new Principal(){

         public String getName() {
            return username;
         }
      };
      subject.getPrincipals().add(principal);
      return subject;
   }

   /**
    * Return a User object containing information about the user
    * with the specified username, if found in the directory;
    * otherwise return <code>null</code>.
    * <p/>
    * If the <code>userPassword</code> configuration attribute is
    * specified, the value of that attribute is retrieved from the
    * user's directory entry. If the <code>userRoleName</code>
    * configuration attribute is specified, all values of that
    * attribute are retrieved from the directory entry.
    *
    * @param username Username to be looked up
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   protected User getUser(String username)
         throws NamingException {

      User user = null;

      // Get attributes to retrieve from user entry
      ArrayList list = new ArrayList();
      if (userPassword != null) {
         list.add(userPassword);
      }
      if (userRoleName != null) {
         list.add(userRoleName);
      }
      String[] attrIds = new String[list.size()];
      list.toArray(attrIds);

      // Use pattern or search for user entry
      if (userPatternFormat != null) {
         log("Finding user by pattern.");
         user = getUserByPattern(username, attrIds);
      } else {
         log("Finding user by search. Subtree is " + userSubtree);
         user = getUserBySearch(username, attrIds);
      }

      return user;
   }

   /**
    * Use the <code>UserPattern</code> configuration attribute to
    * locate the directory entry for the user with the specified
    * username and return a User object; otherwise return
    * <code>null</code>.
    *
    * @param username The username
    * @param attrIds  String[]containing names of attributes to
    *                 retrieve.
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   protected User getUserByPattern(String username,
                                   String[] attrIds)
         throws NamingException {

      if (debug >= 2) {
         log("lookupUser(" + username + ")");
      }

      if (username == null || userPatternFormat == null) return null;

      // Form the dn from the user pattern
      String dn = userPatternFormat.format(new String[]{username});
      if (debug >= 3) {
         log("  dn=" + dn);
      }

      // Return if no attributes to retrieve
      if (attrIds == null || attrIds.length == 0) {
         return new User(username, dn, null, null);
      }

      // Get required attributes from user entry
      Attributes attrs = null;
      try {
         attrs = context.getAttributes(dn, attrIds);
      }
      catch (NameNotFoundException e) {
         return null;
      }
      if (attrs == null) return null;

      // Retrieve value of userPassword
      String password = null;
      if (userPassword != null) {
         password = getAttributeValue(userPassword, attrs);
      }

      // Retrieve values of userRoleName attribute
      List roles = null;
      if (userRoleName != null) {
         roles = addAttributeValues(userRoleName, attrs, roles);
      }

      return new User(username, dn, password, roles);
   }

   /**
    * Search the directory to return a User object containing
    * information about the user with the specified username, if
    * found in the directory; otherwise return <code>null</code>.
    *
    * @param username The username
    * @param attrIds  String[]containing names of attributes to retrieve.
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   protected User getUserBySearch(String username,
                                  String[] attrIds)
         throws NamingException {

      if (username == null || userSearchFormat == null) {
         if (username == null) {
            log("getUserBySearch impossible - username is null.");
         }
         if (userSearchFormat == null) {
            log("getUserBySearch impossible - userSearchFormat is null.");
         }
         return null;
      }

      // Form the search filter
      String filter = userSearchFormat.format(new String[]{username});

      // Set up the search controls
      SearchControls constraints = new SearchControls();

      if (userSubtree) {
         constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
      } else {
         constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
      }

      // Specify the attributes to be retrieved
      if (attrIds == null) {
         attrIds = new String[0];
      }
      constraints.setReturningAttributes(attrIds);

      if (debug > 3) {
         log("  Searching for " + username);
         log("  base: " + userBase + "  filter: " + filter);
      }

      NamingEnumeration results =
            context.search(userBase, filter, constraints);

      // Fail if no entries found
      if (results == null || !results.hasMore()) {
         if (debug > 2) {
            log("  username not found");
         }
         return null;
      }

      // Get result for the first entry found
      SearchResult result = (SearchResult) results.next();

      // Check no further entries were found
      if (results.hasMore()) {
         log("username " + username + " has multiple entries");
         return null;
      }

      // Get the entry's distinguished name
      NameParser parser = context.getNameParser("");
      Name contextName = parser.parse(context.getNameInNamespace());
      Name baseName = parser.parse(userBase);
      Name entryName = parser.parse(result.getName());
      Name name = contextName.addAll(baseName);
      name = name.addAll(entryName);
      String dn = name.toString();

      if (debug > 2) {
         log("  entry found for " + username + " with dn " + dn);
      }

      // Get the entry's attributes
      Attributes attrs = result.getAttributes();
      if (attrs == null) {
         return null;
      }

      // Retrieve value of userPassword
      String password = null;
      if (userPassword != null) {
         password = getAttributeValue(userPassword, attrs);
      }

      // Retrieve values of userRoleName attribute
      List roles = null;
      if (userRoleName != null) {
         roles = addAttributeValues(userRoleName, attrs, roles);
      }

      return new User(username, dn, password, roles);
   }

   public boolean isAnonymousConnection() {
      return userPattern == null && connectionUser == null;
   }

   /**
    * Check credentials by binding to the directory as the user
    *
    * @param user        The User to be authenticated
    * @param credentials Authentication credentials
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   protected boolean bindAsUser(User user, String credentials)
         throws NamingException {

      if (credentials == null || user == null) {
         return false;
      }

      String dn = user.dn;
      if (dn == null) {
         return false;
      }

      // Validate the credentials
      if (debug >= 3) {
         log("  validating credentials by binding as the user");
      }

      // Set up security environment to bind as the user
      context.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
      context.addToEnvironment(Context.SECURITY_CREDENTIALS, credentials);

      // Elicit an LDAP bind operation
      boolean validated = false;
      try {
         if (debug > 2) {
            log("  binding as " + dn);
         }
         context.getAttributes("", null);
         validated = true;
      }
      catch (javax.naming.AuthenticationException e) {
         if (debug > 2) {
            log("  bind attempt failed");
         }
      }

      // Restore the original security environment
      if (connectionUser != null) {
         context.addToEnvironment(Context.SECURITY_PRINCIPAL, connectionUser);
      } else {
         context.removeFromEnvironment(Context.SECURITY_PRINCIPAL);
      }

      if (connectionPassword != null) {
         context.addToEnvironment(Context.SECURITY_CREDENTIALS,
                                  connectionPassword);
      } else {
         context.removeFromEnvironment(Context.SECURITY_CREDENTIALS);
      }

      return validated;
   }

   /**
    * Return a List of roles associated with the given User.  Any
    * roles present in the user's directory entry are supplemented by
    * a directory search. If no roles are associated with this user,
    * a zero-length List is returned.
    *
    * @param user    The User to be checked
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   public List getRoles(User user)
         throws NamingException {

      if (user == null) return null;

      String dn = user.dn;
      String username = user.username;

      if (dn == null || username == null) return null;

      if (debug >= 2) {
         log("  getRoles(" + dn + ")");
      }

      // Start with roles retrieved from the user entry
      List list = user.roles;
      if (list == null) {
         list = new ArrayList();
      }

      // Are we configured to do role searches?
      if (roleFormat == null || roleName == null) return list;

      // Set up parameters for an appropriate search
      String filter = roleFormat.format(new String[]{dn, username});
      SearchControls controls = new SearchControls();
      if (roleSubtree) {
         controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
      } else {
         controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
      }
      controls.setReturningAttributes(new String[]{roleName});

      // Perform the configured search and process the results
      if (debug >= 3) {
         log("  Searching role base '" + roleBase + "' for attribute '" +
             roleName + "'");
         log("  With filter expression '" + filter + "'");
      }
      NamingEnumeration results =
            context.search(roleBase, filter, controls);
      if (results == null) {
         return list;  // Should never happen, but just in case ...
      }
      while (results.hasMore()) {
         SearchResult result = (SearchResult) results.next();
         Attributes attrs = result.getAttributes();
         if (attrs == null) {
            continue;
         }
         list = addAttributeValues(roleName, attrs, list);
      }

      // Return the augmented list of roles
      if (debug >= 2) {
         log("  Returning " + list.size() + " roles");
         for (int i = 0; i < list.size(); i++) {
            log("  Found role " + list.get(i));
         }
      }

      return list;
   }

   /**
    * Return a String representing the value of the specified attribute.
    *
    * @param attrId Attribute name
    * @param attrs  Attributes containing the required value
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   private String getAttributeValue(String attrId, Attributes attrs)
         throws NamingException {

      if (debug >= 3) {
         log("  retrieving attribute " + attrId);
      }

      if (attrId == null || attrs == null) {
         return null;
      }

      Attribute attr = attrs.get(attrId);
      if (attr == null) return null;

      Object value = attr.get();
      if (value == null) return null;
      String valueString = null;
      if (value instanceof byte[]) {
         valueString = new String((byte[]) value);
      } else {
         valueString = value.toString();
      }

      return valueString;
   }

   /**
    * Add values of a specified attribute to a list
    *
    * @param attrId Attribute name
    * @param attrs  Attributes containing the new values
    * @param values ArrayList containing values found so far
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   private List addAttributeValues(String attrId,
                                   Attributes attrs,
                                   List values)
         throws NamingException {

      if (debug >= 3) {
         log("  retrieving values for attribute " + attrId);
      }
      if (attrId == null || attrs == null) {
         return null;
      }
      if (values == null) {
         values = new ArrayList();
      }
      Attribute attr = attrs.get(attrId);
      if (attr == null) {
         return null;
      }
      NamingEnumeration e = attr.getAll();
      while (e.hasMore()) {
         String value = (String) e.next();
         values.add(value);
      }
      return values;
   }

   /**
    * Create our directory context configuration.
    *
    * @return java.util.Hashtable the configuration for the directory context.
    */
   protected Hashtable getDirectoryContextEnvironment(String username, String credentials) {

      Hashtable env = new Hashtable();

      setAttributeIfValueNotNull(env, Context.PROVIDER_URL, connectionURL);
      setAttributeIfValueNotNull(env, Context.INITIAL_CONTEXT_FACTORY, contextFactory);
      setAttributeIfValueNotNull(env, Context.SECURITY_PRINCIPAL, username);
      setAttributeIfValueNotNull(env, Context.SECURITY_CREDENTIALS, credentials);
      setAttributeIfValueNotNull(env, Context.SECURITY_AUTHENTICATION, authentication);
      setAttributeIfValueNotNull(env, Context.SECURITY_PROTOCOL, protocol);
      setAttributeIfValueNotNull(env, Context.REFERRAL, referrals);

      return env;
   }

   private void setAttributeIfValueNotNull(Hashtable env, String attribute, String value) {
      if (value != null) env.put(attribute, value);
   }

   /**
    * Check whether the given User can be authenticated with the
    * given credentials. If the <code>userPassword</code>
    * configuration attribute is specified, the credentials
    * previously retrieved from the directory are compared explicitly
    * with those presented by the user. Otherwise the presented
    * credentials are checked by binding to the directory as the
    * user.
    *
    * @param userName    The UserName to be authenticated
    * @param credentials The credentials presented by the user
    * @throws com.sabre.security.jndi.AuthenticationException if a directory server error occurs
    *                                      if authentication fails
    */
   public void checkCredentials(String userName, String credentials)
         throws com.sabre.security.jndi.AuthenticationException {

      boolean validated = false;
      try {
         User user = this.getUser(userName);

         if (userPassword == null) {
            validated = bindAsUser(user, credentials);
            if(user != null) {
               log("bindAsUser(user, credentials) '" + user.username + "'");
            } else {
               log("bindAsUser(user, credentials)");
            }
         } else {
            validated = compareCredentials(user, credentials);
            if(user != null) {
               log("compareCredentials(user, credentials) '" + user.username + "'");
            } else {
               log("compareCredentials(user, credentials)");
            }
         }

         if (debug >= 2) {
            if(user != null) {
               log("jndiRealm.authenticate" + (validated ? "Success" : "Failure" + " " + user.username));
            } else {
               log("jndiRealm.authenticate" + (validated ? "Success" : "Failure"));
            }
         }
      } catch (CommunicationException ex) {
         log("jndiRealm.exception", ex);
         closeContext();
         throw new com.sabre.security.jndi.AuthenticationException(JNDIAuthenticator.MESSAGE_COMMUNICATION_ERROR_KEY);
       } catch (NamingException ne) {
         throw new com.sabre.security.jndi.AuthenticationException(JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
       }
       if (!validated) {
         throw new com.sabre.security.jndi.AuthenticationException(JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
       }
   }

   /**
    * Check whether the credentials presented by the user match those
    * retrieved from the directory.
    *
    * @param info        The User to be authenticated
    * @param credentials Authentication credentials
    * @throws javax.naming.NamingException if a directory server error occurs
    */
   protected boolean compareCredentials(User info,
                                        String credentials)
         throws NamingException {

      if (info == null || credentials == null) {
         return false;
      }

      String password = info.password;
      if (password == null) {
         return false;
      }

      // Validate the credentials specified by the user
      if (debug >= 3) {
         log("  validating credentials");
      }

      boolean validated = false;
      if (hasMessageDigest()) {
         // Hex hashes should be compared case-insensitive
         if (Base64.isBase64(password.substring(5))) {
            password = HexUtils.convert(Base64.decode(password.substring(5).getBytes()));
         }
         validated = digest(credentials).equalsIgnoreCase(password);
      } else {
         validated = digest(credentials).equals(password);
      }
      return validated;
   }

   protected String digest(String credentials) {
      // If no MessageDigest instance is specified, return unchanged
      if (!hasMessageDigest()) {
         return credentials;
      }

      // Digest the user credentials and return as hexadecimal
      synchronized (this) {
         try {
            messageDigest.reset();
            messageDigest.update(credentials.getBytes());
            return HexUtils.convert(messageDigest.digest());
         }
         catch (Exception e) {
            log("realmBase.digest", e);
            return credentials;
         }
      }
   }

   /**
    * Close any open connection to the directory server for this Realm.
    */
   public void closeContext() {

      // Do nothing if there is no opened connection
      if (context == null) {
         return;
      }

      // Close our opened connection
      try {
         if (debug >= 1) {
            log("Closing directory context");
         }
         context.close();
      }
      catch (NamingException e) {
         log("jndiRealm.close", e);
      }
//      this.context = null;

   }

   /**
    * Open (if necessary) and return a connection to the configured
    * directory server for this Realm.
    *
    * @throws com.sabre.security.jndi.AuthenticationException if a directory server error occurs
    */
   public void open() throws com.sabre.security.jndi.AuthenticationException {

      open(null, null);

   }

   public void open(String username, String credentials) throws com.sabre.security.jndi.AuthenticationException {

      try {

         // Ensure that we have a directory context available
         context = new InitialDirContext(getDirectoryContextEnvironment(username, credentials));

      }
      catch (NamingException e) {

         try {

         // log the first exception.
         log("jndiRealm.exception", e);

         // Try connecting once more.
         context = new InitialDirContext(getDirectoryContextEnvironment(username, credentials));

         } catch (CommunicationException ex) {
               log("jndiRealm.exception", ex);
               closeContext();
               throw new com.sabre.security.jndi.AuthenticationException(JNDIAuthenticator.MESSAGE_COMMUNICATION_ERROR_KEY);
          } catch (NamingException ne) {
               throw new com.sabre.security.jndi.AuthenticationException(JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
          }
      }

   }

   public boolean isFixedUserConnection() {
      return connectionUser != null && connectionPassword != null;
   }

   private boolean hasMessageDigest() {
      return messageDigest != null;
   }

   private void log(String s) {
      log.info(s);
   }

   private void log(String s, Throwable ex) {
      log.info(s, ex);
   }

}

/*
 *
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */


package com.technoetic.xplanner.security.module.jndi;


import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.sabre.security.jndi.JNDIAuthenticator;
import com.sabre.security.jndi.JNDIAuthenticatorImpl;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.LoginModule;
import com.technoetic.xplanner.security.module.LoginSupport;
import com.technoetic.xplanner.security.module.LoginSupportImpl;


/**
 * <p>Implementation of <strong>Realm</strong> that works with a directory
 * server accessed via the Java Naming and Directory Interface (JNDI) APIs.
 * The following constraints are imposed on the data structure in the
 * underlying directory server:</p>
 * <ul>
 * <p/>
 * <li>Each user that can be authenticated is represented by an individual
 * element in the top level <code>DirContext</code> that is accessed
 * via the <code>connectionURL</code> property.</li>
 * <p/>
 * <li>If a socket connection can not be made to the <code>connectURL</code>
 * an attempt will be made to use the <code>alternateURL</code> if it
 * exists.</li>
 * <p/>
 * <li>Each user element has a distinguished name that can be formed by
 * substituting the presented username into a pattern configured by the
 * <code>userPattern</code> property.</li>
 * <p/>
 * <li>Alternatively, if the <code>userPattern</code> property is not
 * specified, a unique element can be located by searching the directory
 * context. In this case:
 * <ul>
 * <li>The <code>userSearch</code> pattern specifies the search filter
 * after substitution of the username.</li>
 * <li>The <code>userBase</code> property can be set to the element that
 * is the base of the subtree containing users.  If not specified,
 * the search base is the top-level context.</li>
 * <li>The <code>userSubtree</code> property can be set to
 * <code>true</code> if you wish to search the entire subtree of the
 * directory context.  The default value of <code>false</code>
 * requests a search of only the current level.</li>
 * </ul>
 * </li>
 * <p/>
 * <li>The user may be authenticated by binding to the directory with the
 * username and password presented. This method is used when the
 * <code>userPassword</code> property is not specified.</li>
 * <p/>
 * <li>The user may be authenticated by retrieving the value of an attribute
 * from the directory and comparing it explicitly with the value presented
 * by the user. This method is used when the <code>userPassword</code>
 * property is specified, in which case:
 * <ul>
 * <li>The element for this user must contain an attribute named by the
 * <code>userPassword</code> property.
 * <li>The value of the user password attribute is either a cleartext
 * String, or the result of passing a cleartext String through the
 * <code>RealmBase.digest()</code> method (using the standard digest
 * support included in <code>RealmBase</code>).
 * <li>The user is considered to be authenticated if the presented
 * credentials (after being passed through
 * <code>RealmBase.digest()</code>) are equal to the retrieved value
 * for the user password attribute.</li>
 * </ul></li>
 * <p/>
 * <li>Each group of users that has been assigned a particular role may be
 * represented by an individual element in the top level
 * <code>DirContext</code> that is accessed via the
 * <code>connectionURL</code> property.  This element has the following
 * characteristics:
 * <ul>
 * <li>The set of all possible groups of interest can be selected by a
 * search pattern configured by the <code>roleSearch</code>
 * property.</li>
 * <li>The <code>roleSearch</code> pattern optionally includes pattern
 * replacements "{0}" for the distinguished name, and/or "{1}" for
 * the username, of the authenticated user for which roles will be
 * retrieved.</li>
 * <li>The <code>roleBase</code> property can be set to the element that
 * is the base of the search for matching roles.  If not specified,
 * the entire context will be searched.</li>
 * <li>The <code>roleSubtree</code> property can be set to
 * <code>true</code> if you wish to search the entire subtree of the
 * directory context.  The default value of <code>false</code>
 * requests a search of only the current level.</li>
 * <li>The element includes an attribute (whose name is configured by
 * the <code>roleName</code> property) containing the name of the
 * role represented by this element.</li>
 * </ul></li>
 * <p/>
 * <li>In addition, roles may be represented by the values of an attribute
 * in the user's element whose name is configured by the
 * <code>userRoleName</code> property.</li>
 * <p/>
 * <li>Note that the standard <code>&lt;security-role-ref&gt;</code> element in
 * the web application deployment descriptor allows applications to refer
 * to roles programmatically by names other than those used in the
 * directory server itself.</li>
 * </ul>
 *
 * @author John Holman
 * @author Craig R. McClanahan
 * @version $Revision: 799 $ $Date: 2005-10-25 04:39:39 -0500 (Tue, 25 Oct 2005) $
 */

public class JNDILoginModule implements LoginModule {

   JNDIAuthenticator authenticator = null;
   LoginSupport support = null;

   public static final  Logger log = Logger.getLogger(JNDILoginModule.class);
   /**
    * Descriptive information about this Realm implementation.
    */
   protected String name = null;
   /**
    * Should we dereference directory aliases?
    * See http://java.sun.com/products/jndi/tutorial/ldap/misc/aliases.html
    */
   protected String derefAliases = "always";

   public JNDILoginModule(JNDIAuthenticator authenticator, LoginSupport support) {
      this.authenticator = authenticator;
      this.support = support;
   }

   public JNDILoginModule() {
      this.authenticator = new JNDIAuthenticatorImpl();
      this.support = new LoginSupportImpl();
   }

   public void setOptions(Map options) {
      authenticator.setOptions(options);
   }

   public Subject authenticate(String userId, String password) throws AuthenticationException {
      Subject subject = null;
      log.debug(ATTEMPTING_TO_AUTHENTICATE + this.getName() + " (" + userId + ")");
      try {
         subject = authenticator.authenticate(userId, password);
      } catch (com.sabre.security.jndi.AuthenticationException e) {
         throw new AuthenticationException(e.getMessage());
      }
      support.populateSubjectPrincipalFromDatabase(subject, userId);
      log.debug(AUTHENTICATION_SUCCESFULL + this.getName());
      return subject;
   }

   public boolean isCapableOfChangingPasswords() {
      return false;
   }

   public void changePassword(String userId, String password) throws AuthenticationException {
      throw new UnsupportedOperationException("change Password not implemented");
   }

   public void logout(HttpServletRequest request) throws AuthenticationException {
      request.getSession().invalidate();
   }

   public void setName(String name) {
      this.name = name;
   }

   /**
    * Return a short name for this Realm implementation.
    */
   public String getName() {
      return this.name;
   }
}

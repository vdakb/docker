/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   GlassFish Server Security Realm

    File        :   Asserter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Asserter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.jaas;

import java.lang.ref.Reference;

import java.lang.reflect.Field;
import java.lang.reflect.Array;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import java.security.Principal;

import javax.security.auth.Subject;

import javax.security.auth.spi.LoginModule;

import javax.security.auth.callback.CallbackHandler;

import javax.security.auth.login.LoginException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.sun.enterprise.security.auth.realm.Realm;

import com.sun.enterprise.security.auth.login.common.PasswordCredential;

import java.util.Arrays;

import org.glassfish.security.common.Group;
import org.glassfish.security.common.PrincipalImpl;

/**
 ** <code>Asserter</code> describes the interface implemented by authentication
 ** technology providers. <code>Asserter</code> modules are plugged in under
 ** applications to provide a particular type of authentication.
 ** <p>
 ** While applications write to the <code>LoginContext</code> API,
 ** authentication technology providers implement the {@code LoginModule}
 ** interface. A {@code Configuration} specifies the <code>Asserter</code>
 ** module(s) to be used with a particular login application. Therefore
 ** different <code>Asserter</code> modules can be plugged in under the
 ** application without requiring any modifications to the application itself.
 ** <p>
 ** The <code>LoginContext</code> is responsible for reading the
 ** <code>Configuration</code> and instantiating the appropriate
 ** <code>Asserter</code> modules. Each <code>Asserter</code> module is
 ** initialized with a {@link Subject}, a {@link CallbackHandler}, shared
 ** <code>Asserter</code> module state, and <code>Login</code> module-specific
 ** options.
 ** <p>
 ** The {@link Subject} represents the {@link Subject} currently being
 ** authenticated and is updated with relevant Credentials if authentication
 ** succeeds. <code>Asserter</code> modules use the {@link CallbackHandler} to
 ** communicate with users. The {@link CallbackHandler} may be used to prompt
 ** for usernames and passwords, for example.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** The {@link CallbackHandler} may be <code>null</code>. <code>Asserter</code>
 ** modules which absolutely require a {@link CallbackHandler} to authenticate
 ** the {@link Subject} may throw a {@link LoginException}.
 ** <br>
 ** <code>Asserter</code> modules optionally use the shared state to share
 ** informationor data among themselves.
 ** <p>
 ** The <code>Asserter</code> module-specific options represent the options
 ** configured for this <code>Asserter</code> module by an administrator or user
 ** in the login <code>Configuration</code>.
 ** <br>
 ** The options are defined by the <code>Login</code> module itself and control
 ** the behavior within it. For example, a <code>Asserter</code> module may
 ** define options to support debugging/testing capabilities. Options are
 ** defined using a key-value syntax, such as <i>debug=true</i>. The
 ** <code>Asserter</code> module stores the options as a {@link Map} so that the
 ** values may be retrieved using the key.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** There is no limit to the number of options a <code>Asserter</code> module
 ** chooses to define.
 ** <p>
 ** The calling application sees the authentication process as a single
 ** operation. However, the authentication process within the
 ** <code>Asserter</code> module proceeds in two distinct phases.
 ** <br>
 ** In the first phase, the <code>Asserter</code> module's
 ** {@link #login() login} method gets invoked by the <code>Asserter</code>
 ** module's {@link #login() login} method. The {@link #login() login} method
 ** for the <code>Asserter</code> module then performs the actual authentication
 ** (prompt for and verify a token for example) and saves its authentication
 ** status as private state information. Once finished, the
 ** <code>Asserter</code> module's {@link #login() login} method either returns
 ** <code>true</code> (if it succeeded) or <code>false</code> (if it should be
 ** ignored), or throws a {@link LoginException} to specify a failure.
 ** <br>
 ** In the failure case, the <code>Asserter</code> module must not retry the
 ** authentication or introduce delays. The responsibility of such tasks belongs
 ** to the application. If the application attempts to retry the authentication,
 ** the <code>Asserter</code> module's {@link #login() login} method will be
 ** called again.
 ** <p>
 ** In the second phase, if the <code>LoginContext</code>'s overall
 ** authentication succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and
 ** OPTIONAL <code>Asserter</code> module succeeded), then the
 ** {@link #commit() commit} method for the <code>Asserter</code> module gets
 ** invoked.
 ** <br>
 ** The {@link #commit() commit} method for a <code>Asserter</code> module
 ** checks its privately saved state to see if its own authentication succeeded.
 ** <br>
 ** If the overall <code>LoginContext</code> authentication succeeded and the
 ** <code>Asserter</code> module's own authentication succeeded, then the
 ** {@link #commit() commit} method associates the relevant Principals
 ** (authenticated identities) and Credentials (authentication data such as
 ** cryptographic keys) with the {@link Subject} located within the
 ** <code>Asserter</code> module.
 ** <p>
 ** If the <code>LoginContext</code>'s overall authentication failed (the
 ** relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL <code>Asserter</code>
 ** module did not succeed), then the {@link #abort() abort} method for each
 ** <code>Asserter</code> module gets invoked. In this case, the
 ** <code>Asserter</code> module removes/destroys any authentication state
 ** originally saved.
 ** <p>
 ** Logging out a {@link Subject} involves only one phase.
 ** <br>
 ** The <code>LoginContext</code> invokes the <code>Asserter</code> module's
 ** {@link #logout() logout} method.  The {@link #logout() logout} method for
 ** the <code>Asserter</code> module then performs the logout procedures, such
 ** as removing Principals or Credentials from the {@link Subject} or logging
 ** session information.
 ** <p>
 ** A <code>Asserter</code> module implementation must have a constructor with
 ** no arguments. This allows classes which load the <code>Asserter</code>
 ** module to instantiate it.
 ** <p>
 ** <b>Note:</b>
 ** <br>
 ** Since a {@link LoginModule} is always invoked within an
 ** <code>AccessController.doPrivileged</code> call, it should not have to call
 ** <code>doPrivileged</code> itself. If it does, it may inadvertently open up a
 ** security hole. For example, a {@link LoginModule} that invokes the
 ** application-provided {@link CallbackHandler} inside a
 ** <code>doPrivileged</code> call opens up a security hole by permitting the
 ** application's {@link CallbackHandler} to gain access to resources it would
 ** otherwise not have been able to access.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Asserter implements LoginModule {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PARAMETER_TYPE         = "type";
  private static final String PARAMETER_NAME         = "name";

  private static final String PARAMETER_TYPE_DEFAULT = "header";
  private static final String PARAMETER_NAME_DEFAULT = "oam_remote_user";

  /**
   ** Query parameter used to pass Bearer token
   **
   ** @see <a href="https://tools.ietf.org/html/rfc6750#section-2.3">The OAuth 2.0 Authorization Framework: Bearer Token Usage</a>
   */
  public static final String TOKEN                  = "access_token";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The current subject that we will enrich with the principals in case of a
   ** successful login.
   */
  protected Subject          subject                = null;
  protected Type             type                   = null;
  protected String           header                 = null;

  /**
   ** The identity to authenticate by this particular {@link LoginModule}.
   */
  protected String           identity               = null;
  /**
   ** The permissions to authorize the principal.
   */
  protected String[]         permission             = null;
  /**
   ** The principal to authenticate by this particular {@link LoginModule}.
   */
  protected PrincipalImpl    userPrincipal          = null;

  protected Realm            currentRealm;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** The type constraint of the asserter feature.
   */
  public enum Type {
      /**
       ** The constraint value if the identity is passed by a HTTP Header.
       */
      HEADER("header")
      /**
       ** The constraint value if the identity is passed by a HTTP Cookie.
       */
    , COOKIE("cookie")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the lower case string value for this asserter type. */
    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Type</code> with a constraint value.
     **
     ** @param  id               the asserter type type of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Type(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value of the asserter type constraint.
     **
     ** @return                  the value of the asserter type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Type</code> constraint from the
     ** given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     */
    public static Type of(final String value) {
      for (Type cursor : Asserter.Type.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Asserter</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public Asserter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (LoginModule)
  /**
   ** Initialize this {@link LoginModule}.
   ** <p>
   ** This method is called (once!) by the <code>LoginContext</code> after this
   ** {@link LoginModule} has been instantiated.
   ** <br>
   ** The purpose of this method is to initialize this {@link LoginModule} with
   ** the relevant information.
   ** <br>
   ** If this {@link LoginModule} does not understand any of the data stored in
   ** <code>shared</code> or <code>option</code> parameters, they can be
   ** ignored.
   **
   ** @param  subject            the {@link Subject} this login attempt will
   **                            populate.
   **                            <br>
   **                            Allowed object is {@link Subject}.
   ** @param  handler            a {@link CallbackHandler} that can be used to
   **                            get the username and in authentication mode,
   **                            the user's credential.
   **                            <br>
   **                            Allowed object is {@link CallbackHandler}.
   ** @param  shared             the state shared with between login modules
   **                            when there are multiple authenticators
   **                            configured.
   **                            <br>
   **                            The current module does not use this parameter.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   ** @param  option             the options specified in the login
   **                            {@code Configuration} for this particular
   **                            {@link LoginModule}.
   **                            <br>
   **                            For example, it can be used to pass in
   **                            configuration data (where is the database
   **                            holding user and group info) and to pass in
   **                            whether the login module is used for
   **                            authentication or to complete identity
   **                            assertion.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   */
  @Override
  public void initialize(final Subject subject, final CallbackHandler handler, final Map<String, ?> shared, final Map<String, ?> option) {
    this.subject = subject;
    this.type    = Type.of(stringProperty(option, PARAMETER_TYPE, PARAMETER_TYPE_DEFAULT));
    this.header  = stringProperty(option, PARAMETER_NAME, PARAMETER_NAME_DEFAULT);

    PasswordCredential credential = null;
    final Iterator<Object> cursor = this.subject.getPrivateCredentials().iterator();
    while (cursor.hasNext() && credential == null) {
      final Object entry = cursor.next();
      if (entry instanceof PasswordCredential)
        credential = (PasswordCredential)entry;
    }
    // need to obtain the requested realm to get parameters
    try {
      this.currentRealm = Realm.getInstance(credential.getRealm());
    }
    catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (LoginModule)
  /**
   ** Method to commit the authentication process (phase 2).
   ** <p>
   ** This method is called if the LoginContext's overall authentication
   ** succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
   ** <code>Asserter</code>s succeeded).
   ** <p>
   ** If this <code>Asserter</code>s own authentication attempt succeeded
   ** (checked by retrieving the private state saved by the <code>login</code>
   ** method), then this method associates relevant {@code Principal}s and with
   ** the {@link Subject} located in the <code>Asserter</code>.
   ** <br>
   ** If this <code>Asserter</code>'s own authentication attempted failed, then
   ** this method removes/destroys any state that was originally saved.
   **
   ** @return                    <code>true</code> if this method succeeded, or
   **                            <code>false</code> if this
   **                            <code>Asserter</code> should be ignored.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws LoginException     if the commit fails.
   */
  @Override
  public boolean commit()
    throws LoginException {

    // add a Principal (authenticated identity) to the Subject
    // assume the user we authenticated is the PrincipalImpl [RI]
    this.userPrincipal = new PrincipalImpl(this.identity);
    final Set<Principal> principals = this.subject.getPrincipals();
    if (!principals.contains(this.userPrincipal))
      principals.add(this.userPrincipal);

    // populate the group in the subject and clean out the slate at the same
    // time
    for (int i = 0; i < this.permission.length; i++) {
      if (this.permission[i] != null) {
        Group group = new Group(this.permission[i]);
        if (!principals.contains(group))
          principals.add(group);
        // cleaning the slate
        this.permission[i] = null;
      }
    }
    // in any case, clean out state.
    this.permission = null;
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort (LoginModule)
  /**
   ** Method to abort the authentication process (phase 2).
   ** <p>
   ** This method is called if the LoginContext's overall authentication failed
   ** (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
   ** {@link LoginModule}s did not succeed).
   ** <p>
   ** If this {@link LoginModule}'s own authentication attempt succeeded
   ** (checked by retrieving the private state saved by the <code>login</code>
   ** method), then this method cleans up any state that was originally saved.
   **
   ** @return                    <code>true</code> if this method succeeded, or
   **                            <code>false</code> if this {@link LoginModule}
   **                            should be ignored.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws LoginException     if the abort fails.
   */
  @Override
  public final boolean abort()
    throws LoginException {

    for (int i = 0; i < this.permission.length; i++) {
      this.permission[i] = null;
    }
    this.permission = null;

    // overall authentication succeeded and commit succeeded, but someone else's
    // commit failed
    logout();
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logout (LoginModule)
  /**
   ** Method which logs out a {@link Subject}.
   ** <p>
   ** An implementation of this method might remove/destroy a Subject's
   ** {@code Principal}s.
   **
   ** @return                    <code>true</code> if this method succeeded, or
   **                            <code>false</code> if this {@link LoginModule}
   **                            should be ignored.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws LoginException     if the logout fails.
   */
  @Override
  public final boolean logout()
    throws LoginException {

    this.subject.getPrincipals().clear();
    this.subject.getPublicCredentials().clear();
    this.subject.getPrivateCredentials().clear();

    this.identity = null;
    return true;
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticateUser
  /**
   ** Perform authentication decision.
   ** <p>
   ** Method returns silently on success and returns a {@link LoginException} on
   ** failure.
   **
   ** @throws LoginException     on authentication failure.
   */
  protected abstract void authenticateUser()
    throws LoginException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitUserAuthentication
  /**
   ** This is a convenience method which can be used by subclasses
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This method is called after the authentication has succeeded. If
   ** authentication failed do not call this method.
   ** <p>
   ** Global instance field succeeded is set to true by this method.
   **
   ** @param  permission         an array of group memberships for user (could
   **                            be empty).
   */
  public final void commitUserAuthentication(final String[] permission) {
    // copy the groups into a new array before storing it in the instance
    this.permission = (permission == null) ? null : Arrays.copyOf(permission, permission.length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertIdentity
  /**
   ** Asserts the user identity based on the private credentials injected
   ** either by HTTP trusted authentication mechanism.
   **
   ** @return                    <code>true</code> if the credentials provided
   **                            could be matched.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected boolean assertIdentity() {
    final HttpServletRequest request = servletRequest();
    switch (this.type) {
      case COOKIE : for (Cookie cursor : request.getCookies()) {
                      if (cursor.getName().equalsIgnoreCase(this.header)) {
                        this.identity = cursor.getValue();
                        break;
                      }
                    }
                    break;
      default     : this.identity = request.getHeader(this.header);
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   servletRequest
  /**
   ** Returns the {@link HttpServletRequest} object that initiated the call to
   ** this module.
   **
   **  @return                   the {@link HttpServletRequest} for this
   **                            request, returns <code>null</code> if the
   **                            {@link HttpServletRequest} object could not be
   **                            obtained.
   */
  static HttpServletRequest servletRequest() {
    try {
      // obtain a reference to the thread locals table of the current thread
      final Thread thread = Thread.currentThread();
      final Field  store  = Thread.class.getDeclaredField("threadLocals");
      store.setAccessible(true);
      Object threadLocalTable = store.get(thread);

      // obtain a reference to the array holding the thread local variables
      // inside the ThreadLocalMap of the current thread
      final Class map = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
      final Field tab = map.getDeclaredField("table");
      tab.setAccessible(true);
      Object table = tab.get(threadLocalTable);

      // the key to the ThreadLocalMap is a WeakReference object
      // the referent field of this object is a reference to the actual
      // ThreadLocal variable
      final Field referentField = Reference.class.getDeclaredField("referent");
      referentField.setAccessible(true);
      
      for (int i = 0; i < Array.getLength(table); i++) {
        // each entry in the table array of ThreadLocalMap is an Entry object
        // representing the thread local reference and its value
        Object entry = Array.get(table, i);
        if (entry != null) {
          // obtain a reference to the thread local object and remove it from
          // the table
          ThreadLocal local = (ThreadLocal)referentField.get(entry);
          Object wrapper = local.get();
          System.out.println("i: --> " + i + " :: " + wrapper);
          if (wrapper instanceof HttpServletRequest)
            return (HttpServletRequest)wrapper;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringProperty
  /**
   ** Obtain a property from the specified {@link Map} by its
   ** <code>name</code>.
   **
   ** @param  store              the store providing the properties.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} and any type
   **                            for the value.
   ** @param  name               the name of the property from the store.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the property value mapped at <code>name</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if the configuration parameters identify
   **                                  a corrupt realm.
   */
  static String stringProperty(final Map<String, ?> store, final String name)
    throws IllegalArgumentException {

    final String value = stringProperty(store, name, null);
    if (value == null)
      throw new IllegalArgumentException(name);
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringProperty
  /**
   ** Obtain a property from the specified {@link Map} by its
   ** <code>name</code>.
   **
   ** @param  store              the store providing the properties.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} and any type
   **                            for the value.
   ** @param  name               the name of the property from the store.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       a default value in case no value is mapped at
   **                            <code>name</code> in the property
   **                            <code>store</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the property value mapped at <code>name</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if the configuration parameters identify
   **                                  a corrupt realm.
   */
  static String stringProperty(final Map<String, ?> store, final String name, final String defaultValue)
    throws IllegalArgumentException {

    final String value = String.class.cast(store.get(name));
    return (value == null) ? defaultValue : value;
  }
}
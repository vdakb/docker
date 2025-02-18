/*
    Oracle Deutschland GmbH

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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Resource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.access.common.spi.schema.LogInUrls;
import oracle.iam.access.common.spi.schema.LogOutUrls;
import oracle.iam.access.common.spi.schema.PublicResourcesList;
import oracle.iam.access.common.spi.schema.ExcludedResourcesList;
import oracle.iam.access.common.spi.schema.NotEnforcedUrls;
import oracle.iam.access.common.spi.schema.ProtectedResourcesList;

////////////////////////////////////////////////////////////////////////////////
// class Resource
// ~~~~~ ~~~~~~~~
/**
 ** <code>Resource</code> specifies the resource URL's that you want the
 ** <code>Access Agent</code> to protect with some authentication scheme or want
 ** to keep public (not protected by the <code>Access Agent</code>).
 ** The resource URLs should be relative paths to the agentBaseUrl.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resource extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String value;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Protected
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Protected</code> specifies the resource URL's that you want
   ** the <code>Access Agent</code> to protect with some authentication scheme.
   ** The resource URLs should be relative paths to the agentBaseUrl.
   */
  public static class Protected extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final ProtectedResourcesList delegate = factory.createProtectedResourcesList();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Protected</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Protected() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link ProtectedResourcesList} delegate of configured
     ** <code>Access Server</code>s.
     **
     ** @return                    the {@link ProtectedResourcesList} delegate.
     */
    public final ProtectedResourcesList delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURL
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** <code>Access Server</code>.
     **
     ** @param  url              the {@link Resource} instance to add.
     */
    public void addConfiguredURL(final Resource url) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending resource property
      this.delegate.getResource().add(url.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Public
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Public</code> specifies the resource URL's that you want to keep
   ** public (not protected by the <code>Access Agent</code>). The resource URLs
   ** should be relative paths to the agentBaseUrl. For instance, you might want
   ** to specify the Home page or the Welcome page of your application
   */
  public static class Public extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final PublicResourcesList delegate = factory.createPublicResourcesList();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Public</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Public() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link PublicResourcesList} delegate of configured
     ** <code>Access Server</code>s.
     **
     ** @return                    the {@link PublicResourcesList} delegate.
     */
    public final PublicResourcesList delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURL
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** <code>Access Server</code>.
     **
     ** @param  url              the {@link Resource} instance to add.
     */
    public void addConfiguredURL(final Resource url) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending resource property
      this.delegate.getResource().add(url.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Excluded
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Excluded</code> specifies the resource URL's that you want to keep
   ** public (not protected by the <code>Access Agent</code>). The resource URLs
   ** should be relative paths to the agentBaseUrl. For instance, you might want
   ** to specify the Home page or the Welcome page of your application
   */
  public static class Excluded extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final ExcludedResourcesList delegate = factory.createExcludedResourcesList();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Excluded</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Excluded() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link ExcludedResourcesList} delegate of configured
     ** <code>Access Server</code>s.
     **
     ** @return                    the {@link ExcludedResourcesList} delegate.
     */
    public final ExcludedResourcesList delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURL
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** <code>Access Server</code>.
     **
     ** @param  url              the {@link Resource} instance to add.
     **
     ** @throws BuildException   if this instance is referencing a predefined
     **                          element.
     */
    public void addConfiguredURL(final Resource url)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending resource property
      this.delegate.getResource().add(url.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class NotEnforced
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** <code>NotEnforced</code> URLs have no policy enforcement. These equate to
   ** {@link Public} URLs, with no protection and access is allowed by all.
   ** <p>
   ** This is specific to OpenSSO <code>Access Agent</code>s.
   */
  public static class NotEnforced extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final NotEnforcedUrls delegate = factory.createNotEnforcedUrls();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>NotEnforced</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public NotEnforced() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link NotEnforcedUrls} delegate of configured
     ** <code>Access Server</code>s.
     **
     ** @return                    the {@link NotEnforcedUrls} delegate.
     */
    public final NotEnforcedUrls delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURL
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** <code>Access Server</code>.
     **
     ** @param  url              the {@link Resource} instance to add.
     */
    public void addConfiguredURL(final Resource url) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending resource property
      this.delegate.getUrl().add(url.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Login
  // ~~~~~ ~~~~~
  /**
   ** <code>Login</code> specifies the resource URL's for login, which must
   ** include the appropriate protocol (HTTP or HTTPS), host, domain, and port
   ** in the following form:
   ** <br>
   ** <code>http://example.domain.com:port</code>
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The port number is optional.
   */
  public static class Login extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final LogInUrls delegate = factory.createLogInUrls();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Login</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Login() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link Login} delegate of configured
     ** <code>Login URL</code>s.
     **
     ** @return                    the {@link LogInUrls} delegate.
     */
    public final LogInUrls delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURL
    /**
     ** Call by the ANT deployment to inject the argument for adding a url.
     **
     ** @param  url              the {@link Resource} instance to add.
     **
     ** @throws BuildException   if this instance is referencing a predefined
     **                          element.
     */
    public void addConfiguredURL(final Resource url)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending login property
      this.delegate.getUrl().add(url.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Logout
  // ~~~~~ ~~~~~~
  /**
   ** <code>Logout</code> specifies the logout URL's which triggers the logout
   ** handler, which removes the cookie (<b><code>OAMAuthnCookie</code></b> for
   ** <code>Access Agent</code>s) and requires the user to re-authenticate the
   ** next time he accesses a resource protected by <code>Access Manager</code>.
   ** <ul>
   **   <li>If there is a match, the <code>Access Agent</code> logout handler is
   **       triggered.
   **   <li>If Logout URL is not configured the request URL is checked for
   **       "<code>logout.</code>" and, if found (except
   **       "<code>logout.gif</code>" and "<code>logout.jpg</code>"), also
   **       triggers the logout handler.
   ** </ul>
   */
  public static class Logout extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final LogOutUrls delegate = factory.createLogOutUrls();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Logout</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Logout() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link Logout} delegate of configured
     ** <code>Logout URL</code>s.
     **
     ** @return                    the {@link LogOutUrls} delegate.
     */
    public final LogOutUrls delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURL
    /**
     ** Call by the ANT deployment to inject the argument for adding a url.
     **
     ** @param  url              the {@link Resource} instance to add.
     **
     ** @throws BuildException   if this instance is referencing a predefined
     **                          element.
     */
    public void addConfiguredURL(final Resource url)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending logout property
      this.delegate.getUrl().add(url.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resource() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   ** Call by the ANT kernel to inject the argument for value.
   **
   ** @param  value            the value for the object wrapper.
   */
  public void setValue(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the object.
   **
   ** @return                    the value for the object wrapper.
   */
  public final String value() {
    return this.value;
  }
}
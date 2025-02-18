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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   SecurityPrincipal.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityPrincipal.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class SecurityPrincipal
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>SecurityPrincipal</code> defines the {@link SecurityRole} that can
 ** be passed as an attribute to all subclasses of {@link ServerContext}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SecurityPrincipal extends SecurityRole {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String TYPE     = "principal";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             password = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityPrincipal</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SecurityPrincipal() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityPrincipal</code> with the specified username.
   **
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public SecurityPrincipal(final String username) {
    // ensure inheritance
    this(username, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityPrincipal</code> with the specified username.
   **
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public SecurityPrincipal(final String username, final String password) {
    // ensure inheritance
    this(username, password, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityPrincipal</code> with the specified username.
   **
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  description        the description of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public SecurityPrincipal(final String username, final String password, final String description) {
    // ensure inheritance
    super(username, description);

    // initialize instance
    this.password    = password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>refid</code>.
   **
   ** @param  reference          the id of this instance.
   **                            <br>
   **                            Allowed object is {@link Reference}.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    // prevent bogus input
    if (this.password != null)
      handleAttributeError("refid");

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof SecurityPrincipal) {
      this.password    = ((SecurityPrincipal)other).password;
      // ensure inheritance
      super.setRefid(reference);
    }
    else
      handleReferenceError(reference, TYPE, other.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername
  /**
   ** Call by the ANT kernel to inject the argument for parameter username.
   **
   ** @param  username           the name of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public final void setUsername(final String username)
    throws BuildException {

    // ensure inheritance
    super.setName(username);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Call by the ANT kernel to inject the argument for parameter password.
   **
   ** @param  password           the password of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public final void setPassword(final String password)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("password");

    this.password = password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the password of the security principal of the server used to
   ** connect to.
   **
   ** @return                    the password of the security principal server
   **                            used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate() {
    // ensure inheritance
    super.validate();

    if (StringUtility.isEmpty(this.password))
      handleAttributeMissing("password");
  }
}
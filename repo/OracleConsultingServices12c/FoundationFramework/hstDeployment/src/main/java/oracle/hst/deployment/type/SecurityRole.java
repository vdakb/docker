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

    File        :   SecurityRole.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityRole.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.ServiceDataType;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class SecurityRole
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>SecurityRole</code> defines the {@link ServiceDataType} that can be
 ** passed as an attribute to all subclasses of {@link ServerContext}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SecurityRole extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String TYPE        = "role";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String             name        = null;
  private String             description = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityRole</code> Ant taskthat allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SecurityRole() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityRole</code> with the specified username.
   **
   ** @param  name               the name of the administrative role.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public SecurityRole(final String name) {
    // ensure inheritance
    this(name, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityRole</code> with the specified username.
   **
   ** @param  name               the name of the administrative role.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  description        the description of the administrative role.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public SecurityRole(final String name, final String description) {
    // ensure inheritance
    super();

    // initialize instance
    this.name        = name;
    this.description = description;
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
    if (this.name != null || this.description != null)
      handleAttributeError("refid");

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof SecurityRole) {
      SecurityRole that = (SecurityRole)other;
      this.name        = that.name;
      this.description = that.description;
      // ensure inheritance
      super.setRefid(reference);
    }
    else
      handleReferenceError(reference, TYPE, other.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT kernel to inject the argument for parameter name.
   **
   ** @param  name               the name of the administrative role.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public final void setName(final String name)
    throws BuildException {

    // prevent bogus input
    checkAttributesAllowed();
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the security principal of the server used to connect
   ** to.
   **
   ** @return                    the name of the security principal server used
   **                            to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT kernel to inject the argument for parameter description.
   **
   ** @param  description        the description of the administrative user.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public final void setDescription(final String description)
    throws BuildException {

    // prevent bogus input
    checkAttributesAllowed();
    this.description = description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the security principal of the server used to
   ** connect to.
   **
   ** @return                    the description of the security principal
   **                            server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
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
    if (StringUtility.isEmpty(this.name))
      handleAttributeMissing("name");
  }
}
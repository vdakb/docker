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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   SystemProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    SystemProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.SystemPropertyInstance;

////////////////////////////////////////////////////////////////////////////////
// class SystemProperty
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>SystemProperty</code> defines the value holder of named value pairs to
 ** be applied on system property definitions of Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemProperty extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final SystemPropertyInstance delegate = new SystemPropertyInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SystemProperty</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SystemProperty() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>ITResource</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(this.delegate.name()) || !StringUtility.isEmpty(this.delegate.name()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  keyword            the key word assigned to a system property in
   **                            Identity Manager to handle.
   */
  public void setName(final String keyword) {
    checkAttributesAllowed();
    this.delegate.name(keyword);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   ** Called to inject the argument for parameter <code>value</code>.
   **
   ** @param  value              the value of the system property in Oracle
   **                            Identity Manager to handle.
   */
  public void setValue(final String value) {
    checkAttributesAllowed();
    this.delegate.value(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description        the description of the parameter in Identity
   **                            Manager.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    this.delegate.description(description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLoginRequired
  /**
   ** Called to inject the argument for parameter <code>login</code>.
   **
   ** @param  loginRequired      <code>true</code> login is required to read
   **                            the system property from Identity Manager;
   **                            otherwise <code>false</code>.
   */
  public void setLoginRequired(final boolean loginRequired) {
    checkAttributesAllowed();
    this.delegate.loginRequired(loginRequired);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link SystemPropertyInstance} delegate of Identity Manager to
   ** handle.
   **
   ** @return                    the {@link SystemPropertyInstance} delegate of
   **                            Identity Manager.
   */
  public final SystemPropertyInstance instance() {
    if (isReference())
      return ((SystemProperty)getCheckedRef()).instance();

    return this.delegate;
  }
}
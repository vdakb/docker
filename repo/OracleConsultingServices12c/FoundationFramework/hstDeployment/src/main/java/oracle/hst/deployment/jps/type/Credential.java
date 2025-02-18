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
    Subsystem   :   Deployment Utilities 12c

    File        :   Credential.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Credential.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.type;

import org.apache.tools.ant.BuildException;

import oracle.security.jps.mas.mgmt.jmx.credstore.PortableCredential;
import oracle.security.jps.mas.mgmt.jmx.credstore.PortablePasswordCredential;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceDataType;

////////////////////////////////////////////////////////////////////////////////
// class Credential
// ~~~~~ ~~~~~~~~~~
public class Credential extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String action;
  private String name;
  private String username;
  private String password;
  private String description;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Credential</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Credential() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAction
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>action</code>.
   **
   ** @param  action             the action to apply on the credential mapping
   **                            in the Oracle WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setAction(final Action action) {
    checkAttributesAllowed();
    this.action = action.getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the action to apply on the credential.
   **
   ** @return                    the action to apply on the credential.
   */
  public final String action() {
    return this.action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the key of the credential mapping in Oracle
   **                            WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the credential to handle.
   **
   ** @return                    the key to associated the credential with
   **                            in Oracle WebLogic Server Domain.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>username</code>.
   **
   ** @param  username           the username associated with the credential
   **                            mapping in Oracle WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setUsername(final String username) {
    checkAttributesAllowed();
    this.username = username;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   username
  /**
   ** Returns the username of the credential to handle.
   **
   ** @return                    the username associated with the credential
   **                            mapping in Oracle WebLogic Server Domain.
   */
  public final String username() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>password</code>.
   **
   ** @param  password           the password associated with the credential
   **                            mapping in Oracle WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setPassword(final String password) {
    checkAttributesAllowed();
    this.password = password;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the username of the credential to handle.
   **
   ** @return                    the password associated with the credential
   **                            mapping in Oracle WebLogic Server Domain.
   */
  public final String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>description</code>.
   **
   ** @param  description        the description of the credential mapping in
   **                            the Oracle WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setDescription(final String description) {
    checkAttributesAllowed();
    this.description = description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the credential mapping in the Oracle WebLogic
   ** Server Domain.
   **
   ** @return                    the description of the credential mapping in
   **                            the Oracle WebLogic Server Domain.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   portable
  /**
   ** Returns a {@link PortableCredential} configured with the properties of
   ** this {@link ServiceDataType}.
   **
   ** @return                    a {@link PortableCredential} configured with
   **                            the properties of this {@link ServiceDataType}.
   **
   ** @throws BuildException     if some data are missing.
   */
  public final PortableCredential portable()
    throws BuildException {

    if (StringUtility.isEmpty(this.username) && !StringUtility.isEmpty(this.password))
      handleAttributeMissing("username");

    if (!StringUtility.isEmpty(this.username) && StringUtility.isEmpty(this.password))
      handleAttributeMissing("password");

    final PortableCredential portable = (StringUtility.isEmpty(username) && StringUtility.isEmpty(password)) ? new PortableCredential() : new PortablePasswordCredential(this.username, this.password.toCharArray(), null, this.description);
    return portable;
  }
}

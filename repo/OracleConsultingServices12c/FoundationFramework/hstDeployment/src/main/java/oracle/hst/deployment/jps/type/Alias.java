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

    File        :   Alias.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Alias.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.jps.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.CredentialStoreHandler;

////////////////////////////////////////////////////////////////////////////////
// class Alias
// ~~~~~ ~~~~~
/**
 ** A credential is uniquely identified by a map name and a key name.
 ** <p>
 ** Typically, the map name corresponds with the name of an application and all
 ** credentials with the same map name define a logical group of credentials,
 ** such as the credentials used by the application.
 ** <p>
 ** All map names in a credential store must be distinct.
 */
public class Alias extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final CredentialStoreHandler.Alias delegate = new CredentialStoreHandler.Alias();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Alias</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Alias() {
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
    this.delegate.action(ServiceOperation.from(action.getValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the bean to manage in Oracle
   **                            WebLogic Server Domain.
   **
   ** @throws BuildException     indicates that refid has to be the only
   **                            attribute if it is set.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link CredentialStoreHandler.Alias} delegate.
   **
   ** @return                    the {@link CredentialStoreHandler.Alias}
   **                            delegate.
   */
  public final CredentialStoreHandler.Alias instance() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCredential
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>credential</code>.
   **
   ** @param  credential         the {@link Credential} to manage in any
   **                            operation.
   **
   ** @throws BuildException     if a {@link Credential} has missing data or is
   **                            already assign to this alias.
   */
  public void addConfiguredCredential(final Credential credential)
    throws BuildException {

    checkChildrenAllowed();
    final CredentialStoreHandler.Credential mapping = new CredentialStoreHandler.Credential(credential.action(), credential.name(), credential.portable());
    this.delegate.add(mapping);
  }
}
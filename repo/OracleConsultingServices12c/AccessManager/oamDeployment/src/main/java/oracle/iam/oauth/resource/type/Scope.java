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

    File        :   Scope.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Scope.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.resource.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.oauth.common.spi.ResourceInstance;

////////////////////////////////////////////////////////////////////////////////
// class Scope
// ~~~~~ ~~~~~
/**
 ** <code>Scope</code> represents scopes of a <code>Resource Server</code>
 ** instance which itself might be created, deleted or configured in Oracle
 ** Access Manager that during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Scope extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final ResourceInstance.Scope delegate = new ResourceInstance.Scope();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Scope</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Scope() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for attribute <code>name</code>.
   **
   ** @param  name               the name of the <code>Scope</code>.
   */
  public final void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription (Overridden)
  /**
   ** Called to inject the argument for attribute <code>description</code>.
   **
   ** @param  description        the description of the <code>Scope</code>.
   */
  @Override
  public final void setDescription(final String description) {
    checkAttributesAllowed();
    this.delegate.description(description);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link ResourceInstance.Scope} delegate of
   ** <code>Resource Server</code> scope to handle.
   **
   ** @return                    the {@link ResourceInstance.Scope}
   **                            delegate.
   */
  final ResourceInstance.Scope delegate() {
    return this.delegate;
  }
}
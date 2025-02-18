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

    File        :   AudienceClaim.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AudienceClaim.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.resource.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.oauth.common.spi.ResourceInstance;

////////////////////////////////////////////////////////////////////////////////
// class AudienceClaim
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>AudienceClaim</code> identifies the audiences for which the OAuth
 ** token is intended. Each principal intended to process the OAuth token must
 ** identify itself with a value in <code>Audience Claim</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AudienceClaim extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final ResourceInstance.AudienceClaim delegate = new ResourceInstance.AudienceClaim();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AudienceClaim</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AudienceClaim() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSubject
  /**
   ** Called to inject the argument for attribute <code>name</code>.
   **
   ** @param  name               the name of the <code>Scope</code>.
   */
  public final void setSubject(final String name) {
    checkAttributesAllowed();
    this.delegate.subject(name);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link ResourceInstance.AudienceClaim} delegate of
   ** <code>Resource Server</code> scope to handle.
   **
   ** @return                    the {@link ResourceInstance.AudienceClaim}
   **                            delegate.
   */
  final ResourceInstance.AudienceClaim delegate() {
    return this.delegate;
  }
}
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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Anonymous Identifier Service

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.pid.app;

import bka.iam.identity.igs.ServiceApplication;

import javax.ws.rs.ApplicationPath;

import oracle.hst.platform.core.utility.CollectionUtility;

import bka.iam.identity.pid.IdentifierEndpoint;

import oracle.hst.platform.rest.provider.ProcessingExceptionMapper;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** Defines the components of a JAX-RS application and supplies additional
 ** metadata.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// It's good practice to include a version number in the path to REST web
// services so you can have multiple versions deployed at once.
// That way consumers don't need to upgrade right away if things are working for
// them.
@ApplicationPath("v1")
public class Service extends ServiceApplication {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Service() {
    // ensure inheritance
    super(CollectionUtility.set(IdentifierEndpoint.class),CollectionUtility.set(ProcessingExceptionMapper.class));
  }
}
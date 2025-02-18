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

    File        :   Initializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Initializer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.pid.app;

import javax.enterprise.context.ApplicationScoped;

import bka.iam.identity.cdi.AssertionStoreDefinition;
import bka.iam.identity.cdi.AssertionMechanismDefinition;

////////////////////////////////////////////////////////////////////////////////
// class Initializer
// ~~~~~ ~~~~~~~~~~~
/**
 ** Initialize the components of a JAX-RS application and supplies additional
 ** metadata.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// to turn this class into a CDI bean
@ApplicationScoped
// this links the application to an token asserter realm
@AssertionStoreDefinition
// enables basic HTTP authentication
// can be replaced by another standard mechanism (form, custom form) or by a
// custom mechanism by implementing HttpAuthenticationMechanism
@AssertionMechanismDefinition
public class Initializer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Initializer</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Initializer() {
    // ensure inheritance
    super();
  }
}
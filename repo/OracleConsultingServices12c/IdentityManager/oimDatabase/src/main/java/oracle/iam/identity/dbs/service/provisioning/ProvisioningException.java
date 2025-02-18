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

    Copyright © 2009. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Oracle Database Account Connector

    File        :   ProvisioningException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProvisioningException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2009-02-01  DSteding    First release version
*/

package oracle.iam.identity.dbs.service.provisioning;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.dbs.resource.ProvisioningBundle;

////////////////////////////////////////////////////////////////////////////////
// final class ProvisioningException
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for thrown from provisioning operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public final class ProvisioningException extends TaskException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1813384151211386704")
  private static final long serialVersionUID = -6144562060604220509L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProvisioningException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   */
  public ProvisioningException(String code) {
    // ensure inheritance
    super(ProvisioningBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProvisioningException</code> from a resource bundle
   ** code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   ** @param  parameter          the substitutions for the placholder contained
   **                            in the message regarding to <code>code</code>.
   */
  public ProvisioningException(final String code, final String parameter) {
    // ensure inheritance
    super(ProvisioningBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProvisioningException</code> from a code and a array
   ** with values for the placeholder contained in the resource string retrieved
   ** for the specified resource code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public ProvisioningException(final String code, final String[] parameter) {
    // ensure inheritance
    super(ProvisioningBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ProvisioningException</code> from a code and a array
   ** with values for the placeholder contained in the resource string retrieved
   ** for the specified resource code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  causing            the causing exception.
   */
  public ProvisioningException(final String code, final Throwable causing) {
    // ensure inheritance
    super(ProvisioningBundle.RESOURCE, code, causing);
  }
}
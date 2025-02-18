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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServerException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityServerException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.IdentityServerBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>IdentityServer</code> operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServerException extends TaskException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5300818048227590382")
  private static final long serialVersionUID = 4419012974201946284L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the exception message.
   */
  public IdentityServerException(final String code) {
    // ensure inheritance
    this(IdentityServerBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   */
  public IdentityServerException(final ListResourceBundle bundle, final String code) {
    // ensure inheritance
    super(bundle, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   ** @param  parameter          the substitutions for the placholder contained
   **                            in the message regarding to <code>code</code>.
   */
  public IdentityServerException(final String code, final String parameter) {
    // ensure inheritance
    this(IdentityServerBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public IdentityServerException(final ListResourceBundle bundle, final String code, final String parameter) {
    // ensure inheritance
    super(bundle, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a code and a array with
   ** values for the placeholder contained in the resource string retrieved for
   ** the specified resource code.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public IdentityServerException(final String code, final String[] parameter) {
    // ensure inheritance
    this(IdentityServerBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public IdentityServerException(final ListResourceBundle bundle, final String code, final String[] parameter) {
    // ensure inheritance
    super(bundle, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public IdentityServerException(final Throwable causing) {
    // ensure inheritance
    this(IdentityServerError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  causing            the causing exception.
   */
  public IdentityServerException(final String code, final Throwable causing) {
    // ensure inheritance
    this(IdentityServerBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> and passes it the causing
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the detail message.
   ** @param  causing            the causing exception.
   */
  public IdentityServerException(final ListResourceBundle bundle, final String code, final Throwable causing) {
    // ensure inheritance
    super(bundle, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a code and passes it
   ** the causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public IdentityServerException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(IdentityServerBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>IdentityServerException</code> from a code and passes it
   ** the causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public IdentityServerException(final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    super(IdentityServerBundle.RESOURCE, code, parameter, causing);
  }
}
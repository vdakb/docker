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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   RequestException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RequestException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.api;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class RequestException
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Base class for all used exception across tiers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RequestException extends Exception {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the resource key to create the localized exception */
  private String            code;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default Constructor
   ** <p>
   ** Create a new <code>RequestException</code> with <code>null</code> as its
   ** detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   */
  protected RequestException() {
    // ensure ineritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public RequestException(final String code) {
    // ensure inheritance
    // ensure inheritance
    this(RequestBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public RequestException(final String code, final String parameter) {
    this(RequestBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  public RequestException(final String code, final Object... parameter) {
    this(RequestBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected RequestException(final ListResourceBundle bundle, final String code) {
    // ensure inheritance
    super(bundle.getString(code));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  protected RequestException(final ListResourceBundle bundle, final String code, final Object... parameter) {
    // ensure inheritance
    super(bundle.formatted(code, parameter));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> and passes it the parent
   ** exception.
   **
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public RequestException(final Throwable causing) {
    this(RequestError.GENERAL, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public RequestException(final String code, final Throwable causing) {
    this(RequestBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public RequestException(final ListResourceBundle bundle, final Throwable causing) {
    // ensure inheritance
    this(bundle, RequestError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public RequestException(final ListResourceBundle bundle, final String code, final Throwable causing) {
    // ensure inheritance
    super(bundle.formatted(code, causing.toString()));

    // set the correct exception chain
    initCause(causing);

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameters associated with the exception
   **                            that occurred.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public RequestException(final String code, final String[] parameter, final Throwable causing) {
    this(RequestBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameters associated with the exception
   **                            that occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public RequestException(final ListResourceBundle bundle, final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle.formatted(code, parameter, causing));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the parameters associated with the exception
   **                            that occurred.
   **                            <br>
   **                            Allowed object is array of{@link String}.
   ** @param  causing            the causing exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public RequestException(final ListResourceBundle bundle, final String code, final Object[] parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle.formatted(code, parameter), causing);

    // store provided code for further processing
    this.code = code;
  }
}
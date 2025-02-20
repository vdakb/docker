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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Frontend Extension
    Subsystem   :   UID Service

    File        :   UIDException.java

    Compiler    :   JDK 1.8

    Author      :   sylver.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    UIDException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-02  SBernet    First release version
*/
package bka.iam.identity.service.uid;

import bka.iam.identity.service.uid.resource.Bundle;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class UIDException
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Base class for all used exception across tiers.
 **
 ** @author  sylver.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class UIDException extends Exception {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  
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
   ** Create a new <code>UIDException</code> with <code>null</code> as its
   ** detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   */
  protected UIDException() {
    // ensure ineritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> from a resource bundle code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the detail message.
   */
  public UIDException(final String code) {
    // ensure inheritance
    this(Bundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public UIDException(final String code, final String parameter) {
    this(Bundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public UIDException(final String code, final Object... parameter) {
    this(Bundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   */
  protected UIDException(final ListResourceBundle bundle, final String code) {
    // ensure inheritance
    super(bundle.getString(code));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> from a code.
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
  protected UIDException(final ListResourceBundle bundle, final String code, final Object... parameter) {
    // ensure inheritance
    super(bundle.formatted(code, parameter));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> and passes it the parent
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public UIDException(final Throwable causing) {
    this(UIDError.GENERAL, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the detail message.
   ** @param  causing            the causing exception.
   */
  public UIDException(final String code, final Throwable causing) {
    this(Bundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  causing            the causing exception.
   */
  public UIDException(final ListResourceBundle bundle, final Throwable causing) {
    // ensure inheritance
    this(bundle, UIDError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the detail message.
   ** @param  causing            the causing exception.
   */
  public UIDException(final ListResourceBundle bundle, final String code, final Throwable causing) {
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
   ** Create a new <code>UIDException</code> and passes it the parent
   ** exception.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public UIDException(final String code, final String[] parameter, final Throwable causing) {
    this(Bundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public UIDException(final ListResourceBundle bundle, final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle.formatted(code, parameter, causing));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>UIDException</code> and passes it the parent
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public UIDException(final ListResourceBundle bundle, final String code, final Object[] parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle.formatted(code, parameter), causing);

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Acccessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Sets the code this exception is related to.
   ** <p>
   ** Makes the exception interpretable by the associated code without parsing
   ** the message.
   **
   ** @param  code               the code this exception is related to.
   */
  protected final void code(final String code) {
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Return the resource key this exception is related to.
   ** <p>
   ** Makes the exception interpretable by the associated code without parsing
   ** the message
   **
   ** @return                    the resource key this exception is related to.
   */
  public final String code() {
    return this.code;
  }
}

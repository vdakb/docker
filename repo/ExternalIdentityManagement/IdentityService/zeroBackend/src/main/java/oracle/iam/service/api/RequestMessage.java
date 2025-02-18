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

    File        :   RequestMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RequestMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.api;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// interface RequestMessage
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RequestMessage implements RequestConstant {
  
  // 00000 - OK
  public static final String OK                             = PREFIX + "00000";

  // 00001 - 00010 system related errors
  public static final String GENERAL                        = PREFIX + "00001";
  public static final String UNHANDLED                      = PREFIX + "00002";
  public static final String ABORT                          = PREFIX + "00003";
  public static final String NOTIMPLEMENTED                 = PREFIX + "00004";

  // 00011 - 00020 method argument related errors
  public static final String ARGUMENT_IS_NULL               = PREFIX + "00011";
  public static final String ARGUMENT_BAD_TYPE              = PREFIX + "00012";
  public static final String ARGUMENT_BAD_VALUE             = PREFIX + "00013";
  public static final String ARGUMENT_SIZE_MISMATCH         = PREFIX + "00014";

  // 00021 - 00030 instance attribute related errors
  public static final String ATTRIBUTE_IS_NULL              = PREFIX + "00021";

  // 00041 - 00050 request related errors
  public static final String REQUEST_APPLICATION_NOTFOUND   = PREFIX + "00045";
  public static final String REQUEST_NAMING_ATTR_NOT_FOUND  = PREFIX + "00046";
  public static final String REQUEST_FAILED                 = PREFIX + "00050";

  // 00051 validation messages
  public static final String NO_NAMING_ATTRIBUTE            = PREFIX + "00051";
  public static final String ATTR_NOT_FOUND                 = PREFIX + "00052";
  public static final String USER_NOT_FOUND                 = PREFIX + "00053";
  public static final String ACCOUNT_NOT_IDENTIFIED         = PREFIX + "00054";
  public static final String ACCOUNT_ALREADY_EXISTS         = PREFIX + "00055";
  public static final String ENTITLEMENT_NOT_UNIQUE         = PREFIX + "00056";
  public static final String ENTITLEMENT_NOT_ASSIGNED       = PREFIX + "00057";
  public static final String ENTITLEMENT_ALREADY_ASSIGNED   = PREFIX + "00058";
  public static final String ACCOUNT_NOT_DISABLED           = PREFIX + "00059";
  public static final String ACCOUNT_ALREADY_DISABLED       = PREFIX + "00060";
  public static final String NAMESPACE_UNKNOWN              = PREFIX + "00061";
  public static final String NAMESPACE_ATTRIBUTE_UNKNOWN    = PREFIX + "00062";
  public static final String NO_REQUIRED_ATTRIBUTE          = PREFIX + "00063";
  public static final String NO_NAMESPACE_ENT_ATTRIBUTE     = PREFIX + "00064";
  public static final String REQUEST_PENDING                = PREFIX + "00065";
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the resource key to create the localized message */
  private String            code;

  /** the resource key to fill the localized message placeholders */
  private Object[]          parameters;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default Constructor
   ** <p>
   ** Create a new <code>RequestMessage</code> with <code>null</code> as its
   ** detail message.
   */
  protected RequestMessage() {
    // ensure ineritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestMessage</code> from a resource bundle code.
   **
   ** @param  code               the resource key for the detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public RequestMessage(final String code) {
    // ensure inheritance
    // ensure inheritance
    this(RequestBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestMessage</code> from a code.
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
//  public RequestMessage(final String code, final String parameter) {
//    this(RequestBundle.RESOURCE, code, parameter);
//  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestMessage</code> from a code.
   **
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameters         the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  public RequestMessage(final String code, final Object... parameters) {
    this(RequestBundle.RESOURCE, code, parameters);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestMessage</code> from a code.
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
  protected RequestMessage(final ListResourceBundle bundle, final String code) {
    // ensure inheritance
//    super(bundle.getString(code));

    // store provided code for further processing
    this.code = code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RequestMessage</code> from a code.
   ** <p>
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   **                            <br>
   **                            Allowed object is {@link ListResourceBundle}.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameters         the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  protected RequestMessage(final ListResourceBundle bundle, final String code, final Object... parameters) {
    // ensure inheritance
//    super(bundle.formatted(code, parameters));

    // store provided code for further processing
    this.code = code;
    this.parameters = parameters;
  }

  public String code() {
    return this.code;
  }
  public Object[] parameters() {
    return this.parameters;
  }
  public String toString() {
    StringBuffer toString = new StringBuffer("RequestMessage code:").append(this.code);
    toString.append(", parameters: ").append(this.parameters);
    return toString.toString();
  }
}
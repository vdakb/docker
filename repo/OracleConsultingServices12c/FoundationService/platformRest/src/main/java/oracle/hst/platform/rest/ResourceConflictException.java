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

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   ResourceConflictException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceConflictException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

////////////////////////////////////////////////////////////////////////////////
// class ResourceConflictException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>REST</code> operations.
 ** <br>
 ** Signals the specified version number does not match the resource's latest
 ** version number or a Service Provider refused to create a new, duplicate
 ** resource.
 ** <p>
 ** This exception corresponds to HTTP response code 409 CONFLICT.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceConflictException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The error keyword that indicates one or more of the attribute values is
   ** already in use or is reserved.
   */
  public static final String TYPE            = "uniqueness";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:584647022910297299")
  private static final long serialVersionUID = -3819512476893320843L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ResourceConflictException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private ResourceConflictException(final String message) {
    // ensure inheritance
   super(409, TYPE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>ResourceConflictException</code> with
   ** the uniqueness error keyword.
   **
   ** @param  message            the error message for this REST exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ResourceConflictException</code>
   **                            wrapping the HTTP-409 response status.
   **                            Possible object is
   **                            <code>ResourceConflictException</code>.
   */
  public static ResourceConflictException of(final String message) {
    return new ResourceConflictException(message);
  }
}
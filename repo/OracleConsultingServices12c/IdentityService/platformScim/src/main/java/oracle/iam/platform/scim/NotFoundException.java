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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic SCIM Interface

    File        :   NotFoundException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NotFoundException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

////////////////////////////////////////////////////////////////////////////////
// class NotFoundException
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>SCIM</code> operations.
 ** <br>
 ** Signals the specified version number does not match the resource's latest
 ** version number or a Service Provider refused to create a new, duplicate
 ** resource.
 ** <p>
 ** This exception corresponds to HTTP response code 404 NOT FOUND.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NotFoundException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The error keyword that indicates somthing is not permitted.
   */
  private static final String TYPE             = "notFound";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:223473629810944415")
  private static final long   serialVersionUID = -5815020660955268257L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NotFoundException</code> from a message code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  message            the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private NotFoundException(final String message) {
    // ensure inheritance
   super(404, TYPE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>NotFoundException</code> with the
   ** resource identifier <code>resource</code>.
   **
   ** @param  resource           the resource identifier the exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>NotFoundException</code> wrapping the
   **                            HTTP-404 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>NotFoundException</code>.
   */
  public static NotFoundException of(final String resource, final Integer identifier) {
    return new NotFoundException(ServiceBundle.string(ServiceError.RESOURCE_NOT_EXISTS, identifier, resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>NotFoundException</code> with the
   ** resource identifier <code>resource</code>.
   **
   ** @param  resource           the resource identifier the exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>NotFoundException</code> wrapping the
   **                            HTTP-404 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>NotFoundException</code>.
   */
  public static NotFoundException of(final String resource, final Long identifier) {
    return new NotFoundException(ServiceBundle.string(ServiceError.RESOURCE_NOT_EXISTS, identifier, resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>NotFoundException</code> with the
   ** resource identifier <code>resource</code>.
   **
   ** @param  resource           the resource identifier the exception belongs
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  identifier         the error message for this SCIM exception.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link NotFoundException} wrapping the
   **                            HTTP-404 response status.
   **                            <br>
   **                            Possible object is
   **                            <code>NotFoundException</code>.
   */
  public static NotFoundException of(final String resource, final String identifier) {
    return new NotFoundException(ServiceBundle.string(ServiceError.RESOURCE_NOT_EXISTS, identifier, resource));
  }
}
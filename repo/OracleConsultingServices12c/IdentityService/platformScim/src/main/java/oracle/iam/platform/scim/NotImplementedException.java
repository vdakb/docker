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

    File        :   NotImplementedException.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NotImplementedException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

////////////////////////////////////////////////////////////////////////////////
// class NotImplementedException
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>SCIM</code> operations.
 ** <br>
 ** Signals the service provider does not support the request operation; for
 ** example, PATCH.
 ** <p>
 ** This exception corresponds to HTTP response code 501 NOT IMPLEMENTED.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NotImplementedException extends ProcessingException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4736187835615316858")
  private static final long serialVersionUID = 5193323488595302610L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>NotImplementedException</code> with the default
   ** message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   */
  private NotImplementedException() {
    // ensure inheritance
    super(501, null, ServiceBundle.string(ServiceError.REQUEST_METHOD_NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>NotImplementedException</code> with
   ** the not-implemented error keyword.
   **
   ** @return                    the <code>NotImplementedException</code>
   **                            wrapping the HTTP-501 response status.
   **                            Possible object is
   **                            <code>NotImplementedException</code>.
   */
  public static NotImplementedException of() {
    return new NotImplementedException();
  }
}
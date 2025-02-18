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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryBase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryBase.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.context;

import javax.naming.NamingException;

import oracle.jdeveloper.connection.iam.model.DirectoryName;

import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.connection.iam.service.DirectoryException;

//////////////////////////////////////////////////////////////////////////////
// class DirectoryBase
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A namingContext of the connected Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryBase extends DirectoryContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4210253281247621513")
  private static final long serialVersionUID = -7586485134510526837L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryBase</code> that belongs to the
   ** specified service endpoint.
   **
   ** @param  endpoint           the @link DirectoryService} endpoint.
   **
   ** @throws DirectoryException if a name syntax violation is detected.
   */
  private DirectoryBase(final DirectoryService endpoint)
    throws DirectoryException {

    // ensure inheritance
    super(endpoint, null);

    // initialize instance
    try {
      this.name = DirectoryName.build(this.service.serverContext());
    }
    catch (NamingException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DirectoryBase</code> that populates its
   ** data object from the specified {@link DirectoryService}.
   **
   ** @param  endpoint           the {@link DirectoryService} providing access
   **                            to the context.
   **
   ** @return                    the validated <code>DirectoryBase</code>.
   **                            Possible object <code>DirectoryBase</code>.
   **
   ** @throws DirectoryException if a name syntax violation is detected.
   */
  public static DirectoryBase build(final DirectoryService endpoint)
    throws DirectoryException {

    return new DirectoryBase(endpoint);
  }
}
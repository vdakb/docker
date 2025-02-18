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
    Subsystem   :   Identity Manager Facility

    File        :   IdentityServiceITResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityServiceITResource.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.navigator;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.connection.oim.service.IdentityService;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServiceITResource
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The expandable navigator node that display <code>ITResource</code>s of the
 ** connected Identity Service navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityServiceITResource extends IdentityServiceBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>IdentityServiceITResource</code> that allows use as a
   ** JavaBean.
   **
   ** @param  parent             the owner of this {@link IdentityServiceBase}.
   **                            <br>
   **                            Allowed object is {@link EndpointElement}.
   ** @param  service            the connection to the {@link IdentityService}.
   **                            <br>
   **                            Allowed object is {@link EndpointElement}.
   */
  public IdentityServiceITResource(final EndpointElement parent, final IdentityService service) {
    // ensure inheritance
    super(parent, service, Bundle.IDENTITY_ITRESOURCE_LABEL, Bundle.IDENTITY_ITRESOURCE_ICON);
  }
}

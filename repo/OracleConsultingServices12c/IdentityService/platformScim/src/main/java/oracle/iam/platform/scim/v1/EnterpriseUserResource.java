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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   EnterpriseUserResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EnterpriseUserResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.v1;

import oracle.iam.platform.scim.annotation.Schema;

import oracle.iam.platform.scim.schema.EnterpriseUser;

////////////////////////////////////////////////////////////////////////////////
// final class EnterpriseUserResource
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This represents a SCIM schema extension commonly used in representing users
 ** that belong to, or act on behalf of a business or enterprise.
 ** <br>
 ** The core schema for <code>User</code> is identified using the URI's:
 ** <code>urn:scim:schemas:extension:enterprise:1.0</code>
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id=EnterpriseUserResource.ID, name="EnterpriseUser", description="Enterprise User")
public class EnterpriseUserResource extends EnterpriseUser<EnterpriseUserResource>  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ID = "urn:scim:schemas:extension:enterprise:1.0";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EnterpriseUserResource</code> SCIM Resource that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EnterpriseUserResource() {
	  // ensure inheritance
    super();
  }
}
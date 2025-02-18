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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Library

    File        :   EnterpriseUserResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EnterpriseUserResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v2.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;

import oracle.iam.identity.icf.scim.annotation.Schema;

import oracle.iam.identity.icf.scim.schema.EnterpriseUser;

////////////////////////////////////////////////////////////////////////////////
// class EnterpriseUserExtension
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This represents a SCIM 2 schema extension commonly used in representing
 ** users that belong to, or act on behalf of a business or enterprise.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id=EnterpriseUserResource.SCHEMA, name=EnterpriseUser.NAME, description="The schema extension \"Enterprise User\" defines attributes commonly used in representing users that belong to, or act on behalf of, a business or enterprise. The enterprise User extension is identified using the following schema URI: \"urn:ietf:params:scim:schemas:extension:enterprise:2.0:User\".")
public class EnterpriseUserResource extends EnterpriseUser<EnterpriseUserResource>  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The identifier of the schema extension. */
  @JsonIgnore(true)
  public static final String SCHEMA = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EnterpriseUserResource</code> SCIM 2 Resource
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public EnterpriseUserResource() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  public static String path(final String attribute) {
    return String.format("%s:%s", SCHEMA, attribute);
  }
}
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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   EntitlementList.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementList.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.persistence.dialect;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementList
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>Entitlement</code> declares the usefull constants to deal with
 ** <code>EntitlementList</code>s in Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
class EntitlementList extends DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the entity to use. */
  static final String ENTITY   = "ent_list";

  /** the attribute name of the system identifier of an entry in the entity. */
  static final String PRIMARY  = "ent_list_key";

  /**
   ** the attribute name of the system identifier of a Resource object in the
   ** entity.
   */
  static final String RESOURCE = "obj_key";

  /**
   ** the attribute name of the system identifier of an IT Resource in the
   ** entity.
   */
  static final String ENDPOINT = "svr_key";

  /** the attribute name of the column identifier of an entity references. */
  static final String PROPERTY = "sdc_key";

  /** the attribute name of the value identifier of an entity references. */
  static final String VALUE    = "lkv_key";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntitlementList</code> with the specified
   ** properties.
   */
  private EntitlementList() {
    // ensure inheritance
    super(null, ENTITY, PRIMARY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>EntitlementList</code> with the
   ** specified properties.
   **
   ** @return                    an instance of <code>EntitlementList</code>
   **                            with the properties provided.
   */
  public static EntitlementList build() {
    return new EntitlementList();
  }
}
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

    File        :   Namespace.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Namespace.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.persistence.dialect;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;

////////////////////////////////////////////////////////////////////////////////
// class Namespace
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>Namespace</code> declares the usefull constants to deal with
 ** <code>Lookup Definition</code>s in Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class Namespace extends DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the entity to use. */
  public static final String ENTITY  = "lku";

  /** the attribute name of the system identifier of an entry in the entity. */
  public static final String PRIMARY = "lku_key";

  /** the attribute name of the unique identifier of an entry in the entity. */
  public static final String UNIQUE  = "lku_type_string_key";

  //////////////////////////////////////////////////////////////////////////////
  // class Entitlement
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>Entitlement</code> declares the usefull constants to deal with
   ** <code>Lookup Values</code>s in Oracle Identity Manager.
   */
  static final class Entitlement extends Namespace {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Entitlement</code> property with the specified
     ** properties.
     */
    protected Entitlement() {
      // ensure inheritance
      super(EntitlementList.ENTITY, EntitlementList.VALUE);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Namespace</code> with the specified properties.
   */
  protected Namespace() {
    // ensure inheritance
    this(ENTITY, PRIMARY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Namespace</code> with the specified properties.
   **
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   */
  protected Namespace(final String name, final String primary) {
    // ensure inheritance
    super(null, name, primary);
  }
}
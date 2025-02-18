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

    File        :   Catalog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Catalog.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.persistence.dialect;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;

////////////////////////////////////////////////////////////////////////////////
// abstract class Catalog
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The <code>Catalog</code> declares the usefull constants to deal with
 ** <code>Catalog</code>s in Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
abstract class Catalog extends DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the entity to use. */
  public static final String ENTITY       = "catalog";

  /** the attribute name of the system identifier of an entry in the entity. */
  public static final String PRIMARY      = "catalog_id";

  /** the attribute name of the key of an entry in the entity. */
  public static final String KEY          = "entity_key";

  /** the attribute name of the type of an entry in the entity. */
  public static final String TYPE         = "entity_type";

  /** the attribute name of the name of an entry in the entity. */
  public static final String NAME         = "entity_name";

  /** the attribute name of the display name of an entry in the entity. */
  public static final String DISPLAY_NAME = "entity_display_name";

  /** the attribute name of the descrption of an entry in the entity. */
  public static final String DESCRIPTION  = "entity_description";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Role
  // ~~~~~ ~~~~
  /**
   ** The <code>Role</code> declares the usefull constants to deal with
   ** cataloge <code>Role</code>s in Oracle Identity Manager.
   */
  static final class Role extends Catalog {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Role</code> property with the specified
     ** properties.
     */
    protected Role() {
      // ensure inheritance
      super(ENTITY, PRIMARY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Application
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>Role</code> declares the usefull constants to deal with
   ** cataloge <code>Application</code>s in Oracle Identity Manager.
   */
  static final class Application extends Catalog {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Application</code> property with the specified
     ** properties.
     */
    protected Application() {
      // ensure inheritance
      super(ENTITY, PRIMARY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Entitlement
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>Role</code> declares the usefull constants to deal with
   ** cataloge <code>Application</code>s in Oracle Identity Manager.
   */
  static final class Entitlement extends Catalog {

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
      super(ENTITY, PRIMARY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Catalog</code> with the specified properties.
   **
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   */
  protected Catalog(final String name, final String primary) {
    // ensure inheritance
    super(null, name, primary);
  }
}
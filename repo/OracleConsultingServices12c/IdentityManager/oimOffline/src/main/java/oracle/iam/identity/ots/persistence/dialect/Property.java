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

    File        :   Property.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Property.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.persistence.dialect;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;

////////////////////////////////////////////////////////////////////////////////
// abstract class Property
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** The <code>Property</code> declares the usefull constants to deal with
 ** <code>SDC properties</code>s in Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
abstract class Property extends DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the entity to use. */
  static final String ENTITY  = "sdp";

  /** the attribute name of the system identifier of an entry in the entity. */
  static final String PRIMARY = "sdc_key";

  /** the attribute name of the name of a property in the entity. */
  static final String NAME    = "sdp_property_name";

  /** the attribute name of the value of a property in the entity. */
  static final String VALUE   = "sdp_property_value";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Column
  // ~~~~~ ~~~~~~
  /**
   ** The <code>Column</code> declares the usefull constants to deal with
   ** <code>SDC properties</code>s in Oracle Identity Manager.
   */
  static final class Column extends Property {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Column</code> property with the specified
     ** properties.
     */
    protected Column() {
      // ensure inheritance
      super(ENTITY, PRIMARY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Entitlement
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>Entitlement</code> declares the usefull constants to deal with
   ** <code>SDC properties</code>s of the entitlement list in Oracle Identity
   ** Manager.
   */
  static final class Entitlement extends Property {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Entitlement</code> property with the
     ** specified properties.
     */
    protected Entitlement() {
      // ensure inheritance
      super(EntitlementList.ENTITY, EntitlementList.PROPERTY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class EntitlementJoin
  // ~~~~~ ~~~~~~~~~~~~~~~
  /**
   ** The <code>EntitlementJoin</code> declares the usefull constants to deal
   ** with <code>SDC properties</code>s of the entitlement list in Oracle
   ** Identity Manager.
   */
  static final class EntitlementJoin extends Property {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Entitlement</code> property with the
     ** specified properties.
     */
    protected EntitlementJoin() {
      // ensure inheritance
      super(String.format("%s, %s, %s", EntitlementList.ENTITY, Property.ENTITY, NamespaceValue.ENTITY), Namespace.PRIMARY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class CatalogJoin
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** The <code>CatalogJoin</code> declares the usefull constants to deal
   ** with <code>SDC properties</code>s of the entitlement list in Oracle
   ** Identity Manager.
   */
  static final class CatalogJoin extends Property {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Entitlement</code> property with the
     ** specified properties.
     */
    protected CatalogJoin() {
      // ensure inheritance
      super(String.format("%s, %s, %s", EntitlementList.ENTITY, Property.ENTITY, Catalog.ENTITY), EntitlementList.PRIMARY);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Property</code> with the specified properties.
   **
   ** @param  name               the name of the entity to access.
   ** @param  primary            the name of attribute identifying a record
   **                            in the entity.
   */
  protected Property(final String name, final String primary) {
    // ensure inheritance
    super(null, name, primary);
  }
}
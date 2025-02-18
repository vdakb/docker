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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.pcf;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import oracle.hst.foundation.utility.CollectionUtility;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Payload;

////////////////////////////////////////////////////////////////////////////////
// class TestCase
// ~~~~~ ~~~~~~~~
/**
 ** The general test case to manage entries in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name a connector service consumer will put in the appropriate options
   ** of a reconciliation process to configure the size of a batch of resources
   ** returned from a Service Provider.
   */
  static final String      BATCH_SIZE_OPTION    = "batchSize";

  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  static final String      SEARCH_ORDER_OPTION  = "searchOrder";

  /** the object class for a organization entitlement */
  static final ObjectClass TENANT    = new ObjectClass(SchemaUtility.createSpecialName("TENANT"));

  /** the object class for a space entitlement */
  static final ObjectClass SPACE     = new ObjectClass(SchemaUtility.createSpecialName("SPACE"));

  static final Set<Attribute> O1        = AttributeFactory.set(new String[][]{{Name.NAME, "Pizza Place"},     {Payload.STATUS, Payload.STATUS_ACTIVE}});
  static final Set<Attribute> O2        = AttributeFactory.set(new String[][]{{Name.NAME, "Moshroom Circle"}, {Payload.STATUS, Payload.STATUS_ACTIVE}});
  static final Set<Attribute> O3        = AttributeFactory.set(new String[][]{{Name.NAME, "Mario Cart"},      {Payload.STATUS, Payload.STATUS_ACTIVE}});

  static final Set<Attribute> S11       = AttributeFactory.set(new String[][]{{Name.NAME, "Pizza Place Wubbler"},            {Payload.TENANT, "4bd93aee-1236-4af8-bee2-9fcea298fc75"}});
  static final Set<Attribute> S12       = AttributeFactory.set(new String[][]{{Name.NAME, "Pizza Place Icecream"},           {Payload.TENANT, "4bd93aee-1236-4af8-bee2-9fcea298fc75"}});
  static final Set<Attribute> S21       = AttributeFactory.set(new String[][]{{Name.NAME, "Moshroom Circle Toad"},           {Payload.TENANT, "94dac3f5-5b98-4639-abf4-e02e07f759c0"}});
  static final Set<Attribute> S22       = AttributeFactory.set(new String[][]{{Name.NAME, "Moshroom Circle Curve of Death"}, {Payload.TENANT, "94dac3f5-5b98-4639-abf4-e02e07f759c0"}});
  static final Set<Attribute> S23       = AttributeFactory.set(new String[][]{{Name.NAME, "Moshroom Circle Picadilly"},      {Payload.TENANT, "94dac3f5-5b98-4639-abf4-e02e07f759c0"}});
  static final Set<Attribute> S31       = AttributeFactory.set(new String[][]{{Name.NAME, "Mario Cart Bowser"},              {Payload.TENANT, "8b2ee639-c5b8-4e9f-81b2-0cdd3803096b"}});
  static final Set<Attribute> S32       = AttributeFactory.set(new String[][]{{Name.NAME, "Mario Cart Peach"},               {Payload.TENANT, "8b2ee639-c5b8-4e9f-81b2-0cdd3803096b"}});
  static final Set<Attribute> S33       = AttributeFactory.set(new String[][]{{Name.NAME, "Mario Cart Toad"},                {Payload.TENANT, "8b2ee639-c5b8-4e9f-81b2-0cdd3803096b"}});
  static final Set<Attribute> S34       = AttributeFactory.set(new String[][]{{Name.NAME, "Mario Cart Luigi"},               {Payload.TENANT, "8b2ee639-c5b8-4e9f-81b2-0cdd3803096b"}});
  static final Set<Attribute> S35       = AttributeFactory.set(new String[][]{{Name.NAME, "Mario Cart Yoshi"},               {Payload.TENANT, "8b2ee639-c5b8-4e9f-81b2-0cdd3803096b"}});

  static Uid                  admin     = new Uid("278d6ed9-736c-41cb-96e2-d3c28d75fbb8");
  static Uid                  uid       = new Uid("7873b610-996a-4190-8253-45aae071a40d");

  static final Set<Attribute> CREATE    = AttributeFactory.set(
    new String[]{
      Name.NAME
      , OperationalAttributes.PASSWORD_NAME
      , "verified"
      , "name.givenName"
      , "name.familyName"
      , "emails.value"
    }
    , new Object[]{
        "azitterbacke"
      , new GuardedString("Welcome1".toCharArray())
      , Boolean.TRUE
      , "Alfons"
      , "Zitterbacke"
      , "alfons.zitterbacke@vm.oracle.com"
    }
  );
  static final Set<Attribute> MODIFY = AttributeFactory.set(
    new String[]{ "emails.value" }
  , new Object[]{ "alfons.zitterbacke@vm.oracle.com"}
  );

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  // ~~~~~ ~~~~~
  public static abstract class Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Map<String, Object>       create = new HashMap<String, Object>();
    final Map<String, Object>       update = new HashMap<String, Object>();

    final List<Map<String, Object>> group  = new ArrayList<Map<String, Object>>();
    final List<Map<String, Object>> tenant = new ArrayList<Map<String, Object>>();
    final List<Map<String, Object>> space  = new ArrayList<Map<String, Object>>();

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create
    /**
     ** Factory method to create the attribute mapping that belongs to the
     ** account data.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account data.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    public abstract Map<String, Object> create();

    ////////////////////////////////////////////////////////////////////////////
    // Method: group
    /**
     ** Factory method to create the entitlement mapping that belongs to the
     ** account groups.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account groups.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    public abstract List<Map<String, Object>> group();
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Zitterbacke
  // ~~~~~ ~~~~~~~~~~~
  public static class ZitterBacke extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid UID = new Uid("5a6137f4-aaac-4d52-aa2b-9b78cf0e3094");

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    /**
     ** Factory method to create the attribute mapping that belongs to the
     ** account data.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account data.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put("__NAME__",        "azitterbacke");
        this.create.put("name.familyName", "Zitterbacke");
        this.create.put("name.givenName",  "Alfons");
        this.create.put("origin",          "uaa");
      }
      return this.create;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: group (Entry)
    /**
     ** Factory method to create the entitlement mapping that belongs to the
     ** account groups.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account groups.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    @Override
    public List<Map<String, Object>> group() {
      if (this.group.size() == 0) {
        this.group.add(CollectionUtility.map("__UID__",  "azitterbacke", "__NAME__", "Zitterbacke"));
      }
      return this.group;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MusterMann
  // ~~~~~ ~~~~~~~~~~
  public static class MusterMann extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Uid UID    = new Uid("4627fac2-ed43-4928-a1f5-4f6bdd25f9d0");

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    /**
     ** Factory method to create the attribute mapping that belongs to the
     ** account data.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account data.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put("__NAME__",        "amustermann");
        this.create.put("name.familyName", "Mustermann");
        this.create.put("name.givenName",  "Alfred");
        this.create.put("origin",          "uaa");
      }
      return this.create;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: group (Entry)
    /**
     ** Factory method to create the entitlement mapping that belongs to the
     ** account groups.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account groups.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    @Override
    public List<Map<String, Object>> group() {
      if (this.group.size() == 0) {
        this.group.add(CollectionUtility.map("__UID__",  "amustermann", "__NAME__", "Mustermann"));
      }
      return this.group;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MusterFrau
  // ~~~~~ ~~~~~~~~~~
  public static class MusterFrau extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Uid UID = new Uid("05027f17-d5b7-435d-9684-a4c771274a84");

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    /**
     ** Factory method to create the attribute mapping that belongs to the
     ** account data.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account data.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put("User ID",         "amusterfrau");
        this.create.put("name.familyName", "Musterfrau");
        this.create.put("name.givenName",  "Agathe");
        this.create.put("origin",          "uaa");
      }
      return this.create;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: group (Entry)
    /**
     ** Factory method to create the entitlement mapping that belongs to the
     ** account groups.
     ** <p>
     ** Keep in mind that the mapping specified here is targeting the core
     ** attribute name (or their aliases) of the Service Provider, not the
     ** attributes of the process form in Identity Manager.
     **
     ** @return                  the attribute mapping that belongs to the
     **                          account groups.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element consist as a {@link String} as the key
     **                          and an {@link Object} as its value.
     */
    @Override
    public List<Map<String, Object>> group() {
      if (this.group.size() == 0) {
        this.group.add(CollectionUtility.map("__UID__",  "amusterfrau", "__NAME__", "Musterfrau"));
      }
      return this.group;
    }
  }
}
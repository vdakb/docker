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
    Subsystem   :   Atlassian Jira Connector

    File        :   TestDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    TestDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  SBernet     First release version
*/

package oracle.iam.identity.gws.integration.jira;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.AttributeFactory;
import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.Uid;

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
  static final ObjectClass PROJECT   = new ObjectClass(SchemaUtility.createSpecialName("PROJECT"));

  /** the object class for a space entitlement */
  static final ObjectClass ROLE      = new ObjectClass(SchemaUtility.createSpecialName("ROLE"));

  static final Set<Attribute> CREATE    = AttributeFactory.set(
    new String[]{
      Name.NAME
      , OperationalAttributes.PASSWORD_NAME
    }
    , new Object[]{
        "azitterbacke"
      , new GuardedString("Welcome1".toCharArray())
    }
  );
  static final Set<Attribute> MODIFY = AttributeFactory.set(
    new String[]{ "emailAddres" }
  , new Object[]{ "alfons.zitterbacke@vm.oracle.com"}
  );

  static final File PROVISIONING   = new File("C:/Oracle/Oracle Consulting/OracleConsultingServices12c/IdentityConnector/icfAtlassianJira/src/test/resources/mds/jira-account-provisioning.xml");
  static final File RECONCILIATION = new File("C:/Oracle/Oracle Consulting/OracleConsultingServices12c/IdentityConnector/icfAtlassianJira/src/test/resources/mds/jira-account-reconciliation.xml");

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
        this.create.put("__NAME__",     "BK2349532");
        this.create.put("email",    "alfons.zitterbacke@vm.oracle.com");
        this.create.put("displayName",  "Alfons Zitterbacke");
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
}
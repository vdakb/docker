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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   TestTenant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestTenant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.gae;

import java.util.Set;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import oracle.iam.junit.TestCaseIntegration;

////////////////////////////////////////////////////////////////////////////////
// class TestTenant
// ~~~~~ ~~~~~~~~~~
/**
 ** The general test case to manage entries in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestTenant extends TestCaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the object class for a organization entitlement */
  static final ObjectClass    TENANT      = new ObjectClass(SchemaUtility.createSpecialName("TENANT"));

  /** the object class for a product entitlement */
  static final ObjectClass    PRODUCT     = new ObjectClass(SchemaUtility.createSpecialName("PRODUCT"));

  /** the object class for an developer acount */
  static final ObjectClass    DEVELOPER   = new ObjectClass(SchemaUtility.createSpecialName("DEVELOPER"));

  /** the object class for an application entitlement */
  static final ObjectClass    APPLICATION = new ObjectClass(SchemaUtility.createSpecialName("APPLICATION"));

  static final Uid            T1          = new Uid("pizza-place");
  static final Uid            T2          = new Uid("moshroom-circle");
  static final Uid            T3          = new Uid("mario-cart");

  static final Set<Attribute> T1C         = AttributeFactory.set(new String[][]{{"type", "paid"}, {Name.NAME, T1.getUidValue()}, {"displayName", "Pizza Place"}});
  static final Set<Attribute> T2C         = AttributeFactory.set(new String[][]{{"type", "paid"}, {Name.NAME, T2.getUidValue()}, {"displayName", "Moshroom Circle"}});
  static final Set<Attribute> T3C         = AttributeFactory.set(new String[][]{{"type", "paid"}, {Name.NAME, T3.getUidValue()}, {"displayName", "Mario Cart"}});

  static final Set<Attribute> T1M         = AttributeFactory.set(new String[][]{{"type", "paid"}, {Name.NAME, T1.getUidValue()}, {"displayName", "Pizza Place Changed"}});
  static final Set<Attribute> T2M         = AttributeFactory.set(new String[][]{{"type", "paid"}, {Name.NAME, T2.getUidValue()}, {"displayName", "Moshroom Circle Changed"}});
  static final Set<Attribute> T3M         = AttributeFactory.set(new String[][]{{"type", "paid"}, {Name.NAME, T3.getUidValue()}, {"displayName", "Mario Cart Changed"}});
}
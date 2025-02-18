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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Connector

    File        :   TestAccount.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    TestAccount.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-19-05  SBernet     First release version
*/

package oracle.iam.identity.gws.integration.ajs;

import java.util.Set;
import java.util.Map;

import java.io.File;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

////////////////////////////////////////////////////////////////////////////////
// class TestAccount
// ~~~~~ ~~~~~~~~~~~
/**
 ** The general test case to manage accounts in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class TestAccount extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Uid                 UID            = new Uid("an4711123");

  static final File                PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ajs-account-provisioning.xml");
  static final File                RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ajs-account-reconciliation.xml");

  static final Map<String, Object> CREATE         = CollectionUtility.map(
    new String[]{
      "UD_AJS_USR_UID"
    , "UD_AJS_USR_PWD"
    , "UD_AJS_USR_EMAIL"
    , "UD_AJS_USR_DISPLAY_NAME"
    }
  , new Object[]{
      UID.getUidValue()
    , "Welcome1"
    , "alfons.zitterbacke@vm.oracle.com"
    , "Alfons Zitterbacke"
    }
  );

  static final Map<String, Object> MODIFY         = CollectionUtility.map(
    new String[]{
      "UD_AJS_USR_EMAIL"
    }
  , new Object[]{
      "maximilian.dumpfbacke@vm.oracle.com"
    }
  );

  static final Map<String, Object> REVERT         = CollectionUtility.map(
    new String[]{
      "UD_AJS_USR_EMAIL"
    }
    , new Object[]{
      "alfons.zitterbacke@vm.oracle.com"
    }
  );

  static final Map<String, Object> RENAME        = CollectionUtility.map(
    new String[]{
      "UD_AJS_USR_UID"
    }
  , new Object[]{
      "az"
    }
  );

  static final Map<String, Object> PASSWORD       = CollectionUtility.map(
    new String[]{
      "UD_AJS_USR_PWD"
    }
  , new Object[]{
      "Welcome1"
    }
  );

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  public static Set<Attribute> create()
    throws TaskException {

    return DescriptorTransformer.build(provisioning(), CREATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  public static Set<Attribute> modify()
    throws TaskException {

    return DescriptorTransformer.build(provisioning(), MODIFY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  public static Set<Attribute> rename()
    throws TaskException {

    return DescriptorTransformer.build(provisioning(), RENAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revert
  public static Set<Attribute> revert()
    throws TaskException {

    return DescriptorTransformer.build(provisioning(), REVERT);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  public static Set<Attribute> password()
    throws TaskException {

    return DescriptorTransformer.build(provisioning(), PASSWORD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  public static Set<Attribute> enable()
    throws TaskException {

    return CollectionUtility.set(new AttributeBuilder().buildEnabled(true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  public static Set<Attribute> disable()
    throws TaskException {

    return CollectionUtility.set(new AttributeBuilder().buildEnabled(false));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioning
  public static Descriptor provisioning()
    throws TaskException {

    return DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconciliation
  public static Descriptor reconciliation()
    throws TaskException {

    return DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), RECONCILIATION);
  }
}
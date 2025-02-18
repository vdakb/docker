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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Identity Governance Service SCIM

    File        :   Assign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Assign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.tenant;

import java.util.Set;
import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.icf.connector.scim.igs.ExtensionClass;

////////////////////////////////////////////////////////////////////////////////
// class Assign
// ~~~~~ ~~~~~~
/**
 ** The test case modify operation on tenants at the target system leveraging
 ** the connector bundle deployed on a <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Assign extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Assign</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Assign() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {Assign.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignT36099Generate
  /**
   ** Test that a particular tenant could be modified by its primary identifier.
   */
  @Test
  public void assignT36099Generate() {
    final Uid                  id   = new Uid("T-36-0-99");
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_TUS_UID" , 5L
    , "UD_IGS_TUS_SID" , "uid.generate"
    );
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      assertNotNull(descriptor);
      final Map<Pair<String, String>, Descriptor> reference = descriptor.reference();
      assertNotNull(reference);
      final Descriptor user = reference.get(Pair.of(ObjectClass.ACCOUNT_NAME, "UD_IGS_TUS"));
      assertNotNull(user);
      final Set<Attribute> dataSet = DescriptorTransformer.build(ExtensionClass.TENANT, user, data);
      assertNotNull(dataSet);
      facade(service()).addAttributeValues(ObjectClass.ACCOUNT, id, dataSet, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignT36099Register
  /**
   ** Test that a particular tenant could be modified by its primary identifier.
   */
  @Test
  public void assignT36099Register() {
    final Uid                  id   = new Uid("T-36-0-99");
    // the map simulates how Identity Governance provides the data after the
    // adapter task createProcessData applied on the process data
    final Map<String, Object>  data = CollectionUtility.map(
      "UD_IGS_TUS_UID" , 5L
    , "UD_IGS_TUS_SID" , "uid.register"
    );
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      assertNotNull(descriptor);
      final Map<Pair<String, String>, Descriptor> reference = descriptor.reference();
      assertNotNull(reference);
      final Descriptor user = reference.get(Pair.of(ObjectClass.ACCOUNT_NAME, "UD_IGS_TUS"));
      assertNotNull(user);
      final Set<Attribute> dataSet = DescriptorTransformer.build(ExtensionClass.TENANT, user, data);
      assertNotNull(dataSet);
      facade(service()).addAttributeValues(ObjectClass.ACCOUNT, id, dataSet, null);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}
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

    File        :   TestMarshalling.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestMarshalling.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.connector;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.EmbeddedObjectBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.iam.identity.icf.connector.scim.igs.ExtensionClass;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.v2.schema.Marshaller;
import oracle.iam.identity.icf.scim.v2.schema.GroupResource;
import oracle.iam.identity.icf.scim.v2.schema.TenantResource;
import oracle.iam.identity.icf.scim.v2.schema.AccountResource;

import org.identityconnectors.framework.common.objects.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class TestMarshalling
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>TestMarshalling</code> covers the basic functionality of a
 ** Connector Bundle Test Cases.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestMarshalling extends TestBaseConnector {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestMarshalling</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestMarshalling() {
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
    final String[] parameter = {TestMarshalling.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   governanceAccount
  /**
   ** Test the marshalling of an Identity Governance Service account resource.
   */
  @Test
  public void governanceAccount() {
    final AccountResource resource = new AccountResource();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   governanceGroup
  /**
   ** Test the marshalling of an Identity Governance Service group resource.
   */
  @Test
  public void governanceGroup() {
    final GroupResource resource = GroupResource.of("uid.generate", "Generate UID's");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   governanceTenant
  /**
   ** Test the marshalling of an Identity Governance Service tenant resource.
   */
  @Test
  public void governanceTenant() {
    final TenantResource resource = TenantResource.of("T-36-0-20", "Federal Criminal Police Office")
      .role(
        CollectionUtility.list(
          TenantResource.Role.of(TenantResource.Role.USER, -1L).scope("uid.generate")
        , TenantResource.Role.of(TenantResource.Role.USER, -1L).scope("uid.register")
        , TenantResource.Role.of(TenantResource.Role.USER,  4L).scope("uid.generate")
        , TenantResource.Role.of(TenantResource.Role.USER,  4L).scope("uid.register")
        )
      )
    ;
    final ConnectorObjectBuilder builder = new ConnectorObjectBuilder();
    builder.setObjectClass(ExtensionClass.TENANT);
    // always set __UID__ and __NAME__ attribute to convince stupid framework
    // developer
    builder.setUid(resource.id());
    builder.setName(resource.displayName());

    final List<EmbeddedObject> embedded = new ArrayList<EmbeddedObject>();
    if (!CollectionUtility.empty(resource.role())) {
      // a lambda function can avoid this loop but performance benchmarks had
      // shown saying that the overhead of Stream.forEach() compared to an
      // ordinary for loop is so significant in general that using it by
      // default will just pile up a lot of useless CPU cycles across the
      // application 
      for (TenantResource.Role cursor : resource.role()) {
        embedded.add(
          new EmbeddedObjectBuilder()
          .setObjectClass(ObjectClass.ACCOUNT)
          .addAttribute(
              // the account identifier needs to be casted to a string to ICF
              // connector framework enforce that it has to be a string
              Marshaller.buildAttribute(Uid.NAME,  cursor.value()).build()
            , Marshaller.buildAttribute(Name.NAME, cursor.display()).build()
            , Marshaller.buildAttribute("scope",   cursor.scope()).build()
          )
          .build()
        );
      }
      // add the collection to the connector object builder
      builder.addAttribute(ObjectClass.ACCOUNT_NAME, embedded);
    }
    final ConnectorObject subject =  builder.build();
    // verify
    assertEquals(subject.getObjectClass(), ExtensionClass.TENANT);
    assertNotNull(subject.getUid());
    assertNotNull(subject.getName());
    assertEquals(subject.getAttributes().size(), 3);
    System.out.println(subject.getUid().getUidValue() + "::" + subject.getName().getNameValue());

    final Attribute account = subject.getAttributeByName(ObjectClass.ACCOUNT_NAME);
    assertNotNull(account);
    
    final List<?> value = account.getValue();
    assertNotNull(value);
    assertEquals(value.size(), 2);

//    assertNotNull(account.getUid());
//    assertNotNull(account.getName());
  }
}
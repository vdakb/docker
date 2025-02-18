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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   TestUserResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestUserResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import oracle.iam.platform.scim.entity.Path;

import oracle.iam.platform.scim.schema.Email;
import oracle.iam.platform.scim.schema.Generic;
import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.v2.UserResource;
import oracle.iam.platform.scim.v2.EnterpriseUserResource;

////////////////////////////////////////////////////////////////////////////////
// class TestUserResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for serialization and de-serialization of the core group
 ** resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestUserResource extends TestResource {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestUserResource</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestUserResource() {
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
    final String[] parameter = { TestUserResource.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRepresentation
  /**
   ** Test de-serializing the full core user representation copied from
   ** RFC 7643.
   */
  @Test
  public void testRepresentation() {
    try {
      final UserResource resource = stream("user-full-representation.json", UserResource.class);
      
      assertNotNull(resource);
      assertNotNull(resource.name());
      assertNotNull(resource.name().givenName());

      assertNotNull(resource.email());

      final Iterator<Email> cursor = resource.email().iterator();
      assertNotNull(cursor.next().value());
      assertNotNull(cursor.next().value());

      final EnterpriseUserResource extension = Support.nodeToValue(resource.extensionValues(Path.build(EnterpriseUserResource.class)).get(0), EnterpriseUserResource.class);
      assertNotNull(extension);
      
      final UserResource response   = Support.objectReader().forType(UserResource.class).readValue(Support.objectWriter().writeValueAsString(resource));
      assertEquals(resource, response);
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testExtension
  /**
   ** Test operations with POJO extension objects.
   */
  @Test
  public void testExtension() {
    final Path   path   = Path.build(EnterpriseUserResource.class);
    final String schema = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";
    try {
      final UserResource           resource = stream("user-full-representation.json", UserResource.class);
      final EnterpriseUserResource node     = Support.nodeToValue(resource.extensionValues(path).get(0), EnterpriseUserResource.class);
      EnterpriseUserResource       pojo     = resource.extension(EnterpriseUserResource.class);

      assertNotNull(pojo);
      assertEquals(pojo, node);
      assertTrue(resource.namespace().contains(schema));

      node.costCenter("1111");
      node.organization("New Organization 1");
      node.division("New Division 1");
      node.department("New Department 1");

      // replace with a path object
      resource.extension(node);
      pojo = resource.extension(EnterpriseUserResource.class);

      assertNotNull(pojo);
      assertEquals(pojo, node);
      assertTrue(resource.namespace().contains(schema));

      // remove with path object
      assertTrue(resource.removeExtension(EnterpriseUserResource.class));
      assertNull(resource.extension(EnterpriseUserResource.class));
      assertFalse(resource.namespace().contains(schema));

      // now recreate the extension, and make sure it is present
      resource.extension(node);
      pojo = resource.extension(EnterpriseUserResource.class);

      assertNotNull(pojo);
      assertEquals(pojo, node);
      assertTrue(resource.namespace().contains(schema));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testGenericResource
  /**
   ** Test conversion to {@link Generic}.
   */
  @Test
  public void testGenericResource() {
    try {
      final UserResource  origin  = stream("user-full-representation.json", UserResource.class);
      final Generic       generic = origin.generic();
      final UserResource  source  = Support.nodeToValue(generic.objectNode(), UserResource.class);
      assertEquals(origin, source);
    }
    catch (Exception e) {
      failed(e);
    }
  }
}
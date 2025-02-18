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

    File        :   TestGroupResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestGroupResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import java.util.Iterator;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.v2.GroupResource;

import oracle.iam.platform.scim.schema.Member;
import oracle.iam.platform.scim.schema.Generic;

////////////////////////////////////////////////////////////////////////////////
// class TestGroupResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for serialization and de-serialization of the core group
 ** resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestGroupResource extends TestResource {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestGroupResource</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestGroupResource() {
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
    final String[] parameter = { TestGroupResource.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRepresentation
  /**
   ** Test de-serializing the full core group representation copied from
   ** RFC 7643.
   */
  @Test
  public void testRepresentation() {
    try {
      final GroupResource resource = stream("group-full-representation.json", GroupResource.class);
      assertNotNull(resource.namespace());
      assertNotNull(resource.id());
      assertNotNull(resource.metadata());
      assertNotNull(resource.displayName());
      assertNotNull(resource.members());
      assertEquals(resource.members().size(), 2);

      for (Member member : resource.members()) {
        assertNotNull(member.value());
        assertNotNull(member.ref());
        assertNotNull(member.display());
      }
      final Iterator<Member> cursor = resource.members().iterator();
      assertEquals(cursor.next().display(), "Babs Jensen");
      assertEquals(cursor.next().display(), "Mandy Pepperidge");
    }
    catch (JsonProcessingException e) {
      failed(e);
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
      final GroupResource origin  = stream("group-full-representation.json", GroupResource.class);
      final Generic       generic = origin.generic();
      final GroupResource source  = Support.nodeToValue(generic.objectNode(), GroupResource.class);
      assertEquals(origin, source);
    }
    catch (Exception e) {
      failed(e);
    }
  }
}

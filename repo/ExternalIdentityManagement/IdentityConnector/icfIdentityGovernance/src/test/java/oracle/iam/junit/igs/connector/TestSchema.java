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

    File        :   TestSchema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestSchema.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.connector;

import org.junit.Test;
import org.junit.FixMethodOrder;

import org.junit.runner.JUnitCore;
import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.scim.igs.ExtensionClass;

import org.identityconnectors.framework.common.objects.ObjectClassInfo;

////////////////////////////////////////////////////////////////////////////////
// class TestSchema
// ~~~~~ ~~~~~~~~~~
/**
 ** The test case to obtain the schema from the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestSchema extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestSchema</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestSchema() {
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
    final String[] parameter = {TestSchema.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   staticSchema
  /**
   ** Test the static schema could be fetched from the target system leveraging
   ** the <code>Java Connector Server</code> connector bundle.
   */
  @Test
  public void staticSchema() {
    try {
      final Schema schema = Network.intranet().schema();
      Network.CONSOLE.debug("staticSchema", schema.toString());

      ObjectClassInfo info = schema.findObjectClassInfo(ObjectClass.ACCOUNT.getObjectClassValue());
      assertNotNull(info);

      info = schema.findObjectClassInfo(ObjectClass.GROUP.getObjectClassValue());
      assertNotNull(info);

      info = schema.findObjectClassInfo(ExtensionClass.TENANT.getObjectClassValue());
      assertNotNull(info);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverSchema
  /**
   ** Test the server schema could be fetched from the target.
   */
  @Test
  public void serverSchema() {
    try {
      final Schema schema = Network.intranet().schema();
      Network.CONSOLE.debug("serverSchema", schema.toString());
      Network.CONSOLE.debug("serverSchema", schema.toString());
      
      assertNotNull(schema.findObjectClassInfo("__ACCOUNT__"));
      assertNotNull(schema.findObjectClassInfo("__GROUP__"));
      assertNotNull(schema.findObjectClassInfo("__TENANT__"));
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}
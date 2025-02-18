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

    File        :   Revoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Revoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.connector.tenant;

import java.util.List;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.v2.request.Operation;

////////////////////////////////////////////////////////////////////////////////
// class Revoke
// ~~~~~ ~~~~~~
/**
 ** The test case modify operation on tenants at the target system leveraging the
 ** connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Revoke extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Revoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Revoke() {
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
    final String[] parameter = {Revoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Test revoke all role request leveraging Web Access Context.
   */
  @Test
  public void execute() {
    try {
      final List<Operation> patch = CollectionUtility.list(
        Operation.remove("roles[value eq 5 and scopes eq \"uid.generate\"]")
      , Operation.remove("roles[value eq 5 and scopes eq \"uid.register\"]")
      );

      Base.latest = CONTEXT.modifyTenant("T-36-0-99", patch);
      assertNotNull(Base.latest);
      assertNotNull(Base.latest.id());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}
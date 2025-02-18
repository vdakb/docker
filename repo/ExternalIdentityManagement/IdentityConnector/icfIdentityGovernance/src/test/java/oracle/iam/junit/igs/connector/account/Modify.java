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

    File        :   Modify.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Modify.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.connector.account;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.scim.v2.request.Operation;

////////////////////////////////////////////////////////////////////////////////
// class Modify
// ~~~~~ ~~~~~~
/**
 ** The test case modify operation on users at the target system leveraging the
 ** connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Modify extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Modify</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Modify() {
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
    final String[] parameter = {Modify.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Test lookup request leveraging Web Access Context.
   */
  @Test
  public void execute() {
    final List<Operation> op = new ArrayList<Operation>();;
    try {
      op.add(Operation.replace("username", "bjensen"));
      Base.latest = CONTEXT.modifyAccount(Base.latest.id(), op);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}
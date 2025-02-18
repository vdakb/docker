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

    System      :   Oracle Identity Manager Unit Testing
    Subsystem   :   Pre-Populate Adapter

    File        :   OrganizationValue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    OrganizationValue.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package bka.iam.identity.adapter;

import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.Organization;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationValue
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>OrganizationName</code> simulates the behaviour of a pre-populate
 ** adapter to obtain the alue of an attribute from an
 ** <code> Organization</code> by the key assigned at an <code>Identity</code>
 ** profile.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OrganizationValue extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OrganizationValue</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public OrganizationValue() {
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
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    final String                  latch  = "228";
    final String                  name   = "ORG_UDF_CODE_NSIS";
    final HashMap<String, Object> filter = new HashMap<>();
    filter.put(Organization.KEY, latch);

    final OrganizationValue test = new OrganizationValue();
    try {
      test.connect();
      tcResultSet resultSet = test.organizationFacade().findOrganizations(filter);
      if (resultSet.getRowCount() == 0) {
        Network.CONSOLE.error("main", TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), latch));
      }
      else if (resultSet.getRowCount() > 1) {
        Network.CONSOLE.error("main", TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), latch));
      }
      else {
        Network.CONSOLE.warning(resultSet.getStringValue(name));
      }
    }
    catch(tcAPIException e) {
      e.printStackTrace();
    }
    catch(tcColumnNotFoundException e) {
      e.printStackTrace();
    }
    catch(TaskException e) {
      e.printStackTrace();
    }
    finally {
      test.disconnect();
    }
  }
}
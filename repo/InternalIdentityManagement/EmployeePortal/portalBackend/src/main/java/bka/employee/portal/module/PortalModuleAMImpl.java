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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Employee Self Service Portal
    Subsystem   :   Common Backend Model Component

    File        :   PortalModuleAMImpl.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PortalModuleAMImpl.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2020-02-23  DSteding    First release version
*/

package bka.employee.portal.module;

import java.sql.SQLException;
import java.sql.CallableStatement;

import oracle.jbo.Session;

import oracle.jbo.server.ApplicationModuleImpl;

import oracle.adf.share.ADFContext;

////////////////////////////////////////////////////////////////////////////////
// class PortalModuleAMImpl
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The local <code>PortalModuleAMImpl</code> to coordinate certain task
 ** belonging to the database session of Employee Self Service Portal
 ** Application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PortalModuleAMImpl extends ApplicationModuleImpl {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PortalModuleAMImpl</code> application module that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argumment constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PortalModuleAMImpl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareSession (overridden)
  /**
   ** To pass user context information such as logged in username or enterprise
   ** name from the Java middle tier to the database session, its required to
   ** override the prepareSession(SessionData data) method in the application
   ** module implementation class. This method will be invoked when an
   ** application module instance is associated with a user session.
   ** <p>
   ** The prepareSession() method can have a custom stored procedure or other
   ** appropriate routines to pass user context data to a database session.
   */
  @Override
  protected void prepareSession (final Session session) {
    // ensure inheritance
    super.prepareSession(session);

    CallableStatement statement = null;
    try {
      statement = getDBTransaction().createCallableStatement("BEGIN epp$session.set_identity(p_context => :context, p_name => :name); END;", 0);
      statement.setString("context", "jee");
      statement.setString("name",    ADFContext.getCurrent().getSecurityContext().getUserPrincipal().getName());
      statement.execute();
    }
    catch (SQLException e) {
      throw new oracle.jbo.JboException(e);
    }
    finally {
      try {
        if (statement != null) {
          statement.close();
        }
      }
      catch (SQLException x) {
        // intentionally left blank
        ;
      }
    }
  }
}
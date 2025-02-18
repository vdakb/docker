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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   SessionAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SessionAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.context;

import java.sql.Statement;
import java.sql.SQLException;

import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

import org.eclipse.persistence.exceptions.DatabaseException;

import org.eclipse.persistence.internal.databaseaccess.DatabaseAccessor;

import oracle.hst.platform.core.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class SessionAdapter
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** JPA Session event adapter
 ** <br>
 ** The JPA postAcquireConnection()/preReleaseConnection() events sets/deletes
 ** the 'client_identifier' userenv variable of the database session.
 ** <p>
 ** The database trigger will be able to extract the current user from here.
 ** <p>
 ** The other events are implemented only for the sake of log trace and logging
 ** only.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SessionAdapter extends SessionEventAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CLIENT = "client_identifier";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Logger logger = Logger.create(SessionAdapter.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SessionAdapter</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SessionAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postAcquireConnection (overridden)
  /**
   ** Set the client identifier.
   ** <p>
   ** This event is raised on when using the client/server sessions.
   ** <br>
   ** This event is raised after a connection is acquired from a connection
   ** pool.
   **
   ** @param  event              the session event describing the context
   **                            further.
   **                            <br>
   **                            Allowed Object is {@link SessionEvent}.
   */
  @Override
  public void postAcquireConnection(final SessionEvent event) {
    final String method     = "postAcquireConnection";
    final String identifier = (String)SessionProvider.get(CLIENT);
    this.logger.entering(SessionAdapter.class.getSimpleName(), method, identifier);
    // if identifier is defined, it is set for the session
    if (identifier != null) {
      DatabaseAccessor accessor = (DatabaseAccessor)event.getResult();
      try (Statement stmt = accessor.getConnection().createStatement()) {
        String sql = "BEGIN cg$sessions.set_identity('" + identifier + "'); " + " END;";
        stmt.execute(sql);
        this.logger.trace("SQL: " + sql);
      }
      catch (DatabaseException e) {
        this.logger.error("DB Error", e);
      }
      catch (SQLException e) {
        this.logger.error("SQL Error", e);
      }
    }
    this.logger.exiting(SessionAdapter.class.getSimpleName(), method, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preReleaseConnection (overridden)
  /**
   ** Delete the client identifier.
   ** <p>
   ** This event is raised on when using the server/client sessions.
   ** <br>
   ** This event is raised before a connection is released into a connection
   ** pool.
   **
   ** @param  event              the session event describing the context
   **                            further.
   **                            <br>
   **                            Allowed Object is {@link SessionEvent}.
   */
  @Override
  public void preReleaseConnection(final SessionEvent event) {
    final String method     = "preReleaseConnection";
    final String identifier = (String)SessionProvider.get(CLIENT);
    this.logger.entering(SessionAdapter.class.getSimpleName(), method, identifier);
    // if identifier is defined, it will be deleted from the session
    if (identifier != null) {
      DatabaseAccessor accessor = (DatabaseAccessor)event.getResult();
      try (Statement stmt = accessor.getConnection().createStatement()) {
        final String sql = "BEGIN cg$sessions.clear_identity; END;";
        stmt.execute(sql);
        this.logger.trace("SQL: " + sql);

      }
      catch (DatabaseException e) {
        this.logger.error("DB Error", e);
      }
      catch (SQLException e) {
        this.logger.error("SQL Error", e);
      }
    }
    logger.exiting(SessionAdapter.class.getSimpleName(), method, identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postReleaseClientSession (overridden)
  /**
   ** For security reasons ensure to remove 'client_identifier' local variable.
   ** <p>
   ** This event is raised on the client session after releasing.
   **
   ** @param  event              the session event describing the context
   **                            further.
   **                            <br>
   **                            Allowed Object is {@link SessionEvent}.
   */
  @Override
  public void postReleaseClientSession(final SessionEvent event) {
    final String method     = "postReleaseClientSession";
    final String identifier = (String)SessionProvider.get(CLIENT);
    this.logger.entering(SessionAdapter.class.getSimpleName(), method, identifier);

    // if the client exits the database, delete the ThreadLocal
    // 'client_identifier' variable
    this.logger.trace(method + ": " + identifier);
    SessionProvider.remove(CLIENT);
    logger.exiting(SessionAdapter.class.getSimpleName(), method, identifier);
  }
}
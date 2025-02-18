/*
    Oracle Deutschland BV & Co. KG

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

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   DatabaseCleanupFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    DatabaseCleanupFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.api;

import oracle.hst.platform.core.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;


/**
 * The DatabaseCleanupFacade to perform the database cleanup triggered by the scheduler.
 */
@Stateless
public class DatabaseCleanupFacade implements Serializable {

  private static final String category       = DatabaseCleanupFacade.class.getName();
  private static final Logger logger         = Logger.create(category);

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long serialVersionUID = -6228381523768226180L;

  /** A query to delete expired entries */
  private static final String QUERY          = "DELETE FROM pwr_requests WHERE created < ?";

  @Resource(name = "jdbc/idsDS")
  private DataSource dataSource;

  /**
   * Performs the query to the database and removes all the expired entries.
   *
   * @param before a {@link LocalDateTime} instance, before that all entries should be deleted.
   */
  public void cleanup(final LocalDateTime before) {
    final String method = "cleanup";
    logger.entering(category, method);

    try {
      try (Connection connection = dataSource.getConnection()) {
        PreparedStatement statement = connection.prepareStatement(QUERY);
        statement.setObject(1, before);
        logger.debug(String.format("SQL: %s", QUERY));

        int result = statement.executeUpdate();
        logger.info(String.format("%d records were deleted during the cleanup.", result));
      } catch (SQLException e) {
        logger.error("Error happened during the database cleanup.", e);
      }
    } finally {
      logger.exiting(category, method);
    }
  }
}

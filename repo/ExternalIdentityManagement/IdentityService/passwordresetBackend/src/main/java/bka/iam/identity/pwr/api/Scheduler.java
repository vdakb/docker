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

    File        :   Scheduler.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    Scheduler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.api;

import oracle.hst.platform.core.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The Scheduler to clean up unused and expired password reset requests from the database.
 */
@Startup
@Singleton
public class Scheduler implements Serializable {

  private static final String category       = Scheduler.class.getName();
  private static final Logger logger         = Logger.create(category);

  /** The official serial version ID which says cryptically which version we're compatible with */
  private static final long serialVersionUID = -4621797453627000811L;

  @Inject
  private DatabaseCleanupFacade facade;

  /**
   * A scheduled method runs every day at 2am to trigger the database cleanup process.
   */
  @SuppressWarnings("unused") //scheduled
  @Schedule(hour = "2", info = "Cleans up the expired request from the database, runs every day at 2am")
  public void cleanupTask() {
    final String method = "cleanupTask";
    logger.entering(category, method);

    try {
      final LocalDateTime before = LocalDateTime.now().minusMinutes(10);
      logger.debug(String.format("Cleaning up password reset request older than %s.", before));

      facade.cleanup(before);
    } finally {
      logger.entering(category, method);
    }
  }
}

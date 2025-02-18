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

    File        :   PersistenceFunction.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    PersistenceFunction.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import java.util.function.Function;

import javax.persistence.EntityManager;

////////////////////////////////////////////////////////////////////////////////
// interface PersistenceFunction
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Collection of callback function belonging to the transaction life cycle.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FunctionalInterface
public interface PersistenceFunction<T> extends Function<EntityManager, T> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeTransaction
  /**
   ** A callback method for the corresponding lifecycle event invoke before
   ** a transaction complets.
   ** <p>
   ** At the time this method is called, the {@link EntityManager} has been
   ** instantiated, but the transaction itself has not yet been started by the
   ** {@link EntityManager}. 
   */
	default void beforeTransaction() {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeTransaction
  /**
   ** A callback method for the corresponding lifecycle event invoke after
   ** a transaction completed.
   ** <p>
   ** At the time this method is called, the {@link EntityManager} has not been
   ** closed, but the transaction itself is either committed or rolled back. 
   */
	default void afterTransaction() {
  }
}
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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   LDAPImportListerner.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPImportListerner.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.util.Collection;

import javax.naming.directory.SearchResult;

import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// interface LDAPImportListerner
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The listener notified to process a particular batch of {@link SearchResult}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface LDAPImportListerner {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final int DEFAULT_BULK_SIZE = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulkSize
  /**
   ** Returns the size of a bulk the reconciliation target of this handler
   ** accepts.
   **
   ** @return                    the size of a bulk the reconciliation target of
   **                            this handler accepts.
   */
  int bulkSize();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  process
  /**
   ** Reconciles a particular bulk of entities of typ E.
   **
   ** @param  bulk               the {@link Collection} to reconcile.
   **
   ** @throws FeatureException   thrown if the processing of the event fails.
   */
  void process(final Collection<SearchResult> bulk)
    throws FeatureException;
}
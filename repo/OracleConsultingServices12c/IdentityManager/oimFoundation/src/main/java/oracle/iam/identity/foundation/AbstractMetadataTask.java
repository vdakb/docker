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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractMetadataTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AbstractMetadataTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;

import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// interface AbstractMetadataTask
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractMetadataTask</code> desclares the base functionality of a
 ** service end point for the Oracle Identity Manager that communicates with
 ** the associated Metadatstore Partition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface AbstractMetadataTask extends Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSession
  /**
   ** Creates a session to the Metadata Store.
   **
   ** @return                    the created {@link MDSSession}.
   */
  MDSSession createSession();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSession
  /**
   ** Creates a session to the Metadata Store with specific session options and
   ** no state handlers.
   **
   ** @param  option             the {@link SessionOptions} to create the
   **                            {@link MDSSession}.
   **
   ** @return                    the created {@link MDSSession}.
   */
  MDSSession createSession(final SessionOptions option);
}
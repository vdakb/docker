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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Descriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Descriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-03-11  DSteding    First release version
*/

package oracle.iam.identity.foundation.provisioning;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskDescriptor;

////////////////////////////////////////////////////////////////////////////////
// class Descriptor
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Descriptor</code> is intend to use where outbound attributes of an
 ** Oracle Identity Manager Object (core or user defined) are mapped to a
 ** provisioning target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class Descriptor extends TaskDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Descriptor</code> which is associated with the
   ** specified task.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code> wrapper.
   */
  public Descriptor(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }
}
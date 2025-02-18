/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   Configuration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Configuration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.model;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.AbstractServiceTask;

////////////////////////////////////////////////////////////////////////////////
// class Configuration
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** An <code>Configuration</code> is the value holder for the entitlement
 ** mapping from the source to the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Configuration extends AbstractLookup {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Configuration</code> that specifies the properties
   ** of the source and target {@link Resource}.
   **
   ** @param  task               the {@link AbstractServiceTask} which has
   **                            instantiated <code>Lookup Definition</code>
   **                            configuration wrapper.
   ** @param  instanceName       the name of the <code>Metadata Descriptor</code>
   **                            instance where this wrapper belongs to.
   **                            Allowed object {@link String}.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in  Oracle Identity Manager metadata
   **                            entries or one or more attributes are missing
   **                            on the <code>Metadata Descriptor</code>.
   */
  public Configuration(final AbstractServiceTask task, final String instanceName)
    throws TaskException {

    super(task, instanceName);
  }
}
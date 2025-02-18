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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ObjectRegistry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ObjectRegistry.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import oracle.hst.deployment.ServiceFrontend;

import oracle.hst.deployment.task.Marshaller;

////////////////////////////////////////////////////////////////////////////////
// abstract class ObjectRegistry
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle object export/import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ObjectRegistry extends Marshaller {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectRegistry</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated this
   **                            service.
   ** @param  indent             <code>true</code> to intend the XML in the
   **                            phase of transformation to a string.
   ** @param  indentNumber       the number of spaces to indent a child node in
   **                            the transformed string.
   */
  protected ObjectRegistry(final ServiceFrontend frontend, final boolean indent, final int indentNumber) {
    // ensure inheritance
    super(frontend, indent, indentNumber);
  }
}
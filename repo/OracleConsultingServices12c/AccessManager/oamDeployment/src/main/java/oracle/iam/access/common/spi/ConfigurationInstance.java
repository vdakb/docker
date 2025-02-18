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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ConfigurationInstance.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigurationInstance.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// abstract class ConfigurationInstance
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>ConfigurationInstance</code> provides properties for the configuration
 ** JMX beans of Oracle Access Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class ConfigurationInstance extends AbstractInstance {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ConfigurationInstance</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ConfigurationInstance() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createParameter
  /**
   ** Returns operation's parameter string accordingly to the create signature
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  protected abstract Object[] createParameter();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSignature
  /**
   ** Returns operation's signature string accordingly to the create parameter
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  protected abstract String[] createSignature();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyParameter
  /**
   ** Returns operation's parameter string accordingly to the modify signature
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  protected abstract Object[] modifyParameter();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifySignature
  /**
   ** Returns operation's signature string accordingly to the modify parameter
   ** arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  protected abstract String[] modifySignature();
}
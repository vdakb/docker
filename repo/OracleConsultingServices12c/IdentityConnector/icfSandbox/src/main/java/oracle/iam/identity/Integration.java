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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Test Case Skeleton

    File        :   Integration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Integration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class Integration
// ~~~~~ ~~~~~~~~~~~
/**
 ** The basic test case providing common functionality used leveraging the
 ** <code>Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Integration extends Flavor {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Integration</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Integration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link ConnectorException} thrown from the
   **                            <code>Connector</code>.
   */
  public static void failed(final ConnectorException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    failed(cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a error code and a detailed message.
   **
   ** @param  cause              the {@link TaskException} thrown from the
   **                            <code>Integration</code>.
   */
  public static void failed(final TaskException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    // this message needs to be split by a "::" character sequence
    failed(cause.code().concat("::").concat(cause.getLocalizedMessage()));
  }
}
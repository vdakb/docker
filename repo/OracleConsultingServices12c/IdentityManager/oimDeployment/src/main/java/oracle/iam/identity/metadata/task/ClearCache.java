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
    Subsystem   :   Metadata Service Utilities 11g

    File        :   ClearCache.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ClearCache.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

////////////////////////////////////////////////////////////////////////////////
// class ClearCache
// ~~~~~ ~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to clear the Metadata Cache in an instance of
 ** Oracle Identity Manager driven by Oracle Metadata Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ClearCache extends Metadata {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String   OPERATION = "clearCache";
  protected static final String[] PARAMETER = null;
  protected static final String[] SIGNATURE = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ClearCache</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ClearCache() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ClearCache</code> event handler that use the specified
   ** {@link Metadata} task providing the JMX bean properties.
   **
   ** @param  other              {@link Metadata} task providing the JMX bean
   **                            properties.
   */
  public ClearCache(final Metadata other) {
    // ensure inheritance
    super(other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation (AbstractInvokerTask)
  /**
   ** Returns the <code>operation</code> name this task will execute.
   **
   ** @return                    the <code>operation</code> name this task will
   **                            execute.
   */
  @Override
  protected final String operation() {
    return OPERATION;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter (AbstractInvokerTask)
  /**
   ** Returns operation's parameter string.and signature arrays.
   **
   ** @return                    the operation's parameter string.
   */
  @Override
  protected final Object[] parameter() {
    return PARAMETER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signature (AbstractInvokerTask)
  /**
   ** Returns operation's signature arrays.
   **
   ** @return                    the operation's signature arrays.
   */
  //
  @Override
  protected final String[] signature() {
    return SIGNATURE;
  }
}
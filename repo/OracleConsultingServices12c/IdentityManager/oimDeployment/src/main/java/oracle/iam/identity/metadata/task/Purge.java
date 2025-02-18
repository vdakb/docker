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

    File        :   Purge.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Purge.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

////////////////////////////////////////////////////////////////////////////////
// class Purge
// ~~~~~ ~~~~~
/**
 ** Invokes the Runtime JMX Bean to removes old versions from version history of
 ** unlabeled documents on the Identity Manager metadata repository
 ** partition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Purge extends Metadata {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the operation this task invokes on the server side */
  protected static final String   OPERATION = " purgeMetadata";

  protected static final String[] SIGNATURE = {
    Long.class.getName()    // 0: olderThan
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Number of seconds which all unlabeled documents will be purged if they are
   ** older than. If this parameter is missing or specified as 0, all versions
   ** except for the top version will be purged.
   */
  private long olderThan = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Purge</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Purge() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Purge</code> event handler that use the specified
   ** {@link Metadata} task providing the JMX bean properties.
   **
   ** @param  other              {@link Metadata} task providing the JMX bean
   **                            properties.
   */
  public Purge(final Metadata other) {
    // ensure inheritance
    super(other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOlderThan
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** cancelOnException.
   **
   ** @param  olderThan          number of seconds which all unlabeled documents
   **                            will be purged if they are older than.
   **                            <p>
   **                            If this parameter is missing or specified as
   **                            <code>0</code>, all versions except for the
   **                            tip version will be purged.
   */
  public void setOlderThan(final long olderThan) {
    this.olderThan = olderThan;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   olderThan
  /**
   ** Returns the number of seconds which all unlabeled documents will be purged
   ** if they are older than.
   **
   ** @return                    number of seconds which all unlabeled documents
   **                            will be purged if they are older than.
   */
  protected final long olderThan() {
    return this.olderThan;
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
    Object[] parameter = new Object[]  {
      this.olderThan // 0: olderThan
    };
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signature (AbstractInvokerTask)
  /**
   ** Returns operation's signature arrays.
   **
   ** @return                    the operation's signature arrays.
   */
  @Override
  protected final String[] signature() {
    return SIGNATURE;
  }
}
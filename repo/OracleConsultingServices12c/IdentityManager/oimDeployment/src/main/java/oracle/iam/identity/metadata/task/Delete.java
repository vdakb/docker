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

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Delete.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** Invokes the Runtime JMX Bean to delete documents from Identity Manager
 ** metadata repository partition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Delete extends Transactional {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the operation this task invokes on the server side */
  protected static final String OPERATION = "deleteMetadata";

  protected static final String[] SIGNATURE         = {
    String[].class.getName()  // 1: docs
  , String[].class.getName()  // 2: restrictCustTo
  , boolean.class.getName()   // 3: excludeAllCust
  , boolean.class.getName()   // 4: excludeBaseDocs
  , boolean.class.getName()   // 5: excludeExtendedMetadata
  , boolean.class.getName()   // 6: cancelOnException
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A Boolean value indicating whether to stop the deletion if an exception is
   ** encountered.
   ** <p>
   ** If the repository is a database repository, the incomplete deletion will
   ** be rolled back.
   */
  private boolean cancelOnException = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Delete</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Delete() {
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
  public Delete(final Metadata other) {
    // ensure inheritance
    super(other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCancelOnException
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** cancelOnException.
   **
   ** @param  cancelOnException  a Boolean value indicating whether to stop the
   **                            delete if an exception is encountered.
   **                            <p>
   **                            If the target repository is a database
   **                            repository, the incomplete delete  will be
   **                            rolled back.
   */
  public void setCancelOnException(final boolean cancelOnException) {
    this.cancelOnException = cancelOnException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancelOnException
  /**
   ** Returns the value indicating whether to stop the delete if an exception is
   ** encountered.
   **
   ** @return                    the value indicating whether to stop the delete
   **                            if an exception is encountered.
   */
  protected final boolean cancelOnException() {
    return this.cancelOnException;
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
    Object[] parameter = new Object[] {
      // 0: docs
      this.documentPath()
      // 1: restrictCustTo
    , this.restrictedPath()
      // 2: excludeAllCust
    , this.excludeAllCustomization()
      // 3: excludeBaseDocs
    , this.excludeBaseDocuments()
      // 4: excludeExtendedMetadata
    , this.excludeExtendedMetadata()
      // 5: fromLabel
    , this.cancelOnException
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws ServiceException {

    final Object[] bulk = parameter();
    final Object[] single = parameter();
    final String[] path = new String[1];
    single[0] = path;
    for (String file : (String[])bulk[0]) {
      path[0] = file;
      invoke(operation(), single, signature());
    }
  }
}
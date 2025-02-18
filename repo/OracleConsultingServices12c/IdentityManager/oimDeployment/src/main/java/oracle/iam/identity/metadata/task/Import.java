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

    File        :   Import.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Import.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Import
// ~~~~~ ~~~~~~
/**
 ** Invokes the Runtime JMX Bean to imports documents to Identity Manager
 ** metadata repository partition from the specified location.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The file(s) to import must accessible from the file system Identity Manager
 ** is running on.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Import extends Transactional {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the operation this task invokes on the server side */
  protected static final String   OPERATION         = "importMetadata";

  protected static final String[] SIGNATURE         = {
    String.class.getName()    // 0: fromLocation
  , String[].class.getName()  // 1: docs
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
   ** A valid, existing absolute path to a directory or archive in the file
   ** system from which the selected documents will be imported.
   ** The directory must be accessible from the machine where the application is
   ** running.
   */
  private String                  fromLocation      = null;

  /**
   ** A Boolean value indicating whether to stop the import if an exception is
   ** encountered.
   ** <p>
   ** If the repository is a database repository, the incomplete import will
   ** be rolled back.
   */
  private boolean                 cancelOnException = true;

  /**
   ** A Boolean value indicating whether to purge any existing file before the
   ** import starts.
   */
  private boolean                 purgeBefore       = false;

  /**
   ** A Boolean value indicating whether to purge any existing file after the
   ** import is completed.
   */
  private boolean                 purgeAfter       = false;

  /**
   ** A Boolean value indicating whether to delete any existing file before it
   ** will be imported.
   */
  private boolean                 deleteFirst       = false;

  /**
   ** A Boolean value indicating whether to clear the cache after all files are
   ** imported.
   */
  private boolean                 clearCache        = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Import</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Import() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFromLocation
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>fromLocation</code>.
   **
   ** @param  fromLocation       a valid, existing absolute path to a directory
   **                            or archive in the file system from which the
   **                            selected documents will be imported. The
   **                            directory must be accessible from the machine
   **                            where the application is running.
   */
  public void setFromLocation(final String fromLocation) {
    this.fromLocation = fromLocation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCancelOnException
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>cancelOnException</code>.
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
  // Method:   setPurgeBefore
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>purgeBefore</code>.
   **
   ** @param  purgeBefore        a Boolean value indicating that any deleted
   **                            file has to be purged before the import
   **                            operation starts.
   */
  public void setPurgeBefore(final boolean purgeBefore) {
    this.purgeBefore = purgeBefore;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   purgeBefore
  /**
   ** Returns the value indicating whether any deleted file has to be purged
   ** before the import operation starts.
   **
   ** @return                    the value indicating whether any deleted
   **                            file has to be purged before the import
   **                            operation starts.
   */
  protected final boolean purgeBefore() {
    return this.purgeBefore;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPurgeAfter
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>purgeAfter</code>.
   **
   ** @param  purgeAfter         a Boolean value indicating that any deleted
   **                            file has to be purged after the import
   **                            operation completes.
   */
  public void setPurgeAfter(final boolean purgeAfter) {
    this.purgeAfter = purgeAfter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   purgeAfter
  /**
   ** Returns the value indicating whether any deleted file has to be purged
   ** after the import operation completes.
   **
   ** @return                    the value indicating whether any deleted
   **                            file has to be purged after the import
   **                            operation completes.
   */
  protected final boolean purgeAfter() {
    return this.purgeAfter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDeleteFirst
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>deleteFileFirst</code>.
   **
   ** @param  deleteFirst        a Boolean value indicating whether each file
   **                            has to be deleted first.
   **                            <p>
   **                            If the target repository is a database
   **                            repository, the incomplete delete  will be
   **                            rolled back.
   */
  public void setDeleteFirst(final boolean deleteFirst) {
    this.deleteFirst = deleteFirst;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteFirst
  /**
   ** Returns the value indicating whether each file has to be deleted first.
   **
   ** @return                    the value indicating whether each file has to
   **                            be deleted first.
   */
  protected final boolean deleteFirst() {
    return this.deleteFirst;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClearCache
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>clearCache</code>.
   **
   ** @param  clearCache        a Boolean value indicating that the metadata
   **                            cache should be cleared after the import
   **                            operations completed.
   */
  public void setClearCache(final boolean clearCache) {
    this.clearCache = clearCache;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearCache
  /**
   ** Returns the value indicating whether each file has to be deleted first.
   **
   ** @return                    the value indicating whether the metadata cahce
   **                            should be cleared after the import operations
   **                            completed.
   */
  protected final boolean clearCache() {
    return this.clearCache;
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
      // 1: fromLocation
      this.fromLocation
      // 2: docs
    , this.documentPath()
      // 3: restrictCustTo
    , this.restrictedPath()
      // 4: excludeAllCust
    , this.excludeAllCustomization()
      // 5: excludeBaseDocs
    , this.excludeBaseDocuments()
      // 6: excludeExtendedMetadata
    , this.excludeExtendedMetadata()
      // 7: fromLabel
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke (overriddden)
  /**
   ** @param  operation          the name of the operation to invoke.
   ** @param  parameter          an array containing the parameters to be set
   **                            when the operation is invoked.
   ** @param  signature          an array containing the signature of the
   **                            operation. The class objects will be loaded
   **                            using the same class loader as the one used for
   **                            loading the MBean on which the operation was
   **                            invoked.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the MBean specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the MBean server.
   */
  @Override
  protected Object invoke(final String operation, final Object[] parameter, final String[] signature)
    throws ServiceException {

    info(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE, operation));
    Object result = null;
    try {
      if (this.purgeBefore) {
        Purge preProcessor = new Purge(this);
        this.connection().invoke(preProcessor.objectName(), preProcessor.operation(), preProcessor.parameter(), preProcessor.signature());
      }

      if (this.deleteFirst) {
        Delete preProcessor = new Delete(this);
        preProcessor.setCancelOnException(this.cancelOnException);
        preProcessor.documentPath(this.documentPath());
        preProcessor.restrictedPath(this.restrictedPath());
        preProcessor.setExcludeAllCust(this.excludeAllCustomization());
        preProcessor.setExcludeBaseDocs(this.excludeBaseDocuments());
        preProcessor.setExcludeExtendedMetadata(this.excludeExtendedMetadata());

        this.connection().invoke(preProcessor.objectName(), preProcessor.operation(), preProcessor.parameter(), preProcessor.signature());
      }
      result = this.connection().invoke(objectName(), operation, parameter, signature);

      if (this.purgeAfter) {
        Purge postProcessor = new Purge(this);
        this.connection().invoke(postProcessor.objectName(), postProcessor.operation(), postProcessor.parameter(), postProcessor.signature());
      }

      if (this.clearCache) {
        ClearCache postProcessor = new ClearCache(this);
        this.connection().invoke(postProcessor.objectName(), postProcessor.operation(), postProcessor.parameter(), postProcessor.signature());
      }
      info(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_SUCCESS, operation));
    }
    catch (Exception e) {
      error(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, operation));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    return result;
  }
}
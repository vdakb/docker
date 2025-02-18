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

    File        :   Export.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Export.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Export
// ~~~~~ ~~~~~~
/**
 ** Invokes the Runtime JMX Bean to exports documents from Identity Manager
 ** metadata repository partition to the specified location.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The file(s) to export are accessible from the file system Identity Manager
 ** is running on only.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Export extends Transactional {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the operation this task invokes on the server side */
  protected static final String   OPERATION           = "exportMetadata";

  protected static final String[] SIGNATURE           = {
    String.class.getName()    // 0: toLocation
  , boolean.class.getName()   // 1: createSubDir
  , String[].class.getName()  // 2: docs
  , String[].class.getName()  // 3: restrictCustTo
  , boolean.class.getName()   // 4: excludeAllCust
  , boolean.class.getName()   // 5: excludeBaseDocs
  , boolean.class.getName()   // 6: excludeExtendedMetadata
  , String.class.getName()    // 7: fromLabel
  , String.class.getName()    // 8: toLabel
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A valid absolute path to a directory or archive in the file system to
   ** which the selected documents will be exported. This location must be
   ** accessible from the machine where the application is running. If it does
   ** not exist, a directory will be created except that when the name ends
   ** with ".jar", ".JAR", ".zip", or ".ZIP", an archive will be created.
   ** Exporting metadata to an existing archive will overwrite the existing
   ** file.
   */
  private String                  toLocation           = null;

  /**
   ** A Boolean value indicating whether a sub-directory with the partition name
   ** will be created under "toLocation" for export.
   */
  private boolean                 createSubDirectories = true;

  /**
   ** The name of the source label. If "targetLabel" is not specified, this
   ** parameter causes documents from the source metadata repository partition
   ** that are associated with this label to be exported. If "targetLabel" is
   ** also specified, this parameter allows any documents that have changed from
   ** the given label to the "targetLabel" to be exported. If it is not
   ** specified, the default is to export the top version of documents. This
   ** parameter is only supported if the source repository is a database
   ** repository.
   */
  private String                  sourceLabel          = null;

  /**
   ** The name of the target label. This parameter allows any documents that
   ** have changed from the "sourceLabel" to the given label to be exported. It
   ** shall only be specified when the "sourceLabel" is specified. It is only
   ** supported if the source repository is a database repository.
   */
  private String                  targetLabel          = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Export</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Export() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setToLocation
  /**
   ** Call by the ANT kernel to inject the argument for parameter toLocation.
   ** <code>toLocation</code>.
   **
   ** @param  toLocation         the valid absolute path to a directory or
   **                            archive in the file system to which the
   **                            selected documents will be exported.
   */
  public void setToLocation(final String toLocation) {
    this.toLocation = toLocation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSourceLabel
  /**
   ** Call by the ANT kernel to inject the argument for parameter toLocation.
   ** <code>sourceLabel</code>.
   **
   ** @param  sourceLabel        the name of the source label
   */
  public void setSourceLabel(final String sourceLabel) {
    this.sourceLabel = sourceLabel;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTargetLabel
  /**
   ** Call by the ANT kernel to inject the argument for parameter toLocation.
   ** <code>targetLabel</code>.
   **
   ** @param  targetLabel        the name of the target label
   */
  public void setTargetLabel(final String targetLabel) {
    this.targetLabel = targetLabel;
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
   ** Returns operation's parameter string accordingly to the signature arrays.
   **
   ** @return                    the operation's parameter strings.
   */
  @Override
  protected final Object[] parameter() {
    final Object[] parameter = new Object[]  {
      // 0: toLocation
      this.toLocation
      // 1: createSubDir
    , this.createSubDirectories
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
    , this.sourceLabel
      // 8: toLabel
    , this.targetLabel
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
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    if (StringUtility.isEmpty(this.toLocation))
      handleAttributeMissing("toLocation");

    // ensure inheritance
    super.validate();
  }
}
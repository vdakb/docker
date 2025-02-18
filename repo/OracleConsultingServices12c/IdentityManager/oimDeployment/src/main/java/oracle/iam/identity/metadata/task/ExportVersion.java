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

    File        :   ExportVersion.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ExportVersion.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

////////////////////////////////////////////////////////////////////////////////
// class ExportVersion
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to exports document versions from Identity
 ** Manager metadata repository partition to the specified location.
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
public class ExportVersion extends Transactional {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the operation this task invokes on the server side */
  protected static final String    OPERATION          = "exportDocVersions";

  protected static final String[] SIGNATURE           = {
    String.class.getName()   // 0: toArchive
  , String.class.getName()   // 1: doc
  , String[].class.getName() // 2: restrictCustTo
  , boolean.class.getName()  // 3: excludeBaseDocs
  , boolean.class.getName()  // 4: excludeExtendedMetadata
  , long.class.getName()     // 5: laterThan
  , int.class.getName()      // 6: maxVersions
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A valid absolute path to an archive in the file system to which the
   ** document versions will be exported. This location must be accessible from
   ** the machine where the application is running. Exporting to an existing
   ** archive will overwrite the existing file.
   */
  private String                  toArchive           = null;

  /**
   ** Document versions considered for export if they are changed in repository
   ** in last specified number of minutes. If <code>0</code>, all versions will
   ** be considered.
   */
  private long                    laterThan           = 0;

  /**
   ** Maximum number of document versions to be exported (counting from the
   ** latest). If <code>0</code>, no limit to the maximum versions.
   */
  private int                     maxVersions         = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ExportVersion</code> event handler that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExportVersion() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setToArchive
  /**
   ** Call by the ANT kernel to inject the argument for parameter toLocation.
   ** <code>toLocation</code>.
   **
   ** @param  toArchive          the valid absolute path to an archive in the
   **                            file system to which the document versions will
   **                            be exported.
   */
  public void setToArchive(final String toArchive) {
    this.toArchive = toArchive;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDocumentPath
  /**
   ** Call by the ANT kernel to inject the argument for parameter toLocation.
   ** <code>document</code>.
   **
   ** @param  path               the full qualified document with package path
   **                            whose version history is to be exported.
   */
  public void setDocumentPath(final String path) {
    documentPath(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLaterThan
  /**
   ** Call by the ANT kernel to inject the argument for parameter toLocation.
   ** <code>laterThan</code>.
   **
   ** @param  laterThan           the document versions considered for export if
   **                             they are changed in repository in last
   **                             specified number of minutes.
   */
  public void setLaterThan(final Long laterThan) {
    this.laterThan = laterThan;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMaxVersions
  /**
   ** Call by the ANT kernel to inject the argument for parameter toLocation.
   ** <code>maxVersions</code>.
   **
   ** @param  maxVersions         the maximum number of document versions to be
   **                             exported (counting from the latest).
   */
  public void setMaxVersions(final Integer maxVersions) {
    this.maxVersions = maxVersions;
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
      // 0: toArchive
      this.toArchive
      // 1: doc
    , this.documentPath()[0]
      // 2: restrictCustTo
    , this.restrictedPath()
      // 3: excludeBaseDocs
    , this.excludeBaseDocuments()
      // 4: excludeExtendedMetadata
    , this.excludeExtendedMetadata()
      // 5: laterThan
    , this.laterThan
      // 5: maxVersions
    , this.maxVersions
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
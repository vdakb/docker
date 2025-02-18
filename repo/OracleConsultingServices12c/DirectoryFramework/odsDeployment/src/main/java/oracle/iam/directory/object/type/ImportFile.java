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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities

    File        :   ImportFile.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ImportFile.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.object.type;

import org.apache.tools.ant.types.FileList;

import oracle.iam.directory.common.spi.instance.FileInstance;

////////////////////////////////////////////////////////////////////////////////
// class ImportFile
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>ImportFile</code> is a special {@link FileList.FileName} that will be
 ** imported to a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ImportFile extends FileList.FileName {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final private FileInstance delegate = new FileInstance();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ImportFile</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ImportFile(){
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

   //////////////////////////////////////////////////////////////////////////////
  // Method:   setFormat
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>format</code>.
   **
   ** @param  format             the output format
   */
  public void setFormat(final Format format) {
    this.delegate.format(format.type());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>version</code>.
   **
   ** @param  version            the version in the created deployment.
   */
  public void setVersion(final String version) {
    this.delegate.version(version);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link FileInstance} delegate of Directory Service object to
   ** handle.
   **
   ** @return                    the {@link FileInstance} delegate.
   */
  public final FileInstance instance() {
    return this.delegate;
  }
}
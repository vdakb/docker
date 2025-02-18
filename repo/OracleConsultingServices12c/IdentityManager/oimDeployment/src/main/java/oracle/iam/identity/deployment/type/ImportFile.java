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

    File        :   ImportFile.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ImportFile.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import java.util.List;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileList.FileName;

////////////////////////////////////////////////////////////////////////////////
// class ImportFile
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>ImportFile</code> is a special {@link FileList.FileName} that will be
 ** imported into Identity Manager.
 ** <p>
 ** This specialization of {@link FileList.FileName} can recieve
 ** {@link Substitution}s that has to be applied on the content of the file
 ** during an import operation
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ImportFile extends FileName {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String DATA_TYPE = "importfile";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private List<Substitution> substitution = new ArrayList<Substitution>();

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
  public ImportFile() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSubstitution
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Substitution}.
   **
   ** @param  physicalType       the name of the category aka the physical type
   **                            of Identity Manager.
   ** @param  origin             the subject of substitution.
   ** @param  replacement        the substitution value.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>ImportFile</code>
   */
  public void addSubstitution(final String physicalType, final String origin, final String replacement)
    throws BuildException {

    addConfiguredSubstitution(new Substitution(physicalType, origin, replacement));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredSubstitution
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Substitution}.
   **
   ** @param  substitution       the subject of substitution.
   **
   ** @throws BuildException     if this instance is referencing a declared
   **                            <code>ImportFile</code>
   */
  public void addConfiguredSubstitution(final Substitution substitution)
    throws BuildException {

    this.substitution.add(substitution);
  }
}
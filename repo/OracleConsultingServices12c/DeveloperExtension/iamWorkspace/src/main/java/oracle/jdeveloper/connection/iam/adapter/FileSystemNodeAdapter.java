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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   FileSystemNodeAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FileSystemNodeAdapter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.util.Map;
import java.util.List;

import javax.naming.Referenceable;

import oracle.adf.share.jndi.AdfJndiContext;

////////////////////////////////////////////////////////////////////////////////
// final class FileSystemNodeAdapter
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public final class FileSystemNodeAdapter extends EndpointContextAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FileSystemNodeAdapter</code> to handle the specified
   ** context.
   **
   ** @param  context            the {@link AdfJndiContext} context to handle.
   */
  public FileSystemNodeAdapter(final AdfJndiContext context) {
    // ensure inheritance
    super(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionNames (EndpointContextAdapter)
  /**
   ** Returns the collection of connection names belonging to
   ** {@link DirectoryType#CLASS}.
   **
   ** @return                    the collection of connection names belonging to
   **                            {@link DirectoryType#CLASS}.
   */
  @Override
  public List<String> connectionNames() {
    return connectionNames(FileSystemType.CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionProviders (EndpointContextAdapter)
  /**
   ** Returns the collection of connection providers belonging to a specific
   ** {@link DirectoryType}.
   **
   ** @return                    the collection of connection providers
   **                            belonging to a specific {@link DirectoryType}.
   */
  @Override
  public List<Map.Entry<String, Referenceable>> connectionProviders() {
    return connectionProviders(FileSystemType.CLASS);
  }
}
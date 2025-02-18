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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryNodeAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryNodeAdapter.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.util.Map;
import java.util.List;

import javax.naming.Referenceable;

import oracle.adf.share.jndi.AdfJndiContext;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryNodeAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryNodeAdapter extends EndpointContextAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryNodeAdapter</code> to handle the specified
   ** context.
   **
   ** @param  context            the {@link AdfJndiContext} context to handle.
   */
  public DirectoryNodeAdapter(final AdfJndiContext context) {
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
    return connectionNames(DirectoryType.CLASS);
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
    return connectionProviders(DirectoryType.CLASS);
  }
}
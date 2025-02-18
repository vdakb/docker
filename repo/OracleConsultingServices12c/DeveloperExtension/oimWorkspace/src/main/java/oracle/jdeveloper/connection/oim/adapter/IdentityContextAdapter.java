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
    Subsystem   :   Identity Manager Facility

    File        :   IdentityContextAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityContextAdapter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.adapter;

import java.util.Map;
import java.util.List;

import javax.naming.Referenceable;

import oracle.adf.share.jndi.AdfJndiContext;

import oracle.jdeveloper.connection.iam.model.Endpoint;

import oracle.jdeveloper.connection.iam.adapter.EndpointContextAdapter;

////////////////////////////////////////////////////////////////////////////////
// class IdentityContextAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityContextAdapter extends EndpointContextAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityContextAdapter</code> to handle the specified
   ** context.
   **
   ** @param  context            the {@link AdfJndiContext} context to handle.
   */
  public IdentityContextAdapter(final AdfJndiContext context) {
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
   ** {@link IdentityType#CLASS}.
   **
   ** @return                    the collection of connection names belonging to
   **                            {@link IdentityType#CLASS}.
   */
  @Override
  public List<String> connectionNames() {
    return connectionNames(IdentityType.CLASS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionProviders (EndpointContextAdapter)
  /**
   ** Returns the collection of connection providers belonging to a specific
   ** {@link IdentityType}.
   **
   ** @return                    the collection of connection providers
   **                            belonging to a specific {@link IdentityType}.
   */
  @Override
  public List<Map.Entry<String, Referenceable>> connectionProviders() {
    return connectionProviders(IdentityType.CLASS);
  }
}
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
    Subsystem   :   Identity Manager Facility

    File        :   IdentityDirectory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityDirectory.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.104 2023-08-13  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.adapter;

import java.util.Locale;

import oracle.jdeveloper.connection.iam.adapter.FileSystemType;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class IdentityDirectory
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Describes a type of connection and provides the name of the
 ** InitialContextFactory that should be used to create an resource catalog
 ** adapter for a provider of this type.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.104
 ** @since   12.2.1.3.42.60.104
 */
public class IdentityDirectory extends FileSystemType {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityDirectory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityDirectory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName (ConnectionType)
  /**
   ** Returns the localized display name of the connection type.
   **
   ** @param  local              requested Locale for the display name.
   **
   ** @return                    the display name of the connection type in the
   **                            requested {@link Locale}.
   */
  @Override
  public String getDisplayName(final Locale local) {
    return Manifest.string(Manifest.IDENTITY_DEPLOY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (ConnectionType)
  /**
   ** Returns the localized description of the connection type.
   **
   ** @param  local              requested {@link Locale} for the description.
   **
   ** @return                    the description of the connection type in the
   **                            requested {@link Locale}.
   */
  @Override
  public String getDescription(final Locale local) {
    return Manifest.string(Manifest.IDENTITY_DEPLOY_DESCRIPTION);
  }
}
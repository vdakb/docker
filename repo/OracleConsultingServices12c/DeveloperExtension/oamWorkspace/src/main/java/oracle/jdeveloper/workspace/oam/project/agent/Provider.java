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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Access Manager Facility

    File        :   Provider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Provider.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.project.agent;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

import oracle.jdeveloper.workspace.oam.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~
/**
 ** This is a properties object, used as the data model for access
 ** project properties.
 ** <p>
 ** This class serves as a implementation of an adapter pattern to bridge
 ** JavaBeans-style getter and setter methods with HashStructure storage related
 ** to deployments of Oracle Access Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public final class Provider extends DataStructureAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Provider()" and enforces use of the public factory
   ** method below.
   */
  private Provider(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link DataStructureAdapter}.
   ** <p>
   ** This method tries to find an existing instance of a
   ** {@link DataStructureAdapter} using {@link Manifest#KEY}.
   ** <p>
   ** If none can be found, this method will create a new HashStructure, first
   ** attempting to wire that into the specified name in the
   ** {@link PropertyStorage} or, failing that, leave the new HashStructure
   ** disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   **
   ** @param  storage            the {@link PropertyStorage} providing the
   **                            desired {@link Provider}.
   **
   ** @return                    the {@link Provider} of the mapped to
   **                            {@link PropertyStorage}.
   **
   ** @throws NullPointerException if <code>storage</code> is <code>null</code>.
   */
  public static Provider instance(final PropertyStorage storage) {
    return new Provider(findOrCreate(storage, Manifest.KEY));
  }
}
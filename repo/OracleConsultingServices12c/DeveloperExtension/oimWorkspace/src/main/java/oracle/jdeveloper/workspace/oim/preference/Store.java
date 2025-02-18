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
    Subsystem   :   Identity Manager Facility

    File        :   Store.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Store.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.preference;

import java.net.URL;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.jdeveloper.workspace.iam.preference.FeaturePreference;

import oracle.jdeveloper.workspace.oim.Manifest;
import oracle.jdeveloper.workspace.oim.Library;

////////////////////////////////////////////////////////////////////////////////
// class Store
// ~~~~~ ~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Store extends FeaturePreference {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the key for the property to locate the directory where the Oracle Identity
   ** Manager Design Console is located.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** preferences, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String CONSOLE = "oim.home";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Store</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new FeaturePreference()" and enforces use of the public factory method below.
   */
  private Store(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   console
  /**
   ** Called to inject the argument for instance attribute
   ** <code>console</code>.
   **
   ** @param  console            the path of the file where the Oracle Identity
   **                            Manager Designe Console is installed.
   */
  public void console(final URL console) {
    this._hash.putURL(CONSOLE, console);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   console
  /**
   ** Returns the instance attribute <code>console</code>.
   **
   ** @return                    the path of the file where the Oracle Identity
   **                            Manager Designe Console is installed.
   */
  public URL console() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return this._hash.getURL(CONSOLE, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspace (DefaultFeaturePreference)
  /**
   ** Returns the instance attribute <code>wks.home</code>.
   **
   ** @return                    the {@link URL} of the workspace directory
   **                            where the Oracle Identity Manager development
   **                            will happens.
   */
  @Override
  public String workspace() {
    // the second parameter to string() is a default value. Defaults are stored
    // in the persistent storage file using a "placeholder".
    return string(WORKSPACE, Library.DEFAULT_FOLDER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release (DefaultFeaturePreference)
  /**
   ** Returns the instance attribute <code>release</code>.
   **
   ** @return                    the release of Oracle Identity Manager used for
   **                            development.
   */
  @Override
  public String release() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(RELEASE, Library.DEFAULT_RELEASE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link Store}.
   ** <p>
   ** This method tries to find an existing instance of a
   ** {@link FeaturePreference} using {@link Manifest#KEY}.
   ** <p>
   ** If none can be found, this method will create a new HashStructure, first
   ** attempting to wire that into the specified name in the
   ** {@link PropertyStorage} or, failing that, leave the new HashStructure
   ** disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   ** <p>
   ** Factory method takes a {@link PropertyStorage} (instead of HashStructure
   ** directly). This decouples the origin of the HashStructure and allows the
   ** future possibility of resolving preferences through multiple layers of
   ** HashStructure.
   ** <p>
   ** Classes/methods that currently implement/return PropertyStorage:
   ** <ul>
   **   <li>oracle.ide.config.Preferences
   **   <li>oracle.ide.model.Project
   **   <li>oracle.ide.model.Workspace
   **   <li>oracle.ide.panels.TraversableContext.getPropertyStorage()
   ** </ul>
   **
   ** @param  storage            the {@link PropertyStorage} providing the
   **                            desired {@link Store}.
   **
   ** @return                    the {@link Store} of the mapped
   **                            {@link PropertyStorage}.
   */
  public static Store instance(final PropertyStorage storage) {
    return new Store(findOrCreate(storage, Manifest.KEY));
  }
}
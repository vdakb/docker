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

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   FeaturePreference.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    FeaturePreference.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.iam.preference;

import java.net.URL;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeaturePreference
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This is a preferences object, used as the data model for Identity and Access
 ** Management preferences
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
public abstract class FeaturePreference extends DataStructureAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the key for the property to specify the support for Maven build features.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** preferences, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String MAVEN_SUPPORT = "mvn.support";

  /**
   ** the key for the property to locate the directory where the Oracle
   ** Identity Consulting Services Framework is located.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** preferences, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String FOUNDATION    = "ocs.home";

  /**
   ** the key for the property to locate the directory where the Oracle
   ** Identity and Access development is or will be take place.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** preferences, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String WORKSPACE     = "wks.home";

  /**
   ** the key for the property to specify the release used for development.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** preferences, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String RELEASE       = "release";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeaturePreference</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new FeaturePreference()" and enforces use of the public factory
   ** methods of the implementing subclass.
   **
   ** @param  storage            the {@link PropertyStorage} this adapters
   **                            {@link HashStructure} will be obtained from.
   ** @param  key                the key the {@link HashStructure} bound at
   **                            {@link PropertyStorage}.
   */
  protected FeaturePreference(final PropertyStorage storage, final String key) {
    // ensure inheritance
    this(findOrCreate(storage, key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeaturePreference</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new FeaturePreference()" and enforces use of the public factory of the
   ** implementing subclasses.
   **
   ** @param  hash               the {@link HashStructure} this adapter wrappes.
   */
  protected FeaturePreference(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature
  /**
   ** Returns the preferences of an extension plugin feature as a
   ** {@link HashStructure}.
   **
   ** @param  feature            the key for the desired {@link HashStructure}.
   **
   ** @return                    the preferences of an extension plugin feature
   **                            as a {@link HashStructure}.
   */
  public final HashStructure feature(final String feature) {
    return this._hash.containsKey(feature) ? this._hash.getHashStructure(feature) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Called to inject the argument for instance attribute
   ** <code>release</code>.
   **
   ** @param  release            the release used for development.
   */
  public final void release(final String release) {
    this._hash.putString(RELEASE, release);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Returns the instance attribute <code>release</code>.
   **
   ** @return                    the release used for development.
   */
  public abstract String release();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   foundation
  /**
   ** Called to inject the argument for instance attribute
   ** <code>ocs.home</code>.
   **
   ** @param  foundation         the {@link URL} of the directory where the
   **                            binaries of a specific product foundation
   **                            framework are located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final void foundation(final URL foundation) {
    this._hash.putURL(FOUNDATION, foundation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   foundation
  /**
   ** Returns the instance attribute <code>ocs.home</code>.
   **
   ** @return                    the {@link URL} of the directory where the
   **                            binaries of a specific product foundation
   **                            framework are located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final URL foundation() {
    // The second parameter to getURL() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return this.url(FOUNDATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspace
  /**
   ** Called to inject the argument for instance attribute
   ** <code>wks.home</code>.
   **
   ** @param  workspace          the {@link URL} of the workspace directory
   **                            where the Oracle Identity and Access Management
   **                            development is or will be take place.
   */
  public final void workspace(final String workspace) {
    this._hash.putString(WORKSPACE, workspace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspace
  /**
   ** Returns the instance attribute <code>wks.home</code>.
   **
   ** @return                    the {@link URL} of the workspace directory
   **                            where the Oracle Identity and Access Management
   **                            development is or will be take place.
   */
  public abstract String workspace();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenSupport
  /**
   ** Called to inject the argument for instance attribute
   ** <code>mvn.support</code>.
   **
   ** @param  state              <code>true</code> if an extension plugin
   **                            feature supports the Maven Project Object
   **                            Model (POM); otherwise <code>false</code>.
   */
  public final void mavenSupport(final boolean state) {
    this._hash.putBoolean(MAVEN_SUPPORT, state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maven
  /**
   ** Returns <code>true</code> if the extension plugin feature supports the
   ** Maven Project Object Model (POM).
   **
   ** @return                    <code>true</code> if an extension plugin
   **                            feature supports the Maven Project Object
   **                            Model (POM); otherwise <code>false</code>.
   */
  public final boolean mavenSupport() {
    // the second parameter to bool() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return bool(MAVEN_SUPPORT, false);
  }
}
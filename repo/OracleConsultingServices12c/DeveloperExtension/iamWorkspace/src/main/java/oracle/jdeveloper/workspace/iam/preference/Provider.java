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

    File        :   Provider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Provider.


    Revisions          Date        Editor      Comment
    -------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13   2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74   2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94   2021-03-09  DSteding    Support for Maven Project Object
                                                Model
    12.2.1.3.42.60.101  2022-06-11  DSteding    Support for Foundation Services
                                                Library Generation
*/

package oracle.jdeveloper.workspace.iam.preference;

import java.net.URL;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;
import oracle.javatools.data.HashStructureAdapter;

import oracle.jdeveloper.workspace.iam.Library;
import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.workspace.iam.model.DataStructureAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~~
/**
 ** This is a Providers object, used as the data model for Identity and Access
 ** Management Providers.
 ** <p>
 ** It consists of two properties, OCS Foundation Framework Home and Headstart
 ** Foundation Framework Home, which you can obtain from the Providers object
 ** using the {@link #foundation()} or {@link #headstart()} methods.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.101
 ** @since   11.1.1.3.37.56.13
 */
public final class Provider extends DataStructureAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the key for the property to locate the directory where the Oracle
   ** Consulting Foundation Framework is located.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** Providers, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String HEADSTART = "hst.home";

  /**
   ** the key for the property to locate the directory where the Oracle
   ** Consulting Foundation Service is located.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** Providers, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String SERVICES  = "ocs.home";

  /**
   ** the key for the property to locate the directory where the Oracle
   ** Platform Service Framework is located.
   ** <p>
   ** This name is used directly in the persistence file that stores
   ** Providers, so you either need to keep it the same between releases, or
   ** deal with migration from previous names in this adapter class.
   */
  public static final String PLATFORM  = "ops.home";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Provider()" and enforces use of the public factory method
   ** below.
   */
  private Provider(final HashStructure hash) {
    // ensure inheritance
    super(hash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspace
  /**
   ** Called to inject the argument for instance attribute
   ** <code>workspace</code>.
   **
   ** @param  workspace          the {@link URL} of the workspace directory
   **                            where the Oracle Identity and Access Management
   **                            development is or will be take place.
   */
  public void workspace(final URL workspace) {
    this._hash.putURL(FeaturePreference.WORKSPACE, workspace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspace
  /**
   ** Returns the instance attribute <code>workspace</code>.
   **
   ** @return                    the {@link URL} of the workspace directory
   **                            where the Oracle Identity and Access Management
   **                            development is or will be take place.
   */
  public final URL workspace() {
    // The second parameter to url() is a default value. Defaults are stored in
    // the persistent storage file using a "placeholder".
    return this.url(FeaturePreference.WORKSPACE, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   foundation
  /**
   ** Called to inject the argument for instance attribute
   ** <code>foundation</code>.
   **
   ** @param  foundation         the {@link URL} of the directory where the
   **                            binaries of a specific product foundation
   **                            framework are located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public void foundation(final URL foundation) {
    this._hash.putURL(FeaturePreference.FOUNDATION, foundation);
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
  public URL foundation() {
    // The second parameter to getURL() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return this.url(FeaturePreference.FOUNDATION, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   headstart
  /**
   ** Called to inject the argument for instance attribute
   ** <code>headstart</code>.
   **
   ** @param  headstart          the {@link URL} of the directory where the
   **                            Oracle Consulting Foundation Framework is
   **                            located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final void headstart(final URL headstart) {
    this._hash.putURL(HEADSTART, headstart);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   headstart
  /**
   ** Returns the instance attribute <code>headstart</code>.
   **
   ** @return                    the {@link URL} of the directory where the
   **                            Oracle Consulting Foundation Framework is
   **                            located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final URL headstart() {
    return this._hash.getURL(HEADSTART, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   services
  /**
   ** Called to inject the argument for instance attribute
   ** <code>service</code>.
   **
   ** @param  services           the {@link URL} of the directory where the
   **                            Oracle Consulting Platform Service is
   **                            located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final void service(final URL services) {
    this._hash.putURL(SERVICES, services);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   services
  /**
   ** Returns the instance attribute <code>services</code>.
   **
   ** @return                    the {@link URL} of the directory where the
   **                            Oracle Consulting Platform Services is
   **                            located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final URL services() {
    return this._hash.getURL(SERVICES, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Called to inject the argument for instance attribute
   ** <code>headstart</code>.
   **
   ** @param  platform           the {@link URL} of the directory where the
   **                            Oracle Platform Service Framework is located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final void platform(final URL platform) {
    this._hash.putURL(PLATFORM, platform);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform
  /**
   ** Returns the instance attribute <code>platform</code>.
   **
   ** @return                    the {@link URL} of the directory where the
   **                            Oracle Platform Service Framework is located.
   **                            <p>
   **                            This directory is part of the checkout process
   **                            from SCM. There is no plan to provided the
   **                            binaries from a different place.
   */
  public final URL platform() {
    return this._hash.getURL(PLATFORM, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Called to inject the argument for instance attribute
   ** <code>release</code>.
   **
   ** @param  release            the release of Oracle Identity and Access
   **                            Management used for development.
   */
  public final void release(final String release) {
    this._hash.putString(FeaturePreference.RELEASE, release);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Returns the instance attribute <code>release</code>.
   **
   ** @return                    the release of Oracle Platform Service used
   **                            for development.
   */
  public final String release() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FeaturePreference.RELEASE, Library.DEFAULT_RELEASE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenSupport
  /**
   ** Called to inject the argument for instance attribute
   ** <code>mvn.support</code>.
   **
   ** @param  state              <code>true</code> if an extension plugin
   **                            feature supports the Maven build features.
   */
  public final void mavenSupport(final boolean state) {
    this._hash.putBoolean(FeaturePreference.MAVEN_SUPPORT, state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maven
  /**
   ** Returns <code>true</code> if an extension plugin feature supports the
   ** Maven Project Object Model.
   **
   ** @return                    <code>true</code> if an extension plugin
   **                            feature supports the Maven build features.
   */
  public final boolean mavenSupport() {
    // the second parameter to bool() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return bool(FeaturePreference.MAVEN_SUPPORT, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link HashStructureAdapter}.
   ** <p>
   ** This method tries to find an existing instance of a
   ** {@link HashStructureAdapter} using {@link Manifest#KEY}.
   ** <p>
   ** If none can be found, this method will create a new HashStructure, first
   ** attempting to wire that into the specified name in the
   ** {@link PropertyStorage} or, failing that, leave the new HashStructure
   ** disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   ** Factory method takes a PropertyStorage (instead of HashStructure
   ** directly). This decouples the origin of the HashStructure and allows the
   ** future possibility of resolving Providers through multiple layers of
   ** HashStructure.
   ** <p>
   ** Classes/methods that currently implement/return PropertyStorage:
   ** <ul>
   **   <li>oracle.ide.config.Providers
   **   <li>oracle.ide.model.Project
   **   <li>oracle.ide.model.Workspace
   **   <li>oracle.ide.panels.TraversableContext.getPropertyStorage()
   ** </ul>
   **
   ** @param  storage            the {@link PropertyStorage} providing acces to
   **                            the prefeneces.
   **
   ** @return                    the <code>Provider</code> ready for use either
   **                            fill up with the defaults or the configured
   **                            values.
   */
  public static Provider instance(final PropertyStorage storage) {
    return new Provider(findOrCreate(storage, Manifest.KEY));
  }
}
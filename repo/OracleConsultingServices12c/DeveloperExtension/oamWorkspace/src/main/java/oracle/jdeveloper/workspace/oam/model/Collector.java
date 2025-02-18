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

    File        :   Collector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Collector.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.model;

import oracle.ide.panels.TraversableContext;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.oam.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class Collector
// ~~~~~ ~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating an Oracle Access
 ** Manager Credential Collector Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Collector extends BuildArtifact {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE                = "oam-collector-ant";
  private static final String TEMPLATE            = Manifest.BUILDFILE_PACKAGE + "." + FILE;
  private static final String DEFAULT_FILE        = FILE + "." + Manifest.CONFIG_FILE_TYPE;
  private static final String DEFAULT_APPLICATION = "ocs-collector";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Collector</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Pages()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private Collector(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template (BuildPropertyAdapter)
  /**
   ** Returns the template of this descriptor the generation is based on.
   **
   ** @return                    the template of this descriptor the generation
   **                            is based on.
   */
  @Override
  public String template() {
    return TEMPLATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application (BuildArtifact)
  /**
   ** Returns the application tag for the library.
   **
   ** @return                    the application tag for the library.
   */
  @Override
  public String application() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(APPLICATION, DEFAULT_APPLICATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file (overridden)
  /**
   ** Returns the file name tag used by the component configuration to store the
   ** bulld file in the local filesystem.
   **
   ** @return                    the file name tag used by the component
   **                            configuration to store the bulld file in the
   **                            local filesystem.
   */
  @Override
  public String file() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FILE, DEFAULT_FILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   project (overridden)
  /**
   ** Returns the project name tag used by the component configuration.
   **
   ** @return                    the project name tag used by the component
   **                            configuration.
   */
  @Override
  public String project() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROJECT, Manifest.string(Manifest.COLLECTOR_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link BuildArtifact}.
   ** <p>
   ** This method tries to find an existing instance of a {@link BuildArtifact}
   ** using {@link Manifest#COLLECTOR} in the design time objects of the
   ** specified {@link TraversableContext}.
   ** <p>
   ** If none can be found, this method will create a new {@link HashStructure},
   ** first attempting to wire that into the specified name in the
   ** {@link TraversableContext} or, failing that, leave the new
   ** {@link HashStructure} disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   ** Factory method takes a <code>PropertyStorage</code> (instead of
   ** {@link HashStructure} directly). This decouples the origin of the
   ** {@link HashStructure} and allows the future possibility of resolving
   ** preferences through multiple layers of {@link HashStructure}.
   ** <p>
   ** Classes/methods that currently implement/return PropertyStorage:
   ** <ul>
   **   <li>oracle.ide.config.Preferences
   **   <li>oracle.ide.model.Project
   **   <li>oracle.ide.model.Workspace
   **   <li>oracle.ide.panels.TraversableContext.getPropertyStorage()
   ** </ul>
   **
   ** @param  context            the data provider to initialize the instance.
   **
   ** @return                    the <code>Collector</code> instance obtained
   **                            from the specifed {@link TraversableContext}.
   */
  public static Collector instance(final TraversableContext context) {
    Collector instance = (Collector)context.getDesignTimeObject(Manifest.COLLECTOR);
    if (instance == null) {
      instance = new Collector(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.COLLECTOR, instance);
    }
    return instance;
  }
}
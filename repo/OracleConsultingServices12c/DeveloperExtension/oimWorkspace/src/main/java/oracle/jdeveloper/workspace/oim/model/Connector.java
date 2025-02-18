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

    File        :   Connector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Connector.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    Change setter/getter from name
                                               to file.
                                               --
                                               Properties to maintain the
                                               deployment destinations added
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import java.net.URL;

import oracle.ide.panels.TraversableContext;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class Connector
// ~~~~~ ~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating an Oracle Identity
 ** Manager Connector Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Connector extends BuildArtifact {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE                  = "oim-connector-ant";
  private static final String TEMPLATE              = Manifest.BUILDFILE_PACKAGE + "." + FILE;
  private static final String DEFAULT_FILE          = FILE + "." + Manifest.CONFIG_FILE_TYPE;
  private static final String DEFAULT_APPLICATION   = "ocs-connector";

  private static final String DESTINATION_TARGET    = "destination.target";
  private static final String DESTINATION_TRUSTED   = "destination.trusted";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Connector</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Connector()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private Connector(final HashStructure structure) {
    // ensure inheritance
    super(structure);
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
    // The second parameter to getString() is a default value. Defaults are
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
    return string(PROJECT, Manifest.string(Manifest.CONNECTOR_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destinationTarget
  /**
   ** Sets the destination of the target systen deployment configuration used by
   ** the component configuration.
   **
   ** @param  destination        the destination of the target systen deployment
   **                            configuration used by the component
   **                            configuration.
   */
  public void destinationTarget(final URL destination) {
    this._hash.putURL(DESTINATION_TARGET, destination);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destinationTarget
  /**
   ** Returns the destination of the target systen deployment configuration used
   ** by the component configuration.
   **
   ** @return                    the destination of the target systen deployment
   **                            configuration used by the component
   **                            configuration.
   */
  public URL destinationTarget() {
    return this.url(DESTINATION_TARGET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destinationTrusted
  /**
   ** Sets the destination of the trusted systen deployment configuration used by
   ** the component configuration.
   **
   ** @param  destination        the destination of the trusted systen deployment
   **                            configuration used by the component
   **                            configuration.
   */
  public void destinationTrusted(final URL destination) {
    this._hash.putURL(DESTINATION_TRUSTED, destination);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destinationTrusted
  /**
   ** Returns the destination of the trusted systen deployment configuration used
   ** by the component configuration.
   **
   ** @return                    the destination of the trusted systen deployment
   **                            configuration used by the component
   **                            configuration.
   */
  public URL destinationTrusted() {
    return this.url(DESTINATION_TRUSTED);
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
  public final String template() {
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
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(APPLICATION, DEFAULT_APPLICATION);
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
   ** using {@link Manifest#CONNECTOR} in the design time objects of the
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
   ** @return                    the <code>Connector</code> instance obtained
   **                            from the specifed {@link TraversableContext}.
   */
  public static Connector instance(final TraversableContext context) {
    Connector instance = (Connector)context.getDesignTimeObject(Manifest.CONNECTOR);
    if (instance == null) {
      instance = new Connector(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.CONNECTOR, instance);
    }
    return instance;
  }
}
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

    File        :   OAMServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OAMServer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    The name of the managed server is
                                               included now in the properties
    11.1.1.3.37.60.32  2012-10-20  DSteding    Cloning of an existing file
                                               implemented.
    11.1.1.3.37.60.61  2014-10-25  DSteding    Fix for Defect DE-000142
                                               Port is not set correctly if
                                               properties created by cloning.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.model;

import java.net.URL;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.javatools.data.HashStructure;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.model.FMWServer;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.oam.Manifest;

import oracle.jdeveloper.workspace.oam.parser.OAMServerHandler;

////////////////////////////////////////////////////////////////////////////////
// class OAMServer
// ~~~~~ ~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating an Oracle Access
 ** Manager application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OAMServer extends FMWServer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE              = "oam-server";
  private static final String TEMPLATE          = Manifest.BUILDFILE_PACKAGE + "." + FILE;
  private static final String DEFAULT_FILE      = FILE + "." + Manifest.CONFIG_FILE_TYPE;
  private static final String DEFAULT_PORT      = "8010";
  private static final String DEFAULT_USERNAME  = "weblogic";
  private static final String DEFAULT_SERVER    = "oam";
  private static final String DEFAULT_PARTITION = "oam";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OAMServer</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new OAMServer()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private OAMServer(final HashStructure structure) {
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
    return string(PROJECT, Manifest.string(Manifest.OAM_SERVER_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port (overridden)
  /**
   ** Returns the port the Application Server is listening on.
   **
   ** @return                    the port the Application Server is listening
   **                            on.
   */
  @Override
  public String port() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PORT, DEFAULT_PORT);
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
  // Method:   defaultServer (FMWServer)
  /**
   ** Returns the default name of the managed server in a Oracle WebLogic
   ** environment.
   **
   ** @return                    the default name of the managed server in a
   **                            Oracle WebLogic environment.
   */
  @Override
  public final String defaultServer() {
    return DEFAULT_SERVER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform (PlatformConfig)
  /**
   ** Called to inject the argument for parameter <code>platform</code>.
   **
   ** @param  platform           the value for the attribute
   **                            <code>platform</code> used as the RMI/JNDI
   **                            provider.
   */
  @Override
  public final void platform(final String platform) {
    this._hash.putString(OAMServerHandler.PLATFORM, platform);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   platform (PlatformConfig)
  /**
   ** Returns the <code>platform</code> attribute for the RMI/JNDI provider.
   **
   ** @return                    the <code>platform</code> attribute for the
   **                            RMI/JNDI provider.
   */
  @Override
  public final String platform() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(OAMServerHandler.PLATFORM, StringUtility.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host (PlatformConfig)
  /**
   ** Sets the host name where the Application Server is deployed
   **
   ** @param  host               the host name where the Application Server is
   **                            deployed.
   */
  @Override
  public void host(final String host) {
    this._hash.putString(OAMServerHandler.HOST, host);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   host (PlatformConfig)
  /**
   ** Returns the host name where the Application Server is deployed
   **
   ** @return                    the host name where the Application Server is
   **                            deployed.
   */
  @Override
  public String host() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(OAMServerHandler.HOST, StringUtility.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username (PlatformConfig)
  /**
   ** Called to inject the argument for parameter <code>username</code>.
   **
   ** @param  username           the name of the security principal server used
   **                            to connect to.
   */
  @Override
  public final void username(final String username) {
    this._hash.putString(OAMServerHandler.USERNAME, username);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   username (PlatformConfig)
  /**
   ** Returns the name of the security principal of the server used to connect
   ** to.
   **
   ** @return                    the name of the security principal server used
   **                            to connect to.
   */
  @Override
  public final String username() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(OAMServerHandler.USERNAME, DEFAULT_USERNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (PlatformConfig)
  /**
   ** Called to inject the argument for parameter <code>password</code>.
   **
   ** @param  password           the password of the administrative user.
   */
  @Override
  public final void password(final String password) {
    this._hash.putString(OAMServerHandler.PASSWORD, password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (PlatformConfig)
  /**
   ** Returns the password of the security principal of the server used to
   ** connect to.
   **
   ** @return                    the password of the security principal server
   **                            used to connect to.
   */
  @Override
  public final String password() {
    return string(OAMServerHandler.PASSWORD, StringUtility.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultProtocol (FMWServer)
  /**
   ** Returns the default protocol to communicate with the server component.
   **
   ** @return                    the default protocol to communicate with the
   **                            server component.
   */
  @Override
  public final String defaultProtocol(){
    return DEFAULT_PROTOCOL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultPartition (FMWServer)
  /**
   ** Returns the default metadata partition used by the component configuration.
   **
   ** @return                    the default metadata partition used by the
   **                            component configuration.
   */
  @Override
  public final String defaultPartition(){
    return DEFAULT_PARTITION;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link FMWServer}.
   ** <p>
   ** This method tries to find an existing instance of a {@link FMWServer}
   ** using {@link Manifest#OAM_SERVER} in the design time objects of the
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
   ** @return                    the <code>OAMServer</code> instance obtained
   **                            from the specifed {@link TraversableContext}.
   */
  public static OAMServer instance(final TraversableContext context) {
    OAMServer instance = (OAMServer)context.getDesignTimeObject(Manifest.OAM_SERVER);
    if (instance == null) {
      instance = new OAMServer(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.OAM_SERVER, instance);
      // check if a file is aleady existing that might be copied from another
      // workspace or manually created before this wizard runs to pick-up the
      // configuration from there instead to provided the defaults only
      // this operation assumes that the workspace directory where the
      // application will be created with in is already set in the traversable
      // context but needs not to be exist in the file system
      final URL source = URLFactory.newURL(URLFileSystem.getParent(TemplateWizardUtil.getApplicationURL(context)), DEFAULT_FILE);
      if (URLFileSystem.exists(source))
        instance.clone(source);
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone (overridden)
  /**
   ** Clones this configuration from an existing file.
   **
   ** @param  source             the {@link URL} providing the values for the
   **                            cloning process.
   */
  @Override
  public void clone(final URL source) {
    OAMServerHandler handler = new OAMServerHandler(source);
    try {
      handler.load();
      platform(handler.propertyValue(OAMServerHandler.PLATFORM));
      protocol(handler.propertyValue(OAMServerHandler.PROTOCOL));
      host(handler.propertyValue(OAMServerHandler.HOST));
      // Defect DE-000142
      // Port is not set correctly if properties created by cloning.
      port(handler.propertyValue(OAMServerHandler.PORT));
      server(handler.propertyValue(OAMServerHandler.NAME));
      home(handler.propertyValue(OAMServerHandler.HOME));
      username(handler.propertyValue(OAMServerHandler.USERNAME));
      password(handler.propertyValue(OAMServerHandler.PASSWORD));
    }
    catch (XMLFileHandlerException e) {
      e.printStackTrace();
    }
  }
}
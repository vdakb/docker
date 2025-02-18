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
    Subsystem   :   Identity and Access Management Facilities

    File        :   JEEServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    JEEServer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    Change setter/getter from name
                                               to file.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Cloning of an existing file
                                               implemented.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import java.net.URL;

import oracle.javatools.data.HashStructure;
import oracle.javatools.data.PropertyStorage;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.parser.JEEServerHandler;
import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

////////////////////////////////////////////////////////////////////////////////
// class JEEServer
// ~~~~~ ~~~~~~~~~
/**
 ** The model to support the Preference dialog for creating the preferences
 ** stored in the {@link JEEServer} model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class JEEServer extends ServerConfig {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String DEFAULT_PROTOCOL = "t3";
  private static final String DEFAULT_PORT     = "7001";
  private static final String DEFAULT_SERVER   = "adm";
  private static final String DEFAULT_USERNAME = "weblogic";

  private static final String FILE             = "jee-server";
  private static final String TEMPLATE         = Manifest.TEMPLATE_PACKAGE + "." + FILE;

  private static final String DEFAULT_FILE     = FILE + "." + Manifest.CONFIG_FILE_TYPE;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JEEServer</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new JEEServer()" and enforces use of the public factory method
   ** below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private JEEServer(final HashStructure structure) {
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
    return string(PROJECT, Manifest.string(Manifest.JEE_SERVER_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Call to inject the argument for parameter <code>protocol</code>.
   **
   ** @param  protocol           the protocol to communicate with the JEE Server.
   */
  public void protocol(final String protocol) {
    this._hash.putString(JEEServerHandler.PROTOCOL, protocol);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Returns the class name of the initial context factory.
   **
   ** @return                    the protocol to communicate with the JEE Server.
   */
  public final String protocol() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(JEEServerHandler.PROTOCOL, DEFAULT_PROTOCOL);
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
    this._hash.putString(JEEServerHandler.PLATFORM, platform);
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
    return string(JEEServerHandler.PLATFORM, StringUtility.EMPTY);
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
    this._hash.putString(JEEServerHandler.HOST, host);
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
    return string(JEEServerHandler.HOST, StringUtility.EMPTY);
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
    this._hash.putString(JEEServerHandler.USERNAME, username);
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
    return string(JEEServerHandler.USERNAME, DEFAULT_USERNAME);
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
    this._hash.putString(JEEServerHandler.PASSWORD, password);
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
    return string(JEEServerHandler.PASSWORD, StringUtility.EMPTY);
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link ServerConfig}.
   ** <p>
   ** This method tries to find an existing instance of a {@link ServerConfig}
   ** using {@link Manifest#JEE_SERVER} in the design time objects of the
   ** specified {@link TraversableContext}.
   ** <p>
   ** If none can be found, this method will create a new {@link HashStructure},
   ** first attempting to wire that into the specified name in the
   ** {@link TraversableContext} or, failing that, leave the new
   ** {@link HashStructure} disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   ** Factory method takes a {@link PropertyStorage} (instead of
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
   ** @return                    the <code>JEEServer</code> instance obtained
   **                            from the specifed {@link TraversableContext}.
   */
  public static JEEServer instance(final TraversableContext context) {
    JEEServer instance = (JEEServer)context.getDesignTimeObject(Manifest.JEE_SERVER);
    if (instance == null) {
      instance = new JEEServer(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.JEE_SERVER, instance);
      // check if a file is aleady existing that might be copied from another
      // workspace or manually created before this wizard runs to pick-up the
      // configuration from there instead to provided the defaults only
      // this operation assumes that the workspace directory where the
      // application will be created with in is already set in the traversable
      // context but needs not to be exist in the file system
      final URL workspace = URLFileSystem.getParent(TemplateWizardUtil.getApplicationURL(context));
      final URL source    = URLFactory.newURL(workspace, DEFAULT_FILE);
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
    JEEServerHandler handler = new JEEServerHandler(source);
    try {
      handler.load();
      platform(handler.propertyValue(JEEServerHandler.PLATFORM));
      protocol(handler.propertyValue(JEEServerHandler.PROTOCOL));
      host(handler.propertyValue(JEEServerHandler.HOST));
      port(handler.propertyValue(JEEServerHandler.PORT));
      home(handler.propertyValue(JEEServerHandler.HOME));
      server(handler.propertyValue(JEEServerHandler.NAME));
      username(handler.propertyValue(JEEServerHandler.USERNAME));
      password(handler.propertyValue(JEEServerHandler.PASSWORD));
    }
    catch (XMLFileHandlerException e) {
      e.printStackTrace();
    }
  }
}
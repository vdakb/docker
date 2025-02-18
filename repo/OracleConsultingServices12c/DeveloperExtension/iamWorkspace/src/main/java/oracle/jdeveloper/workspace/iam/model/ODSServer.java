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

    File        :   ODSServer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ODSServer.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    The name of the managed server is
                                               included now in the properties
    11.1.1.3.37.60.32  2012-10-20  DSteding    Cloning of an existing file
                                               implemented.
    11.1.1.3.37.60.61  2014-10-25  DSteding    Fix for Defect DE-000145
                                               Port is not set correctly if
                                               properties created by cloning.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.model;

import java.net.URL;

import oracle.javatools.data.HashStructure;

import oracle.ide.net.URLFactory;
import oracle.ide.net.URLFileSystem;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.parser.ODSServerHandler;

////////////////////////////////////////////////////////////////////////////////
// class ODSServer
// ~~~~~ ~~~~~~~~~
/**
 ** The model to support the Preference dialog for creating the preferences
 ** stored in the <code>ODSServer</code> model.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ODSServer extends PlatformConfig {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  PROTOCOL          = "protocol";
  public static final String  HOME              = "home";
  public static final String  INSTANCE          = "instance";
  public static final String  CONTEXT           = "context";

  private static final String FILE              = "ods-server";
  private static final String TEMPLATE          = Manifest.TEMPLATE_PACKAGE + "." + FILE;
  private static final String DEFAULT_FILE      = FILE + "." + Manifest.CONFIG_FILE_TYPE;
  private static final String DEFAULT_PROTOCOL  = "ldap";
  private static final String DEFAULT_PORT      = "1389";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ODSServer</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ODSServer()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private ODSServer(final HashStructure structure) {
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
    return string(PROJECT, Manifest.string(Manifest.ODS_SERVER_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   port (overridden)
  /**
   ** Returns the port the Application Server is listening on.
   **
   ** @return                    the port the Directory Server is listening
   **                            on.
   */
  @Override
  public String port() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PORT, DEFAULT_PORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Call to inject the argument for parameter <code>protocol</code>.
   **
   ** @param  protocol           the protocol to communicate with the Directory
   **                            Server.
   */
  public final void protocol(final String protocol) {
    this._hash.putString(PROTOCOL, protocol);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   protocol
  /**
   ** Returns the default protocol to communicate with the server component.
   **
   ** @return                    the protocol to communicate with the Directory
   **                            Server.
   */
  public final String protocol() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROTOCOL, DEFAULT_PROTOCOL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   home
  /**
   ** Called to inject the argument for instance attribute <code>home</code>.
   **
   ** @param  home               the home folder the Directory Server is
   **                            installed.
   */
  public void home(final String home) {
    this._hash.putString(HOME, home);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   home
  /**
   ** Returns the home the Application Server is listening on.
   **
   ** @return                    the home folder the Directory Server is
   **                            installed.
   */
  public String home() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(HOME, StringUtility.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Call to inject the argument for parameter <code>instance</code>.
   **
   ** @param  instance           the instance to communicate with the Directory
   **                            Server.
   */
  public final void instance(final String instance) {
    this._hash.putString(INSTANCE, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the instance to communicate with the server component.
   **
   ** @return                    the instance to communicate with the Directory
   **                            Server.
   */
  public final String instance() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(INSTANCE, StringUtility.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Call to inject the argument for parameter <code>context</code>.
   **
   ** @param  context            the context to communicate with the Directory
   **                            Server.
   */
  public final void context(final String context) {
    this._hash.putString(CONTEXT, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the context to communicate with the server component.
   **
   ** @return                    the context to communicate with the Directory
   **                            Server.
   */
  public final String context() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(CONTEXT, StringUtility.EMPTY);
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
    this._hash.putString(ODSServerHandler.PLATFORM, platform);
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
    return string(ODSServerHandler.PLATFORM, StringUtility.EMPTY);
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
    this._hash.putString(ODSServerHandler.HOST, host);
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
    return string(ODSServerHandler.HOST, StringUtility.EMPTY);
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
    this._hash.putString(ODSServerHandler.USERNAME, username);
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
    return string(ODSServerHandler.USERNAME, StringUtility.EMPTY);
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
    this._hash.putString(ODSServerHandler.PASSWORD, password);
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
    return string(ODSServerHandler.PASSWORD, StringUtility.EMPTY);
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
   ** using {@link Manifest#ODS_SERVER} in the design time objects of the
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
   ** @return                    the <code>ODSServer</code> instance obtained
   **                            from the specifed {@link TraversableContext}.
   */
  public static ODSServer instance(final TraversableContext context) {
    ODSServer instance = (ODSServer)context.getDesignTimeObject(Manifest.ODS_SERVER);
    if (instance == null) {
      instance = new ODSServer(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.ODS_SERVER, instance);
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
    ODSServerHandler handler = new ODSServerHandler(source);
    try {
      handler.load();
      platform(handler.propertyValue(ODSServerHandler.PLATFORM));
      protocol(handler.propertyValue(ODSServerHandler.PROTOCOL));
      host(handler.propertyValue(ODSServerHandler.HOST));
      // Defect DE-000145
      // Port is not set correctly if properties created by cloning.
      port(handler.propertyValue(ODSServerHandler.PORT));
      home(handler.propertyValue(ODSServerHandler.HOME));
      instance(handler.propertyValue(ODSServerHandler.INSTANCE));
      username(handler.propertyValue(ODSServerHandler.USERNAME));
      password(handler.propertyValue(ODSServerHandler.PASSWORD));
    }
    catch (XMLFileHandlerException e) {
      e.printStackTrace();
    }
  }
}
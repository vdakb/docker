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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   ConfigHandler.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConfigHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.handler;

import java.util.Map;
import java.util.List;
import java.util.EnumMap;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;

import org.opends.server.tools.dsconfig.DSConfig;

import org.opends.server.tools.managesuffix.ManageSuffix;

import oracle.hst.deployment.ServiceFrontend;

import oracle.hst.deployment.task.ServiceProvider;

import oracle.hst.deployment.type.ODSServerContext;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;
import oracle.iam.directory.common.FeatureResourceBundle;

import oracle.iam.directory.common.task.FeaturePlatformTask;

import oracle.iam.directory.common.spi.instance.CommandInstance;

////////////////////////////////////////////////////////////////////////////////
// abstract class ConfigHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Invokes an operation on Directory Server to managed configuration.
 ** <p>
 ** This class provides a command-line tool which enables administrators to
 ** configure the Directory Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ConfigHandler extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** keeps track of subsequently called invocations to avoid the initialization
   ** of DSConfig more than once
   ** <br>
   ** As long as this marker is true DSConfig will be called with the option to
   ** initialize the stack
   */
  static boolean initialize = true;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the connection Bind options
  final Map<Bind, String>     bind     = new EnumMap<Bind, String>(Bind.class);

  // the commands to Excecute
  final List<CommandInstance> workload = new ArrayList<CommandInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Bind
  // ~~~~ ~~~~
  /**
   ** The <code>ConfigurationHandler</code> contacts the directory server over
   ** SSL through the administration connector.
   ** These connection options are used to contact the directory server.
   */
  public enum Bind {
    /**
     ** Contact the server on the specified hostname or IP address.
     ** <p>
     ** <b>Note</b>: If this option is not provided, a default of localhost is
     ** used.
     */
    hostname("hostname"),

    /**
     ** Contact the server at the specified administration port.
     ** <p>
     ** <b>Note</b>: If this option is not provided, the administration port
     ** of the local configuration is used.
     */
    hostport("port"),

    /**
     ** Use the bind DN to authenticate to the server.
     ** <br>
     ** This option is used when performing simple authentication and is not
     ** required if SASL authentication is to be used.
     ** <p>
     ** The default value for this option is
     ** <code>cn=Directory Manager</code>.
     */
    username("bindDN"),

    /**
     ** Trust all server SSL certificates that the server presents.
     ** <br>
     ** This option can be used for convenience and testing purposes, but for
     ** security reasons a trust store should be used to determine whether the
     ** client should accept the server certificate. If the client and the
     ** server are running in the same instance, there is no certificate
     ** interaction.
     */
    trustAll("trustAll"),

   /**
     ** Use the bind password when authenticating to the server.
     ** <br>
     ** This option can be used for simple authentication as well as
     ** password-based SASL mechanisms.
     ** <p>
     ** <b>Note</b>: This option must not be used in conjunction with
     ** {@link #passwordFile}.
     */
    password("bindPassword"),

    /**
     ** Use the bind password in the specified file when authenticating to the
     ** server.
     ** <p>
     ** <b>Note</b>: This option must not be used in conjunction with
     ** {@link #password}.
     */
    passwordFile("bindPasswordFile"),

    /**
     ** Use the client keystore certificate in the specified path.
     */
    keyStorePath("keyStorePath"),

    /**
     ** Use the password needed to access the certificates in the client
     ** keystore.
     ** <br>
     ** This option is only required if {@link #keyStorePath} is used.
     ** <p>
     ** <b>Note</b>: This option must not be used in conjunction with
     ** {@link #keyStorePasswordFile}.
     */
    keyStorePassword("keyStorePassword"),

    /**
     ** Use the password in the specified file to access the certificates in
     ** the client keystore.
     ** <br>
     ** This option is only required if {@link #keyStorePath} is used.
     ** <p>
     ** <b>Note</b>: This option must not be used in conjunction with
     ** {@link #keyStorePassword}.
     */
    keyStorePasswordFile("keyStorePasswordFile"),

    /**
     ** Use the specified options for SASL authentication.
     ** <p>
     ** <b>Note</b>: SASL is not supported for Oracle Unified Directory proxy.
     */
    sasl("saslOption"),

    /**
     ** Use the client trust store certificate in the specified path.
     ** <br>
     ** This option is not needed if {@link #trustAll} is used, although a
     ** trust store should be used when working in a production environment.
     */
    trustStorePath("trustStorePath"),

    /**
     ** Use the password needed to access the certificates in the client trust
     ** store.
     ** <br>
     ** This option is only required if {@link #trustStorePath} is used and
     ** the specified trust store requires a password in order to access its
     ** contents (which most trust stores do not require).
     ** <p>
     ** <b>Note</b>: This option must not be used in conjunction with
     ** {@link #trustStorePasswordFile}.
     */
    trustStorePassword("trustStorePassword"),

    /**
     ** Use the password in the specified file to access the certificates in
     ** the client trust store.
     ** <br>
     ** This option is only required if {@link #trustStorePath} is used and
     ** the specified trust store requires a password in order to access its
     ** contents (most trust stores do not require this).
     ** <p>
     ** <b>Note</b>: This option must not be used in conjunction with
     ** {@link #trustStorePassword}.
     */
    trustStorePasswordFile("trustStorePasswordFile")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Bind</code>
     **
     ** @param  id               the <code>ConfigurationHandler</code> accepts
     **                          an option in either its short form (for
     **                          example, <code>-h hostname</code>) or its long
     **                          form equivalent (for example,
     **                          <code>--hostname hostname</code>).
     **                          <br>
     **                          Per design only the long form equivalent will
     **                          be accepted.
     **                          Allowed object is {@link String}.
     */
    Bind(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value of the id property.
     **
     ** @return                  the value of the id property.
     **                          Possible object is {@link String}.
     */
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper bind option from the given string
     ** value.
     **
     ** @param  id                 the string value the bind option should be
     **                            returned for.
     **
     ** @return                    bind option.
     **                            Possible object is <code>Bind</code>.
     */
    public static Bind from(final String id) {
      for (Bind cursor : Bind.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: argument
    /**
     ** Returns the string representation of bind option.
     **
     ** @return                  the string representation of bind option.
     **                          Nothing else the id prepended with two dashes.
     */
    public String argument() {
      return String.format("--%s", this.id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Invokes an operation on Directory Server to managed configuration.
   ** <p>
   ** This class provides a command-line tool which enables
   ** administrators to configure the Directory Server.
   */
  public static final class Server extends ConfigHandler {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new <code>Server</code> handler to initialize the instance.
     **
     ** @param  frontend         the {link ServiceFrontend} that instantiated
     **                          this service.
     */
    public Server(final ServiceFrontend frontend) {
      // ensure inheritance
      super(frontend);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: validate (ServiceProvider)
    /**
     ** The entry point to validate the task to perform.
     **
     ** @throws BuildException    in case an error does occur.
     */
    @Override
    public void validate() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke (ConfigHandler)
    /**
     ** Ivokes the command definition for the specfied {@link CommandInstance}.
     **
     ** @param  bind               the command arguments belonging to bind.
     ** @param  instance           the {@link CommandInstance} to apply.
     **
     ** @throws FeatureException   in case an error does occur.
     */
    @Override
    protected void invoke(final List<String> bind, final CommandInstance instance)
      throws FeatureException {

      final List<String> arguments = CollectionUtility.list(bind);
      arguments.addAll(instance.optionArguments());
      arguments.add(instance.name());
      arguments.addAll(instance.parameterArguments());
//      System.out.println(String.format("dsconfig %s", arguments.toString()));
      final String[] args = arguments.toArray(new String[0]);
      int returnCode = 0;
      if (initialize) {
        // avoid the exceution below by set the marker accordingly
        initialize = false;
        // the configuration tool is called again hence we can avoid that
        // DSConfig runs through initialization which will throw exceptions
        returnCode = DSConfig.main(args, !initialize, System.out, System.err);
      }
      else {
       // the configuration tool is called for the first time hence DSConfig
       // needs to initialize itself
       returnCode = DSConfig.main(args, initialize, System.out, System.err);
      }
      if (returnCode != 0) {
        final String[] parameter = {instance.name(), "Return code is " + returnCode};
        if (failonerror())
          throw new BuildException(FeatureResourceBundle.format(FeatureError.COMMAND_EXCECUTION_FAILED, parameter));
        else
          error(FeatureResourceBundle.format(FeatureError.COMMAND_EXCECUTION_FAILED, parameter));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Suffix
  // ~~~~~ ~~~~~~
  /**
   ** The <code>Suffix</code> habdler allows you to create and manage local
   ** suffixes that store data in a local database.
   ** <br>
   ** Although you can also use {@link Server} handler to create and manage
   ** suffixes, the <code>SuffixHandler</code> is a dedicated tool, and much
   ** easier to use.
   ** <br>
   ** For example, the <code>SuffixHandler</code> command requires only a DN to be
   ** able to create a suffix.
   */
  public static final class Suffix extends ConfigHandler {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new <code>Suffix</code> handler to initialize the instance.
     **
     ** @param  frontend         the {link ServiceFrontend} that instantiated
     **                          this service.
     */
    public Suffix(final ServiceFrontend frontend) {
      // ensure inheritance
      super(frontend);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: validate (ServiceProvider)
    /**
     ** The entry point to validate the task to perform.
     **
     ** @throws BuildException    in case an error does occur.
     */
    @Override
    public void validate() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: invoke (ConfigHandler)
    /**
     ** Ivokes the command definition for the specfied {@link CommandInstance}.
     **
     ** @param  bind               the command arguments belonging to bind.
     ** @param  instance           the {@link CommandInstance} to apply.
     **
     ** @throws FeatureException   in case an error does occur.
     */
    @Override
    protected void invoke(final List<String> bind, final CommandInstance instance)
      throws FeatureException {

      final List<String> arguments = CollectionUtility.list(bind);
      arguments.addAll(instance.optionArguments());
      arguments.add(instance.name());
      arguments.addAll(instance.parameterArguments());
//      System.out.println(String.format("managesuffix %s", arguments.toString()));
      final String[] args = arguments.toArray(new String[0]);
      int returnCode = 0;
      if (initialize) {
        // avoid the exceution below by set the marker accordingly
        initialize = false;
        // the configuration tool is called again hence we can avoid that
        // ManageSuffix runs through initialization which will throw exceptions
        returnCode = ManageSuffix.mainCLI(args, !initialize, System.out, System.err, System.in);
      }
      else {
        returnCode = ManageSuffix.mainCLI(args, initialize, System.out, System.err, System.in);
      }
      if (returnCode != 0) {
        final String[] parameter = {instance.name(), "Return code is " + returnCode};
        if (failonerror())
          throw new BuildException(FeatureResourceBundle.format(FeatureError.COMMAND_EXCECUTION_FAILED, parameter));
        else
          error(FeatureResourceBundle.format(FeatureError.COMMAND_EXCECUTION_FAILED, parameter));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ConfigHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this handler.
   */
  protected ConfigHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bindOption
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>bind</code>.
   **
   ** @param  bind               the bind identifier to add.
   ** @param  value              the value to bind at bind.
   */
  public void bindOption(final String bind, final String value) {
    this.bind.put(Bind.from(bind), value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   bindArguments
  /**
   ** Returns the value of the bind option property as a list of command line
   ** arguments.
   **
   ** @return                   the value of the bind option property as a list
   **                           of command line arguments.
   */
  public final List<String> bindArguments() {
    final ODSServerContext context   = ((FeaturePlatformTask)frontend()).context();
    final List<String>     arguments = new ArrayList<String>();
    // hostname and port are always arguments to pass
    arguments.add(Bind.hostname.argument());
    arguments.add(context.host());
    arguments.add(Bind.hostport.argument());
    arguments.add(context.port());
    // if not trust for all certificates is configured evaluate the TLS parameter
    if (!this.bind.containsKey(Bind.trustAll)) {
      // verify if the trust store will be used to bind to the directory server
      if (this.bind.containsKey(Bind.trustStorePath)) {
        arguments.add(Bind.trustStorePath.argument());
        arguments.add(this.bind.get(Bind.trustStorePath));
      }
      // either password or password file or nothing will be used to access
      // trusted key store
      if (this.bind.containsKey(Bind.trustStorePassword)) {
        arguments.add(Bind.trustStorePassword.argument());
        arguments.add(this.bind.get(Bind.trustStorePassword));
      }
      else if (this.bind.containsKey(Bind.trustStorePasswordFile)) {
        arguments.add(Bind.trustStorePasswordFile.argument());
        arguments.add(this.bind.get(Bind.trustStorePasswordFile));
      }
      // verify if the key store will be used to bind to the directory server
      if (this.bind.containsKey(Bind.keyStorePath)) {
        arguments.add(Bind.trustStorePath.argument());
        arguments.add(this.bind.get(Bind.trustStorePath));
      }
      // either password or password file will be used to access client key
      // store
      if (this.bind.containsKey(Bind.keyStorePassword)) {
        arguments.add(Bind.keyStorePassword.argument());
        arguments.add(this.bind.get(Bind.keyStorePassword));
      }
      else if (this.bind.containsKey(Bind.keyStorePasswordFile)) {
        arguments.add(Bind.keyStorePasswordFile.argument());
        arguments.add(this.bind.get(Bind.keyStorePasswordFile));
      }
    }
    else
      arguments.add(Bind.trustAll.argument());

    arguments.add(Bind.username.argument());
    arguments.add(context.username());
    if (this.bind.containsKey(Bind.passwordFile)) {
      arguments.add(Bind.passwordFile.argument());
      arguments.add(this.bind.get(Bind.passwordFile));
    }
    else {
      arguments.add(Bind.password.argument());
      arguments.add(context.password());
    }
    return arguments;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified command to the workload.
   **
   ** @param  instance           the {@link CommandInstance} to add.
   */
  public void add(final CommandInstance instance) {
    this.workload.add(instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Executes all command definition on a Directory Service.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  public void execute()
    throws FeatureException {

    final List<String> bind = bindArguments();
    for (CommandInstance cursor : this.workload) {
      invoke(bind, cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Ivokes the command definition for the specfied {@link CommandInstance}.
   **
   ** @param  bind               the command arguments belonging to bind.
   ** @param  instance           the {@link CommandInstance} to apply.
   **
   ** @throws FeatureException   in case an error does occur.
   */
  protected abstract void invoke(final List<String> bind, final CommandInstance instance)
    throws FeatureException;
}
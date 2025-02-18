package oracle.iam.identity.icf.connector.oig.local;

import org.identityconnectors.framework.common.objects.Schema;

import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.jes.ServerMessage;
import oracle.iam.identity.icf.jes.ServerEndpoint;
import oracle.iam.identity.icf.jes.ServerConnector;
import oracle.iam.identity.icf.jes.LocalConfiguration;

import oracle.iam.identity.icf.resource.ServerBundle;

import oracle.iam.identity.icf.connector.oig.Bundle;

import oracle.iam.identity.icf.connector.oig.schema.ServiceSchema;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** <code>Main</code> implements the basic functionality of an Identity Manager
 ** {@link ServerConnector} for a JES application system.
 ** <p>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ConnectorClass(configurationClass=LocalConfiguration.class, displayNameKey="name", messageCatalogPaths="oracle.iam.identity.icf.connector.oig.local.Main.properties")
public class Main extends Bundle {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Main</code> RMI connector that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Main() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (Connector)
  /**
   ** Initialize the connector with its configuration.
   ** <p>
   ** For instance in a JDBC Connector this would include the database URL,
   ** password, and user.
   **
   ** @param  configuration      the instance of the {@link Configuration}
   **                            object implemented by the
   **                            <code>Connector</code> developer and populated
   **                            with information in order to initialize the
   **                            <code>Connector</code>.
   **
   ** @throws ConnectorException if the authentication/authorization request
   **                            implemented in the {@link Context} constructor
   **                            fails.
   */
  @Override
  public void init(final Configuration configuration)
    throws ConnectorException {

    final String method = "init";
    entering(method);
    this.config  = (LocalConfiguration)configuration;
    this.context = Context.build(this.config.endpoint());
    final ServerEndpoint endpoint = this.context.endpoint();
    try {
      debug(method, ServerBundle.string(ServerMessage.LOGGINGIN, endpoint.serviceURL(), endpoint.principalUsername()));
      // establish the connection to the target system
      this.context.login();
      debug(method, ServerBundle.string(ServerMessage.LOGGEDIN, endpoint.serviceURL(), endpoint.principalUsername()));
    }
    catch (SystemException e) {
      // report the exception in the log spooled for further investigation
      fatal(method, e);
      // send back the exception occured
      propagate(e);
    }
    finally {
      // reset the system properties to the origin
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema (SchemaOp)
  /**
   ** Describes the types of objects this Connector supports.
   ** <p>
   ** This method is considered an operation since determining supported objects
   ** may require configuration information and allows this determination to be
   ** dynamic.
   ** <p>
   ** The special [@link Uid} attribute should never appear in the schema, as it
   ** is not a real attribute of an object, rather a reference to it.
   ** <br>
   ** If the resource object-class has a writable unique id attribute that is
   ** different than its name, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @return                    the configuration that was passed to
   **                            {@link #init(Configuration)}.
   */
  @Override
  public final Schema schema()
    throws ConnectorException {

    final String method = "schema";
    entering(method);
    try {
      if (this.schema == null)
        this.schema = ServiceSchema.instance.schema(oracle.iam.identity.icf.connector.oig.remote.Main.class);
    }
    finally {
      exiting(method);
    }
    return this.schema;
  }
}
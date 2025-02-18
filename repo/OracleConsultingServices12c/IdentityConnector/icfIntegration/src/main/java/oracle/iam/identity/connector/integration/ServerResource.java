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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Integration

    File        :   ServerResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.util.Map;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.AbstractTask;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractResource;
import oracle.iam.identity.foundation.AbstractAttribute;
import oracle.iam.identity.foundation.ITResourceAttribute;

////////////////////////////////////////////////////////////////////////////////
// class ServerResource
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>ServerResource</code> is the value holder for the IT Resource of
 ** a Connector Server assigned to the provisoning or reconciliation task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.1
 ** @since   3.1.0.1
 */
public class ServerResource extends AbstractResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the hostname
   ** or IP address of the Connector Server where this IT Resource will be
   ** working on.
   */
  public static final String SERVERNAME                = "Host";

  /**
   ** Attribute tag which must be defined on an IT Resource to hold the port of
   ** the Connector Server where this IT Resource will be working on.
   */
  public static final String SERVERPORT               = "Port";

  /**
   ** Attribute tag which must be defined on an IT Resource to specify if the
   ** communication with the Connector Server is secured.
   */
  public static final String SECURESOCKET             = "UseSSL";

  /**
   ** Attribute tag which must be defined on an IT Resource to specify the
   ** security token.
   */
  public static final String SECURETOKEN              = "Key";

  /**
   ** Attribute tag which must be defined on an IT Resource to specifiy the time
   ** (in milliseconds) within which the target system is expected to respond to
   ** a connection attempt.
   */
  public static final String CONNECTIONTIMEOUT        = "Timeout";

  /** the array with the attributes for the IT Resource */
  private static final ITResourceAttribute[] attribute = {
    ITResourceAttribute.build(SERVERNAME,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SERVERPORT,        ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURETOKEN,       ITResourceAttribute.MANDATORY)
  , ITResourceAttribute.build(SECURESOCKET,      ITResourceAttribute.OPTIONAL)
  , ITResourceAttribute.build(CONNECTIONTIMEOUT, ITResourceAttribute.OPTIONAL)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerResource</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will not be populated from the repository of
   ** the Identity Manager. Only instance key and instance name are obtained.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  protected ServerResource(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the <code>IT Resource</code>
   ** specified by the given instance key.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of
   ** Identity Manager and also all well known attributes.
   ** <p>
   ** Usual an instance of this wrapper will be created in this manner if
   ** the Connection Pool is used to aquire a connection
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the system identifier of the
   **                            <code>IT Resource</code> instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries.
   */
  public ServerResource(final Loggable loggable, final Long instance)
    throws TaskException {

    // ensure inheritance
    super(loggable, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerResource</code> which is associated with the
   ** specified {@link Loggable} and belongs to the IT Resource specified by the
   ** given name.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the repository of
   ** Identity Manager and also all well known attributes.
   **
   ** @param  task               the {@link AbstractTask} that instantiate this
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link AbstractTask}.
   ** @param  instance           the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      if the IT Resource is not defined in the Oracle
   **                            Identity manager meta entries or one or more
   **                            attributes are missing on the IT Resource Type
   **                            Definition.
   */
  protected ServerResource(final AbstractTask task, final String instance)
    throws TaskException {

    // ensure inheritance
    super(task, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerResource</code> that is not associated with a
   ** task.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameter {link Map}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of this
   **                            <code>IT Resource</code> configuration where
   **                            this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            IT Resource Type Definition.
   */
  protected ServerResource(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    // ensure inheritance
    super(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerResource</code> which is associated with the
   ** specified task.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverName         the host name or IP address of the Connector
   **                            Server is deployed on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Connector Server is listening on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Connector Server.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  secureToken        the token to secure the communication between
   **                            Identity Manager and the Connector Server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeout            the time in milliseconds to wait a connection
   **                            should be established until the attempts to
   **                            connect will be timed out.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            IT Resource Type Definition.
   */
  protected ServerResource(final Loggable loggable, final String serverName, final String serverPort, final boolean secureSocket, final String secureToken, final int timeout)
    throws TaskException {

    // ensure inheritance
    super(loggable);

    attribute(SERVERNAME,        serverName);
    attribute(SERVERPORT,        serverPort);
    attribute(SECURESOCKET,      secureSocket  ? SystemConstant.TRUE : SystemConstant.FALSE);
    attribute(SECURETOKEN,       secureToken);
    attribute(CONNECTIONTIMEOUT, String.valueOf(timeout));

    validateAttributes(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverName
  /**
   ** Returns the name of the server where the Connector Server is deployed on
   ** and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_NAME}.
   **
   ** @return                    the name of the server where the Connector
   **                            Server is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String serverName() {
    return stringValue(SERVERNAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverPort
  /**
   ** Returns the listener port of the server the Connector Server is deployed
   ** on and this <code>IT Resource</code> is configured for.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SERVER_PORT}.
   **
   ** @return                    the listener port of the server the Connector
   **                            Server is deployed on and this
   **                            <code>IT Resource</code> is configured for.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int serverPort() {
    return integerValue(SERVERPORT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstrcatConnector)
  /**
   ** Returns the array with names which should be populated from the
   ** IT Resource definition of Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   **                            <br>
   **                            Possible object is array of
   **                            {@link AbstractAttribute}.
   */
  @Override
  public AbstractAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerResource</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The IT Resource specified does not belong a {@link AbstractResource} thus
   ** it has to populated manually.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an instance of <code>ServerResource</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is {@link ServerResource}.
   */
  public static ServerResource build(final Loggable loggable) {
    return new ServerResource(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerResource</code> which is associated
   ** with the specified task and belongs to the IT Resource specified by the
   ** given name.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known
   ** attributes using the specified <code>instance</code> name.
   **
   ** @param  task               the {@link AbstractTask} that instantiate the
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link AbstractTask}.
   ** @param  instance           the name of the IT Resource instance where this
   **                            wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServerResource</code>.
   **                            <br>
   **                            Possible object is <code>ServerResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  public static ServerResource build(final AbstractTask task, final String instance)
    throws TaskException {

    return new ServerResource(task, instance);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerResource</code> which is associated
   ** with the specified task and belongs to the IT Resource specified by the
   ** given name.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  instance           the internal identifier of an
   **                            <code>IT Resource</code> in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServerResource</code>.
   **                            <br>
   **                            Possible object is {@link ServerResource}.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given key or one or more attributes are missing
   **                            on the <code>IT Resource</code> instance.
   */
  public static ServerResource build(final Loggable loggable, final Long instance)
    throws TaskException {

    return new ServerResource(loggable, instance);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerResource</code> that is not
   ** associated with a task.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameter {link Map}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  parameter          the {@link Map} providing the parameter of this
   **                            <code>IT Resource</code> configuration where
   **                            this wrapper belongs to.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServerResource</code>.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            IT Resource Type Definition.
   */
  public static ServerResource build(final Loggable loggable, final Map<String, String> parameter)
    throws TaskException {

    return new ServerResource(loggable, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ServerResource</code> which is associated
   ** with the specified task and belongs to the IT Resource specified by the
   ** given name.
   ** <br>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> name.
   **
   ** @param  loggable           the {@link Loggable} that instantiate the
   **                            <code>ServerResource</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serverName         the host name or IP address of the Connector
   **                            Server is deployed on.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Connector Server is listening on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Connector Server.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  secureToken        the token to secure the communication between
   **                            Identity Manager and the Connector Server.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeout            the time in milliseconds to wait a connection
   **                            should be established until the attempts to
   **                            connect will be timed out.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a populated <code>IT Resource</code> instance
   **                            wrapped as <code>ServerResource</code>.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  public static ServerResource build(final Loggable loggable, final String serverName, final String serverPort, final boolean secureSocket, final String secureToken, final int timeout)
    throws TaskException {

    return new ServerResource(loggable, serverName, serverPort, secureSocket, secureToken, timeout);
  }
}
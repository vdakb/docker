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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   ServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.BuildException;

////////////////////////////////////////////////////////////////////////////////
// abstract class ServerContext
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>ServerContext</code> a generic server and runtime implementation that
 ** can adjust its behaviour by a server type definition.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ServerContext extends AbstractContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String CONTEXT_TYPE             = "server";

  /**
   ** Default value of the timeout period for reading data on an already
   ** established connection.
   ** <br>
   ** The unit of this values are milliseconds
   */
  public static final int    TIMEOUT_DEFAULT_RESPONSE = 60000;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ServerType         type                     = null;
  private int                timeoutResponse          = TIMEOUT_DEFAULT_RESPONSE;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link ServerType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the RMI/JNDI
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the RMI/JNDI provider.
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            content provider connection.
   **                            Allowed object is <code>int</code>.
   ** @param  timeoutResponse    the timeout period for reading data on an
   **                            already established connection.
   **                            Allowed object is <code>int</code>.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  protected ServerContext(final ServerType type, final String protocol, final String host, final String port, final int timeoutConnection, final int timeoutResponse, final SecurityPrincipal principal) {
    // ensure inheritance
    super(protocol, host, port, timeoutConnection, principal);

    // initialize instance attributes
    this.type            = type;
    this.timeoutResponse = timeoutResponse;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>type</code>.
   **
   ** @param  type               the value for the attribute
   **                            <code>type</code> used as the RMI/JNDI
   **                            provider.
   **                            Allowed object is {@link ServerType}.
   **
   ** @throws BuildException     if the <code>type</code> is already specified
   **                            by a reference.
   */
  public final void setType(final ServerType type)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("type");

    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the server used to connect to.
   **
   ** @return                    the type of the server used to connect to.
   **                            Possible object is {@link ServerType}.
   */
  public final ServerType type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeOutResponse
  /**
   ** Sets the timeout period for reading data on an already established
   ** connection.
   ** <p>
   ** A non-zero value specifies the timeout when reading from input stream when
   ** a connection is established to a resource. If the timeout expires before
   ** there is data available for read, a
   ** java.net.SocketTimeOutResponseException is raised.
   ** <br>
   ** A timeout of zero is interpreted as an infinite timeout.
   **
   ** @param  value              the maximum time between establishing a
   **                            connection and receiving data from the
   **                            connection.
   **                            Allowed object is <code>int</code>.
   */
  public final void setTimeOutResponse(final int value) {
    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("timeoutResponse");

    this.timeoutResponse = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeoutResponse
  /**
   ** Returns the timeout period for reading data on an already established
   ** connection.
   ** <p>
   ** A non-zero value specifies the timeout when reading from Input stream when
   ** a connection is established to a resource. If the timeout expires before
   ** there is data available for read, a java.net.SocketTimeOutResponseException is
   ** raised. A timeout of zero is interpreted as an infinite timeout.
   **
   ** @return                    the maximum time between establishing a
   **                            connection and receiving data from the
   **                            connection.
   **                            Possible object is <code>int</code>.
   */
  public final int timeoutResponse() {
    return this.timeoutResponse;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to perform.
   */
  @Override
  public void validate() {
    if (this.type == null)
      handleAttributeMissing("ServerType");

    // ensure inheritance
    super.validate();
  }
}
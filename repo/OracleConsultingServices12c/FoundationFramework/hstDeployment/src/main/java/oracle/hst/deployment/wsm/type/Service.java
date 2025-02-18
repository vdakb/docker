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
    Subsystem   :   Deployment Utilities 12c

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Service.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wsm.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.hst.deployment.spi.WebServicePolicyHandler;

////////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
public abstract class Service extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final WebServicePolicyHandler.Service delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Server
  // ~~~~~ ~~~~~~
  /**
   ** Invokes the Runtime JMX Bean to configure WebService Manager Server
   ** Policies.
   */
  public static class Server extends Service {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Server</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Server() {
      // ensure inheritance
      super(true);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Client
  // ~~~~~ ~~~~~~
  /**
   ** Invokes the Runtime JMX Bean to configure WebService Manager Client
   ** Policies.
   */
  public static class Client extends Service {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Client</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Client() {
      // ensure inheritance
      super(false);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> type that belongs either to the server
   ** or client side.
   ** <br>
   **
   ** @param  server             <code>true</code> if the service belongs to a
   **                            server; <code>false</code> if the service
   **                            belongs to a client.
   **                            Allowed object is <code>boolean</code>.
   */
  public Service(final boolean server) {
    // ensure inheritance
    super();

    // initialize instance
    this.delegate = new WebServicePolicyHandler.Service(server);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>type</code>.
   **
   ** @param  type               the type of the WebService to handle in Oracle
   **                            WebLogic Domain.
   **                            The value is either <code>wls</code> or
   **                            <code>jrf</code> where <code>jrf</code> is the
   **                            default.
   **                            Allowed object is {@link ServiceType}.
   */
  public void setType(final ServiceType type) {
    checkAttributesAllowed();
    this.delegate.type(WebServicePolicyHandler.Service.ServiceType.from(type.getValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>location</code>.
   **
   ** @param  location           the name of the location to handle in Oracle
   **                            WebLogic Domain.
   **                            Allowed object is {@link String}.
   */
  public void setLocation(final String location) {
    checkAttributesAllowed();
    this.delegate.location(location);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the application to handle in Oracle
   **                            WebLogic Domain.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String name) {
    checkAttributesAllowed();
    this.delegate.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setService
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>service</code>.
   **
   ** @param  name               the name of the bean in Oracle WebLogic Domain
   **                            to handle.
   **                            Allowed object is {@link String}.
   */
  public void setService(final String name) {
    checkAttributesAllowed();
    this.delegate.serviceName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModule
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>module</code>.
   **
   ** @param  name               the name of the bean in Oracle WebLogic Domain
   **                            to handle.
   **                            Allowed object is {@link String}.
   */
  public void setModule(final String name) {
    checkAttributesAllowed();
    this.delegate.moduleName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setModuleType
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>moduleType</code>.
   **
   ** @param  type               the module type of a WebServivce in Oracle
   **                            WebLogic Domain to handle.
   **                            Allowed object is {@link ModuleType}.
   */
  public void setModuleType(final ModuleType type) {
    checkAttributesAllowed();
    this.delegate.moduleType(WebServicePolicyHandler.Service.ModuleType.from(type.getValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPort
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>port</code>.
   **
   ** @param  port               the port of the bean in Oracle WebLogic Domain
   **                            to handle.
   **                            Allowed object is {@link String}.
   */
  public void setPort(final String port) {
    checkAttributesAllowed();
    this.delegate.portName(port);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link WebServicePolicyHandler.Service} delegate.
   **
   ** @return                    the {@link WebServicePolicyHandler.Service}
   **                            delegate.
   **                            Possible object is
   **                            {@link WebServicePolicyHandler.Service}.
   */
  public final WebServicePolicyHandler.Service instance() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredAttach
  /**
   ** Add the specified policies to the operations that has to be attached to
   ** the service.
   **
   ** @param  operation          the policies to be attached to the Oracle
   **                            WebLogic Domain webservice instance.
   **                            Allowed object is {@link Operation.Attach}.
   */
  public void addConfiguredAttach(final Operation.Attach operation) {
    for (Policy policy : operation.policy())
      this.delegate.addAttach(policy.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredDetach
  /**
   ** Add the specified policies to the operations that has to be attached to
   ** the service.
   **
   ** @param  operation          the policies to be attached to the Oracle
   **                            WebLogic Domain webservice instance.
   **                            Allowed object is {@link Operation.Detach}.
   */
  public void addConfiguredDetach(final Operation.Detach operation) {
    for (Policy policy : operation.policy())
      this.delegate.addDetach(policy.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredEnable
  /**
   ** Add the specified policies to the operations that has to be enabled on
   ** the service.
   **
   ** @param  operation          the policies to be enabled on the Oracle
   **                            WebLogic Domain webservice instance.
   **                            Allowed object is {@link Operation.Enable}.
   */
  public void addConfiguredEnable(final Operation.Enable operation) {
    for (Policy policy : operation.policy())
      this.delegate.addEnable(policy.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredDisable
  /**
   ** Add the specified policies to the operations that has to be disabled on
   ** the service.
   **
   ** @param  operation          the policies to be disabled on the Oracle
   **                            WebLogic Domain webservice instance.
   **                            Allowed object is {@link Operation.Disable}.
   */
  public void addConfiguredDisable(final Operation.Disable operation) {
    for (Policy policy : operation.policy())
      this.delegate.addDisable(policy.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredReport
  /**
   ** Add the specified policies to the operations that has to be reported on
   ** the service.
   **
   ** @param  operation          the policies to be reported on the Oracle
   **                            WebLogic Domain webservice instance.
   **                            Allowed object is {@link Operation.Report}.
   */
  public void addConfiguredReport(final Operation.Report operation) {
    for (Policy policy : operation.policy())
      this.delegate.addReport(policy.instance());
  }
}
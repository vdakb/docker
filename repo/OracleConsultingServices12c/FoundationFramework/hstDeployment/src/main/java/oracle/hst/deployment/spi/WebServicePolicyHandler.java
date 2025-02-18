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

    File        :   WebServicePolicyHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WebServicePolicyHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import javax.management.MBeanServerConnection;

import javax.management.ObjectName;

import oracle.hst.deployment.ServiceAction;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class WebServicePolicyHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to manage the security context in Oracle
 ** WebLogic Server domains related to Webservices.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WebServicePolicyHandler extends EnvironmentHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final List<Service> multiple = new ArrayList<Service>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Policy
  // ~~~~~ ~~~~~~
  public static class Policy extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the category of the <code>PolicyReference</code>; security is the
     ** default
     **/
    String category = "security";

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Policy</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Policy() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   category
    /**
     ** Sets the category of the <code>PolicyReference</code>.
     **
     ** @param  value            the category of the
     **                          <code>PolicyReference</code>.
     **                          Allowed object is {@link String}.
     */
    public final void category(final String value) {
      this.category = value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Service
  // ~~~~~ ~~~~~~~
  public static class Service extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the type of the WebService; the deafult is 'jrf' */
    ServiceType        type        = ServiceType.JRF;

    /** the location (aka Managed Server) of the service */
    String             location;

    /**
     ** the name of the Web service in the application or SOA composite
     ** <br>
     ** For example, {http://namespace/}serviceName.
     ** <br>
     ** Note that the namespace ({http://namespace/}) should not be included for
     ** a SOA composite. */
    String             serviceName;

    /**
     ** the name of the Web module or SOA composite
     ** <br>
     ** For example HelloWorld[1.0])
     ** <br>
     ** Note for a SOA composite, the composite name is required (for example
     ** <code>default/HelloWorld[1.0]</code>), and the <code>moduleType</code>
     ** must be set to <code>soa</code>.
     */
    String             moduleName;

    /** the module type can be web, soa or wls; default is 'web' */
    ModuleType         moduleType  = ModuleType.WEB;

    String             portName;

    /** indicates that the service belongs to the server or client side */
    final boolean      server;

    /** the policies to be attached to the service endpoint */
    final List<Policy> attach  = new ArrayList<Policy>();

    /** the policies to be detached from the service endpoint */
    final List<Policy> detach  = new ArrayList<Policy>();

    /** the policies to be enabled to the service endpoint */
    final List<Policy> enable  = new ArrayList<Policy>();

    /** the policies to be disabled to the service endpoint */
    final List<Policy> disable = new ArrayList<Policy>();

    /** the policies to be reported on the service endpoint */
    final List<Policy> report  = new ArrayList<Policy>();

    ////////////////////////////////////////////////////////////////////////////
    // class ServiceType
    // ~~~~~ ~~~~~~~~~~~
    public static enum ServiceType implements ServiceAction {
        /** Java Enterprise Edition Web services */
        WLS("wls")
        /**
         ** Oracle Infrastructure Web Services packaged as a Web module
         ** (including an EJB)
         */
      , JRF("jrf");

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      // the official serial version ID which says cryptically which version
       // we're compatible with
       private static final long serialVersionUID = -1L;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>ServiceType</code> that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      ServiceType(final String id) {
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   id (ServiceAction)
      /**
       ** Returns the id of the property.
       **
       ** @return                    the id of the property.
       */
      @Override
      public String id() {
        return this.id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

       /////////////////////////////////////////////////////////////////////////
      // Method: from
      /**
       ** Factory method to create a proper service operation from the given
       ** string value.
       **
       ** @param  value          the string value the service operation should
       **                        be returned for.
       **
       ** @return                the service operation property.
       */
      public static ServiceType from(final String value) {
        for (ServiceType cursor : ServiceType.values()) {
          if (cursor.id.equals(value))
            return cursor;
        }
        throw new IllegalArgumentException(value);
      }
    }
    ////////////////////////////////////////////////////////////////////////////
    // class ModuleType
    // ~~~~~ ~~~~~~~~~~
    public static enum ModuleType implements ServiceAction {
        /**
         ** Oracle Infrastructure Web Services packaged as a Web module
         ** (including an EJB)
         */
        WEB("web")
        /** Java Enterprise Edition Web services */
      , WLS("wls")
        /** SOA composite */
      , SOA("soa")
      ;

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      // the official serial version ID which says cryptically which version
       // we're compatible with
       private static final long serialVersionUID = -1L;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>ModuleType</code> that allows use as a JavaBean.
       ** <br>
       ** Zero argument constructor required by the framework.
       ** <br>
       ** Default Constructor
       */
      ModuleType(final String id) {
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method:   id (ServiceAction)
      /**
       ** Returns the id of the property.
       **
       ** @return                    the id of the property.
       */
      @Override
      public String id() {
        return this.id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

       /////////////////////////////////////////////////////////////////////////
      // Method: from
      /**
       ** Factory method to create a proper module type from the given
       ** string value.
       **
       ** @param  value          the string value the module type should
       **                        be returned for.
       **
       ** @return                the module type property.
       */
      public static ModuleType from(final String value) {
        for (ModuleType cursor : ModuleType.values()) {
          if (cursor.id.equals(value))
            return cursor;
        }
        throw new IllegalArgumentException(value);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Service</code> type that belongs either to the server
   ** or client side.
     **
     ** @param  server           <code>true</code> if the service belongs to a
     **                          server; <code>false</code> if the service
     **                          belongs to a client.
     */
    public Service(final boolean server) {
      // ensure inheritance
      super();

      // initialize instance
      this.server = server;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   location
    /**
     ** Called to inject the argument for parameter <code>location</code>.
     **
     ** @param  location         the location of the service.
     **                          Allowed object is {@link String}.
     */
    public final void location(final String location) {
      this.location = location;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   type
    /**
     ** Called to inject the argument for parameter <code>type</code>.
     **
     ** @param  type             the {@link ServiceType} of the service.
     **                          Allowed object is {@link ServiceType}.
     */
    public final void type(final ServiceType type) {
      this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   serviceName
    /**
     ** Called to inject the argument for parameter <code>serviceName</code>.
     **
     ** @param  name             the name of the service or None for SOA.
     **                          Allowed object is {@link String}.
     */
    public final void serviceName(final String name) {
      this.serviceName = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   moduleName
    /**
     ** Called to inject the argument for parameter <code>moduleName</code>.
     **
     ** @param  name             the web module name or SOA composite name.
     **                          e.g.: HelloWorld[1.0].
     **                          Allowed object is {@link String}.
     */
    public final void moduleName(final String name) {
      this.moduleName = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   moduleType
    /**
     ** Called to inject the argument for parameter <code>moduleType</code>.
     **
     ** @param  type             the module type can be web or soa.
     **                          Allowed object is {@link ModuleType}.
     */
    public final void moduleType(final ModuleType type) {
      this.moduleType = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   portName
    /**
     ** Called to inject the argument for parameter <code>port</code>.
     **
     ** @param  port             the port context to handle in Oracle Weblogic
     **                          Domain server entity instance.
     **                          Allowed object is {@link String}.
     */
    public final void portName(final String port) {
      this.portName = port;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groupded by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   validate (overridden)
    /**
     ** The entry point to validate the type to use.
     **
     ** @throws BuildException   in case the instance does not meet the
     **                          requirements.
     */
    @Override
    public void validate()
      throws BuildException {

      // ensure inheritance
      super.validate();

      if (this.type == null)
        handleAttributeMissing("type");

      if (StringUtility.isEmpty(this.serviceName))
        handleAttributeMissing("serviceName");

      if (this.moduleType == null)
        handleAttributeMissing("moduleType");

      if (StringUtility.isEmpty(this.portName))
        handleAttributeMissing("portName");

      if (this.attach.isEmpty() && this.detach.isEmpty() && this.enable.isEmpty() && this.disable.isEmpty()&& this.report.isEmpty())
        throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

      for (Policy policy : this.attach)
        policy.validate();

      for (Policy policy : this.detach)
        policy.validate();

      for (Policy policy : this.enable)
        policy.validate();

      for (Policy policy : this.disable)
        policy.validate();

      for (Policy policy : this.report)
        policy.validate();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addAttach
    /**
     ** Add the specified {@link Policy} to the policies that have to be
     ** attached.
     **
     ** @param  policy           the {@link Policy} that have to be attached.
     **
     ** @throws BuildException   if the specified {@link Service} is already
     **                          assigned to this task.
     **                          Allowed object is {@link Policy}.
     */
    public void addAttach(final Policy policy)
      throws BuildException {

      // prevent bogus input
      if (this.attach.contains(policy))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, policy.name()));

      // add the instance to the object to handle
      this.attach.add(policy);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addDetach
    /**
     ** Add the specified {@link Policy} to the policies that have to be
     ** detached.
     **
     ** @param  policy           the {@link Policy} that have to be detached.
     **                          Allowed object is {@link Policy}.
     **
     ** @throws BuildException   if the specified {@link Service} is already
     **                          assigned to this task.
     */
    public void addDetach(final Policy policy)
      throws BuildException {

      // prevent bogus input
      if (this.detach.contains(policy))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, policy.name()));

      // add the instance to the object to handle
      this.detach.add(policy);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addEnable
    /**
     ** Add the specified {@link Policy} to the policies that have to be
     ** enabled.
     **
     ** @param  policy           the {@link Policy} that have to be enabled.
     **                          Allowed object is {@link Policy}.
     **
     ** @throws BuildException   if the specified {@link Service} is already
     **                          assigned to this task.
     */
    public void addEnable(final Policy policy)
      throws BuildException {

      // prevent bogus input
      if (this.enable.contains(policy))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, policy.name()));

      // add the instance to the object to handle
      this.enable.add(policy);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addDisable
    /**
     ** Add the specified {@link Policy} to the policies that have to be
     ** disabled.
     **
     ** @param  policy           the {@link Policy} that have to be disabled.
     **                          Allowed object is {@link Policy}.
     **
     ** @throws BuildException   if the specified {@link Service} is already
     **                          assigned to this task.
     */
    public void addDisable(final Policy policy)
      throws BuildException {

      // prevent bogus input
      if (this.disable.contains(policy))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, policy.name()));

      // add the instance to the object to handle
      this.disable.add(policy);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addReport
    /**
     ** Add the specified {@link Policy} to the policies that have to be
     ** reported.
     **
     ** @param  policy           the {@link Policy} that have to be reported.
     **                          Allowed object is {@link Policy}.
     **
     ** @throws BuildException   if the specified {@link Service} is already
     **                          assigned to this task.
     */
    public void addReport(final Policy policy)
      throws BuildException {

      // prevent bogus input
      if (this.report.contains(policy))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, policy.name()));

      // add the instance to the object to handle
      this.report.add(policy);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>WebServicePolicyHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   **                            Allowed object is {@link ServiceFrontend}.
   */
  public WebServicePolicyHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  public void validate()
    throws BuildException {

    if (this.multiple.isEmpty())
      throw new BuildException(ServiceResourceBundle.string(ServiceError.OBJECT_ELEMENT_MANDATORY));

    for (Service i : this.multiple)
      i.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified {@link Service} to the policies that have to be
   ** granted.
   **
   ** @param  service            the {@link Service} that have to be granted.
   **
   ** @throws BuildException     if the specified {@link Service} is already
   **                            assigned to this task.
   **                            Allowed object is {@link Service}.
   */
  public void add(final Service service)
    throws BuildException {

    // prevent bogus input
    if (this.multiple.contains(service))
      throw new BuildException(ServiceResourceBundle.format(ServiceError.OBJECT_ELEMENT_ONLYONCE, service.name()));

    // add the instance to the object to handle
    this.multiple.add(service);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Performs all action on the instances assigned to this task through the
   ** given {@link MBeanServerConnection} to attach/detach policies to or from
   ** the web service.
   **
   ** @param  connection         the {@link MBeanServerConnection} providing
   **                            access to the remote service facades.
   **                            Allowed object is
   **                            {@link MBeanServerConnection}.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void execute(final MBeanServerConnection connection)
    throws ServiceException {

    // initialize the business logic layer to operate on
    WebServiceManager manager = null;
    try {
      manager = new WebServiceManager(this.frontend, connection);
    }
    catch (Throwable t) {
      handleThrowable(t);
    }

    boolean restart = false;
    for (Service service : this.multiple) {
      boolean changed = false;
      final ObjectName serviceEndpoint = manager.serviceEndpoint(service);
      if (service.report.size() > 0)
        manager.reportPolicy(serviceEndpoint, service.name(), service.report);

      // performs all action on the service instances assigned to this task
      // through the given {@link MBeanServerConnection} to
      // attach/detach/enable/disable policies on the web service endpoint.
      if (service.detach.size() > 0)
        changed = changed ?  changed : manager.detachPolicy(serviceEndpoint, service.name(), service.detach);

      if (service.attach.size() > 0)
        changed = changed ?  changed : manager.attachPolicy(serviceEndpoint, service.name(), service.attach, service.server);

      if (service.disable.size() > 0)
        changed = changed ?  changed : manager.togglePolicy(serviceEndpoint, service.name(), service.enable, false);

      if (service.enable.size() > 0)
        changed = changed ?  changed : manager.togglePolicy(serviceEndpoint, service.name(), service.enable, true);

      restart = restart ? restart : changed && Service.ModuleType.WEB == service.moduleType;
      if (restart)
        warning(ServiceResourceBundle.format(ServiceMessage.WEBSERVICE_RESTART, service.name()));
    }
  }
}
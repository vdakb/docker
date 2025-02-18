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

    System      :   Identity Manager Library
    Subsystem   :   Metadata Service Utilities 11g

    File        :   Metadata.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Metadata.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import oracle.hst.deployment.task.AbstractInvokerTask;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class Metadata
// ~~~~~~~~ ~~~~~ ~~~~~~~~
/**
 ** Oracle Metadata Services (MDS) Repository contains metadata for certain
 ** types of deployed applications. Those deployed applications can be custom
 ** Java EE applications and some Oracle Fusion Middleware component
 ** applications, such as Oracle Identity Manager and Oracle Web Services
 ** Manager.
 ** <p>
 ** Oracle Fusion Middleware supports multiple repository types. A repository
 ** type represents a specific schema or set of schemas that belong to a
 ** specific Oracle Fusion Middleware component (for example, Oracle BPEL
 ** Process Manager or Oracle Internet Directory.) Oracle Fusion Middleware
 ** supports Edition-Based Redefinition (EBR), which enables you to upgrade the
 ** database component of an application while it is in use, thereby minimizing
 ** or eliminating down time. The schemas in a repository can be EBR-enabled
 ** schemas.
 */
public abstract class Metadata extends AbstractInvokerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String UNVERSIONED = "oracle.mds.lcm:Location=%s,name=MDSAppRuntime,type=MDSAppRuntime,Application=%s";
  private static final String VERSIONED   = "oracle.mds.lcm:Location=%s,name=MDSAppRuntime,type=MDSAppRuntime,Application=%s,ApplicationVersion=%s";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String              location;
  private String              application;
  private String              version;

  private ObjectName          name;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Metadata</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Metadata() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Metadata</code> event handler that use the specified
   ** {@link Metadata} task providing the JMX bean properties.
   **
   ** @param  other              <code>Metadata</code> task providing the JMX
   **                            bean properties.
   */
  protected Metadata(final Metadata other) {
    // ensure inheritance
    super();

    // initialize instance
    this.location    = other.location;
    this.application = other.application;
    this.version     = other.version;
    this.name        = other.objectName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLocation
  /**
   ** Sets the name of the location the runtime server has to connect to.
   **
   ** @param  location           the name of the location the runtime server
   **                            has to connect to.
   */
  public final void setLocation(final String location) {
    this.location = location;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name     = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns the name of the location the runtime server has to connect to.
   **
   ** @return                    the name of the location the runtime server has
   **                            to connect to.
   */
  protected final String location() {
    return this.location;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setApplication
  /**
   ** Sets the name of the application the runtime server has to connect to.
   **
   ** @param  application        the name of the application the runtime server
   **                            has to connect to.
   */
  public final void setApplication(final String application) {
    this.application = application;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name        = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the name of the application the runtime server has to connect to.
   **
   ** @return                    the name of the application the runtime server
   **                            has to connect to.
   */
  protected final String application() {
    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion
  /**
   ** Sets the version of the application the runtime server has to connect to.
   **
   ** @param  version            the version of the application the runtime
   **                            server has to connect to.
   */
  public final void setVersion(final String version) {
    this.version = version;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name    = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the version of the application the runtime server has to connect
   ** to.
   **
   ** @return                    the version of the application the runtime
   **                            server has to connect to.
   */
  protected final String version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runtimeService (AbstactServiceTask)
  /**
   ** Returns the name of the runtime service to connect to.
   **
   ** @return                    the name of the runtime service to connect to.
   */
  @Override
  protected final String runtimeService() {
    return AbstractInvocationHandler.DOMAIN_SERVER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvokerTask)
  /**
   ** Returns the JMX {@link ObjectName}  this task will execute.
   **
   ** @return                    the JMX {@link ObjectName}  this task will
   **                            execute.
   */
  @Override
  protected final ObjectName objectName() {
    if (this.name == null) {
      synchronized (UNVERSIONED) {
        try {
          if (this.version == null)
            this.name = new ObjectName(String.format(UNVERSIONED, this.location, this.application));
          else
            this.name = new ObjectName(String.format(VERSIONED, this.location, this.application, this.version));
        }
        catch (MalformedObjectNameException e) {
          throw new AssertionError(e.getMessage());
        }
      }
    }
    return this.name;
  }
}
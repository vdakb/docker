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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ApplicationRuntimeHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationRuntimeHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Set;
import java.util.HashSet;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.AbstractInvocationHandler;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// abstract class ApplicationRuntimeHandler
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>ApplicationRuntimeHandler</code> provides access to the application
 ** runtime configuration JMX MBeans of Oracle Access Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class ApplicationRuntimeHandler extends AbstractInvocationHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  APPLICATION = "oam_admin";
  public static final String  NAME        = "OamWLST";
  public static final String  TYPE        = "oam.wlst";

  private static final String BEAN        = "com.oracle.oam:Application=%s,name=%s,type=%s";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String> flatten     = new HashSet<String>();

  private final String[]      property    = { APPLICATION, NAME, TYPE };
  private ObjectName          name        = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationRuntimeHandler</code> Ant task which will
   ** gain access to a certain JMX bean.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#print}
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  protected ApplicationRuntimeHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend);

    // initiallize instance attributes
    this.operation(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Sets the name of the application the runtime server has to connect to.
   **
   ** @param  application        the name of the application the runtime server
   **                            has to connect to.
   */
  public final void application(final String application) {
    this.property[0] = application;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name = null;
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
    return this.property[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Sets the version of the application the runtime server has to connect to.
   **
   ** @param  version            the version of the application the runtime
   **                            server has to connect to.
   */
  public final void version(final String version) {
    this.property[1] = version;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name        = null;
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
    return this.property[1];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the runtime bean to perform operations.
   **
   ** @param  name               the name of the runtime bean to perform
   **                            operations.
   */
  public final void name(final String name) {
    this.property[2] = name;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the runtime bean to perform operations.
   **
   ** @return                    the name of the runtime bean to perform
   **                            operations.
   */
  protected final String name() {
    return this.property[2];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type of the runtime bean to perform operations.
   **
   ** @param  type               the type of the runtime bean to perform
   **                            operations.
   */
  public final void type(final String type) {
    this.property[3] = type;
    // reset the bean name to ensure the object name is rebuild on subsequently
    // calls
    this.name = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the runtime bean to perform operations.
   **
   ** @return                    the type of the runtime bean to perform
   **                            operations.
   */
  protected final String type() {
    return this.property[3];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectName (AbstractInvokerTask)
  /**
   ** Returns the JMX {@link ObjectName} this task will execute.
   **
   ** @return                    the JMX {@link ObjectName} this task will
   **                            execute.
   **
   ** @throws FeatureException   if the JMX {@link ObjectName} to build is
   **                            malformed.
   */
  @Override
  protected ObjectName objectName()
    throws FeatureException {

    if (this.name == null) {
      synchronized(BEAN) {
        try {
          this.name = new ObjectName(String.format(BEAN, this.property));
        }
        catch (MalformedObjectNameException e) {
          throw new FeatureException(FeatureError.DOMAIN_OBJECTNAME_MALFORMED, e);
        }
      }
    }
    return this.name;
  }
}
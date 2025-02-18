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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Approval Facility

    File        :   ServiceProviderFactory.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceProviderFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.context;

import java.util.Map;
import java.util.HashMap;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.workflow.context.spi.JBoss;
import oracle.iam.identity.workflow.context.spi.Glassfish;
import oracle.iam.identity.workflow.context.spi.WebLogic;
import oracle.iam.identity.workflow.context.spi.WebSphere;

////////////////////////////////////////////////////////////////////////////////
// class ServiceProviderFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implements generic methods for instantiation of {@link ServiceProvider}s.
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 ** <br>
 ** Implements the service locator design pattern.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class ServiceProviderFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final ServiceProviderFactory                                       instance = new ServiceProviderFactory();

  private static final Map<ServiceProvider.Type, Class<? extends ServiceProvider>> provider = new HashMap<ServiceProvider.Type, Class<? extends ServiceProvider>>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    synchronized(provider) {
      provider.put(ServiceProvider.Type.WEBLOGIC,  WebLogic.class);
      provider.put(ServiceProvider.Type.WEBSPHERE, WebSphere.class);
      provider.put(ServiceProvider.Type.GLASSFISH, Glassfish.class);
      provider.put(ServiceProvider.Type.JBOSS,     JBoss.class);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (protected)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new ServiceProviderFactory()"
   */
  private ServiceProviderFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates the {@link ServiceProvider} for the specified server type to
   ** establish a connection to the target system.
   **
   ** @param  type               the type of the server to bind to.
   **
   ** @return                    the {@link ServiceProvider} used to communicate
   **                            with the server.
   **
   ** @throws TaskException      if the {@link ServiceProvider} could not be
   **                            instantiated.
   */
  public static ServiceProvider create(final ServiceProvider.Type type)
    throws TaskException {
    Class<? extends ServiceProvider> clazz = provider.get(type);
    try {
      return clazz.newInstance();
    }
    catch (InstantiationException e) {
      throw new TaskException(TaskError.CLASSNOTCREATE, clazz.toString());
    }
    catch (IllegalAccessException e) {
      throw new TaskException(TaskError.CLASSNOACCESS, clazz.toString());
    }
  }

  public static void main(String[] args) {
    ServiceProvider provider = null;
    try {
      provider = ServiceProviderFactory.create(ServiceProvider.Type.WEBLOGIC);
      provider.connect("t3://leslie.vm.oracle.com:8005", "xelsysadm", "Welcome1".toCharArray());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (provider != null)
        provider.disconnect();
    }
  }
}

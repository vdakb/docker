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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Frontend Extension
    Subsystem   :   Identity Provider Discovery

    File        :   ContextLoader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ContextLoader.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.idp;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import oracle.as.jmx.framework.PortableMBeanFactory;

import bka.iam.platform.access.idp.mbean.ConfigurationService;
import bka.iam.platform.access.idp.mbean.ConfigurationServiceMXBean;

////////////////////////////////////////////////////////////////////////////////
// class ContextLoader
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Bootstrap listener to start up and shut down
 ** <code>Web Application Context</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ContextLoader implements ServletContextListener {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static ObjectName MBEAN;
  // the classes for the JMX client are part of a Web application, the JNDI name
  // for the Runtime MBeanServer is: java:comp/env/jmx/runtime
  // see https://docs.oracle.com/cd/E12839_01/web.1111/e13729/instmbeans.htm#JMXPG154
  static String     RUNTIME = "java:comp/env/jmx/runtime";

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      MBEAN = new ObjectName("bka.iam.platform:Name=config,Type=idp");
    }
    catch (MalformedObjectNameException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
	/**
	 ** Create a new <code>ContextLoader</code> that will create a web application
   ** context based on the "contextClass" and "contextConfigLocation" servlet
   ** context-params.
   ** See {@link ContextLoader} superclass documentation for details on default values for each.
	 ** <p>
   ** This constructor is typically used when declaring
   ** <code>ContextLoader</code> as a <code>listener</code> within
   ** {@code web.xml}, where a no-arg constructor is required.
   ** <p>
   ** The created application context will be registered into the ServletContext
   ** under the attribute name
   ** {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE} and
   ** the application context will be closed when the {@link #contextDestroyed}
   ** lifecycle method is invoked on this listener.
   **
   ** @see #contextInitialized(ServletContextEvent)
   ** @see #contextDestroyed(ServletContextEvent)
	 */
  public ContextLoader() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextInitialized (ServletContextListener)
  /**
   ** Initialize the root web application context.
   */
  @Override
  public void contextInitialized(final ServletContextEvent event) {
    try {
      final PortableMBeanFactory factory = new PortableMBeanFactory();
      final MBeanServer          runtime = factory.getMBeanServer();
      final ObjectName           name    = factory.translateObjectNameToGlobalNameSpace(MBEAN);
      if (!runtime.isRegistered(name)) {
        ConfigurationServiceMXBean mbean = new ConfigurationService();
        runtime.registerMBean(factory.createMBean(mbean, ConfigurationServiceMXBean.class), name);
        event.getServletContext().setAttribute("storage", ((ConfigurationService)mbean).storage());
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextDestroyed (ServletContextListener)
  /**
   ** Close the root web application context.
   */
  @Override
  public void contextDestroyed(final ServletContextEvent event) {
    try {
      final PortableMBeanFactory factory = new PortableMBeanFactory();
      final MBeanServer          runtime = factory.getMBeanServer();
      final ObjectName           name    = factory.translateObjectNameToGlobalNameSpace(MBEAN);
      if (runtime.isRegistered(name)) {
        runtime.unregisterMBean(name);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
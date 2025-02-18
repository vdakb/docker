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

    System      :   Foundation Shared Library
    Subsystem   :   Common shared naming facilities

    File        :   ServiceLocator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceLocator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.naming;

import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;

import javax.naming.NamingException;
import javax.naming.InitialContext;

import javax.rmi.PortableRemoteObject;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import javax.management.MBeanServer;

import javax.sql.DataSource;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class ServiceLocator
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Implements generic methods for retrieving elements from the JNDI namespace.
 ** This class uses cache mechanism to store the looked up JNDI objects for
 ** faster retrieval.
 ** <br>
 ** Funtionality of this bean includes:
 ** <ul>
 **   <li> Lookup object from JNDI tree
 **   <li> Cache looked up objects in a hashtable for faster subsequent retrievals
 ** </ul>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 ** <br>
 ** Implements the service locator design pattern.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class ServiceLocator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String         COMPONENT   = "java:comp/";
  public static final String         RESOURCE    = COMPONENT + "resource/";
  public static final String         ENV         = COMPONENT + "env/";
  public static final String         JMS         = ENV + "jms/";
  public static final String         JMX         = ENV + "jmx/";
  public static final String         JMX_RUNTIME = JMX + "runtime";
  public static final String         EJB         = ENV + "ejb/";
  public static final String         EJBLOCAL    = EJB + "local/";
  public static final String         JDBC        = "jdbc/";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static InitialContext      context     = null;

  // choose a hashtable to get a thread safe implementation
  private static Map<String, Object> cache       = new HashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (protected)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier protected prevents other classes using
   ** "new ServiceLocator()"
   */
  protected ServiceLocator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isJBoss
  /**
   ** Return <code>true</code> if the {@link InitialContext} driving this
   ** <code>ServiceLocator</code> is a JBoss.
   **
   ** @return                     <code>true</code> if the {@link InitialContext}
   **                             driving this <code>ServiceLocator</code> is a
   **                             JBoss; otherwise <code>false</code>.
   **
   ** @throws  LocatorException   if the <code>InitialContext</code> could not
   **                             be created at the first time this method is
   **                             invoked.
   */
  public static boolean isJBoss()
    throws LocatorException {

    boolean vendorIs = false;
    try {
      final Hashtable<? extends Object, ? extends Object> environment = getContext().getEnvironment();
      final String    factory     = (String)environment.get(InitialContext.URL_PKG_PREFIXES);
      if (!StringUtility.isEmpty(factory))
        vendorIs = factory.startsWith("org.jboss");
    }
    catch (NamingException e) {
      throw new LocatorException(LocatorError.CONTEXT_ENVIRONMENT, e);
    }
    return vendorIs;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContext
  /**
   ** Retrieves an environment value for a given name from Java ENC.
   **
   ** @return                     the context this locator use to lookup
   **                             JNDI resources.
   **
   ** @throws  LocatorException   if the <code>InitialContext</code> could not
   **                             be created at the first time this method is
   **                             invoked.
   */
  public static InitialContext getContext()
    throws LocatorException {

    return getInitialContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnvironment
  /**
   ** Retrieves an environment value for a given name from Java ENC.
   **
   ** @param  name                the name of the environment value to lookup.
   **
   ** @return  Object             an environment value.
   **
   ** @throws LocatorException    if <code>null</code> is passed for
   **                             <code>name</code> or <code>name</code> is
   **                             empty or if environment could not be found.
   */
  public static Object getEnvironment(final String name)
    throws LocatorException {

    return lookup(ENV + name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRemoteBean
  /**
   ** Retrieves the remote EJB bean interface for a given name from Java ENC.
   **
   ** @param  name               the name of enterperise java bean to lookup.
   ** @param  clazz              the imlementation to narrow
   **
   ** @return                    an EJB Home object.
   **                            Intended for remote interfaces.
   **
   ** @throws LocatorException   if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if object could not be found.
   */
  public static EJBHome getRemoteBean(final String name, final Class<?> clazz)
    throws LocatorException {

    Object object = lookup(EJB + name);
    if (null == object)
      return null;

    return (EJBHome)PortableRemoteObject.narrow(object, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRemoteBean
  /**
   ** Retrieves the remote EJB Home bean interface for a given name from Java
   ** ENC.
   ** <br>
   ** Note: the EJB MUST be a 3.0 Bean.
   **
   ** @param  name               the name of enterperise java bean to lookup.
   **
   ** @return                    an EJB object. Intended for remote interfaces.
   **
   ** @throws LocatorException   if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if object could not be found.
   */
  public static EJBHome getRemoteBean(final String name)
    throws LocatorException {

    return (EJBHome)lookup(EJB + name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocalBean
  /**
   ** Retrieves the local EJB bean interface for a given name from Java ENC.
   **
   ** @param  name               the name of enterperise java bean to lookup.
   **
   ** @return                    an EJB object. Intended for local interfaces.
   **
   ** @throws LocatorException   if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if object could not be found.
   */
  public static EJBLocalHome getLocalBean(final String name)
    throws LocatorException {

    return (EJBLocalHome)lookup(EJBLOCAL + name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionFactory
  /**
   ** Retrieves the connection factory interface for a given name from
   ** Java ENC.
   **
   ** @param  provider           the name of the JNDI location of the resource
   **                            provider.
   ** @param  factoryName        the name of enterperise connection factory to
   **                            lookup.
   **
   ** @return                    a connection factory object. Intended for local
   **                            interfaces.
   **
   ** @throws LocatorException   if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if object could not be found.
   */
  public static ConnectionFactory getConnectionFactory(final String provider, final String factoryName)
    throws LocatorException {

    return (ConnectionFactory)getMessagingObject(provider, factoryName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDestination
  /**
   ** Retrieves the destination interface (aka a queue) for a given name from
   ** Java ENC.
   **
   ** @param  provider           the name of the JNDI location of the resource
   **                            provider.
   ** @param  destinationName    the name of enterperise message destination to
   **                            lookup.
   **
   ** @return                    a message destination. Intended for local
   **                            interfaces.
   **
   ** @throws  LocatorException  if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if object could not be found.
   */
  public static Destination getDestination(final String provider, final String destinationName)
    throws LocatorException {

    return (Destination)getMessagingObject(provider, destinationName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMessagingObject
  /**
   ** Retrieves the messaging interface for a given name from Java ENC.
   **
   ** @param  provider           the name of the JNDI location of the resource
   **                            provider.
   ** @param  name               the name of enterperise queue to lookup.
   **
   ** @return                    a messaging interface object. Intended for
   **                            local interfaces.
   **
   ** @throws LocatorException   if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if object could not be found.
   */
  public static Object getMessagingObject(final String provider, final String name)
    throws LocatorException {

    if (null == provider || 0 == provider.length())
      throw new LocatorException(LocatorError.ARGUMENT_IS_NULL, "provider");

    if (null == name || 0 == name.length())
      throw new LocatorException(LocatorError.ARGUMENT_IS_NULL, "name");

    return lookup(RESOURCE + provider + "/" + name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDatasource
  /**
   ** Retrieves the jdbc datasource for a given name from Java ENC.
   **
   ** @param  name               the name of jdbc datasource to lookup.
   **
   ** @return                    a {@link DataSource} object.
   **
   ** @throws LocatorException   if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if datasource could not be found.
   */
  public static DataSource getDatasource(final String name)
    throws LocatorException {

    return (DataSource)lookup(JDBC + name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRuntimeServer
  /**
   ** Retrieves the JMX Runtime Server for a given name from Java ENC.
   **
   ** @return                    a {@link MBeanServer} object.
   **
   ** @throws LocatorException   if JMX Runtime Server could not be found.
   */
  public static MBeanServer getRuntimeServer()
    throws LocatorException {

    return (MBeanServer)lookup(JMX_RUNTIME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Retrieves an object for a given name from Java ENC.
   **
   ** @param   name              the name must contain the location key.
   **
   ** @return  Object            an object.
   **
   ** @throws  LocatorException  if <code>null</code> is passed for
   **                            <code>name</code> or <code>name</code> is
   **                            empty or if object could not be found.
   */
  public static Object lookup(final String name)
    throws LocatorException {

    if (name == null)
      throw new LocatorException(LocatorError.ARGUMENT_IS_NULL, "name");

    if (name.length() == 0)
      throw new LocatorException(LocatorError.ARGUMENT_BAD_VALUE, "name");

    if (cache.containsKey(name))
      return cache.get(name);

    try {
      Object object = getContext().lookup(name);
      synchronized (cache) {
        cache.put(name, object);
      }
      return object;
    }
    catch (NamingException e) {
      throw new LocatorException(LocatorError.OBJECT_LOOKUP, name, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInitialContext
  /**
   ** Retrieves an environment value for a given name from Java ENC.
   **
   ** @return                     the context this locator use to lookup
   **                             JNDI resources.
   **
   ** @throws  LocatorException   if the <code>InitialContext</code> could not
   **                             be created at the first time this method is
   **                             invoked.
   */
  private static InitialContext getInitialContext()
    throws LocatorException {

    if (context == null)
      try {
        context = new InitialContext();
      }
      catch (NamingException e) {
        throw new LocatorException(LocatorError.CONTEXT_INITIALIZE, e);
      }

    return context;
  }
}
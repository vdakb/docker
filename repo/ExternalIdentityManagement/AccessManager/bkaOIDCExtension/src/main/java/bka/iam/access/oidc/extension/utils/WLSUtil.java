/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   WLSUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    WLSUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-07  TSebo     First release version
*/
package bka.iam.access.oidc.extension.utils;

import java.util.Map;
import java.util.HashMap;

import java.util.logging.Logger;

import javax.sql.DataSource;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.AttributeNotFoundException;
import javax.management.MalformedObjectNameException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import oracle.security.jps.JpsContext;
import oracle.security.jps.JpsContextFactory;

import oracle.security.jps.service.credstore.Credential;
import oracle.security.jps.service.credstore.CredentialMap;
import oracle.security.jps.service.credstore.CredentialStore;
import oracle.security.jps.service.credstore.PasswordCredential;

////////////////////////////////////////////////////////////////////////////////
// class WLSUtil
// ~~~~~ ~~~~~~~
/**
 ** WebLogic utility class
 ** <br>
 ** It return AdminServerURL, ManagedServerURL and allow access to the WebLogic
 ** Credentials
 ** <p>
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WLSUtil {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  /**
   ** Class name captured for logging purpose
   */
  private static final String   CLASS          = WLSUtil.class.getName();
  /**
   ** Logger created based on the class name
   */
  private static final Logger   LOGGER         = Logger.getLogger(CLASS);
  /**
   ** MBean Server name
   */
  private static String         MBEAN_SERVER  = "java:comp/env/jmx/runtime";
  /**
   ** Runtim Service MBean name
   */
  private static String         MBEAN_RUNTIME = "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adminServerURL
  /**
   ** Returns the Admin Server URL.
   **
   ** @return                    the Admin Server URL.
   **/
  public static String adminServerURL() {
    final String method = "adminsServerURL";
    LOGGER.entering(CLASS, method);
    String adminServerURL = null;
    Context ctx = null;
    try {
      ctx = new InitialContext();
      MBeanServer mBeanServer = (MBeanServer) ctx.lookup(MBEAN_SERVER);
      ObjectName serviceObjectName = new ObjectName(MBEAN_RUNTIME);
      String serverName = (String) mBeanServer.getAttribute(serviceObjectName, "ServerName");

      LOGGER.finest("ServerName is: " + serverName);

      ObjectName serverRuntime = new ObjectName("com.bea:Name=" + serverName + ",Type=ServerRuntime");

      final String adminServerHost  = (String) mBeanServer.getAttribute(serverRuntime, "AdminServerHost");
      final Integer adminServerPort = (Integer) mBeanServer.getAttribute(serverRuntime, "AdminServerListenPort");
      final Boolean isSecured       = (Boolean) mBeanServer.getAttribute(serverRuntime, "AdminServerListenPortSecure");

      adminServerURL = (isSecured.booleanValue() ? "https" : "http") + "://" + adminServerHost + ":" + adminServerPort;

      LOGGER.fine("AdminServerHost: "             + adminServerHost);
      LOGGER.fine("AdminServerListenPort: "       + adminServerPort);
      LOGGER.fine("AdminServerListenPortSecure: " + isSecured);
      LOGGER.fine("Admin Server HTTP URL: " + adminServerURL);
    }
    catch (NamingException e) {
      LOGGER.severe("NamingException: " + e);
    }
    catch (MalformedObjectNameException e) {
      LOGGER.severe("MalformedObjectNameException: " + e);
    }
    catch (AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException e) {
      LOGGER.severe("Unknow error: " + e);
    }
    finally {
      if (ctx != null) {
        try {
          ctx.close();
        }
        catch (NamingException e) {
          LOGGER.throwing(CLASS, method, e);
        }
      }
    }
    LOGGER.exiting(CLASS, method, adminServerURL);
    return adminServerURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointURL
  /**
   ** Return Internal Server URL.
   ** <br>
   ** It can be used in case standart REST API needs to be executed.
   **
   ** @param   path              the path segment to qualifiy the server URL
   **                            with.
   **
   ** @return                    the internal server URL where web application
   **                            is deployed.
   **/
  public static String endpointURL(final String path) {
    final String method = "endpointURL";
    LOGGER.entering(CLASS, method, path);
    final String endpoint = serverURL() + path;
    LOGGER.exiting(CLASS, method, endpoint);
    return endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverURL
  /**
   ** Return Internal Server URL.
   ** <br>
   ** It can be used in case standart REST API needs to be executed.
   **
   ** @return                    the internal server URL where web application
   **                            is deployed.
   **/
  public static String serverURL() {
    final String method = "serverURL";
    LOGGER.entering(CLASS, method);

    String defaultURL = null;
    Context ctx = null;
    try {
      ctx = new InitialContext();
      MBeanServer mBeanServer = (MBeanServer)ctx.lookup(MBEAN_SERVER);
      ObjectName  objectName  = new ObjectName(MBEAN_RUNTIME);
      String      serverName  = (String)mBeanServer.getAttribute(objectName, "ServerName");

      LOGGER.finest("ServerName is: " + serverName);
      ObjectName serverRuntime = new ObjectName("com.bea:Name=" + serverName + ",Type=ServerRuntime");

      defaultURL = (String)mBeanServer.getAttribute(serverRuntime, "DefaultURL");
      defaultURL = defaultURL.replaceFirst("t3", "http");
      LOGGER.fine("ServerHost: " + defaultURL);
    }
    catch (NamingException e) {
      LOGGER.severe("NamingException: " + e);
    }
    catch (MalformedObjectNameException e) {
      LOGGER.severe("MalformedObjectNameException: " + e);
    }
    catch (AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException e) {
      LOGGER.severe("Unknow error: " + e);
    }
    finally {
      if (ctx != null) {
        try {
          ctx.close();
        }
        catch (NamingException e) {
          LOGGER.throwing(CLASS, method, e);
        }
      }
    }
    LOGGER.exiting(CLASS, method, defaultURL);
    return defaultURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSource
  /**
   ** Retrieves the jdbc datasource for a given name from Java ENC.
   **
   ** @param  name               the name of jdbc datasource to lookup.
   **
   ** @return                    a {@link DataSource} object.
   **
   ** @throws NamingException    if a naming exception is encountered.
   */
  public static DataSource dataSource(final String name)
    throws NamingException {

    final String method = "dataSource";
    LOGGER.entering(CLASS, method);
    Context ctx = null;
    try {
      ctx = new InitialContext();
      return (DataSource)ctx.lookup(name);
    }
    catch (NamingException e) {
      LOGGER.throwing(CLASS, method, e);
      throw e;
    }
    finally {
      if (ctx != null) {
        try {
          ctx.close();
        }
        catch (NamingException e) {
          LOGGER.throwing(CLASS, method, e);
        }
      }
      LOGGER.exiting(CLASS, method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCredentialsFromCSF
  /**
   ** Fetches credentials from WebLogic Credential Store.
   ** On weblogic server folloiwng perminssion grant is needed <br>
   ** Code base: file:${oracle.deployed.app.dir}/oidc.extension.access.module${oracle.deployed.app.ext}
   ** grantPermission(codeBaseURL="file:${oracle.deployed.app.dir}/oidc.extension.access.module_12.2.1.4${oracle.deployed.app.ext}",permClass="oracle.security.jps.service.credstore.CredentialAccessPermission",permTarget="context=SYSTEM,mapName=OAM_CONFIG,keyName=*",permActions="read")
   ** @param map   Name of map where key is stored
   ** @param key   Name of key
   ** @return  HashMap of connection information including
   **          <br>username<br>password<br>description
   **/
  public static Map<String, String> getCredentialsFromCSF(String map, String key) {
    final String method = "getCredentialsFromCSF";
    LOGGER.entering(CLASS, method, new String[]{map, key});
    String userName    = "";
    String password    = "";
    String description = "";
    Map<String, String> credentials = null;
    try {
      final JpsContext ctx = JpsContextFactory.getContextFactory().getContext();
      LOGGER.finest("Context:  " + ctx.getName());
      final CredentialStore cs = ctx.getServiceInstance(CredentialStore.class);
      LOGGER.finest("Credential Store: " + cs.getName());
      final CredentialMap cmap = cs.getCredentialMap(map);
      LOGGER.finest("Credential Map: " + cmap);
      final Credential cred = cmap.getCredential(key);
      LOGGER.finest("Gathered Credential");
      if (cred instanceof PasswordCredential) {
        final PasswordCredential pcred = (PasswordCredential) cred;
        char[] p = pcred.getPassword();
        userName = pcred.getName();
        password = new String(p);
        description = pcred.getDescription();
      }
      credentials = new HashMap<String, String>();
      credentials.put("username",    userName);
      credentials.put("password",    password);
      credentials.put("description", description);
    }
    catch (Exception e) {
      LOGGER.throwing(CLASS, method, e);
    }
    LOGGER.exiting(CLASS, method);
    return credentials;
  }
}
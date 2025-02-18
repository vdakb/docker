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
    Subsystem   :   Custom Rest Service

    File        :   WLSUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    WLSUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-07  TSebo     First release version
*/
package bka.iam.identity.scim.extension.utils;

import java.util.Set;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import oracle.as.jmx.framework.PortableMBeanFactory;

////////////////////////////////////////////////////////////////////////////////
// class WLSUtil
// ~~~~~ ~~~~~~~~~~~~
/**
 ** WebLogic utility class
 ** <br>
 ** It returns ManagedServerURL
 ** <p>
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WLSUtil {

    //////////////////////////////////////////////////////////////////////////////
    // static final attributes
    //////////////////////////////////////////////////////////////////////////////

    /**
     ** Logger created based on the class name
     */
    private static Logger logger = Logger.getLogger(WLSUtil.class.getName());

    /**
     ** MBean Server name
     */
    private static String MBEAN_SERVER_NAME = "java:comp/env/jmx/runtime";

    /**
     ** Runtim Service MBean name
     */
    private static String RUNTIME_SERVICE_MBEAN =
        "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean";

    //////////////////////////////////////////////////////////////////////////////
    // Method:   getServerURL

    /**
     ** Return Internal Server URL. It can be used in case standart REST API needs to be executed.
     ** @return internal server URL where web application is deployed
     **/
    public static String getServerURL() {

        String method = "getAdminsServerURL";
        logger.entering(WLSUtil.class.getName(), method);

        String defaultURL = null;
        InitialContext ctx = null;

        try {
            ctx = new InitialContext();
            MBeanServer mBeanServer = (MBeanServer) ctx.lookup(MBEAN_SERVER_NAME);
            ObjectName serviceObjectName = new ObjectName(RUNTIME_SERVICE_MBEAN);
            String serverName = (String) mBeanServer.getAttribute(serviceObjectName, "ServerName");

            logger.finest("ServerName is: " + serverName);

            ObjectName serverRuntime = new ObjectName("com.bea:Name=" + serverName + ",Type=ServerRuntime");

            defaultURL = (String) mBeanServer.getAttribute(serverRuntime, "DefaultURL");
            defaultURL = defaultURL.replaceFirst("t3", "http");

            logger.fine("ServerHost: " + defaultURL);


        } catch (NamingException e) {
            logger.severe("NamingException: " + e);
        } catch (MalformedObjectNameException e) {
            logger.severe("MalformedObjectNameException: " + e);
        } catch (AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException e) {
            logger.severe("Unknow error: " + e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    logger.warning("Unable to close InitialContext() " + e);
                }
            }
        }
        logger.exiting(WLSUtil.class.getName(), method, defaultURL);
        return defaultURL;
    }

    public static String getOIMUrl() {
        String oimUrl = null;
        try {
            PortableMBeanFactory portableMBeanFactory = new PortableMBeanFactory();
            MBeanServerConnection connection = portableMBeanFactory.getPrivilegedMBeanServer();

            Set<ObjectName> oimObjSet =
                connection.queryNames(new ObjectName("oracle.iam:*,type=XMLConfig.DiscoveryConfig"), null);
            for (ObjectName objName : oimObjSet) {
                oimUrl = connection.getAttribute(objName, "OimExternalFrontEndURL").toString();
                if (null != oimUrl) {
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return oimUrl;

    }


}

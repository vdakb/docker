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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   OAuth Registration

    File        :   OIMService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.service;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Collections;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;

import javax.sql.DataSource;

import oracle.iam.platform.OIMClient;
import oracle.iam.platform.Platform;

import weblogic.security.auth.login.UsernamePasswordLoginModule;

////////////////////////////////////////////////////////////////////////////////
 // class OIMService
 // ~~~~~ ~~~~~~~~
 /**
  ** OIM Service class, which allow to call OIM API from remote machine and also from server.
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class OIMService {
	
	private static final String className = OIMService.class.getName();
	private static Logger       logger = Logger.getLogger(className);
	
	private static String APPSERVER_TYPE_WLS = "wls";
	
	private static OIMClient OIM_CLIENT = null;
	
		
	/**
	 ** Connect and login to OIM from local client
	 ** @param url URL to OIM managed server e.g. t3://OIM_HOSTNAME:OIM_PORT
	 ** @param username OIM user name (xelsysadm)
	 ** @param password OIM user password
	 ** @throws LoginException
	 */
	public static void initialize(String url, 
														 String username, 
														 String password) throws LoginException {
	
		String methodName = "initialize";
		logger.entering(className,methodName);
	
	  System.setProperty("APPSERVER_TYPE",APPSERVER_TYPE_WLS);
	  
		// Line belowe replace need of loading config\auth.conf 
	  Configuration.setConfiguration(new Configuration() {
	      @Override
	      public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
	          return new AppConfigurationEntry[]{new AppConfigurationEntry(UsernamePasswordLoginModule.class.getName(), AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, Collections.singletonMap("debug", "true"))};
	      }
	  });
	  
	  Hashtable<String,String> env = new Hashtable<String,String>();
	  env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, OIMClient.WLS_CONTEXT_FACTORY);
	  env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, url);
	  OIMClient oimClient = new OIMClient(env);
	  logger.finest("Connection to "+url+" ......");
	  oimClient.login(username, password.toCharArray());
	  logger.fine("Connected to "+url+" ......");

    OIMService.OIM_CLIENT = oimClient;	
		
	  logger.exiting(className,methodName);
	}
	
	/**
   ** Get OIM Service API
   ** @param serviceClass OIM Service API class
   ** @return OIM Service API
   */
	public static <T> T getService(Class<T> serviceClass){
	  String methodName = "getService";
	  logger.entering(className,methodName);
		
		if(OIMService.OIM_CLIENT == null)
      OIMService.OIM_CLIENT = new OIMClient();
		
	  logger.exiting(className,methodName);
		return OIM_CLIENT.getService(serviceClass);
	}
	
		
	/**
	 ** Get OIM operational DB connection
	 ** @return OIM Operational DB connection
	 */
	public static DataSource getOperationalDS(){
	  String methodName = "getOperationalDS";
	  logger.entering(className,methodName);
	  
		DataSource ds = Platform.getOperationalDS();
		
	  logger.exiting(className,methodName);
		return ds;
	}
	
	/**
	 ** Get OIM operational DB connection from local client
	 ** @param url URL to OIM managed server e.g. t3://OIM_HOSTNAME:OIM_PORT
	 ** @return OIM operational DB connection
	 ** @throws NamingException
	 ** @throws SQLException
	 */
	public static DataSource getOperationalDS(String url) throws NamingException, SQLException {
	  String methodName = "getOperationalDS";
	  logger.entering(className,methodName);
		
	  Hashtable env = new Hashtable();
	  env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, OIMClient.WLS_CONTEXT_FACTORY);
	  env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, url);
		
		Context context = new InitialContext(env);
	  DataSource ds = (DataSource) context.lookup("jdbc/operationsDB");
		
	  logger.exiting(className,methodName);
		return ds;
	}
	
	/**
	 ** Connecto to external DB. WebLogic Data Source needs to be defined with jdbcName
	 ** @param jdbcName JDBC name defined in the WebLogic DataSources e.g. jdbc/XXOIG
	 ** @return
	 ** @throws NamingException
	 ** @throws SQLException
	 */
	public static Connection getExternalConnection(String jdbcName) throws NamingException, SQLException {
	  String methodName = "getConnection";
	  logger.entering(className,methodName);
		
	  Connection con = null;
	  Context context = new InitialContext();
	  DataSource ds = (DataSource) context.lookup(jdbcName);
	  logger.fine("Datasource "+jdbcName +" exists in weblogic configuration");
	  if(ds != null){
	    con = ds.getConnection();
	    logger.fine("Connection to datasource "+jdbcName +" was established");
	  }
	  
	  logger.exiting(className,methodName);
	  return con;
	}
	
	/**
	 ** Connecto to external DB from local client. WebLogic Data Source needs to be defined with jdbcName
	 ** @param url URL to OIM managed server e.g. t3://OIM_HOSTNAME:OIM_PORT
	 ** @param jdbcName JDBC name defined in the WebLogic DataSources e.g. jdbc/XXOIG
	 ** @return DB Connection
	 ** @throws NamingException
	 ** @throws SQLException
	 */
	public static Connection getExternalConnection(String url, String jdbcName) throws NamingException, SQLException {
		String methodName = "getConnection";
		logger.entering(className,methodName);
		
		Connection con = null;
		
	  Hashtable env = new Hashtable();
	  env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, OIMClient.WLS_CONTEXT_FACTORY);
	  env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, url);
	  Context context = new InitialContext(env);
		
		DataSource ds = (DataSource) context.lookup(jdbcName);
		logger.fine("Datasource "+jdbcName +" exists in weblogic configuration");
		if(ds != null){
			con = ds.getConnection();
			logger.fine("Connection to datasource "+jdbcName +" was established");
		}
		
		logger.exiting(className,methodName);
		return con;
	}
	
	/**
	 ** Close DB connection
	 ** @param con DB Connection
	 */
	public static void close(Connection con){
	  String methodName = "close";
	  logger.entering(className,methodName);
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
			  logger.warning("Connection can't be closed:  "+e);
			  logger.throwing(className,methodName,e);
			}
		}	
	  logger.exiting(className,methodName);
	}
}


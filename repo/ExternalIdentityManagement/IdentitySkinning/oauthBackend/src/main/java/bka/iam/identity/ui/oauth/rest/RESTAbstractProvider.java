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

    File        :   RESTAbstractProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    Register.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-05-02  Tomas Sebo    First release version
*/
package bka.iam.identity.ui.oauth.rest;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

import java.util.logging.Logger;

import weblogic.jaxrs.api.client.Client;

////////////////////////////////////////////////////////////////////////////////
 // class RESTAbstractProvider
 // ~~~~~ ~~~~~~~~
 /**
  ** Abstract Rest Provider with generic method related to REST connection
  **
  ** @author  tomas.t.sebo@oracle.com
  ** @version 1.0.0.0
  ** @since   1.0.0.0
  */
public class RESTAbstractProvider {
  
  private static final String className = RESTAbstractProvider.class.getName();
  private static Logger       logger = Logger.getLogger(className);
  
  private static final int CONNECTION_TIMEOUT=5000; //5 seconds
   
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getWebResource
  /**
   ** Get Web Resource
   ** @param rootURL Admin Root Server URL e.g. https://oam.example.com:7001
   ** @return WebResource object
   **/
  protected WebResource getWebResource(String rootURL,String username, String password) {
    String methodName = "getWebResource";
    logger.entering(className,methodName);
    
    // Create REST Client
    Client c = Client.create();
    c.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
    c.getProperties().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
    c.setConnectTimeout(CONNECTION_TIMEOUT);

    // Create REST Resource
    WebResource resource = c.resource(rootURL);

    // Set Basic Authorization header
    c.addFilter(new HTTPBasicAuthFilter(username, password));
    
    logger.exiting(className,methodName);
    return resource;
  }
  
  
  
  
  
}

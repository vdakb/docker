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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   SAP R3 System

    File        :   Ping.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Cengiz.Tuztas@oracle.com

    Purpose     :   This file implements the class
                    Ping.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-06-10  CTuztas    First release version
*/

package oracle.iam.identity.sap.diagnostic;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import com.sap.conn.jco.JCoDestination;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.sap.control.Feature;
import oracle.iam.identity.sap.control.Resource;
import oracle.iam.identity.sap.control.DestinationProvider;

public class Ping {

	private static JCoDestination     destination;

	private final Connection          connection   = new Connection();

  private final Map<String, String> resource;

  public Ping(final Map<String, String> resource) {
    // ensure inheritance
    super();

    // simulate the creation of an IT Resource by creating the appropriate mapping
    this.resource = resource;
  }

  /**
   **
   */
  public void open() {
    final Properties environment = new Properties();
    environment.setProperty(DestinationProvider.JCO_ASHOST, this.resource.get(Resource.APPLICATION_SERVER_HOST));
    environment.setProperty(DestinationProvider.JCO_SYSNR,  this.resource.get(Resource.SYSTEM_NUMBER));
    environment.setProperty(DestinationProvider.JCO_CLIENT, this.resource.get(Resource.CLIENT_LOGON));
    environment.setProperty(DestinationProvider.JCO_USER,   this.resource.get(Resource.PRINCIPAL_NAME));
    environment.setProperty(DestinationProvider.JCO_PASSWD, this.resource.get(Resource.PRINCIPAL_PASSWORD));
    environment.setProperty(DestinationProvider.JCO_LANG,   this.resource.get(Resource.SYSTEM_LANGUAGE));

    if ((this.resource.get(Resource.SECURE_SOCKET)).equalsIgnoreCase("true")) {
      environment.setProperty(DestinationProvider.JCO_SNC_MODE,        "1");
      environment.setProperty(DestinationProvider.JCO_SNC_PARTNERNAME, this.resource.get(Feature.SECURE_NAME_REMOTE));
      environment.setProperty(DestinationProvider.JCO_SNC_QOP,         this.resource.get(Feature.SECURE_LEVEL));
      environment.setProperty(DestinationProvider.JCO_SNC_MYNAME,      this.resource.get(Feature.SECURE_NAME_LOCAL));
      environment.setProperty(DestinationProvider.JCO_SNC_LIBRARY,     this.resource.get(Feature.SECURE_LIBRARY_PATH));
    }
    else {
      environment.setProperty(DestinationProvider.JCO_SNC_MODE,        "0");
    }
    if (!StringUtility.isEmpty(this.resource.get(Resource.SYSTEM_NAME)))
      environment.setProperty(DestinationProvider.JCO_R3NAME, this.resource.get(Resource.SYSTEM_NAME));

    if (!StringUtility.isEmpty(this.resource.get(Resource.MESSAGE_SERVER_HOST)))
      environment.setProperty(DestinationProvider.JCO_MSSERV, this.resource.get(Resource.MESSAGE_SERVER_HOST));

    // For Logon Groups
    if (!StringUtility.isEmpty(this.resource.get(Resource.APPLICATION_SERVER_GROUP)))
      environment.setProperty(DestinationProvider.JCO_GROUP, this.resource.get(Resource.APPLICATION_SERVER_GROUP));

    if (((this.resource.get(Resource.SYSTEM_LANGUAGE)).equalsIgnoreCase("ja")) && ((this.resource.get(Resource.SECURE_SOCKET)).equalsIgnoreCase("false")))
      environment.setProperty(DestinationProvider.JCO_CODEPAGE, "8000");

    if (this.resource.get(Feature.TRACE_ENABLED).equalsIgnoreCase("true")) {
      environment.setProperty(DestinationProvider.JCO_TRACE,        "1");
      environment.setProperty("jco.trace_level",                        this.resource.get(Feature.TRACE_LEVEL));
    }
    else {
      environment.setProperty(DestinationProvider.JCO_TRACE,        "0");
      environment.setProperty("jco.trace_level",                        "0");
    }

    // passing the host as the destination and destination's connection
    // properties to the SAP target
    Connection.provider.addDestination(this.resource.get(Resource.APPLICATION_SERVER_HOST), environment);
    try {
      destination = this.connection.open(this.resource.get(Resource.APPLICATION_SERVER_HOST));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ping() {
    if (destination != null)
      try {
        destination.ping();
      }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   **
   */
  public void close() {
    if (destination != null)
      try {
        this.connection.close(destination);
      }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    final Map<String, String> endpoint = new HashMap<String, String>();
    endpoint.put(Resource.MESSAGE_SERVER_HOST,      "sap-f1s.fs01.vwf.vwfs-ad");
    endpoint.put(Resource.APPLICATION_SERVER_HOST,  "sap-f1s.fs01.vwf.vwfs-ad");
    endpoint.put(Resource.APPLICATION_SERVER_GROUP, "public");
    endpoint.put(Resource.SYSTEM_NAME,              "F1S");
    endpoint.put(Resource.SYSTEM_LANGUAGE,          "EN");
    endpoint.put(Resource.SYSTEM_TIMEZONE,          "GMT+01:00");
    endpoint.put(Resource.SYSTEM_NUMBER,            "05");
    endpoint.put(Resource.CLIENT_LOGON,             "101");
    endpoint.put(Resource.PRINCIPAL_NAME,           "AIM_USER");
    endpoint.put(Resource.PRINCIPAL_PASSWORD,       "Zgd.umw.oim.2013");
    endpoint.put(Resource.SECURE_SOCKET,            "false");
    endpoint.put(Feature.TRACE_ENABLED,            "true");
    endpoint.put(Feature.TRACE_LEVEL,              "10");
    final Ping ping = new Ping(endpoint);
    ping.open();
    ping.ping();
    ping.close();
  }
}

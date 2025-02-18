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

    File        :   Connection.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Cengiz.Tuztas@oracle.com

    Purpose     :   This file implements the class
                    Connection.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-06-10  CTuztas    First release version
*/

package oracle.iam.identity.sap.diagnostic;

import java.util.Map;
import java.util.Properties;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunctionTemplate;

import com.sap.conn.jco.ext.Environment;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Connection
// ~~~~~ ~~~~~~~~~~
public class Connection {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static JCoDestination connection;
  protected static Provider       provider    = Provider.instance();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Properties            properties;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Connection</code>
   */
  public Connection() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDestination
  /**
   ** Creates property from IT Resource attributes required to connect to the
   ** respective SAP system.
   **
   ** @param  resource           contains the IT Resource values mapping in
   **                            {@link Map} with code key as JCO parameter and
   **                            decode as IT Resource attribute name.
   ** @param  parameter          provides SAP connection values as a {@link Map}
   **                            with code key as JCO parameter and decode as
   **                            value entered for the IT Resource parameter.
   **
   ** @return                    the connection to the SAP system wrapped in a
   **                            {@link JCoDestination}.
   */
  public JCoDestination createDestination(final Map<String, String> resource, final Map<String, String> parameter) {
    int clientPropLength = Constant.clientProperties.length;

    final String destination = parameter.get("App server host");
    for (int i = 0; i < clientPropLength; i++) {
      final String attribute = resource.get(Constant.clientProperties[i]);
      if (StringUtility.isEmpty(attribute))
        continue;
      final String value = parameter.get(attribute);
      if (StringUtility.isEmpty(value))
        continue;
      this.properties.setProperty(Constant.clientProperties[i], value);
    }
    provider.addDestination(destination, this.properties);
    try {
      connection = open(destination);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
		return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** Registers the properties provided and starts the connection to the SAP
   ** system,
   **
   ** @param  destination        the host anme of the SAP system to connect.
   **
   ** @return                    the connection wrapped as a
   **                            {@link JCoDestination}.
   */
  public JCoDestination open(final String destination)
    throws Exception {

    JCoDestination connection = null;
    try {
      if (!Provider.register) {
        try {
          Environment.registerDestinationDataProvider(provider);
        }
        catch (IllegalStateException e) {
          e.printStackTrace();
        }
        Provider.register = true;
      }
      connection = JCoDestinationManager.getDestination(destination);
      JCoContext.begin(connection);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close the connection wirh the SAP system represented by the specified
   ** {@link JCoDestination}.
   **
   ** @param  connection         the destination handle to close.
   */
  public JCoDestination close(final JCoDestination connection)
    throws Exception {
    if (connection != null)
      try {
        JCoContext.end(connection);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jcoFunction
  /**
   ** Returnss the JCoFunction after executing a BAPI
   **
   ** @param  destination        the {@link JCoDestination} reference holding
   **                            connection details.
   ** @param  functionName       the name of the BAPI to be executed.
   **                            For example: BAPI_USER_GETDETAILS.
   **
   ** @return                    the {@link JCoFunction} for the associated BAPI
   **                            Name and {@link JCoDestination} having the
   **                            connection properties
   */
  public JCoFunction jcoFunction(final JCoDestination destination, final String functionName) {
    // Connect to repository and return the function according to the BAPI name
    // passed
    JCoFunction function = null;
    try {
      JCoRepository       repository = destination.getRepository();
      JCoFunctionTemplate template   = repository.getFunctionTemplate(functionName);
      function = template.getFunction();
    }
    catch (JCoException e) {
      e.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return function;
  }
}
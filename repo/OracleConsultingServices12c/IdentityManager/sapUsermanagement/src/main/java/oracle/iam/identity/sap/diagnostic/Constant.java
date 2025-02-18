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

    File        :   Constant.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Cengiz.Tuztas@oracle.com

    Purpose     :   This file implements the interface
                    Constant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-06-10  CTuztas    First release version
*/

package oracle.iam.identity.sap.diagnostic;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.ServerDataProvider;

////////////////////////////////////////////////////////////////////////////////
// interface Constant
// ~~~~~~~~~ ~~~~~~~~
public interface Constant {
  /**
   ** the supported properties for MiddlewareJavaRfc, i.e. the default RFC
   ** layer, in alphabetical order
   */
  static final String[] clientProperties = {
    DestinationDataProvider.JCO_ASHOST            // "jco.client.ashost"
  , DestinationDataProvider.JCO_ALIAS_USER        // "jco.client.alias_user"
  , DestinationDataProvider.JCO_CLIENT            // "jco.client.client"
  , DestinationDataProvider.JCO_CODEPAGE          // "jco.client.codepage"
  , DestinationDataProvider.JCO_CPIC_TRACE        // "jco.client.cpic_trace"
  , DestinationDataProvider.JCO_DEST              // "jco.client.dest"
  , DestinationDataProvider.JCO_EXPIRATION_PERIOD // "jco.destination.expiration_check_period"
  , DestinationDataProvider.JCO_EXPIRATION_TIME   // "jco.destination.expiration_time"
  , DestinationDataProvider.JCO_GETSSO2           // "jco.client.getsso2"
  , DestinationDataProvider.JCO_GROUP             // "jco.client.group"
  , DestinationDataProvider.JCO_GWHOST            // "jco.client.gwhost"
  , DestinationDataProvider.JCO_GWSERV            // "jco.client.gwserv"
  , DestinationDataProvider.JCO_LANG              // "jco.client.lang"
  , DestinationDataProvider.JCO_LCHECK            // "jco.client.lcheck"
  , DestinationDataProvider.JCO_MAX_GET_TIME      // "jco.destination.max_get_client_time"
  , DestinationDataProvider.JCO_MSHOST            // "jco.client.mshost"
  , DestinationDataProvider.JCO_MSSERV            // "jco.client.msserv"
  , DestinationDataProvider.JCO_MYSAPSSO2         // "jco.client.mysapsso2"
  , DestinationDataProvider.JCO_PASSWD            // "jco.client.passwd"
  , DestinationDataProvider.JCO_PCS               // "jco.client.pcs"
  , DestinationDataProvider.JCO_R3NAME            // "jco.client.r3name"
  , DestinationDataProvider.JCO_SAPROUTER         // "jco.client.saprouter"
  , DestinationDataProvider.JCO_SNC_LIBRARY       // "jco.client.snc_lib"
  , DestinationDataProvider.JCO_SNC_MODE          // "jco.client.snc_mode"
  , DestinationDataProvider.JCO_SNC_MYNAME        // "jco.client.snc_myname"
  , DestinationDataProvider.JCO_SNC_PARTNERNAME   // "jco.client.snc_partnername"
  , DestinationDataProvider.JCO_SNC_QOP           // "jco.client.snc_qop"
  , DestinationDataProvider.JCO_SYSNR             // "jco.client.sysnr"
  , DestinationDataProvider.JCO_TPHOST            // "jco.client.tphost"
  , DestinationDataProvider.JCO_TPNAME            // "jco.client.tpname"
  , DestinationDataProvider.JCO_TRACE             // "jco.client.trace"
  , DestinationDataProvider.JCO_TYPE              // "jco.client.type"
  , DestinationDataProvider.JCO_USER              // "jco.client.user"
  , DestinationDataProvider.JCO_X509CERT          // "jco.client.x509cert"
  , DestinationDataProvider.JCO_PEAK_LIMIT        // "jco.destination.peak_limit"
  , DestinationDataProvider.JCO_POOL_CAPACITY     // "jco.destination.pool_capacity"
  , DestinationDataProvider.JCO_REPOSITORY_DEST   // "jco.destination.repository_destination"
  , DestinationDataProvider.JCO_REPOSITORY_PASSWD // "jco.destination.repository.passwd"
  , DestinationDataProvider.JCO_REPOSITORY_SNC    // "jco.destination.repository.snc_mode"
  , DestinationDataProvider.JCO_REPOSITORY_DEST   // "jco.destination.repository.user"
  };

  /**
   ** the supported properties for MiddlewareJavaRfc, i.e. the default RFC
   ** layer, in alphabetical order
   */
  static final String[] serverProperties = {
    ServerDataProvider.JCO_CONNECTION_COUNT       // "jco.server.connection_count"
  , ServerDataProvider.JCO_GWHOST                 // "jco.server.gwhost"
  , ServerDataProvider.JCO_GWSERV                 // "jco.server.gwserv"
  , ServerDataProvider.JCO_MAX_STARTUP_DELAY      // "jco.server.max_startup_delay"
  , ServerDataProvider.JCO_PROGID                 // "jco.server.progid"
  , "jco.server.repository_destination"
  , ServerDataProvider.JCO_SAPROUTER              // "jco.server.saprouter"
  , ServerDataProvider.JCO_SNC_LIBRARY            // "jco.server.snc_lib"
  , ServerDataProvider.JCO_SNC_MODE               // "jco.server.snc_mode"
  , ServerDataProvider.JCO_SNC_MYNAME             // "jco.server.snc_myname"
  , ServerDataProvider.JCO_SNC_QOP                // "jco.server.snc_qop"
  , ServerDataProvider.JCO_TRACE                  //  "jco.server.trace"
  , "jco.server.unicode"
  };

	static final String[] mandatoryAttributes = {
    "Client logon"
  , "Language"
  , "Password"
  , "SNC mode"
  , "User logon"
  };

	static final String[] securityAttributtes = {
    "SNC lib"
  , "SNC my name"
  , "SNC partner name"
  , "SNC qop"
  };
}
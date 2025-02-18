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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic WebService Connector

    File        :   Connector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Connector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.resource;

////////////////////////////////////////////////////////////////////////////////
// interface Connector
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Connector {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PREFIX                        = "REST-";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Endpoint
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Declares global visible resource identifier used for target endpoint
   ** information purpose.
   */
  public interface Endpoint {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String SERVICE_HOST_LABEL          = PREFIX + "00011";
    static final String SERVICE_HOST_HINT           = PREFIX + "00012";
    static final String SERVICE_PORT_LABEL          = PREFIX + "00013";
    static final String SERVICE_PORT_HINT           = PREFIX + "00014";
    static final String SERVER_HOST_LABEL           = PREFIX + "00015";
    static final String SERVER_HOST_HINT            = PREFIX + "00016";
    static final String SERVER_PORT_LABEL           = PREFIX + "00017";
    static final String SERVER_PORT_HINT            = PREFIX + "00018";
    static final String SECURE_LABEL                = PREFIX + "00019";
    static final String SECURE_HINT                 = PREFIX + "00020";
    static final String CONTEXT_LABEL               = PREFIX + "00021";
    static final String CONTEXT_HINT                = PREFIX + "00022";
    static final String TYPE_CONTENT_LABEL          = PREFIX + "00023";
    static final String TYPE_CONTENT_HINT           = PREFIX + "00024";
    static final String TYPE_ACCEPT_LABEL           = PREFIX + "00025";
    static final String TYPE_ACCEPT_HINT            = PREFIX + "00026";
    static final String AUTHORIZATION_SERVER_LABEL  = PREFIX + "00027";
    static final String AUTHORIZATION_SERVER_HINT   = PREFIX + "00028";
    static final String AUTHENTICATION_SCHEME_LABEL = PREFIX + "00029";
    static final String AUTHENTICATION_SCHEME_HINT  = PREFIX + "00030";
    static final String CLIENT_IDENTIFIER_LABEL     = PREFIX + "00031";
    static final String CLIENT_IDENTIFIER_HINT      = PREFIX + "00032";
    static final String CLIENT_SECRET_LABEL         = PREFIX + "00033";
    static final String CLIENT_SECRET_HINT          = PREFIX + "00034";
    static final String PRINCIPAL_USERNAME_LABEL    = PREFIX + "00035";
    static final String PRINCIPAL_USERNAME_HINT     = PREFIX + "00036";
    static final String PRINCIPAL_PASSWORD_LABEL    = PREFIX + "00037";
    static final String PRINCIPAL_PASSWORD_HINT     = PREFIX + "00038";
    static final String RESOURCE_OWNERNAME_LABEL    = PREFIX + "00039";
    static final String RESOURCE_OWNERNAME_HINT     = PREFIX + "00040";
    static final String RESOURCE_CREDENTIAL_LABEL   = PREFIX + "00041";
    static final String RESOURCE_CREDENTIAL_HINT    = PREFIX + "00042";
    static final String COUNTRY_LABEL               = PREFIX + "00043";
    static final String COUNTRY_HINT                = PREFIX + "00044";
    static final String LANGUAGE_LABEL              = PREFIX + "00045";
    static final String LANGUAGE_HINT               = PREFIX + "00046";
    static final String TIMEZONE_LABEL              = PREFIX + "00047";
    static final String TIMEZONE_HINT               = PREFIX + "00048";
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Feature
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** Declares global visible resource identifier used for extended target
   ** endpoint information purpose.
   */
  public interface Feature {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String FETCH_SCHEMA_LABEL          = PREFIX + "00101";
    static final String FETCH_SCHEMA_HINT           = PREFIX + "00102";
    static final String RFC_9110_LABEL              = PREFIX + "00103";
    static final String RFC_9110_HINT               = PREFIX + "00104";
    static final String ENTERPRICE_FEATURE_LABEL    = PREFIX + "00105";
    static final String ENTERPRICE_FEATURE_HINT     = PREFIX + "00106";
    static final String CONTEXT_SCHEMA_LABEL        = PREFIX + "00107";
    static final String CONTEXT_SCHEMA_HINT         = PREFIX + "00108";
    static final String CONTEXT_RESOURCE_LABEL      = PREFIX + "00109";
    static final String CONTEXT_RESOURCE_HINT       = PREFIX + "00110";
    static final String CONTEXT_ACCOUNT_LABEL       = PREFIX + "00111";
    static final String CONTEXT_ACCOUNT_HINT        = PREFIX + "00112";
    static final String CONTEXT_GROUP_LABEL         = PREFIX + "00113";
    static final String CONTEXT_GROUP_HINT          = PREFIX + "00114";
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Connection
  // ~~~~~~~~~ ~~~~~~~~~~
  /**
   ** Declares global visible resource identifier used for target endpoint
   ** connection information purpose.
   */
  public interface Connection {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final String CONNECT_TIMEOUT_LABEL       = PREFIX + "00201";
    static final String CONNECT_TIMEOUT_HINT        = PREFIX + "00202";
    static final String RESPONSE_TIMEOUT_LABEL      = PREFIX + "00203";
    static final String RESPONSE_TIMEOUT_HINT       = PREFIX + "00204";
  }
}
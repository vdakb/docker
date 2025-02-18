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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   Connector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Connector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.resource;

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

  static final String PREFIX                        = "JES-";

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

    static final String SERVER_HOST_LABEL        = PREFIX + "00011";
    static final String SERVER_HOST_HINT         = PREFIX + "00012";
    static final String SERVER_PORT_LABEL        = PREFIX + "00013";
    static final String SERVER_PORT_HINT         = PREFIX + "00014";
    static final String SERVER_NAME_LABEL        = PREFIX + "00015";
    static final String SERVER_NAME_HINT         = PREFIX + "00016";
    static final String SERVER_TYPE_LABEL        = PREFIX + "00017";
    static final String SERVER_TYPE_HINT         = PREFIX + "00018";
    static final String SECURE_SOCKET_LABEL      = PREFIX + "00019";
    static final String SECURE_SOCKET_HINT       = PREFIX + "00020";
    static final String ROOT_CONTEXT_LABEL       = PREFIX + "00021";
    static final String ROOT_CONTEXT_HINT        = PREFIX + "00022";
    static final String PRINCIPAL_USERNAME_LABEL = PREFIX + "00023";
    static final String PRINCIPAL_USERNAME_HINT  = PREFIX + "00024";
    static final String PRINCIPAL_PASSWORD_LABEL = PREFIX + "00025";
    static final String PRINCIPAL_PASSWORD_HINT  = PREFIX + "00026";
    static final String DOMAIN_USERNAME_LABEL    = PREFIX + "00027";
    static final String DOMAIN_USERNAME_HINT     = PREFIX + "00028";
    static final String DOMAIN_PASSWORD_LABEL    = PREFIX + "00029";
    static final String DOMAIN_PASSWORD_HINT     = PREFIX + "00030";
    static final String LOGIN_CONFIG_LABEL       = PREFIX + "00031";
    static final String LOGIN_CONFIG_HINT        = PREFIX + "00032";
    static final String COUNTRY_LABEL            = PREFIX + "00033";
    static final String COUNTRY_HINT             = PREFIX + "00034";
    static final String LANGUAGE_LABEL           = PREFIX + "00035";
    static final String LANGUAGE_HINT            = PREFIX + "00036";
    static final String TIMEZONE_LABEL           = PREFIX + "00037";
    static final String TIMEZONE_HINT            = PREFIX + "00038";
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

    static final String CONNECT_TIMEOUT_LABEL    = PREFIX + "00201";
    static final String CONNECT_TIMEOUT_HINT     = PREFIX + "00202";
    static final String RESPONSE_TIMEOUT_LABEL   = PREFIX + "00203";
    static final String RESPONSE_TIMEOUT_HINT    = PREFIX + "00204";
  }
}
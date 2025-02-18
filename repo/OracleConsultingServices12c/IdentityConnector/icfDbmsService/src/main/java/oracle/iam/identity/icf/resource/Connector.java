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
    Subsystem   :   Generic Database Connector

    File        :   Connector.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Connector.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
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

  static final String PREFIX                        = "DBS-";

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

    static final String DRIVER_LABEL                = PREFIX + "00011";
    static final String DRIVER_HINT                 = PREFIX + "00012";
    static final String HOST_LABEL                  = PREFIX + "00013";
    static final String HOST_HINT                   = PREFIX + "00014";
    static final String PORT_LABEL                  = PREFIX + "00015";
    static final String PORT_HINT                   = PREFIX + "00016";
    static final String SECURE_LABEL                = PREFIX + "00017";
    static final String SECURE_HINT                 = PREFIX + "00018";
    static final String NAME_LABEL                  = PREFIX + "00019";
    static final String NAME_HINT                   = PREFIX + "00020";
    static final String SCHEMA_LABEL                = PREFIX + "00021";
    static final String SCHEMA_HINT                 = PREFIX + "00022";
    static final String PRINCIPAL_USERNAME_LABEL    = PREFIX + "00023";
    static final String PRINCIPAL_USERNAME_HINT     = PREFIX + "00024";
    static final String PRINCIPAL_PASSWORD_LABEL    = PREFIX + "00025";
    static final String PRINCIPAL_PASSWORD_HINT     = PREFIX + "00026";
    static final String COUNTRY_LABEL               = PREFIX + "00027";
    static final String COUNTRY_HINT                = PREFIX + "00028";
    static final String LANGUAGE_LABEL              = PREFIX + "00029";
    static final String LANGUAGE_HINT               = PREFIX + "00030";
    static final String TIMEZONE_LABEL              = PREFIX + "00032";
    static final String TIMEZONE_HINT               = PREFIX + "00033";
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

    static final String CONNECT_TIMEOUT_LABEL       = PREFIX + "00041";
    static final String CONNECT_TIMEOUT_HINT        = PREFIX + "00042";
    static final String CONNECT_RETRYCOUNT_LABEL    = PREFIX + "00043";
    static final String CONNECT_RETRYCOUNT_HINT     = PREFIX + "00044";
    static final String CONNECT_RETRYINTERVAL_LABEL = PREFIX + "00045";
    static final String CONNECT_RETRYINTERVAL_HINT  = PREFIX + "00046";
    static final String RESPONSE_TIMEOUT_LABEL      = PREFIX + "00047";
    static final String RESPONSE_TIMEOUT_HINT       = PREFIX + "00048";
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

    static final String SYSTEM_TIMESTAMP_LABEL      = PREFIX + "00051";
    static final String SYSTEM_TIMESTAMP_HINT       = PREFIX + "00052";
    static final String ENFORCE_AUTOCOMMIT_LABEL    = PREFIX + "00053";
    static final String ENFORCE_AUTOCOMMIT_HINT     = PREFIX + "00054";
    static final String ROWNUM_ATTRIBUTE_LABEL      = PREFIX + "00055";
    static final String ROWNUM_ATTRIBUTE_HINT       = PREFIX + "00056";
    static final String ENTRY_IDENTIFIER_LABEL      = PREFIX + "00057";
    static final String ENTRY_IDENTIFIER_HINT       = PREFIX + "00058";
    static final String ENTRY_UNIQUENAME_LABEL      = PREFIX + "00059";
    static final String ENTRY_UNIQUENAME_HINT       = PREFIX + "00060";
    static final String ENTRY_PASSWORD_LABEL        = PREFIX + "00061";
    static final String ENTRY_PASSWORD_HINT         = PREFIX + "00062";
    static final String ENTRY_STATUS_LABEL          = PREFIX + "00063";
    static final String ENTRY_STATUS_HINT           = PREFIX + "00064";
    static final String ENTRY_CREATOR_LABEL         = PREFIX + "00065";
    static final String ENTRY_CREATOR_HINT          = PREFIX + "00066";
    static final String ENTRY_CREATED_LABEL         = PREFIX + "00067";
    static final String ENTRY_CREATED_HINT          = PREFIX + "00068";
    static final String ENTRY_MODIFIER_LABEL        = PREFIX + "00069";
    static final String ENTRY_MODIFIER_HINT         = PREFIX + "00070";
    static final String ENTRY_MODIFIED_LABEL        = PREFIX + "00071";
    static final String ENTRY_MODIFIED_HINT         = PREFIX + "00072";
    static final String FETCH_SCHEMA_LABEL          = PREFIX + "00073";
    static final String FETCH_SCHEMA_HINT           = PREFIX + "00074";
  }
}
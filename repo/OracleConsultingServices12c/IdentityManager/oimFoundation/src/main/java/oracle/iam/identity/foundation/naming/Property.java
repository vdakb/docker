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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Property.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Property.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface Property
// ~~~~~~~~~ ~~~~~~~~
/**
 ** The <code>Lookup</code> declares the usefull constants to deal with
 ** <code>System Property Definition</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface Property {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Constant to access Oracle Identity Manager <code>User Language</code>
   ** system properties.
   */
  static final String LANGUAGE           = "user.language";

  /**
   ** Constant to access Oracle Identity Manager <code>User Region</code>
   ** system properties.
   */
  static final String REGION             = "user.region";

  /**
   ** Constant to access Oracle Identity Manager <code>Email Server</code>
   ** system properties.
   */
  static final String MAILSERVER         = "XL.Mailserver";

  /**
   ** Constant to access Oracle Identity Manager <code>Default Date Format</code>
   ** system properties.
   */
  static final String DATEFORMAT         = "XL.DefaultDateFormat";

  /**
   ** Constant to access Oracle Identity Manager <code>Read Limit</code>
   ** system properties.
   */
  static final String READLIMIT          = "XL.READ_LIMIT";

  /** Standard user language */
  static final String DEFAULT_LANGUAGE   = "en";

  /** Standard user region */
  static final String DEFAULT_REGION     = "US";

  /** Standard mail server */
  static final String DEFAULT_MAILSERVER = "Email Server";

  /** Standard default date format */
  static final String DEFAULT_DATEFORMAT = "yyyy/MM/dd hh:mm:ss z";

  /** Standard read limit */
  static final String DEFAULT_READLIMIT  = "500";
}
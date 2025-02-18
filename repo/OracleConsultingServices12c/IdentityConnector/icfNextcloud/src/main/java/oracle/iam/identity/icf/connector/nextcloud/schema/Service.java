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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Nextcloud Connector

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Service.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.nextcloud.schema;

////////////////////////////////////////////////////////////////////////////////
// final class Service
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** An interface to describe and transform Nextcloud Resource object to and from
 ** Identity Connector attribute collections.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Service {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The JSON of the entity system identifier attribute for users, teams and
   ** organizations.
   */
  public static final String ID           = "id";
  /**
   ** The JSON element of the email attribute for users and teams.
   */
  public static final String EMAIL        = "email";
  /**
   ** The JSON element of the activation status attribute of a user.
   */
  public static final String ENABLED      = "enabled";
  /**
   ** The JSON element of the display attribute of a role.
   */
  public static final String DISPLAY_NAME = "displayName";
  /**
   ** The JSON element of the preference value language.
   */
  public static final String LANGUAGE     = "language";

  /**
   ** The one and only instance of the <code>Service</code>.
   ** <p>
   ** Singleton Pattern
   */
  public static final Service instance    = new Service();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default Keyclaok <code>Service</code> connector that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Service()" and enforces use of the public method below.
   */
  private Service() {
    // ensure inheritance
    super();
  }
}

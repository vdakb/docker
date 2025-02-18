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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Identity Governance Connector

    File        :   ServiceClass.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceClass.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.oig.schema;

import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

///////////////////////////////////////////////////////////////////////////////
// final class ServiceClass
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** This is object classes extending the standard schema.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class ServiceClass {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The object class name for a organization. */
  public static final String      TENANT_NAME = SchemaUtility.createSpecialName("tenant");

  /**
   ** The object class name  belonging to an organization extending the
   ** standard schema.
   */
  public static final ObjectClass TENANT      = new ObjectClass(TENANT_NAME);

  /** The object class name for a global admin role. */
  public static final String      GLOBAL_NAME = SchemaUtility.createSpecialName("global");

  /**
   ** The object class name  belonging to a global admin role extending the
   ** standard schema.
   */
  public static final ObjectClass GLOBAL      = new ObjectClass(GLOBAL_NAME);

  /** The object class name for a scoped admin role. */
  public static final String      SCOPED_NAME = SchemaUtility.createSpecialName("scoped");

  /**
   ** The object class name  belonging to a scoped admin role extending the
   ** standard schema.
   */
  public static final ObjectClass SCOPED      = new ObjectClass(SCOPED_NAME);


  /** The object class name for a scoped admin role. */
  public static final String      OU_ROLE_NAME = SchemaUtility.createSpecialName("ou_role");

  /**
   ** The object class name  belonging to a scoped admin role extending the
   ** standard schema.
   */
  public static final ObjectClass OU_ROLE      = new ObjectClass(OU_ROLE_NAME);


  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>ServiceClass</code> identity service connector
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ServiceClass()" and enforces use of the public method below.
   */
  private ServiceClass() {
    // should never be instantiated
    throw new AssertionError();
  }
}
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
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Keycloak.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Keycloak.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

///////////////////////////////////////////////////////////////////////////////
// enum Keycloak
// ~~~~ ~~~~~~~~
/**
 ** This is object classes extending the standard schema.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum Keycloak {

    /**
     ** The object class name belonging to a realm role extending the standard schema.
     */
    ROLE(Keycloak.ENTITY_ROLE)

    /*
     * The object class name belonging to a client role extending the standard schema.
     */
  , CLIENTROLE(Keycloak.ENTITY_CLIENT_ROLE)
    /*
     * The object class name belonging to a credential extending the standard schema.
     */
  , CREDENTIAL(Keycloak.ENTITY_CREDENTIAL)
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name of the user entity.
   */
  public static final String ENTITY_USER        = "User";
  /**
   ** The name of the role entity.
   */
  public static final String ENTITY_ROLE        = "RealmRole";
  /**
   ** The name of the client role entity.
   */
  public static final String ENTITY_CLIENT_ROLE = "ClientRole";
  /**
   ** The name of the group entity.
   */
  public static final String ENTITY_GROUP       = "Group";
  /**
   ** The name of the credential entity.
   */
  public static final String ENTITY_CREDENTIAL  = "Credential";
  /**
   ** The name of the access permission resource.
   */
  public static final String ENTITY_ACCESS      = "Access";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public final String      name;
  public final ObjectClass clazz;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for <code>Keycloak</code> with a constraint value.
   **
   ** @param  name             the constraint name (used in ICF schemas) of the
   **                          object.
   **                          <br>
   **                          Allowed object is {@link String}.
   */
  Keycloak(final String name) {
    this.name  = SchemaUtility.createSpecialName(name);
    this.clazz = new ObjectClass(this.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  from
  /**
   ** Factory method to create a proper <code>Keycloak</code> constraint
   ** from the given string value.
   **
   ** @param  id                 the string value the order constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Keycloak</code> constraint.
   **                            <br>
   **                            Possible object is <code>Keycloak</code>.
   */
  public static Keycloak from(final String id) {
    for (Keycloak cursor : Keycloak.values()) {
      if (cursor.name.equals(id))
        return cursor;
    }
    throw new IllegalArgumentException(id);
  }
}
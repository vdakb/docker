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
    Subsystem   :   Grafana Connector

    File        :   Grafana.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the enum
                    Grafana.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.schema;

import oracle.iam.identity.icf.foundation.utility.SchemaUtility;

import org.identityconnectors.framework.common.objects.ObjectClass;

///////////////////////////////////////////////////////////////////////////////
// enum Grafana
// ~~~~ ~~~~~~~
/**
 ** This is object classes extending the standard schema.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum Grafana {

    /**
     ** The object class name belonging to a team extending the standard schema.
     */
    TEAM(Grafana.ENTITY_TEAM)
    /**
     ** The object class name belonging to an organization extending the
     ** standard schema.
     */
  , ORGANIZATION(Grafana.ENTITY_ORGANIZATION)
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name of the user entity.
   */
  public static final String ENTITY_USER         = "User";
  /**
   ** The name of the role entity.
   */
  public static final String ENTITY_ROLE         = "Role";
  /**
   ** The name of the team entity.
   */
  public static final String ENTITY_TEAM         = "Team";
  /**
   ** The name of the organization entity.
   */
  public static final String ENTITY_ORGANIZATION = "Organization";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  public final String        name;
  public final ObjectClass   clazz;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for <code>Grafana</code> with a constraint value.
   **
   ** @param  name             the constraint name (used in ICF schemas) of the
   **                          object.
   **                          <br>
   **                          Allowed object is {@link String}.
   */
  Grafana(final String name) {
    this.name  = SchemaUtility.createSpecialName(name);
    this.clazz = new ObjectClass(this.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  from
  /**
   ** Factory method to create a proper <code>Grafana</code> constraint
   ** from the given string value.
   **
   ** @param  id                 the string value the order constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Grafana</code> constraint.
   **                            <br>
   **                            Possible object is <code>Grafana</code>.
   */
  public static Grafana from(final String id) {
    for (Grafana cursor : Grafana.values()) {
      if (cursor.name.equals(id))
        return cursor;
    }
    throw new IllegalArgumentException(id);
  }
}
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
    Subsystem   :   GitLab Connector

    File        :   Project.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Project.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.gitlab.schema;

import org.identityconnectors.framework.common.objects.Schema;
import org.identityconnectors.framework.common.objects.SchemaBuilder;

import oracle.iam.identity.icf.connector.AbstractConnector;

////////////////////////////////////////////////////////////////////////////////
// final class Service
// ~~~~~ ~~~~~ ~~~~~~~
/**
 ** An interface to describe and transform Keycloak Resource object to and from
 ** Identity Connector attribute collections.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Service {

  /**
   ** The one and only instance of the <code>Service</code>.
   ** <p>
   ** Singleton Pattern
   */
  public static final Service instance = new Service();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default GitLab <code>Service</code> connector that allows
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

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Describes the types of objects this Connector supports upon request.
   ** <p>
   ** The schema is cached over the lifetime of this connector.
   ** <p>
   ** Builds schema meta-data from configuration without obtaining meta-data
   ** from target environment.
   ** <p>
   ** This method is considered an operation since determining supported objects
   ** may require configuration information and allows this determination to be
   ** dynamic.
   ** <p>
   ** The special [@link Uid} attribute should never appear in the schema, as it
   ** is not a real attribute of an object, rather a reference to it.
   ** <br>
   ** If the resource object-class has a writable unique id attribute that is
   ** different than its name, then the schema should contain a
   ** resource-specific attribute that represents this unique id.
   ** <br>
   ** For example, a Unix account object might contain unix <code>uid</code>.
   **
   ** @param  clazz              the connector {@link Class} for which the
   **                            schema is built.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link AbstractConnector}.
   **
   ** @return                    the {@link Schema}.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  public final Schema schema(final Class<? extends AbstractConnector> clazz) {
    final SchemaBuilder   builder = new SchemaBuilder(clazz);
    return builder.build();
  }
}
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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   SystemProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemProperty.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.schema;

import java.util.Map;

////////////////////////////////////////////////////////////////////////////////
// class SystemProperty
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>System Property</code> resource in Openfire.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemProperty extends Entity.Property {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String DOMAIN        = "xmpp.domain";
  public static final String PASSWORD      = "passwordKey";
  public static final String ITERATION     = "sasl.scram-sha-1.iteration-count";
  public static final String ADMINISTRATOR = "admin.authorizedJIDs";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>SystemProperty</code> target resource that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private SystemProperty() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SystemProperty</code> for the specified key-value pair.
   **
   ** @param  name               the name of the <code>Property</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the <code>Property</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private SystemProperty(final String name, final String value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    attribute(NAME, name);
    attribute(VALUE, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SystemProperty</code> target resource with the values
   ** supplied by the given mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  private SystemProperty(final Map<String, Object> data) {
    // ensure inheritance
    super(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>SystemProperty</code> instance.
   **
   ** @return                    the <code>SystemProperty</code> instance
   **                            created.
   **                            <br>
   **                            Possible object is <code>SystemProperty</code>.
   */
  public static SystemProperty build() {
    return new SystemProperty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>SystemProperty</code> instance with the
   ** <code>name</code> specified and an initial <code>value</code>.
   **
   ** @param  name               the name of the <code>SystemProperty</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the initial value of the
   **                            <code>SystemProperty</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SystemProperty</code> instance
   **                            created.
   **                            <br>
   **                            Possible object is <code>SystemProperty</code>.
   */
  public static SystemProperty build(final String name, final String value) {
    return new SystemProperty(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>SystemProperty</code> instance that
   ** populates its values from the given attribute mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the <code>SystemProperty</code> instance
   **                            populated.
   **                            <br>
   **                            Possible object is <code>SystemProperty</code>.
   */
  public static SystemProperty build(final Map<String, Object> data) {
    return new SystemProperty(data);
  }
}
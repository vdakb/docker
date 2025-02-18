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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   IdentityServerResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    IdentityServerResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.rmi;

import java.util.Map;
import java.util.LinkedHashMap;

import oracle.iam.platform.OIMClient;

public enum IdentityServerType {

  /** the type name of a WebLogic Managed Server */
  WEBLOGIC(
      IdentityServerConstant.SERVER_TYPE_WEBLOGIC
    , IdentityServerConstant.SYSTEM_PROPERTY_WEBLOGIC
    , OIMClient.WLS_CONTEXT_FACTORY
    , IdentityServerConstant.PROTOCOL_WEBLOGIC_DEFAULT
    , IdentityServerConstant.PROTOCOL_WEBLOGIC_SECURE
    ),

    /** the type name of a WebSphere Server */
  WEBSPHERE(
      IdentityServerConstant.SERVER_TYPE_WEBSPHERE
    , IdentityServerConstant.SYSTEM_PROPERTY_WEBSPHERE
    , OIMClient.WAS_CONTEXT_FACTORY
    , IdentityServerConstant.PROTOCOL_WEBSPHERE_DEFAULT
    , IdentityServerConstant.PROTOCOL_WEBSPHERE_SECURE
    );

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5374570349594455335")
  private static final long serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String value;
  private final String property;
  private final String factory;
  private final String nonssl;
  private final String ssl;

  private final Map<String, String> old = new LinkedHashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerType</code> that allows use as a JavaBean.
   **
   ** @param  property           the name of the system property value to
   **                            identify the server type.
   ** @param  factory            the full qualified class name of the JNDI
   **                            context factory.
   ** @param  nonssl             the non-ssl protocol prefix.
   ** @param  ssl                the ssl protocol prefix.
   */
  IdentityServerType(final String value, final String property, final String factory, final String nonssl, final String ssl) {
    // initialize instance attributes
    this.value    = value;
    this.property = property;
    this.factory  = factory;
    this.nonssl   = nonssl;
    this.ssl      = ssl;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  ////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the value property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String value() {
    return this.value;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Returns the name of the system property value to identify the server
   ** type.
   **
   ** @return                    possible object is {@link String}.
   */
  public String property() {
    return this.property;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   factory
  /**
   ** Returns the value of the vendor specific context factory.
   **
   ** @return                    possible object is {@link String}.
   */
  public String factory() {
    return this.factory;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  ////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   fromValue
  /**
   ** Factory method to create a proper server type from the given string
   ** value.
   **
   ** @param  value              the string value the server type should be
   **                            returned for.
   **
   ** @return                    the server type.
   */
  public static IdentityServerType fromValue(final String value) {
    for (IdentityServerType cursor : IdentityServerType.values()) {
      if (cursor.value.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   saveSystemProperties
  /**
   ** Manage system properties that needs to exists et the time a connection
   ** is established to the specific server type.
   */
  public void saveSystemProperties() {
    this.old.put(IdentityServerConstant.JAVA_SECURITY_CONFIG, System.getProperty(this.property));
    this.old.put(this.property, System.getProperty(this.property));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   restoreSystemProperties
  /**
   ** Restore system properties that might be overriden by
   ** {@link #saveSystemProperties()} et the time a connection was established
   ** to the specific server type.
   */
  public void restoreSystemProperties() {
    // reset the system properties to the origin
    for (Map.Entry<String, String> property : old.entrySet()) {
      final String name  = property.getKey();
      final String value = property.getValue();
      if (value == null)
        System.getProperties().remove(name);
      else
        System.setProperty(name, value);
    }
  }
}
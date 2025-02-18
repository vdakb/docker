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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ClientProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    ClientProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// enum ClientProperty
// ~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>ClientProperty</code> defines sepcific parameter type declarations
 ** regarding <code>Client</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum ClientProperty implements FeatureProperty {

  /** ??? */
  ATTRIBUTES("attributes", Type.STRING, false, null),

  /** ??? */
  ATTRIBUTNAME("attrName", Type.STRING, false, null),

  /** ??? */
  ATTRIBUTVALUE("attrValue", Type.STRING, false, null),

  /** ??? */
  ATTRIBUTTYPE("attrType", Type.STRING, false, null),

  /**
   ** The ClientID of an <code>Resource Client</code>.
   **  Will be auto generated if not specified.
   */
  ID("id", Type.STRING, false, null),

  /** An optional description of the <code>Resource Client</code>. */
  DESCRIPTION("description", Type.STRING, false, null),
  /**
   ** Name of the Identity Domain under which the <code>Resource Client</code>
   ** is created.
   */
  DOMAIN("idDomain", Type.STRING, true, null),

  /** Allowed Values: [ "CONFIDENTIAL_CLIENT", "PUBLIC_CLIENT", "MOBILE_CLIENT" ] */
  TYPE("clientType", Type.STRING, false, null),

  /** Mandatory scope which is the default scope returned in the token */
  DEFAULTSCOPE("defaultScope", Type.STRING, true, null),

  /** ??? */
  GRANTTYPES("grantTypes", Type.STRING, false, null),

  /** the internal identifier of an <code>Resource Client</code>. */
  UID("uid", Type.STRING, false, null),

  /** the public name of an <code>Resource Client</code>. */
  NAME("name", Type.STRING, false, null),

  /** ??? */
  REDIRECTURIS("redirectURIs", Type.STRING, false, null),

  /** ??? */
  REDIRECTURL("url", Type.STRING, false, null),

  /** ??? */
  REDIRECTSECURE("isHttps", Type.BOOLEAN, false, "false"),

  /** ??? */
  SCOPES("scopes", Type.STRING, false, null),

  /** Password for the client if confidential */
  SECRET("secret", Type.STRING, false, null);

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the entity type declaration for debugging purpose */
  static final String       ENTITY           = "Resource Client";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2973070333329194794")
  private static final long serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String   id;
  final Type     type;
  final boolean  required;
  final String   defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ClientProperty</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  ClientProperty(final String id, final Type type, final boolean required, final String defaultValue) {
    this.id           = id;
    this.type         = type;
    this.required     = required;
    this.defaultValue = defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (ServiceAction)
  /**
   ** Returns the id of the property.
   **
   ** @return                    the id of the property.
   */
  @Override
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type (FeatureProperty)
  /**
   ** Returns the type of the property.
   **
   ** @return                    the type of the property.
   */
  @Override
  public Type type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   required (FeatureProperty)
  /**
   ** Returns <code>true</code> if the property is mandatory.
   **
   ** @return                    <code>true</code> if the property is mandatory;
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean required() {
    return this.required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultValue (FeatureProperty)
  /**
   ** Returns the defaultValue of the property if any.
   **
   ** @return                    the defaultValue of the property if any.
   */
  @Override
  public String defaultValue() {
    return this.defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a proper identity domain property from the given
   ** string value.
   **
   ** @param  value              the string value the client property should be
   **                            returned for.
   **
   ** @return                    the client property.
   */
  public static ClientProperty from(final String value) {
    for (ClientProperty cursor : ClientProperty.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insensitive
  /**
   ** Factory method to create a proper resource client property from the given
   ** string value.
   **
   ** @param  value              the string value the resource client property
   **                            should be returned for.
   **
   ** @return                    the identity domain property.
   */
  public static ClientProperty insensitive(final String value) {
    for (ClientProperty cursor : ClientProperty.values()) {
      if (cursor.id.equalsIgnoreCase(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}
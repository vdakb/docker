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

    File        :   ResourceProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    ResourceProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// enum ResourceProperty
// ~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>ResourceProperty</code> defines sepcific parameter type declarations
 ** regarding <code>Resource Server</code>s.
 ** <p>
 ** A <code>Resource Server</code> hosts protected resources. The
 ** <code>Resource Server</code> is capable of accepting and responding to
 ** protected resource requests using access tokens.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum ResourceProperty implements FeatureProperty {

  /** An optional description of the <code>Resource Server</code>. */
  AUDIENCECLAIM("audienceClaim", Type.STRING, false, null),

  /** An optional description of the <code>Resource Server</code>. */
  DESCRIPTION("description", Type.STRING, false, null),

  /**
   ** Name of the Identity Domain under which the <code>Resource Server</code>
   ** is created.
   */
  DOMAIN("idDomain", Type.STRING, true, null),

  /** the internal identifier of an <code>Resource Server</code>. */
  ID("id", Type.STRING, false, null),

  /** the public name of an <code>Resource Server</code>. */
  NAME("name", Type.STRING, false, null),

  /** ??? */
  NAMESPACEPREFIX("resourceServerNameSpacePrefix", Type.STRING, false, null),

  /** ??? */
  SCOPES("scopes", Type.STRING, false, null),

  /** ??? */
  SCOPENAME("scopeName", Type.STRING, false, null),

  /** An optional description of the <code>Resource Server</code>. */
  SUBJECTS("subjects", Type.STRING, false, null),

  /** ??? */
  TOKENATTRIBUTES("tokenAttributes", Type.STRING, false, null),

  /** ??? */
  TOKENATTRIBUTENAME("attrName", Type.STRING, false, null),

  /** ??? */
  TOKENATTRIBUTEVALUE("attrValue", Type.STRING, false, null),

  /** ??? */
  TOKENATTRIBUTETYPE("attrType", Type.STRING, false, null),

  /** ??? */
  TYPE("resServerType", Type.STRING, false, null);

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the entity type declaration for debugging purpose */
  static final String       ENTITY           = "Resource Server";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1184224603218756033")
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
   ** Constructs a <code>ResourceProperty</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  ResourceProperty(final String id, final Type type, final boolean required, final String defaultValue) {
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
   ** Factory method to create a proper resource server property from the given
   ** string value.
   **
   ** @param  value              the string value the resource server property
   **                            should be returned for.
   **
   ** @return                    the resource server property.
   */
  public static ResourceProperty from(final String value) {
    for (ResourceProperty cursor : ResourceProperty.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insensitive
  /**
   ** Factory method to create a proper resource server property from the given
   ** string value.
   **
   ** @param  value              the string value the resource server property
   **                            should be returned for.
   **
   ** @return                    the resource server property.
   */
  public static ResourceProperty insensitive(final String value) {
    for (ResourceProperty cursor : ResourceProperty.values()) {
      if (cursor.id.equalsIgnoreCase(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}
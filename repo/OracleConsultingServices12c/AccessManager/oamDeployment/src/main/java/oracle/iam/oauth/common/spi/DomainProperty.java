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

    File        :   DomainProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    DomainProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.spi;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// enum DomainProperty
// ~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>DomainProperty</code> defines sepcific parameter type declarations
 ** regarding <code>Identity Domain</code>s.
 ** <p>
 ** A <code>Identity Domain</code> is a entity that contain all artifacts
 ** required to provide standard OAuth Services .
 ** <p>
 ** Each <code>Identity Domain</code> is an independent entity.
 ** <br>
 ** One of the primary use cases of the <code>Identity Domain</code> is for
 ** multi tenants deployments. Each <code>Identity Domain</code> will correspond
 ** to a tenant. This can apply to different departments in an organization if
 ** there is a need for independence. This will also be useful for cloud
 ** deployments where each <code>Identity Domain</code> can correspond to a
 ** separate tenant or entity. The following artifacts are just some of the
 ** components configured within an OAuth Services <code>Identity Domain</code>.
 ** <ul>
 **   <li>One or more Clients
 **   <li>One or more Resource Servers
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum DomainProperty implements FeatureProperty {

  /**
   ** Fully qualified URL of the customized consent page to use by the
   ** <code>Identity Domain</code>.
   */
  CONSENTPAGEURL("consentPageURL", Type.STRING, false, null),

  /**
   ** Fully qualified URL of the customized consent page to use by the
   ** <code>Identity Domain</code>.
   */
  CUSTOMATTRIBUTES("customAttrs", Type.STRING, false, null),

  /** An optional description of the <code>Identity Domain</code>. */
  DESCRIPTION("description", Type.STRING, false, null),

  /**
   ** Fully qualified URL of the customized error page  to use by the
   ** <code>Identity Domain</code>.
   */
  ERRORPAGEURL("errorPageURL", Type.STRING, false, null),

  /** the internal identifier of an <code>Identity Domain</code>. */
  ID("id", Type.STRING, false, null),

  /** the public name of an <code>Identity Domain</code>. */
  NAME("name", Type.STRING, false, null),

  /**
   ** Name of the <code>Identity Store</code> against which authentication is
   ** completed by the <code>Identity Domain</code>.
   **
   */
  IDENTITYPROVIDER("identityProvider", Type.STRING, false, null),

  /** ??? */
  TOKENSETTINGS("tokenSettings", Type.STRING, false, null),

  /** ??? */
  TOKENTYPE("tokenType", Type.STRING, false, null),

  /** ??? */
  TOKENEXPIRY("tokenExpiry", Type.INTEGER, false, null),

  /** ??? */
  TOKENLIFECYCLE("lifeCycleEnabled", Type.BOOLEAN, false, null),

  /** ??? */
  REFRESHTOKENENABLED("refreshTokenEnabled", Type.BOOLEAN, false, null),

  /** ??? */
  REFRESHTOKENEXPIRY("refreshTokenExpiry", Type.INTEGER, false, null),

  /** ??? */
  REFRESHTOKENLIFECYCLE("refreshTokenLifeCycleEnabled", Type.BOOLEAN, false, null),

  /** ??? */
  TRUSTSTOREIDENTIFIER("trustStoreIdentifier", Type.STRING, false, null);

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the entity type declaration for debugging purpose */
  static final String       ENTITY           = "Identity Domain";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4188824445079358778")
  private static final long serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String              id;
  final Type                type;
  final boolean             required;
  final String              defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DomainProperty</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  DomainProperty(final String id, final Type type, final boolean required, final String defaultValue) {
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
   ** @param  value              the string value the identity domain property
   **                            should be returned for.
   **
   ** @return                    the identity domain property.
   */
  public static DomainProperty from(final String value) {
    for (DomainProperty cursor : DomainProperty.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insensitive
  /**
   ** Factory method to create a proper identity domain property from the given
   ** string value.
   **
   ** @param  value              the string value the identity domain property
   **                            should be returned for.
   **
   ** @return                    the identity domain property.
   */
  public static DomainProperty insensitive(final String value) {
    for (DomainProperty cursor : DomainProperty.values()) {
      if (cursor.id.equalsIgnoreCase(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}
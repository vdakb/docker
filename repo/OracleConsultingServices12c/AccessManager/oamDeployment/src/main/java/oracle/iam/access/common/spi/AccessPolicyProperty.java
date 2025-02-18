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

    File        :   AccessPolicyProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    AccessPolicyProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// enum AccessPolicyProperty
// ~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AccessPolicyProperty</code> defines sepcific parameter type
 ** declarations regarding <code>Authentication/Authorization Policy</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum AccessPolicyProperty implements FeatureProperty {

    /**
     ** The top-level construct of the Oracle Access Manager 11g policy model is
     ** the <code>Application Domain</code>.
     ** <br>
     ** Each application domain provides a logical container for resources, and
     ** the associated authentication and authorization policies that dictate
     ** who can access these.
     ** <p>
     ** The size and number of <code>Application Domain</code>s is up to the
     ** administrator; the decision can be based on individual
     ** <code>Application Resource</code>s or any other logical grouping as
     ** needed. An <code>Application Domain</code> can be automatically created
     ** during <code>Access Agent</code> registration. Also, administrators can
     ** protect multiple <code>Application Domain</code>s using the same agent
     ** by manually creating the <code>Application Domain</code> and adding the
     ** resources and policies.
     */
    APPLICATION_DOMAIN("applicationDomainName", Type.STRING, false, null)
  , AUTHENTICATION_SCHEME_PROTECTED("authenticationSchemeProtected", Type.STRING, false, null)
  , AUTHENTICATION_SCHEME_PUBLIC("authenticationSchemePublic", Type.STRING, false, null)
  , PROTECTED_SCHEME("protectedScheme", Type.STRING, false, null)
  , PUBLIC_SCHEME("publicScheme", Type.STRING, false, null),
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the entity type declaration for debugging purpose */
  public static final String ENTITY           = "Policy";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1644882060748575644")
  private static final long  serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String   id;
  final Type     type;
  final boolean  required;
  final String   defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Mode
  // ~~~~ ~~~~
  /**
   ** enum to specify the mode of an <code>Access Policy</code> request.
   */
  public enum Mode {
      CREATE("policyCreate")
    , MODIFY("policyUpdate")
    , DELETE("policyDelete")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-1564531186277387264")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Mode</code>
     **
     ** @param  value            the mode value.
     */
    Mode(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the mode property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper request mode from the given string
     ** value.
     **
     ** @param  value              the string value the request mode should be
     **                            returned for.
     **
     ** @return                    the request mode.
     */
    public static Mode from(final String value) {
      for (Mode cursor : Mode.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccessPolicyProperty</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  AccessPolicyProperty(final String id, final Type type, final boolean required, final String defaultValue) {
    this.id           = id;
    this.type         = type;
    this.required     = required;
    this.defaultValue = defaultValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (ServiceOperation)
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
   ** Factory method to create a proper access server property from the given
   ** string value.
   **
   ** @param  value              the string value the access server property
   **                            should be returned for.
   **
   ** @return                    the access server property.
   */
  public static AccessPolicyProperty from(final String value) {
    for (AccessPolicyProperty cursor : AccessPolicyProperty.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}
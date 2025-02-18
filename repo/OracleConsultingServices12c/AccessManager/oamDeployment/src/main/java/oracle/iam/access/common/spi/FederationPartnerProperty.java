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

    File        :   FederationPartnerProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FederationPartnerProperty.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import oracle.iam.access.common.FeatureProperty;

////////////////////////////////////////////////////////////////////////////////
// enum FederationPartnerProperty
// ~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>FederationPartnerProperty</code> defines sepcific parameter type
 ** declarations regarding <code>Federation Partner</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum FederationPartnerProperty implements FeatureProperty {

  /**
   ** The name of the federation partner to be created, deleted or configured.
   ** <p>
   ** <b>Mandatory parameter</b>
   */
  NAME("partnerName", Type.STRING, true, null);

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the entity type declaration for debugging purpose */
  static final String   ENTITY           = "Federation Partner";

  /**
   ** operation executed to determine existance of an
   ** <code>Federation Partner</code>
   */
  static final String   REPORT           = "listPartners";

  /** operation executed to create an <code>Federation Partner</code> */
  static final String   CREATE           = "addLDAP";

  /** operation executed to modify a <code>Federation Partner</code> */
  static final String   MODIFY           = "editLDAP";

  /** operation executed to delete a <code>Federation Partner</code> */
  static final String   DELETE           = "removePartner";

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to list a <code>Federation Partner</code> of a certain
   ** type.
   */
  static final String[] SIGNATURE_LIST   = {
    String.class.getName() //  0: location
  };

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to report a <code>Federation Partner</code> configuration.
   */
  static final String[] SIGNATURE_REPORT = {
  };

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to create an <code>Federation Partner</code>
   ** configuration.
   */
  static final String[] SIGNATURE_CREATE = {
  };

  /**
   ** Java method signature passed to the MBean Server to find the appropriate
   ** MBean interface to modify an <code>Federation Partner</code>
   ** configuration.
   */
  static final String[] SIGNATURE_MODIFY = {
  };

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
   ** Constructs a <code>IdentityProviderProperty</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  FederationPartnerProperty(final String id, final Type type, final boolean required, final String defaultValue) {
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
   ** Factory method to create a proper FEDERATION PARTNER property from the
   ** given string value.
   **
   ** @param  value              the string value the identity store property
   **                            should be returned for.
   **
   ** @return                    the identity store property.
   */
  public static FederationPartnerProperty from(final String value) {
    for (FederationPartnerProperty cursor : FederationPartnerProperty.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}
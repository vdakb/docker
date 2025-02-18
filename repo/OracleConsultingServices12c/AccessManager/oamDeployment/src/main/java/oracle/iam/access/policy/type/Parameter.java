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

    File        :   Parameter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Parameter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.policy.type;

import oracle.iam.access.common.type.FeatureParameter;

import oracle.iam.access.common.spi.AccessPolicyProperty;

////////////////////////////////////////////////////////////////////////////////
// class Parameter
// ~~~~~ ~~~~~~~~
/**
 ** <code>Parameter</code> defines the attribute restriction on values that can
 ** be passed as a nested parameter to deployment instances.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Parameter extends FeatureParameter{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the parameter definitions in alphabetical order
  private static final String[] VALUE = {
    AccessPolicyProperty.APPLICATION_DOMAIN.id()
  , AccessPolicyProperty.AUTHENTICATION_SCHEME_PROTECTED.id()
  , AccessPolicyProperty.AUTHENTICATION_SCHEME_PUBLIC.id()
  , AccessPolicyProperty.PROTECTED_SCHEME.id()
  , AccessPolicyProperty.PUBLIC_SCHEME.id()
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Parameter</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Parameter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Parameter</code> with the specified name and value.
   **
   ** @param  physicalType       the name of the parameter aka the physical type
   **                            of the Oracle Acccess Manager.
   ** @param  name               the value for the name.
   */
  public Parameter(final String physicalType, final String name) {
    // ensure inheritance
    super(physicalType, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return VALUE;
  }
}
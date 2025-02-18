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

    File        :   FeatureParameter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureParameter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureParameter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** <code>FeatureParameter</code> represents a named <code>Value</code>
 ** instance that passed to an {@link FeatureInstance}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FeatureParameter extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String name = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeatureParameter</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected FeatureParameter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FeatureParameter</code> with the specified name and
   ** value.
   **
   ** @param  physicalType       the name of the parameter aka the physical type
   **                            of the Oracle Acccess Manager.
   ** @param  name               the value for the name.
   */
  public FeatureParameter(final String physicalType, final String name) {
    // ensure inheritance
    super();

    this.setValue(physicalType);
    this.setName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT kernel to inject the argument for parameter name.
   **
   ** @param  name             the name of the Oracle Identity Manager object
   **                          this category wraps.
   */
  public void setName(final String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the object to be exported or imported by this
   ** category.
   **
   ** @return                    the name of the object to be exported or
   **                            imported by this category.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Parameter</code> object that
   ** represents the same <code>name</code> as this object.
   **
   ** @param other               the object to compare this
   **                            <code>Parameter</code> against.
   **
   ** @return                   <code>true</code> if the
   **                           <code>Parameter</code>s are
   **                           equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof FeatureParameter))
      return false;

    final FeatureParameter parameter = (FeatureParameter)other;
    return (this.value.equals(parameter.value) && this.name.equals(parameter.name));
  }
}
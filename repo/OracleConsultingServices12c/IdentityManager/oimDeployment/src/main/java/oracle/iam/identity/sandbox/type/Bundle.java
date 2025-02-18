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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Sandbox Service Utilities 11g

    File        :   Attribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Attribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.common.spi.SandboxInstance;

////////////////////////////////////////////////////////////////////////////////
// class Bundle
// ~~~~~ ~~~~~~
/**
 ** <code>Bundle</code> represents a particular resource string of an attribute
 ** in Identity Manager that is used to localize form attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bundle extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final SandboxInstance.Bundle delegate = new SandboxInstance.Bundle();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Bundle</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Bundle() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another <code>Sandbox</code>
   ** instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  public void setRefid(final Reference reference)
    throws BuildException {

    if (!StringUtility.isEmpty(this.delegate.clazz()))
      throw tooManyAttributes();

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClass
  /**
   ** Call by the ANT deployment to inject the argument for a resource bundle
   ** classname.
   **
   ** @param  value              the classname for a resource bundle.
   */
  public void setClass(final String value) {
    checkAttributesAllowed();
    this.delegate.clazz(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScope
  /**
   ** Call by the ANT deployment to inject the argument for the scope of a
   ** resource bundle.
   **
   ** @param  value              the scope for a resource bundle.
   */
  public void setScope(final String value) {
    checkAttributesAllowed();
    this.delegate.scope(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link SandboxInstance.Bundle} this ANT type wrappes.
   **
   ** @return                    the {@link SandboxInstance.Bundle} this ANT
   **                            type wrappes.
   */
  public SandboxInstance.Bundle delegate() {
    return this.delegate;
  }
}
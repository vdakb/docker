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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Resource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.iam.identity.common.spi.ResourceInstance;

////////////////////////////////////////////////////////////////////////////////
// class Resource
// ~~~~~ ~~~~~~~~
/**
 ** <code>Resource</code> represents a <code>Resource Object</code> instance in
 ** Identity Manager that might be created or configured after or during an
 ** import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resource extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resource() {
    // ensure inheritance
    this(new ResourceInstance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resource</code> which wrappes the specified
   ** {@link AbstractInstance} as the delegate.
   ** <br>
   ** All setters and getters are operating on the delegate.
   **
   ** @param  delegate           the {@link AbstractInstance} to wrap.
   */
  protected Resource(final AbstractInstance delegate) {
    // ensure inheritance
    super(delegate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>Resource</code> instance.
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

    // ensure inheritance
    super.setRefid(reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the {@link ResourceInstance} delegate of Identity Manager to
   ** handle.
   **
   ** @return                    the {@link ResourceInstance} delegate of
   **                            Identity Manager to handle.
   */
  public ResourceInstance instance() {
    if (isReference())
      return ((Resource)getCheckedRef()).instance();

    return (ResourceInstance)this.delegate;
  }
}
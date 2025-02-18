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

package oracle.iam.oauth.domain.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.oauth.common.spi.DomainInstance;

////////////////////////////////////////////////////////////////////////////////
// class Attribute
// ~~~~~ ~~~~~~~~
/**
 ** <code>Attribute</code> represents custom attributes of an
 ** <code>Identity Domain</code> instance in Oracle Access Manager which itself
 ** might be created, deleted or configured during an import operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Attribute extends ServiceDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final DomainInstance.Attribute delegate = new DomainInstance.Attribute();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Attribute() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId
  /**
   ** Called to inject the argument for attribute <code>id</code>.
   **
   ** @param  id                 the id of the <code>Attribute</code>.
   */
  public final void setId(final String id) {
    checkAttributesAllowed();
    this.delegate.id(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   ** Called to inject the argument for attribute <code>id</code>.
   **
   ** @param  value              the value of the <code>Attribute</code>.
   */
  public final void setValue(final String value) {
    checkAttributesAllowed();
    this.delegate.value(value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the {@link DomainInstance.Attribute} delegate of Identity
   ** Domain object to handle.
   **
   ** @return                    the {@link DomainInstance.Attribute} delegate.
   */
  final DomainInstance.Attribute delegate() {
    return this.delegate;
  }
}
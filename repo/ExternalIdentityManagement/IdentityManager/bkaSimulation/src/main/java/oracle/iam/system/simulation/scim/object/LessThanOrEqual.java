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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   LessThanOrEqual.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LessThanOrEqual.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.object;

import com.fasterxml.jackson.databind.node.ValueNode;

import oracle.iam.system.simulation.ServiceException;

import oracle.iam.system.simulation.scim.Path;

////////////////////////////////////////////////////////////////////////////////
// class LessThanOrEqual
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Determine if the <code>Entity</code> attribute value is less than or equals
 ** the one provided in the filter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LessThanOrEqual extends AttributeFilter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LessThanOrEqual</code> which belongs to the specified
   ** {@link Path}.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding <code>Entity</code>
   **                            attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   */
  public LessThanOrEqual(final Path path, final ValueNode value) {
    // ensure inheritance
    super(Type.LE, path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Filter)
  /**
   ** Applies a {@link Visitor} to this <code>Filter</code>.
   **
   ** @param  <R>                the return type of the visitor's methods.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  <P>                the type of the additional parameters to the
   **                            visitor's methods.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
   ** @param  visitor            the filter visitor.
   **                            <br>
   **                            Allowed object is {@link Visitor}}.
   ** @param  parameter          the optional additional visitor parameter.
   **                            <br>
   **                            Allowed object is <code>P</code>.
   **
   ** @return                    a result as specified by the visitor.
   **                            <br>
   **                            Possible object is <code>R</code>.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  @Override
  public <R, P> R accept(final Visitor<R, P> visitor, final P parameter)
    throws ServiceException {

    return visitor.lessThanOrEqual(parameter, this);
  }
}
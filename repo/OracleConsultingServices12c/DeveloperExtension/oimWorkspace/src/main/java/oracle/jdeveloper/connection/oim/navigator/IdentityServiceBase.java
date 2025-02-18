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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity Manager Facility

    File        :   IdentityServiceBase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    IdentityServiceBase.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim.navigator;

import javax.swing.Icon;

import oracle.ide.model.Node;

import oracle.ide.controls.WaitCursor;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointFolder;
import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

import oracle.jdeveloper.connection.oim.Bundle;

import oracle.jdeveloper.connection.oim.service.IdentityService;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServiceBase
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The expandable navigator node of the connected Identity Service navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class IdentityServiceBase extends EndpointFolder {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final int             symbol;
  final int             display;
  final IdentityService service;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>IdentityServiceRoot</code> that allows use as a
   ** JavaBean.
   **
   ** @param  parent             the owner of this {@link Node}.
   **                            <br>
   **                            Allowed object is {@link EndpointElement}.
   ** @param  service            the connection to the {@link IdentityService}.
   **                            <br>
   **                            Allowed object is {@link IdentityService}.
   ** @param  display            the resource key of the text to display in the
   **                            navigator for the element.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  symbol             the resource key of the icon to display in the
   **                            navigator for the element.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public IdentityServiceBase(final EndpointElement parent, final IdentityService service, final int display, final int symbol) {
    // ensure inheritance
    super(parent, service);

    // initialize instance attributes
    this.service = service;
    this.display = display;
    this.symbol  = symbol;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (overridden)
  /**
   ** Returns the {@link Icon} that represents the {@link Node} in the UI.
   **
   ** @return                    the {@link Icon} of the {@link Node}.
   */
  @Override
  public final Icon getIcon() {
    return Bundle.icon(this.symbol);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
  /**
   ** Provides the label that represents the <code>EndpointElement</code> in the
   ** navigator.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this <code>EndpointElement</code> and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the
   **                            <code>EndpointElement</code>.
   */
  @Override
  public String getShortLabel() {
    return Bundle.string(this.display);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manageable (overridden)
  /**
   ** Returns the {@link IdentityService} element.
   **
   ** @return                    the {@link IdentityService} element.
   */
  @Override
  public IdentityService manageable() {
    return (IdentityService)super.manageable();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waitCursor (EndpointElement)
  /**
   ** Returns the appropriate wait cursor shape.
   **
   ** @return                    the appropriate wait cursor shape.
   */
  @Override
  protected final WaitCursor waitCursor() {
    return IdentityNavigatorManager.instance().waitCursor();
  }
}
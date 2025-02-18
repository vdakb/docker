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
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryServiceEntry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryServiceEntry.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.ods;

import javax.swing.Icon;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryServiceEntry
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** An navigator entry of the connected Directory Service navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryServiceEntry extends    DirectoryServiceBase
                                   implements Manageable.Removeable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2388559709806625207")
  private static final long serialVersionUID = -2558392691355197093L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryServiceEntry</code> that maintains
   ** the specified {@link Manageable}.
   **
   ** @param  parent             the owner of this {@link DirectoryServiceBase}.
   **                            <br>
   **                            Allowed object is {@link EndpointElement}.
   ** @param  context            the {@link Manageable} context to maintain.
   **                            <br>
   **                            Allowed object is {@link Manageable}.
   */
  public DirectoryServiceEntry(final EndpointElement parent, final Manageable context) {
    // ensure inheritance
    super(parent, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon
  /**
   ** Returns the {@link Icon} that represents the <code>EndpointFolder</code>
   ** in the UI.
   **
   ** @return                    the {@link Icon} of the
   **                            <code>EndpointFolder</code>.
   **                            <br>
   **                            Possible object is {@link Icon}.
   */
  @Override
  public Icon getIcon() {
    return manageable().name().symbol();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
  /**
   ** Provides the label that represents the node.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Wizard and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   */
  @Override
  public String getShortLabel() {
    return manageable().rdn().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Manageable.Removeable)
  /**
   ** Performs all action to delete an element.
   */
  @Override
  public void remove() {
    manageable().remove();
    delete();
    final EndpointElement parent = parent();
    if (parent instanceof Manageable.Refreshable) {
      ((Manageable.Refreshable)parent).refresh();
      parent.fireStructureChanged();
    }
  }
}
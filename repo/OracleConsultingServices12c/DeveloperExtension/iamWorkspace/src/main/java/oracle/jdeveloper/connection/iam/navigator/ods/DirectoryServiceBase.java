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

    File        :   DirectoryServiceBase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryServiceBase.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.ods;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import oracle.ide.controls.WaitCursor;

import oracle.ide.model.Element;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointFolder;
import oracle.jdeveloper.connection.iam.navigator.node.EndpointElement;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;
import oracle.jdeveloper.connection.iam.navigator.context.DirectoryContext;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryServiceBase
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A navigator namingContext of the connected Directory Service navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryServiceBase extends    EndpointFolder
                                  implements Manageable.Searchable
                                  ,          Manageable.Exportable
                                  ,          Manageable.Importable
                                  ,          Manageable.Modifiable
                                  ,          Manageable.Refreshable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5249844872541447787")
  private static final long serialVersionUID = -5745174709219386919L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryServiceBase</code> that allows use as a
   ** JavaBean.
   **
   ** @param  parent             the owner of this {@link EndpointFolder}.
   **                            <br>
   **                            Allowed object is {@link EndpointElement}.
   ** @param  context            the {@link Manageable} context to maintain.
   **                            <br>
   **                            Allowed object is {@link Manageable}.
   */
  public DirectoryServiceBase(final EndpointElement parent, final Manageable context) {
    // ensure inheritance
    super(parent, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manageable (overridden)
  /**
   ** Returns the {@link Manageable} element.
   **
   ** @return                    the {@link Manageable} element.
   **                            <br>
   **                            Possible object is {@link DirectoryContext}.
   */
  @Override
  public DirectoryContext manageable() {
    return (DirectoryContext)super.manageable();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (overridden)
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
    return DirectorySchema.Symbol.BASE.icon();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
  /**
   ** Provides the label that represents the Wizard in the Object Gallery.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this Wizard and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the Wizard.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getShortLabel() {
    return manageable().name().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (overridden)
  /**
   ** Provides the long label that represents the connection element.
   **
   ** @return                    the human readable long label of the connection
   **                            element.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getLongLabel() {
    return manageable().name().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (Manageable.Searchable)
  /**
   ** Performs all action to search for elements.
   */
  @Override
  public final void search() {
    //TODO: implement
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify (Manageable.Modifiable)
  /**
   ** Performs all action to modify an element.
   **
   ** @return                  <code>true</code> if the modify activity
   **                          succeeded; <code>false</code> if the modify
   **                          activity was canceled.
   **                          <br>
   **                          Possible object is <code>bolean</code>.
   */
  @Override
  public final boolean modify() {
    return false;
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
   **                            <br>
   **                            Possible object is {@link WaitCursor}.
   */
  @Override
  protected final WaitCursor waitCursor() {
    return DirectoryNavigatorManager.instance().waitCursor();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Performs all actions to load child elements.
   **
   ** @return                    the collection of child elemments belonging to
   **                            this {@link EndpointFolder}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Element}.
   */
  protected List<Element> populate() {
    final List<Element> element = new ArrayList<Element>();
    for (DirectoryContext cursor : manageable().search()) {
      element.add(new DirectoryServiceEntry(this, cursor));
    }
    return element;
  }
}
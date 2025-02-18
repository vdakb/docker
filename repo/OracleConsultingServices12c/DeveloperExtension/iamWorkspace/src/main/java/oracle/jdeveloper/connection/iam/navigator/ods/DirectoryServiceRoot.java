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

    File        :   DirectoryServiceRoot.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryServiceRoot.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.ods;

import java.util.List;
import java.util.ArrayList;

import javax.swing.Icon;

import oracle.ide.controls.WaitCursor;

import oracle.ide.model.Element;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.navigator.EndpointServiceRoot;

import oracle.jdeveloper.connection.iam.service.DirectoryException;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;
import oracle.jdeveloper.connection.iam.navigator.context.DirectoryBase;
import oracle.jdeveloper.connection.iam.navigator.context.DirectoryContext;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryServiceRoot
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The navigator RootDSE of the connected Directory Service navigator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public class DirectoryServiceRoot extends EndpointServiceRoot {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1353594054817784891")
  private static final long serialVersionUID = 2264704737296051748L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String               name;
  final DirectoryServiceBase base;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DirectoryServiceRoot</code> that allows use as a
   ** JavaBean.
   **
   ** @param  navigator          the Initial Context that owns this context.
   **                            <br>
   **                            Allowed object is
   **                            {@link DirectoryNavigatorRoot}.
   ** @param  name               the connection id.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  manageable         the data provider for this
   **                            {@link EndpointServiceRoot}.
   **                            <br>
   **                            Allowed object is {@link Manageable}.
   **
   ** @throws DirectoryException if a name syntax violation is detected.
   */
  public DirectoryServiceRoot(final DirectoryNavigatorRoot navigator, final String name, final Manageable manageable)
    throws DirectoryException  {

    // ensure inheritance
    super(navigator, manageable);

    // initialize instance attributes
    this.name = name;
    this.base = new DirectoryServiceBase(navigator, DirectoryBase.build(manageable().service()));
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
  // Method:   getIcon
  /**
   ** Returns the {@link Icon} that represents the
   ** <code>DirectoryServiceRoot</code> in the UI.
   **
   ** @return                    the {@link Icon} of the
   **                            <code>DirectoryServiceRoot</code>.
   **                            <br>
   **                            Possible object is {@link Icon}.
   */
  @Override
  public Icon getIcon() {
    return DirectorySchema.Symbol.ROOT.icon();
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
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (overridden)
  /**
   ** Provides the long label that represents the connection element.
   **
   ** @return                    the human readable long label of the connection
   **                            element.
   */
  @Override
  public String getLongLabel() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManageables (Manageable.Composite)
  @Override
  public List<Manageable> getManageables() {
    final List<Manageable> list = new ArrayList<Manageable>();
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Manageable.Removeable)
  /**
   ** Performs all action to delete an element.
   */
  @Override
  public void remove() {
    manageable().remove();
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
  // Method:   populate (overridden)
  /**
   ** Performs all actions to load child elements.
   **
   ** @return                    the collection of child elemments belonging to
   **                            this {@link EndpointServiceRoot}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type ({@link Element}.
   */
  @Override
  protected List<Element> populate() {
    final List<Element> element = new ArrayList<Element>();
    element.add(this.base);
    return element;
  }
}
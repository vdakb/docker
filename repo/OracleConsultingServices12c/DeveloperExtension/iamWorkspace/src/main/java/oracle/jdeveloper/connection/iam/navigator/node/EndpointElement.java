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

    File        :   EndpointElement.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointElement.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import oracle.ide.util.Assert;

import oracle.ide.model.Node;
import oracle.ide.model.Element;
import oracle.ide.model.Observer;
import oracle.ide.model.UpdateMessage;

import oracle.ide.controls.WaitCursor;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointElement
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>EndpointElement</code> is a complete, default implementation of the
 ** <code>Element</code> interface.
 ** <br>
 ** This is a convenient starting point for the implementation of data classes
 ** which can be integrated with the IDE framework.
 ** <p>
 ** If a subclass of <code>EndpointElement</code> adheres to the JavaBeans API,
 ** it can also interoperate with the IDE's marshalling framework, which is used
 ** for persistence. Such a subclass can be persisted automatically (i.e. no
 ** additional persistence code needs to be written), so long as the subclass
 ** implementation follows the guidelines of the marshalling framework. For
 ** details, see the documentation for the oracle.ide.marshal package and its
 ** subpackages.
 ** <p>
 ** A JavaBean also has the advantage of interoperating with the property
 ** inspector. Newer IDE APIs are also being considered which leverage off the
 ** JavaBeans API, so implementing a <code>EndpointElement</code> subclass to
 ** follow the JavaBeans API will also result in compatibility with future
 ** features.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointElement extends    Node
                                      implements Observer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean               frozen     = false;
  private final Manageable      manageable;
  /** The owner of this {@link Node} used to send events */
  private final EndpointElement parent;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointElement</code> that maintains the
   ** specified {@link Manageable}.
   **
   ** @param  parent             the owner of this {@link Node}.
   **                            <br>
   **                            Allowed object is <code>EndpointElement</code>.
   ** @param  manageable         the data provider for this {@link Element} to
   **                            maintain.
   **                            <br>
   **                            Allowed object is {@link Manageable}.
   */
  public EndpointElement(final EndpointElement parent, final Manageable manageable) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.parent     = parent;
    this.manageable = manageable;

    // initialize instance state
    attach(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIcon (overridden)
  /**
   ** Returns the {@link Icon} that represents the <code>EndpointElement</code>
   ** in the UI.
   **
   ** @return                    the {@link Icon} of the
   **                            <code>EndpointElement</code>.
   **                            <br>
   **                            Possible object is {@link Icon}.
   */
  @Override
  public Icon getIcon() {
    return Bundle.icon(Bundle.CONNECTION_LEAF_ICON);
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getShortLabel() {
    return getClass().getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getData (overridden)
  /**
   ** Returns the data object associated with this {@link Element}.
   ** <br>
   ** Implementations will often simply return this, since the {@link Element}
   ** is often its own data object. If the implementation returns an object
   ** other than this, be sure to set ElementAttributes.DECORATES_DATA_ELEMENT.
   **
   ** @return                    the data object associated with this
   **                            {@link Element} instance.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  @Override
  public Object getData() {
    return this.manageable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Returns the owning node.
   **
   ** @return                    the owning node.
   **                            <br>
   **                            Possible object is {@link Node}.
   */
  public final EndpointElement parent() {
    return this.parent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frozen
  /**
   ** Returns the state of the editor.
   **
   ** @return                    <code>true</code> if the editor is frozen;
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean frozen() {
    return this.frozen;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manageable
  /**
   ** Returns the {@link Manageable} element.
   **
   ** @return                    the {@link Manageable} element.
   **                            <br>
   **                            Possible object is {@link Manageable}.
   */
  public Manageable manageable() {
    return this.manageable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isTrackedInNodeCache (overridden)
  /**
   ** The return value of this method indicates whether this {@link Node} should
   ** be tracked by the NodeFactory cache.
   ** <br>
   ** The default return value is <code>true</code>. {@link Node} types that
   ** should not be tracked by the NodeFactory cache should override this method
   ** to return <code>false</code>.
   **
   ** @return                    <code>true</code> if this {@link Node} should
   **                            be tracked by the IDE's NodeFactory cache;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  protected boolean isTrackedInNodeCache() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   update (Observer)
  /**
   ** Notification message.
   ** <p>
   ** Subjects call this method when they notify their observers that the
   ** subjects state has changed.
   **
   ** @param  observed           the subject whose state has changed.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  change             what has changed.
   **                            <br>
   **                            Allowed object is {@link UpdateMessage}.
   */
  public void update(final Object observed, final UpdateMessage change) {
    Assert.check(observed == this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waitCursor
  /**
   ** Returns the appropriate wait cursor shape.
   **
   ** @return                    the appropriate wait cursor shape.
   **                            <br>
   **                            Possible object is {@link WaitCursor}.
   */
  protected abstract WaitCursor waitCursor();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fireStateChanged
  /**
   ** Notifies all observers that the state of the subject has changed.
   */
  public void fireStateChanged() {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        final UpdateMessage payload = new UpdateMessage(UpdateMessage.CONTENT_MODIFIED, EndpointElement.this);
        notifyObservers(EndpointElement.this, payload);
      }
    });
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fireStructureChanged
  /**
   ** Notifies all observers that the structure of the subject has changed.
   */
  public void fireStructureChanged() {
    SwingUtilities.invokeLater(
      new Runnable() {
        public void run() {
          UpdateMessage.fireStructureChanged(EndpointElement.this);
        }
      }
    );
  }
}
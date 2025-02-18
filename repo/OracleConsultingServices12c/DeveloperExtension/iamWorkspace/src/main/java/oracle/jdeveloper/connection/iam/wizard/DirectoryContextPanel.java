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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   DirectoryContextPanel.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DirectoryContextPanel.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.wizard;

import java.beans.VetoableChangeListener;

import java.awt.Component;

import javax.swing.JPanel;

import oracle.ide.panels.Traversable;
import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.connection.iam.model.DirectoryEntry;
import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.navigator.context.DirectoryContext;

//////////////////////////////////////////////////////////////////////////////
// class DirectoryContextPanel
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
public abstract class DirectoryContextPanel extends    JPanel
                                            implements Traversable
                                            ,          VetoableChangeListener {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The DATA key should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   */
  public static final String SCHEMA           = "ods/schema";
  public static final String CONTEXT          = "ods/context";
  public static final String NAME             = "ods/name";
  public static final String ENTRY            = "ods/entry";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3117268385609572735")
  private static final long  serialVersionUID = 2499703802781602168L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  DirectoryEntry             entry;
  DirectorySchema            schema;
  DirectoryContext           context;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryContextPanel</code> panel that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected DirectoryContextPanel() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialFocus
  /**
   ** Returns the {@link Component} that will get the focus if the dialog window
   ** containing this panel is opened.
   **
   ** @return                    the {@link Component} that will get the focus.
   **                            <br>
   **                            Allowed object is {@link Component}.
   */
  public abstract Component initialFocus();

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHelpID (Traversable)
  /**
   ** Returns the context-sensitive help topic ID to use for this
   ** {@link Traversable}.
   ** <br>
   ** A null <code>return</code> value means that the {@link Traversable}
   ** implementation doesn't specify a help topic ID. However, there are other
   ** ways that a help topic ID could get associated with a {@link Traversable}.
   **
   ** @return                    the context-sensitive help topic ID to use for
   **                            this {@link Traversable}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getHelpID() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getComponent (Traversable)
  /**
   ** Normally, the {@link Traversable} class will itself be the UI Component.
   ** <br>
   ** Therefore, <code>getComponent()</code> typically just returns this. In
   ** this situation the <code>getComponent()</code> method then is simply a
   ** means of avoiding a type cast.
   ** <p>
   ** In other cases, it would be useful to have the ability to return a
   ** different Component based on the contents of the
   ** {@link TraversableContext} that is passed to the
   ** {@link #onEntry(TraversableContext)} method. UI containers (e.g. property
   ** dialogs and wizards) that are designed to use the {@link Traversable}
   ** interface must call the {@link #onEntry(TraversableContext)} method before
   ** calling <code>getComponent()</code>. This allows a {@link Traversable}
   ** implementation to have the opportunity to configure the UI Component or
   ** even create a new one before it is displayed.
   ** <p>
   ** In either situation, the implementation should strive to return the same
   ** Component instance as often as possible rather than creating a new
   ** instance becaues the UI container will call this method frequently.
   **
   ** @return                    the UI Component that the user interacts with
   **                            for creating or editing an object.
   **                            <br>
   **                            Possible object is {@link Component}.
   */
  @Override
  public final Component getComponent() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExitTransition (Traversable)
  /**
   ** Returns the exit transition that can be used by a
   ** {@link Traversable}-aware wizard.
   ** <br>
   ** The wizard can use the exit transition to direct the user through an
   ** alternate or streamlined set of panels based on their current input.
   ** <p>
   ** If the {@link Traversable} implementation does not support multiple exit
   ** transitions or is not used in a wizard, then this method should just
   ** return <code>null</code>.
   **
   ** @return                    the exit transition for the {@link Traversable}
   **                            that is used by dynamic interview-style wizards
   **                            to determine the next course of action. A
   **                            {@link Traversable} class that does not support
   **                            multiple possible transitions should just
   **                            return <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  @Override
  public final Object getExitTransition() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onEntry (Traversable)
  /**
   ** This method is called when the <code>Traversable</code> is being entered.
   ** <p>
   ** The data that the <code>Traversable</code> should use to populate its UI
   ** components comes from the specified {@link TraversableContext}.
   ** <p>
   ** When the same <code>Traversable</code> is entered more than once in the
   ** course of interacting with the user, the <code>Traversable</code> needs to
   ** reload the data directly from the {@link TraversableContext} rather than
   ** caching data objects. Some reasons for this include:
   ** <ul>
   **   <li>Other <code>Traversable</code> may edit the data objects or even
   **       replace them.
   **   <li>The same <code>Traversable</code> instance may be used for editing
   **       multiple different instances of the same object type.
   ** </ul>
   ** Loading data directly from the {@link TraversableContext} is the best way
   ** to ensure that the <code>Traversable</code> will not be editing the wrong
   ** data.
   ** <p>
   ** The <code>Traversable</code> should not even cache references to data
   ** objects between invocations of onEntry and
   ** {@link #onExit(TraversableContext)} because the UI container is not
   ** required to guarantee that the references will be identical.
   **
   ** @param  context            the data wrapper where the
   **                            <code>Traversable</code> locates the data that
   **                            it needs to populate the UI.
   */
  @Override
  public void onEntry(final TraversableContext context) {
    // initialize instance attributes
    this.schema  = (DirectorySchema)context.get(SCHEMA);
    this.context = (DirectoryContext)context.get(CONTEXT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create a {@link DirectoryContextCreate} panel that
   ** populates its data object from the specified {@link TraversableContext}
   ** passed by the dialog launcher at {@link #onEntry(TraversableContext)}.
   **
   ** @return                    the {@link DirectoryContextCreate} panel in
   **                            sync with the data provided in the
   **                            {@link TraversableContext}.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContextPanel</code>.
   */
  public static DirectoryContextPanel create() {
    return new DirectoryContextCreate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   similar
  /**
   ** Factory method to create a {@link DirectoryContextSimilar} panel that
   ** populates its data object from the specified {@link TraversableContext}
   ** passed by the dialog launcher at {@link #onEntry(TraversableContext)}.
   **
   ** @return                    the {@link DirectoryContextSimilar} panel in
   **                            sync with the data provided in the
   **                            {@link TraversableContext}.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContextPanel</code>.
   */
  public static DirectoryContextPanel similar() {
    return new DirectoryContextSimilar();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rename
  /**
   ** Factory method to create a {@link DirectoryContextRename} panel that
   ** populates its data object from the specified {@link TraversableContext}
   ** passed by the dialog launcher at {@link #onEntry(TraversableContext)}.
   **
   ** @return                    the {@link DirectoryContextRename} panel in
   **                            sync with the data provided in the
   **                            {@link TraversableContext}.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContextPanel</code>.
   */
  public static DirectoryContextPanel rename() {
    return new DirectoryContextRename();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   move
  /**
   ** Factory method to create a {@link DirectoryContextMove} panel that
   ** populates its data object from the specified {@link TraversableContext}
   ** passed by the dialog launcher at {@link #onEntry(TraversableContext)}.
   **
   ** @return                    the {@link DirectoryContextMove} panel in
   **                            sync with the data provided in the
   **                            {@link TraversableContext}.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContextPanel</code>.
   */
  public static DirectoryContextPanel move() {
    return new DirectoryContextMove();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Factory method to create a {@link DirectoryContextExport} panel that
   ** populates its data object from the specified {@link TraversableContext}
   ** passed by the dialog launcher at {@link #onEntry(TraversableContext)}.
   **
   ** @return                    the {@link DirectoryContextExport} panel in
   **                            sync with the data provided in the
   **                            {@link TraversableContext}.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContextPanel</code>.
   */
  public static DirectoryContextPanel marshal() {
    return new DirectoryContextExport();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link DirectoryContextImport} panel that
   ** populates its data object from the specified {@link TraversableContext}
   ** passed by the dialog launcher at {@link #onEntry(TraversableContext)}.
   **
   ** @return                    the {@link DirectoryContextImport} panel in
   **                            sync with the data provided in the
   **                            {@link TraversableContext}.
   **                            <br>
   **                            Possible object is
   **                            <code>DirectoryContextPanel</code>.
   */
  public static DirectoryContextPanel unmarshal() {
    return new DirectoryContextImport();
  }
}
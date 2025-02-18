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

    File        :   EndpointFolder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointFolder.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import oracle.ide.util.Assert;

import oracle.ide.model.Node;
import oracle.ide.model.Folder;
import oracle.ide.model.Element;
import oracle.ide.model.DefaultElement;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointFolder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
public abstract class EndpointFolder extends    EndpointElement
                                     implements Folder {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final State[] state    = { State.NOTLOADED };
  private List<Element> children = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum State
  // ~~~~ ~~~~~
  /**
   ** Superclass {@link Node} has a similare enum but unfortunately it is
   ** declared private.
   ** <br>
   ** What kind of paranoid is that. An enum isn't inheritable hence never
   ** mutable.
   */
  public static enum State {NOTLOADED, LOADING, LOADED;}

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>EndpointFolder</code> that belongs to the specified
   ** {@link Manageable}.
   **
   ** @param  parent             the owner of this {@link Node}.
   **                            <br>
   **                            Allowed object is {@link EndpointElement}.
   ** @param  manageable         the {@link Manageable} to display in the UI of
   **                            this {@link Folder}.
   **                            <br>
   **                            Allowed object is {@link Manageable}.
   */
  public EndpointFolder(final EndpointElement parent, final Manageable manageable) {
    // ensure inheritance
    super(parent, manageable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mayHaveChildren (overridden)
  /**
   ** This method is part of the {@link Element} interface to provide a
   ** convenient way of determining whether an object may have children without
   ** having to test the object's type with the instanceof operator or having to
   ** downcast to a more specific type.
   ** <p>
   ** An implementation of {@link Element} that represents a leaf in a tree
   ** structure should return <code>false</code> from this method.
   ** <br>
   ** An implementation of {@link Element} that could represent a non-leaf in a
   ** tree structure should return <code>true</code> from this method, even if
   ** it does not currently contain any children.
   ** <p>
   ** This implementation always returns <code>true</code>.
   **
   ** @return                    <code>true</code> if this {@link Element} may
   **                            contain child {@link Element}s.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean mayHaveChildren() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getChildren
  /**
   ** This method is part of the {@link Element} interface to provide a
   ** convenient way of getting an {@link Iterator} over any contained child
   ** {@link Element}s without having to test the object's type with the
   ** instanceof operator or having to downcast to a more specific type.
   ** <p>
   ** An implementation of {@link Element} that represents a leaf in a tree
   ** structure should return <code>null</code> from this method.
   ** <br>
   ** An implementation of {@link Element} that could represent a non-leaf in a
   ** tree structure should return either an {@link Iterator} over the child
   ** {@link Element}s or <code>null</code> if there are no children.
   **
   ** @return                    an {@link Iterator} over any child
   **                            {@link Element}s contained by this
   **                            {@link Element}. If there are no children,
   **                            <code>null</code> is returned.
   **                            <br>
   **                            Possible object is {@link Iterator}.
   */
  @Override
  public Iterator getChildren() {
    synchronized (this.state) {
      if (state() == State.NOTLOADED) {
        if (SwingUtilities.isEventDispatchThread()) {
          populateBackground();
        }
        else {
          populateForeground();
        }
      }
      return Collections.unmodifiableList(this.children).iterator();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Determines if the state can be changed from <code>current</code> to
   ** <code>value</code>.
   **
   ** @param  value              the new state to set for the navigator node.
   **                            <br>
   **                            Allowed object is {@link State}.
   ** @param  current            the current state to the navigator node may
   **                            have.
   **                            <br>
   **                            Allowed object is {@link State}.
   **
   ** @return                    <code>true</code> if the state could be changed
   **                            from <code>current</code> to
   **                            <code>value</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected final boolean state(final State value, final State current) {
    synchronized (this.state) {
      if (state() == current) {
        state(value);
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Sets the current state of the navigator node.
   **
   ** @param  value              the state to set for the navigator node.
   **                            <br>
   **                            Allowed object is {@link State}.
   **
   ** @return                    the previous state of the navigator node.
   **                            <br>
   **                            Possible object is {@link State}.
   */
  private State state(final State value) {
    synchronized (this.state) {
      State oldState = this.state[0];
      this.state[0] = value;
      return oldState;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Returns the current state of the navigator node.
   **
   ** @return                    the current state of the navigator node.
   **                            <br>
   **                            Possible object is {@link State}.
   */
  private State state() {
    synchronized (this.state) {
      return this.state[0];
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Folder)
  /**
   ** Returns the current number of children in the folder.
   **
   ** @return                    the current number of children in the folder.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int size() {
    return populateForeground().size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canAdd (Folder)
  /**
   ** Other classes can call this method to determine whether the given
   ** {@link Element} can be added to this {@link Folder}.
   **
   ** @param  element            the {@link Element} that is about to be added
   **                            to this {@link Folder}.
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link Element} can be added to this
   **                            {@link Folder}; <code>false</code> if the
   **                            {@link Element} cannot be added.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean canAdd(final Element element) {
    return element != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (Folder)
  /**
   ** Appends a child {@link Element} to the end of the Folder.
   **
   ** @param  child              the child {@link Element} to the end of this
   **                            {@link Folder}.
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link Element} is added to this
   **                            {@link Folder}; <code>false</code> if the
   **                            {@link Element} is not added.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean add(final Element child) {
    Assert.precondition(child != null);
    return populateForeground().add(child);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canRemove (Folder)
  /**
   ** Other classes can call this method to determine whether the specified
   ** {@link Element} can be removed from this {@link EndpointFolder}.
   **
   ** @param  element            the {@link Element} that is about to be removed
   **                            to this {@link Folder}.
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link Element} can be removed to this
   **                            {@link Folder}; <code>false</code> if the
   **                            {@link Element} cannot be removed.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean canRemove(final Element element) {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Folder)
  /**
   ** Removes the specified child {@link Element}.
   ** <br>
   ** If the child object appears more than once, only the first instance is
   ** removed.
   **
   ** @param  child              the child {@link Element} to remove.
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link Element} is removed to this
   **                            {@link Folder}; <code>false</code> if the
   **                            {@link Element} is not removed.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean remove(final Element child) {
    Assert.precondition(child != null);
    return populateForeground().remove(child);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAll (Folder)
  /**
   ** Removes all children from the folder.
   */
  @Override
  public void removeAll() {
    populateForeground().clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsChild (Folder)
  /**
   ** Returns <code>true</code> if the folder contains the specified child
   ** {@link Element}; returns <code>false</code> otherwise.
   **
   ** @param  child              the child {@link Element} to verify.
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link Element} is contained to this
   **                            {@link Folder}; <code>false</code> if the
   **                            {@link Element} is not contained.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean containsChild(final Element child) {
    return populateForeground().contains(child);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Performs all action to refresh an element.
   */
  public void refresh() {
    if (!mayHaveChildren())
      return;

    synchronized (this.state) {
      if (state() != State.LOADED) {
        return;
      }
    }
    final Runnable refresher = new Runnable() {
      public void run() {
        refreshFolder();
      }
    };
    if (SwingUtilities.isEventDispatchThread()) {
      loading();
      EndpointTaskProcessor.instance().execute("Refreshing " + getShortLabel(), refresher);
    }
    else {
      refresher.run();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshFolder
  /**
   ** Performs all action to refresh an element.
   */
  protected void refreshFolder() {
    assert (!SwingUtilities.isEventDispatchThread());
    reloadChildren();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reloadChildren
  /**
   ** Performs all action to refresh an element.
   */
  protected void reloadChildren() {
    state(State.NOTLOADED, State.LOADED);
    populateForeground();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateBackground
  /**
   ** Performs all actions to load child elements in the background.
   ** <br>
   ** During the activities to load the children a loading node is displayed
   ** until all child are loaded.
   */
  protected void populateBackground() {
    final Runnable loader = new Runnable() {
      public void run() {
        populateForeground();
      }
    };
    waitCursor().show();
    loading();
    EndpointTaskProcessor.instance().execute("Fetching " + getShortLabel(), loader);
    waitCursor().hide();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateForeground
  /**
   ** Performs all actions to load child elements.
   **
   ** @return                    the collection of child elemments belonging to
   **                            this {@link Folder}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Element}.
   */
  protected List<Element> populateForeground() {
    synchronized (this.state) {
      if (state() == State.LOADED) {
        return this.children;
      }

      if (state(State.LOADING, State.NOTLOADED)) {
        String threadName = getShortLabel() + "loader";
        Thread loader = new Thread(threadName) {
          public void run() {
            List<Element> children = new ArrayList<Element>();
            try {
              children = populate();
            }
            finally {
              synchronized (EndpointFolder.this.state) {
                EndpointFolder.this.children = children;
                EndpointFolder.this.state(EndpointFolder.State.LOADED);
                EndpointFolder.this.state.notifyAll();
              }
              fireStructureChanged();
            }
          }
        };
        loader.start();
      }

      try {
        this.state.wait();
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this.children;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Performs all actions to load child elements.
   **
   ** @return                    the collection of child elemments belonging to
   **                            this {@link Folder}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Element}.
   */
  protected List<Element> populate() {
    final List<Element> children = new ArrayList<Element>();
    return children;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loading
  /**
   ** free allocated resources to make it easier for the garbage collector and
   ** add a temporary child to visualize the process.
   */
  private void loading() {
    synchronized (this.state) {
      // free allocated resources to make it easier for the garbage collector
      if (this.children != null) {
        this.children.clear();
        this.children = null;
      }
      this.children = new ArrayList<Element>();
      this.children.add(new DefaultElement() {
        public String getShortLabel() {
          return Bundle.string(Bundle.CONNECTION_BUSY_TEXT);
        }
        public Icon getIcon() {
          return Bundle.icon(Bundle.CONNECTION_BUSY_ICON);
        }
      });
    }
    fireStructureChanged();
  }
}
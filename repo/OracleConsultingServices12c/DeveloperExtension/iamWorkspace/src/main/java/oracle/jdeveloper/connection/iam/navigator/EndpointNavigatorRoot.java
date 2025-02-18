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

    File        :   EndpointNavigatorRoot.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointNavigatorRoot.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Collections;
import java.util.LinkedHashMap;

import javax.naming.Binding;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.NamespaceChangeListener;

import oracle.adf.rc.core.RCSession;

import oracle.adf.share.connection.ConnectionType;

import oracle.ide.model.Element;

import oracle.jdeveloper.connection.iam.navigator.context.Manageable;

import oracle.jdeveloper.connection.iam.navigator.node.EndpointFolder;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointNavigatorRoot
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** A navigator root node for the custom navigator.
 ** <br>
 ** This node aggregates all connection of a specific type as
 ** {@link EndpointFolder} nodes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointNavigatorRoot extends    EndpointFolder
                                            implements Manageable.Composite
                                            ,          Manageable.Refreshable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-907078418126228051")
  private static final long serialVersionUID = -6747702924079206604L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String                         displayName;
  protected final transient State                listener        = new State();
  protected final transient Map<String, Element> contexts        = new LinkedHashMap<String, Element>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class State
  // ~~~~~ ~~~~~
  /**
   ** An <code>NamespaceChangeListener</code> observing the state changes of an
   ** element.
   */
  class State implements NamespaceChangeListener {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>State</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    State() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: namingExceptionThrown (NamingListener)
    /**
     ** Called when a naming exception is thrown while attempting to fire a
     ** <code>NamingEvent</code>.
     **
     ** @param  event             the nonnull event.
     */
    @Override
    public void namingExceptionThrown(final NamingExceptionEvent event) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: objectAdded (NamespaceChangeListener)
    /**
     ** Called when an object has been added.
     ** <p>
     ** The binding of the newly added object can be obtained using
     ** <code>event.getNewBinding()</code>.
     **
     ** @param  event             the nonnull event.
     **
     ** @see    NamingEvent#OBJECT_ADDED
     */
    @Override
    public void objectAdded(final NamingEvent event) {
      if (!applicableProvider(event.getNewBinding())) {
        return;
      }
      EndpointNavigatorRoot.this.createContext();
      String connId = event.getNewBinding().getName();
      EndpointNavigatorRoot.this.add(connId);
      reloadChildren();
      fireStructureChanged();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: objectRemoved (NamespaceChangeListener)
    /**
     ** Called when an object has been removed.
     ** <p>
     **  The binding of the removed object can be obtained using
     ** <code>event.getNewBinding()</code>.
     **
     ** @param  event             the nonnull event.
     **
     ** @see    NamingEvent#OBJECT_REMOVED
     */
    @Override
    public void objectRemoved(final NamingEvent event) {
      if (!applicableProvider(event.getOldBinding())) {
        return;
      }

      String connId = event.getOldBinding().getName();
      EndpointNavigatorRoot.this.remove(connId);
      reloadChildren();
      fireStructureChanged();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: objectRenamed (NamespaceChangeListener)
    /**
     ** Called when an object has been renamed.
     ** <p>
     ** The binding of the renamed object can be obtained using
     ** <code>event.getNewBinding()</code>. Its old binding (before the rename)
     ** can be obtained using <code>event.getOldBinding()</code>.
     ** <br>
     ** One of these may be <code>null</code> if the old/new binding was outside
     ** the scope in which the listener has registered interest.
     **
     ** @param  event             the nonnull event.
     **
     ** @see    NamingEvent#OBJECT_RENAMED
     */
    @Override
    public void objectRenamed(final NamingEvent event) {
      if (!applicableProvider(event.getNewBinding())) {
        return;
      }

      String connId = event.getOldBinding().getName();
      EndpointNavigatorRoot.this.remove(connId);
      connId = event.getNewBinding().getName();
      EndpointNavigatorRoot.this.add(connId);
      reloadChildren();
      fireStructureChanged();
    }

    private boolean applicableProvider(final Binding binding) {
      return ConnectionType.class.equals(binding.getClassName());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>EndpointNavigatorRoot</code> with name that belongs
   ** to the specified {@link RCSession}.
   **
   ** @param   name              the name that represents the
   **                            <code>EndpointNavigatorRoot</code> in the UI.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected EndpointNavigatorRoot(final String name) {
    // ensure inheritance
    super(null, null);

    // initialize instance state
//    manageable(this);

    // initialize instance attributes
    this.displayName = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session
  /**
   ** Returns the session established to the Resource Palette.
   **
   ** @return                    the session established to the Resource
   **                            Palette.
   */
  public abstract RCSession session();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getShortLabel (overridden)
  /**
   ** Provides the label that represents the element in a navigator.
   ** <p>
   ** The label should be a noun or noun phrase naming the item that is created
   ** by this element and should omit the word "new".
   ** <br>
   ** Examples: "Java Class", "Java Interface", "XML Document",
   ** "Database Connection".
   **
   ** @return                    the human readable label of the element.
   */
  @Override
  public String getShortLabel() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLongLabel (overridden)
  /**
   ** Provides the long label that represents the catalog element.
   **
   ** @return                    the human readable long label of the element.
   */
  @Override
  public String getLongLabel() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isIdeDefault
  /**
   ** Determines if this context is a default context belonging to the IDE or
   ** if its a application specific context.
   ** <br>
   ** Return always <code>false</code>.
   **
   ** @return                    <code>true</code> if this context is a default
   **                            context belonging to the IDE or if its a
   **                            application specific context.
   */
  public boolean isIdeDefault() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpointNavigatorRootList
  public Collection<EndpointNavigatorRoot> endpointNavigatorRootList() {
    createContext();
    final List<EndpointNavigatorRoot> ret = new ArrayList<EndpointNavigatorRoot>();
    synchronized (this.contexts) {
      for (Element e : this.contexts.values()) {
        if ((e instanceof EndpointNavigatorRoot)) {
          ret.add((EndpointNavigatorRoot)e);
        }
      }
    }
    Collections.sort(ret, new Comparator<EndpointNavigatorRoot>() {
      public int compare(EndpointNavigatorRoot o1, EndpointNavigatorRoot o2) {
        int i = o1.getShortLabel().compareToIgnoreCase(o2.getShortLabel());
        if (i == 0) {
          i = o1.getShortLabel().compareTo(o2.getShortLabel());
        }
        return i;
      }
    });
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManageables (Manageable.Composite)
  @Override
  public List<Manageable> getManageables() {
    final List<Manageable> manageables = new ArrayList<Manageable>();
    for (EndpointNavigatorRoot a : endpointNavigatorRootList()) {
      manageables.add(a.manageable());
    }
    return manageables;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createContext
  /**
   ** Initialize the context of the navigator root node by population the
   ** connection descriptors.
   */
  protected abstract void createContext();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  protected abstract void add(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  protected abstract void remove(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate (overridden)
  /**
   ** Performs all actions to load child elements.
   **
   ** @return                    the collection of child elemments belonging to
   **                            this {@link EndpointFolder}.
   */
  @Override
  protected List<Element> populate() {
    synchronized(this.contexts) {
      createContext();
      return new ArrayList<Element>(this.contexts.values());
    }
  }
}
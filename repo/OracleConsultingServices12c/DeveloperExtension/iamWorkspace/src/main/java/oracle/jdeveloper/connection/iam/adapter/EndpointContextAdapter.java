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

    File        :   EndpointContextAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointContextAdapter.


    Revisions        Date       Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.adapter;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.naming.Referenceable;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;

import javax.naming.event.EventContext;
import javax.naming.event.NamingListener;

import oracle.adf.share.jndi.AdfJndiContext;
import oracle.adf.share.jndi.ConnectionException;

import oracle.adf.share.connection.ConnectionType;

import oracle.ide.util.Assert;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class EndpointContextAdapter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Adapter class specialized to pull a specific connection type from the
 ** JNDI tree.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public abstract class EndpointContextAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final AdfJndiContext context;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class Pair
  // ~~~~~ ~~~~
  class Pair implements Map.Entry<String, Referenceable> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    String        k;
    Referenceable v;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    Pair(final String k, final Referenceable v) {
      this.k = k;
      this.v = v;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getKey (Map.Entry)
    /**
     ** Returns the key corresponding to this entry.
     **
     ** @return                  the key corresponding to this entry.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String getKey() {
      return this.k;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setValue (Map.Entry)
    /**
     ** Replaces the value corresponding to this entry with the specified value
     ** (optional operation).
     ** <br>
     ** (Writes through to the map.)
     ** <br>
     ** The behavior of this call is undefined if the mapping has already been
     ** removed from the map (by the iterator's <code>remove</code> operation).
     **
     ** @param  value            the new value to be stored in this entry.
     **                          <br>
     **                          Allowed object is {@link Referenceable}.
     **
     ** @return                  the old value corresponding to the entry.
     **                          <br>
     **                          Possible object is {@link Referenceable}.
     */
    public Referenceable setValue(final Referenceable value) {
      final Referenceable o = this.v;
      this.v = value;
      return o;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getValue (Map.Entry)
    /**
     ** Returns the value corresponding to this entry.
     ** <br>
     ** If the mapping has been removed from the backing map (by the iterator's
     ** <code>remove</code> operation), the results of this call are undefined.
     **
     ** @return                  the value corresponding to this entry.
     **                          <br>
     **                          Possible object is {@link Referenceable}.
     */
    @Override
    public Referenceable getValue() {
      return this.v;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EndpointContextAdapter</code> to handle the specified
   ** context.
   **
   ** @param  context            the {@link AdfJndiContext} context to handle.
   **                            <br>
   **                            Allowed object is {@link AdfJndiContext}.
   */
  protected EndpointContextAdapter(final AdfJndiContext context) {
    this.context = context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionNames
  /**
   ** Returns the collection of connection names belonging to a specific
   ** provider class.
   **
   ** @return                    the collection of connection names belonging to
   **                            a specific provider class.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public abstract List<String> connectionNames();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionProviders
  /**
   ** Returns the collection of connection providers belonging to a specific
   ** {@link ConnectionType}.
   **
   ** @return                    the collection of connection providers
   **                            belonging to a specific {@link ConnectionType}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Map.Entry}.
   */
  public abstract List<Map.Entry<String, Referenceable>> connectionProviders();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
  /**
   ** Returns the {@link Referenceable} associated with the Resource Palette
   ** that matches the specified identifier.
   ** <p>
   ** The class itself cannot be used as a matching criteria because it can be
   ** deployed in a different bundle hence the OSGi class loading behavior
   ** prevents to access any method of such a class.
   **
   ** @param  identifier         the identifier of {@link Referenceable} to
   **                            match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Referenceable} associated with the
   **                            Resource Palette that matches the specified
   **                            identifier.
   **                            <br>
   **                            Possible object is {@link Referenceable}.
   */
  public Referenceable find(final String identifier) {
    try {
      return (Referenceable)this.context.lookup(identifier);
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bind
  /**
   ** Binds the name to an {@link Referenceable}.
   **
   ** @param  name               the name to bind; may not be empty;
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  endpoint           the {@link Referenceable} to bind; possibly
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Referenceable}.
   **
   ** @throws ConnectionException if bind the name to an {@link Referenceable}
   **                             fails.
   */
  public void bind(final String name, final Referenceable endpoint) {
    try {
      this.context.bind(name, endpoint);
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unbind
  /**
   ** Unbinds the named object.
   **
   ** @param  name               the name to bind; may not be empty;
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ConnectionException if unbind the name  fails.
   */
  public void unbind(final String name) {
    try {
      this.context.unbind(name);
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   save
  /**
   ** Save the context.
   **
   ** @throws ConnectionException if context could not be saved.
   */
  public void save() {
    try {
      this.context.save();
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage(), e);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public void addContextListener(final NamingListener listener) {
    Assert.check(this.context instanceof EventContext);
    EventContext eventContext = (EventContext)this.context;
    try {
      eventContext.addNamingListener(StringUtility.EMPTY, 1, listener);
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage());
    }
  }

  public void removeContextListener(NamingListener contextListener) {
    Assert.check(this.context instanceof EventContext);
    EventContext eventContext = (EventContext)this.context;
    try {
      eventContext.removeNamingListener(contextListener);
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionNames
  /**
   ** Returns the collection of connection names belonging to a specific
   ** provider class.
   **
   ** @param  provider           the {@link Class} type of the provider to
   **                            lookup the connection names for.
   **                            <br>
   **                            Allowed object is {@link Class} extending
   **                            type {@link Referenceable}.
   **
   ** @return                    the collection of connection names belonging to
   **                            a specific provider class.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  protected List<String> connectionNames(final Class<? extends Referenceable> provider) {
    final List<String> names = new ArrayList<String>();
    try {
      final NamingEnumeration ncpe = this.context.list(StringUtility.EMPTY);
      while (ncpe.hasMoreElements()) {
        final NameClassPair pair = (NameClassPair)ncpe.nextElement();
        if (provider.getName().equals(pair.getClassName()))
          names.add(pair.getName());
      }
    }
    catch (NamingException e) {
      throw new ConnectionException(e.getMessage());
    }
    return names;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectionProviders
  /**
   ** Returns the collection of connection providers belonging to a specific
   ** {@link ConnectionType}.
   **
   ** @param  type               the {@link ConnectionType} of the provider to
   **                            lookup the connection providers for.
   **                            <br>
   **                            Allowed object is {@link Class} extending
   **                            type {@link Referenceable}.
   **
   ** @return                    the collection of connection providers
   **                            belonging to a specific {@link ConnectionType}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Map.Entry}.
   */
  protected List<Map.Entry<String, Referenceable>> connectionProviders(final Class<? extends Referenceable> type) {
    List<Map.Entry<String, Referenceable>> providers = new ArrayList<Map.Entry<String, Referenceable>>();
    try {
      NamingEnumeration ncpe = this.context.list(StringUtility.EMPTY);
      while (ncpe.hasMoreElements()) {
        final NameClassPair pair = (NameClassPair)ncpe.nextElement();
        if (type.getName().equals(pair.getClassName())) {
          final Object o = this.context.lookup(pair.getName());
          assert ((o instanceof Referenceable));
          providers.add(new Pair(pair.getName(), (Referenceable)o));
        }
      }
    }
    catch (NamingException e) {
      throw new ConnectionException("Error trying to list Endpints", e);
    }
    return providers;
  }
}
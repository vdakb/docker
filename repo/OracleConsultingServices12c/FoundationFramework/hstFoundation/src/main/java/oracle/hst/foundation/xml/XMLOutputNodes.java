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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared XML Stream Facilities

    File        :   XMLOutputNodes.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLOutputNodes.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.util.Iterator;
import java.util.LinkedHashMap;

////////////////////////////////////////////////////////////////////////////////
// class XMLOutputNodes
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>XMLOutputNodes</code> is used to collect attribute nodes for an
 ** output node. This will create a generic node to add to the collection. The
 ** nodes created will be used by the output node to write attributes for an
 ** element.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
class XMLOutputNodes extends    LinkedHashMap<String, XMLOutputNode>
                     implements XMLNodes<XMLOutputNode> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2598816896156952406")
  private static final long   serialVersionUID = 6549395500252012599L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the source node that this node collection belongs to. */
  private final XMLOutputNode source;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLOutputNodes</code> object.
   ** <p
   ** This is used to create a node collection that itself is used to create and
   ** collect nodes, which will be used as attributes for an output element.
   */
  public XMLOutputNodes(final XMLOutputNode source) {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.source = source;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   self (XMLNodes)
  /**
   ** This is used to acquire the actual node this collection represents.
   ** <p>
   ** The source node provides further details on the context of the node, such
   ** as the parent name, the namespace, and even the value in the node.
   ** <br>
   ** Care should be taken when using this.
   **
   ** @return                    the node that this collection represents.
   */
  @Override
  public XMLOutputNode self() {
    return this.source;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (XMLNodes)
  /**
   ** This is used to get the name of the element that owns the nodes for the
   ** specified collection. This can be used to determine which element the node
   ** collection belongs to.
   **
   ** @return                    the name of the owning element.
   */
  @Override
  public String name() {
    return this.source.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (XMLNodes)
  /**
   ** This is used to acquire the {@link XMLNode} mapped to the given name.
   ** <p>
   ** This returns a name value pair that represents either an attribute or
   ** element. If no node is mapped to the specified name then this method will
   ** return <code>null</code>.
   **
   ** @param  name               the name of the node to retrieve.
   **
   ** @return                    the node mapped to the given name.
   */
  @Override
  public XMLOutputNode lookup(final String name) {
    return super.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put (XMLNodes)
  /**
   ** This is used to add a new {@link XMLNode} to the collection.
   ** <p>
   ** The type of node that is created an added is left up to the collection
   ** implementation. Once a node is created with the name value pair it can be
   ** retrieved and used.
   **
   ** @param  name               the name of the node to be created.
   ** @param  value              the value to be given to the node.
   **
   ** @return                    the node that has been added to the collection.
   */
  @Override
  public XMLOutputNode put(final String name, final String value) {
    final XMLOutputNode node = new XMLOutputAttribute(this.source, name, value);
    if (this.source != null)
      put(name, node);

    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (XMLNodes)
  /**
   ** This is used to remove the {@link XMLNode} mapped to the given name.
   ** <p>
   ** This returns a name value pair that represents either an attribute or
   ** element. If no node is mapped to the specified name then this method will
   ** return <code>null</code>.
   **
   ** @param  name               the name of the node to remove.
   **
   ** @return                    the node mapped to the given name.
   */
  @Override
  public XMLOutputNode remove(final String name) {
    return super.remove(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty (XMLNodes)
  /**
   ** Returns <code>true</code> if the collection contains no elements.
   **
   ** @return                    <code>true</code> if the collection contains no
   **                            elements.
   */
  @Override
  public boolean empty() {
    return super.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iteratable)
  /**
   ** This returns an iterator for the names of all the nodes in this
   ** <code>XMLNodes</code> collection.
   ** <p>
   ** This allows the names to be iterated within a for each loop in order to
   ** extract nodes.
   **
   ** @return                   the names of the nodes in the collection.
   */
  @Override
  public Iterator<String> iterator() {
    return keySet().iterator();
  }
}
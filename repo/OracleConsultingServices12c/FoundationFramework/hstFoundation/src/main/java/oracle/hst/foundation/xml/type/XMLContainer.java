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

    File        :   XMLContainer.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    XMLContainer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml.type;

import java.util.Iterator;
import java.util.LinkedHashMap;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.xml.XMLNode;
import oracle.hst.foundation.xml.XMLNodes;
import oracle.hst.foundation.xml.XMLException;

////////////////////////////////////////////////////////////////////////////////
// class XMLContainer
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The base object for both Element and Document.
 ** <br>
 ** The children of a Container can be any type of XMLNode.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class XMLContainer extends    LinkedHashMap<String, XMLNode>
                          implements XMLNodes<XMLNode>
                          ,          XMLNode {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2437381582819640778")
  private static final long serialVersionUID = 4693921687416197561L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the source node that this node collection belongs to. */
  private final AbstractNode source;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLContainer</code> object.
   ** <p>
   ** This is used to create a node collection that is used to create and
   ** collect nodes, which will be used as attributes for an output element.
   **
   ** @param  source             the root node for collecting.
   */
  public XMLContainer(final AbstractNode source) {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.source = source;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (XMLNode)
  /**
   ** Returns the name of the node that this represents.
   ** <p>
   ** This is an immutable property and should not change for any node.
   **
   ** @return                    the name of the node that this represents.
   */
  @Override
  public String name() {
    return this.source.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: parent (XMLOutputNode)
  /**
   ** This is used to acquire the <code>Node</code> that is the parent of this
   ** node.
   ** <p>
   ** This will return the node that is the direct parent of this node and
   ** allows for siblings to make use of nodes with their parents if required.
   **
   ** @return                  the parent node for this node.
   */
  @Override
  public final XMLNode parent() {
    return this.source.parent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value (XMLNode)
  /**
   ** Returns the value for the node that this represents.
   ** <p>
   ** The returned value is the concatenation of the string value of all child
   ** nodes.
   ** <p>
   ** This is a modifiable property for the node and can be changed.
   **
   ** @return                    the value for this node instance.
   **
   ** @throws XMLException       if there is a problem getting the value.
   */
  @Override
  public String value()
    throws XMLException {

    if (isEmpty()) {
      return SystemConstant.EMPTY;
    }

    StringBuilder sb = new StringBuilder();
    for (Iterator<XMLNode> i = this.values().iterator(); i.hasNext(); ) {
      XMLNode child = i.next();
      sb.append(child.value());
    }
    return sb.toString();
  }

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
  public XMLNode self() {
    return this.source;
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
  public XMLNode lookup(final String name) {
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
  public XMLNode put(final String name, final String value) {
    final XMLNode node = new XMLAttribute(this.source, name, value);
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
  public XMLNode remove(final String name) {
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
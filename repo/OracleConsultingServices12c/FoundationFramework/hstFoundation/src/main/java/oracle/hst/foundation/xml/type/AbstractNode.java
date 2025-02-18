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

    File        :   AbstractNode.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractNode.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml.type;

import oracle.hst.foundation.xml.XMLNode;

////////////////////////////////////////////////////////////////////////////////
// class AbstractNode
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
public abstract class AbstractNode implements XMLNode {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2593035011066517630")
  private static final long serialVersionUID = 2170951686896619419L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the localname (non-qualified) for this {@link XMLNode}. */
  private final String      name;

  /** the node type being created. */
  private final int         type;

  /** the parent {@link XMLNode} that this {@link XMLNode} belongs to. */
  private XMLNode           parent = null;

  /** the namespace to which this {@link XMLNode} belongs. */
  private String            namespace;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Creates a new AbstractNode
   **
   ** @param  namespace          the namespace URI for this node.
   **                            May be <code>null</code>.
   ** @param  name               the local-name of this node.
   **                            May be <code>null</code>.
   ** @param  type               the node type being created.
   */
  AbstractNode(final String namespace, final String name, final int type) {
    // ensure inheritance
    this(null, namespace, name, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Creates a new AbstractNode
   **
   ** @param  namespace          the namespace URI for this node.
   **                            May be <code>null</code>.
   ** @param  name               the local-name of this node.
   **                            May be <code>null</code>.
   ** @param  type               the node type being created.
   */
  AbstractNode(final XMLNode parent, final String namespace, final String name, final int type) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.parent    = parent;
    this.namespace = namespace;
    this.name      = name;
    this.type      = type;
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
    return this.name;
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
    return this.parent;
  }
}
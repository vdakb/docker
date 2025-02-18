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

    File        :   XMLElement.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    XMLElement.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml.type;

import oracle.hst.foundation.xml.XMLNode;
import oracle.hst.foundation.xml.XMLException;

////////////////////////////////////////////////////////////////////////////////
// abstract class XMLFragment
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** The base object for both Element and Document.
 ** <br>
 ** The children of a Container can be any type of XMLNode.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public abstract class XMLFragment extends AbstractNode {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-255852413152431421")
  private static final long    serialVersionUID = 2652101891044845858L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the direct childs of this {@link XMLNode}. */
  protected final XMLContainer containees;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Creates a new XMLElement
   **
   ** @param  parent             the parent {@link XMLNode} that this
   **                            {@link XMLNode} belongs to.
   **                            May be <code>null</code>.
   ** @param  namespace          the namespace URI for this node.
   **                            May be <code>null</code>.
   ** @param  name               the local-name of this node.
   **                            May be <code>null</code>.
   ** @param  type               the node type being created.
   */
  protected XMLFragment(final XMLNode parent, final String namespace, final String name, final int type) {
    // ensure inheritance
    super(parent, namespace, name, type);

    // initialize instance attributes
    this.containees = new XMLContainer(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

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

    return this.containees.value();
  }
}

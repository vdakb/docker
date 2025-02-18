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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   XMLNodes.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLNodes.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface XMLNodes
// ~~~~~~~~~ ~~~~~~~~
/**
 ** The <code>XMLNodes</code> object represents a collection of nodes that can
 ** be set as name value pairs.
 ** <p>
 ** This typically represents the attributes that belong to an element and is
 ** used as an neutral way to access an element for either an input or output
 ** event.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   12.2.1.3.42.60.74
 */
public interface XMLNodes<T extends XMLNode> extends Iterable<String>
                                             ,       Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9027382449384558757")
  static final long serialVersionUID = -6845555079156618882L;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   self
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
  T self();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** This is used to get the name of the element that owns the nodes for the
   ** specified collection. This can be used to determine which element the node
   ** collection belongs to.
   **
   ** @return                    the name of the owning element.
   */
  String name();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
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
  T lookup(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put
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
  T put(final String name, final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
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
  T remove(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Returns <code>true</code> if the collection contains no elements.
   **
   ** @return                    <code>true</code> if the collection contains no
   **                            elements.
   */
  boolean empty();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the amount of collected elements.
   **
   ** @return                    the amount of collected elements.
   */
  int size();
}

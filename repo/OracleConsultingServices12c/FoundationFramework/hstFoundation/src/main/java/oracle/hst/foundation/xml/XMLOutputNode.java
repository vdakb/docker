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

    File        :   XMLOutputNode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLOutputNode.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

////////////////////////////////////////////////////////////////////////////////
// interface XMLOutputNode
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>XMLOutputNode</code> object is used to represent a cursor which
 ** can be used to write XML elements and attributes. Each of the output node
 ** objects represents a element, and can be used to add attributes to that
 ** element as well as child elements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface XMLOutputNode extends XMLNode {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7255328611179786774")
  static final long serialVersionUID = 1931401647267433033L;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRoot
  /**
   ** This method is used to determine if this node is the root node for the
   ** XML document.
   ** <p>
   ** The root node is the first node in the document and has no sibling nodes.
   ** This is <code>false</code> if the node has a parent node or a sibling
   ** node.
   **
   ** @return                    <code>true</code> if this is the root node
   **                            within the document.
   */
   boolean isRoot();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   committed
  /**
   ** This is used to determine whether the node has been committed.
   ** <p>
   ** If the node has been committed, then this will return <code>true</code>.
   ** When committed the node can no longer produce child nodes.
   **
   ** @return                    <code>true</code> if this node has already been
   **                            committed.
   */
  boolean committed();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** This is used to change the name of an output node.
   ** <p>
   ** This will only affect the name of the node if the node has not yet been
   ** committed. If the node is committed then this will not be reflected in the
   ** resulting XML generated.
   **
   ** @param  name               the name to change the node to.
   */
  void name(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** This is used to set a text value to the element.
   ** <p>
   ** This should be added to the element if the element contains no child
   ** elements. If the value cannot be added an exception is thrown.
   **
   ** @param  value              the text value to add to this element.
   **
   ** @throws XMLException       if child nodes exists
   */
  void value(final String value)
    throws XMLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode
  /**
   ** This is used to set the output mode of this node to either be CDATA,
   ** escaped, or inherited.
   ** <p>
   ** If the mode is set to data then any value specified will be written in a
   ** CDATA block, if this is set to escaped values are escaped. If however this
   ** method is set to inherited then the mode is inherited from the parent
   ** node.
   **
   ** @param  mode               the {@link XMLOutputMode} to set the node to.
   */
  void mode(final XMLOutputMode mode);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode
  /**
   ** The {@link XMLOutputMode} is used to indicate the output mode of this
   ** node. Three modes are possible, each determines how a value, if specified,
   ** is written to the resulting XML document.
   ** <p>
   ** This is determined by the <code>data</code> method which will set the
   ** output to be CDATA or escaped, if neither is specified the mode is
   ** inherited.
   **
   ** @return                   the {@link XMLOutputMode} of this output node
   **                           object.
   */
  XMLOutputMode mode();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data
  /**
   ** This is used to set the output mode of this node to either be CDATA or
   ** escaped.
   ** <p>
   ** If this is set to <code>true</code> any value specified will be written in
   ** a CDATA block, if this is set to <code>false</code> the value is escaped.
   ** If however this method is never invoked then the mode is inherited from
   ** the parent.
   **
   ** @param  data               <code>true</code> the value is written as a
   **                            CDATA block.
   */
  void data(final boolean data);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comment
  /**
   ** This is used to set a text comment to the element.
   ** <p>
   ** This will be written just before the actual element is written. Only a
   ** single comment can be set for each output node written.
   **
   ** @param  comment            the comment to set on the node.
   */
  void comment(final String comment);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comment
  /**
   ** This is used to get the text comment for the element.
   ** <p>
   ** This can be <code>null</code> if no comment has been set.
   ** <br>
   ** If no comment is set on the node then no comment will be written to the
   ** resulting XML.
   **
   ** @return                   the comment associated with this element.
   */
  String comment();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** This is used to acquire the prefix for this output node.
   ** <p>
   ** If the output node is an element then this will search its parent nodes
   ** until the prefix that is currently in scope is found.
   ** <br>
   ** If however this node is an attribute then the hierarchy of nodes is not
   ** searched as attributes to not inherit namespaces.
   **
   ** @return                    the prefix associated with this node.
   */
  String prefix();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** This is used to acquire the prefix for this output node.
   ** <p>
   ** If the output node is an element then this will search its parent nodes
   ** until the prefix that is currently in scope is found.
   ** <br>
   ** If however this node is an attribute then the hierarchy of nodes is not
   ** searched as attributes to not inherit namespaces.
   **
   ** @param  inherit            if there is no explicit prefix then inherit.
   **
   ** @return                    the prefix associated with this node.
   */
  String prefix(final boolean inherit);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** This is used to set the reference for the node.
   ** <p>
   ** Setting the reference implies that the node is a qualified node within the
   ** XML document. Both elements and attributes can be qualified.
   ** <br>
   ** Depending on the prefix set on this node or, failing that, any parent node
   ** for the reference, the element will appear in the XML document with that
   ** string prefixed to the node name.
   **
   ** @param  reference          the reference for the node.
   */
  void reference(final String reference);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference
  /**
   ** This is used to acquire the namespace URI reference associated with this
   ** node.
   ** <p>
   ** Although it is recommended that the namespace reference is a URI it does
   ** not have to be, it can be any unique identifier that can be used to
   ** distinguish the qualified names.
   **
   ** @return                    the namespace URI reference for this.
   */
  String reference();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** This method is used for convenience to add an attribute node to the
   ** attribute <code>NodeMap</code>. The attribute added can be removed from
   ** the element by using the node map.
   **
   ** @param  name               the name of the attribute to be added.
   ** @param  value              the value of the node to be added.
   **
   ** @return                    the node that has just been added.
   */
  XMLOutputNode attribute(final String name, final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** This returns a <code>NodeMap</code> which can be used to add nodes to the
   ** element before that element has been committed.
   ** <p>
   ** Nodes can be removed or added to the map and will appear as attributes on
   ** the written element when it is committed.
   **
   ** @return                    the node map used to manipulate attributes.
   */
  XMLNodes<XMLOutputNode> attributes();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespaces
  /**
   ** This returns the {@link XMLNamespace}s for this node.
   ** <p>
   ** Only an element can have namespaces, so if this node represents an
   ** attribute the elements namespaces will be provided when this is requested.
   ** By adding a namespace it becomes in scope for the current element all all
   ** child elements of that element.
   **
   ** @return                    the namespaces associated with the node.
   */
  XMLNamespace namespaces();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** This is used to acquire the <code>Node</code> that is the parent of this
   ** node.
   ** <p>
   ** This will return the node that is the direct parent of this node and
   ** allows for siblings to make use of nodes with their parents if required.
   **
   ** @return                    the parent node for this node.
   */
  XMLOutputNode parent();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** This is used to create a child element within the element that this object
   ** represents. When a new child is created with this method then the previous
   ** child is committed to the document.
   ** <p>
   ** The created <code>XMLOutputNode</code> object can be used to add
   ** attributes to the child element as well as other elements.
   **
   ** @param  name               the name of the child element to create.
   **
   ** @return                    the child element within this element.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  XMLOutputNode element(final String name)
    throws XMLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** This is used to remove any uncommitted changes.
   ** <p>
   ** Removal of an output node can only be done if it has no siblings and has
   ** not yet been committed. If the node is committed then this will throw an
   ** exception to indicate that it cannot be removed.
   **
   ** @throws XMLException       if the node cannot be removed.
   */
  void remove()
    throws XMLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit
  /**
   ** The <code>commit</code> method is used flush and commit any child nodes
   ** that have been created by this node. This allows the output to be
   ** completed when building of the XML document has been completed.
   ** <p>
   ** If output fails an exception is thrown.
   **
   ** @throws XMLException       if the node cannot be committed.
   */
  void commit()
    throws XMLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** The <code>close</code> method is used flush and close the underlying
   ** character stream created by this node. This allows the output to be
   ** completed when building of the XML document has been completed.
   ** <p>
   ** If output fails an exception is thrown.
   **
   ** @throws XMLException       if the node cannot be close.
   */
  void close()
    throws XMLException;
}
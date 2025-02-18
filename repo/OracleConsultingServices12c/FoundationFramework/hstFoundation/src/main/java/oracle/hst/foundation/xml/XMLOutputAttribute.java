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

    File        :   XMLOutputAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLOutputAttribute.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import oracle.hst.foundation.resource.XMLStreamBundle;

//////////////////////////////////////////////////////////////////////////////
// class XMLOutputAttribute
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>XMLOutputAttribute</code> object is used to represent a node added
 ** to the output node collection.
 ** <p>
 ** It represents a simple name value pair that is used as an attribute by an
 ** output element.
 ** <br>
 ** This shares its namespaces with the parent element so that any namespaces
 ** added to the attribute are actually added to the parent element, which
 ** ensures correct scoping.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class XMLOutputAttribute implements XMLOutputNode {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9073384313847423680")
  private static final long serialVersionUID = -1215790932981308684L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of this node object instance. */
  private String            name;

  /** the value of this node object instance. */
  private String            value;

  /** the namespace reference for this node. */
  private String            reference;

  /** the output node that this node requires. */
  private XMLOutputNode     source;

  /** contains the namespaces for the parent element. */
  private XMLNamespace      scope;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for the <code>XMLOutputAttribute</code> object.
   ** <p>
   ** This is used to create a simple name value pair attribute holder.
   **
   ** @param  source             the {@link XMLOutputNode} this attribute
   **                            belongs to.
   ** @param  name               the name that is used for the node.
   ** @param  value              the value used for the node.
   */
  public XMLOutputAttribute(final XMLOutputNode source, final String name, final String value) {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.name   = name;
    this.value  = value;
    this.source = source;
    this.scope  = source.namespaces();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRoot (XMLOutputNode)
  /**
   ** This method is used to determine if this node is the root node for the XML
   ** document.
   ** <p>
   ** The root node is the first node in the document and has no sibling nodes.
   ** This is <code>false</code> if the node has a parent node or a sibling
   ** node.
   **
   ** @return                    <code>true</code> if this is the root node
   **                            within the document.
   */
  @Override
  public final boolean isRoot() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   committed (XMLOutputNode)
  /**
   ** This is used to determine whether the node has been committed.
   ** <p>
   ** If the node has been committed, then this will return <code>true</code>.
   ** When committed the node can no longer produce child nodes.
   **
   ** @return                    <code>true</code> if this node has already been
   **                            committed.
   */
  @Override
  public final boolean committed() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (XMLOutputNode)
  /**
   ** This is used to change the name of an output node.
   ** <p>
   ** This will only affect the name of the node if the node has not yet been
   ** committed. If the node is committed then this will not be reflected in
   ** the resulting XML generated.
   **
   ** @param  name               the name to change the node to.
   */
  @Override
  public void name(final String name) {
    this.name = name;
  }

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
  // Method:   value (XMLOutputNode)
  /**
   ** This is used to set a text value to the element.
   ** <p>
   ** This should be added to the element if the element contains no child
   ** elements. If the value cannot be added an exception is thrown.
   **
   ** @param  value              the text value to add to this element.
   */
  @Override
  public void value(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value (XMLNode)
  /**
   ** Returns the value for the node that this represents.
   ** <p>
   ** This is a modifiable property for the node and can be changed.
   **
   ** @return                    the value for this node instance.
   **
   ** @throws XMLException       if there is a problem getting the value.
   */
  @Override
  public final String value()
    throws XMLException {

    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode (XMLOutputNode)
  /**
   ** This is used to set the output mode of this node to either be CDATA,
   ** escaped, or inherited.
   ** <p>
   ** If the mode is set to data then any value specified will be written in a
   ** CDATA block, if this is set to escaped values are escaped. If however
   ** this method is set to inherited then the mode is inherited from the
   ** parent node.
   **
   ** @param  mode               the {@link XMLOutputMode} to set the node to.
   */
  @Override
  public void mode(final XMLOutputMode mode) {
    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mode (XMLOutputNode)
  /**
   ** The {@link XMLOutputMode} is used to indicate the output mode of this
   ** node. Three modes are possible, each determines how a value, if
   ** specified, is written to the resulting XML document.
   ** <p>
   ** This is determined by the <code>data</code> method which will set the
   ** output to be CDATA or escaped, if neither is specified the mode is
   ** inherited.
   **
   ** @return                    the {@link XMLOutputMode} of this output node
   **                            object.
   */
  @Override
  public XMLOutputMode mode() {
    return XMLOutputMode.INHERIT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   data (XMLOutputNode)
  /**
   ** This is used to set the output mode of this node to either be CDATA or
   ** escaped.
   ** <p>
   ** If this is set to true the any value specified will be written in a
   ** CDATA block, if this is set to <code>false</code> the values is escaped.
   ** If however this method is never invoked then the mode is inherited from
   ** the parent.
   **
   ** @param  data               <code>true</code> the value is written as a
   **                            CDATA block.
   */
  @Override
  public void data(final boolean data) {
    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comment (XMLOutputNode)
  /**
   ** This is used to set a text comment to the element.
   ** <p>
   ** This will be written just before the actual element is written. Only a
   ** single comment can be set for each output node written.
   **
   ** @param  comment            the comment to set on the node.
   */
  @Override
  public void comment(final String comment) {
    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comment (XMLOutputNode)
  /**
   ** This is used to get the text comment for the element.
   ** <p>
   ** This can be <code>null</code> if no comment has been set.
   ** <br>
   ** If no comment is set on the node then no comment will be written to the
   ** resulting XML.
   **
   ** @return                    the comment associated with this element.
   */
  @Override
  public String comment() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix (XMLOutputNode)
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
  @Override
  public String prefix() {
    return this.scope.prefix(this.reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix (XMLOutputNode)
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
  @Override
  public String prefix(final boolean inherit) {
    return this.scope.prefix(this.reference);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference (XMLOutputNode)
  /**
   ** This is used to set the reference for the node.
   ** <p>
   ** Setting the reference implies that the node is a qualified node within
   ** the XML document. Both elements and attributes can be qualified.
   ** <br>
   ** Depending on the prefix set on this node or, failing that, any parent
   ** node for the reference, the element will appear in the XML document with
   ** that string prefixed to the node name.
   **
   ** @param  reference          the reference for the node.
   */
  @Override
  public void reference(final String reference) {
    this.reference = reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reference (XMLOutputNode)
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
  @Override
  public String reference() {
    return this.reference;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute (XMLOutputNode)
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
  @Override
  public XMLOutputNode attribute(final String name, final String value) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (XMLOutputNode)
  /**
   ** This returns a <code>NodeMap</code> which can be used to add nodes to
   ** the element before that element has been committed.
   ** <p>
   ** Nodes can be removed or added to the map and will appear as attributes
   ** on the written element when it is committed.
   **
   ** @return                    the node map used to manipulate attributes.
   */
  @Override
  public XMLNodes<XMLOutputNode> attributes() {
    return new XMLOutputNodes(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespaces (XMLOutputNode)
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
  @Override
  public XMLNamespace namespaces() {
    return this.scope;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent (XMLOutputNode)
  /**
   ** This is used to acquire the <code>Node</code> that is the parent of this
   ** node.
   ** <p>
   ** This will return the node that is the direct parent of this node and
   ** allows for siblings to make use of nodes with their parents if required.
   **
   ** @return                    the parent node for this node.
   */
  @Override
  public final XMLOutputNode parent() {
    return this.source;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (XMLOutputNode)
  /**
   ** This is used to create a child element within the element that this
   ** object represents. When a new child is created with this method then the
   ** previous child is committed to the document.
   ** <p>
   ** The created <code>XMLOutputNode</code> object can be used to add
   ** attributes to the child element as well as other elements.
   **
   ** @param  name               the name of the child element to create.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the child element within this element.
   **                            <br>
   **                            Possible object is <code>XMLOutputNode</code>.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  @Override
  public final XMLOutputNode element(final String name)
    throws XMLException {

    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (XMLOutputNode)
  /**
   ** This is used to remove any uncommitted changes.
   ** <p>
   ** Removal of an output node can only be done if it has no siblings and has
   ** not yet been committed. If the node is committed then this will throw an
   ** exception to indicate that it cannot be removed.
   **
   ** @throws XMLException       if the node cannot be removed.
   */
  @Override
  public void remove()
    throws XMLException {

    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commit (XMLOutputNode)
  /**
   ** The <code>commit</code> method is used flush and commit any child nodes
   ** that have been created by this node. This allows the output to be
   ** completed when building of the XML document has been completed.
   ** <p>
   ** If output fails an exception is thrown.
   **
   ** @throws XMLException      if the node cannot be committed.
   */
  @Override
  public void commit()
    throws XMLException {

    return;
  }

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
  @Override
  public void close()
    throws XMLException {

    return;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overriden)
  /**
   ** This is used to acquire the name and value of the attribute.
   ** <p>
   ** Implementing this method ensures that debugging the output node is
   ** simplified as it is possible to get the actual value.
   **
   ** @return                    the details of this output node.
   */
  @Override
  public String toString() {
    return XMLStreamBundle.format(XMLMessage.ATTRIBUTE, this.name, this.value);
  }
}
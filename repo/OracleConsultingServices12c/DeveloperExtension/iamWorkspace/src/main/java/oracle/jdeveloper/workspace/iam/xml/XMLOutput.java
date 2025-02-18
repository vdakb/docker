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

    File        :   XMLOutput.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLOutput.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

import java.io.IOException;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.Writer;
import java.io.Serializable;

import oracle.ide.Ide;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// class XMLOutput
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>XMLOutput</code> object is used to create a writer that will write
 ** well formed indented XML for a given output node. This is used in the
 ** serialization process to convert an object into an XML document.
 ** <p>
 ** This keeps a stack of all the active output nodes so that if an output node
 ** has been committed it cannot write any further data to the XML document.
 ** This allows all output nodes to be independent of each other as the node
 ** write organizes the write access.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   12.2.1.3.42.60.74
 */
class XMLOutput implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7412227230598810454")
  private static final long        serialVersionUID = 3356827049582837533L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** determines if we expand the namespace prefixes. */
  private final boolean            verbose;

  /** the formatter used to indent the XML elements and escape text. */
  private final XMLFormatter       writer;

  /** the stack of output nodes that are not yet ended. */
  private final Stack              stack;

  /** the set of as yet uncommitted elements blocks. */
  private final Set<XMLOutputNode> pending;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The is used to order the {@link XMLOutputNode} objects from the top down.
   ** This is basically used to reverse the order of the linked list so that the
   ** stack can be iterated within a for each loop easily. This can also be used
   ** to remove a node.
   */
  private static class Sequence implements Iterator<XMLOutputNode> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the cursor used to acquire objects from the stack. */
    private int   cursor;

    /** the collection this {@link Iterator} is used for. */
    private Stack stack;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for the <code>Sequence</code> object. This is used to
     ** position the cursor at the end of the list so the last inserted output
     ** node is the first returned from this.
     */
    public Sequence(final Stack stack) {
      // ensure inheritance
      super();

      // intialize instance attributes
      this.stack  = stack;
      this.cursor = stack.size();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hasNext (Iterator)
    /**
     ** This is used to determine if the cursor has reached the start of the
     ** list. When the cursor reaches the start of the list then this method
     ** returns false.
     **
     ** @return                  <code>true</code> if there are more nodes left.
     */
    public boolean hasNext() {
      return this.cursor > 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: next (Iterator)
    /**
     ** Returns the <code>OutputNode</code> object at the cursor position. If
     ** the cursor has reached the start of the list then this returns
     ** <code>null</code> instead of the first output node.
     **
     ** @return                  the node from the cursor position.
     */
    public XMLOutputNode next() {
      if (hasNext()) {
        return this.stack.get(--cursor);
      }
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (Iterator)
    /**
     ** Removes the match from the cursor position. This also ensures that the
     ** node is removed from the active set so that it is not longer considered
     ** a relevant output node.
     */
    public void remove() {
      this.stack.purge(this.cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Stack
  // ~~~~~ ~~~~~
  /**
   ** The <code>Stack</code> is used to keep track of the nodes that have been
   ** written to the document. This ensures that when nodes are written to the
   ** XML document that the writer can tell whether a child node for a given
   ** <code>XMLOutputNode</code> can be created. Each created node is pushed,
   ** and popped when ended.
   */
  private static class Stack extends ArrayList<XMLOutputNode> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:6311258335418426895")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the set of nodes that have not been committed. */
    private final Set<XMLOutputNode> active;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for the <code>Stack</code> object.
     ** <p>
     ** This is used to create a stack that can be used to keep track of the
     ** elements that have been written to the XML document.
     */
    public Stack(final Set<XMLOutputNode> active) {
      // ensure inheritance
      super();

      // intialize instance attributes
      this.active = active;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: top
    /**
     ** This is used to acquire the {@link XMLOutputNode} from the top of the
     ** output stack. This is used when the writer wants to determine the current
     ** element written to the XML document.
     **
     ** @return                   the node from the top of the stack.
     */
    public XMLOutputNode top() {
      final int size = size();
      return (size <= 0) ? null : get(size - 1);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: bottom
    /**
     ** This is used to acquire the {@link XMLOutputNode} from the bottom of
     ** the output stack. This is used when the writer wants to determine the
     ** root element for the written XML document.
     **
     ** @return                  the node from the bottom of the stack.
     */
    public XMLOutputNode bottom() {
      final int size = size();
      return (size <= 0) ? null : get(0);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: push
    /**
     ** This method is used to add an {@link XMLOutputNode} to the top of the
     ** stack. This is used when an element is written to the XML document, and
     ** allows the writer to determine if a child node can be created from a
     ** given output node.
     **
     ** @param  value            the output node to add to the stack.
     */
    public XMLOutputNode push(final XMLOutputNode value) {
      // ensure inheritance
      super.add(value);

      this.active.add(value);
      return value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: pop
    /**
     ** This is used to remove the {@link XMLOutputNode} from the top of the
     ** output stack. This is used when an element has been ended and the output
     ** writer wants to block child creation.
     **
     ** @return                  the node from the top of the stack.
     */
    public XMLOutputNode pop() {
      final int size = size();
      return (size <= 0) ? null : purge(size - 1);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: purge
    /**
     ** The <code>purge</code> method is used to purge a match from the provided
     ** position. This also ensures that the active set has the node removed so
     ** that it is no longer relevant.
     **
     ** @param  index            the index of the node that is to be removed.
     **
     ** @return                   the node removed from the specified index.
     */
    public XMLOutputNode purge(final int index) {
      final XMLOutputNode node = remove(index);
      if (node != null)
        this.active.remove(node);

      return node;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: iterator
    /**
     ** This is returns an {@link Iterator} that is used to loop through the
     ** ouptut nodes from the top down. This allows the node writer to determine
     ** what <code>Mode</code> should be used by an output node.
     ** <p>
     ** This reverses the iteration of the list.
     **
     ** @return                 an iterator to iterate from the top down.
     */
    @Override
    public Iterator<XMLOutputNode> iterator() {
      return new Sequence(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Resolver
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>Resolver</code> object will store the namespaces for an element.
   ** <p>
   ** Each namespace added to this map can be added with a prefix. A prefix is
   ** added only if the associated reference has not been added to a parent
   ** element. If a parent element has the associated reference, then the
   ** parents prefix is the one that will be returned when requested from this
   ** collection.
   **
   ** @see Element
   */
  private static class Resolver extends    LinkedHashMap<String, String>
                                implements XMLNamespace {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2825089125284004000")
    private static final long    serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

     /** the actual XML element this is associated with. */
     private final XMLOutputNode source;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for the <code>Resolver</code> object.
     ** <p>
     ** This is used to create a resolver for namespace prefixes using the
     ** hierarchy of elements. Resolving the prefix in this way avoids having to
     ** redeclare the same namespace with another prefix in a child element if
     ** it has already been declared.
     **
     ** @param  source           the XML element this is associated to.
     */
    public Resolver(final XMLOutputNode source) {
      // ensure inheritance
      super();

      // intialize instance attributes
      this.source = source;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix (XMLNamespace)
    /**
     ** This is the prefix that is associated with the source element.
     ** <p>
     ** If the source element does not contain a namespace reference then this
     ** will return its parents namespace. This ensures that if a namespace has
     ** been declared its child elements will inherit its prefix.
     **
     ** @return                  the prefix that is currently in scope.
     */
    @Override
    public String prefix() {
      return this.source.prefix();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix (XMLNamespace)
    /**
     ** This acquires the prefix for the specified namespace reference.
     ** <p>
     ** If the namespace reference has been set on this node with a given prefix
     ** then that prefix is returned, however if it has not been set this will
     ** search the parent elements to find the prefix that is in scope for the
     ** specified reference.
     **
     ** @param  reference        the reference to find a matching prefix for.
     **
     ** @return                  the prefix that is is scope.
     */
    @Override
    public String prefix(final String reference) {
      final int size = size();
      if (size > 0) {
        final String prefix = get(reference);
        if (prefix != null)
          return prefix;
      }
      return resolvePrefix(reference);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reference (XMLNamespace)
    /**
     ** This is used to add the namespace reference to the namespace map.
     ** <p>
     ** If the namespace has been added to a parent node then this will not add
     ** the reference. The prefix added to the map will be the default
     ** namespace, which is an empty prefix.
     **
     ** @param  reference        the reference to be added.
     **
     ** @return                   the prefix that has been replaced.
     */
    @Override
    public String reference(final String reference) {
      return reference(reference, "");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reference (XMLNamespace)
    /**
     ** This is used to add the namespace reference to the namespace map.
     ** <p>
     ** If the namespace has been added to a parent node then this will not add
     ** the reference.
     **
     ** @param  reference        the reference to be added.
     ** @param  prefix           the prefix to be added to the reference.
     **
     ** @return                  the prefix that has been replaced.
     */
    @Override
    public String reference(final String reference, final String prefix) {
      final String parent = resolvePrefix(reference);
      if (parent != null)
        return null;

      return put(reference, prefix);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: match (XMLNamespace)
    /**
     ** This acquires the namespace reference for the specified prefix.
     ** <p>
     ** If the provided prefix has been set on this node with a given reference
     ** then that reference is returned, however if it has not been set this
     ** will search the parent elements to find the reference that is in scope
     ** for the specified reference.
     **
     ** @param  prefix           the prefix to find a matching reference for.
     **
     ** @return                  the reference that is in scope.
     */
    @Override
    public String match(final String prefix) {
      if (containsValue(prefix)) {
        for (String reference : this) {
          String value = get(reference);

          if (value != null) {
            if (value.equals(prefix)) {
              return reference;
            }
          }
        }
      }
      return resolveReference(prefix);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: iterator (XMLNamespace)
    /**
     ** This returns an iterator for the namespace of all the nodes in this
     ** <code>NamespaceMap</code>.
     ** <p>
     ** This allows the namespaces to be iterated within a for each loop in order
     ** to extract the prefix values associated with the map.
     **
     ** @return                   the namespaces contained in this map.
     */
    @Override
    public Iterator<String> iterator() {
      return keySet().iterator();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   resolveReference
    /**
     ** This method will resolve the reference or the specified prefix by
     ** searching the parent nodes in order. This allows the prefix that is
     ** currently in scope for the reference to be acquired.
     **
     ** @param  prefix           the prefix to find a matching reference for.
     **
     ** @return                  the reference that is is scope.
     */
    private String resolveReference(final String prefix) {
      final XMLNamespace parent = this.source.namespaces();
      return (parent == null) ? null : parent.reference(prefix);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   resolvePrefix
    /**
     ** This method will resolve the prefix or the specified reference by
     ** searching the parent nodes in order. This allows the prefix that is
     ** currently in scope for the reference to be acquired.
     **
     ** @param  reference        the reference to find a matching prefix for.
     **
     ** @return                  the prefix that is is scope.
     */
    private String resolvePrefix(final String reference) {
      final XMLNamespace parent = this.source.namespaces();
      if (parent != null) {
        final String prefix = parent.prefix(reference);
        if (!containsValue(prefix))
          return prefix;
      }
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Element
  // ~~~~~ ~~~~~~~
  /**
   ** The <code>Element</code> object represents an XML element.
   ** <p>
   ** Attributes can be added to this before ant child element has been acquired
   ** from it. Once a child element has been acquired the attributes will be
   ** written an can no longer be manipulated, the same applies to any text
   ** value set for the element.
   */
  private static class Element implements XMLOutputNode {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-6613603149800832568")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the name of this output document node instance. */
    private String         name;

    /** the value that has been set on this document. */
    private String         value;

    /** the namespace reference used by this. */
    private String         reference;

    /** the comment that is to be written for the node. */
    private String         comment;

    /** the parent XML element to this output node. */
    private XMLOutputNode  parent;

    /** the attributes that have been set for the element.. */
    private XMLOutputNodes attributes;

    /** the attributes that have been set for the element.. */
    private XMLNamespace   scope;

    /** the output mode of this output document object. */
    private XMLOutputMode  mode;

    /** the writer that is used to create the element. */
    private XMLOutput      output;

    /** the output stack used by the node writer object. */
    private Stack          stack;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for the <code>Element</code> object.
     ** <p>
     ** This is used to create an output element that can create elements for an
     ** XML document. This requires the writer that is used to generate the
     ** actual document and the name of this node.
     **
     ** @param  parent           the parent node to this output node.
     ** @param  output           the writer used to generate the file.
     ** @param  name             the name of the element this represents.

     */
    public Element(final XMLOutputNode parent, final XMLOutput output, final String name) {
      // ensure inheritance
      super();

      // intialize instance attributes
      this.attributes = new XMLOutputNodes(this);
      this.mode       = XMLOutputMode.INHERIT;
      this.parent     = parent;
      this.output     = output;
      this.name       = name;
      this.scope      = new Resolver(parent);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: isRoot (XMLOutputNode)
    /**
     ** This method is used to determine if this node is the root node for the
     ** XML document.
     ** <p>
     ** The root node is the first node in the document and has no sibling
     ** nodes. This is <code>false</code> if the node has a parent node or a
     ** sibling node.
     **
     ** @return                  <code>true</code> if this is the root node
     **                          within the document.
     */
    @Override
    public boolean isRoot() {
      return this.output.isRoot(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: committed (XMLOutputNode)
    /**
     ** This is used to determine whether the node has been committed.
     ** <p
     ** If the node has been committed, then this will return <code>true</code>.
     ** When committed the node can no longer produce child nodes.
     **
     ** @return                  <code>true</code> if this node has already been
     **                          committed.
     */
    @Override
    public final boolean committed() {
      return this.stack.isEmpty();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (XMLOutputNode)
    /**
     ** This is used to change the name of an output node.
     ** <p>
     ** This will only affect the name of the node if the node has not yet been
     ** committed. If the node is committed then this will not be reflected in
     ** the resulting XML generated.
     **
     ** @param  name             the name to change the node to.
     */
    @Override
    public void name(final String name) {
      this.name = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (XMLNode)
    /**
     ** Returns the name of the node that this represents.
     ** <p>
     ** This is an immutable property and should not change for any node.
     **
     ** @return                  the name of the node that this represents.
     */
    @Override
    public String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (XMLOutputNode)
    /**
     ** This is used to set a text value to the element.
     ** <p>
     ** This should be added to the element if the element contains no child
     ** elements. If the value cannot be added an exception is thrown.
     **
     ** @param  value            the text value to add to this element.
     */
    @Override
    public void value(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (XMLNode)
    /**
     ** Returns the value for the node that this represents.
     ** <p>
     ** This is a modifiable property for the node and can be changed.
     **
     ** @return                  the value for this node instance.
     */
    @Override
    public final String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: comment (XMLOutputNode)
    /**
     ** This is used to set a text comment to the element.
     ** <p>
     ** This will be written just before the actual element is written. Only a
     ** single comment can be set for each output node written.
     **
     ** @param  comment          the comment to set on the node.
     */
    @Override
    public void comment(final String comment) {
      this.comment = comment;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: comment (XMLOutputNode)
    /**
     ** This is used to get the text comment for the element.
     ** <p>
     ** This can be <code>null</code> if no comment has been set.
     ** <br>
     ** If no comment is set on the node then no comment will be written to the
     ** resulting XML.
     **
     ** @return                  the comment associated with this element.
     */
    @Override
    public String comment() {
      return this.comment;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mode (XMLOutputNode)
    /**
     ** This is used to set the output mode of this node to either be CDATA,
     ** escaped, or inherited.
     ** <p>
     ** If the mode is set to data then any value specified will be written in a
     ** CDATA block, if this is set to escaped values are escaped. If however
     ** this method is set to inherited then the mode is inherited from the
     ** parent node.
     **
     ** @param  mode             the {@link XMLOutputMode} to set the node to.
     */
    @Override
    public void mode(final XMLOutputMode mode) {
      this.mode = mode;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mode (XMLOutputNode)
    /**
     ** The {@link XMLOutputMode} is used to indicate the output mode of this
     ** node. Three modes are possible, each determines how a value, if
     ** specified, is written to the resulting XML document.
     ** <p>
     ** This is determined by the <code>data</code> method which will set the
     ** output to be CDATA or escaped, if neither is specified the mode is
     ** inherited.
     **
     ** @return                  the {@link XMLOutputMode} of this output node
     **                          object.
     */
    @Override
    public XMLOutputMode mode() {
      return this.mode;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: data (XMLOutputNode)
    /**
     ** This is used to set the output mode of this node to either be CDATA or
     ** escaped.
     ** <p>
     ** If this is set to true the any value specified will be written in a
     ** CDATA block, if this is set to <code>false</code> the values is escaped.
     ** If however this method is never invoked then the mode is inherited from
     ** the parent.
     **
     ** @param  data             <code>true</code> the value is written as a
     **                          CDATA block.
     */
    public void data(final boolean data) {
      this.mode = (data ? XMLOutputMode.DATA : XMLOutputMode.ESCAPE);
    }

    ////////////////////////////////////////////////////////////////////////////
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
    public final XMLOutputNode parent() {
      return this.parent;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: element (XMLOutputNode)
    /**
     ** This is used to create a child element within the element that this
     ** object represents. When a new child is created with this method then the
     ** previous child is committed to the document.
     ** <p>
     ** The created <code>XMLOutputNode</code> object can be used to add
     ** attributes to the child element as well as other elements.
     **
     ** @param  name             the name of the child element to create.
     **
     ** @throws IOException      if the node cannot written to the stream.
     */
    @Override
    public final XMLOutputNode element(final String name)
      throws IOException {

      // prevent bogus state if the element has already childs
      if (this.value == null) {
        return this.output.element(this, name);
      }
      else {
        MessageDialog.error(
          Ide.getMainWindow()
        , ComponentBundle.string(ComponentBundle.XML_OUTPUT_NODE_INVALID)
        , ComponentBundle.string(ComponentBundle.XML_TITLE)
        , null
        );
        return null;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reference (XMLOutputNode)
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
     ** @param  reference        the reference for the node.
     */
    @Override
    public void reference(final String reference) {
      this.reference = reference;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reference (XMLOutputNode)
    /**
     ** This is used to acquire the namespace URI reference associated with this
     ** node.
     ** <p>
     ** Although it is recommended that the namespace reference is a URI it does
     ** not have to be, it can be any unique identifier that can be used to
     ** distinguish the qualified names.
     **
     ** @return                  the namespace URI reference for this.
     */
    @Override
    public String reference() {
      return this.reference;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix (XMLOutputNode)
    /**
     ** This is used to acquire the prefix for this output node.
     ** <p>
     ** If the output node is an element then this will search its parent nodes
     ** until the prefix that is currently in scope is found.
     ** <br>
     ** If however this node is an attribute then the hierarchy of nodes is not
     ** searched as attributes do not inherit namespaces.
     **
     ** @return                  the prefix associated with this node.
     */
    @Override
    public String prefix() {
      return prefix(true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix (XMLOutputNode)
    /**
     ** This is used to acquire the prefix for this output node.
     ** <p>
     ** If the output node is an element then this will search its parent nodes
     ** until the prefix that is currently in scope is found.
     ** <br>
     ** If however this node is an attribute then the hierarchy of nodes is not
     ** searched as attributes do not inherit namespaces.
     **
     ** @param  inherit          if there is no explicit prefix then inherit.
     **
     ** @return                  the prefix associated with this node.
     */
    @Override
    public String prefix(final boolean inherit) {
      final String prefix = this.scope.prefix(this.reference);
      if (inherit) {
        if (prefix == null)
          return this.parent.prefix();
      }
      return prefix;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: namespaces (XMLOutputNode)
    /**
     ** This returns the {@link XMLNamespace}s for this node.
     ** <p>
     ** Only an element can have namespaces, so if this node represents an
     ** attribute the elements namespaces will be provided when this is requested.
     ** By adding a namespace it becomes in scope for the current element and
     ** all child elements of that element.
     **
     ** @return                    the namespaces associated with the node.
     */
    @Override
    public XMLNamespace namespaces() {
      return this.scope;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: attribute (XMLOutputNode)
    /**
     ** This method is used for convenience to add an attribute node to the
     ** attribute <code>NodeMap</code>. The attribute added can be removed from
     ** the element by using the node map.
     **
     ** @param  name             the name of the attribute to be added.
     ** @param  value            the value of the node to be added.
     **
     ** @return                  the node that has just been added.
     */
    @Override
    public XMLOutputNode attribute(final String name, final String value) {
      return this.attributes.put(name, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: attributes (XMLOutputNode)
    /**
     ** This returns a <code>NodeMap</code> which can be used to add nodes to
     ** the element before that element has been committed.
     ** <p>
     ** Nodes can be removed or added to the map and will appear as attributes
     ** on the written element when it is committed.
     **
     ** @return                  the node map used to manipulate attributes.
     */
    @Override
    public XMLNodes<XMLOutputNode> attributes() {
      return this.attributes;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (XMLOutputNode)
    /**
     ** This is used to remove any uncommitted changes.
     ** <p>
     ** Removal of an output node can only be done if it has no siblings and has
     ** not yet been committed. If the node is committed then this will throw an
     ** exception to indicate that it cannot be removed.
     */
    @Override
    public void remove() {
      this.output.remove(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: close (XMLOutputNode)
    /**
     ** The <code>close</code> method is used flush and close the underlying
     ** character stream created by this node. This allows the output to be
     ** completed when building of the XML document has been completed.
     ** <p>
     ** If output fails an exception is thrown.
     **
     ** @throws IOException      if the character sequences cannot written to
     **                          the stream.
     */
    @Override
    public void close()
      throws IOException {

      commit();
      if (isRoot())
        this.output.close(this);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: commit (XMLOutputNode)
    /**
     ** The <code>commit</code> method is used flush and commit any child nodes
     ** that have been created by this node. This allows the output to be
     ** completed when building of the XML document has been completed.
     ** <p>
     ** If output fails an exception is thrown.
     **
     ** @throws IOException      if the character sequences cannot written to
     **                          the stream.
     */
    @Override
    public void commit()
      throws IOException {

      this.output.commit(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Document
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>Document</code> object is used to represent the root of an
   ** XML document.
   ** <p>
   ** This does not actually represent anything that will be written to the
   ** generated document. It is used as a way to create the root document
   ** element.
   ** <p>
   ** Once the root element has been created it can be committed by using this
   ** object.
   */
  private static class Document extends Element {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-5373497491556709533")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the output stack used by the node writer object. */
    private Stack stack;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for the <code>Document</code> object.
     ** <p>
     ** This is used to create an empty output node object that can be used to
     ** create a root element for the generated document.
     **
     ** @param  writer           the node writer to write the node to.
     ** @param  stack            the stack that contains the open nodes.
     */
    public Document(final XMLOutput writer, final Stack stack) {
      // ensure inheritance
      super(null, writer, "root");

      // intialize instance attributes
      this.stack = stack;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: isRoot (overridden)
    /**
     ** This method is used to determine if this node is the root node for the
     ** XML document.
     ** <p>
     ** The root node is the first node in the document and has no sibling
     ** nodes. This is <code>false</code> if the node has a parent node or a
     ** sibling node.
     **
     ** @return                  <code>true</code> if this is the root node
     **                          within the document.
     */
    @Override
    public final boolean isRoot() {
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix (overridden)
    /**
     ** This is used to acquire the prefix for this output node.
     ** <p>
     ** If the output node is an element then this will search its parent nodes
     ** until the prefix that is currently in scope is found.
     ** <br>
     ** If however this node is an attribute then the hierarchy of nodes is not
     ** searched as attributes do not inherit namespaces.
     **
     ** @param  inherit          if there is no explicit prefix then inherit.
     **
     ** @return                  the prefix associated with this node.
     */
    @Override
    public String prefix(final boolean inherit) {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: namespaces (overridden)
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
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (overridden)
    /**
     ** This is used to remove any uncommitted changes.
     ** <p>
     ** Removal of an output node can only be done if it has no siblings and has
     ** not yet been committed. If the node is committed then this will throw an
     ** exception to indicate that it cannot be removed.
     */
    @Override
    public void remove() {
      if (!this.stack.isEmpty()) {
        this.stack.bottom().remove();
      }
      else {
        MessageDialog.error(
          Ide.getMainWindow()
        , ComponentBundle.string(ComponentBundle.XML_OUTPUT_ROOT)
        , ComponentBundle.string(ComponentBundle.XML_TITLE)
        , null
        );
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: commit (overridden)
    /**
     ** The <code>commit</code> method is used flush and commit any child nodes
     ** that have been created by this node. This allows the output to be
     ** completed when building of the XML document has been completed.
     ** <p>
     ** If output fails an exception is thrown.
     **
     ** @throws IOException        if the buffered document cannot written to
     **                            the stream.
     */
    @Override
    public void commit()
      throws IOException {

      if (!this.stack.isEmpty()) {
        this.stack.bottom().commit();
      }
      else {
        MessageDialog.error(
          Ide.getMainWindow()
        , ComponentBundle.string(ComponentBundle.XML_OUTPUT_ROOT)
        , ComponentBundle.string(ComponentBundle.XML_TITLE)
        , null
        );
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLOutput</code> object.
   ** <p>
   ** This will create the object that is used to control an output elements
   ** access to the generated XML document.
   ** <br>
   ** This keeps a stack of active and uncommitted elements.
   **
   ** @param  writer             the output for the resulting document.
   */
  public XMLOutput(final Writer writer) {
    this(writer, new XMLFormat());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLOutput</code> object.
   ** <p>
   ** This will create the object that is used to control an output elements
   ** access to the generated XML document.
   ** <br>
   ** This keeps a stack of active and uncommitted elements.
   **
   ** @param  writer             the output for the resulting document.
   ** @param  format             used to format the generated document.
   */
  public XMLOutput(final Writer writer, final XMLFormat format) {
    this(writer, format, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLOutput</code> object.
   ** <p>
   ** This will create the object that is used to control an output elements
   ** access to the generated XML document.
   ** <br>
   ** This keeps a stack of active and uncommitted elements.
   **
   ** @param  writer             the output for the resulting document.
   ** @param  format             used to format the generated document.
   ** @param  verbose            determines if we expand the namespaces.
   */
  public XMLOutput(final Writer writer, final XMLFormat format, boolean verbose) {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.verbose = verbose;
    this.pending = new HashSet<XMLOutputNode>();
    this.stack   = new Stack(this.pending);
    this.writer  = new XMLFormatter(writer, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isRoot
  /**
   ** This method is used to determine if the node is the root node for the XML
   ** document. The root node is the first node in the document and has no
   ** sibling nodes. This is <code>false</code> if the node has a parent node or
   ** a sibling node.
   **
   ** @param  node               the node that is check as the root.
   **
   ** @return                    <code>true</code> if the node is the root node
   **                            for the document.
   */
  public boolean isRoot(final XMLOutputNode node) {
    return this.stack.bottom() == node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  committed
  /**
   ** This is used to determine if the specified node has been committed. If
   ** this returns <code>true</code> then the node is committed and cannot be
   ** used to add further child elements.
   **
   ** @param  node               the node to check for commit status.
   **
   ** @return                    <code>true</code> if the node has been
   **                            committed.
   */
  public boolean committed(final XMLOutputNode node) {
    return !this.pending.contains(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  remove
  /**
   ** This method is used to remove the output node from the output buffer if
   ** that node has not yet been committed. This allows a node that has been
   ** created to be deleted, ensuring that it will not affect the resulting XML
   ** document structure.
   **
   ** @param  node               the output node that is to be removed.
   */
  public void remove(final XMLOutputNode node) {
    if (this.stack.top() == node) {
      this.stack.pop();
    }
    else {
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.format(ComponentBundle.XML_OUTPUT_NODE_REMOVE, node.name())
      , ComponentBundle.string(ComponentBundle.XML_TITLE)
      , null
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  close
  /**
   ** This method is used to commit all nodes on the stack up to and including
   ** the specified node. This will effectively create end tags for any nodes
   ** that are currently open up to the specified element. Once committed the
   ** output node can no longer be used to create child elements, nor can any of
   ** its child elements.
   **
   ** @param  parent             the node that is to be closed.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  public void close(final XMLOutputNode parent)
    throws IOException {

    commit(parent);
    this.writer.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  commit
  /**
   ** This method is used to commit all nodes on the stack up to and including
   ** the specified node. This will effectively create end tags for any nodes
   ** that are currently open up to the specified element. Once committed the
   ** output node can no longer be used to create child elements, nor can any of
   ** its child elements.
   **
   ** @param  parent             the node that is to be committed.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  public void commit(final XMLOutputNode parent)
    throws IOException {

    if (this.stack.contains(parent)) {
      XMLOutputNode top = this.stack.top();

      if (!committed(top))
        writeStart(top);

      while (this.stack.top() != parent)
        writeEnd(this.stack.pop());

      writeEnd(parent);
      this.stack.pop();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  document
  /**
   ** This is used to acquire the root output node for the document.
   ** <p>
   ** This will create an empty node that can be used to generate the root
   ** document element as a child to the document.
   ** <p>
   ** Depending on whether or not an encoding has been specified this method
   ** will write a prolog to the generated XML document.
   ** <br>
   ** Each prolog written uses an XML version of "1.0".
   **
   ** @return                    an output element for the document.
   **
   ** @throws IOException        if the document cannot written to the stream.
   */
  public XMLOutputNode document()
    throws IOException {

    final Document document = new Document(this, this.stack);
    if (this.stack.isEmpty())
      this.writer.writeProlog();

    return document;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  element
  /**
   ** This is used to create a new element under the specified node.
   ** <p>
   ** This will effectively commit all nodes that are open until this node is
   ** encountered. Once the specified node is encountered on the stack a new
   ** element is created with the specified name.
   **
   ** @param  parent             the node that is to be committed.
   ** @param  name               the name of the start element to create.
   **
   ** @return                    a child node for the given parent.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  public XMLOutputNode element(final XMLOutputNode parent, final String name)
    throws IOException {

    if (this.stack.isEmpty())
      return writeStart(parent, name);

    if (this.stack.contains(parent)) {
      XMLOutputNode top = this.stack.top();

      if (!committed(top)) {
        writeStart(top);
      }
      while (this.stack.top() != parent) {
        writeEnd(this.stack.pop());
      }
      if (!this.stack.isEmpty()) {
        writeValue(parent);
      }
      return writeStart(parent, name);
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeStart
  /**
   ** This is used to begin writing on a new XML element.
   ** <p>
   ** This is typically done by writing any comments required. This will create
   ** an output node of the specified name before writing the comment, if any
   ** exists. Once the comment has been written the node is pushed on to the
   ** head of the output node stack.
   **
   ** @param  parent             the parent node to the next output node.
   ** @param  name               the name of the node that is to be created.
   **
   ** @return                    an output node used for writing content.
   */
  private XMLOutputNode writeStart(final XMLOutputNode parent, final String name) {
    // prevent bogus input
    if (name == null) {
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.string(ComponentBundle.XML_OUTPUT_NODE_NAME)
      , ComponentBundle.string(ComponentBundle.XML_TITLE)
      , null
      );
      return null;
    }
    XMLOutputNode node = new Element(parent, this, name);
    return stack.push(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeStart
  /**
   ** This is used to write the XML element to the underlying buffer.
   ** <p>
   ** The element is written in the order of element prefix and name followed by
   ** the attributes an finally the namespaces for the element. Once this is
   ** finished the element is committed to
   **
   ** @param  node               the node that is to be fully written.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  private void writeStart(final XMLOutputNode node)
    throws IOException {

    writeComment(node);
    writeName(node);
    writeAttributes(node);
    writeNamespaces(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeEnd
  /**
   ** This is used to write a new end element to the resulting XML document.
   ** <p>
   ** This will acquire the name and value of the given node, if the node has a
   ** value that is written. Finally a new end tag is written to the document
   ** and the output is flushed.
   **
   ** @param  node               this is the node that is to have an end tag.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  private void writeEnd(final XMLOutputNode node)
    throws IOException {

    final String name   = node.name();
    final String prefix = node.prefix(this.verbose);
    final String value  = node.value();
    // enforce to write a CDATA block regardless if there are meaningfull data
    if (node.mode() == XMLOutputMode.DATA || value != null)
      writeValue(node);

    if (name != null) {
      this.writer.writeEnd(name, prefix);
      this.writer.flush();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeComment
  /**
   ** This is used to write a comment to the document.
   ** <p>
   ** Comments appear just before the element name, this allows an logical
   ** association between the comment and the node to be made.
   **
   ** @param  node               the node that is to have its name written.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  private void writeComment(final XMLOutputNode node)
    throws IOException {

    final String comment = node.comment();
    if (comment != null)
      this.writer.writeComment(comment);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeName
  /**
   ** This is used to write a new start element to the resulting XML document.
   ** <p>
   ** This will create an output node of the specified name before writing the
   ** start tag. Once the tag is written the node is pushed on to the head of
   ** the output node stack.
   **
   ** @param  node               the node that is to have its name written.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  private void writeName(final XMLOutputNode node)
    throws IOException {

    final String prefix = node.prefix(this.verbose);
    final String name   = node.name();
    if (name != null)
      this.writer.writeStart(name, prefix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeValue
  /**
   ** This is used to write an element value to the resulting XML document.
   ** <p>
   ** This will search the nodes parents for the write mode, if the mode is
   ** CDATA then that is what is used to write the data, otherwise the value is
   ** written as plain text.
   ** <p>
   ** One side effect of this method is that it clears the value of the output
   ** node once it has been written to the XML. This is needed, it can however
   ** cause confusion within the API.
   **
   ** @param  node               the node to write the value of.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  private void writeValue(final XMLOutputNode node)
    throws IOException {

    XMLOutputMode mode  = node.mode();
    final String  value = node.value();
    if (value != null) {
      for (XMLOutputNode next : this.stack) {
        if (mode != XMLOutputMode.INHERIT)
          break;

        mode = next.mode();
      }
      this.writer.writeText(value, mode);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeAttributes
  /**
   ** This is used to write the attributes of the specified node to the output.
   ** This will iterate over each node entered on to the node. Once written the
   ** node is considered inactive.
   **
   ** @param  node               this is the node to have is attributes written.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  private void writeAttributes(final XMLOutputNode node)
    throws IOException {

    final XMLNodes<XMLOutputNode> map = node.attributes();
    for (String name : map) {
      final XMLOutputNode entry  = map.lookup(name);
      final String        value  = entry.value();
      final String        prefix = entry.prefix(this.verbose);
      this.writer.writeAttribute(name, value, prefix);
    }
    this.pending.remove(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeNamespaces
  /**
   ** This is used to write the namespaces of the specified node to
   ** the output. This will iterate over each namespace entered on
   ** to the node. Once written the node is considered qualified.
   **
   ** @param  node               this is the node to have is attributes written.
   **
   ** @throws IOException        if the node cannot written to the stream.
   */
  private void writeNamespaces(final XMLOutputNode node)
    throws IOException {

    final XMLNamespace map = node.namespaces();
    for (String name : map)
      this.writer.writeNamespace(name, map.prefix(name));
  }
}
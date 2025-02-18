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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   DescriptorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DescriptorFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.service.reconciliation;

import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

import oracle.mds.persistence.MDSIOException;
import oracle.mds.persistence.PDocument;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.XMLException;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.TaskDescriptorFactory;

import oracle.iam.identity.foundation.reconciliation.Descriptor;

////////////////////////////////////////////////////////////////////////////////
// final class DescriptorFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information for an attribute mapping used
 ** by a reconciliation task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public final class DescriptorFactory extends TaskDescriptorFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ELEMENT_LOOKUP      = "lookup";

  /**
   ** the XML element that identifies an mlti-valued attribute in Oracle
   ** Identity Manager.
   */
  public static final String ELEMENT_MULTIVALUED = "multivalued";

  /**
   ** the XML element that identifies an entitlement element in Oracle Identity
   ** Manager.
   */
  public static final String ELEMENT_ENTITLEMENT = "entitlement";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Descriptor descriptor;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Grammar
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants.
   */
  enum Grammar {
      /** the encoded valid grammar state transitions in the parser. */
      INIT(new Grammar[0], "")
    , DESCRIPTOR(INIT,        ELEMENT_DESCRIPTOR)
    , PROFILE(DESCRIPTOR,     ELEMENT_PROFILE)
    , MULTIVALUED(DESCRIPTOR, ELEMENT_MULTIVALUED)
    , LOOKUP(DESCRIPTOR,      ELEMENT_LOOKUP)
    , ENTITLEMENT(DESCRIPTOR, ELEMENT_ENTITLEMENT)
    , CONSTANT(
        new Grammar[] {
          DESCRIPTOR
        , ENTITLEMENT
        },                    ELEMENT_CONSTANT)
    , ATTRIBUTE(
        new Grammar[] {
          DESCRIPTOR
        , ENTITLEMENT
        },                    ELEMENT_ATTRIBUTE)
    , TRANSFORMER(
        new Grammar[] {
          DESCRIPTOR
        , ENTITLEMENT
        },                    ELEMENT_TRANSFORMER)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8359176875112780432")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** array of parent states to this one. */
    final Grammar parents[];

    /** the name of the tag for this state. */
    final String  tag;

    ////////////////////////////////////////////////////////////////////////////
    // static init block
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** This code executes after the enums have been constructed.
     ** <p>
     ** Because of order of execution when initializing an enum, you can't call
     ** static functions in an enum constructor. (They are constructed before
     ** static initialization).
     ** <p>
     ** Instead, we use a static initializer to populate the lookup map after
     ** all the enums are constructed.
      */
    static {
      for (Grammar state : Grammar.values()) {
        lookup.put(state.tag, state);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Grammar</code> with a single parent state.
     **
     ** @param  parent           the predecessor where this grammar state can
     **                          occure within.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     */
    Grammar(final Grammar parent, final String tag) {
      this(new Grammar[]{parent}, tag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Grammar</code> with a multiple parent states.
     **
     ** @param  parents          the predecessors where this grammar state can
     **                          occure within.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     */
    Grammar(final Grammar[] parents, String tag) {
      this.parents = parents;
      this.tag     = tag;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: lookup
    /**
     ** Lookups the <code>Grammar</code> for the specified a tag name.
     **
     ** @param  tag              the name of the tag the <code>Grammar</code>
     **                          is requested for.
     **
     ** @return                  the <code>Grammar</code> for that tag.
     */
    public static Grammar lookup(final String tag) {
      return lookup.get(tag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   transition
    /**
     ** Checks whether it is valid to transition to the specified state from
     ** this state.
     **
     ** @param  state
     **
     ** @return
     */
    public boolean transition(final Grammar state) {
      if (this.equals(state))
        return true;

      for (int i = 0; i < state.parents.length; i++) {
        if (state.parents[i].equals(this))
          return true;
      }
      for (int i = 0; i < this.parents.length; i++) {
        if (this.parents[i].equals(state))
          return true;
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~
  /**
   ** Handles parsing of a XML file which defines the mapping descriptor.
   */
  private class Parser extends SAXInput {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Grammar              cursor = Grammar.INIT;
    private final Stack<Grammar> state  = new Stack<Grammar>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Parser</code>.
     **
     ** @throws SAXException     in case {@link SAXInput} is not able to create
     **                          an appropriate parser.
     */
    private Parser()
      throws SAXException {

      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: startElement (overridden)
    /**
     ** Receive notification of the beginning of an element.
     ** <p>
     ** The Parser will invoke this method at the beginning of every element in
     ** the XML document; there will be a corresponding
     ** {@link #endElement(String, String, String) endElement} event for every
     ** startElement event (even when the element is empty). All of the
     ** element's content will be reported, in order, before the corresponding
     ** endElement event.
     ** <p>
     ** This event allows up to three name components for each element:
     ** <ol>
     **   <li>the Namespace URI;</li>
     **   <li>the local name; and</li>
     **   <li>the qualified (prefixed) name.</li>
     ** </ol>
     ** Any or all of these may be provided, depending on the values of the
     ** <code>http://xml.org/sax/features/namespaces</code> and the
     ** <code>http://xml.org/sax/features/namespace-prefixes</code> properties:
     ** <ul>
     **   <li>the Namespace URI and local name are required when the namespaces
     **       property is <code>true</code> (the default), and are optional when
     **       the namespaces property is <code>false</code> (if one is
     **       specified, both must be);
     **   <li>the qualified name is required when the namespace-prefixes
     **       property is <code>true</code>, and is optional when the
     **       namespace-prefixes property is <code>false</code> (the default).
     ** </ul>
     ** Note that the attribute list provided will contain only attributes with
     ** explicit values (specified or defaulted):
     **   #IMPLIED attributes will be omitted.
     **   The attribute list will contain attributes used for Namespace
     **   declarations (xmlns* attributes) only if the
     **   <code>http://xml.org/sax/features/namespace-prefixes</code> property
     **   is <code>true</code> (it is <code>false</code> by default, and support
     **   for a <code>true</code> value is optional).
     ** <p>
     ** Like {@link #characters(char[], int, int) characters()}, attribute
     ** values may have characters that need more than one <code>char</code>
     ** value.
     **
     ** @param  uri              the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     ** @param  attributes       the attributes attached to the element.
     **                          If there are no attributes, it shall be an
     **                          empty {@link Attributes} object. The value of
     **                          this object after startElement returns is
     **                          undefined.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see    #endElement(String, String, String)
     ** @see    org.xml.sax.Attributes
     ** @see    org.xml.sax.helpers.AttributesImpl
     */
    @Override
    public void startElement(final String uri, final String localName, final String qualifiedName, final Attributes attributes)
      throws SAXException {

      // check for state transition
      Grammar state = Grammar.lookup(localName);
      if (state == null) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), localName };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_UNKNOWN, arguments));
      }
      // we know it's a valid tag
      if (!this.cursor.transition(state)) {
        // invalid transition
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), this.cursor.toString(), state.toString() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_TRANSISTION, arguments));
      }
      // change FSA state
      this.state.push(this.cursor);
      this.cursor = state;
      // dispatch to handling method
      AttributeMapping mapping = null;
      switch (this.cursor) {
        case DESCRIPTOR  : processDescriptor(DescriptorFactory.this.descriptor, attributes);
                           // put the collection provider for attributes and
                           // their transformations on the value stack
                           push(DescriptorFactory.this.descriptor);
                           break;
        case PROFILE     : DescriptorFactory.this.descriptor.profile().put(attributes.getValue(ATTRIBUTE_NAME), attributes.getValue(ATTRIBUTE_ATTRIBUTE));
                           break;
        case CONSTANT    : // there must be a task descriptor on the value
                           // stack; we need to cast her to TaskDescriptor due
                           // to an EntityReference is a subclass of
                           // TaskDescriptor
                           mapping = ((TaskDescriptor)peek()).constantMapping();
                           mapping.put(attributes.getValue(ATTRIBUTE_NAME), attributes.getValue(ATTRIBUTE_VALUE));
                           break;
        case ATTRIBUTE   : final String source = attributes.getValue(ATTRIBUTE_SOURCE);
                           // there must be a task descriptor on the value
                           // stack; we need to cast her to TaskDescriptor due
                           // to an EntityReference is a subclass of
                           // TaskDescriptor
                           mapping = ((TaskDescriptor)peek()).attributeMapping();
                           // to figure out the destination use the source
                           final String destination = (String)mapping.get(source);
                           if (StringUtility.isEmpty(destination))
                             mapping.put(source, attributes.getValue(ATTRIBUTE_NAME));
                           else
                             mapping.put(source, destination + "|" + attributes.getValue(ATTRIBUTE_NAME));
                           break;
        case TRANSFORMER : // there must be a task descriptor on the value
                           // stack; we need to cast her to TaskDescriptor due
                           // to an DirectoryEntityReference is a subclass of
                           // TaskDescriptor
                           mapping = ((TaskDescriptor)peek()).transformationMapping();
                           mapping.put(attributes.getValue(ATTRIBUTE_NAME), attributes.getValue(ATTRIBUTE_CLASS));
                           break;
        case MULTIVALUED : // there must be a task descriptor on the value stack
                           mapping = ((Descriptor)peek()).multivalued();
                           mapping.put(attributes.getValue(ATTRIBUTE_NAME), attributes.getValue(ATTRIBUTE_NAME));
                           break;
        case LOOKUP      : // put the name of the attribute on the value stack
                           // to compose the mapping later
                           push(attributes.getValue(ATTRIBUTE_NAME));
                           break;
        case ENTITLEMENT : unmarshalEntitlement(attributes);
                           break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endElement (overridden)
    /**
     ** Receive notification of the end of an element.
     ** <p>
     ** The SAX parser will invoke this method at the end of every element in
     ** the XML document; there will be a corresponding
     ** {@link #startElement(String, String, String, Attributes) startElement}
     ** event for every endElement event (even when the element is empty).
     ** <p>
     ** For information on the names, see
     ** {@link #startElement (String, String, String, Attributes)}.
     **
     ** @param  uri              the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @Override
    public void endElement(final String uri, final String localName, final String qualifiedName)
      throws SAXException {

      // dispatch to handling method
      switch (this.cursor) {
        case DESCRIPTOR  :
        case LOOKUP      :
        case ENTITLEMENT : // remove the collection provider for attributes and
                          // their transformations from the value stack
                          pop();
                          break;
      }
      // change FSA state
      this.cursor = this.state.pop();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: character (overridden)
    /**
     ** Receive notification of character data inside an element.
     ** <p>
     **
     ** @param ch                the characters.
     ** @param start             the start position in the character array.
     ** @param length            the number of characters to use from the
     **                          character array.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @Override
    public void characters(char ch[], int start, int length) {
      switch (this.cursor) {
        case LOOKUP : // if we have a lookup there must be an attribute name on
                      // the value stack
                      DescriptorFactory.this.descriptor.lookup().put((String)peek(), String.valueOf(ch, start, length));
                      break;
        default     : break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalEntitlement
    /**
     ** Starts unmarshalling of an {@link TaskDescriptor.Entitlement} from the
     ** XML stream.
     **
     ** @param  attributes       the attributes attached to the entitlement
     **                          element.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     */
    private void unmarshalEntitlement(final Attributes attributes)
      throws SAXException {

      try {
        final TaskDescriptor.Entitlement entitlement = new TaskDescriptor.Entitlement(DescriptorFactory.this.descriptor, attributes.getValue(ATTRIBUTE_SOURCE));
        entitlement.transformationEnabled(StringUtility.stringToBool(attributes.getValue(TaskDescriptor.TRANSFORMATION_ENABLED)));
        DescriptorFactory.this.descriptor.entitlement().put(attributes.getValue(ATTRIBUTE_NAME), entitlement);
        // put the collection provider for attributes and their
        // transformations on the value stack
        push(entitlement);
      }
      catch (TaskException e) {
        throw new SAXException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DescriptorFactory</code> that configures the specified
   ** {@link Descriptor} .
   **
   ** @param  descriptor         the {@link Descriptor} to configure by this
   **                            {@link TaskDescriptorFactory}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private DescriptorFactory(final Descriptor descriptor)
    throws TaskException {

    // ensure inheritance
    super();

    // prevent bogus input
    if (descriptor == null)
      throw TaskException.argumentIsNull("descriptor");

    // initialize instance attributes
    this.descriptor = descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from a {@link File}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link TaskDescriptorFactory}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Descriptor descriptor, final File file)
    throws TaskException {

    configure(descriptor, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from a
   ** {@link File}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link TaskDescriptorFactory}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Descriptor descriptor, final File file, final boolean validate)
    throws TaskException {

    try {
      configure(descriptor, new FileInputStream(file), validate);
    }
    catch (IOException e) {
      throw TaskException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputStream}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link TaskDescriptorFactory}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Descriptor descriptor, final InputStream stream)
    throws TaskException {

    configure(descriptor, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputStream}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link TaskDescriptorFactory}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Descriptor descriptor, final InputStream stream, final boolean validate)
    throws TaskException {

    // prevent bogus input
    if (stream == null)
      throw TaskException.argumentIsNull("stream");

    configure(descriptor, new InputSource(stream), validate);
    try {
      stream.close();
    }
    catch (IOException e) {
      throw TaskException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputSource}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link TaskDescriptorFactory}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Descriptor descriptor, final InputSource source)
    throws TaskException {

    configure(descriptor, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputSource}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link TaskDescriptorFactory}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Descriptor descriptor, final InputSource source, final boolean validate)
    throws TaskException {

    // prevent bogus input
    if (source == null)
      throw TaskException.argumentIsNull("source");

    // validate the provided stream against the schema
    final DescriptorFactory factory = new DescriptorFactory(descriptor);
    if (validate) {
      try {
        factory.validate(Descriptor.class, source);
      }
      catch (XMLException e) {
        throw new TaskException(e.code());
      }
    }
    factory.configure(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from a {@link PDocument}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link TaskDescriptorFactory}.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Descriptor descriptor, final PDocument document)
    throws TaskException {

    // prevent bogus input
    if (document == null)
      throw TaskException.argumentIsNull("document");

    final DescriptorFactory factory = new DescriptorFactory(descriptor);
    try {
      factory.configure(document.read());
    }
    catch (MDSIOException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private void configure(final InputSource source)
    throws TaskException {

    // prevent bogus input
    if (source == null)
      throw TaskException.argumentIsNull("source");

    try {
      final Parser parser = new Parser();
      parser.processDocument(source);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }
}
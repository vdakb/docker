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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Framework

    File        :   DescriptorParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DescriptorParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

import oracle.hst.foundation.object.Pair;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLError;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DescriptorParser
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This is the class to unmarshals meta information of provisioning
 ** description.
 ** <p>
 ** Handles parsing of a XML file which defines the mapping descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DescriptorParser extends SAXInput {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the namespace declaration of a mapping descriptor */
  protected static final String NAMESPACE             = "http://www.oracle.com/schema/oim/mapping";

  /** the XML element that is the root element of the structure */
  protected static final String ELEMENT_DESCRIPTOR    = "descriptor";

  /** the XML namespace declaration */
  protected static final String ATTRIBUTE_NAMESPACE   = "xmlns";

  /**
   ** the XML element that identifies a source or target template in Identity
   ** Manager
   */
  protected static final String ELEMENT_TEMPLATE      = "template";

  /**
   ** the XML element that identifies a source or target attribute in Identity
   ** Manager
   */
  protected static final String ELEMENT_ATTRIBUTE     = "attribute";

  /**
   ** the XML element that identifies a transformation to apply on a source or
   ** target attribute in Identity Manager
   */
  protected static final String ELEMENT_TRANSFORMER   = "transformer";

  /**
   ** the XML element that identifies a source or target multi-valued attribute
   ** in Identity Manager
   */
  protected static final String ELEMENT_REFERENCE     = "multivalue";

  /** the XML attribute specifies the flags of an target attribute */
  protected static final String ELEMENT_FLAG          = "flag";

  /** the XML attribute specifies the type of an source or target attribute */
  protected static final String ATTRIBUTE_TYPE        = "type";

  /** the XML attribute specifies the name of an source or target attribute */
  protected static final String ATTRIBUTE_NAME        = "name";

  /** the XML attribute specifies the name of a source provider */
  protected static final String ATTRIBUTE_SOURCE      = "source";

  /** the XML attribute specifies if the class name of a transformer */
  protected static final String ATTRIBUTE_CLASS       = "class";

  /**
   ** the XML attribute that identifies a transformation to be applied on an
   ** attribute in Identity Manager
   */
  protected static final String ATTRIBUTE_TRANSFORMER = "transformer";

  /** the XML attribute specifies the value of an attribute flag */
  protected static final String ATTRIBUTE_VALUE       = "value";

  /**
   ** the XML element that identifies an action in Identity Manager
   */
  protected static final String ELEMENT_ACTION        = "action";

  /**
   ** the XML element that identifies an action option in Identity Manager
   */
  protected static final String ELEMENT_OPTION        = "option";

  /**
   ** the XML element that identifies an action script in Identity Manager
   */
  protected static final String ELEMENT_COMMAND       = "command";

  /** the XML attribute specifies the value of an action target */
  protected static final String ATTRIBUTE_TARGET      = "target";

  /** the XML attribute specifies the value of an action timing */
  protected static final String ATTRIBUTE_PHASE       = "phase";

  /** the XML attribute specifies the value of an action timing */
  protected static final String ATTRIBUTE_LANGUAGE    = "language";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Descriptor    descriptor;

  private Grammar               cursor = Grammar.INIT;
  private final Stack<Grammar>  state  = new Stack<Grammar>();

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
    , DESCRIPTOR(INIT,                                   ELEMENT_DESCRIPTOR)
    , ACTION(DESCRIPTOR,                                 ELEMENT_ACTION)
    , REFERENCE(DESCRIPTOR,                              ELEMENT_REFERENCE)
    , TEMPLATE(new Grammar[] {DESCRIPTOR, REFERENCE},    ELEMENT_TEMPLATE)
    , ATTRIBUTE(new Grammar[] {DESCRIPTOR, REFERENCE},   ELEMENT_ATTRIBUTE)
    , TRANSFORMER(new Grammar[] {DESCRIPTOR, REFERENCE}, ELEMENT_TRANSFORMER)
    , FLAG(ATTRIBUTE,                                    ELEMENT_FLAG)
    , COMMAND(ACTION,                                    ELEMENT_COMMAND)
    , OPTION(ACTION,                                     ELEMENT_OPTION)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

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
     ** Because of order of execution when initializing an enum, you can't
     ** call static functions in an enum constructor. (They are constructed
     ** before static initialization).
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
     **                          <br>
     **                          Allowed object is {@link Grammar}.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
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
     **                          <br>
     **                          Allowed object is array {@link Grammar}s.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
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
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Grammar</code> for that tag.
     **                          <br>
     **                          Possible object is {@link Grammar}.
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
     ** @param  state            the grammar state to validate.
     **                          <br>
     **                          Allowed object is {@link Grammar}.
     **
     ** @return                  <code>true</code> if the given state is in the
     **                          expected range; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
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
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for <code>DescriptorParser</code>.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured by this
   **                            {@link SAXInput}.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   **
   ** @throws SAXException       in case {@link SAXInput} is not able to create
   **                            an appropriate parser.
   */
  public DescriptorParser(final Descriptor descriptor)
    throws SAXException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.descriptor = descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startElement (overridden)
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
   ** <code>http://xml.org/sax/features/namespace-prefixes</code>
   ** properties:
   ** <ul>
   **   <li>the Namespace URI and local name are required when the
   **       namespaces property is <code>true</code> (the default), and are
   **       optional when the namespaces property is <code>false</code> (if
   **       one is specified, both must be);
   **   <li>the qualified name is required when the namespace-prefixes
   **       property is <code>true</code>, and is optional when the
   **       namespace-prefixes property is <code>false</code> (the default).
   ** </ul>
   ** Note that the attribute list provided will contain only attributes
   ** with explicit values (specified or defaulted):
   **   #IMPLIED attributes will be omitted.
   **   The attribute list will contain attributes used for Namespace
   **   declarations (xmlns* attributes) only if the
   **   <code>http://xml.org/sax/features/namespace-prefixes</code> property
   **   is <code>true</code> (it is <code>false</code> by default, and
   **   support for a <code>true</code> value is optional).
   ** <p>
   ** Like {@link #characters(char[], int, int) characters()}, attribute
   ** values may have characters that need more than one <code>char</code>
   ** value.
   **
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attributes         the attributes attached to the element.
   **                            If there are no attributes, it shall be an
   **                            empty {@link Attributes} object. The value of
   **                            this object after startElement returns is
   **                            undefined.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
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
    switch (this.cursor) {
      case DESCRIPTOR  : unmarshalDescriptor(attributes);
                         break;
      case TEMPLATE    : unmarshalTemplate(attributes);
                         break;
      case ATTRIBUTE   : unmarshalAttribute(attributes);
                         break;
      case REFERENCE   : unmarshalReference(attributes);
                         break;
      case TRANSFORMER : unmarshalTransformer(attributes);
                         break;
      case FLAG        : push(new StringBuilder());
                         break;
      case ACTION      : unmarshalAction(attributes);
                          break;
      case COMMAND     : push(new StringBuilder());
                         break;
      case OPTION      : push(attributes.getValue(ATTRIBUTE_NAME));
                         push(new StringBuilder());
                         break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endElement (overridden)
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
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   */
  @Override
  @SuppressWarnings("unchecked") 
  public void endElement(final String uri, final String localName, final String qualifiedName)
    throws SAXException {

    // dispatch to handling method
    switch (this.cursor) {
      case DESCRIPTOR  : pop();
                         break;
      case TEMPLATE    : // obtain the template mapping from the stack to have
                         // access to that descriptor as the first element on
                         // the stack (navigation rule)
                         final Descriptor.Template template = (Descriptor.Template)pop();
                         // there must be a descriptor mapping on the value
                         // stack
                         ((Descriptor)peek()).template.add(template);
                         //this.descriptor.attribute.add((Descriptor.Attribute)pop());
                         break;
      case ATTRIBUTE   : // obtain the attribute mapping from the stack to have
                         // access to that descriptor as the first element on
                         // the stack (navigation rule)
                         final Descriptor.Attribute attribute = (Descriptor.Attribute)pop();
                         // there must be a descriptor mapping on the value
                         // stack
                         ((Descriptor)peek()).attribute.add(attribute);
                         //this.descriptor.attribute.add((Descriptor.Attribute)pop());
                         break;
      case REFERENCE   : // obtain the descriptor mapping from the stack to have
                         // access to that descriptor as the first element on
                         // the stack (navigation rule)
                         final Descriptor mapping = (Descriptor)pop();
                         // obtain the descriptor systemId from the stack to
                         // have as the second element on the stack (navigation rule)
                         final Pair<String, String> name  = (Pair<String, String>)pop();
                         // there must be a descriptor mapping on the value
                         // stack
                         ((Descriptor)peek()).reference.put(name, mapping);
                         break;
      case FLAG        : unmarshalFlag();
                         break;
      case ACTION      : // obtain the value from the stack first to get access
                         // to the descriptor mapping as the first element on
                         // the stack (navigation rule)
                         final Descriptor.Action        action = (Descriptor.Action)pop();
                         // obtain the timing value from the stack
                         final Descriptor.Action.Phase phase   = (Descriptor.Action.Phase)pop();
                         // there must be a descriptor mapping on the value
                         // stack
                         ((Descriptor)peek()).action.put(phase, action);
                         break;
      case COMMAND     : unmarshalCommand();
                         break;
      case OPTION      : unmarshalOption();
                         break;
    }
    // change FSA state
    this.cursor = this.state.pop();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   character (overridden)
  /**
   ** Receive notification of character data inside an element.
   ** <p>
   **
   ** @param buffer              the character buffer parsed so far.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   ** @param start               the start position in the character array.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param length              the number of characters to use from the
   **                            character array.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  public void characters(char buffer[], int start, int length) {
    switch (this.cursor) {
      case FLAG    :
      case COMMAND :
      case OPTION  : // if we have a flag, a script or an option there must be a
                     // StringBuilder on the value stack
                     ((StringBuilder)peek()).append(buffer, start, length);
                     break;
      default      : break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalDescriptor
  /**
   ** Factory method to fetch the <code>Descriptor</code> attributes from a
   ** XML stream.
   **
   ** @param  attributes         the {@link Attributes} providing access to
   **                            the descriptor configuration.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       if the required properties name has no value.
   */
  protected void unmarshalDescriptor(final Attributes attributes)
    throws SAXException {

    // XSD specifies the identifier attributeis required
    if (StringUtility.isEmpty(attributes.getValue(Descriptor.IDENTIFIER)))
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, Descriptor.IDENTIFIER));
    // XSD specifies the unique name attribute is required
    if (StringUtility.isEmpty(attributes.getValue(Descriptor.UNIQUENAME)))
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, Descriptor.UNIQUENAME));

    this.descriptor.identifier(attributes.getValue(Descriptor.IDENTIFIER));
    this.descriptor.uniqueName(attributes.getValue(Descriptor.UNIQUENAME));
    this.descriptor.status(attributes.getValue(Descriptor.STATUS));
    this.descriptor.password(attributes.getValue(Descriptor.PASSWORD));
    this.descriptor.transformation(booleanValue(attributes.getValue(Descriptor.TRANSFORMATION), false));
    // put the collection for attributes and their transformations on the value
    // stack
    push(this.descriptor);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalTemplate
  /**
   ** Starts unmarshalling of a <code>Template</code> from the XML stream.
   **
   ** @param  attributes         the attributes attached to the
   **                            <code>Template</code> element.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       if the parser detects that there is no
   **                            identifier for the attribute or the identifier
   **                            itself is empty.
   */
  protected void unmarshalTemplate(final Attributes attributes)
    throws SAXException {

    // XSD specifies the type name of any element and attribute may exists
    Descriptor.Template.Type type = Descriptor.Template.Type.STRING;
    if (!StringUtility.isEmpty(attributes.getValue(ATTRIBUTE_TYPE))) {
      try {
        type = Descriptor.Template.Type.from(attributes.getValue(ATTRIBUTE_TYPE));
      }
      catch (IllegalArgumentException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), attributes.getValue(ATTRIBUTE_TYPE), ATTRIBUTE_TYPE };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
      }
    }
    // XSD specifies the target name of any element and attribute must exists
    String name = attributes.getValue(ATTRIBUTE_NAME);
    if (StringUtility.isEmpty(name)) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_NAME };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
    }
    // XSD specifies the source name of any element and attribute must exists
    String source = attributes.getValue(ATTRIBUTE_SOURCE);
    if (StringUtility.isEmpty(source)) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_SOURCE };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
    }

    // XSD specifies the transformer class of any element and attribute may
    // exists hence no further validation required
    final String transformer = attributes.getValue(ATTRIBUTE_TRANSFORMER);
    if (!StringUtility.isEmpty(transformer)) {
      this.descriptor.transformer.put(name, transformer);
    }
    // put the template on the value stack
    push(Descriptor.buildTemplate(type, name, source));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalAttribute
  /**
   ** Starts unmarshalling of an <code>Attribute</code> from the XML stream.
   **
   ** @param  attributes         the attributes attached to the
   **                            <code>Attribute</code> element.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       if the parser detects that there is no
   **                            identifier for the attribute or the identifier
   **                            itself is empty.
   */
  protected void unmarshalAttribute(final Attributes attributes)
    throws SAXException {

    // XSD specifies the type name of any element and attribute may exists
    Descriptor.Attribute.Type type = Descriptor.Attribute.Type.STRING;
    if (!StringUtility.isEmpty(attributes.getValue(ATTRIBUTE_TYPE))) {
      try {
        type = Descriptor.Attribute.Type.from(attributes.getValue(ATTRIBUTE_TYPE));
      }
      catch (IllegalArgumentException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), attributes.getValue(ATTRIBUTE_TYPE), ATTRIBUTE_TYPE };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
      }
    }
    // XSD specifies the target name of any element and attribute must exists
    String name = attributes.getValue(ATTRIBUTE_NAME);
    if (StringUtility.isEmpty(name)) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_NAME };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
    }
    // XSD specifies the source name of any element and attribute must exists
    String source = attributes.getValue(ATTRIBUTE_SOURCE);
    if (StringUtility.isEmpty(source)) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_SOURCE };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
    }

    // XSD specifies the transformer class of any element and attribute may
    // exists hence no further validation required
    final String transformer = attributes.getValue(ATTRIBUTE_TRANSFORMER);
    if (!StringUtility.isEmpty(transformer)) {
      this.descriptor.transformer.put(name, transformer);
    }
    // put the attribute on the value stack
    push(Descriptor.buildAttribute(type, name, source));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalTransformer
  /**
   ** Starts unmarshalling of a <code>Transformer</code> from the XML stream.
   **
   ** @param  attributes         the attributes attached to the
   **                            <code>Transformer</code> element.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       if the parser detects that there is no
   **                            identifier for the attribute or the identifier
   **                            itself is empty.
   */
  protected void unmarshalTransformer(final Attributes attributes)
    throws SAXException {

    // XSD specifies the target name of any element and attribute must exists
    String name = attributes.getValue(ATTRIBUTE_NAME);
    if (StringUtility.isEmpty(name)) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_NAME };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
    }
    // XSD specifies the source name of any element and attribute must exists
    String clazz = attributes.getValue(ATTRIBUTE_CLASS);
    if (StringUtility.isEmpty(clazz)) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_CLASS };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
    }
    // there must be a descriptor mapping on the value stack
    ((Descriptor)peek()).transformer.put(name, clazz);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalReference
  /**
   ** Starts unmarshalling of a multi-valued <code>Attribute</code> descriptor
   ** from the XML stream.
   **
   ** @param  attributes         the attributes attached to the multi-valued
   **                            <code>Attribute</code> descriptor element.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       if the parser detects that there is no
   **                            identifier for the attribute or the identifier
   **                            itself is empty.
   */
  protected void unmarshalReference(final Attributes attributes)
    throws SAXException {

    // XSD specifies the target name of any element and attribute must exists
    final String name = attributes.getValue(ATTRIBUTE_NAME);
    if (StringUtility.isEmpty(name)) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_NAME };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
    }
    final String source = attributes.getValue(ATTRIBUTE_SOURCE);
    // the system id of the multi-valued element and its source on the stack
    push(Pair.of(name, source == null ? name : source));
    final Descriptor reference = Descriptor.buildReference(this.descriptor);
    reference.transformation(booleanValue(attributes.getValue(Descriptor.TRANSFORMATION), false));
    // put an empty collection on the stack to collect the attribute mapping and
    // their transformations on the stack
    push(reference);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalFlag
  /**
   ** Starts unmarshalling of a <code>Flag</code> from the XML stream.
   **
   ** @throws SAXException       if the parser detects that there is no
   **                            flag for the attribute or the identifier
   **                            itself is misspelled.
   */
  protected void unmarshalFlag()
    throws SAXException {

    final StringBuilder builder = (StringBuilder)pop();
    final String        value   = builder.toString();
    if (!StringUtility.isEmpty(value)) {
      try {
        // there must be a provisioning attribute on the value stack
        ((Descriptor.Attribute)peek()).flag().add(Descriptor.Attribute.Flag.from(value));
      }
      catch (IllegalArgumentException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), value, ELEMENT_FLAG };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_VALUE, arguments));
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalAction
  /**
   ** Starts unmarshalling of an <code>Action</code> from the XML stream.
   **
   ** @param  attributes         the attributes attached to the
   **                            <code>Attribute</code> element.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       if the parser detects that there is no
   **                            flag for the attribute or the identifier
   **                            itself is misspelled.
   */
  protected void unmarshalAction(final Attributes attributes)
    throws SAXException {

    // XSD specifies the timing name of any element and attribute may exists
    if (StringUtility.isEmpty(attributes.getValue(ATTRIBUTE_PHASE)))
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, ATTRIBUTE_PHASE));

    Descriptor.Action.Phase phase = null;
    try {
      phase = Descriptor.Action.Phase.from(attributes.getValue(ATTRIBUTE_PHASE));
    }
    catch (IllegalArgumentException e) {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), attributes.getValue(ATTRIBUTE_PHASE), ATTRIBUTE_PHASE };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
    }

    // XSD specifies the language name of any element and attribute may exists
    Descriptor.Action.Target target = Descriptor.Action.Target.CONNECTOR;
    // XSD specifies the target name of any element and attribute may exists
    if (!StringUtility.isEmpty(attributes.getValue(ATTRIBUTE_TARGET))) {
      try {
        target = Descriptor.Action.Target.from(attributes.getValue(ATTRIBUTE_TARGET));
      }
      catch (IllegalArgumentException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), attributes.getValue(ATTRIBUTE_TARGET), ATTRIBUTE_TARGET };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
      }
    }

    Descriptor.Action.Language language = Descriptor.Action.Language.GROOVY;
    if (!StringUtility.isEmpty(attributes.getValue(ATTRIBUTE_LANGUAGE))) {
      try {
        language = Descriptor.Action.Language.from(attributes.getValue(ATTRIBUTE_LANGUAGE));
      }
      catch (IllegalArgumentException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), attributes.getValue(ATTRIBUTE_LANGUAGE), ATTRIBUTE_LANGUAGE };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
      }
    }
    // put the timing on the value stack; will be used for mapping purpose only
    push(phase);
    // put the action on the value stack
    push(Descriptor.buildAction(target, language));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalCommand
  /**
   ** Starts unmarshalling of a command from the XML stream.
   **
   ** @throws SAXException       if the parser detects that the command syntax
   **                            is misspelled.
   */
  protected void unmarshalCommand()
    throws SAXException {

    final StringBuilder builder = (StringBuilder)pop();
    final String        value   = builder.toString();
    if (!StringUtility.isEmpty(value)) {
      try {
        // there must be a provisioning action on the value stack
        ((Descriptor.Action)peek()).command(value);
      }
      catch (IllegalArgumentException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), value, ELEMENT_FLAG };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_VALUE, arguments));
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: unmarshalOption
  /**
   ** Starts unmarshalling of an <code>Option</code> from the XML stream.
   **
   ** @throws SAXException       if the parser detects that there is no
   **                            flag for the attribute or the identifier
   **                            itself is misspelled.
   */
  @SuppressWarnings("unchecked") 
  protected void unmarshalOption()
    throws SAXException {

    // obtain the option value from the stack first to get access to the value
    final StringBuilder builder = (StringBuilder)pop();
    final String        value   = builder.toString();
    if (!StringUtility.isEmpty(value)) {
      try {
        // secondary obtain the option name from the stack first to get access to the
        // option name
        final Pair pair = Pair.of((String)pop(), value);
        // there must be a provisioning action on the value stack
        ((Descriptor.Action)peek()).option().add(pair);
      }
      catch (IllegalArgumentException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), value, ELEMENT_FLAG };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_VALUE, arguments));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  private static boolean booleanValue(final String value, final boolean defaultValue) {
    return StringUtility.isEmpty(value) ? defaultValue : Boolean.valueOf(value);
  }
}
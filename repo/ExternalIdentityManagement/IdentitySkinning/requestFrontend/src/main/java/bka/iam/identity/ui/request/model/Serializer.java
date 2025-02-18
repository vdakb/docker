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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Special Account Request

    File        :   Serializer.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Serializer.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.ui.request.model;

import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

import org.w3c.dom.Document;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.DOMInput;
import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.hst.foundation.resource.XMLStreamBundle;

import bka.iam.identity.ui.RequestError;
import bka.iam.identity.ui.RequestException;

import bka.iam.identity.ui.request.Adapter;

////////////////////////////////////////////////////////////////////////////////
// class Serializer
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>Serializer</code> provide methods to populate extended
 ** <code>Request Template</code> configuration from Matadata Service.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
class Serializer extends XMLProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ELEMENT_CONFIGURATION = "configuration";
  public static final String ELEMENT_ENVIRONMENT   = "environment";
  public static final String ELEMENT_TEMPLATE      = "template";
  public static final String ELEMENT_PREDECESSOR   = "predecessor";
  public static final String ELEMENT_APPLICATION   = "application";
  public static final String ELEMENT_ENTITLEMENT   = "entitlement";
  public static final String ELEMENT_ATTRIBUTE     = "attribute";

  public static final String ATTRIBUTE_ID          = "id";
  public static final String ATTRIBUTE_BULK        = "bulk";
  public static final String ATTRIBUTE_TYPE        = "type";
  public static final String ATTRIBUTE_LABEL       = "label";
  public static final String ATTRIBUTE_MAPPING     = "mapping";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  /////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Grammar
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants.
   */
  enum Grammar {
      /** the encoded valid grammar state transitions in the parser. */
      INIT(new Grammar[0], "")
    , CONFIGURATION(INIT,                                                 ELEMENT_CONFIGURATION)
    , ENVIRONMENT  (CONFIGURATION,                                        ELEMENT_ENVIRONMENT)
    , TEMPLATE     (ENVIRONMENT,                                          ELEMENT_TEMPLATE)
    , PREDECESSOR  (TEMPLATE,                                             ELEMENT_PREDECESSOR)
    , APPLICATION  (TEMPLATE,                                             ELEMENT_APPLICATION)
    , ENTITLEMENT  (APPLICATION,                                          ELEMENT_ENTITLEMENT)
    , ATTRIBUTE    (new Grammar[]{APPLICATION, PREDECESSOR, ENTITLEMENT}, ELEMENT_ATTRIBUTE)
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
  // ~~~~~ ~~~~~~
  /**
   ** Handles parsing of a XML file which defines the mapping descriptor.
   */
  private class Parser extends DOMInput {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Grammar              cursor = Grammar.INIT;
    private final Stack<Grammar> state  = new Stack<Grammar>();
    private final Configuration  model;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Parser</code>.
     **
     ** @param  feature          the {@link Configuration} to be unmarshalled by
     **                          this {@link XMLProcessor}.
     **                          <br>
     **                          Allowed object is {@link Configuration}.
     **
     ** @throws SAXException     in case {@link DOMInput} is not able to create
     **                          an appropriate parser.
     */
    private Parser(final Configuration  model)
      throws SAXException {

      // ensure inheritance
      super();

      // initialize instance attributes
      this.model = model;
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
      try {
        switch (state) {
          case CONFIGURATION : this.model.bulk(Boolean.valueOf(attributes.getValue(ATTRIBUTE_BULK)).booleanValue());
                               break;
          case ENVIRONMENT   : push(new Environment(attributes.getValue(ATTRIBUTE_ID), attributes.getValue(ATTRIBUTE_LABEL)));
                               break;
          case TEMPLATE      : push(new Template(attributes.getValue(ATTRIBUTE_ID), attributes.getValue(ATTRIBUTE_LABEL)));
                               break;
          case PREDECESSOR   : 
          case APPLICATION   : push(new Application(attributes.getValue(ATTRIBUTE_ID)));
                               break;
          case ENTITLEMENT   : push(new Permission(attributes.getValue(ATTRIBUTE_ID)));
                               break;
          case ATTRIBUTE     : push(new Attribute(attributes.getValue(ATTRIBUTE_ID), attributes.getValue(ATTRIBUTE_LABEL), attributes.getValue(ATTRIBUTE_MAPPING), attributes.getValue(ATTRIBUTE_TYPE)));
                               push(new StringBuilder());
                               break;
          default            : break;
        }
      }
      catch (RequestException e) {
        throw new SAXException(e.getLocalizedMessage());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endElement (overridden)
    /**
     ** Receive notification of the end of an element.
     ** <p>
     ** The SAX parser will invoke this method at the end of every element in
     ** the XML document; there will be a corresponding
     ** {@link #startElement(String, String, String, Attributes) startElement} event for every endElement event
     ** (even when the element is empty).
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
        case ENVIRONMENT : final Environment environment = (Environment)pop();
                           this.model.add(environment);
                           break;
        case TEMPLATE    : final Template template = (Template)pop();
                           ((Environment)peek()).add(template);
                           break;
        case PREDECESSOR : final Application predecessor = (Application)pop();
                           ((Template)peek()).predecessor = predecessor;
                           break;
        case APPLICATION : final Application application = (Application)pop();
                           ((Template)peek()).add(application);
                           break;
        case ENTITLEMENT : final Permission permission = (Permission)pop();
                           ((Application)peek()).add(permission);
                           break;
        case ATTRIBUTE   : final StringBuilder         value     = (StringBuilder)pop();
                           final Attribute attribute = (Attribute)pop();
                           attribute.value(value.toString());
                           // if an attribute is about to handle there must be
                           // an attribute container on the stack
                           ((Permission)peek()).add(attribute);
                           break;
      }
      // change FSA state
      this.cursor = this.state.pop();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: characters (overridden)
    /**
     ** Receive notification of character data.
     ** <p>
     ** The Parser will call this method to report each chunk of character data.
     ** SAX parsers may return all contiguous character data in a single chunk,
     ** or they may split it into several chunks; however, all of the characters
     ** in any single event must come from the same external entity so that the
     ** {@link org.xml.sax.Locator} provides useful information.
     ** <p>
     ** The application must not attempt to read from the array outside of the
     ** specified range.
     ** <p>
     ** Individual characters may consist of more than one Java
     ** <code>char</code> value. There are two important cases where this
     ** happens, because characters can't be represented in just sixteen bits.
     ** In one case, characters are represented in a <em>Surrogate Pair</em>,
     ** using two special Unicode values. Such characters are in the so-called
     ** "Astral Planes", with a code point above U+FFFF. A second case involves
     ** composite characters, such as a base character combining with one or
     ** more accent characters.
     ** <p>
     ** Your code should not assume that algorithms using
     ** <code>char</code>-at-a-time idioms will be working in character units;
     ** in some cases they will split characters. This is relevant wherever XML
     ** permits arbitrary characters, such as attribute values, processing
     ** instruction data, and comments as well as in data reported from this
     ** method. It's also generally relevant whenever Java code manipulates
     ** internationalized text; the issue isn't unique to XML.
     ** <p>
     ** Note that some parsers will report whitespace in element content using
     ** the {@link #ignorableWhitespace ignorableWhitespace} method rather than
     ** this one (validating parsers <em>must</em> do so).
     **
     ** @param  buffer           the characters from the XML document.
     ** @param  start            the start position in the array.
     ** @param  length           the number of characters to read from the
     **                          array.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     ** @see    #ignorableWhitespace
     ** @see    org.xml.sax.Locator
     */
    @Override
    @SuppressWarnings("unchecked")
    public void characters(final char[] buffer, final int start, final int length)
      throws SAXException {

      // where we are?
      switch (this.cursor) {
        case ATTRIBUTE   : // if we have a mapping or an entitlement element
                           // there must be a StringBuilder on the value stack
                           ((StringBuilder)peek()).append(buffer, start, length);
                           break;
        default          : break;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Serializer</code> backing bean that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Serializer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to unmarshal a {@link Configuration} from an
   ** {@link Document}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled by
   **                            this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  document           the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link Document}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void unmarshal(final Configuration feature, final Document document)
    throws RequestException {

    // prevent bogus input
    if (document == null)
      throw Adapter.exception(RequestError.ARGUMENT_IS_NULL, "document");

    final Serializer factory = new Serializer();
    factory.parse(feature, document);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Factory method to parse a {@link Configuration} from an
   ** {@link Document}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled by
   **                            this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @throws RequestException  in the event of misconfiguration (such as
   **                           failure to set an essential property) or if
   **                           initialization fails.
   */
  private void parse(final Configuration feature, final Document document)
    throws RequestException {

    // prevent bogus input
    if (document == null)
      throw Adapter.exception(RequestError.ARGUMENT_IS_NULL, "document");

    try {
      final Parser parser = new Parser(feature);
      parser.processDocument(document);
    }
    catch (SAXException e) {
      throw Adapter.exception(RequestError.REQUEST_CONFIGURATION_PARSING, e);
    }
  }
}
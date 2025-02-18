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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   FeatureFactory.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MDSIOException;

import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.hst.foundation.resource.XMLStreamBundle;

////////////////////////////////////////////////////////////////////////////////
// final class FeatureFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of feature description for
 ** a Connection..
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public final class FeatureFactory extends XMLProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  NAMESPACE                      = "http://www.oracle.com/schema/iam/sap";

  private static final String ELEMENT_FEATURE                = "feature";
  private static final String ELEMENT_JCO                    = "jco";
  private static final String ELEMENT_JCO_CLIENT             = "client";
  private static final String ELEMENT_JCO_CLIENT_TRACE       = "trace";
  private static final String ELEMENT_JCO_CLIENT_CPIC        = "cpic";
  private static final String ELEMENT_JCO_CLIENT_TRACE_LEVEL = "level";
  private static final String ELEMENT_JCO_CLIENT_SNC         = "snc";
  private static final String ELEMENT_JCO_CLIENT_SNC_QUALITY = "quality";
  private static final String ELEMENT_JCO_CLIENT_SNC_LOCAL   = "local";
  private static final String ELEMENT_JCO_CLIENT_SNC_REMOTE  = "remote";
  private static final String ELEMENT_JCO_CLIENT_SNC_LIBRARY = "library";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Feature       feature;

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
      INIT                   (new Grammar[0], "")
    , FEATURE                (INIT,           ELEMENT_FEATURE)
    , JCO                    (FEATURE,        ELEMENT_JCO)
    , JCO_CLIENT             (JCO,            ELEMENT_JCO_CLIENT)
    , JCO_CLIENT_TRACE       (JCO_CLIENT,     ELEMENT_JCO_CLIENT_TRACE)
    , JCO_CLIENT_CPIC        (JCO_CLIENT,     ELEMENT_JCO_CLIENT_CPIC)
    , JCO_CLIENT_TRACE_LEVEL (JCO_CLIENT,     ELEMENT_JCO_CLIENT_TRACE_LEVEL)
    , JCO_CLIENT_SNC         (JCO_CLIENT,     ELEMENT_JCO_CLIENT_SNC)
    , JCO_CLIENT_SNC_QUALITY (JCO_CLIENT_SNC, ELEMENT_JCO_CLIENT_SNC_QUALITY)
    , JCO_CLIENT_SNC_LOCAL   (JCO_CLIENT_SNC, ELEMENT_JCO_CLIENT_SNC_LOCAL)
    , JCO_CLIENT_SNC_REMOTE  (JCO_CLIENT_SNC, ELEMENT_JCO_CLIENT_SNC_REMOTE)
    , JCO_CLIENT_SNC_LIBRARY (JCO_CLIENT_SNC, ELEMENT_JCO_CLIENT_SNC_LIBRARY)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:5376245079552119836")
    private static final long serialVersionUID = 1L;

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
      switch (this.cursor) {
        case FEATURE             : processFeature(attributes);
                                   break;
        default                  : break;
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
        case JCO_CLIENT_TRACE       : FeatureFactory.this.feature.put(Feature.TRACE_ENABLED, String.copyValueOf(buffer, start, length));
                                      break;
        case JCO_CLIENT_CPIC        : FeatureFactory.this.feature.put(Feature.TRACE_LEVEL_CPIC, String.copyValueOf(buffer, start, length));
                                      break;
        case JCO_CLIENT_TRACE_LEVEL : FeatureFactory.this.feature.put(Feature.TRACE_LEVEL, String.copyValueOf(buffer, start, length));
                                      break;
        case JCO_CLIENT_SNC_QUALITY : FeatureFactory.this.feature.put(Feature.SECURE_LEVEL, String.copyValueOf(buffer, start, length));
                                      break;
        case JCO_CLIENT_SNC_LOCAL   : FeatureFactory.this.feature.put(Feature.SECURE_NAME_LOCAL, String.copyValueOf(buffer, start, length));
                                      break;
        case JCO_CLIENT_SNC_REMOTE  : FeatureFactory.this.feature.put(Feature.SECURE_NAME_REMOTE, String.copyValueOf(buffer, start, length));
                                      break;
        case JCO_CLIENT_SNC_LIBRARY : FeatureFactory.this.feature.put(Feature.SECURE_LIBRARY_PATH, String.copyValueOf(buffer, start, length));
                                      break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   processFeature
    /**
     ** Factory method to fetch the <code>Feature</code> attributes from a
     ** XML stream.
     **
     ** @throws SAXException       if the required attribute name has no value.
     */
    private void processFeature(final Attributes attributes)
      throws SAXException {

      for (int i = 0; i < attributes.getLength(); i++) {
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FeatureFactory</code>.
   **
   ** @param  feature            the {@link Feature} that this factory will
   **                            configure.
   */
  private FeatureFactory(final Feature feature) {
    // ensure inheritance
    super();

    // initialize instance
    this.feature = feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from a {@link File}.
   **
   ** @param  feature            the {@link Feature} to be configured by this
   **                            {@link XMLProcessor}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static void configure(final Feature feature, final File file)
    throws ConnectionException {

    configure(feature, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from a {@link File}.
   **
   ** @param  feature            the {@link Feature} to be configured by this
   **                            {@link XMLProcessor}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static void configure(final Feature feature, final File file, final boolean validate)
    throws ConnectionException {

    try {
      configure(feature, new FileInputStream(file), validate);
    }
    catch (IOException e) {
      throw new ConnectionException(ConnectionError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link Feature} to be configured by this
   **                            {@link XMLProcessor}.
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static void create(final Feature feature, final InputStream stream)
    throws ConnectionException {

    configure(feature, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link Feature} to be configured by this
   **                            {@link XMLProcessor}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static Feature configure(final Feature feature, final InputStream stream, final boolean validate)
    throws ConnectionException {

    // prevent bogus input
    if (stream == null)
      throw new ConnectionException(ConnectionError.ARGUMENT_IS_NULL, "stream");

    configure(feature, new InputSource(stream), validate);
    try {
      stream.close();
    }
    catch (IOException e) {
      throw new ConnectionException(ConnectionError.ABORT, e);
    }
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link Feature} to be configured by this
   **                            {@link XMLProcessor}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static void configure(final Feature feature, final InputSource source)
    throws ConnectionException {

    configure(feature, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link Feature} to be configured by this
   **                            {@link XMLProcessor}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static void configure(final Feature feature, final InputSource source, final boolean validate)
    throws ConnectionException {

    // validate the provided stream against the schema
    final FeatureFactory factory = new FeatureFactory(feature);
    if (validate) {
      try {
        factory.validate(Feature.class, source);
      }
      catch (XMLException e) {
        throw new ConnectionException(e);
      }
    }
    factory.configure(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link Feature} to be configured by this
   **                            {@link XMLProcessor}.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  public static void configure(final Feature feature, final PDocument document)
    throws ConnectionException {

    // prevent bogus input
    if (document == null)
      throw new ConnectionException(ConnectionError.ARGUMENT_IS_NULL, "document");

    final FeatureFactory factory = new FeatureFactory(feature);
    try {
      factory.configure(document.read());
    }
    catch (MDSIOException e) {
      throw new ConnectionException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Feature} from an {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws ConnectionException  in the event of misconfiguration (such as
   **                              failure to set an essential property) or if
   **                              initialization fails.
   */
  private void configure(final InputSource source)
    throws ConnectionException {

    // prevent bogus input
    if (source == null)
      throw new ConnectionException(ConnectionError.ARGUMENT_IS_NULL, "source");

    try {
      final Parser parser = new Parser();
      parser.processDocument(source);
    }
    catch (SAXException e) {
      throw new ConnectionException(e);
    }
  }
}
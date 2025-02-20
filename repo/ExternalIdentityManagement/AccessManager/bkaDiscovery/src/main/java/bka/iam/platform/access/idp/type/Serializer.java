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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Frontend Extension
    Subsystem   :   Identity Provider Discovery

    File        :   Serializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Serializer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.idp.type;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import oracle.hst.foundation.resource.XMLStreamBundle;
import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLOutputNode;
import oracle.hst.foundation.xml.XMLProcessor;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

////////////////////////////////////////////////////////////////////////////////
// final class Serializer
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** This is the class to create meta information for a
 ** <code>Credentail Collector</code> configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Serializer extends XMLProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         PROLOG  = "<?xml version=\"1.0\" encoding=\"US-ASCII\" standalone=\"yes\"?>";

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
    , ROOT          (INIT,                             Configuration.ROOT)
    , NETWORK       (ROOT,                             Configuration.NETWORK)
    , PROVIDER      (ROOT,                             Configuration.PROVIDER)
    , CIDR          (NETWORK,                          Configuration.CIDR)
    , ID            (PROVIDER,                         Configuration.ID)
    , NAME          (PROVIDER,                         Configuration.NAME)
    , SYMBOL        (PROVIDER,                         Configuration.SYMBOL)
    , PARTNER       (new Grammar[]{PROVIDER, NETWORK}, Configuration.PARTNER)
    , OTP_TEMPLATE  (ROOT,                             Configuration.OTP_TEMPLATE)
    , LOCALE        (OTP_TEMPLATE,                     Configuration.LOCALE)
    , SUBJECT       (OTP_TEMPLATE,                     Configuration.SUBJECT)
    , BODY          (OTP_TEMPLATE,                     Configuration.BODY)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
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
    private final Configuration  feature;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Parser</code>.
     **
     ** @param  feature          the {@link Configuration} to be unmarshalled
     **                          by this {@link XMLProcessor}.
     **                          <br>
     **                          Allowed object is {@link Configuration}.
      **
     ** @throws SAXException     in case {@link SAXInput} is not able to create
     **                          an appropriate parser.
     */
    private Parser(final Configuration  feature)
      throws SAXException {

      // ensure inheritance
      super();

      // initialize instance attributes
      this.feature = feature;
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
      switch (state) {
        case ROOT           : break;
        case ID             :
        case NAME           :
        case CIDR           :
        case SYMBOL         :
        case PARTNER        :
	case LOCALE         :
	case SUBJECT        :
	case BODY           : push(new StringBuilder());
                              break;
        case PROVIDER       : push(Provider.build());
                              break;
        case NETWORK        : push(Network.build());
                              break;
	case OTP_TEMPLATE   : push(new OTPTemplate());;
			      break;      
        default             : break;
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
        case NETWORK        : final Network holder = (Network)pop();
                              this.feature.network().put(Subnet.build(holder.cidr), this.feature.provider().get(holder.partner));
                              break;
        case PROVIDER       : final Provider provider = (Provider)pop();
                              this.feature.provider().put(provider.id, provider);
                              break;
        case CIDR           : String cidr = pop().toString();
                              ((Network)peek()).cidr = cidr;
                              break;
        case ID             : String identifier = pop().toString();
                              ((Provider)peek()).id = identifier;
                              break;
        case NAME           : String name = pop().toString();
                              ((Provider)peek()).name = name;
                              break;
        case SYMBOL         : String symbol = pop().toString();
                              ((Provider)peek()).symbol = symbol;
                              break;
        case PARTNER        : String partner = pop().toString();
                              if (peek() instanceof Provider)
                                ((Provider)peek()).partner = partner;
                              else if (peek() instanceof Network)
                                ((Network)peek()).partner = partner;
                              else
                                throw new SAXException(peek().getClass().getName());
                              break;
	case OTP_TEMPLATE   : OTPTemplate otpTemplate = (OTPTemplate)pop();
			      this.feature.otpTemplate().add(otpTemplate);
			      break;
	case LOCALE         : String locale = pop().toString();
			      ((OTPTemplate)peek()).setLocale(locale);
			      break;
	case SUBJECT         : String subject = pop().toString();
			      ((OTPTemplate)peek()).setSubject(subject);
				break;
	case BODY            : 
			      String body = decodeBody(pop().toString(),(OTPTemplate)peek());
			      ((OTPTemplate)peek()).setBody(body);
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
        case ID             :
        case NAME           :
        case CIDR           :
        case PARTNER        :
        case SYMBOL         :
	case LOCALE         :
	case SUBJECT        :
	case BODY           :// if we have an attribute there must be a
                              // StringBuilder on the value stack
                              ((StringBuilder)peek()).append(buffer, start, length);
                              break;
        default             : break;
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
  // Method:   marshal
  /**
   ** Marshal a java content tree as XML data to the specified URL.
   **
   ** @param  feature            the {@link Configuration} to be marshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  url                the destination for the descriptor to persist.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void marshal(final Configuration feature, final URL url)
    throws XMLException {

    marshal(feature, url.toExternalForm());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Marshall a {@link Configuration} to a {@link File} specified by
   ** <code>file</code>.
   **
   ** @param  feature            the {@link Configuration} to be marshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  file               the destination for the descriptor to persist.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void marshal(final Configuration feature, final String file)
    throws XMLException {

    marshal(feature, new File(file));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Marshall a {@link Configuration} to a {@link File}.
   **
   ** @param  feature            the {@link Configuration} to be marshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  file               the destination for the descriptor to persist.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void marshal(final Configuration feature, final File file)
    throws XMLException {

    // create the enclosing element of a configuration
    final XMLOutputNode root = XMLProcessor.marshal(null, file, new XMLFormat(PROLOG)).element(Configuration.ROOT);
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS,     Configuration.NAMESPACE);
    root.attribute(XMLProcessor.ATTRIBUTE_XMLNS_XSI, XMLProcessor.SCHEMA);
    root.attribute(XMLProcessor.ATTRIBUTE_SCHEMA,    Configuration.SCHEMA);
    for (Map.Entry<String, Provider> entry : feature.provider().entrySet()) {
      // create the enclosing element of a provider
      XMLOutputNode provider = root.element(Configuration.PROVIDER);
      // create the enclosing element of a provider identifier
      XMLOutputNode item     = provider.element(Configuration.ID);
      item.value(entry.getValue().id);
      // create the enclosing element of a provider name
      item     = provider.element(Configuration.NAME);
      item.value(entry.getValue().name);
      // create the enclosing element of a provider symbol
      item     = provider.element(Configuration.SYMBOL);
      item.value(entry.getValue().symbol);
      // create the enclosing element of a provider partner
      item     = provider.element(Configuration.PARTNER);
      item.value(entry.getValue().partner);
    }
    for (Map.Entry<Subnet, Provider> entry : feature.network().entrySet()) {
      // create the enclosing element of a network
      XMLOutputNode network = root.element(Configuration.NETWORK);
      // create the enclosing element of a network cidr
      XMLOutputNode item    = network.element(Configuration.CIDR);
      item.value(Subnet.cidr(entry.getKey()));
      // create the enclosing element of a network partner
      item = network.element(Configuration.PARTNER);
      item.value(entry.getValue().id);
    }
    for (OTPTemplate entry : feature.otpTemplate()) {
      // create the enclosing element of a otptemplate
      XMLOutputNode otpTemplate = root.element(Configuration.OTP_TEMPLATE);
      // create the enclosing element of a tempalte locale
      XMLOutputNode item  = otpTemplate.element(Configuration.LOCALE);
      item.value(entry.getLocale());  
      // create the enclosing element of a tempalte subject
      item = otpTemplate.element(Configuration.SUBJECT);
      item.value(entry.getSubject());
      // create the enclosing element of a tempalte body
      item = otpTemplate.element(Configuration.BODY);
      item.value(Base64.getEncoder().encodeToString(entry.getBody().getBytes()));
    }
    root.commit();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Unmarshal XML data from the specified URL and return the resulting content
   ** tree.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   **
   ** @param  url                the url to unmarshal XML data from.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   */
  public static void unmarshal(final Configuration feature, final URL url)
    throws SAXException
    ,      IOException {

    unmarshal(feature, url.toExternalForm());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Unmarshal XML data from the specified URL and return the resulting content
   ** tree.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  url                the url to unmarshal XML data from.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void unmarshal(final Configuration feature, final String url)
    throws SAXException {

    unmarshal(feature, new InputSource(url));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to unmarshal a {@link Configuration} from a
   ** {@link File}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   ** @throws IOException        in the event of I/O failures.
   */
  public static void unmarshal(final Configuration feature, final File file)
    throws SAXException
    ,      IOException {

    unmarshal(feature, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to unmarshal a {@link Configuration} from a
   ** {@link File}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   ** @throws IOException        in the event of I/O failures.
   */
  public static void unmarshal(final Configuration feature, final File file, final boolean validate)
    throws SAXException
    ,      IOException {

    try {
      unmarshal(feature, new BufferedInputStream(new FileInputStream(file)), validate);
    }
    catch( FileNotFoundException e ) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to unmarshal a {@link Configuration} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   ** @throws IOException        in the event of I/O failures.
   */
  public static void unmarshal(final Configuration feature, final InputStream stream)
    throws SAXException
    ,      IOException {

    unmarshal(feature, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to unmarshal a {@link Configuration} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   ** @throws IOException        in the event of I/O failures.
   */
  public static void unmarshal(final Configuration feature, final InputStream stream, final boolean validate)
    throws SAXException
    ,      IOException {

    unmarshal(feature, new InputSource(stream), validate);
    stream.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to unmarshal a {@link Configuration} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshaled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void unmarshal(final Configuration feature, final InputSource source)
    throws SAXException {

    unmarshal(feature, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to unmarshal a {@link Configuration} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void unmarshal(final Configuration feature, final InputSource source, final boolean validate)
    throws SAXException {

    final Serializer factory = new Serializer();
    // validate the provided soource against the schema if requested
    if (validate) {
      try {
        factory.validate(Configuration.class, source);
      }
      catch (XMLException e) {
        throw new SAXException(e);
      }
    }
    factory.parse(feature, source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Factory method to parse a {@link Configuration} from an {@link InputSource}.
   **
   ** @param  feature            the {@link Configuration} to be unmarshalled
   **                            by this {@link XMLProcessor}.
   **                            <br>
   **                            Allowed object is {@link Configuration}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private void parse(final Configuration feature, final InputSource source)
    throws SAXException {

    final Parser parser = new Parser(feature);
    parser.processDocument(source);
  }
  
  
  /////////////////////////////////////////////////////////////////////////////
  // Method:   decodeBody
  /**
   ** OTP Body is Base64 encoded, when user modify xml file it could happend that
   ** decode would failded.
   **
   ** @param  emailBody          the {@link String} Base64 encoded email body 
   **                            template
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  template           OTP Template, it is used for providing better
   **                            error message to the log
   **                            <br>
   **                            Allowed object is {@link OTPTemplate}.
   **
   */
  private String decodeBody(String emailBody, OTPTemplate template){
    String body = "";
    try{
      body = new String(Base64.getDecoder().decode(emailBody));
    }
    catch (IllegalArgumentException e){
      //TODO Implement corrent logger
      System.out.println("Failed to decode base64 string: "+ emailBody +" for OTP Email template locale <"+(template != null ? template.getLocale():"")+"> , Error message"+e.getMessage());
    }
    return body;
  }
  
}
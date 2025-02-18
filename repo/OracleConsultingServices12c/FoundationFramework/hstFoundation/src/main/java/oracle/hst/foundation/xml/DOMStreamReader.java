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

    File        :   DOMStreamReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DOMStreamReader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.XMLConstants;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;

import javax.xml.namespace.QName;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.ProcessingInstruction;

import oracle.hst.foundation.resource.XMLStreamBundle;

////////////////////////////////////////////////////////////////////////////////
// class DOMStreamReader
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DOMStreamReader</code> interface allows forward, read-only access
 ** to XML. It is designed to be the lowest level and most efficient way to read
 ** XML data.
 ** <p>
 ** The <code>DOMStreamReader</code> is designed to iterate over XML using
 ** {@link #next()} and {@link #hasNext()}. The data can be accessed using
 ** methods such as {@link #getEventType()}, {@link #getNamespaceURI()},
 ** {@link #getLocalName()} and {@link #getText()}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
class DOMStreamReader implements XMLStreamReader {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The root node for which StAX events are synthesized.
   ** <br>
   ** This may be a {@link Document} or an {@link Element}.
   */
  private final Node       root;

  /**
   ** The DOM node corresponding to the current StAX event.
   ** <p>
   ** This is <code>null</code> if the current event is
   ** {@link #START_DOCUMENT} or {@link #END_DOCUMENT} and {@link #root} is an
   ** {@link Element}.
   */
  private Node             cursor;

  /** The current StAX event type */
  private int              event;

  private boolean          attributesFetched;
  private int              attributeCount;
  private Attr[]           attributes       = new Attr[8];
  private int              namespaceCount;
  private Attr[]           namespaces       = new Attr[8];
  private NamespaceContext namespaceContext;
  private final boolean    expandEntity;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class NamespaceContext
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** Parsing partial {@link NamespaceContext} implementation that takes care
   ** of the implicit namespace bindings (for the <code>xml</code> and
   ** <code>xmlns</code> prefixes) defined in the {@link NamespaceContext}
   ** Javadoc.
   */
  class NamespaceContext extends XMLNamespaceContext {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final DOMStreamReader reader;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>NamespaceContext</code> parser from a
     ** {@link DOMStreamReader}.
     ** <p>
     ** The {@link DOMStreamReader} class allows forward, read-only access to
     ** XML. It is designed to be the lowest level and most efficient way to
     ** read XML data.
     */
    NamespaceContext(final DOMStreamReader reader) {
      // ensure inheritance
      super();

      // initialize instance
      this.reader = reader;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: namespaceURI (XMLNamespaceContext)
    /**
     ** Returns namespace URI bound to a prefix in the current scope.
     ** <p>
     ** The contract of this method is the same as
     ** {@link XMLNamespaceContext#getNamespaceURI(String)}, except that the
     ** implementation is not required to handle the implicit namespace
     ** bindings.
     **
     ** @param  prefix           the prefix to look up.
     **
     ** @return                  the Namespace URI bound to prefix in the
     **                          current scope.
     */
    @Override
    protected String namespaceURI(final String prefix) {
      final String namespaceURI = this.reader.getNamespaceURI(prefix);
      return namespaceURI == null ? "" : namespaceURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefix (XMLNamespaceContext)
    /**
     ** Returns prefix bound to namespace URI in the current scope.
     ** <p>
     ** The contract of this method is the same as
     ** {@link XMLNamespaceContext#getPrefix(String)}, except that the
     ** implementation is not required to handle the implicit namespace
     ** bindings.
     **
     ** @param  namespaceURI     the URI of Namespace to lookup.
     **
     ** @return                  the prefix bound to Namespace URI in current
     **                          context.
     */
    @Override
    protected String prefix(final String namespaceURI) {
      final Set<String> seen = new HashSet<String>();
      Node current = this.reader.cursor();
      do {
        final NamedNodeMap attributes = current.getAttributes();
        if (attributes != null) {
          for (int i = 0, l = attributes.getLength(); i < l; i++) {
            final Attr attr = (Attr)attributes.item(i);
            if (DOMStreamReader.isNSDecl(attr)) {
              String prefix = namespacePrefix(attr);
              if (prefix == null)
                prefix = "";

              if (seen.add(prefix) && attr.getValue().equals(namespaceURI))
                return prefix;
            }
          }
        }
        current = current.getParentNode();
      } while (current != null);
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: prefixes (XMLNamespaceContext)
    /**
     ** Returns all prefixes bound to a namespace URI in the current scope.
     ** <p>
     ** The contract of this method is the same as
     ** {@link NamespaceContext#getPrefixes(String)}, except that the
     ** implementation is not required to handle the implicit namespace
     ** bindings.
     **
     ** @param  namespaceURI     the URI of Namespace to lookup.
     **
     ** @return                  an iterator for all prefixes bound to the
     **                          namespace URI in the current scope.
     */
    @Override
    protected Iterator<String> prefixes(final String namespaceURI) {
      // seenPrefixes tracks all prefixes we have encountered; this is important
      // to handle prefixes that are overridden by descendant elements
      final Set<String> seen  = new HashSet<String>();
      final Set<String> match = new HashSet<String>();
      Node current = this.reader.cursor();
      do {
        final NamedNodeMap attributes = current.getAttributes();
        if (attributes != null) {
          for (int i = 0, l = attributes.getLength(); i < l; i++) {
            final Attr attr = (Attr)attributes.item(i);
            if (isNSDecl(attr)) {
              String prefix = namespacePrefix(attr);
              if (prefix == null)
                prefix = "";

              if (seen.add(prefix) && attr.getValue().equals(namespaceURI))
                match.add(prefix);
            }
          }
        }
        current = current.getParentNode();
      } while (current != null);
      return match.iterator();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMStreamReader</code> from a {@link Node}.
   ** <p>
   ** The <code>XMLStreamReader</code> class allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   */
  DOMStreamReader(final boolean expandEntity) {
    // ensure inheritance
    this(null, expandEntity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMStreamReader</code> from a {@link Node}.
   ** <p>
   ** The <code>XMLStreamReader</code> class allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  node               the source to parse the underlying XML.
   */
  DOMStreamReader(final Node node, final boolean expandEntity) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.root         = node;
    this.cursor       = (node != null && node.getNodeType() == Node.DOCUMENT_NODE) ? node : null;
    this.expandEntity = expandEntity;
    this.event        = START_DOCUMENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isNSDecl
  /**
   ** Checks if the specified {@link Attr} is a the namespace URI.
   ** {@link XMLConstants#XMLNS_ATTRIBUTE_NS_URI}.
   **
   ** @param  attribute          the {@link Attr} to check if its a namespace
   **                            URI.
   **
   ** @return                    <code>true</code> if the {@link Attr} is a
   **                            namespace URI; otherwise <code>false</code>.
   */
  static boolean isNSDecl(final Attr attribute) {
    return XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespacePrefix
  /**
   ** Returns the local part of the qualified name of the specified {@link Attr}
   ** if the attribute is prefixed with a namespace.
   **
   ** @param  attribute          the {@link Attr} to inspect.
   **
   ** @return                    <code>null</code> if the {@link Attr} has no
   **                            namespace prefix; otherwise <the localname of
   **                            the {link Attr}.
   */
  static String namespacePrefix(final Attr attribute) {
    final String prefix = attribute.getPrefix();
    return prefix == null ? null : attribute.getLocalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cursor
  /**
   ** Returns the current DOM {@link Node} corresponding to the current StAX
   ** event.
   ** <p>
   ** This is <code>null</code> if the current event is
   ** {@link #START_DOCUMENT} or {@link #END_DOCUMENT} and {@link #root} is an
   ** {@link Element}.
   **
   ** @return                    the current DOM  {@link Node} corresponding to
   **                            the current StAX event.
   **                            May be <code>null</code>.
   */
  final Node cursor() {
    return this.cursor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootName
  /**
   ** Returns the name of DTD; i.e., the name immediately following the
   ** <code>DOCTYPE</code> keyword.
   **
   ** @return                    the public identifier of the external subset.
   */
  public String rootName() {
    return ((DocumentType)this.cursor).getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicId
  /**
   ** Returns the public identifier of the external subset.
   **
   ** @return                    the public identifier of the external subset.
   */
  public String publicId() {
    return ((DocumentType)this.cursor).getPublicId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemId
  /**
   ** Returns the system identifier of the external subset.
   **
   ** @return                    the system identifier of the external subset.
   */
  public String systemId() {
    return ((DocumentType)this.cursor).getSystemId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (XMLStreamReader)
  /**
   ** Frees any resources associated with this Reader.
   ** <p>
   ** This method does not close the underlying input source.
   **
   ** @throws XMLStreamException if there are errors freeing associated
   **                            resources.
   */
  @Override
  public void close()
    throws XMLStreamException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperty (XMLStreamReader)
  /**
   ** Returns the value of a feature/property from the underlying
   ** implementation.
   **
   ** @param  name               the name of the property, may not be
   **                            <code>null</code>.
   **
   ** @return                    the value of the property.
   **
   ** @throws IllegalArgumentException if name is <code>null</code>.
   */
  @Override
  public Object getProperty(final String name) {
    return DTDStreamReader.PROPERTY.equals(name) ? this : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocation (XMLStreamReader)
  /**
   ** Return the current location of the processor.
   ** <p>
   ** If the Location is unknown the processor should return an implementation
   ** of {@link Location} that returns <code>-1</code> for the location and
   ** <code>null</code> for the <code>publicId</code> and <code>systemId</code>.
   ** <p>
   ** The location information is only valid until next() is called.
   */
  @Override
  public Location getLocation() {
    return StreamInput.UNKNOWN;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVersion (XMLStreamReader)
  /**
   ** Returns the xml version declared on the xml declaration. Will be
   ** <code>null</code> if none was declared.
   **
   ** @return                    the XML version or <code>null</code>.
   */
  @Override
  public String getVersion() {
    return this.cursor != null ? ((Document)this.cursor).getXmlVersion() : "1.0";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEncoding (XMLStreamReader)
  /**
   ** Return input encoding if known or <code>null</code> if unknown.
   **
   ** @return                    the encoding of this instance or
   **                            <code>null</code>.
   */
  @Override
  public String getEncoding() {
    if (this.event != START_DOCUMENT)
      throw new IllegalStateException();

    return this.cursor != null ? ((Document)this.cursor).getInputEncoding() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCharacterEncodingScheme (XMLStreamReader)
  /**
   ** Returns the character encoding declared on the xml declaration, will be
   ** <code>null</code> if none was declared.
   **
   ** @return                    the encoding declared in the document or
   **                            <code>null</code>.
   */
  @Override
  public String getCharacterEncodingScheme() {
    if (this.event != START_DOCUMENT)
      throw new IllegalStateException();

    return this.cursor != null ? ((Document)this.cursor).getXmlEncoding() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isStandalone (XMLStreamReader)
  /**
   ** Returns the standalone declaration from the xml declaration.
   **
   ** @return                    <code>true</code> if this is standalone, or
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean isStandalone() {
    return this.cursor != null ? ((Document)this.cursor).getXmlStandalone() : true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   standaloneSet (XMLStreamReader)
  /**
   ** Checks if standalone was set in the document.
   **
   ** @return                    <code>true</code> if standalone was set in the
   **                            document, or <code>false</code> otherwise.
   */
  @Override
  public boolean standaloneSet() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPITarget (XMLStreamReader)
  /**
   ** Returns the target of a processing instruction.
   **
   ** @return                    the target or <code>null</code>.
   */
  @Override
  public String getPITarget() {
    if (this.event != PROCESSING_INSTRUCTION)
      throw new IllegalStateException();

    return ((ProcessingInstruction)this.cursor).getTarget();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPIData (XMLStreamReader)
  /**
   ** Returns the data section of a processing instruction.
   **
   ** @return                    the data or <code>null</code>.
   */
  @Override
  public String getPIData() {
    if (this.event != PROCESSING_INSTRUCTION)
      throw new IllegalStateException();

    return ((ProcessingInstruction)this.cursor).getData();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isStartElement (XMLStreamReader)
  /**
   ** Returns <code>true</code> if the cursor points to a start tag (otherwise
   ** <code>false</code>).
   **
   ** @return                    <code>true</code> if the cursor points to a
   **                            start tag, <code>false</code> otherwise.
   */
  @Override
  public boolean isStartElement() {
    return getEventType() == START_ELEMENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEndElement (XMLStreamReader)
  /**
   ** Returns <code>true</code> if the cursor points to an end tag (otherwise
   ** <code>false</code>).
   **
   ** @return                    <code>true</code> if the cursor points to an
   **                            end tag, <code>false</code> otherwise.
   */
  @Override
  public boolean isEndElement() {
    return getEventType() == END_ELEMENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCharacters (XMLStreamReader)
  /**
   ** Returns <code>true</code> if the cursor points to a character data event
   **
   ** @return                    <code>true</code> if the cursor points to
   **                            character data, <code>false</code> otherwise.
   */
  @Override
  public boolean isCharacters() {
    return getEventType() == CHARACTERS;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWhiteSpace (XMLStreamReader)
  /**
   ** Returns <code>true</code> if the cursor points to a character data event
   ** that consists of all whitespace.
   **
   ** @return                    <code>true</code> if the cursor points to all
   **                            whitespace, <code>false</code> otherwise.
   */
  @Override
  public boolean isWhiteSpace() {
    switch (getEventType()) {
      case SPACE       : return true;
      // XMLStreamReader Javadoc says that isWhiteSpace "returns true if the
      // cursor points to a character data event that consists of all
      // whitespace". This means that this method may return true for a
      // CHARACTER event and we need to scan the text of the node.
      case CHARACTERS  : final String text = getText();
                         for (int i = 0; i < text.length(); i++) {
                           final char c = text.charAt(i);
                           if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                             return false;
                           }
                         }
                         return true;
      default          : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEventType (XMLStreamReader)
  /**
   ** Returns an integer code that indicates the type of the event the cursor is
   ** pointing to.
   **
   ** @return                    an integer code that indicates the type of the
   **                            event the cursor is pointing to.
   */
  @Override
  public int getEventType() {
    return this.event;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasName (XMLStreamReader)
  /**
   ** Returns <code>true</code> if the current event has a name (is a
   ** {@link #START_ELEMENT} or {@link #END_ELEMENT}) returns
   ** <code>false</code> otherwise.
   **
   ** @return                    <code>true</code> if the current event has a
   **                            name, <code>false</code> otherwise.
   */
  @Override
  public boolean hasName() {
    final int event = getEventType();
    return (event == START_ELEMENT || event == END_ELEMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasText (XMLStreamReader)
  /**
   ** Returns <code>true</code> if the current event has text,
   ** <code>false</code> otherwise. The following events have text:
   ** {@link #CHARACTERS}, {@link #DTD}, {@link #ENTITY_REFERENCE},
   ** {@link #COMMENT}, {@link #SPACE}.
   **
   ** @return                    <code>true</code> if the current event has
   **                            text, <code>false</code> otherwise.
   */
  @Override
  public boolean hasText() {
    final int event = getEventType();
    return ((event == CHARACTERS) || (event == DTD) || (event == CDATA) || (event == ENTITY_REFERENCE) || (event == COMMENT) || (event == SPACE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNext (XMLStreamReader)
  /**
   ** Returns <code>true</code> if there are more parsing events and
   ** <code>false</code> if there are no more events. This method will return
   ** <code>false</code> if the current state of the XMLStreamReader is
   ** {@link #END_DOCUMENT}.
   **
   ** @return                    <code>true</code> if there are more events,
   **                            <code>false</code> otherwise.
   **
   ** @throws XMLStreamException if there is a fatal error detecting the next
   **                            state.
   */
  @Override
  public boolean hasNext()
    throws XMLStreamException {

    return this.event != END_DOCUMENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespaceContext (XMLStreamReader)
  /**
   ** Returns a read only namespace context for the current position. The
   ** context is transient and only valid until a call to next() changes the
   ** state of the reader.
   **
   ** @return                    a namespace context
   */
  @Override
  public NamespaceContext getNamespaceContext() {
    // lazy init of the collection
    if (this.namespaceContext == null)
      this.namespaceContext = new NamespaceContext(this);

    return this.namespaceContext;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespaceCount (XMLStreamReader)
  /**
   ** Returns the count of namespaces declared on this {@link #START_ELEMENT} or
   ** END_ELEMENT, this method is only valid on a {@link #START_ELEMENT},
   ** {@link #END_ELEMENT} or {@link #NAMESPACE}. On an {@link #END_ELEMENT} the
   ** count is of the namespaces that are about to go out of scope. This is the
   ** equivalent of the information reported by SAX callback for an end element
   ** event.
   **
   ** @return                    returns the number of namespace declarations on
   **                            this specific element.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT},
   **                               {@link #END_ELEMENT} or {@link #NAMESPACE}.
   */
  @Override
  public int getNamespaceCount() {
    switch (this.event) {
      case START_ELEMENT :
      case END_ELEMENT   : fetchAttributes();
                           return this.namespaceCount;
      default            : throw new IllegalStateException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespacePrefix (XMLStreamReader)
  /**
   ** Returns the prefix for the namespace declared at the index. Returns
   ** <code>null</code> if this is the default namespace declaration.
   **
   ** @param  index              the position of the namespace declaration.
   **
   ** @return                    returns the namespace prefix
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT},
   **                               {@link #END_ELEMENT} or {@link #NAMESPACE}.
   */
  @Override
  public String getNamespacePrefix(int index) {
    return namespacePrefix(namespace(index));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespaceURI (XMLStreamReader)
  /**
   ** If the current event is a {@link #START_ELEMENT} or {@link #END_ELEMENT}
   ** this method returns the URI of the prefix or the default namespace.
   ** <br>
   ** Returns <code>null</code> if the event does not have a prefix.
   **
   ** @return                    the URI bound to this elements prefix, the
   **                            default namespace, or <code>null</code>.
   */
  @Override
  public String getNamespaceURI() {
    switch (this.event) {
      case START_ELEMENT :
      case END_ELEMENT   : return this.cursor.getNamespaceURI();
      default            : throw new IllegalStateException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespaceURI (XMLStreamReader)
  /**
   ** Returns the URI for the namespace declared at the index.
   **
   ** @param  index              the position of the namespace declaration.
   **
   ** @return                    the namespace URI
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT},
   **                               {@link #END_ELEMENT} or {@link #NAMESPACE}.
   */
  @Override
  public String getNamespaceURI(int index) {
    return namespace(index).getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespaceURI (XMLStreamReader)
  /**
   ** Return the URI for the given prefix.
   ** <p>
   ** The URI returned depends on the current state of the processor.
   ** <p>
   ** <strong>NOTE:</strong>
   ** <br>
   ** The 'xml' prefix is bound as defined in
   ** <a href="http://www.w3.org/TR/REC-xml-names/#ns-using">Namespaces in XML</a>
   ** specification to "http://www.w3.org/XML/1998/namespace".
   ** <p>
   ** <strong>NOTE:</strong>
   ** <br>
   ** The 'xmlns' prefix must be resolved to following namespace
   ** <a href="http://www.w3.org/2000/xmlns/">http://www.w3.org/2000/xmlns/</a>.
   **
   ** @param  prefix             the prefix to lookup, may not be
   **                            <code>null</code>.
   **
   ** @return                    the URI bound to the given prefix or
   **                            <code>null</code> if it is not bound.
   **
   ** @throws IllegalArgumentException if the prefix is <code>null</code>
   */
  @Override
  public String getNamespaceURI(String prefix) {
    Node current = this.cursor;
    do {
      NamedNodeMap attributes = current.getAttributes();
      if (attributes != null) {
        for (int i = 0, l = attributes.getLength(); i < l; i++) {
          Attr attr = (Attr)attributes.item(i);
          if (isNSDecl(attr)) {
            String candidatePrefix = namespacePrefix(attr);
            if (candidatePrefix == null) {
              candidatePrefix = "";
            }
            if (candidatePrefix.equals(prefix)) {
              return attr.getValue();
            }
          }
        }
      }
      current = current.getParentNode();
    }
    while (current != null);
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrefix (XMLStreamReader)
  /**
   ** Returns the prefix of the current event or <code>null</code> if the event
   ** does not have a prefix.
   **
   ** @return                    the prefix or <code>null</code>.
   */
  @Override
  public String getPrefix() {
    switch (this.event) {
      case START_ELEMENT :
      case END_ELEMENT   : return this.cursor.getPrefix();
      default            : throw new IllegalStateException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTextStart (XMLStreamReader)
  /**
   ** Returns the offset into the text character array where the first character
   ** (of this text event) is stored.
   **
   ** @return                    the offset into the text character array where
   **                            the first character is stored.
   **
   ** @throws IllegalStateException if this state is not a valid text state.
   */
  @Override
  public int getTextStart() {
    // call internalGetText to throw an IllegalStateException if appropriate
    text();
    return 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTextLength (XMLStreamReader)
  /**
   ** Returns the length of the sequence of characters for this Text event
   ** within the text character array.
   **
   ** @return                    the length of the sequence of characters for
   **                            the event.
   **
   ** @throws IllegalStateException if this state is not a valid text state.
   */
  @Override
  public int getTextLength() {
    return text().length();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getText (XMLStreamReader)
  /**
   ** Returns the current value of the parse event as a string, this returns the
   ** string value of a {@link #CHARACTERS} event, returns the value of a
   ** {@link #COMMENT}, the replacement value for an {@link #ENTITY_REFERENCE},
   ** the string value of a {@link #CDATA} section, the string value for a
   ** {@link #SPACE} event, or the String value of the internal subset of the
   ** {@link #DTD}.
   ** <p>
   ** If an {@link #ENTITY_REFERENCE} has been resolved, any character data will
   ** be reported as {@link #CHARACTERS} events.
   **
   ** @return                    the current text or <code>null</code>.
   **
   ** @throws IllegalStateException if this state is not a valid text state.
   */
  @Override
  public String getText() {
    switch (this.event) {
      case DTD              : return ((DocumentType)this.cursor).getInternalSubset();
      // DOM only gives access to the parsed replacement value, but StAX returns
      // the unparsed replacement value
      case ENTITY_REFERENCE : return null;
      default               : return text();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTextCharacters (XMLStreamReader)
  /**
   ** Returns an array which contains the characters from this event. This
   ** array should be treated as read-only and transient. I.e. the array will
   ** contain the text characters until the XMLStreamReader moves on to the next
   ** event. Attempts to hold into the character array beyond that time or
   ** modify the contents of the array are breaches of the contract for this
   ** interface.
   **
   ** @return                    the current text or an empty array.
   **
   ** @throws IllegalStateException if this state is not a valid text state.
   */
  @Override
  public char[] getTextCharacters() {
    return text().toCharArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTextCharacters (XMLStreamReader)
  /**
   ** Returns the the text associated with a {@link #CHARACTERS}, {@link #SPACE}
   ** or {@link #CDATA} event. Text starting a "sourceStart" is copied into
   ** "target" starting at "targetStart". Up to "length" characters are copied.
   ** The number of characters actually copied is returned.
   ** <p>
   ** The "sourceStart" argument must be greater or equal to <code>0</code> and
   ** less than or equal to the number of characters associated with the event.
   ** Usually, one requests text starting at a "sourceStart" of <code>0</code>.
   ** If the number of characters actually copied is less than the "length",
   ** then there is no more text. Otherwise, subsequent calls need to be made
   ** until all text has been retrieved. For example:
   ** <pre>
   **   int length = 1024;
   **   char[] myBuffer = new char[length];
   **
   **   for (int sourceStart = 0 ; ; sourceStart += length) {
   **     int chunk = stream.getTextCharacters(sourceStart, myBuffer, 0, length);
   **     if (chunk < length)
   **       break;
   **   }
   ** </pre>
   ** XMLStreamException may be thrown if there are any XML errors in the
   ** underlying source. The <code>targetStart</code> argument must be greater
   ** than or equal to <code>0</code> and less than the length of
   ** <code>target</code>, <code>length</code> must be greater than
   ** <code>0</code> and <code>targetStart + length</code> must be less than or
   ** equal to length of <code>target</code>.
   **
   ** @param  sourceStart        the index of the first character in the source
   **                            array to copy.
   ** @param  target             the destination array.
   ** @param  targetStart        the start offset in the target array.
   ** @param  length             the number of characters to copy.
   **
   ** @return                     the number of characters actually copied.
   **
   ** @throws XMLStreamException        if the underlying XML source is not
   **                                   well-formed.
   ** @throws IndexOutOfBoundsException if <code>targetStart &lt; 0</code> or
   **                                   &gt; than the length of
   **                                   <code>target</code> or if
   **                                   <code>length &lt; 0</code> or
   **                                   <code>targetStart + length</code> >
   **                                   length of target.
   ** @throws UnsupportedOperationException if this method is not supported
   ** @throws NullPointerException is   if <code>target</code> is
   **                                   <code>null</code>.
   */
  @Override
   public int getTextCharacters(final int sourceStart, final char[] target, final int targetStart, final int length)
     throws XMLStreamException {

    final String text = text();
    final int    size = Math.min(length, text.length() - sourceStart);
    text.getChars(sourceStart, sourceStart + size, target, targetStart);
    return size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getElementText (XMLStreamReader)
  /**
   ** Reads the content of a text-only element, an exception is thrown if this
   ** is not a text-only element. Regardless of value of
   ** javax.xml.stream.isCoalescing this method always returns coalesced
   ** content.
   ** <br>
   ** Precondition: the current event is {@link #START_ELEMENT}.
   ** <br>
   ** Postcondition: the current event is the corresponding {@link #END_ELEMENT}.
   ** <br>
   ** The method does the following (implementations are free to optimized but
   ** must do equivalent processing):
   ** <pre>
   **   if (getEventType() != XMLStreamConstants.START_ELEMENT) {
   **     throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
   **   }
   **   int eventType = next();
   **   StringBuilder content = new StringBuilder();
   **   while(eventType != XMLStreamConstants.END_ELEMENT ) {
   **     if(eventType == XMLStreamConstants.CHARACTERS || eventType == XMLStreamConstants.CDATA || eventType == XMLStreamConstants.SPACE || eventType == XMLStreamConstants.ENTITY_REFERENCE) {
   **       buf.append(getText());
   **     }
   **     else if (eventType == XMLStreamConstants.PROCESSING_INSTRUCTION || eventType == XMLStreamConstants.COMMENT) {
   **       // skipping
   **     }
   **     else if (eventType == XMLStreamConstants.END_DOCUMENT) {
   **       throw new XMLStreamException("unexpected end of document when reading element text content", this);
   **     }
   **     else if(eventType == XMLStreamConstants.START_ELEMENT) {
   **       throw new XMLStreamException("element text content may not contain START_ELEMENT", getLocation());
   **     }
   **     else {
   **       throw new XMLStreamException("Unexpected event type "+eventType, getLocation());
   **     }
   **     eventType = next();
   **   }
   **   return buf.toString();
   ** </pre>
   **
   ** @throws XMLStreamException if the current event is not a
   **                            {@link #START_ELEMENT} or if a non text element
   **                            is encountered
   */
  @Override
  public String getElementText()
    throws XMLStreamException {

    ////////////////////////////////////////////////////
    //// shameless stolen from the API documentation ///
    if (getEventType() != START_ELEMENT) {
      final Location position  = getLocation();
      final String[] arguments = {String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
      throw new XMLStreamException(XMLStreamBundle.format(XMLError.PARSING_ELEMENT_START, arguments));
    }

    final StringBuffer content   = new StringBuffer();
    int                eventType = next();
    while (eventType != END_ELEMENT) {
      if (eventType == CHARACTERS || eventType == CDATA || eventType == SPACE || eventType == ENTITY_REFERENCE) {
        content.append(getText());
      }
      else if (eventType == PROCESSING_INSTRUCTION || eventType == COMMENT) {
        // skipping
      }
      else if (eventType == END_DOCUMENT) {
        throw new XMLStreamException(XMLStreamBundle.string(XMLError.PARSING_DOCUMENT_END));
      }
      else if (eventType == START_ELEMENT) {
        final Location position  = getLocation();
        final String[] arguments = {String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
        throw new XMLStreamException(XMLStreamBundle.format(XMLError.PARSING_ELEMENT_TEXT, arguments));
      }
      else {
        final Location position  = getLocation();
        final String[] arguments = {String.valueOf(eventType), String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
        throw new XMLStreamException(XMLStreamBundle.format(XMLError.PARSING_UNEXPECTED_TYPE, arguments));
      }
      eventType = next();
    }
    return content.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (XMLStreamReader)
  /**
   ** Returns a {@link QName} for the current {@link #START_ELEMENT} or
   ** {@link #END_ELEMENT} event.
   **
   ** @return                    the {@link QName} for the current
   **                            {@link #START_ELEMENT} or {@link #END_ELEMENT}
   **                            event.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #END_ELEMENT}
   */
  @Override
  public QName getName() {
    switch (this.event) {
      case START_ELEMENT :
      case END_ELEMENT   : return qualifiedName(this.cursor);
      default            : throw new IllegalStateException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLocalName (XMLStreamReader)
  /**
   ** Returns the (local) name of the current event.
   ** <p>
   ** For {@link #START_ELEMENT} or {@link #END_ELEMENT} returns the (local)
   ** name of the current element. For {@link #ENTITY_REFERENCE} it returns
   ** entity name. The current event must be {@link #START_ELEMENT} or
   ** {@link #END_ELEMENT}, or {@link #ENTITY_REFERENCE}.
   **
   ** @return                    the localName.
   **
   ** @throws IllegalStateException if this not a {@link #START_ELEMENT},
   **                               {@link #END_ELEMENT} or
   **                               {@link #ENTITY_REFERENCE}.
   */
  @Override
  public String getLocalName() {
    switch (this.event) {
      case START_ELEMENT    :
      case END_ELEMENT      : return this.cursor.getLocalName();
      case ENTITY_REFERENCE : return this.cursor.getNodeName();
      default               : throw new IllegalStateException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeCount (XMLStreamReader)
  /**
   ** Returns the count of attributes on this {@link #START_ELEMENT}, this
   ** method is only valid on a {@link #START_ELEMENT} or {@link #ATTRIBUTE}.
   ** This count excludes namespace definitions. Attribute indices are
   ** zero-based.
   **
   ** @return                    the number of attributes.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public int getAttributeCount() {
    if (this.event != START_ELEMENT)
      throw new IllegalStateException();

    fetchAttributes();
    return this.attributeCount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAttributeSpecified (XMLStreamReader)
  /**
   ** Returns a boolean which indicates if this attribute was created by
   ** default.
   **
   ** @param  index              the position of the attribute.
   **
   ** @return                     <code>true</code> if this is a default
   **                             attribute.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public boolean isAttributeSpecified(int index) {
    return attribute(index).getSpecified();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeName (XMLStreamReader)
  /**
   ** Returns the {@link QName} of the attribute at the provided index.
   **
   ** @param  index              the position of the attribute.
   **
   ** @return                    the {@link QName} of the attribute.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public QName getAttributeName(final int index) {
    return qualifiedName(attribute(index));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeNamespace (XMLStreamReader)
  /**
   ** Returns the namespace of the attribute at the provided index.
   **
   ** @param  index              the position of the attribute.
   **
   ** @return                    the namespace URI (can be <code>null</code>).
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public String getAttributeNamespace(int index) {
    return attribute(index).getNamespaceURI();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeLocalName (XMLStreamReader)
  /**
   ** Returns the localName of the attribute at the provided index.
   **
   ** @param  index              the position of the attribute.
   **
   ** @return                    the localName of the attribute.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public String getAttributeLocalName(int index) {
    return attribute(index).getLocalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributePrefix (XMLStreamReader)
  /**
   ** Returns the prefix of this attribute at the provided index.
   **
   ** @param  index              the position of the attribute.
   **
   ** @return                    the prefix of the attribute.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public String getAttributePrefix(int index) {
    return attribute(index).getPrefix();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeType (XMLStreamReader)
  /**
   ** Returns the XML type of the attribute at the provided index.
   **
   ** @param                     index the position of the attribute.
   **
   ** @return                     the XML type of the attribute.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public String getAttributeType(int index) {
    if (this.event != START_ELEMENT)
      throw new IllegalStateException();

    return "CDATA";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeValue (XMLStreamReader)
  /**
   ** Returns the value of the attribute at the index.
   **
   ** @param  index              the position of the attribute.
   **
   ** @return                    the attribute value.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public String getAttributeValue(int index) {
    return attribute(index).getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeValue (XMLStreamReader)
  /**
   ** Returns the normalized attribute value of the attribute with the namespace
   ** and localName.
   ** <p>
   ** If the namespaceURI is <code>null</code> the namespace is not checked for
   ** equality.
   **
   ** @param  namespaceURI       the namespace of the attribute.
   ** @param  localName          the local name of the attribute, cannot be
   **                            <code>null</code>.
   **
   ** @return                    the value of the attribute , returns
   **                            <code>null</code> if not found.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT} or
   **                               {@link #ATTRIBUTE}.
   */
  @Override
  public String getAttributeValue(final String namespaceURI, final String localName) {
    return ((Element)this.cursor).getAttributeNS(namespaceURI == null || namespaceURI.length() == 0 ? null : namespaceURI, localName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextTag (XMLStreamReader)
  /**
   ** Skips any white space (isWhiteSpace() returns <code>true</code>),
   ** {@link #COMMENT}, or {@link #PROCESSING_INSTRUCTION}, until a
   ** {@link #START_ELEMENT} or {@link #END_ELEMENT} is reached.
   ** <p>
   ** If other than white space characters, {@link #COMMENT},
   ** {@link #PROCESSING_INSTRUCTION}, {@link #START_ELEMENT},
   ** {@link #END_ELEMENT} are encountered, an exception is thrown. This method
   ** should be used when processing element-only content seperated by white
   ** space.
   ** <br>
   ** <b>Precondition</b>: none
   ** <br>
   ** <b>Postcondition</b>:
   ** <br>
   ** the current event is {@link #START_ELEMENT} or {@link #END_ELEMENT} and
   ** cursor may have moved over any whitespace event.
   ** <br>
   ** Essentially it does the following (implementations are free to optimized
   ** but must do equivalent processing):
   ** <pre>
   **   int eventType = next();
   **   while((eventType == XMLStreamConstants.CHARACTERS &amp;&amp; isWhiteSpace()) // skip whitespace
   **       ||(eventType == XMLStreamConstants.CDATA &amp;&amp; isWhiteSpace())      // skip whitespace
   **       || eventType == XMLStreamConstants.SPACE
   **       || eventType == XMLStreamConstants.PROCESSING_INSTRUCTION
   **       || eventType == XMLStreamConstants.COMMENT) {
   **     eventType = next();
   **   }
   **   if (eventType != XMLStreamConstants.START_ELEMENT &amp;&amp; eventType != XMLStreamConstants.END_ELEMENT) {
   **     throw new String XMLStreamException("expected start or end tag", getLocation());
   **   }
   **   return eventType;
   ** </pre>
   **
   ** @return                    the event type of the element read (
   **                            {@link #START_ELEMENT} or {@link #END_ELEMENT})
   **
   ** @throws XMLStreamException if the current event is not white space,
   **                            {@link #PROCESSING_INSTRUCTION},
   **                            {@link #START_ELEMENT} or {@link #END_ELEMENT}.
   */
  @Override
  public int nextTag()
    throws XMLStreamException {

    int eventType = next();
    while (
       (eventType == CHARACTERS && isWhiteSpace()) // skip whitespace
    || (eventType == CDATA && isWhiteSpace()) // skip whitespace
    ||  eventType == SPACE
    ||  eventType == PROCESSING_INSTRUCTION
    ||  eventType == COMMENT) {
      eventType = next();
    }
    if (eventType != START_ELEMENT && eventType != END_ELEMENT) {
      final Location position  = getLocation();
      final String[] arguments = {String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
      throw new XMLStreamException(XMLStreamBundle.format(XMLError.PARSING_ELEMENT_STARTEND, arguments));
    }

    return eventType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next (XMLStreamReader)
  /**
   ** Returns the next parsing event - a processor may return all contiguous
   ** character data in a single chunk, or it may split it into several chunks.
   ** <p>
   ** If the property javax.xml.stream.isCoalescing is set to <code>true</code>
   ** element content must be coalesced and only one {@link #CHARACTERS} event must be
   ** returned for contiguous element content or CDATA Sections.
   ** <p>
   ** By default entity references must be expanded and reported transparently
   ** to the application. An exception will be thrown if an entity reference
   ** cannot be expanded. If element content is empty (i.e. content is "") then
   ** no {@link #CHARACTERS} event will be reported.
   ** <p>
   ** <b>NOTE:</b>
   ** <br>
   ** empty element (such as &lt;tag/>) will be reported with two separate
   ** events: {@link #START_ELEMENT}, {@link #END_ELEMENT} - This preserves parsing equivalency of
   ** empty element to &lt;tag>&lt;/tag>.
   ** <p>
   ** This method will throw an IllegalStateException if it is called after
   ** hasNext() returns <code>false</code>.
   **
   ** @return                    the integer code corresponding to the current
   **                            parse event.
   **
   ** @throws XMLStreamException     if there is an error processing the
   **                                underlying XML source.
   */
  @Override
  public int next()
    throws XMLStreamException {

    if (this.event == END_DOCUMENT)
      throw new NoSuchElementException(XMLStreamBundle.format(XMLError.PARSING_FATAL, XMLStreamBundle.string(XMLError.PARSING_UNEXPECTED_END)));

    boolean forceTraverse = false;
    while (true) {
      // determine the DOM node corresponding to the next event and determine if
      // we have already traversed the descendants of that node
      boolean visited;
      if (this.cursor == null) {
        // we get here if the root node is an element (not a document) and we
        // have not yet traversed that element (in which case the current event
        // must be START_DOCUMENT)
        this.cursor = this.root;
        visited     = false;
      }
      else if (this.event == START_DOCUMENT || this.event == START_ELEMENT || forceTraverse) {
        final Node firstChild = this.cursor.getFirstChild();
        if (firstChild == null) {
          visited = true;
        }
        else {
          this.cursor = firstChild;
          visited     = false;
        }
        forceTraverse = false;
      }
      else if (this.cursor == this.root) {
        // we get here if the root node is an element and we have finished
        // traversal of that element
        this.cursor = null;
        visited     = true;
      }
      else {
        final Node nextSibling = this.cursor.getNextSibling();
        if (nextSibling == null) {
          this.cursor = this.cursor.getParentNode();
          visited     = true;
        }
        else {
          this.cursor = nextSibling;
          visited     = false;
        }
      }
      // determine the event type for the next node
      switch (this.cursor == null ? Node.DOCUMENT_NODE : this.cursor.getNodeType()) {
        case Node.DOCUMENT_NODE               : this.event = END_DOCUMENT;
                                                break;
        case Node.DOCUMENT_TYPE_NODE          : this.event = DTD;
                                                break;
        case Node.ELEMENT_NODE                : this.event = visited ? END_ELEMENT : START_ELEMENT;
                                                // namespace declarations can be
                                                // queried on an END_ELEMENT
                                                // event; always reset the
                                                // attributesLoaded flag
                                                this.attributesFetched = false;
                                                break;
        case Node.TEXT_NODE                   : this.event = ((Text)this.cursor).isElementContentWhitespace() ? SPACE : CHARACTERS;
                                                break;
        case Node.CDATA_SECTION_NODE          : this.event = CDATA;
                                                break;
        case Node.COMMENT_NODE                : this.event = COMMENT;
                                                break;
        case Node.PROCESSING_INSTRUCTION_NODE : this.event = PROCESSING_INSTRUCTION;
                                                break;
        case Node.ENTITY_REFERENCE_NODE       : if (this.expandEntity) {
                                                  if (!visited) {
                                                    forceTraverse = true;
                                                  }
                                                  continue;
                                                }
                                                else {
                                                  this.event = ENTITY_REFERENCE;
                                                }
                                                break;
        default                               : final Location position  = getLocation();
                                                final String[] arguments = {String.valueOf(this.cursor.getNodeType()), String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
                                                throw new IllegalStateException(XMLStreamBundle.format(XMLError.PARSING_FATAL, XMLStreamBundle.format(XMLError.PARSING_UNEXPECTED_NODE, arguments)));
      }
      return this.event;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   require (XMLStreamReader)
  /**
   ** Test if the current event is of the given type and if the namespace and
   ** name match the current namespace and name of the current event.If the
   ** namespaceURI is <code>null</code> it is not checked for equality, if the
   ** localName is <code>null</code> it is not checked for equality.
   **
   ** @param  type               the event type.
   ** @param  namespaceURI       the uri of the event, may be <code>null</code>
   ** @param  localName          the localName of the event, may be
   **                            <code>null</code>
   **
   ** @throws XMLStreamException if the required values are not matched.
   */
  @Override
  public void require(int type, String namespaceURI, String localName)
    throws XMLStreamException {

    int actualType = getEventType();

    if (type != actualType) {
      final Location position  = getLocation();
      final String[] arguments = {StreamInput.eventTypeString(type), StreamInput.eventTypeString(actualType), String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
      throw new XMLStreamException(XMLStreamBundle.format(XMLError.REQUIRED_EVENTTYPE_MISMATCH, arguments));
    }

    if (localName != null) {
      if (actualType != START_ELEMENT && actualType != END_ELEMENT && actualType != ENTITY_REFERENCE) {
        final Location position  = getLocation();
        final String[] arguments = {StreamInput.eventTypeString(actualType), String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
        throw new XMLStreamException(XMLStreamBundle.format(XMLError.REQUIRED_LOCALNAME_EXPECTED, arguments));
      }

      String actualLocalName = getLocalName();
      if (actualLocalName != localName && !actualLocalName.equals(localName)) {
        final Location position  = getLocation();
        final String[] arguments = {localName, actualLocalName, String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
        throw new XMLStreamException(XMLStreamBundle.format(XMLError.REQUIRED_LOCALNAME_MISMATCH, arguments));
      }
    }

    if (namespaceURI != null) {
      if (actualType != START_ELEMENT && actualType != END_ELEMENT) {
        final Location position  = getLocation();
        final String[] arguments = {StreamInput.eventTypeString(actualType), String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
        throw new XMLStreamException(XMLStreamBundle.format(XMLError.REQUIRED_NAMESPACE_EXPECTED, arguments));
      }

      String actualUri = getNamespaceURI();
      if (namespaceURI.length() == 0) {
        if (actualUri != null && actualUri.length() > 0) {
          final Location position  = getLocation();
          final String[] arguments = {actualUri, String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
          throw new XMLStreamException(XMLStreamBundle.format(XMLError.REQUIRED_NAMESPACE_UNEXPECTED, arguments));
        }
      }
      else {
        if (!namespaceURI.equals(actualUri)) {
          final Location position  = getLocation();
          final String[] arguments = {namespaceURI, actualUri, String.valueOf(position.getLineNumber()), String.valueOf(position.getColumnNumber())};
          throw new XMLStreamException(XMLStreamBundle.format(XMLError.REQUIRED_NAMESPACE_MISMATCH, arguments));
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the namespace at the index.
   **
   ** @param  index              the position of the namespace.
   **
   ** @return                    the namespace at position.
   */
  private Attr namespace(final int index) {
    switch (this.event) {
      case START_ELEMENT :
      case END_ELEMENT   : fetchAttributes();
                           return this.namespaces[index];
      default            : throw new IllegalStateException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attribute at the index.
   **
   ** @param  index              the position of the attribute.
   **
   ** @return                    the attribute at position.
   **
   ** @throws IllegalStateException if this is not a {@link #START_ELEMENT}.
   */
  private Attr attribute(final int index) {
    if (this.event != START_ELEMENT)
      throw new IllegalStateException();

    fetchAttributes();
    return this.attributes[index];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchAttributes
  /**
   **
   */
  private void fetchAttributes() {
    if (!this.attributesFetched) {
      this.attributeCount = 0;
      this.namespaceCount = 0;
      final NamedNodeMap attrs = this.cursor.getAttributes();
      for (int i = 0, l = attrs.getLength(); i < l; i++) {
        final Attr attr = (Attr)attrs.item(i);
        if (isNSDecl(attr)) {
          if (this.namespaceCount == this.namespaces.length) {
            this.namespaces = ensureCapacity(this.namespaces);
          }
          this.namespaces[this.namespaceCount++] = attr;
        }
        else {
          if (this.attributeCount == this.attributes.length) {
            this.attributes = ensureCapacity(this.attributes);
          }
          this.attributes[this.attributeCount++] = attr;
        }
      }
      this.attributesFetched = true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureCapacity
  /**
   ** Increases the capacity of tha <code>Attr[]</code> instance to ensure that
   ** it can hold at least the double number of elements as the existing
   ** number of elements.
   **
   ** @param  source             the source array to increase in size.
   **
   ** @return                    the newly created array with the increased
   **                            capacity.
   */
   private Attr[] ensureCapacity(final Attr[] source) {
     final Attr[] newArray = new Attr[source.length * 2];
     System.arraycopy(source, 0, newArray, 0, source.length);
     return newArray;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   text
  /**
   ** Returns the value of the current {@link Node} if this is one of
   ** <ul>
   **   <li>XMLStreamConstants#SPACE
   **   <li>XMLStreamConstants#CDATA
   **   <li>XMLStreamConstants#COMMENT
   **   <li>XMLStreamConstants#CHARACTERS
   ** </ul>
   **
   ** @return                    the value of the current {@link Node}.
   **
   ** @throws IllegalStateException if the contraint above is violated.
   */
  private String text() {
    switch (this.event) {
      case SPACE      :
      case CDATA      :
      case COMMENT    :
      case CHARACTERS : return this.cursor.getNodeValue();
      default         : throw new IllegalStateException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   qualifiedName
  /**
   ** Returns the {@link QName} of the specified {@link Node}.
   **
   ** @param  node               the {@link Node} to inspect.
   **
   ** @return                    the {@link QName} of the specified
   **                            {@link Node} <code>node</code>.
   */
  private static QName qualifiedName(final Node node) {
    final String prefix = node.getPrefix();
    return new QName(node.getNamespaceURI(), node.getLocalName(), prefix == null ? "" : prefix);
  }
}
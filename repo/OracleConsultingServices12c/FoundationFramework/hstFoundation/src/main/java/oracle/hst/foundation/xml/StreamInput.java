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

    File        :   StreamInput.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StreamInput.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.Reader;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import javax.xml.transform.Source;

import javax.xml.transform.dom.DOMSource;

import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// class StreamInput
// ~~~~~ ~~~~~~~~~~~
/**
 ** The {@link XMLStreamReader} interface allows forward, read-only access to
 ** XML. It is designed to be the lowest level and most efficient way to read
 ** XML data.
 ** <p>
 ** The {@link XMLStreamReader} is designed to iterate over XML using
 ** {@link #next()} and {@link #hasNext()}. The data can be accessed using
 ** methods such as {@link #getEventType()}, {@link #getNamespaceURI()},
 ** {@link #getLocalName()} and {@link #getText()}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class StreamInput extends    XMLInput
                         implements XMLStreamReader
                         ,          XMLStreamConstants {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Location  UNKNOWN = new Location();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final XMLStreamReader reader;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Location
  // ~~~~~ ~~~~~~~~
  /**
   ** Location {@link javax.xml.stream.Location} implementation.
   ** <p>
   ** It always returns <code>-1</code> for the location and <code>null</code>
   ** for the publicId and systemId. It may be used by
   ** {@link javax.xml.stream.XMLStreamReader} implementations that don't
   ** support the concept of location.
   */
  protected static class Location implements javax.xml.stream.Location {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getLineNumber (Location)
    /**
     ** Returns the line number where the current event ends, returns
     ** <code>-1</code> if none is available.
     **
     ** @return                  the current line number.
     **                          Always <code>-1</code>.
     */
    @Override
    public int getLineNumber() {
      return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getColumnNumber (Location)
    /**
     ** Returns the column number where the current event ends, returns
     ** <code>-1</code> if none is available.
     **
     ** @return                  the current line column.
     **                          Always <code>-1</code>.
     */
    @Override
    public int getColumnNumber() {
      return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getCharacterOffset (Location)
    /**
     ** Returns the byte or character offset into the input source this location
     ** is pointing to. If the input source is a file or a byte stream then this
     ** is the byte offset into that stream, but if the input source is a
     ** character media then the offset is the character offset.
     ** <br>
     ** Returns <code>-1</code> if there is no offset available.
     **
     ** @return                  the current offset.
     **                          Always <code>-1</code>.
     */
    @Override
    public int getCharacterOffset() {
      return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getPublicId (Location)
    /**
     ** Returns the public ID of the XML.
     **
     ** @return                  the public ID, always <code>null</code> due to
     **                          not available.
     */
    @Override
    public String getPublicId() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getSystemId (Location)
    /**
     ** Returns the system ID of the XML.
     **
     ** @return                  the system ID, always <code>null</code> due to
     **                          not available.
     */
    @Override
    public String getSystemId() {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>StreamInput</code> from a {@link Reader}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link XMLStreamReader} interface allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  reader             the {@link Reader} to read from.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  public StreamInput(final Loggable loggable, final Reader reader)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, streamReader(reader));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>StreamInput</code> from a {@link InputStream}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link XMLStreamReader} interface allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  stream             the {@link InputStream} to read from.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  public StreamInput(final Loggable loggable, final InputStream stream)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, streamReader(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>StreamInput</code> from a {@link DOMSource}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link XMLStreamReader} interface allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  source             the {@link Source} to read from.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  public StreamInput(final Loggable loggable, final Source source)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, streamReader(source));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>StreamInput</code> with a specified
   ** {@link XMLStreamReader}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link XMLStreamReader} interface allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  reader             the {@link XMLStreamReader} used to read.
   */
  protected StreamInput(final Loggable loggable, final XMLStreamReader reader) {
    // ensure inheritance
    super(loggable);

    // initialize instance
    this.reader = reader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  reader
  /**
   ** Returns the underlying streaming implementation.
   **
   ** @return                    the underlying streaming implementation.
   */
  protected final XMLStreamReader reader() {
    return this.reader;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  namespaceAware
  /**
   ** Determines if the XML processor is configured to support namespaces.
   **
   ** @return                    <code>true</code> if the XML processor is
   **                            configured to support namespaces; otherwise
   **                            <code>false</code>.
   */
  protected boolean namespaceAware() {
    final Object o = getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
    // StAX defaults to namespace aware, so let's use similar logics (although
    // all compliant implementations really should return a valid value)
    return ((o instanceof Boolean) && ((Boolean)o).booleanValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  coalescing
  /**
   ** Determines if the XML processor is configured to coalesce adjacent
   ** character data.
   **
   ** @return                    <code>true</code> if the XML processor is
   **                            configured to coalesce adjacent character data;
   **                            otherwise <code>false</code>.
   */
  protected boolean coalescing() {
    final Object o = getProperty(XMLInputFactory.IS_COALESCING);
    return ((o instanceof Boolean) && ((Boolean)o).booleanValue());
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

    this.reader.close();
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
    return this.reader.getProperty(name);
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
  public javax.xml.stream.Location getLocation() {
    return this.reader.getLocation();
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
    return this.reader.getVersion();
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
    return this.reader.getEncoding();
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
    return this.reader.getCharacterEncodingScheme();
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
    return this.reader.isStandalone();
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
    return this.reader.standaloneSet();
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
    return this.reader.getPITarget();
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
    return this.reader.getPIData();
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
    return this.reader.isStartElement();
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
    return this.reader.isEndElement();
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
    return this.reader.isCharacters();
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
    return this.reader.isWhiteSpace();
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
    return this.reader.getEventType();
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
    return this.reader.hasName();
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
    return this.reader.hasText();
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

    return this.reader.hasNext();
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
    return this.reader.getNamespaceContext();
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
    return this.reader.getNamespaceCount();
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
    return this.reader.getNamespacePrefix(index);
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
    return this.reader.getNamespaceURI();
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
    return this.reader.getNamespaceURI(index);
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
    return this.reader.getNamespaceURI(prefix);
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
    return this.reader.getPrefix();
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
    return this.reader.getTextStart();
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
    return this.reader.getTextLength();
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
    return this.reader.getText();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTextCharacters (XMLStreamReader)
  /**
   ** Returns an array which contains the characters from this event. This
   ** array should be treated as read-only and transient. I.e. the array will
   ** contain the text characters until the XMLStreamReader moves on to the next
   ** event. Attempts to hold on the character array beyond that time or
   ** modify the contents of the array are breaches of the contract for this
   ** interface.
   **
   ** @return                    the current text or an empty array.
   **
   ** @throws IllegalStateException if this state is not a valid text state.
   */
  @Override
  public char[] getTextCharacters() {
    return this.reader.getTextCharacters();
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
   **     if (chunk &lt; length)
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
   **                                   <code>targetStart + length</code>
   **                                   length of target.
   ** @throws UnsupportedOperationException if this method is not supported
   ** @throws NullPointerException is   if <code>target</code> is
   **                                   <code>null</code>.
   */
  @Override
   public int getTextCharacters(final int sourceStart, final char[] target, final int targetStart, final int length)
     throws XMLStreamException {

    return this.reader.getTextCharacters(sourceStart, target, targetStart, length);
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
   **       content.append(getText());
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
   **   return content.toString();
   ** </pre>
   **
   ** @throws XMLStreamException if the current event is not a
   **                            {@link #START_ELEMENT} or if a non text element
   **                            is encountered
   */
  @Override
  public String getElementText()
    throws XMLStreamException {

    return this.reader.getElementText();
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
    return this.reader.getName();
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
    return this.reader.getLocalName();
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
    return this.reader.getAttributeCount();
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
    return this.reader.isAttributeSpecified(index);
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
    return this.reader.getAttributeName(index);
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
    return this.reader.getAttributeNamespace(index);
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
    return this.reader.getAttributeLocalName(index);
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
    return this.reader.getAttributePrefix(index);
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
    return this.reader.getAttributeType(index);
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
    return this.reader.getAttributeValue(index);
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
    return this.reader.getAttributeValue(namespaceURI, localName);
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

    return this.reader.nextTag();
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
   ** empty element (such as &lt;tag/&gt;) will be reported with two separate
   ** events: {@link #START_ELEMENT}, {@link #END_ELEMENT} - This preserves parsing equivalency of
   ** empty element to &lt;tag&gt;&lt;/tag&gt;.
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

    return this.reader.next();
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

    this.reader.require(type, namespaceURI, localName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   position (XMLInput)
  /**
   ** Returns the location (line number and colum) where the current event ends.
   **
   ** @return                    the location (line number and colum) where the
   **                            current event ends.
   */
  @Override
  public final int[] position() {
    final javax.xml.stream.Location location = this.reader.getLocation();
    final int[] position = { location.getLineNumber(), location.getColumnNumber() };
    return position;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventTypeString
  /**
   ** Get the string representation of a given StAX event type.
   ** <p>
   ** The returned value is the name of the constant in
   ** {@link XMLStreamConstants} corresponding to the event type.
   **
   ** @param  event              the event type as returned by
   **                            {@link javax.xml.stream.events.XMLEvent#getEventType()},
   **                            {@link javax.xml.stream.XMLStreamReader#getEventType()} or
   **                            {@link javax.xml.stream.XMLStreamReader#next()}.
   **
   ** @return                    a string representation of the event type.
   */
  public static String eventTypeString(final int event) {
    String state = null;
    switch (event) {
      case START_ELEMENT          : state = "START_ELEMENT";
                                    break;
      case START_DOCUMENT         : state = "START_DOCUMENT";
                                    break;
      case CHARACTERS             : state = "CHARACTERS";
                                    break;
      case CDATA                  : state = "CDATA";
                                    break;
      case END_ELEMENT            : state = "END_ELEMENT";
                                    break;
      case END_DOCUMENT           : state = "END_DOCUMENT";
                                    break;
      case SPACE                  : state = "SPACE";
                                    break;
      case COMMENT                : state = "COMMENT";
                                    break;
      case DTD                    : state = "DTD";
                                    break;
      case PROCESSING_INSTRUCTION : state = "PROCESSING_INSTRUCTION";
                                    break;
      case ENTITY_REFERENCE       : state = "ENTITY_REFERENCE";
                                    break;
      default                     : state = "UNKNOWN_STATE: " + event;
    }
    return state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eventTypeCode
  /**
   ** Get the string representation of a given StAX event type.
   ** <p>
   ** The returned value is the resource key of the constant in
   ** {@link XMLMessage} corresponding to the event type.
   **
   ** @param  event              the event type as returned by
   **                            {@link javax.xml.stream.events.XMLEvent#getEventType()},
   **                            {@link javax.xml.stream.XMLStreamReader#getEventType()} or
   **                            {@link javax.xml.stream.XMLStreamReader#next()}.
   **
   ** @return                    a string representation of the event type.
   */
  public static final String eventTypeCode(final int event) {
    switch (event) {
      case START_DOCUMENT : return XMLMessage.EVENT_DOCUMENT_START;
      case END_DOCUMENT   : return XMLMessage.EVENT_DOCUMENT_END;
      case START_ELEMENT  : return XMLMessage.EVENT_ELEMENT_START;
      case END_ELEMENT    : return XMLMessage.EVENT_ELEMENT_END;
      case CHARACTERS     : return XMLMessage.EVENT_TEXT;
      default             : return XMLMessage.EVENT_UNKNOWN;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamReader
  /**
   ** Create a new StreamInput from a {@link Reader}.
   **
   ** @param  reader             the {@link Reader} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  protected static final XMLStreamReader streamReader(final Reader reader)
    throws XMLStreamException {

    return streamReader(createInputFactory(true), reader);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamReader
  /**
   ** Create a new {@link XMLStreamReader} that meets our requirements.
   **
   ** @param  factory            the {@link XMLInputFactory} configured to
   **                            create an appropriate {@link XMLStreamReader}.
   ** @param  reader             the {@link Reader} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  protected static XMLStreamReader streamReader(final XMLInputFactory factory, final Reader reader)
    throws XMLStreamException {

    return factory.createXMLStreamReader(reader);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamReader
  /**
   ** Create a new {@link XMLStreamReader} that meets our requirements.
   **
   ** @param  source             the {@link Source} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  protected static final XMLStreamReader streamReader(final Source source)
    throws XMLStreamException {

    return streamReader(createInputFactory(true), source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamReader
  /**
   ** Create a new {@link XMLStreamReader} that meets our requirements.
   **
   ** @param  factory            the {@link XMLInputFactory} configured to
   **                            create an appropriate {@link XMLStreamReader}.
   ** @param  source             the {@link Source} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  protected static XMLStreamReader streamReader(final XMLInputFactory factory, final Source source)
    throws XMLStreamException {

    return factory.createXMLStreamReader(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Create a new StreamInput from a {@link InputStream}.
   **
   ** @param  stream             the {@link InputStream} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  protected static final XMLStreamReader streamReader(final InputStream stream)
    throws XMLStreamException {

    return streamReader(createInputFactory(true), stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamReader
  /**
   ** Create a new {@link XMLStreamReader} that meets our requirements.
   **
   ** @param  factory            the {@link XMLInputFactory} configured to
   **                            create an appropriate {@link XMLStreamReader}.
   ** @param  stream             the {@link InputStream} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLInputFactory} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  protected static XMLStreamReader streamReader(final XMLInputFactory factory, final InputStream stream)
    throws XMLStreamException {

    return factory.createXMLStreamReader(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createInputFactory
  /**
   ** Factory method to create a {@link XMLInputFactory} instance which is then
   ** used to obtain an {@link XMLStreamReader} instance.
   ** <p>
   ** This method uses the following ordered lookup procedure to determine the
   ** {@link XMLInputFactory} implementation class to load:
   ** <ol>
   **   <li>Use the <code>javax.xml.stream.XMLInputFactory</code> system
   **       property.
   **   <li>Use the properties file "<code>lib/stax.properties</code>" in the
   **       JRE directory.
   **       <br>
   **       This configuration file is in standard
   **       <code>java.util.Properties</code> format and contains the fully
   **       qualified name of the implementation class with the key being the
   **       system property defined above.
   **   <li>Use the Services API (as detailed in the JAR specification), if
   **       available, to determine the classname. The Services API will look
   **       for a classname in the file
   **       <code>META-INF/services/javax.xml.stream.XMLInputFactory</code> in
   **       jars available to the runtime.
   **   <li>Platform default XMLInputFactory instance.
   ** </ol>
   ** Once an application has obtained a reference to a {@link XMLInputFactory}
   ** it can use the factory to configure and obtain stream instances.
   **
   ** @param  namespaces         turn on/off namespace support, this is to
   **                            support XML 1.0 documents, only the true
   **                            setting must besupported.
   **
   ** @return                    the {@link XMLInputFactory} configured for our
   **                            purpose.
   */
  protected static XMLInputFactory createInputFactory(final boolean namespaces) {
    final XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, namespaces ? Boolean.TRUE : Boolean.FALSE);
    return factory;
  }
}
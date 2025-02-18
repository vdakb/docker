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

    File        :   DOMConverter.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DOMConverter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.Reader;
import java.io.InputStream;

import javax.xml.XMLConstants;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Source;

import javax.xml.stream.XMLStreamException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class DOMConverter
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A Utility class to read an XML document from a file into the XMLNode tree
 ** used by the XMLDiff class.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class DOMConverter extends StreamInput {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final   DocumentBuilder builder;

  /**
   ** Whether ignorable white space should be ignored, ie not added in the DOM
   ** tree. If <code>true</code>, it will be ignored; if </code>false</code>, it
   ** will be added in the tree. Default value if </code>false</code>.
   */
  private boolean ignoreWhiteSpace = false;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class NameRecycler
  // ~~~~~ ~~~~~~~~~~~~
  static final class NameRecycler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    String prefix    = null;
    String localName = null;
    String name      = null;

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    public String qualify(final String prefix, final String localName) {
      if ((localName == this.localName) && (prefix == this.prefix))
        return this.name;

      this.localName = localName;
      this.prefix    = prefix;
      StringBuilder sb = new StringBuilder(1 + prefix.length() + localName.length());
      sb.append(prefix).append(':').append(localName);
      this.name = sb.toString();
      return this.name;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMConverter</code> from a {@link Reader}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link StreamInput} class allows forward, read-only access to XML. It
   ** is designed to be the lowest level and most efficient way to read XML
   ** data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  reader             the {@link Reader} to read from.
   **
   ** @throws XMLStreamException in case <code>XMLInputFactory</code> is not
   **                            able to create an appropriate
   **                            <code>XMLStreamReader</code>.
   */
  public DOMConverter(final Loggable loggable, final Reader reader)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, reader, constructBuilder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMConverter</code> from a {@link Reader}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link StreamInput} class allows forward, read-only access to XML. It
   ** is designed to be the lowest level and most efficient way to read XML
   ** data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  reader             the {@link Reader} to read from.
   ** @param  builder            the {@link DocumentBuilder} to build the DOM
   **                            tree.
   **
   ** @throws XMLStreamException in case <code>XMLInputFactory</code> is not
   **                            able to create an appropriate
   **                            <code>XMLStreamReader</code>.
   */
  public DOMConverter(final Loggable loggable, final Reader reader, final DocumentBuilder builder)
    throws XMLStreamException {

    // ensure inheritance
    super(loggable, reader);

    // initialize instance
    this.builder = builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMConverter</code> from a {@link InputStream}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link StreamInput} class allows forward, read-only access to XML. It
   ** is designed to be the lowest level and most efficient way to read XML
   ** data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  stream             the {@link InputStream} to read from.
   **
   ** @throws XMLStreamException in case <code>XMLInputFactory</code> is not
   **                            able to create an appropriate
   **                            <code>XMLStreamReader</code>.
   */
  public DOMConverter(final Loggable loggable, final InputStream stream)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, stream, constructBuilder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMConverter</code> from a {@link InputStream}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link StreamInput} class allows forward, read-only access to XML. It
   ** is designed to be the lowest level and most efficient way to read XML
   ** data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  stream             the {@link InputStream} to read from.
   ** @param  builder            the {@link DocumentBuilder} to build the DOM
   **                            tree.
   **
   ** @throws XMLStreamException in case <code>XMLInputFactory</code> is not
   **                            able to create an appropriate
   **                            <code>XMLStreamReader</code>.
   */
  public DOMConverter(final Loggable loggable, final InputStream stream, final DocumentBuilder builder)
    throws XMLStreamException {

    // ensure inheritance
    super(loggable, stream);

    // initialize instance
    this.builder = builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMConverter</code> from a {@link Source}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link StreamInput} class allows forward, read-only access to XML. It
   ** is designed to be the lowest level and most efficient way to read XML
   ** data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  source             the {@link Source} to read from.
   **
   ** @throws XMLStreamException in case <code>XMLInputFactory</code> is not
   **                            able to create an appropriate
   **                            <code>XMLStreamReader</code>.
   */
  public DOMConverter(final Loggable loggable, final Source source)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, source, constructBuilder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMConverter</code> from a {@link Source}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link StreamInput} class allows forward, read-only access to XML. It
   ** is designed to be the lowest level and most efficient way to read XML
   ** data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  source             the {@link Source} to read from.
   ** @param  builder            the {@link DocumentBuilder} used to create a
   **                            instance of a DOM Document object.
   **
   ** @throws XMLStreamException in case <code>XMLInputFactory</code> is not
   **                            able to create an appropriate
   **                            <code>XMLStreamReader</code>.
   */
  public DOMConverter(final Loggable loggable, final Source source, final DocumentBuilder builder)
    throws XMLStreamException {

    // ensure inheritance
    super(loggable, source);

    // initialize instance
    this.builder = builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ignoreWhiteSpace
  /**
   ** Whether the build methods will add ignorable (element) white space in the
   ** DOM tree or not.
   ** <p>
   ** Whether all-whitespace text segment is ignorable white space or not is
   ** based on DTD read in, as per XML specifications (white space is only
   ** significant in mixed content or pure text elements).
   **
   ** @param  state              <code>true</code> all-whitespace text segment
   **                            is ignorable white space; otherwise
   **                            <code>false</code>.
   */
  public void ignoreWhiteSpace(final boolean state) {
    this.ignoreWhiteSpace = state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDocument (StreamInput)
  /**
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void processDocument()
    throws XMLException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  constructBuilder
  /**
   ** Factory method to a new instance of a {DocumentBuilder} using the
   ** currently configured parameters.
   **
   ** @return                    a new instance of a {DocumentBuilder}.
   **
   ** @throws ParserConfigurationException if a {DocumentBuilder} cannot be
   **                                      created which satisfies the
   **                                      configuration requested.
   */
  private static final DocumentBuilder constructBuilder() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  buildDocument
  /**
   ** This method will create a {@link Document} instance using the default JAXP
   ** DOM document construction mechanism and populated using the given StAX
   ** stream reader. Namespace-awareness will be enabled for the
   ** {@link DocumentBuilderFactory} constructed; if this is not wanted, caller
   ** should construct DocumentBuilder separately.
   ** <p>
   ** <b>Note</b>: underlying stream reader will not be closed by calling this
   ** method.
   **
   ** @return                    {@link Document} - DOM document object.
   **
   ** @throws XMLException       if the reader threw such exception (to indicate
   **                            a parsing or I/O problem)
   */
  public Document buildDocument()
    throws XMLException {

    return buildDocument(this.builder);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  buildDocument
  /**
   ** This method will create a {@link Document} instance using given
   ** {@link DocumentBuilderFactory} and populated using the given StAX stream
   ** reader.
   ** <p>
   ** <b>Note</b>: underlying stream reader will not be closed by calling this
   ** method.
   ** 
   ** @param  builder            the {@link DocumentBuilder} used to create a
   **                            instance of a DOM Document object.
   **
   ** @return                    {@link Document} - DOM document object.
   **
   ** @throws XMLException       if the reader threw such exception (to indicate
   **                            a parsing or I/O problem)
   */
  public Document buildDocument(final DocumentBuilder builder)
    throws XMLException {

    final Document doc = builder.newDocument();
    buildDocument(doc);
    return doc;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  buildDocument
  /**
   ** This method will populate given {@link Document} using the given StAX
   ** stream reader instance.
   ** <p>
   ** <b>Implementation note</b>: recursion has been eliminated by using nodes'
   ** parent/child relationship; this improves performance somewhat (classic
   ** recursion-by-iteration-and-explicit stack transformation)
   ** <p>
   ** <b>Note</b>: underlying stream reader will not be closed by calling this
   ** method.
   **
   ** @param  doc                the {@link Document} being build.
   **
   ** @throws XMLException       if the reader threw such exception (to indicate
   **                            a parsing or I/O problem)
   */
  public void buildDocument(final Document doc)
    throws XMLException {

    // One important distinction; to build "whole" document (including PIs and
    // comments outside of root node), we must start with START_DOCUMENT event.
    final boolean      wholeDocument     = getEventType() == START_DOCUMENT;
    final boolean      namespaceAware    = namespaceAware();
    final NameRecycler namespaceRecycler = new NameRecycler();
    try {
      Node cursor = doc;

      main_loop:
      for (int eventType = getEventType(); true; eventType = next()) {
        Node child;
        switch (eventType) {
          // check for an event is a CDATA section
          case CDATA                  : child = doc.createCDATASection(getText());
                                        break;
          case SPACE                  : if (this.ignoreWhiteSpace || cursor == doc) {
                                          continue main_loop;
                                        }
                                        // intentionally fall through
          case CHARACTERS             : child = doc.createTextNode(getText());
                                        break;
          case COMMENT                : child = doc.createComment(getText());
                                        break;
          case END_DOCUMENT           : break main_loop;
          case END_ELEMENT            : cursor = cursor.getParentNode();
                                        if ((cursor == null) && (cursor == doc)) {
                                          // if the root element closed, we now need to bail
                                          // out UNLESS we are building "whole document"
                                          // (in which case still need to get possible PIs,
                                          // comments)
                                          if (!wholeDocument)
                                            break main_loop;
                                        }
                                        continue main_loop;
          case ENTITY_DECLARATION     :
          case NOTATION_DECLARATION   : // shouldn't really get these, but maybe some stream
                                        // readers do provide the info. If so, better ignore
                                        // it -- DTD event should have most/all we need.
                                        continue main_loop;
          case ENTITY_REFERENCE       : child = doc.createEntityReference(getLocalName());
                                        break;
          case PROCESSING_INSTRUCTION : child = doc.createProcessingInstruction(getPITarget(), getPIData());
                                        break;
          case START_ELEMENT          : // need to add a new element...
                                        String ln = getLocalName();
                                        Element element;
                                        if (namespaceAware) {
                                          String qname = getPrefix();
                                          element = StringUtility.isEmpty(qname)? doc.createElement(ln) : doc.createElementNS(getNamespaceURI(), qname);
                                        }
                                        // if non-ns-aware, things are simpler
                                        else {
                                          element = doc.createElement(ln);
                                        }
                                        // silly old DOM: must mix in namespace declarations
                                        // in there...
                                        for (int i = 0, len = getNamespaceCount(); i < len; i++) {
                                          String prefix = getNamespacePrefix(i);
                                          String qname  = StringUtility.isEmpty(prefix) ? XMLConstants.XMLNS_ATTRIBUTE : namespaceRecycler.qualify(XMLConstants.XMLNS_ATTRIBUTE, prefix);
                                          element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, qname, getNamespaceURI(i));
                                        }
                                        // ... and then the attributes:
                                        for (int i = 0, len = getAttributeCount(); i < len; i++) {
                                          ln = getAttributeLocalName(i);
                                          if (namespaceAware) {
                                            String prefix = getAttributePrefix(i);
                                            if ((prefix != null) && (prefix.length() > 0)) {
                                              ln = namespaceRecycler.qualify(prefix, ln);
                                            }
                                            element.setAttributeNS(getAttributeNamespace(i), ln, getAttributeValue(i));
                                          }
                                          else {
                                            element.setAttribute(ln, getAttributeValue(i));
                                          }
                                        }
                                        // ... and then 'push' new element...
                                        cursor.appendChild(element);
                                        cursor = element;
                                        continue main_loop;
          case DTD                    : // !!! Note: StAX does not expose enough information about
                                        // doctype declaration (specifically, public and system id!);
                                        // (altough StAX2 would...)
                                        // Worse, DOM1/2 do not specify a way to create the DocType
                                        // node, even if StAX provided it. This is pretty silly,
                                        // all in all.
                                        continue main_loop;
          case START_DOCUMENT         : // this should only be received at the beginning of document ...
                                        // so, should we indicate the problem or not?
                                        // For now, let it pass: maybe some (broken) readers pass
                                        // that info as first event in beginning of doc?
                                        continue main_loop;
         // should never get these, from a stream reader (commented out entries are just FYI; default catches
         // them all)
          case ATTRIBUTE              :
          case NAMESPACE              :
          default                     : throw new XMLException("Unrecognized iterator event type: " + getEventType() + "; should not receive such types (broken stream reader?)");
        }

        if (child != null)
          cursor.appendChild(child);
      }
    }
    catch (XMLStreamException e) {
      // intentionally left blank
      ;
    }
  }
}

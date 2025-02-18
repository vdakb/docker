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

    File        :   DOMInput.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DOMInput.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

import org.xml.sax.ext.LexicalHandler;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.resource.XMLStreamBundle;

////////////////////////////////////////////////////////////////////////////////
// class DOMInput
// ~~~~~ ~~~~~~~~
/**
 ** Default base class for handling of XML documents using DOM.
 ** <p>
 ** Helper class that produces a SAX stream from a DOM Document.
 */
public abstract class DOMInput extends SAXInput {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final LexicalHandler  lexer;
  private final ContentHandler  handler;
  private final DocumentBuilder builder;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMInput</code> that will use a third-party DOM
   ** parser (chosen by JAXP) to handle the parsing duties to construct a
   ** document.
   **
   ** @throws SAXException       in case {@link DocumentBuilderFactory} is not
   **                            able to create an appropriate
   **                            {@link DocumentBuilder}.
   */
  public DOMInput()
    throws SAXException {

    // ensure inheritance
    this(createParser());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMInput</code> that will use a  manually choosen
   ** COM parser to handle the parsing duties to construct a document.
   **
   ** @param  builder            the {@link DocumentBuilder} used to handle the
   **                            parsing duties to construct a document.
   **
   ** @throws SAXException       in case {@link DocumentBuilderFactory} is not
   **                            able to create an appropriate
   **                            {@link DocumentBuilder}.
   */
  public DOMInput(final DocumentBuilder builder)
    throws SAXException {

    // ensure inheritance
    this(builder, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DOMInput</code> that will use a third-party DOM
   ** parser (chosen by JAXP) to handle the parsing duties to construct a
   ** document.
   **
   ** @param  handler            the {@link ContentHandler} simply listens to
   **                            the SAX events generated after the entire
   **                            document was parsed.
   **
   ** @throws SAXException       in case {@link DocumentBuilderFactory} is not
   **                            able to create an appropriate
   **                            {@link DocumentBuilder}.
   */
  public DOMInput(final ContentHandler handler)
    throws SAXException {

    // ensure inheritance
    this(createParser(), handler);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>SAXInput</code> that will use a  manually choosen
   ** DOM parser to handle the parsing duties and a SAX handler listen to the
   ** SAX events.
   **
   ** @param  builder            the {@link DocumentBuilder} used to handle the
   **                            parsing duties to construct a document.
   ** @param  handler            the {@link ContentHandler} simply listens to
   **                            the SAX events generated after the entire
   **                            document was parsed.
   **
   ** @throws SAXException       in case {@link DocumentBuilderFactory} is not
   **                            able to create an appropriate
   **                            {@link DocumentBuilder}.
   */
  public DOMInput(final DocumentBuilder builder, final ContentHandler handler)
    throws SAXException {

    // ensure inheritance
    super();

    // initialize instance
    this.builder = builder;
    this.handler = (handler == null) ? this : handler;
    this.lexer   = (this.handler instanceof LexicalHandler) ? (LexicalHandler)this.handler : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDocument (overridden)
  /**
   ** @param  stream             the {@link InputStream} containing the content
   **                            to be parsed.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  @Override
  public void processDocument(final InputStream stream)
    throws SAXException {

    processDocument(new InputSource(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDocument (overridden)
  /**
   ** @param  source             the {@link InputSource} containing the content
   **                            to be parsed.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  @Override
  public void processDocument(final InputSource source)
    throws SAXException {

    try {
      processDocument(this.builder.parse(source));
    }
    catch (IOException e) {
      throw new SAXException(XMLStreamBundle.format(SystemError.UNHANDLED, e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDocument (overridden)
  /**
   ** @param  document           the {@link Document} containing the content
   **                            to be parsed.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void processDocument(final Document document)
    throws SAXException {

    try {
      @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
      com.sun.org.apache.xalan.internal.xsltc.trax.DOM2SAX dom2sax = new com.sun.org.apache.xalan.internal.xsltc.trax.DOM2SAX(document);
      dom2sax.setContentHandler(this.handler);
      dom2sax.parse();
    }
    catch (IOException e) {
      throw new SAXException(XMLStreamBundle.format(SystemError.UNHANDLED, e.getLocalizedMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createParser
  /**
   ** Create a new {@link DocumentBuilder} that meets our requirements.
   **
   ** @return                    the {@link DocumentBuilder} configured for our
   **                            purpose.
   **
   ** @throws SAXException       in case {@link DocumentBuilderFactory} is not
   **                            able to create an appropriate
   **                            {@link DocumentBuilder}.
   */
  private static DocumentBuilder createParser()
    throws SAXException {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    // set namespaceAware to true to get a DOM Level 2 tree with nodes
    // containing namespace information. This is necessary because the default
    // value from JAXP 1.0 was defined to be false.
    factory.setNamespaceAware(true);

    try {
      factory.setFeature("http://xml.org/sax/features/namespaces", true);
      // create a DocumentBuilder that satisfies the constraints specified by
      // the DocumentBuilderFactory
      return factory.newDocumentBuilder();
    }
    catch (FactoryConfigurationError e) {
      // JAXP suffers from excessive brain-damage caused by intellectual
      // in-breeding at Sun. (Basically the Sun engineers spend way too much
      // time talking to each other and not nearly enough time talking to people
      // outside Sun.) Fortunately, you can happily ignore most of the JAXP
      // brain damage and not be any the poorer for it.
      // This, however, is one of the few problems you can't avoid if you're
      // going to use JAXP at all. DocumentBuilderFactory.newInstance() should
      // throw a ClassNotFoundException if it can't locate the factory class.
      // However, what it does throw is an Error, specifically a
      // FactoryConfigurationError. Very few programs are prepared to respond to
      // errors as opposed to exceptions. You should catch this error in your
      // JAXP programs as quickly as possible even though the compiler won't
      // require you to, and you should never rethrow it or otherwise let it
      // escape from the method that produced it.
      throw new SAXException(XMLStreamBundle.string(SystemError.CLASSNOTFOUND), e.getException());
    }
    catch (ParserConfigurationException e) {
      throw new SAXException(XMLStreamBundle.format(SystemError.UNHANDLED, e.getLocalizedMessage()), e);
    }
  }
}
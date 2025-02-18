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

    File        :   SAXInput.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SAXInput.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

import java.util.Stack;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Locator;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;

import org.xml.sax.helpers.DefaultHandler;

////////////////////////////////////////////////////////////////////////////////
// class SAXInput
// ~~~~~ ~~~~~~~~
/**
 ** Default base class for real-time handling of XML events.
 ** <p>
 ** This is the main implementation that most SAX applications has to implement:
 ** if the application needs to be informed of basic parsing events, it has
 ** to implement the interface {@link ContentHandler} and registers an instance
 ** with  the SAX parser using the
 ** {@link org.xml.sax.XMLReader#setContentHandler setContentHandler} method.
 ** The parser uses the instance to report basic document-related events like
 ** the start and end of elements and character data.
 ** <p>
 ** The order of events in the interface {@link ContentHandler} is very
 ** important, and mirrors the order of information in the document itself. For
 ** example, all of an element's content (character data, processing
 ** instructions, and/or subelements) will appear, in order, between the
 ** startElement event and the corresponding endElement event.
 ** <p>
 ** The class uses a third-party SAX parser (chosen by JAXP by default, or you
 ** can choose manually) to handle the parsing duties and simply listens to the
 ** SAX events to construct a document. Details which SAX does not provide, such
 ** as whitespace outside the root element, are not represented. Information
 ** about SAX can be found at
 ** <a href="http://www.saxproject.org">http://www.saxproject.org</a>.
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   12.2.1.3.42.60.74
 */
public abstract class SAXInput extends DefaultHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final SAXParser     parser;

  private   final Stack<Object> values   = new Stack<Object>();

  private   Locator             locator  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>SAXInput</code> that will use a third-party SAX
   ** parser (chosen by JAXP)  to handle the parsing duties and simply listens
   ** to the SAX events to construct a document.
   **
   ** @throws SAXException       in case {@link SAXParserFactory} is not able to
   **                            create an appropriate {@link SAXParser}.
   */
  public SAXInput()
    throws SAXException {

    // ensure inheritance
    this(createParser(true, true));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>SAXInput</code> that will use a  manually choosen
   ** SAX parser to handle the parsing duties and simply listens to the SAX
   ** events to construct a document.
   **
   ** @param  parser           the {@link SAXParser} used to handle the parsing
   **                          duties and simply listens to the SAX events to
   **                          construct a document.
   */
  public SAXInput(final SAXParser parser) {
    // ensure inheritance
    super();

    // initialize instance
    this.parser = parser;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parser
  /**
   ** Returns the {@link SAXParser}.
   **
   ** @return                  the {@link SAXParser}.
   */
  protected SAXParser parser() {
    return this.parser;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reader
  /**
   ** Returns the {@link XMLReader}.
   **
   ** @return                  the {@link XMLReader}.
   **
   ** @throws SAXException       in case {@link SAXParser} is not able to
   **                            create an appropriate {@link XMLReader}.
   */
  protected XMLReader reader()
    throws SAXException {

    return this.parser.getXMLReader();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   position
  /**
   ** Returns the location (line number and colum) where the current event
   ** ends.
   **
   ** @return                  the location (line number and colum) where the
   **                          current event ends.
   */
  protected final int[] position() {
    final int[] position = {-1, -1};
    if (this.locator != null) {
      position[0] = this.locator.getLineNumber();
      position[1] = this.locator.getColumnNumber();
    }
    return position;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createParser
  /**
   ** Create a new {@link SAXParser} that meets our requirements.
   ** <p>
   ** Namespace handling is controlled by these two properties:
   ** <ul>
   **   <li><code>http://xml.org/sax/features/namespaces</code>
   **   <li><code>http://xml.org/sax/features/namespace-prefixes</code>
   ** </ul>
   ** <code>http://xml.org/sax/features/namespaces</code> feature determines
   ** whether namespace URIs and local names are passed to
   ** <code>startElement()</code> and <code>endElement()</code>. The default,
   ** <code>true</code>, passes both namespace URIs and local names. However, if
   ** <code>http://xml.org/sax/features/namespaces</code> is <code>false</code>,
   ** then the parser may pass the namespace URI and the local name, or it may
   ** just pass empty strings for these two arguments.
   ** <p>
   ** <code>http://xml.org/sax/features/namespace-prefixes</code> feature
   ** determines two things:
   ** <ul>
   **   <li>Whether or not namespace declaration <code>xmlns</code> and
   **       <code>xmlns:prefix</code> attributes are included in the
   **       <code>Attributes</code> object passed to
   **       <code>startElement()</code>. The default, <code>false</code>, is not
   **       to include them.
   **   <li>Whether or not the qualified names should be passed as the third
   **       argument to the <code>startElement()</code> method. The default,
   **       <code>false</code>, is, not to require qualified names. However,
   **       even if <code>http://xml.org/sax/features/namespace-prefixes</code>
   **       is <code>false</code>, parsers are allowed to report the qualified
   **       name, and most do so.
   ** </ul>
   ** In other words:
   ** <ul>
   **   <li>The parser is only guaranteed to provide the namespace URIs and
   **       local names of elements and attributes if
   **       <code>http://xml.org/sax/features/namespaces</code> is
   **       <code>true</code> (which it is by default).
   **   <li>The parser is only guaranteed to provide the qualified names of
   **       elements and attributes if
   **       <code>http://xml.org/sax/features/namespace-prefixes</code> is
   **       <code>true</code> (which it is not by default).
   **   <li>The parser provides namespace declaration attributes if and only if
   **       <code>http://xml.org/sax/features/namespace-prefixes</code> is
   **       <code>true</code> (which it is not by default).
   **   <li>The parser always has the option to provide the namespace URI, local
   **       name, and qualified name, regardless of the values of
   **       <code>http://xml.org/sax/features/namespaces</code> and
   **       <code>http://xml.org/sax/features/namespace-prefixes</code>.
   **       However, you should not rely on this behavior.
   ** </ul>
   **
   ** @param  validation         whether to produce a validating SAX parser.
   ** @param  namespaces         whether to provide namespace support.
   **
   ** @return                    the {@link SAXParser} configured by the factory
   **                            for our purpose.
   **
   ** @throws SAXException       in case {@link SAXParserFactory} is not able to
   **                            create an appropriate {@link SAXParser}.
   */
  public static SAXParser createParser(final boolean validation, final boolean namespaces)
    throws SAXException {

    return createParser(createParserFactory(validation, namespaces));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createParser
  /**
   ** Create a new {@link SAXParser} that meets our requirements.
   **
   ** @param  factory            the {@link SAXParserFactory} configured to
   **                            create an appropriate {@link SAXParser}.
   **
   ** @return                    the {@link SAXParser} configured by the factory
   **                            for our purpose.
   **
   ** @throws SAXException       in case {@link SAXParserFactory} is not able to
   **                            create an appropriate {@link SAXParser}.
   */
  public static SAXParser createParser(final SAXParserFactory factory)
    throws SAXException {

    try {
      return factory.newSAXParser();
    }
    catch (ParserConfigurationException e) {
      throw new SAXException(e);
    }
    catch (SAXException e) {
      throw new SAXException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createParserFactory
  /**
   ** Factory method to create a {@link SAXParserFactory} instance which is then
   ** used to obtain an {@link SAXParser} instance.
   ** <p>
   ** Namespace handling is controlled by these two properties:
   ** <ul>
   **   <li><code>http://xml.org/sax/features/namespaces</code>
   **   <li><code>http://xml.org/sax/features/namespace-prefixes</code>
   ** </ul>
   ** <code>http://xml.org/sax/features/namespaces</code> feature determines
   ** whether namespace URIs and local names are passed to
   ** <code>startElement()</code> and <code>endElement()</code>. The default,
   ** <code>true</code>, passes both namespace URIs and local names. However, if
   ** <code>http://xml.org/sax/features/namespaces</code> is <code>false</code>,
   ** then the parser may pass the namespace URI and the local name, or it may
   ** just pass empty strings for these two arguments.
   ** <p>
   ** <code>http://xml.org/sax/features/namespace-prefixes</code> feature
   ** determines two things:
   ** <ul>
   **   <li>Whether or not namespace declaration <code>xmlns</code> and
   **       <code>xmlns:prefix</code> attributes are included in the
   **       <code>Attributes</code> object passed to
   **       <code>startElement()</code>. The default, <code>false</code>, is not
   **       to include them.
   **   <li>Whether or not the qualified names should be passed as the third
   **       argument to the <code>startElement()</code> method. The default,
   **       <code>false</code>, is, not to require qualified names. However,
   **       even if <code>http://xml.org/sax/features/namespace-prefixes</code>
   **       is <code>false</code>, parsers are allowed to report the qualified
   **       name, and most do so.
   ** </ul>
   ** In other words:
   ** <ul>
   **   <li>The parser is only guaranteed to provide the namespace URIs and
   **       local names of elements and attributes if
   **       <code>http://xml.org/sax/features/namespaces</code> is
   **       <code>true</code> (which it is by default).
   **   <li>The parser is only guaranteed to provide the qualified names of
   **       elements and attributes if
   **       <code>http://xml.org/sax/features/namespace-prefixes</code> is
   **       <code>true</code> (which it is not by default).
   **   <li>The parser provides namespace declaration attributes if and only if
   **       <code>http://xml.org/sax/features/namespace-prefixes</code> is
   **       <code>true</code> (which it is not by default).
   **   <li>The parser always has the option to provide the namespace URI, local
   **       name, and qualified name, regardless of the values of
   **       <code>http://xml.org/sax/features/namespaces</code> and
   **       <code>http://xml.org/sax/features/namespace-prefixes</code>.
   **       However, you should not rely on this behavior.
   ** </ul>
   **
   ** @param  validation         whether to produce a validating SAX parser.
   ** @param  namespaces         whether to provide namespace support.
   **
   ** @return                    the {@link SAXParser} configured for our
   **                            purpose.
   **
   ** @throws SAXException       in case an appropriate {@link SAXParserFactory}
   **                            cannot be created.
   */
  public static SAXParserFactory createParserFactory(final boolean validation, final boolean namespaces)
    throws SAXException {

    final SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(namespaces);
    try {
      factory.setFeature("http://xml.org/sax/features/namespaces",         namespaces);
      factory.setFeature("http://xml.org/sax/features/namespace-prefixes", namespaces);
      factory.setValidating(validation);
      // create a SAXParser that satisfies the constraints specified by the
      // SAXParserFactory
      return factory;
    }
    catch (ParserConfigurationException e) {
      throw new SAXException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDocumentLocator (overridden)
  /**
   ** Receive an object for locating the origin of SAX document events.
   ** <p>
   ** SAX parsers are strongly encouraged (though not absolutely required) to
   ** supply a locator: if it does so, it must supply the locator to the
   ** application by invoking this method before invoking any of the other
   ** methods in the ContentHandler interface.
   ** <p>
   ** The locator allows the application to determine the end position of any
   ** document-related event, even if the parser is not reporting an error.
   ** Typically, the application will use this information for reporting its own
   ** errors (such as character content that does not match an application's
   ** business rules). The information returned by the locator is probably not
   ** sufficient for use with a search engine.
   ** <p>
   ** Note that the locator will return correct information only during the
   ** invocation SAX event callbacks after {@link #startDocument startDocument}
   ** returns and before {@link #endDocument endDocument} is called. The
   ** application should not attempt to use it at any other time.
   **
   ** @param  locator            an object that can return the location of any
   **                            SAX document event.
   **
   ** @see    Locator
   */
  @Override
  public void setDocumentLocator(final Locator locator) {
    this.locator = locator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   push
  /**
   ** Pushes an item on the top of the object stack.
   ** <p>
   ** This has exactly the same effect as:
   ** <blockquote><pre>
   ** addElement(item)</pre></blockquote>
   **
   ** @param  item               the item to be pushed onto this stack.
   **
   ** @return                    the <code>item</code> argument.
   **
   ** @see    Stack#push
   */
  protected Object push(final Object item) {
    return this.values.push(item);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   peek
  /**
   ** Looks at the object at the top of this stack without removing it from the
   ** value stack.
   **
   ** @return                    the object at the top of the value stack (the
   **                            last item of the {@link Stack} object).
   */
  protected Object peek() {
    return this.values.peek();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pop
  /**
   ** Removes the object at the top of this stack and returns that object as
   ** the value of this function.
   **
   ** @return                    the object at the top of the value stack (the
   **                            last item of the {@link Stack} object).
   */
  protected Object pop() {
    return this.values.pop();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: processDocument
  /**
   ** Parse the content of the given {@link InputStream} instance as XML.
   **
   ** @param  stream             the {@link InputStream} containing the content
   **                            to be parsed.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void processDocument(final InputStream stream)
    throws SAXException {

    try {
      processDocument(new InputSource(stream));
    }
    finally {
      try {
        stream.close();
      }
      catch (IOException e) {
        throw new SAXException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: processDocument
  /**
   ** Parse the content of the given {@link InputSource} instance as XML.
   **
   ** @param  source             the {@link InputSource} containing the content
   **                            to be parsed.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void processDocument(final InputSource source)
    throws SAXException {

    try {
      this.parser.parse(source, this);
    }
    catch (IOException e) {
      throw new SAXException(e);
    }
  }
}
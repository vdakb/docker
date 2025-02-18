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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   XMLFileHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    XMLFileHandler.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.69  2017-31-01  DSteding    Changed compatibility annotation
                                               accordingliy to the build.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.parser;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.CDATASection;

import oracle.jdeveloper.workspace.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class XMLFileHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** XML file Descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class XMLFileHandler extends File {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String                      XML_VERSION      = "1.0";
  static final String                      XML_ENCODING     = "UTF-8";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3045453860877761031")
  static final long                        serialVersionUID = -1067568892465490522L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient Element                root;

  private transient DocumentBuilderFactory factory;

  private final Map<String, String>        registry = new HashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Reader
  // ~~~~~ ~~~~~~~
  /**
   ** The Identity and Access Management specific configuration reader.
   */
  protected static class Reader  {

    ////////////////////////////////////////////////////////////////////////////
    // static attributes
    ////////////////////////////////////////////////////////////////////////////

    private static Map<String, XPathExpression> evaluators = new HashMap<String, XPathExpression>();

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   firstElement
    public static Node firstElement(final Element element, final String tagName) {
      // prevent bogus input
      if (element == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_ELEMENT_REQUIRED));
      if (tagName == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_TAG_REQUIRED));

      NodeList list = element.getElementsByTagName(tagName);
      return list != null & list.getLength() > 0 ? list.item(0) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   selectNode
    /**
     ** Returns the W3C Node instance associated with the XPath selection
     ** supplied.
     **
     ** @param  node             the document node to be searched.
     ** @param  expression       the XPath expression to be used in the
     **                          selection.
     **
     ** @return                  the W3C Node instance at the specified
     **                          location in the document, or <code>null</code>.
     */
    public static Node selectNode(final Node node, final String expression) {
      // prevent bogus input
      if (node == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_NODE_REQUIRED));
      if (expression == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_EXPRESSION_REQUIRED));

      try {
        return (Node)evaluator(expression).evaluate(node, XPathConstants.NODE);
      }
      catch (XPathExpressionException e) {
        throw new RuntimeException(Bundle.string(Bundle.PARSER_INVALID_XPATH), e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   selectNodes
    public static NodeList selectNodes(final Node node, final String expression) {
      // prevent bogus input
      if (node == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_NODE_REQUIRED));
      if (expression == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_EXPRESSION_REQUIRED));

      try {
        return (NodeList)evaluator(expression).evaluate(node, XPathConstants.NODESET);
      }
      catch (XPathExpressionException e) {
        throw new RuntimeException(Bundle.string(Bundle.PARSER_INVALID_XPATH), e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   elementsByName
    public static List<Node> elementsByName(final Element element, final String tagName) {
      // prevent bogus input
      if (element == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_ELEMENT_REQUIRED));
      if (tagName == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_TAG_REQUIRED));

      final NodeList nodes = element.getElementsByTagName(tagName);
      if (nodes == null)
        return Collections.EMPTY_LIST;

      final List<Node> list = new ArrayList<Node>();
      for (int i = 0; i < nodes.getLength(); i++) {
        final Node node = nodes.item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE)
          list.add(node);
      }
      return list;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   elements
    public static List<Node> elements(final Element node) {
      // prevent bogus input
      if (node == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_NODE_REQUIRED));

      final NodeList nodes = node.getChildNodes();

      final List<Node> list = new ArrayList<Node>();
      if (nodes == null)
        return list;

      for (int i = 0; i < nodes.getLength(); i++) {
        final Node n = nodes.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE)
          list.add(n);
      }
      return list;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   elementText
    public static String elementText(final Element element) {
      if (element == null)
        return null;

      final StringBuffer buffer = new StringBuffer();
      final NodeList     child  = element.getChildNodes();
      for (int i = 0; i < child.getLength(); i++) {
        final Node node = child.item(i);
        if (node instanceof CDATASection)
          buffer.append(node.getNodeValue());
        else if (node instanceof Text)
          buffer.append(node.getNodeValue());
      }
      return buffer.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   attribute
    /**
     ** Attempts to retrieve the specified attribute from the element.
     **
     ** @param  element          the {@link Element} to inspect.
     ** @param  attributeName    the name of the attribute the value has to
     **                          returned for.
     **
     ** @return                  the attribute value if it exists, otherwise it
     **                          returns <code>null</code>.
     */
    public static String attribute(final Element element, final String attributeName) {
      if (element == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_ELEMENT_REQUIRED));

      if (attributeName == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_ATTRIBUTE_REQUIRED));

      final Attr attribute = element.getAttributeNode(attributeName);
      return (attribute != null) ? attribute.getValue() : null;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   evaluator
    private static XPathExpression evaluator(final String expression)
      throws XPathExpressionException {

      // prevent bogus input
      if (expression == null)
        throw new NullPointerException(Bundle.string(Bundle.PARSER_EXPRESSION_REQUIRED));

      XPathExpression evaluator = evaluators.get(expression);
      if (evaluator == null) {
        // create XPathFactory object
        final XPathFactory factory = XPathFactory.newInstance();
        // create XPath object
        final XPath xpath = factory.newXPath();
        evaluator = xpath.compile(expression);
        evaluators.put(expression, evaluator);
      }

      return evaluator;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>XMLFileHandler</code> instance by converting the given
   ** pathname string into an abstract pathname.
   ** <p>
   ** If the given string is the empty string, then the result is the empty
   ** abstract pathname.
   **
   ** @param  name               the name of the {@link File}.
   **
   ** @throws NullPointerException if the <code>name</code> argument is
   **                              <code>null</code>.
   */
  public XMLFileHandler(final String name) {
    // ensure inheritance
    this(null, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new <code>XMLFileHandler</code> instance from a parent abstract
   ** pathname and a child pathname string.
   **<p>
   ** If <code>parent</code> is <code>null</code> then the new
   ** <code>FileHandler</code> instance is created as if by invoking the
   ** single-argument <code>FileHandler</code> constructor on the given
   ** <code>child</code> pathname string.
   ** <p>
   ** Otherwise the <code>parent</code> abstract pathname is taken to denote a
   ** directory, and the <code>child</code> pathname string is taken to denote
   ** either a directory or a file. If the <code>child</code> pathname string is
   ** absolute then it is converted into a relative pathname in a
   ** system-dependent way. If <code>parent</code> is the empty abstract
   ** pathname then the new <code>FileHandler</code> instance is created by
   ** converting <code>child</code> into an abstract pathname and resolving the
   ** result against a system-dependent default directory. Otherwise each
   ** pathname string is converted into an abstract pathname and the child
   ** abstract pathname is resolved against the parent.
   **
   ** @param parent              the parent abstract pathname
   ** @param  name               the name of the {@link File}.
   **
   ** @throws NullPointerException if the <code>name</code> argument is
   **                              <code>null</code>.
   */
  public XMLFileHandler(final XMLFileHandler parent, final String name) {
    // ensure inheritance
    super(parent, name);

    // initialize instance
    this.factory = DocumentBuilderFactory.newInstance();

    // initialize instance
    register();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootElement
  /**
   ** Sets the root {@link Element} of this descriptor to use as required.
   **
   ** @param  root               the associated root {@link Element}.
   */
  public final void rootElement(final Element root) {
    this.root = root;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootElement
  /**
   ** Returns the root {@link Element} of this <code>XMLFileHandler</code> to
   ** use as required.
   **
   ** @return                       the associated <code>XMLFileHandler</code>.
   */
  public final Element rootElement() {
    return this.root;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   document
  /**
   ** Creates and returns an empty {@link Document}
   **
   ** @return                    the newly created {@link Document}.
   **
   ** @throws XMLFileHandlerException if the {@link Document} is not created by
   **                                 the {@link DocumentBuilderFactory}.
   */
  protected final Document document()
    throws XMLFileHandlerException {

    Document document = null;
    try {
      this.factory.newDocumentBuilder().newDocument();
      document.setXmlVersion(XML_VERSION);
    }
    catch (Exception e) {
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_DOCUMENT_ERROR, e.getMessage()), e);
    }
    return document;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   document
  /**
   ** Loads a <code>XMLFileHandler</code> by parsing and unmarshalling it to a
   ** {@link Document}.
   **
   ** @param  handle             the {@link XMLFileHandler} to parse.
   **
   ** @return                    the parsed {@link Document}.
   **
   ** @throws XMLFileHandlerException if the specified {@link XMLFileHandler}
   **                                 does not meet the requirements.
   */
  protected Document document(final XMLFileHandler handle)
    throws XMLFileHandlerException {

    XMLFileHandler.validateFile(handle);
    try {
      return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(handle);
    }
    catch (Exception e) {
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_READ_ERROR, handle.getAbsolutePath(), e.getMessage()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   document
  /**
   ** Save the specified {@link Document} definitions of the specified
   ** {@link XMLFileHandler}.
   **
   ** @param  handle             the {@link XMLFileHandler} where
   **                            <code>document</code> will be written to.
   ** @param  document           the {@link Document} to save.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  protected void document(final XMLFileHandler handle, final Document document)
    throws XMLFileHandlerException {

    // prevent bogus input
    if (handle == null)
      throw new NullPointerException(Bundle.string(Bundle.PARSER_FILE_REQUIRED));
    if (document == null)
      throw new NullPointerException(Bundle.string(Bundle.PARSER_DOCUMENT_REQUIRED));

    try {
      if (!handle.exists())
        handle.createNewFile();

      XMLFileHandler.validateFile(handle);

      // Use a Transformer for output
      TransformerFactory factory     = TransformerFactory.newInstance();
      Transformer        transformer = factory.newTransformer();
      // we want to pretty format the XML output
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(System.out);
      transformer.transform(source, result);
    }
    catch (Exception e) {
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_WRITE_ERROR, handle.getAbsolutePath()), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Load the importable element definitions from the file.
   **
   ** @param  rootPath                the xpath to the root element of the
   **                                 document this file belongs to.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public void load(final String rootPath)
    throws XMLFileHandlerException {

    // obtain the attributes from the root element to make it possible to handle
    // it properly in any GUI
    rootElement((Element)Reader.selectNode(document(this), rootPath));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Callback method invoked from the constructor to let a subclass hook in the
   ** property registration.
   */
  protected void register() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateFile
  /**
   ** Checks a {@link XMLFileHandler} for
   ** <ul>
   **   <li>existance
   **   <li>is a file.
   **   <li>is readable.
   ** </ul>
   **
   ** @param  handle             the <code>XMLFileHandler</code> to validate.
   **
   ** @throws XMLFileHandlerException if the specified
   **                                 <code>XMLFileHandler</code> does not meet
   **                                 the requirements.
   */
  public static void validateFile(final XMLFileHandler handle)
    throws XMLFileHandlerException {

    if (!handle.exists())
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_FILE_NOTEXISTS, handle.getAbsolutePath()));

    if (!handle.isFile())
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_FILE_NOTAFILE, handle.getAbsolutePath()));

    if (!handle.canRead())
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_FILE_NOTREADABLE, handle.getAbsolutePath()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** Registers a path expression.
   **
   ** @param  parameter          the name of the parameter the xpath has to be
   **                            registered for.
   ** @param  path               the xpath expression to be associated with the
   **                            specified parameter.
   */
  protected void register(final String parameter, final String path) {
    this.registry.put(parameter, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @param  name               the mapped attribute name of the xpath to read.
   **
   ** @return                    the
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public Element element(final String name)
    throws XMLFileHandlerException {

    final String path = this.registry.get(name);
    if (path == null)
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_ATTRIBUTE_NOTMAAPPED, name));

    final Element element = (Element)Reader.selectNode(rootElement(), path);
    if (element == null)
      throw new XMLFileHandlerException(Bundle.format(Bundle.PARSER_ELEMENT_NOTEXISTS, path));

    return element;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propertyValue
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @param  name               the mapped attribute name of the xpath to read.
   **
   ** @return                    the property value obtained from the ANT file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public String propertyValue(final String name)
    throws XMLFileHandlerException {

    return elementAttributeValue(name, "value");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elementAttributeValue
  /**
   ** Fetchs the xpath specified by <code>name</code> from the document.
   **
   ** @param  name               the mapped attribute name of the xpath to read.
   ** @param  attribute          the name of the attribute the value has to
   **                            returned for.
   **
   ** @return                    the attribute value for <code>value</code>
   **                            obtained from the ANT file.
   **
   ** @throws XMLFileHandlerException if the anything goes wrong
   */
  public String elementAttributeValue(final String name, final String attribute)
    throws XMLFileHandlerException {

    return Reader.attribute(element(name), attribute);
  }
}
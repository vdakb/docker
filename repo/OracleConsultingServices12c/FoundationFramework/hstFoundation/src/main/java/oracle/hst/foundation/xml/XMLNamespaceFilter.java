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

    File        :   XMLNamespacFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLNamespacFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

////////////////////////////////////////////////////////////////////////////////
// class XMLNamespaceFilter
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This class is designed to sit between an {@link org.xml.sax.XMLReader
 ** XMLReader} and the client application's event handlers.
 ** <p>
 ** By default, it does nothing but pass requests up to the reader and events
 ** on to the handlers unmodified, but subclasses can override specific methods
 ** to modify the event stream or the configuration requests as they pass
 ** through.
 */
public class XMLNamespaceFilter extends XMLFilterImpl {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String  namespaceUri;
  private boolean addNamespace;

  // state variable
  private boolean addedNamespace = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** This filter will have no parent: you must assign a parent before you start
   ** a parse or do any configuration with setFeature or setProperty, unless you
   ** use this as a pure event consumer rather than as a
   ** {@link org.xml.sax.XMLReader XMLReader}
   */
  public XMLNamespaceFilter() {
    // ensure inhritance
    this("", false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLNamespaceFilter</code> object.
   ** <p>
   ** This creates an object that can be used to write XML in an indented format
   ** to the specified writer. The XML written will be well formed.
   **
   ** @param  namespaceUri       the Namespace URI to inject, or an empty string
   **                            if the Namespace URI is filtered aou.
   ** @param  addNamespace       <code>false</code> if Namespace is filtered
   **                            out
   */
  public XMLNamespaceFilter(final String namespaceUri, boolean addNamespace) {
    // ensure inhritance
    super();

    // initailiza intsnace attributes
    this.namespaceUri = namespaceUri;
    this.addNamespace = addNamespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: startDocument (overridden)
  /**
   ** Receive notification of the beginning of a document.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    #endDocument()
   */
  @Override
  public void startDocument()
    throws SAXException {

    // ensure inheritance
    super.startDocument();

    if (this.addNamespace) {
      startControlledPrefixMapping();
    }
  }

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
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   ** @param  attributes         the attributes attached to the element.
   **                            If there are no attributes, it shall be an
   **                            empty {@link Attributes} object. The value of
   **                            this object after startElement returns is
   **                            undefined.
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

    super.startElement(this.namespaceUri, localName, qualifiedName, attributes);
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
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception
   */
  @Override
  public void endElement(final String uri, final String localName, final String qualifiedName)
    throws SAXException {

    super.endElement(this.namespaceUri, localName, qualifiedName);
  }

  @Override
  public void startPrefixMapping(final String prefix, final String url)
    throws SAXException {


    if (this.addNamespace) {
      this.startControlledPrefixMapping();
    }
    else {
      // Remove the namespace, i.e. don't call startPrefixMapping for parent!
    }
  }

  private void startControlledPrefixMapping()
    throws SAXException {

    if (this.addNamespace && !this.addedNamespace) {
      // we should add namespace since it is set and has not yet been done.
      super.startPrefixMapping("", this.namespaceUri);

      //Make sure we dont do it twice
      this.addedNamespace = true;
    }
  }
}
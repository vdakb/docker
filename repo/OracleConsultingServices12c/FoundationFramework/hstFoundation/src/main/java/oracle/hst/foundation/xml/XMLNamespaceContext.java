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

    File        :   XMLNamespaceContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLNamespaceContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.util.Iterator;
import java.util.Collections;

import javax.xml.XMLConstants;

import javax.xml.namespace.NamespaceContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class XMLNamespaceContext
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Partial {@link NamespaceContext} implementation that takes care of the
 ** implicit namespace bindings (for the <code>xml</code> and <code>xmlns</code>
 ** prefixes) defined in the {@link NamespaceContext} Javadoc.
 */
public abstract class XMLNamespaceContext implements NamespaceContext {

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespaceURI (NamespaceContext)
  /**
   ** Returns Namespace URI bound to a prefix in the current scope.
   ** <p>
   ** When requesting a Namespace URI by prefix, the following table describes
   ** the returned Namespace URI value for all possible prefix values:
   ** <table border="2" rules="all" cellpadding="4" summary="">
   ** <thead>
   ** <tr>
   **   <th align="center" colspan="2">
   **     <code>getNamespaceURI(prefix)</code> return value for specified prefixes
   **   </th>
   ** </tr>
   ** <tr>
   **   <th>prefix parameter</th>
   **   <th>Namespace URI return value</th>
   ** </tr>
   ** </thead>
   ** <tbody>
   ** <tr>
   **   <td><code>DEFAULT_NS_PREFIX</code> ("")</td>
   **   <td>default Namespace URI in the current scope or
   **        <code>{@link javax.xml.XMLConstants#NULL_NS_URI XMLConstants.NULL_NS_URI("")}</code>
   **        when there is no default Namespace URI in the current scope</td>
   ** </tr>
   ** <tr>
   **   <td>bound prefix</td>
   **   <td>Namespace URI bound to prefix in current scope</td>
   ** </tr>
   ** <tr>
   **   <td>unbound prefix</td>
   **   <td>
   **     <code>{@link javax.xml.XMLConstants#NULL_NS_URI XMLConstants.NULL_NS_URI("")}</code>
   **   </td>
   ** </tr>
   ** <tr>
   **   <td><code>XMLConstants.XML_NS_PREFIX</code> ("xml")</td>
   **   <td><code>XMLConstants.XML_NS_URI</code>("http://www.w3.org/XML/1998/namespace")</td>
   ** </tr>
   ** <tr>
   **   <td><code>XMLConstants.XMLNS_ATTRIBUTE</code> ("xmlns")</td>
   **   <td><code>XMLConstants.XMLNS_ATTRIBUTE_NS_URI</code>("http://www.w3.org/2000/xmlns/")</td>
   ** </tr>
   ** <tr>
   **   <td><code>null</code></td>
   **   <td><code>IllegalArgumentException</code> is thrown</td>
   ** </tr>
   ** </tbody>
   ** </table>
   **
   ** @param  prefix             the prefix to look up.
   **
   ** @return                    the Namespace URI bound to prefix in the
   **                            current scope.
   **
   ** @throws IllegalArgumentException when <code>prefix</code> is
   **                                  <code>null</code>.
   */
  @Override
  public String getNamespaceURI(final String prefix) {
    if (prefix == null)
      throw new IllegalArgumentException("prefix can't be null");

    if (prefix.equals(XMLConstants.XML_NS_PREFIX))
      return XMLConstants.XML_NS_URI;
    else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE))
      return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
    else
      return namespaceURI(prefix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrefix (NamespaceContext)
  /**
   ** Returns prefix bound to Namespace URI in the current scope.
   ** <p>
   ** To get all prefixes bound to a Namespace URI in the current scope, use
   ** {@link #getPrefixes(String namespaceURI)}.
   ** <p>
   ** When requesting a prefix by Namespace URI, the following table describes
   ** the returned prefix value for all Namespace URI values:
   ** <table border="2" rules="all" cellpadding="4" summary="">
   ** <thead>
   ** <tr>
   **   <th align="center" colspan="2">
   **     <code>getPrefix(namespaceURI)</code> return value for specified
   **     Namespace URIs
   **   </th>
   ** </tr>
   ** <tr>
   **   <th>Namespace URI parameter</th>
   **   <th>prefix value returned</th>
   ** </tr>
   ** </thead>
   ** <tbody>
   ** <tr>
   **   <td>&lt;default Namespace URI&gt;</td>
   **   <td><code>XMLConstants.DEFAULT_NS_PREFIX</code> ("")</td>
   ** </tr>
   ** <tr>
   **   <td>bound Namespace URI</td>
   **   <td>prefix bound to Namespace URI in the current scope,
   **       if multiple prefixes are bound to the Namespace URI in
   **       the current scope, a single arbitrary prefix, whose
   **       choice is implementation dependent, is returned</td>
   ** </tr>
   ** <tr>
   **   <td>unbound Namespace URI</td>
   **   <td><code>null</code></td>
   ** </tr>
   ** <tr>
   **   <td><code>XMLConstants.XML_NS_URI</code>("http://www.w3.org/XML/1998/namespace")</td>
   **   <td><code>XMLConstants.XML_NS_PREFIX</code> ("xml")</td>
   ** </tr>
   ** <tr>
   **   <td><code>XMLConstants.XMLNS_ATTRIBUTE_NS_URI</code> ("http://www.w3.org/2000/xmlns/")</td>
   **   <td><code>XMLConstants.XMLNS_ATTRIBUTE</code> ("xmlns")</td>
   ** </tr>
   ** <tr>
   **   <td><code>null</code></td>
   **   <td><code>IllegalArgumentException</code> is thrown</td>
   ** </tr>
   ** </tbody>
   ** </table>
   **
   ** @param  namespaceURI       the URI of Namespace to lookup.
   **
   ** @return                    the prefix bound to Namespace URI in current
   **                            context.
   **
   ** @throws IllegalArgumentException when <code>namespaceURI</code> is
   **                                  <code>null</code>.
   */
  @Override
  public String getPrefix(final String namespaceURI) {
    if (namespaceURI == null)
      throw new IllegalArgumentException("namespaceURI can't be null");

    if (namespaceURI.equals(XMLConstants.XML_NS_URI))
      return XMLConstants.XML_NS_PREFIX;
    else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI))
      return XMLConstants.XMLNS_ATTRIBUTE;
    else
      return prefix(namespaceURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrefixes (NamespaceContext)
  /**
   ** Returns all prefixes bound to a Namespace URI in the current scope.
   ** <p>
   ** An Iterator over String elements is returned in an arbitrary,
   ** <strong>implementation dependent</strong>, order.
   ** <p>
   ** <strong>The <code>Iterator</code> is <em>not</em> modifiable, e.g. the
   ** <code>remove()</code> method will throw
   ** <code>UnsupportedOperationException</code>.</strong>
   ** <p>
   ** When requesting prefixes by Namespace URI, the following table describes
   ** the returned prefixes value for all Namespace URI values:
   ** <table border="2" rules="all" cellpadding="4" summary="">
   ** <thead>
   ** <tr>
   **   <th align="center" colspan="2">
   **     <code>getPrefixes(namespaceURI)</code> return value for specified Namespace
   **     URIs</th>
   ** </tr>
   ** <tr>
   **   <th>Namespace URI parameter</th>
   **   <th>prefixes value returned</th>
   ** </tr>
   ** </thead>
   ** <tbody>
   ** <tr>
   **   <td>bound Namespace URI, including the &lt;default Namespace URI&gt;</td>
   **   <td>
   **     <code>Iterator</code> over prefixes bound to Namespace URI in the
   **     current scope in an arbitrary,
   **     <strong>implementation dependent</strong>, order
   **   </td>
   ** </tr>
   ** <tr>
   **   <td>unbound Namespace URI</td>
   **   <td>empty <code>Iterator</code></td>
   ** </tr>
   ** <tr>
   **   <td><code>XMLConstants.XML_NS_URI</code>("http://www.w3.org/XML/1998/namespace")</td>
   **   <td><code>Iterator</code> with one element set to <code>XMLConstants.XML_NS_PREFIX</code> ("xml")</td>
   ** </tr>
   ** <tr>
   **   <td><code>XMLConstants.XMLNS_ATTRIBUTE_NS_URI</code>("http://www.w3.org/2000/xmlns/")</td>
   **   <td>
   **     <code>Iterator</code> with one element set to
   **     <code>XMLConstants.XMLNS_ATTRIBUTE</code> ("xmlns")
   **   </td>
   ** </tr>
   ** <tr>
   **   <td><code>null</code></td>
   **   <td><code>IllegalArgumentException</code> is thrown</td>
   ** </tr>
   ** </tbody>
   ** </table>
   **
   ** @param  namespaceURI       URI of Namespace to lookup.
   **
   ** @return                    <code>Iterator</code> for all prefixes bound to
   **                            the Namespace URI in the current scope.
   **
   ** @throws IllegalArgumentException when <code>namespaceURI</code> is
   **                                  <code>null</code>
   */
  public Iterator<String> getPrefixes(final String namespaceURI) {
    if (namespaceURI == null)
      throw new IllegalArgumentException("namespaceURI can't be null");

    if (namespaceURI.equals(XMLConstants.XML_NS_URI))
      return Collections.singleton(XMLConstants.XML_NS_PREFIX).iterator();
    else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI))
      return Collections.singleton(XMLConstants.XMLNS_ATTRIBUTE).iterator();
    else
      return prefixes(namespaceURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespaceURI
  /**
   ** Returns namespace URI bound to a prefix in the current scope.
   ** <p>
   ** The contract of this method is the same as
   ** {@link NamespaceContext#getNamespaceURI(String)}, except that the
   ** implementation is not required to handle the implicit namespace bindings.
   **
   ** @param  prefix             the prefix to look up.
   **
   ** @return                    the Namespace URI bound to prefix in the
   **                            current scope.
   */
  protected abstract String namespaceURI(final String prefix);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Returns prefix bound to namespace URI in the current scope.
   ** <p>
   ** The contract of this method is the same as
   ** {@link NamespaceContext#getPrefix(String)}, except that the implementation
   ** is not required to handle the implicit namespace bindings.
   **
   ** @param  namespaceURI       the URI of Namespace to lookup.
   **
   ** @return                    the prefix bound to Namespace URI in current
   **                            context.
   */
  protected abstract String prefix(final String namespaceURI);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Returns all prefixes bound to a namespace URI in the current scope.
   ** <p>
   ** The contract of this method is the same as
   ** {@link NamespaceContext#getPrefixes(String)}, except that the
   ** implementation is not required to handle the implicit namespace bindings.
   **
   ** @param  namespaceURI       the URI of Namespace to lookup.
   **
   ** @return                    an iterator for all prefixes bound to the
   **                            namespace URI in the current scope.
   */
  protected abstract Iterator<String> prefixes(final String namespaceURI);
}
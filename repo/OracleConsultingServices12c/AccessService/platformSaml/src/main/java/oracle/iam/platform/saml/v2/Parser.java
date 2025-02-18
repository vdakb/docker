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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Parser.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Parser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.v2;

import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.XMLConstants;

import javax.xml.namespace.NamespaceContext;

import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import oracle.iam.platform.saml.AssertionError;
import oracle.iam.platform.saml.AssertionProcessor;
import oracle.iam.platform.saml.AssertionException;

import oracle.iam.platform.saml.AssertionBundle;

import oracle.iam.platform.saml.v2.schema.NameID;
import oracle.iam.platform.saml.v2.schema.Issuer;
import oracle.iam.platform.saml.v2.schema.Version;
import oracle.iam.platform.saml.v2.schema.Subject;
import oracle.iam.platform.saml.v2.schema.Statement;
import oracle.iam.platform.saml.v2.schema.Assertion;
import oracle.iam.platform.saml.v2.schema.Conditions;
import oracle.iam.platform.saml.v2.schema.AuthnContext;
import oracle.iam.platform.saml.v2.schema.AuthnStatement;
import oracle.iam.platform.saml.v2.schema.SubjectConfirmation;
import oracle.iam.platform.saml.v2.schema.SubjectConfirmationData;

////////////////////////////////////////////////////////////////////////////
// final class Parser
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** SAML Assertion Parser class of Java Toolkit.
 ** <p>
 ** A class that implements the settings parser from IdP Metadata
 ** <p>
 ** This class does not validate in any way the URL that is introduced, make
 ** sure to validate it properly before use it in a metadata method.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Parser {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** SAML 2.0 Assertion QName prefix. */
  public static final String            PREFIX    = "saml";

  /** SAML 2.0 Assertion XML Namespace. */
  public static final String            NAMESPACE = "urn:oasis:names:tc:SAML:2.0:assertion";

  /** SAML 2.0 Assertion Namespace Context. */
  private static final NamespaceContext CONTEXT   = new Context();

  ////////////////////////////////////////////////////////////////////////////
  // Member classes
  ////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // class Context
  // ~~~~~ ~~~~~~~~~
  /**
   ** The provider of the well known namespaces of a SAML Identity Assertion.
   */
  private static class Context implements NamespaceContext {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////
    
    static final Map<String, String> mapping = Collections.singletonMap(PREFIX, NAMESPACE);

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Context</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Context() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: getNamespaceURI (NamespaceContext)
    /**
     ** Return a Namespace URI bound to a prefix in the current scope.
     ** <p>
     ** When requesting a Namespace URI by prefix, the following table describes
     ** the returned Namespace URI value for all
     ** possible prefix values:
     ** <table border="2" rules="all" cellpadding="4">
     ** <thead>
     ** <tr>
     **   <td align="center" colspan="2">
     **     <code>getNamespaceURI(prefix)</code> return value for specified
     **     prefixes
     **   </td>
     ** </tr>
     ** <tr>
     **   <td>Prefix Parameter</td>
     **   <td>Namespace URI return value</td>
     ** </tr>
     ** </thead>
     ** <tbody>
     ** <tr>
     **   <td><code>DEFAULT_NS_PREFIX</code> ("")</td>
     **   <td>Default Namespace URI in the current scope or
     **       {@link XMLConstants#NULL_NS_URI XMLConstants.NULL_NS_URI("")}
     **       when there is no default Namespace URI in the current scope.
     **   </td>
     ** </tr>
     ** <tr>
     **   <td>Bound Prefix</td>
     **   <td>Namespace URI bound to prefix in current scope</td>
     ** </tr>
     ** <tr>
     **   <td>Unbound Prefix</td>
     **   <td>{@link XMLConstants#NULL_NS_URI XMLConstants.NULL_NS_URI("")}</td>
     ** </tr>
     ** <tr>
     **   <td><code>XMLConstants.XML_NS_PREFIX</code> ("xml")</td>
     **   <td><code>XMLConstants.XML_NS_URI</code> ("http://www.w3.org/XML/1998/namespace")</td>
     ** </tr>
     ** <tr>
     **   <td><code>XMLConstants.XMLNS_ATTRIBUTE</code> ("xmlns")</td>
     **   <td><code>XMLConstants.XMLNS_ATTRIBUTE_NS_URI</code> ("http://www.w3.org/2000/xmlns/")</td>
     ** </tr>
     ** <tr>
     **   <td><code>null</code></td>
     **   <td><code>IllegalArgumentException</code> is thrown</td>
     ** </tr>
     ** </tbody>
     ** </table>
     **
     ** @param  prefix           the namespace prefix to look up.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the Namespace URI bound to prefix in the
     **                          current scope; or
     **                          {@link XMLConstants#NULL_NS_URI} if the prefix
     **                          is not mapped.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @throws IllegalArgumentException when <code>prefix</code> is
     **                                  <code>null</code>.
     */
    @Override
    public String getNamespaceURI(final String prefix) {
      return (mapping.containsKey(prefix)) ? mapping.get(prefix) : XMLConstants.NULL_NS_URI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getPrefix (NamespaceContext)
    /**
     ** Returns the prefix bound to Namespace URI in the current scope.
     ** <p>
     ** To get all prefixes bound to a Namespace URI in the current scope, use
     ** {@link #getPrefixes(String namespaceURI)}.
     **
     ** @param  uri              the URI of Namespace to lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the prefix bound to Namespace URI in current
     **                          context.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @throws IllegalArgumentException when <code>uri</code> is
     **                                  <code>null</code>.
     */
    @Override
    public String getPrefix(final String uri) {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getPrefixes (NamespaceContext)
    /**
     ** Returns all prefixes bound to a Namespace URI in the current scope.
     ** </p>
     ** An {@link Iterator} over String elements is returned in an arbitrary,
     ** <strong>implementation dependent</strong>, order.
     ** <p>
     ** <strong>The <code>Iterator</code> is <em>not</em> modifiable. e.g. the
     ** <code>remove()</code> method will throw
     ** <code>UnsupportedOperationException</code>.</strong>
     **
     ** @param  uri              the URI of Namespace to lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  an {@link Iterator} for all prefixes bound to
     **                          the Namespace URI in the current scope.
     **                          <br>
     **                          Possible object is {@link Iterator} over
     **                          {@link String}.
     *
     * @throws IllegalArgumentException When <code>namespaceURI</code> is
     *   <code>null</code>
     */
    @Override
    public Iterator<?> getPrefixes(final String uri) {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Parser</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Parser() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertion
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link Assertion} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link Assertion} object.
   **                            <br>
   **                            Possible object is {@link Assertion}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  public static Assertion assertion(final Element node)
    throws AssertionException {

    return Assertion.builder()
      .id(node.getAttribute(Assertion.ID))
      .version(Version.of(node.getAttribute(Assertion.VERSION)))
      .issueInstant(AssertionProcessor.dateTime(node.getAttribute(Assertion.ISSUE_INSTANT)))
      .issuer(issuer(node))
      .subject(subject(node))
      .condition(condition(node))
      .statement(authnStatement(node))
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   issuer
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link Issuer} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link Issuer} object.
   **                            <br>
   **                            Possible object is {@link Issuer}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static Issuer issuer(final Element node)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    final Element cursor = AssertionProcessor.node(node, xpath(Issuer.LOCAL));
    return Issuer.builder().value(cursor.getTextContent()).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subject
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link Subject} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param node the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link Subject} object.
   **                            <br>
   **                            Possible object is {@link Subject}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static Subject subject(final Element node)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    final Element cursor = AssertionProcessor.node(node, xpath(Subject.LOCAL));
    return Subject.builder()
      .nameID(nameID(cursor))
      .confirmation(subjectConfirmation(cursor))
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subjectConfirmation
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link List} of {@link SubjectConfirmation} objects.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new collection of
   **                            {@link SubjectConfirmation} s object.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link SubjectConfirmation}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static List<SubjectConfirmation> subjectConfirmation(final Element node)
    throws AssertionException {

    List<SubjectConfirmation> result = null;
    final NodeList            list   = AssertionProcessor.list(node, xpath(SubjectConfirmation.LOCAL));
    if (list != null && list.getLength() > 0) {
      result = new ArrayList<SubjectConfirmation>(list.getLength());
      for (int i = 0; i < list.getLength(); i++) {
        final Element cursor = (Element)list.item(i);
        result.add(SubjectConfirmation.builder()
//          .baseID(baseID(cursor))
//          .nameID(nameID(cursor))
//          .encryptedID(encryptedID(cursor))
          .data(subjectConfirmationData(cursor))
          .method(cursor.getAttribute(SubjectConfirmation.METHOD))
          .build()
        );
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subjectConfirmationData
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link oracle.iam.platform.v2.schema.SubjectConfirmationData} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   * @return                     a new {@link SubjectConfirmationData} object.
   **                            <br>
   **                            Possible object is
   **                            {@link SubjectConfirmationData}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static SubjectConfirmationData subjectConfirmationData(final Element node)
    throws AssertionException {
    
    final Element cursor = AssertionProcessor.node(node, xpath(SubjectConfirmationData.LOCAL));
    return SubjectConfirmationData.builder()
//      .before(zonedDateTime(cursor.getAttribute(SubjectConfirmationData.BEFORE)))
//      .after(zonedDateTime(cursor.getAttribute(SubjectConfirmationData.AFTER)))
      .address(cursor.getAttribute(SubjectConfirmationData.ADDRESS))
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameID
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link NameID} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param node the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link NameID} object.
   **                            <br>
   **                            Possible object is {@link NameID}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static NameID nameID(final Element node)
    throws AssertionException {

    final Element cursor = AssertionProcessor.node(node, xpath(NameID.LOCAL));
    return NameID.builder()
      .value(cursor.getTextContent())
      .format(cursor.getAttribute(NameID.FORMAT))
      .spProvidedID(cursor.getAttribute(NameID.SPPROVIDED_ID))
      .nameQualifier(cursor.getAttribute(NameID.NAME_QUALIFIER))
      .spNameQualifier(cursor.getAttribute(NameID.SP_NAME_QUALIFIER))
      .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   conditions
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link Conditions} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param node                the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link Conditions} object.
   **                            <br>
   **                            Possible object is {@link Conditions}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static Conditions condition(final Element node)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    final Element cursor = AssertionProcessor.node(node, xpath(Conditions.LOCAL));
    return Conditions.builder()
      .before(AssertionProcessor.dateTime(cursor.getAttribute(Conditions.BEFORE)))
      .after(AssertionProcessor.dateTime(cursor.getAttribute(Conditions.AFTER)))
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authnStatement
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link oracle.iam.platform.v2.schema.AuthnStatement} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return a new collection of {@link AuthnStatement}
   **                            object.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AuthnStatement}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static List<Statement> authnStatement(final Element node)
    throws AssertionException {

    List<Statement> result = null;
    final NodeList  list   = AssertionProcessor.list(node, xpath(AuthnStatement.LOCAL));
    if (list != null && list.getLength() > 0) {
      result = new ArrayList<Statement>(list.getLength());
      for (int i = 0; i < list.getLength(); i++) {
        final Element cursor = (Element)list.item(i);
        result.add(AuthnStatement.builder()
            .authnInstant(AssertionProcessor.dateTime(cursor.getAttribute(AuthnStatement.AUTHN_INSTANT)))
           .sessionAfter(AssertionProcessor.dateTime(cursor.getAttribute(AuthnStatement.SESSION_AFTER)))
            .sessionIndex(cursor.getAttribute(AuthnStatement.SESSION_INDEX))
            .authnContext(authnContext(cursor))
            .build()
        );
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authnContext
  /**
   **Parse the content of the given XML {@link Element} and return a new
   ** {@link AuthnContext} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node              the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link AuthnContext} object.
   **                            <br>
   **                            Possible object is {@link AuthnContext}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static AuthnContext authnContext(final Element node)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    final Element cursor = AssertionProcessor.node(node, xpath(AuthnContext.LOCAL));
    return AuthnContext.builder()
      .classRef(authnContextClassRef(cursor))
      .decl(authnContextDecl(cursor))
      .declRef(authnContextDeclRef(cursor))
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authnContextClassRef
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link AuthnContext.ClassRef} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link AuthnContext.ClassRef} object.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthnContext.ClassRef}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static AuthnContext.ClassRef authnContextClassRef(final Element node)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    final Element cursor = AssertionProcessor.node(node, xpath(oracle.iam.platform.saml.v2.schema.AuthnContext.ClassRef.LOCAL));
    return cursor == null ? null : AuthnContext.classRef(cursor.getTextContent());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authnContextDecl
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link AuthnContext.Decl} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link AuthnContext.Decl} object.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthnContext.Decl}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static AuthnContext.Decl authnContextDecl(final Element node)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    final Element cursor = AssertionProcessor.node(node, xpath(oracle.iam.platform.saml.v2.schema.AuthnContext.Decl.LOCAL));
    return cursor == null ? null : AuthnContext.decl(cursor.getTextContent());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authnContextDeclRef
  /**
   ** Parse the content of the given XML {@link Element} and return a new
   ** {@link AuthnContext.DeclRef} object.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link Element} is
   ** <code>null</code>.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Element}.
   **
   ** @return                    a new {@link AuthnContext.DeclRef} object.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthnContext.DeclRef}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>node</code> is <code>null</code>.
   */
  private static AuthnContext.DeclRef authnContextDeclRef(final Element node)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    final Element cursor = AssertionProcessor.node(node, xpath(oracle.iam.platform.saml.v2.schema.AuthnContext.DeclRef.LOCAL));
    return cursor == null ? null : AuthnContext.declRef(cursor.getTextContent());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   xpath
  /**
   ** Compile an {@link XPath} expression for later evaluation.
   ** <p>
   ** If <code>local</code> contains any {@link XPathFunction}s, they must be
   ** available via the {@link XPathFunctionResolver}.
   ** <br>
   ** An {@link XPathExpressionException} will be thrown if the
   ** <code>XPathFunction</code> cannot be resovled with the
   ** <code>XPathFunctionResolver</code>.
   ** <p>
   ** If <code>local</code> contains any variables, the
   ** {@link XPathVariableResolver} in effect <strong>at compile time</strong>
   ** will be used to resolve them.
   ** <p>
   ** If <code>local</code> is <code>null</code>, a
   ** <code>NullPointerException</code> is thrown.
   **
   **  @param  local             the string representation of the XPath
   **                            expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the compiled XPath expression.
   **                            <br>
   **                            Possible object is {@link XPathExpression}.
   **
   ** @throws AssertionException   if <code>expression</code> cannot be
   **                              compiled.
   ** @throws NullPointerException if <code>local</code> is <code>null</code>.
   */
  private static XPathExpression xpath(final String local)
    throws AssertionException {

    return AssertionProcessor.compile(CONTEXT, PREFIX, local);
  }
}
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

    File        :   AssertionProcessor.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AssertionProcessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml;

import java.util.Set;
import java.util.Objects;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.time.format.DateTimeFormatter;

import java.security.PublicKey;

import javax.xml.XMLConstants;

import javax.xml.namespace.NamespaceContext;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Source;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXParseException;

import oracle.hst.platform.core.logging.Logger;

import oracle.hst.platform.core.utility.DateUtility;

import oracle.iam.platform.saml.xml.SignatureValidator;

import oracle.iam.platform.saml.v2.Parser;

import oracle.iam.platform.saml.v2.schema.Assertion;
import oracle.iam.platform.saml.v2.schema.Conditions;

////////////////////////////////////////////////////////////////////////////
// final class AssertionProcessor
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An unmarshaller for SAML 2.0 objects.
 ** <p>
 ** SAML assertions are popular method for passing authentication and
 ** authorization information between identity providers and consumers using
 ** various single sign-on protocols.
 ** <p>
 ** References:
 ** <ul>
 **   <li>[1] OASIS Standard. [SAMLAuthnCxt]
 **           <br>
 **           <a href="http://docs.oasis-open.org/security/saml/v2.0/saml-authn-context-2.0-os.pdf">Authentication Context for the OASIS Security Assertion Markup Language (SAML) V2.0, March 2005.</a>
 **   <li>[2] OASIS Standard [SAMLBind]
 **           <br>
 **           <a href="https://docs.oasis-open.org/security/saml/v2.0/saml-bindings-2.0-os.pdf">Bindings for the OASIS Security Assertion Markup Language (SAML) V2.0, March 2005.</a>
 **   <li>[3] OASIS Standard [SAMLConform]
 **           <br>
 **           <a href="http://docs.oasis-open.org/security/saml/v2.0/saml-conformance-2.0-os.pdf">Conformance Requirements for the OASIS Security Assertion Markup Language (SAML) V2.0, March 2005.</a>
 **   <li>[4] OASIS Standard [SAMLCore]
 **           <br>
 **           <a href="https://docs.oasis-open.org/security/saml/v2.0/saml-core-2.0-os.pdf">Assertions and Protocols for the OASIS Security Assertion Markup Language (SAML) V2.0, March 2005.</a>
 **   <li>[5] OASIS Standard [SAMLGloss]
 **           <br>
 **           <a href="https://docs.oasis-open.org/security/saml/v2.0/saml-glossary-2.0-os.pdf">Glossary for the OASIS Security Assertion Markup Language (SAML) V2.0, March 2005.</a>
 **   <li>[6] OASIS Standard [SAMLMeta]
 **           <br>
 **           <a href="https://docs.oasis-open.org/security/saml/v2.0/saml-metadata-2.0-os.pdf">Metadata for the OASIS Security Assertion Markup Language SAML) V2.0, March 2005.</a>
 **   <li>[7] OASIS Standard [SAMLProf]
 **           <br>
 **           <a href="https://docs.oasis-open.org/security/saml/v2.0/saml-profiles-2.0-os.pdf">Profiles for the OASIS Security Assertion Markup Language (SAML) V2.0, March 2005.</a>
 **   <li>[8] OASIS Standard [SAMLSec]
 **           <br>
 **           <a href="https://docs.oasis-open.org/security/saml/v2.0/saml-sec-consider-2.0-os.pdf">Security and Privacy Considerations for the OASIS Security Assertion Markup Language (SAML) V2.0, March 2005.</a>
 ** </ul>
 ** <p>
 ** However their practical security strongly depends on correct implementation,
 ** especially on the consumer side.
 ** <p>
 ** Somorovsky and others have demonstrated a number of XML signature related
 ** vulnerabilities in SAML assertion validation frameworks. Refer to:
 ** <ul>
 **   <li>[9] Somorovsky, J., Mayer, A., Schwenk, J., Kampmann, M., and Jensen, M.
 **       <br>
 **       <a href="https://www.usenix.org/system/files/conference/usenixsecurity12/sec12-final91-8-23-12.pdf">On Breaking SAML: Be Whoever You Want to Be</a>
 **   <li>[10] Somorovsky, J., Mayer, A., Schwenk, J., Kampmann, M., and Jensen, M.
 **       <br>
 **       <a href="https://www.usenix.org/conference/usenixsecurity12/technical-sessions/presentation/somorovsky">On Breaking SAML: Be Whoever You Want to Be</a>
 **       <br>
 **       In Proceedings of the 21st USENIX Security Symposium, 2012 (April 2013).
 ** </ul>
 ** <p>
 ** To countermeasure the vulnerabilities detected this implementation follows
 ** the recommendation of Open Web Application Security Project (OWASP):
 ** <ul>
 **   <li>[11] Pawel Krawczyk, pawel.krawczyk@owasp.org
 **       <br>
 **       <a href="https://arxiv.org/ftp/arxiv/papers/1401/1401.7483.pdf">Secure SAML validation to prevent XML signature wrapping attacks</a>
 **   <li>[12]
 **       <br>
 **       <a href="https://cheatsheetseries.owasp.org/cheatsheets/SAML_Security_Cheat_Sheet.html">OWASP Cheat Sheet Series :: SAML Security Cheat Sheet</a>
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class AssertionProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The SAML 2.0 Schema to validate against */
//  static final String               SCHEMA     = "saml/v2/xsd/saml-schema-assertion-2.0.xsd";

  /** The XPath factore to discover nodes. */
  private static final XPathFactory FACTORY    = XPathFactory.newInstance();
  /**
   ** Get the JVM’s current default time zone.
   ** <br>
   ** Can change at any moment during runtime.
   ** <br>
   ** If important, confirm with the user.
   */
  static final ZoneId               TIMEZONE   = ZoneId.systemDefault();
  /**
   ** The XML Schema standard defines clear rules for specifying dates in XML
   ** format. In order to use this format, the Java class XMLGregorianCalendar,
   ** introduced in Java 1.5, is a representation of the W3C XML Schema 1.0
   ** date/time datatypes.
   */
  static final DateTimeFormatter    TIMEFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(TIMEZONE);

  private final static Logger       LOGGER     = Logger.create(AssertionProcessor.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // class PayloadError
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** A basic implementation for SAX error handler.
   */
  private static class PayloadError implements ErrorHandler {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: warning (ErrorHandler)
    /**
     ** Receive notification of a warning.
     ** <p>
     ** SAX parsers will use this method to report conditions that are not
     ** errors or fatal errors as defined by the XML recommendation.
     ** <p>
     ** The SAX parser must continue to provide normal parsing events after
     ** invoking this method: it should still be possible for the application to
     ** process the document through to the end.
     **
     ** @param  throwable        the warning information encapsulated in a SAX
     **                          parse exception.
     **                          <br>
     **                          Allowed object is {@link SAXParseException}.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see    SAXParseException
     */
    @Override
    public void warning(final SAXParseException throwable)
      throws SAXException {

      LOGGER.warn(AssertionBundle.string(AssertionError.PARSER_WARNING, throwable.getLocalizedMessage()));
      throw throwable;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: error (ErrorHandler)
    /**
     ** Receive notification of a recoverable error.
     ** <p>
     ** This corresponds to the definition of "error" in section 1.2 of the W3C
     ** XML 1.0 Recommendation. For example, a validating parser would use this
     ** callback to report the violation of a validity constraint.
     ** <p>
     ** The SAX parser must continue to provide normal parsing events after
     ** invoking this method: it should still be possible for the application to
     ** process the document through to the end. If the application cannot do
     ** so, then the parser should report a fatal error even if the XML
     ** recommendation does not require it to do so.
     **
     ** @param  throwable        the error information encapsulated in a SAX
     **                          parse exception.
     **                          <br>
     **                          Allowed object is {@link SAXParseException}.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see    SAXParseException
     */
    @Override
    public void error(final SAXParseException throwable)
      throws SAXException {

      LOGGER.warn(AssertionBundle.string(AssertionError.PARSER_ERROR, throwable.getLocalizedMessage()));
      throw throwable;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fatalError (ErrorHandler)
    /**
     ** Receive notification of a non-recoverable error.
     ** <p>
     ** <strong>There is an apparent contradiction between the documentation for
     ** this method and the documentation for
     ** {@link org.xml.sax.ContentHandler#endDocument}. Until this ambiguity is
     ** resolved in a future major release, clients should make no assumptions
     ** about whether endDocument() will or will not be invoked when the parser
     ** has reported a fatalError() or thrown an exception.</strong>.
     ** <p>
     ** This corresponds to the definition of "fatal error" in section 1.2 of the
     ** W3C XML 1.0 Recommendation. For example, a parser would use this callback
     ** to report the violation of a well-formedness constraint.
     ** <p>
     ** The application must assume that the document is unusable after the parser
     ** has invoked this method, and should continue (if at all) only for the sake
     ** of collecting additional error messages: in fact, SAX parsers are free to
     ** stop reporting any other events once this method has been invoked.
     **
     ** @param  throwable          the error information encapsulated in a SAX
     **                            parse exception.
     **
     ** @throws SAXException       any SAX exception, possibly wrapping another
     **                            exception.
     **
     ** @see    SAXParseException
     */
    @Override
    public void fatalError(final SAXParseException throwable)
      throws SAXException {

      LOGGER.warn(AssertionBundle.string(AssertionError.PARSER_FATAL, throwable.getLocalizedMessage()));
      throw throwable;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Resolver
  // ~~~~~ ~~~~~~~~
  /**
   ** A basic implementation for SAX entity resolver.
   */
  private static class Resolver implements EntityResolver {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Indicator for debugging purpose */
    private static final String THIS = Resolver.class.getName();

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: warning (EntityResolver)
    /**
     ** The parser will call this method before opening any external entity
     ** except the top-level document entity. Such entities include the external
     ** DTD subset and external parameter entities referenced within the DTD
     ** (in either case, only if the parser reads external parameter entities),
     ** and external general entities referenced within the document element (if
     ** the parser reads external general entities).
     **
     ** @param  publicId         the public identifier of the external entity
     **                          being referenced, or <code>null</code> if none
     **                          was supplied.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  systemId         the system identifier of the external entity
     **                          being referenced.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  an {@link InputSource} object describing the
     **                          new input source, or <code>null</code> to
     **                          request that the parser open a regular URI
     **                          connection to the system identifier.
     **                          <br>
     **                          Possible object is {@link InputSource}.
     **
     ** @throws IOException      a Java-specific IO exception, possibly the
     **                          result of creating a new InputStream or Reader
     **                          for the {@link InputSource}.
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     */
    @Override
    public InputSource resolveEntity(final String publicId, final String systemId)
      throws IOException
      ,      SAXException {

      final String method="resolveEntity";
      LOGGER.entering(THIS, method);

      String file = null;
      if (systemId.endsWith("XMLSchema.dtd")) {
        file = "saml/v2/xsd/xml-schema.dtd";
      }
      else if (systemId.endsWith("datatypes.dtd")) {
        file = "saml/v2/xsd/xml-datatypes.dtd";
      }
      else if (systemId.endsWith("xenc-schema.xsd")) {
        file = "saml/v2/xsd/xenc-schema.xsd";
      }
      else if (systemId.endsWith("xmldsig-core-schema.xsd")) {
        file = "saml/v2/xsd/xmldsig-core-schema.xsd";
      }
      else if (systemId.endsWith("saml-schema-assertion-2.0.xsd")) {
        file = "saml/v2/xsd/saml-schema-assertion-2.0.xsd";
      }
      else if (systemId.endsWith("saml-schema-protocol-2.0.xsd")) {
        file = "saml/v2/xsd/saml-schema-protocol-2.0.xsd";
      }
      LOGGER.debug(AssertionBundle.string(AssertionMessage.PARSER_ENTITY_RESOLVED, systemId, publicId, file));

      InputSource schema = null;
      if (file != null) {
        schema = new InputSource(AssertionProcessor.class.getClassLoader().getResourceAsStream(file));
      }
      LOGGER.exiting(THIS, method, schema);
      return schema;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Builder
  // ~~~~ ~~~~~~~
  /**
   ** A {@link DocumentBuilderFactory} unmarshaller for SAML 2.0 objects.
   */
  enum Builder {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** Indicator for debugging purpose */
    private static final String                 THIS = Builder.class.getName();

    private static final DocumentBuilderFactory dbf  = DocumentBuilderFactory.newInstance();

    ////////////////////////////////////////////////////////////////////////////
    // static init block
    ////////////////////////////////////////////////////////////////////////////

    // extend the features of the document builder factory with the necessary
    // properties
    static {
      dbf.setValidating(true);
      dbf.setNamespaceAware(true);
      dbf.setIgnoringComments(true);
      dbf.setIgnoringElementContentWhitespace(true);
      /*
       * Tell the factory that W3C XML schema language will be used
       */
      dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", XMLConstants.W3C_XML_SCHEMA_NS_URI);
      /*
       * Process XML documents within resource limits to prevent DoS.
       *
       * Reference:
       * http://docs.oracle.com/javase/7/docs/api/javax/xml/XMLConstants.html#FEATURE_SECURE_PROCESSING
       */
      factoryFeature(dbf, XMLConstants.FEATURE_SECURE_PROCESSING, true);
      /*
       * Prevent automated downloads of external DTD and schemas and only use
       * those provided locally by EntityResolver.
       *
       * References:
       * http://docs.oracle.com/javase/7/docs/api/javax/xml/XMLConstants.html#ACCESS_EXTERNAL_DTD
       * http://docs.oracle.com/javase/7/docs/api/javax/xml/XMLConstants.html#ACCESS_EXTERNAL_SCHEMA
       */
      dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,    "file,jar");
      dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "file,jar");
      /*
       * Try to enable stricter validator settings if the XML parser
       * implementation supports them and disable automation that could
       * potentially lead to attacks.
       *
       * Reference:
       * http://xerces.apache.org/xerces2-j/features.html
       */
      // disable DTD to prevent override of ID elements
      factoryFeature(dbf, "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
      // disable schemaLocation override and only rely on local EntityResolver
      factoryFeature(dbf, "http://apache.org/xml/features/honour-all-schemaLocations", false);
      // validate schema
      factoryFeature(dbf, "http://apache.org/xml/features/validation/schema", true);
      factoryFeature(dbf, "http://apache.org/xml/features/validation/schema-full-checking", true);
      // additional features, not available in all parsers
      factoryFeature(dbf, "http://apache.org/xml/features/validation/id-idref-checking", true);
      factoryFeature(dbf, "http://apache.org/xml/features/validation/identity-constraint-checking", true);
      factoryFeature(dbf, "http://apache.org/xml/features/standard-uri-conformant", true);
      factoryFeature(dbf, "http://xml.org/sax/features/unicode-normalization-checking", true);
      // prevent external entity processing (XXE)
      factoryFeature(dbf, "http://xml.org/sax/features/external-general-entities", false);
      factoryFeature(dbf, "http://apache.org/xml/features/disallow-doctype-decl",  false);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: parse
    /**
     ** Parse the content of the given URI as an XML document and return a new
     ** DOM {@link Element} object.
     ** <br>
     ** An <code>NullPointerException</code> is thrown if the
     ** {@link InputSource} is <code>null</code>.
     **
     ** @param  source           the location of the content to be parsed.
     **                          <br>
     **                          Allowed object is {@link InputSource}.
     **
     ** @return                  a new DOM {@link Document} object.
     **                          <br>
     **                          Possible object is {@link Document}.
     **
     ** @throws AssertionException   if any parse errors or any IO errors occur.
     ** @throws NullPointerException if <code>source</code> is
     **                               <code>null</code>.
     */
    public Document parse(final InputSource source)
      throws AssertionException {

      return parse(source, errorHandler());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: parse
    /**
     ** Parse the content of the given URI as an XML document and return a new
     ** DOM {@link Element} object.
     ** <br>
     ** An <code>NullPointerException</code> is thrown if the
     ** {@link InputSource} is <code>null</code>.
     ** <p>
     ** The supplied SAX error handler is attached to the parser to track what's
     ** happens.
     **
     ** @param  source           the location of the content to be parsed.
     **                          <br>
     **                          Allowed object is {@link InputSource}.
     ** @param  errorHandler     the SAX error handler to attach.
     **                          <br>
     **                          Allowed object is {@link ErrorHandler}.
     **
     ** @return                  a new DOM {@link Document} object.
     **                          <br>
     **                          Possible object is {@link Document}.
     **
     ** @throws AssertionException   if any parse errors or any IO errors occur.
     ** @throws NullPointerException if <code>source</code> is
     **                               <code>null</code>.
     */
    public Document parse(final InputSource source, final ErrorHandler errorHandler)
      throws AssertionException {

      // prevent bogus input
      Objects.requireNonNull(source, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "source"));

      final String method="parse";
      LOGGER.entering(THIS, method);
      // TODO: we need to do some caching of the schema to avaoid parsing it
      //       on each request
      //       unfortunately set it in the static initializer block will end up
      //       in an exhausted stream for the resource
      //       as a workaround fetching the schema on every request solve this
      //       for the time being but its expensive
      /*
    	 * Specify the *initial* schema.
       * This should be schema for the root document even if it uses a number
       * of other schemas (through namespaces).
  	   *
  	   * We supply it as InputSource because if it's supplied as string, the
       * parser will download it automatically each time the processor is run.
       * InputSource will point to the same file, just downloaded and stored
       * locally. Example on how to supply it:
  	   *
  	   * All remaining schemas will be supplied through entity resolver
       * (see below). The tricky part seems to be that if the initial schema
       * is not supplied via schemaSource, the entity resolver will not be
       * ever called.
  	   */
      dbf.setAttribute(
        "http://java.sun.com/xml/jaxp/properties/schemaSource"
      , AssertionProcessor.class.getClassLoader().getResourceAsStream("saml/v2/xsd/saml-schema-assertion-2.0.xsd")
      );
      try {
        // create XML parser object from previously configured builder
        final DocumentBuilder builder = dbf.newDocumentBuilder();
        // assign a separate error handler to the XML parses.
        // This wouldn't be really necessary but it's Java requirement.
        // If validation used (and we do) we need to have an error handler.
        builder.setErrorHandler(errorHandler);
        // configure an entity resolver, function that will return appropriate
        // schemas to the parser on demand.
        // This is needed for two reasons:
        // 1) parser would normally download them automatically, but it usually
        //    takes a lot of time and they are not cached;
        // 2) schemas that are referenced with non-URL addresses (not "http://")
        //    cannot be downloaded automatically
        builder.setEntityResolver(entityResolver());
        return builder.parse(source);
      }
      catch (SAXException e) {
        throw AssertionException.abort(e);
      }
      catch (ParserConfigurationException e) {
        throw AssertionException.general(e);
      }
      catch (IOException e) {
        throw AssertionException.unhandled(e);
      }
      finally {
        LOGGER.exiting(THIS, method);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parse the content of the given string as an XML document and return a new
   ** {@link Assertion} java content tree.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link String}
   ** <code>source</code> is <code>null</code>.
   **
   ** @param  publicKey          the validating key for the required signature.
   **                            <br>
   **                            Allowed object is {@link PublicKey}.
   ** @param  source             the string representation of an XML document to
   **                            be validated and parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   *
   * @return                     a new {@link Assertion} java content tree.
   **                            <br>
   **                            Possible object is {@link Assertion}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>publicKey</code> and/or
   ***                             <code>source</code> is <code>null</code>.
   */
  public static Assertion parse(final PublicKey publicKey, final String source)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(source, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "source"));
    return parse(publicKey, new InputSource(new StringReader(source)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parse the content of the given stream as an XML document and return a new
   ** {@link Assertion} java content tree.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link InputStream}
   ** <code>path</code> is <code>null</code>.
   **
   ** @param  publicKey          the validating key for the required signature.
   **                            <br>
   **                            Allowed object is {@link PublicKey}.
   ** @param  stream             the location of the content to be parsed.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    a new {@link Assertion} java content tree.
   **                            <br>
   **                            Possible object is {@link Assertion}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>publicKey</code> and/or
   **                              <code>stream</code> is <code>null</code>.
   */
  public static Assertion parse(final PublicKey publicKey, final InputStream stream)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(publicKey, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "publicKey"));
    Objects.requireNonNull(stream, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "stream"));
    return parse(publicKey, new InputSource(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parse the content of the given stream as an XML document and return a new
   ** {@link Assertion} java content tree.
   ** <br>
   ** An <code>NullPointerException</code> is thrown if the {@link InputSource}
   ** <code>path</code> is <code>null</code>.
   **
   ** @param publicKey           the validating key for the required signature.
   **                            <br>
   **                            Allowed object is {@link PublicKey}.
   ** @param source              the location of the content to be parsed.
   **                            <br>
   **                            Allowed object is {@link Source}.
   **
   ** @return                    a new {@link Assertion} java content tree.
   **                            <br>
   **                            Possible object is {@link Assertion}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if <code>publicKey</code> and/or
   **                               <code>source</code> is <code>null</code>.
   */
  public static Assertion parse(final PublicKey publicKey, final InputSource source)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(source, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "source"));
    return parse(publicKey, Builder.INSTANCE.parse(source));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compile
  /**
   ** Compile an {@link XPath} expression for later evaluation.
   ** <p>
   ** If <code>expression</code> contains any {@code XPathFunction}s, they must
   ** be available via the {@code XPathFunctionResolver}.
   ** <br>
   ** An {@link XPathExpressionException} will be thrown if the
   ** <code>XPathFunction</code> cannot be resovled with the
   ** <code>XPathFunctionResolver</code>.
   ** <p>
   ** If <code>expression</code> contains any variables, the
   ** {@code XPathVariableResolver} in effect <strong>at compile time</strong>
   ** will be used to resolve them.
   ** <p>
   ** If <code>expression</code> is <code>null</code>, a
   ** <code>NullPointerException</code> is thrown.
   **
   **  @param  context           the namespace context to use.
   **                            <br>
   **                            Allowed object is {@link NamespaceContext}.
   **  @param  prefix            the namespace prefix expression.
   **                            <br>
   **                            Allowed object is {@link String}.
   **  @param  local             the local representation of the XPath
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
   ** @throws NullPointerException if one or ore arguments passed in are
   **                              <code>null</code>.
   */
  public static XPathExpression compile(final NamespaceContext context, final String prefix, final String local)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(context, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "context"));
    Objects.requireNonNull(prefix, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "prefix"));
    Objects.requireNonNull(local, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "local"));
    final XPath xpath= FACTORY.newXPath();
    xpath.setNamespaceContext(context);
    try {
      return xpath.compile(prefix + ":" + local);
    }
    catch (XPathExpressionException e) {
      throw AssertionException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   node
  /**
   ** Evaluate the compiled XPath expression in the specified context and return
   ** the result as an {@link Element}.
   ** <p>
   ** See <a href="#XPathExpression-evaluation">Evaluation of XPath Expressions</a>
   ** for context item evaluation, variable, function and QName resolution and
   ** return type conversion.
   ** <p>
   ** If a <code>null</code> value is provided for <code>node</code>, an empty
   ** document will be used for the context.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Node}.
   ** @param  expression         the compiled XPath expression.
   **                            <br>
   **                            Allowed object is {@link XPathExpression}.
   **
   ** @return                    the {@link Element} that is the result of
   **                            evaluating the expression.
   **                            <br>
   **                            Possible object is {@link Element}.
   **
   ** @throws AssertionException   if the expression cannot be evaluated.
   ** @throws NullPointerException if <code>node</code> or
   **                              <code>expression</code> is <code>null</code>.
   */
  public static Element node(final Node node, final XPathExpression expression)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node,       AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    Objects.requireNonNull(expression, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "expression"));
    try {
      return (Element)expression.evaluate(node, XPathConstants.NODE);
    }
    catch (XPathExpressionException e) {
      throw AssertionException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Evaluate the compiled XPath expression in the specified context and return
   ** the result as an {@link NodeList}.
   ** <p>
   ** See <a href="#XPathExpression-evaluation">Evaluation of XPath Expressions</a>
   ** for context item evaluation, variable, function and QName resolution and
   ** return type conversion.
   ** <p>
   ** If a <code>null</code> value is provided for <code>node</code>, an empty
   ** document will be used for the context.
   **
   ** @param  node               the starting context (a node, for example).
   **                            <br>
   **                            Allowed object is {@link Node}.
   ** @param  expression         the compiled XPath expression.
   **                            <br>
   **                            Allowed object is {@link XPathExpression}.
   **
   ** @return                    the {@link Element} that is the result of
   **                            evaluating the expression.
   **                            <br>
   **                            Possible object is {@link NodeList}.
   **
   ** @throws AssertionException   if the expression cannot be evaluated.
   ** @throws NullPointerException if <code>node</code> or
   **                              <code>expression</code> is <code>null</code>.
   */
  public static NodeList list(final Node node, final XPathExpression expression)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(node,       AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "node"));
    Objects.requireNonNull(expression, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "expression"));
    try {
      return (NodeList)expression.evaluate(node, XPathConstants.NODESET);
    }
    catch (XPathExpressionException e) {
      throw AssertionException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorHandler
  /**
   ** Factory method to create an {@link ErrorHandler}
   **
   ** @return                    the internal {@link ErrorHandler} instance.
   **                            <br>
   **                            Possible object is {@link ErrorHandler}.
   */
  public static ErrorHandler errorHandler() {
    return new PayloadError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityResolver
  /**
   ** Factory method to create an {@link EntityResolver}
   **
   ** @return                    the internal {@link EntityResolver} instance.
   **                            <br>
   **                            Possible object is {@link EntityResolver}.
   */
  public static EntityResolver entityResolver() {
    return new Resolver();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateTime
  /**
   ** Parsing the lexical string representation is defined in
   ** <a href="http://www.w3.org/TR/xmlschema-2/#dateTime-order">XML Schema 1.0 Part 2, Section 3.2.[7-14].1, <em>Lexical Representation</em>.</a>
   ** <p>
   ** The string representation may not have any leading and trailing
   ** whitespaces.
   **
   ** @param  value              the time before which of the confirmation is
   **                            not valid.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ZonedDateTime</code> parsed from the
   **                            lexical string representation.
   **                            <br>
   **                            Possible object is {@link ZonedDateTime}.
   */
  public static ZonedDateTime dateTime(final String value) {
    return (value == null || value.length() == 0) ? null : ZonedDateTime.parse(value, TIMEFORMAT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify
  /**
   ** Verifies selected or all claims from the specified JWT claims set.
   **
   ** @param  assertion          the assertion to verify.
   **                            <br>
   **                            Allowed object is {@link Assertion}.
   ** @param  claim              the token claim set.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @throws AssertionException if the {@link Assertion} claims rejected.
   */
  public static void verify(final Assertion assertion, final Set<String> claim, final long clockSkew)
    throws AssertionException {

    if (claim != null && claim.size() > 0) {
      // check time window
      final ZonedDateTime now = ZonedDateTime.now();
      if (claim.contains(Assertion.ISSUE_INSTANT)) {
        if (DateUtility.after(assertion.issueInstant(), now, clockSkew))
          throw AssertionException.issuedAfter(assertion.issueInstant());
      }
      if (claim.contains(Conditions.BEFORE)) {
        if (DateUtility.after(assertion.condition().before(), now, clockSkew))
          throw AssertionException.notBefore(assertion.condition().before());
      }      
      if (claim.contains(Conditions.AFTER)) {
        if (DateUtility.before(assertion.condition().after(), now, clockSkew))
          throw AssertionException.notAfter(assertion.condition().after());
      }  
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parse the content of the given URI as an XML document and return a new
   ** {@link Assertion} java content tree after the signature validation
   ** succeeded.
   ** <br>
   ** A <code>NullPointerException</code> is thrown if either
   ** {@link PublicKey publicKey} or {@link Document document} is
   ** <code>null</code>.
   **
   ** @param  publicKey          the validating key for the required signature.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link PublicKey}.
   ** @param  document           the node of the SAML 2.0 assertion element.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Document}.
   **
   ** @return                    a new {@link Assertion} java content tree.
   **                            <br>
   **                            Possible object is {@link Assertion}.
   **
   ** @throws AssertionException   if any parse errors or any IO errors occur.
   ** @throws NullPointerException if either <code>publicKey</code> or
   **                              <code>document</code> is <code>null</code>.
   */
  private static Assertion parse(final PublicKey publicKey, final Document document)
    throws AssertionException {

    // prevent bogus input
    Objects.requireNonNull(publicKey, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "publicKey"));
    Objects.requireNonNull(document,  AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "document"));
    Assertion result = null;
    if (SignatureValidator.validate(publicKey, document))
      result = Parser.assertion(document.getDocumentElement());

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   factoryFeature
  /**
   ** Set a feature for the specified {@link DocumentBuilderFactory}.
   ** <p>
   ** Feature names are fully qualified {@link java.net.URI}s.
   ** <br>
   ** Implementations may define their own features.
   ** <br>
   ** A {@link ParserConfigurationException} is catched if the
   ** {@link DocumentBuilderFactory} cannot support the feature. It is possible
   ** for the {@link DocumentBuilderFactory} to expose a feature value but be
   ** unable to change its state.
   ** <p>
   ** All implementations are required to support the
   ** {@link XMLConstants#FEATURE_SECURE_PROCESSING} feature.
   ** <p>
   ** When the feature is:
   ** <ul>
   **   <li><code>true</code>:  the implementation will limit XML processing to
   **                           conform to implementation limits.
   **                           <br>
   **                           Examples include enity expansion limits and XML
   **                           Schema constructs that would consume large
   **                           amounts of resources.
   **                           <br>
   **                           If XML processing is limited for security
   **                           reasons, it will be reported via a call to the
   **                           registered
   **                           {@link org.xml.sax.ErrorHandler#fatalError(SAXParseException exception)}.
   **                           <br>
   **                           See {@link  DocumentBuilder#setErrorHandler(org.xml.sax.ErrorHandler errorHandler)}.
   **   <li><code>false</code>: the implementation will processing XML according
   **                           to the XML specifications without regard to
   **                           possible implementation limits.
   ** </ul>
   **
   ** @param  name               the name of the feature.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the literal feature state <code>true</code> or
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws NullPointerException if the <code>feature</code> parameter is
   **                               <code>null</code>.
   */
  private static void factoryFeature(final DocumentBuilderFactory factory, final String feature, final boolean state) {
    try {
      factory.setFeature(feature, state);
    }
    catch (ParserConfigurationException e) {
      // normally silently dying but want to know which feature isn't supported
      LOGGER.warn(AssertionBundle.string(AssertionError.PARSER_FEATURE, feature));
    }
  }
}
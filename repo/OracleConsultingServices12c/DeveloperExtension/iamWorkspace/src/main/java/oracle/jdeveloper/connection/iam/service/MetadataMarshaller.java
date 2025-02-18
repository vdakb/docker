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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   MetadataMarshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MetadataMarshaller.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.service;

import java.io.Writer;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import oracle.ide.net.URLFileSystem;

import oracle.jdeveloper.connection.iam.Bundle;

////////////////////////////////////////////////////////////////////////////////
// class MetadataMarshaller
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle metadata artifacts that are
 ** written to or loaded from a file in the local file system the task is
 ** executed on.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
public final class MetadataMarshaller implements ErrorHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean           validationError = false;
  private SAXParseException parseException  = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>MetadataMarshaller</code> to initialize the instance.
   */
  private MetadataMarshaller() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ErrorHandler)
  /**
   ** Receive notification of a non-recoverable error.
   ** <p>
   ** <strong>There is an apparent contradiction between the documentation for
   ** this method and the documentation for {@link org.xml.sax.ContentHandler#endDocument}.
   ** Until this ambiguity is resolved in a future major release, clients should
   ** make no assumptions about whether endDocument() will or will not be
   ** invoked when the parser has reported a fatalError() or thrown an
   ** exception.</strong>
   ** <p>
   ** This corresponds to the definition of "fatal error" in section 1.2 of the
   ** W3C XML 1.0 Recommendation.  For example, a parser would use this callback
   ** to report the violation of a well-formedness constraint.
   ** <p>
   ** The application must assume that the document is unusable after the parser
   ** has invoked this method, and should continue (if at all) only for the sake
   ** of collecting additional error messages: in fact, SAX parsers are free to
   ** stop reporting any other events once this method has been invoked.
   **
   ** @param  exception          the error information encapsulated in a SAX
   **                            parse exception.
   **                            <br>
   **                            Allowed object is {@link SAXParseException}.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    SAXParseException
   */
  public void fatalError(final SAXParseException exception)
    throws SAXException {

    this.validationError = true;
    this.parseException  = exception;
    throw new SAXException(Bundle.string(Bundle.METADATA_DOCUMENT_ERROR), this.parseException);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ErrorHandler)
  /**
   ** Receive notification of a recoverable error.
   ** <p>
   ** This corresponds to the definition of "error" in section 1.2 of the W3C
   ** XML 1.0 Recommendation. For example, a validating parser would use this
   ** callback to report the violation of a validity constraint. The default
   ** behaviour is to take no action.
   ** <p>
   ** The SAX parser must continue to provide normal parsing events after
   ** invoking this method: it should still be possible for the application to
   ** process the document through to the end. If the application cannot do so,
   ** then the parser should report a fatal error even if the XML recommendation
   ** does not require it to do so.
   ** <p>
   ** Filters may use this method to report other, non-XML errors as well.
   **
   ** @param  exception          the error information encapsulated in a SAX
   **                            parse exception.
   **                            <br>
   **                            Allowed object is {@link SAXParseException}.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    SAXParseException
   */
  public void error (final SAXParseException exception)
    throws SAXException {

    this.validationError = true;
    this.parseException  = exception;
    throw new SAXException(Bundle.string(Bundle.METADATA_DOCUMENT_ERROR), this.parseException);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ErrorHandler)
  /**
   ** Receive notification of a warning.
   ** <p>
   ** SAX parsers will use this method to report conditions that are not errors
   ** or fatal errors as defined by the XML recommendation. The default
   ** behaviour is to take no action.
   ** <p>
   ** The SAX parser must continue to provide normal parsing events after
   ** invoking this method: it should still be possible for the application to
   ** process the document through to the end.
   ** <p>
   ** Filters may use this method to report other, non-XML warnings as well.
   **
   ** @param exception           the warning information encapsulated in a SAX
   **                            parse exception.
   **                            <br>
   **                            Allowed object is {@link SAXParseException}.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    SAXParseException
   */
  public void warning(final SAXParseException exception)
    throws SAXException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Export the metadata definition from a Oracle Metadata Store to the file
   ** system represented by {@link URL} <code>target</code>.
   **
   ** @param  target             the {@link URL} as the target of the export
   **                            operation.
   **                            <br>
   **                            Allowed object is {@link URL}.
   ** @param  document           the {@link Document} to export.
   **                            <br>
   **                            Allowed object is {@link Document}.
   **
   ** @throws MetadataException  if an unrecoverable error occurs during the
   **                            course of the transformation.
   */
  public static void write(final URL target, final Document document)
    throws MetadataException {

    // if requested in the task create the path to the file on the fly
    final URL parent = URLFileSystem.getParent(target);
    if (!URLFileSystem.exists(parent))
      URLFileSystem.mkdirs(parent);

    final MetadataMarshaller marshaller = new MetadataMarshaller();

    Writer writer = null;
    // create the DocumentBuilder
    final DocumentBuilder builder = builder();
    try {
      // the marshaller itself implements the ErrroHandler interface for use by
      // the created parser
      // setting this to <code>null</code> will result in the underlying
      // implementation using it's own default implementation and behavior
      // thats whta we don't want
      builder.setErrorHandler(marshaller);

      // creates a transformer with the appropriate properties for indention.
      // the output produced by the transformation that will use the created
      // transformer will be indented or nor regarding to the values of indent
      // and indentNumber
      final Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD,               "xml" );
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT,               "yes");

      writer = new BufferedWriter(new OutputStreamWriter(URLFileSystem.openOutputStream(target)));
      final StreamResult stream = new StreamResult(writer);
      transformer.transform(new DOMSource(document.getDocumentElement()), stream);
      writer.flush();
    }
    catch (IOException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
    }
    catch (TransformerException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_ABORT), e);
    }
    finally {
      if (writer != null)
        try {
          writer.close();
        }
        catch (IOException e) {
          throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Import the metadata definition into a Oracle Metadata Store from the file
   ** system represented by {@link URL} <code>source</code>.
   **
   ** @param  source             the {@link URL} as the target of the export
   **                            operation.
   **                            <br>
   **                            Allowed object is {@link URL}.
   **
   ** @return                    the {@link Document} read from the file system.
   **                            <br>
   **                            Possible object is {@link Document}.
   **
   ** @throws MetadataException  if an unrecoverable error occurs during the
   **                            course of the transformation.
   */
  public static Document read(final URL source)
    throws MetadataException {

    if (!URLFileSystem.exists(source))
      throw new MetadataException(Bundle.format(Bundle.CONTEXT_RESOURCE_NOT_EXISTS, source.toExternalForm()));

    // create the DocumentBuilder instances
    final DocumentBuilder builder = builder();
    try {
      return builder.parse(URLFileSystem.openInputStream(source));
    }
    catch (IOException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_UNHANDLED), e);
    }
    catch (SAXException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_ABORT), e);
    }
  }

  static DocumentBuilder builder()
    throws MetadataException {

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(true);
    // extend the features of the document builder factory with the necessary
    // properties
    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
    try {
      return factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      throw new MetadataException(Bundle.string(Bundle.CONTEXT_ERROR_GENERAL), e);
    }
  }
}
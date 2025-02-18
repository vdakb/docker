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

    File        :   XMLProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLProcessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import javax.xml.XMLConstants;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import javax.xml.transform.Source;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.sax.SAXSource;

import javax.xml.transform.stream.StreamSource;

import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.foundation.resource.XMLStreamBundle;

////////////////////////////////////////////////////////////////////////////////
// class XMLProcessor
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A processor that checks an XML document against {@link Schema}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class XMLProcessor implements ErrorHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String SCHEMA              = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;

  public static final String ATTRIBUTE_XMLNS     = XMLConstants.XMLNS_ATTRIBUTE;
  public static final String ATTRIBUTE_XMLNS_XSI = "xmlns:xsi";
  public static final String ATTRIBUTE_SCHEMA    = "xsi:schemaLocation";

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>XMLProcessor</code> that allows use as a JavaBean.
   */
  public XMLProcessor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ErrorHandler)
  /**
   ** Receive notification of a warning.
   ** <p>
   ** SAX parsers will use this method to report conditions that are not errors
   ** or fatal errors as defined by the XML recommendation.
   ** <p>
   ** The SAX parser must continue to provide normal parsing events after
   ** invoking this method: it should still be possible for the application to
   ** process the document through to the end.
   **
   ** @param  throwable          the warning information encapsulated in a SAX
   **                            parse exception.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    SAXParseException
   */
  @Override
  public void warning(final SAXParseException throwable)
    throws SAXException {

    throw throwable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ErrorHandler)
  /**
   ** Receive notification of a recoverable error.
   ** <p>
   ** This corresponds to the definition of "error" in section 1.2 of the W3C
   ** XML 1.0 Recommendation. For example, a validating parser would use this
   ** callback to report the violation of a validity constraint.
   ** <p>
   ** The SAX parser must continue to provide normal parsing events after
   ** invoking this method: it should still be possible for the application to
   ** process the document through to the end. If the application cannot do so,
   ** then the parser should report a fatal error even if the XML recommendation
   ** does not require it to do so.
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
  public void error(final SAXParseException throwable)
    throws SAXException {

    throw throwable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatalError (ErrorHandler)
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

    throw throwable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  createStreamReader
  /**
   ** Factory method to create a {@link XMLStreamReader} from a {@link Reader}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The <code>XMLStreamReader</code> class allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  reader             the {@link Reader} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static XMLStreamReader createStreamReader(final Reader reader)
    throws XMLException {

    // prevent bogus input
    if (reader == null)
      throw new XMLException(SystemError.ARGUMENT_IS_NULL, "reader");

    try {
      return createInputFactory(true).createXMLStreamReader(reader);
    }
    catch (XMLStreamException e) {
      throw new XMLException(SystemError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStreamReader
  /**
   ** Factory method to create a {@link XMLStreamReader} from an
   ** {@link DOMSource}.
   **
   ** @param  source             the source to parse the underlying XML.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static javax.xml.stream.XMLStreamReader createStreamReader(final DOMSource source)
    throws XMLStreamException {

    // prevent bogus input
    if (source == null)
      throw new XMLStreamException(XMLStreamBundle.format(SystemError.ARGUMENT_IS_NULL, "source"));

    return new DOMStreamReader(source.getNode(), true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStreamReader
  /**
   ** Factory method to create a {@link XMLStreamReader} from an {@link Source}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   **
   ** @param  source             the source to parse the underlying XML.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static XMLStreamReader createStreamReader(final Source source)
    throws XMLException {

    // prevent bogus input
    if (source == null)
      throw new XMLException(SystemError.ARGUMENT_IS_NULL, "source");

    try {
      return createInputFactory(true).createXMLStreamReader(source);
    }
    catch (XMLStreamException e) {
      throw new XMLException(SystemError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  createStreamReader
  /**
   ** Factory method to create a from a {@link InputStream}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The <code>XMLStreamReader</code> class allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  stream             the {@link InputStream} to read from.
   **
   ** @return                    the {@link XMLStreamReader} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static XMLStreamReader createStreamReader(final InputStream stream)
    throws XMLException {

    // prevent bogus input
    if (stream == null)
      throw new XMLException(SystemError.ARGUMENT_IS_NULL, "stream");

    try {
      return createInputFactory(true).createXMLStreamReader(stream);
    }
    catch (XMLStreamException e) {
      throw new XMLException(SystemError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createInputFactory
  /**
   ** Factory method to create a {@link XMLInputFactory} instance which is then
   ** used to obtain an {@link XMLStreamReader} instance.
   **
   ** @param  namespaces         property used to turn on/off namespace support,
   **                            this is to support XML 1.0 documents.
   **
   ** @return                    the {@link XMLInputFactory} configured for our
   **                            purpose.
   */
  public static XMLInputFactory createInputFactory(final boolean namespaces) {
    final XMLInputFactory factory = XMLInputFactory.newFactory();
    factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, namespaces ? Boolean.TRUE : Boolean.FALSE);
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** This is used to create an <code>XMLOutputNode</code> that can be used to
   ** write a well formed XML document.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  format             the format to use for the document.
   **
   **
   ** @return                    the constructed {@link XMLOutputNode} which
   **                            writes to system out leveraging the specified
   **                            <code>format</code>.
   **
   ** @throws XMLException       if there is an I/O error.
   */
  public static XMLOutputNode marshal(final Loggable loggable, final XMLFormat format)
    throws XMLException {

    return marshal(loggable, new PrintWriter(System.out), format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** This is used to create an <code>XMLOutputNode</code> that can be used to
   ** write a well formed XML document. The {@link File} specified will have XML
   ** elements, attributes, and text written to it as output nodes are created
   ** and populated.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  file               the {@link File} of the generated XML.
   ** @param  format             the format to use for the document.
   **
   ** @return                    the constructed {@link XMLOutputNode} which
   **                            writes to the specified <code>file</code>
   **                            leveraging the specified <code>format</code>.
   **
   ** @throws XMLException       if there is an I/O error.
   */
  public static XMLOutputNode marshal(final Loggable loggable, final File file, final XMLFormat format)
    throws XMLException {

    // initialize the output stream
    try {
      return marshal(loggable, new PrintWriter(file), format);
    }
    catch (FileNotFoundException e) {
      throw new XMLException(SystemError.FILE_MISSING, file.getAbsolutePath());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** This is used to create an <code>XMLOutputNode</code> that can be used to
   ** write a well formed XML document. The {@link Writer} specified will have
   ** XML elements, attributes, and text written to it as output nodes are
   ** created and populated.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  writer             the {@link Writer} of the generated XML.
   ** @param  format             the format to use for the document.
   **
   ** @return                    the constructed {@link XMLOutputNode} which
   **                            writes to the specified <code>writer</code>
   **                            leveraging the specified <code>format</code>.
   **
   ** @throws XMLException       if there is an I/O error.
   */
  public static XMLOutputNode marshal(final Loggable loggable, final Writer writer, final XMLFormat format)
    throws XMLException {

    return new XMLOutput(loggable, writer, format).document();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifys an instance document (as represented by the {@link InputStream})
   ** conforms to the schema.
   **
   ** @param  container          the {@link Class} that will receive the data.
   **                            The programming contract is that the schema has
   **                            the same name as the class and is in the same
   **                            directory.
   ** @param  stream             instance document represented by a
   **                            {@link InputStream}.
   **
   ** @throws XMLException       thrown on a validation error with
   **                            {@link SAXException} as the cause.
   */
  public void validate(final Class<?> container, final InputStream stream)
    throws XMLException {

    validate(container, new InputSource(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifys an instance document (as represented by the {@link InputSource})
   ** conforms to the schema.
   **
   ** @param  container          the {@link Class} that will receive the data.
   **                            The programming contract is that the schema has
   **                            the same name as the class and is in the same
   **                            directory.
   ** @param  source             instance document represented by a
   **                            {@link InputSource}.
   **
   ** @throws XMLException       thrown on a validation error with
   **                            {@link SAXException} as the cause.
   */
  public void validate(final Class<?> container, final InputSource source)
    throws XMLException {

    validate(container, new SAXSource(source));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the specified  the {@link Schema} shared across all instancesobject.
   ** <p>
   ** Initializer for singleton pattern. {@link Schema} are threadsafe and
   ** sharable.
   **
   ** @param  container          the {@link Class} that will receive the data.
   **                            The programming contract is that the schema has
   **                            the same name as the class and is in the same
   **                            directory.
   ** @param  source             instance document represented by a
   **                            {@link Source}.
   **
   ** @throws XMLException       thrown on a validation error with
   **                            {@link SAXException} as the cause.
   */
  public void validate(final Class<?> container, final Source source)
    throws XMLException {

    // path we expect to find the schema in the classpath
    final String      path   = ClassUtility.classNameToFile(container.getName(), ClassUtility.XSD);
    final ClassLoader loader = container.getClassLoader();
    final InputStream stream = loader.getResourceAsStream(path);
    if (stream == null)
      throw new XMLException(SystemError.FILE_MISSING, path);

    validate(stream, source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifys an instance document (as represented by the {@link Source})
   ** conforms to the schema.
   ** <p>
   ** A {@link Validator} object is not thread-safe and not reentrant. In other
   ** words, we have to ensure that one Validator object is not used from more
   ** than one thread at any given time, and while the validate method is
   ** invoked, applications may not recursively call the validate method.
   **
   ** @param  stream             the {@link InputStream} representing the
   **                            {@link Schema} to validate the <code>surce</code>
   **                            against.
   ** @param  source             instance document represented by a
   **                            {@link Source}.
   **
   ** @throws XMLException       thrown on a validation error with
   **                            {@link SAXException} as the cause.
   */
  public void validate(final InputStream stream, final Source source)
    throws XMLException {

    try {
      final Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(stream));
      validate(schema, source);
    }
    catch (Exception e) {
      throw new XMLException(XMLError.SCHEMA_FATAL, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Verifys an instance document (as represented by the {@link Source})
   ** conforms to the schema.
   ** <p>
   ** A {@link Validator} object is not thread-safe and not reentrant. In other
   ** words, we have to ensure that one Validator object is not used from more
   ** than one thread at any given time, and while the validate method is
   ** invoked, applications may not recursively call the validate method.
   **
   ** @param  schema             the  {@link Schema} to validate the
   **                            <code>surce</code> against.
   ** @param  source             instance document represented by a
   **                            {@link Source}.
   **
   ** @throws XMLException       thrown on a validation error with
   **                            {@link SAXException} as the cause.
   */
  public synchronized void validate(final Schema schema, final Source source)
    throws XMLException {

    try {
      Validator validator = schema.newValidator();
      validator.setErrorHandler(this);
      validator.validate(source);
    }
    catch (SAXParseException e) {
      final String[] arguments = { String.valueOf(e.getLineNumber()), String.valueOf(e.getColumnNumber()), e.getLocalizedMessage()};
      throw new XMLException(XMLError.SCHEMA_FAILED, arguments);
    }
    catch (Exception e) {
      throw new XMLException(XMLError.SCHEMA_FATAL, e.getLocalizedMessage());
    }
  }
}
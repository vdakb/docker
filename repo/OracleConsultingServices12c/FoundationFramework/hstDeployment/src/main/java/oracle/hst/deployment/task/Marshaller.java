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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Marshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.hst.deployment.task;

import java.io.File;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Marshaller
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Provides basic implementations to handle metadata artifacts that are
 ** written to or loaded from a file in the local file system the task is
 ** executed on.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Marshaller extends ServiceProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String            ENCODING         = "UTF-8";
  public static final boolean           INDENT           = true;
  public static final int               INDENTNUMBER     = 2;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean                       forceOverride    = false;

  /**
   ** A Boolean value indicating whether to stop the import if an exception is
   ** encountered.
   ** <p>
   ** If the repository is a database repository, the incomplete import will
   ** be rolled back.
   */
  private boolean                      cancelOnException = true;

  private String                       encoding          = ENCODING;

  /**
   ** If evaluated to <code>true</code> the output will be intended; otherwise
   ** not.
   */
  private boolean                       indent           = INDENT;

  /**
   ** specifiies how many spaces will be used to indent a child node in the
   ** produced XML stream.
   ** <p>
   ** The amount of indention is ignored as long as the instance attribute
   ** <code>indent</code> ist not <code>true</code>.
   */
  private int                          indentNumber      = INDENTNUMBER;

  private final DocumentBuilderFactory factory;

  protected DocumentBuilder            builder           = null;
  protected Document                   document          = null;
  protected Transformer                transformer       = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>Marshaller</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  indent             <code>true</code> to intend the XML in the
   **                            phase of transformation to a string.
   ** @param  indentNumber       the number of spaces to indent a child node in
   **                            the transformed string.
   */
  protected Marshaller(final ServiceFrontend frontend, final boolean indent, final int indentNumber) {
    // ensure inheritance
    super(frontend);

    // initialize the instance attributes
    this.indent       = indent;
    this.indentNumber = indentNumber;

    this.factory      = DocumentBuilderFactory.newInstance();
    this.factory.setValidating(false);
    this.factory.setNamespaceAware(true);
    // extend the features of the document builder factory with the necessary
    // properties
    this.factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Called by the ANT deployment to inject the argument for parameter
   ** <code>forceOverride</code>.
   **
   ** @param  forceOverride      <code>true</code> to override the existing file
   **                            without to aks for user confirmation.
   */
  public void forceOverride(final boolean forceOverride) {
    this.forceOverride = forceOverride;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   forceOverride
  /**
   ** Returns the how to handle existing files
   ** to.
   **
   ** @return                    <code>true</code> if the existing file will be
   **                            overridden without any further confirmation.
   */
  public final boolean forceOverride() {
    return this.forceOverride;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancelOnException
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** cancelOnException.
   **
   ** @param  cancelOnException  a Boolean value indicating whether to stop the
   **                            delete if an exception is encountered.
   **                            <p>
   **                            If the target repository is a database
   **                            repository, the incomplete delete  will be
   **                            rolled back.
   */
  public void cancelOnException(final boolean cancelOnException) {
    this.cancelOnException = cancelOnException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancelOnException
  /**
   ** Returns the value indicating whether to stop the delete if an exception is
   ** encountered.
   **
   ** @return                    the value indicating whether to stop the delete
   **                            if an exception is encountered.
   */
  public final boolean cancelOnException() {
    return this.cancelOnException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoding
  /**
   ** Called to inject the argument for attribute <code>encoding</code>.
   **
   ** @param  encoding         the encoding used to read or write the file.
   */
  public final void encoding(final String encoding) {
    this.encoding = encoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoding
  /**
   ** Returns the encoding used to read or write the file.
   **
   ** @return                    the encoding used to read or write the file.
   */
  public final String encoding() {
    return this.encoding;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indent
  /**
   ** Called to inject the argument for instance attribute <code>indent</code>.
   ** <p>
   ** If this will be evaluated to <code>true</code> the output will be
   ** intended; otherwise not.
   **
   ** @param  indent             <code>true</code> to intend the output.
   */
  public void indent(final boolean indent) {
    this.indent = indent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indentNumber
  /**
   ** Called to inject the argument for instance attribute
   ** <code>indentNumber</code>.
   ** <p>
   ** The <code>indentNumber</code> specifiies how many spaces will be used to
   ** indent a child node in the produced XML-file.
   ** <p>
   ** The amount of indention is ignored as long as the instance attribute
   ** <code>indent</code> ist not <code>true</code>.
   **
   ** @param  indentNumber       the number of spaces to indent a child node in
   **                            the created file.
   */
  public void indentNumber(final int indentNumber) {
    this.indentNumber = indentNumber;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   document
  /**
   ** Sets the document used to read from or write to a file.
   **
   ** @param  document           the document used to read from or write to a
   **                            file.
   */
  protected final void document(final Document document) {
    this.document = document;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   document
  /**
   ** Returns the document used to read from or write to a file.
   **
   ** @return                    the document used to read from or write to a
   **                            file.
   */
  public final Document document() {
    return this.document;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBuilder
  /**
   ** Creates the {@link DocumentBuilder}.
   **
   ** @param  errorHandler       the {@link ErrorHandler} to be used by the
   **                            created parser.
   **                            <p>
   **                            Setting this to <code>null</code> will result
   **                            in the underlying implementation using it's
   **                            own default implementation and behavior.
   **
   ** @throws ServiceException   if the {@link DocumentBuilder} could not be
   **                            created.
   */
  protected void createBuilder(final ErrorHandler errorHandler)
    throws ServiceException {

    try {
      this.builder = this.factory.newDocumentBuilder();
      if (errorHandler != null)
        this.builder.setErrorHandler(errorHandler);
    }
    catch (Exception e) {
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTransformer
  /**
   ** Creates a {@link Transformer} with the appropriate properties for
   ** indention.
   ** <p>
   ** The output produced by the transformation that will use the created
   ** transformer will be indented or nor regarding to the values of
   ** <code>indent</code> and <code>indentNumber</code>.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final void createTransformer()
    throws ServiceException {

    try {
      TransformerFactory factory = TransformerFactory.newInstance();
      if (this.indent)
        factory.setAttribute("indent-number", new Integer(this.indentNumber));

      this.transformer = factory.newTransformer();
      this.transformer.setOutputProperty(OutputKeys.INDENT, this.indent ? SystemConstant.YES : SystemConstant.NO);
    }
    catch (TransformerException e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeDocument
  /**
   ** Writes the managed XML to the specified file
   **
   ** @param  exportFile         the {@link File} the document will be written
   **                            to.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void writeDocument(final File exportFile)
    throws ServiceException {

    Writer writer = null;
    try {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile), this.encoding));
      transformDocument(writer);
      writer.flush();
    }
    catch (Exception e) {
      final String[] arguments = { exportFile.getName(), e.getLocalizedMessage() };
      error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_DOWNLOAD, arguments));
      if (this.failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    finally {
      if (writer != null)
        try {
          writer.close();
        }
        catch (Exception e) {
          error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
          if (failonerror())
            throw new ServiceException(ServiceError.UNHANDLED, e);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformDocument
  /**
   ** Transforms the managed XML to a string
   **
   ** @return                    the transformated DOM represented as a string.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected String transformDocument()
    throws ServiceException {

    final CharArrayWriter writer = new CharArrayWriter();
    transformDocument(writer);
    return new String(writer.toCharArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformDocument
  /**
   ** Transforms the managed XML to a string
   **
   ** @param  writer             the {@link Writer} the document content will be
   **                            written to.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void transformDocument(final Writer writer)
    throws ServiceException {

    final StreamResult stream = new StreamResult(writer);
    try {
      this.transformer.transform(new DOMSource(this.document.getDocumentElement()), stream);
    }
    catch (Exception e) {
      error(ServiceResourceBundle.format(ServiceError.METADATA_DOCUMENT_TRANSFORMATION, e.getLocalizedMessage()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDocument
  /**
   ** Read the XML fragment and creates the DOM tree.
   **
   ** @param  fragment           the XML fragment to parse.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final void createDocument(final String fragment)
    throws ServiceException {

    InputStreamReader reader = null;
    try {
      // build a parsable object to create a XML document
      reader = new InputStreamReader(new ByteArrayInputStream(fragment.getBytes(this.encoding)));

      // parse the stream
      createDocument(reader);
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
    finally {
      if (reader != null)
        try {
          reader.close();
        }
        catch (Exception e) {
          if (failonerror())
            throw new ServiceException(ServiceError.UNHANDLED, e);
          else
            error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDocument
  /**
   ** Read the XML file and creates the DOM tree.
   **
   ** @param  reader             the {@link InputStreamReader} to fetch the
   **                            content form the file.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected final void createDocument(final InputStreamReader reader)
    throws ServiceException {

    try {
      InputSource source   = new InputSource(reader);
      // parse the stream
      this.document = builder.parse(source);
    }
    catch (Exception e) {
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
      else
        error(ServiceResourceBundle.format(ServiceError.UNHANDLED, e.getLocalizedMessage()));
    }
  }
}
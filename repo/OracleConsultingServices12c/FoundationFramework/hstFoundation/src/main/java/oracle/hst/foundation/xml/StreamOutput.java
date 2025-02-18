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

    File        :   StreamOutput.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StreamOutput.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.Writer;
import java.io.OutputStream;

import javax.xml.namespace.NamespaceContext;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;

import oracle.hst.foundation.logging.Loggable;

////////////////////////////////////////////////////////////////////////////////
// class StreamOutput
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The {@link StreamOutput} interface specifies how to write XML.
 ** <p>
 ** The {@link StreamOutput} does not perform well formedness checking on its
 ** input. However the writeCharacters method is required to escape &amp;, &lt;
 ** and &gt;
 ** <p>
 ** For attribute values the writeAttribute method will escape the above
 ** characters plus &quot; to ensure that all character content and attribute
 ** values are well formed.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
class StreamOutput extends    XMLOperation
                   implements XMLStreamWriter
                   ,          XMLStreamConstants {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final XMLStreamWriter writer;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>StreamOutput</code> from a {@link Writer}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  writer             the {@link Writer} used to write.
   **
   ** @throws XMLStreamException in case {@link XMLOutputFactory} is not able to
   **                            create an appropriate {@link XMLStreamWriter}.
   */
  public StreamOutput(final Loggable loggable, final Writer writer)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, streamWriter(writer));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>StreamOutput</code> from an {@link OutputStream}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  stream             the {@link OutputStream} used to write.
   **
   ** @throws XMLStreamException in case {@link XMLOutputFactory} is not able to
   **                            create an appropriate {@link XMLStreamWriter}.
   */
  public  StreamOutput(final Loggable loggable, final OutputStream stream)
    throws XMLStreamException {

    // ensure inheritance
    this(loggable, streamWriter(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>StreamOutput</code> with a specified
   ** {@link XMLStreamWriter}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  writer           the {@link XMLStreamWriter} used to read.
   */
  private StreamOutput(final Loggable loggable, final XMLStreamWriter writer) {
    // ensure inheritance
    super(loggable);

    // initialize instance
    this.writer = writer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getProperty (XMLStreamWriter)
  /**
   ** Returns the value of a feature/property from the underlying
   ** implementation.
   **
   ** @param  name               the name of the property, may not be
   **                            <code>null</code>.
   **
   ** @return                    the value of the property.
   **
   ** @throws NullPointerException     if name is <code>null</code>.
   ** @throws IllegalArgumentException if the property is not supported
   */
  @Override
  public Object getProperty(final String name) {
    return this.writer.getProperty(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (XMLStreamWriter)
  /**
   ** Close this writer and free any resources associated with the writer.
   ** <p>
   ** This must not close the underlying output stream.
   **
   ** @throws XMLStreamException if there are errors freeing associated
   **                            resources.
   */
  @Override
  public void close()
    throws XMLStreamException {

    this.writer.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNamespaceContext (XMLStreamWriter)
  /**
   ** Binds a URI to the default namespace
   ** <p>
   ** This URI is bound in the scope of the current {@link #START_ELEMENT} /
   ** {@link #END_ELEMENT} pair.
   ** <p>
   ** If this method is called before a {@link #START_ELEMENT} has been written
   ** the UIR is bound in the root scope.
   **
   ** @param  uri                the uri to bind to the default namespace, may
   **                            be <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors freeing associated
   **                            resources.
   */
  @Override
  public void setDefaultNamespace(final String uri)
    throws XMLStreamException {

    this.writer.setDefaultNamespace(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNamespaceContext (XMLStreamWriter)
  /**
   ** Sets the current namespace context for prefix and URI bindings.
   ** <p>
   ** This context becomes the root namespace context for writing and will
   ** replace the current root namespace context. Subsequent calls to setPrefix
   ** and setDefaultNamespace will bind namespaces using the context passed to
   ** the method as the root context for resolving namespaces. This method may
   ** only be called once at the start of the document. It does not cause the
   ** namespaces to be declared. If a namespace URI to prefix mapping is found
   ** in the namespace  context it is treated as declared and the prefix may be
   ** used by the StreamWriter.
   **
   ** @param  context            the namespace context to use for this writer,
   **                            may not be <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors freeing associated
   **                            resources.
   */
  @Override
  public void setNamespaceContext(final NamespaceContext context)
    throws XMLStreamException {

    this.writer.setNamespaceContext(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespaceContext (XMLStreamWriter)
  /**
   ** Returns a namespace context for the current position.
   **
   ** @return                    a namespace context
   */
  @Override
  public NamespaceContext getNamespaceContext() {
    return this.writer.getNamespaceContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPrefix (XMLStreamWriter)
  /**
   ** Sets the prefix the uri is bound to.
   ** <p>
   ** This prefix is bound in the scope of the current {@link #START_ELEMENT} /
   ** {@link #END_ELEMENT} pair. If this method is called before a
   ** {@link #START_ELEMENT} has been written the prefix is bound in the root
   ** scope.
   **
   ** @param  prefix             the prefix to bind to the uri, may not be
   **                            <code>null</code>.
   ** @param  uri                the uri to bind to the prefix, may be
   **                            <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void setPrefix(final String prefix, final String uri)
    throws XMLStreamException {

  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrefix (XMLStreamWriter)
  /**
   ** Returns the prefix the URI is bound to.
   **
   ** @return                    the prefix or <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public String getPrefix(final String uri)
    throws XMLStreamException {

    return this.writer.getPrefix(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flush (XMLStreamWriter)
  /**
   ** Write any cached data to the underlying output mechanism.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void flush()
    throws XMLStreamException {

    this.writer.flush();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeDTD (XMLStreamWriter)
  /**
   ** Write a DTD section.
   ** <p>
   ** This string represents the entire doctypedecl production from the XML 1.0
   ** specification.
   **
   ** @param  dtd                the DTD to be written.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeDTD(final String dtd)
    throws XMLStreamException {

    this.writer.writeDTD(dtd);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeProcessingInstruction (XMLStreamWriter)
  /**
   ** Writes a processing instruction.
   **
   ** @param  target             the target of the processing instruction, may
   **                            not be <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeProcessingInstruction(final String target)
    throws XMLStreamException {

    this.writer.writeProcessingInstruction(target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeProcessingInstruction (XMLStreamWriter)
  /**
   ** Writes a processing instruction.
   **
   ** @param  target             the target of the processing instruction, may
   **                            not be <code>null</code>.
   ** @param  data               the data contained in the processing
   **                            instruction, may not be <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeProcessingInstruction(final String target, final String data)
    throws XMLStreamException {

    this.writer.writeProcessingInstruction(target, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeNamespace (XMLStreamWriter)
  /**
   ** Writes a namespace to the output stream.
   ** <p>
   ** If the prefix argument to this method is the empty string, "xmlns", or
   ** <code>null</code> this method will delegate to writeDefaultNamespace
   **
   ** @param  prefix             the prefix to bind this namespace to.
   ** @param  namespaceURI       the uri to bind the prefix to.
   **
   ** @throws IllegalStateException if the current state does not allow
   **                               Namespace writing.
   ** @throws XMLStreamException    if there are errors writing to the
   **                               underlying output mechanism.
   */
  @Override
  public void writeNamespace(final String prefix, final String namespaceURI)
    throws XMLStreamException {

    this.writer.writeNamespace(prefix, namespaceURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeDefaultNamespace (XMLStreamWriter)
  /**
   ** Writes the default namespace to the stream.
   **
   ** @param  namespaceURI       the uri to bind the prefix to.
   **
   ** @throws IllegalStateException if the current state does not allow
   **                               Namespace writing.
   ** @throws XMLStreamException    if there are errors writing to the
   **                               underlying output mechanism.
   */
  @Override
  public void writeDefaultNamespace(final String namespaceURI)
    throws XMLStreamException {

    this.writer.writeDefaultNamespace(namespaceURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartDocument (XMLStreamWriter)
  /**
   ** Write the XML Declaration.
   ** <p>
   ** Defaults the XML version to 1.0, and the encoding to utf-8.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeStartDocument()
    throws XMLStreamException {

    this.writer.writeStartDocument();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartDocument (XMLStreamWriter)
  /**
   ** Write the XML Declaration. Defaults the XML version to 1.0.
   **
   ** @param  version             the version of the xml document.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeStartDocument(final String version)
    throws XMLStreamException {

    this.writer.writeStartDocument(version);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartDocument (XMLStreamWriter)
  /**
   ** Write the XML Declaration.
   ** <p>
   ** Note that the encoding parameter does not set the actual encoding of the
   ** underlying output. That must be set when the instance of the
   ** {@link XMLStreamWriter} is created using the
   ** <code>XMLOutputFactory</code>.
   **
   ** @param  encoding            encoding of the xml declaration
   ** @param  version             the version of the xml document.
   **
   ** @throws XMLStreamException  if given encoding does not match encoding of
   **                             the underlying stream.
   */
  @Override
  public void writeStartDocument(final String encoding, final String version)
    throws XMLStreamException {

    this.writer.writeStartDocument(encoding, version);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEndDocument (XMLStreamWriter)
  /**
   ** Closes any start tags and writes corresponding end tags.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeEndDocument()
    throws XMLStreamException {

    this.writer.writeEndDocument();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeComment (XMLStreamWriter)
  /**
   ** Writes an xml comment with the data enclosed.
   **
   ** @param  data               the data contained in the comment, may be
   **                            <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeComment(final String data)
    throws XMLStreamException {

    this.writer.writeComment(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartElement (XMLStreamWriter)
  /**
   ** Writes a start tag to the output.
   ** <p>
   ** All writeStartElement methods open a new scope in the internal namespace
   ** context. Writing the corresponding EndElement causes the scope to be
   ** closed.
   **
   ** @param  localName          local name of the tag, may not be
   **                            <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeStartElement(final String localName)
    throws XMLStreamException {

    this.writer.writeStartElement(localName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartElement (XMLStreamWriter)
  /**
   ** Writes a start tag to the output.
   ** <p>
   ** All writeStartElement methods open a new scope in the internal namespace
   ** context. Writing the corresponding EndElement causes the scope to be
   ** closed.
   **
   ** @param  namespaceURI       the namespaceURI of the prefix to use, may not
   **                            be <code>null</code>.
   ** @param  localName          local name of the tag, may not be
   **                            <code>null</code>.
   **
   ** @throws XMLStreamException if the namespace URI has not been bound to a
   **                            prefix and
   **                            javax.xml.stream.isRepairingNamespaces has not
   **                            been set to <code>true</code>.
   */
  public void writeStartElement(final String namespaceURI, final String localName)
    throws XMLStreamException {

    this.writer.writeStartElement(namespaceURI, localName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartElement (XMLStreamWriter)
  /**
   ** Writes a start tag to the output.
   ** <p>
   ** All writeStartElement methods open a new scope in the internal namespace
   ** context. Writing the corresponding EndElement causes the scope to be
   ** closed.
   **
   ** @param  localName          local name of the tag, may not be
   **                            <code>null</code>.
   ** @param  prefix             the prefix of the tag, may not be
   **                            <code>null</code>.
   ** @param  namespaceURI       the namespaceURI of the prefix to use, may not
   **                            be <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeStartElement(String prefix, String localName, String namespaceURI)
    throws XMLStreamException {

    this.writer.writeStartElement(prefix, localName, namespaceURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEndElement (XMLStreamWriter)
  /**
   ** Writes an end tag to the output relying on the internal  state of the
   ** writer to determine the prefix and local name of the event.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeEndElement()
    throws XMLStreamException {

    this.writer.writeEndElement();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEmptyElement (XMLStreamWriter)
  /**
   ** Writes an empty element tag to the output.
   **
   ** @param  localName          local name of the tag, may not be
   **                            <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeEmptyElement(final String localName)
    throws XMLStreamException {

    this.writer.writeEmptyElement(localName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEmptyElement (XMLStreamWriter)
  /**
   ** Writes an empty element tag to the output.
   **
   ** @param  namespaceURI       the uri to bind the tag to, may not be
   **                            <code>null</code>.
   ** @param  localName          local name of the tag, may not be
   **                            <code>null</code>.
   **
   ** @throws XMLStreamException if the namespace URI has not been bound to a
   **                            prefix and
   **                            javax.xml.stream.isRepairingNamespaces has not
   **                            been set to <code>true</code>.
   */
  @Override
  public void writeEmptyElement(final String namespaceURI, final String localName)
    throws XMLStreamException {

    this.writer.writeEmptyElement(namespaceURI, localName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEmptyElement (XMLStreamWriter)
  /**
   * Writes an empty element tag to the output
   * @param prefix the prefix of the tag, may not be <code>null</code>
   ** @param  localName          local name of the tag, may not be
   **                            <code>null</code>.
   ** @param  namespaceURI       the uri to bind the tag to, may not be
   **                            <code>null</code>.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeEmptyElement(final String prefix, final String localName, final String namespaceURI)
    throws XMLStreamException {

    this.writer.writeEmptyElement(prefix, localName, namespaceURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeAttribute (XMLStreamWriter)
  /**
   ** Writes an attribute to the output stream without a prefix.
   **
   ** @param  localName          the local name of the attribute.
   ** @param  value              the value of the attribute.
   **
   ** @throws IllegalStateException if the current state does not allow
   **                               Attribute writing.
   ** @throws XMLStreamException    if there are errors writing to the
   **                               underlying output mechanism.
   */
  @Override
  public void writeAttribute(final String localName, final String value)
    throws XMLStreamException {

    this.writer.writeAttribute(localName, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeAttribute (XMLStreamWriter)
  /**
   ** Writes an attribute to the output stream.
   **
   ** @param  namespaceURI       the uri of the prefix for this attribute.
   ** @param  localName          the local name of the attribute.
   ** @param  value              the value of the attribute.
   **
   ** @throws IllegalStateException if the current state does not allow
   **                               Attribute writing.
   ** @throws XMLStreamException    if the namespace URI has not been bound to a
   **                               prefix and
   **                               javax.xml.stream.isRepairingNamespaces has
   **                               not been set to <code>true</code>.
   */
  @Override
  public void writeAttribute(final String namespaceURI, final String localName, final String value)
    throws XMLStreamException {

    this.writer.writeAttribute(namespaceURI, localName, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeAttribute (XMLStreamWriter)
  /**
   ** Writes an attribute to the output stream.
   **
   ** @param  prefix             the prefix for this attribute
   ** @param  namespaceURI       the uri of the prefix for this attribute.
   ** @param  localName          the local name of the attribute.
   ** @param  value              the value of the attribute.
   **
   ** @throws IllegalStateException if the current state does not allow
   **                               Attribute writing.
   ** @throws XMLStreamException    if the namespace URI has not been bound to a
   **                               prefix and
   **                               javax.xml.stream.isRepairingNamespaces has
   **                               not been set to <code>true</code>.
   */
  @Override
  public void writeAttribute(final String prefix, final String namespaceURI, final String localName, final String value)
    throws XMLStreamException {

    this.writer.writeAttribute(prefix, namespaceURI, localName, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeCharacters (XMLStreamWriter)
  /**
   ** Write text to the output.
   **
   ** @param  text               the value to write.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeCharacters(final String text)
    throws XMLStreamException {

    this.writer.writeCharacters(text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeCharacters (XMLStreamWriter)
  /**
   ** Write text to the output.
   **
   ** @param  text               the value to write.
   ** @param  start              the starting position in the array.
   ** @param  len                the number of characters to write.
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeCharacters(final char[] text, final int start, final int len)
    throws XMLStreamException {

    this.writer.writeCharacters(text, start, len);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeCData (XMLStreamWriter)
  /**
   * Writes a CData section
   * @param data the data contained in the CData Section, may not be <code>null</code>
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeCData(final String data)
    throws XMLStreamException {

    this.writer.writeCData(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEntityRef (XMLStreamWriter)
  /**
   ** Writes an entity reference.
   **
   ** @param  name               the name of the entity
   **
   ** @throws XMLStreamException if there are errors writing to the underlying
   **                            output mechanism.
   */
  @Override
  public void writeEntityRef(final String name)
    throws XMLStreamException {

    this.writer.writeEntityRef(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamWriter
  /**
   ** Create a new {@link XMLStreamWriter} that meets our requirements.
   **
   ** @param  writer             the {@link Writer} to write to.
   **
   ** @return                    the {@link XMLStreamWriter} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLOutputFactory} is not able to
   **                            create an appropriate {@link XMLStreamWriter}.
   */
  protected static XMLStreamWriter streamWriter(final Writer writer)
    throws XMLStreamException {

    return streamWriter(createOutputFactory(), writer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamWriter
  /**
   ** Create a new {@link XMLStreamWriter} that meets our requirements.
   **
   ** @param  factory            the {@link XMLOutputFactory} configured to
   **                            create an appropriate {@link XMLStreamWriter}.
   ** @param  writer             the {@link Writer} to write to.
   **
   ** @return                    the {@link XMLStreamWriter} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLOutputFactory} is not able to
   **                            create an appropriate {@link XMLStreamWriter}.
   */
  protected static XMLStreamWriter streamWriter(final XMLOutputFactory factory, final Writer writer)
    throws XMLStreamException {

    return factory.createXMLStreamWriter(writer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamWriter
  /**
   ** Create a new {@link XMLStreamWriter} that meets our requirements.
   **
   ** @param  stream             the {@link OutputStream} to write to.
   **
   ** @return                    the {@link XMLStreamWriter} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLOutputFactory} is not able to
   **                            create an appropriate {@link XMLStreamWriter}.
   */
  protected static XMLStreamWriter streamWriter(final OutputStream stream)
    throws XMLStreamException {

    return streamWriter(createOutputFactory(), stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   streamWriter
  /**
   ** Create a new {@link XMLStreamWriter} that meets our requirements.
   **
   ** @param  factory            the {@link XMLOutputFactory} configured to
   **                            create an appropriate {@link XMLStreamWriter}.
   ** @param  stream             the {@link OutputStream} to write to.
   **
   ** @return                    the {@link XMLStreamWriter} configured by the
   **                            factory for our purpose.
   **
   ** @throws XMLStreamException in case {@link XMLOutputFactory} is not able to
   **                            create an appropriate {@link XMLStreamWriter}.
   */
  protected static XMLStreamWriter streamWriter(final XMLOutputFactory factory, final OutputStream stream)
    throws XMLStreamException {

    return factory.createXMLStreamWriter(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOutputFactory
  /**
   ** Factory method to create a {@link XMLOutputFactory} instance which is then
   ** used to obtain an {@link XMLStreamWriter} instance.
   ** <p>
   ** This method uses the following ordered lookup procedure to determine the
   ** {@link XMLOutputFactory} implementation class to load:
   ** <ol>
   **   <li>Use the <code>javax.xml.stream.XMLOutputFactory</code> system
   **       property.
   **   <li>Use the properties file "<code>lib/stax.properties</code>" in the
   **       JRE directory.
   **       <br>
   **       This configuration file is in standard
   **       <code>java.util.Properties</code> format and contains the fully
   **       qualified name of the implementation class with the key being the
   **       system property defined above.
   **   <li>Use the Services API (as detailed in the JAR specification), if
   **       available, to determine the classname. The Services API will look
   **       for a classname in the file
   **       <code>META-INF/services/javax.xml.stream.XMLOutputFactory</code> in
   **       jars available to the runtime.
   **   <li>Platform default XMLOutputFactory instance.
   ** </ol>
   ** Once an application has obtained a reference to a {@link XMLOutputFactory}
   ** it can use the factory to configure and obtain stream instances.
   **
   ** @return                    the {@link XMLOutputFactory} configured for our
   **                            purpose.
   */
  protected static XMLOutputFactory createOutputFactory() {
    return XMLOutputFactory.newFactory();
  }
}
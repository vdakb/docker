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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Integration

    File        :   DescriptorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DescriptorFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MDSIOException;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class DescriptorFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of provisioning and
 ** reconciliation descriptors.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public final class DescriptorFactory extends XMLProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DescriptorParser parser;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DescriptorFactory</code> to configure the specified
   ** {@link Descriptor}.
   **
   ** @param  descriptor         the {@link Descriptor} that this factory will
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private DescriptorFactory(final Descriptor descriptor)
    throws SAXException {

    // ensure inheritance
    super();

    // initialize instance
    this.parser = new DescriptorParser(descriptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from a {@link File}.
   **
   ** @param  descriptor         the {@link Descriptor} that this factory will
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the {@link Descriptor} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link Descriptor}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Descriptor configure(final Descriptor descriptor, final File file)
    throws TaskException {

    return configure(descriptor, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from a
   ** {@link File}.
   **
   ** @param  descriptor         the {@link Descriptor} that this factory will
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link Descriptor} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link Descriptor}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Descriptor configure(final Descriptor descriptor, final File file, final boolean validate)
    throws TaskException {

    try {
      return configure(descriptor, new FileInputStream(file), validate);
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputStream}.
   **
   ** @param  descriptor         the {@link Descriptor} that this factory will
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the {@link Descriptor} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link Descriptor}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Descriptor configure(final Descriptor descriptor, final InputStream stream)
    throws TaskException {

    return configure(descriptor, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to create a {@link Descriptor} from an
   ** {@link InputStream}.
   **
   ** @param  descriptor         the {@link Descriptor} that this factory will
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link Descriptor} to allow method
   **                            chaining.
   **                            Possible object is {@link Descriptor}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Descriptor configure(final Descriptor descriptor, final InputStream stream, final boolean validate)
    throws TaskException {

    configure(descriptor, new InputSource(stream), validate);
    try {
      stream.close();
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
    return descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputSource}.
   **
   ** @param  descriptor         the {@link Descriptor} that this factory will
   **                            configure.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    the {@link Descriptor} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link Descriptor}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Descriptor configure(final Descriptor descriptor, final InputSource source)
    throws TaskException {

    return configure(descriptor, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputSource}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured
   **                            by this {@link DescriptorFactory}.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link Descriptor} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link Descriptor}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Descriptor configure(final Descriptor descriptor, final InputSource source, final boolean validate)
    throws TaskException {

    try {
      final DescriptorFactory factory = new DescriptorFactory(descriptor);
      factory.unmarshal(source, validate);
      return descriptor;
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputStream}.
   **
   ** @param  descriptor         the {@link Descriptor} to be configured
   **                            by this {@link DescriptorFactory}.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **                            <br>
   **                            Allowed object is {@link PDocument}.
   **
   ** @return                    the {@link Descriptor} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link Descriptor}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Descriptor configure(final Descriptor descriptor, final PDocument document)
    throws TaskException {

    try {
      final DescriptorFactory factory = new DescriptorFactory(descriptor);
      factory.unmarshal(document);
      return descriptor;
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from a {@link File}.
   **
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void unmarshal(final File file)
    throws TaskException {

    unmarshal(file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from a {@link File}.
   **
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void unmarshal(final File file, final boolean validate)
    throws TaskException {

    try {
      unmarshal(new FileInputStream(file), validate);
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from an {@link InputStream}.
   **
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void unmarshal(final InputStream stream)
    throws TaskException {

    unmarshal(stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from an {@link InputStream}.
   **
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void unmarshal(final InputStream stream, final boolean validate)
    throws TaskException {

    // prevent bogus input
    if (stream == null)
      throw new TaskException(TaskError.ARGUMENT_IS_NULL, "stream");

    unmarshal(new InputSource(stream), validate);
    try {
      stream.close();
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from an {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void unmarshal(final InputSource source, final boolean validate)
    throws TaskException {

    // validate the provided stream against the schema
    if (validate) {
      try {
        validate(Descriptor.class, source);
      }
      catch (XMLException e) {
        throw new TaskException(e);
      }
    }
    unmarshal(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link PDocument}.
   **
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **                            <br>
   **                            Allowed object is {@link PDocument}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void unmarshal(final PDocument document)
    throws TaskException {

    // prevent bogus input
    if (document == null)
      throw new TaskException(TaskError.ARGUMENT_IS_NULL, "document");

    try {
      unmarshal(document.read());
    }
    catch (MDSIOException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from an {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private void unmarshal(final InputSource source)
    throws TaskException {

    // prevent bogus input
    if (source == null)
      throw new TaskException(TaskError.ARGUMENT_IS_NULL, "source");

    try {
      this.parser.processDocument(source);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }
}
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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   RegistrationHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RegistrationHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.util.Set;
import java.util.HashSet;

import java.io.File;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;

import oracle.hst.deployment.spi.AbstractServletHandler;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.access.common.FeatureError;
import oracle.iam.access.common.FeatureException;

import oracle.iam.access.common.FeatureResourceBundle;
import oracle.iam.access.common.spi.schema.ObjectFactory;

////////////////////////////////////////////////////////////////////////////////
// class RegistrationHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes services in Oracle Access Manager domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RegistrationHandler extends AbstractServletHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String         SCHEMA_RESOURCE = "META-INF/registration.xsd";
  static final String         SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String> flatten         = new HashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class ValidationHandler
  // ~~~~~ ~~~~~~~~~~~~~~~~~~
  /**
   ** The basic event handler implementation for schema validation errors.
   */
  private static class ValidationHandler implements ValidationEventHandler {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ValidationHandler</code>
     */
    private ValidationHandler() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handleEvent (ValidationEventHandler)
    /**
     ** Receive notification of a validation warning or error.
     ** <bR>
     ** The {@link ValidationEvent} will have a {@link ValidationEventLocator}
     ** embedded in it that indicates where the error or warning occurred.
     ** <p>
     ** If an unchecked runtime exception is thrown from this method, the JAXB
     ** provider will treat it as if the method returned false and interrupt the
     ** current unmarshal, validate, or marshal operation.
     **
     ** @param  event            the encapsulated validation event information.
     **                          It is a provider error if this parameter is
     **                          <code>null</code>.
     **
     ** @return                  <code>true</code> if the JAXB Provider should
     **                          attempt to continue the current unmarshal,
     **                          validate, or marshal operation after handling
     **                          this warning/error, <code>false</code> if the
     **                          provider should terminate the current
     **                          operation with the appropriate
     **                          <code>UnmarshalException</code>,
     **                          <code>ValidationException</code>, or
     **                          <code>MarshalException</code>.
     **
     ** @throws IllegalArgumentException if the event object is
     **                                  <code>null</code>.
     */
    @Override
    public boolean handleEvent(final ValidationEvent event) {
      final int severity = event.getSeverity();
      if (severity == ValidationEvent.FATAL_ERROR || severity == ValidationEvent.ERROR) {
        final ValidationEventLocator locator = event.getLocator();
        final String[] parameter = {String.valueOf(locator.getLineNumber()), String.valueOf(locator.getColumnNumber()), event.getMessage()};
        FeatureResourceBundle.format(FeatureError.REMOTE_BINDING_VIOLATION, parameter);
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RegistrationHandler</code> Ant task which will gain
   ** access to a certain JMX bean.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  public RegistrationHandler(final ServiceFrontend frontend, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend);

    // initiallize instance attributes
    this.operation(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseToString
  /**
   ** Marshal the content tree rooted at <code>element</code> into an output
   ** stream.
   **
   ** @param  element            the root of content tree to be marshalled.
   **
   ** @return                    the ...
   **
   ** @throws FeatureException   if the {@link JAXBContext} could not be created
   **                            or the outout file isn't accessible.
   */
  public static String parseToString(final Object element)
    throws FeatureException {

    final ByteArrayOutputStream stream = new ByteArrayOutputStream();
    marshal(stream, element);
    try {
      return stream.toString(StringUtility.UNICODE.name());
    }
    catch (UnsupportedEncodingException e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_ENCODING, StringUtility.UNICODE.name());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toFile
  /**
   ** This method takes in a parsed object and converts it to an XML file.
   **
   ** @param  element            the XML element to downstream.
   ** @param  fileName           the name of the file to store the result.
   **
   ** @throws FeatureException   if the {@link JAXBContext} could not be created
   **                            or the outout file isn't accessible.
   */
  public static void toFile(final Object element, final String fileName)
    throws FeatureException {

    try {
      final OutputStream stream = new FileOutputStream(fileName);
      marshal(stream, element);
    }
    catch (FileNotFoundException e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_FILE_OPEN, fileName);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromFile
  /**
   ** This method unmarshalles an XML file to give a parsed object.
   **
   ** @param  fileName           the name of the file to parse.
   **
   ** @return                    the root of content tree being unmarshalled.
   **
   ** @throws FeatureException   if the <code>Marshaller</code> is unable to
   **                            unmarshal <code>element</code> (or any object
   **                            reachable from <code>obj</code>).
   **                            See <a href="#unmarshalEx">Unmarshalling XML
   **                            Data</a>.
   */
  public static Object fromFile(final String fileName)
    throws FeatureException {

    BufferedInputStream source = null;
    try {
      source = new BufferedInputStream(new FileInputStream(fileName));
    }
    catch (FileNotFoundException e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_FILE_OPEN, fileName);
    }
    return unmarshal(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** This method takes in a string and converts it to a parsed Object.
   **
   ** @param  stream             the XML  provided by to this stream.
   **
   ** @return                    the unmarshalled root of content tree.
   **
   ** @throws FeatureException   if the <code>Marshaller</code> is unable to
   **                            unmarshal <code>element</code> (or any object
   **                            reachable from <code>obj</code>).
   **                            See <a href="#unmarshalEx">Unmarshalling XML
   **                            Data</a>.
   */
  static Object unmarshal(final BufferedInputStream stream)
    throws FeatureException {

    final Unmarshaller unmarshaller = createUnmarshaller();
    try {
      return unmarshaller.unmarshal(stream);
    }
    catch (Exception e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_UNMARSHAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** This method takes in a parsed object and converts it to an XML string.
   **
   ** @param  stream             the XML output will be added to this stream.
   ** @param  element            the root of content tree to be marshalled.
   **
   ** @throws FeatureException   if the <code>Marshaller</code> is unable to
   **                            marshal <code>element</code> (or any object
   **                            reachable from <code>obj</code>).
   **                            See <a href="#elementMarshalling"> Marshalling
   **                            a JAXB element</a>.
   */
  static void marshal(final OutputStream stream, final Object element)
    throws FeatureException {

    try {
      createMarshaller().marshal(element, stream);
    }
    catch (Exception e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_MARSHAL, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMarshaller
  /**
   ** Create a {@link Marshaller} object that can be used to convert a java
   ** content tree into XML data.
   **
   ** @return                    a {@link Marshaller} object.
   **
   ** @throws FeatureException   if an error was encountered while creating the
   **                            {@link Marshaller} object.
   */
  static Marshaller createMarshaller()
    throws FeatureException {

    try {
      // The JAXBContext instance is initialized from a list of colon separated
      // Java package names. Each java package contains JAXB mapped classes,
      // schema-derived classes and/or user annotated classes.
      // Additionally, the java package may contain JAXB package annotations
      // that must be processed. (see JLS 3rd Edition, Section 7.4.1. Package
      // Annotations).
      final JAXBContext context    = JAXBContext.newInstance(ClassUtility.packageName(ObjectFactory.class));
      final Marshaller  marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.valueOf(true));
//      marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,  "http://www.oracle.com/schema/oim/request");
      return marshaller;
    }
    catch (JAXBException e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_CONTEXT, e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createUnmarshaller
  /**
   ** Create a {@link Unmarshaller} object that can be used to convert a java
   ** content tree into XML data.
   **
   ** @return                    a {@link Unmarshaller} object.
   **
   ** @throws FeatureException   if an error was encountered while creating the
   **                            {@link Unmarshaller} object.
   */
  static Unmarshaller createUnmarshaller()
    throws FeatureException {

    Unmarshaller unmarshaller = null;
    try {
      // The JAXBContext instance is initialized from a list of colon separated
      // Java package names. Each java package contains JAXB mapped classes,
      // schema-derived classes and/or user annotated classes.
      // Additionally, the java package may contain JAXB package annotations
      // that must be processed. (see JLS 3rd Edition, Section 7.4.1. Package
      // Annotations).
      unmarshaller = JAXBContext.newInstance(ClassUtility.packageName(ObjectFactory.class)).createUnmarshaller();
      final SchemaFactory factory  = SchemaFactory.newInstance(SCHEMA_INSTANCE);
      final URL           resource = RegistrationHandler.class.getClassLoader().getResource(SCHEMA_RESOURCE);
      if (resource != null)
        unmarshaller.setSchema(factory.newSchema(resource));
      else
        unmarshaller.setSchema(factory.newSchema(new File(SCHEMA_RESOURCE)));
      unmarshaller.setEventHandler(new ValidationHandler());
    }
    catch (JAXBException e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_CONTEXT, e.getLocalizedMessage());
    }
    catch (SAXException e) {
      throw new FeatureException(FeatureError.REMOTE_BINDING_SCHEMA, e);
    }
    return unmarshaller;
  }
}
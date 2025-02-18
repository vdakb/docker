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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVDescriptorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVDescriptorFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.lang.reflect.Constructor;

import java.util.Comparator;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

import oracle.xml.parser.schema.XSDException;
import oracle.xml.parser.schema.XSDBuilder;
import oracle.xml.parser.schema.XMLSchema;

import oracle.xml.parser.v2.SAXParser;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.Transformer;
import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// final class CSVDescriptorFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the superclass for the meta information about queryable and mutable
 ** attributes in a CSV file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVDescriptorFactory extends DefaultHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // element tags
  public static final String DESCRIPTOR     = "descriptor";
  public static final String ATTRIBUTES     = "attributes";
  public static final String ATTRIBUTE      = "attribute";
  public static final String PROPERTIES     = "properties";
  public static final String TRANSFORMATION = "transformation";
  public static final String PROCESSOR      = "processor";
  public static final String DICTIONARY     = "dictionary";
  public static final String INBOUND        = "inbound";
  public static final String OUTBOUND       = "outbound";

  // attribute tags
  public static final String IDENTIFIER     = "identifier";
  public static final String READONLY       = "readonly";
  public static final String MANDATORY      = "mandatory";
  public static final String NAME           = "name";
  public static final String TYPE           = "type";
  public static final String FORMATEXTERNAL = "formatExternal";
  public static final String FORMATINTERNAL = "formatInternal";
  public static final String DIRECTION      = "direction";
  public static final String CLASS          = "className";
  public static final String SEQUENCE       = "sequence";
  public static final String DEFAULT        = "default";
  public static final String ORIGIN         = "origin";
  public static final String VALUE          = "value";
  public static final String CONSTRAINT     = "constraint";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class XMLDescriptorParser
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~
  /**
   ** Handles parsing of a XML file which defines the descriptor.
   */
  private static class XMLDescriptorParser extends DefaultHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private File                descriptorFile  = null;

    private boolean             attributes      = false;
    private boolean             transformations = false;
    private boolean             processors      = false;

    private CSVDescriptor       descriptor      = null;
    private CSVAttribute        attribute       = null;
    private Transformer<String> transformer     = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>XMLDescriptorParser</code>
     **
     ** @param  descriptorFile   the CSV configuration file for the descriptor
     **                          to create from XML.
     */
    protected XMLDescriptorParser(final URL descriptorFile) {
      this.descriptorFile = new File(descriptorFile.getFile());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>XMLDescriptorParser</code>
     **
     ** @param  descriptorFile   the CSV configuration file for the descriptor
     **                          to create from XML.
     */
    protected XMLDescriptorParser(final File descriptorFile) {
      this.descriptorFile = descriptorFile;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   startElement (overridden)
    /**
     ** Receive notification of the start of an element.
     ** <br>
     ** Takes specific actions at the start of each element (such as allocating
     ** a new attributes.
     **
     ** @param  nameSpace        the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed.
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     ** @param  properties       the attributes attached to the element. If
     **                          there are no attributes, it shall be an empty
     **                          {@link Attributes} object.
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see DefaultHandler#startElement
     */
    @Override
    public void startElement(final String nameSpace, final String localName, final String qualifiedName, final Attributes properties)
      throws SAXException {

      try {
        if (localName.equals(DESCRIPTOR)) {
          this.descriptor = createDescriptor();
        }
        else if (localName.equals(ATTRIBUTES)) {
          // mark that we enter the attributes area
          this.attributes = true;
        }
        else if (localName.equals(TRANSFORMATION)) {
          // mark that we enter the transformations area
          this.transformations = true;
        }
        else if (localName.equals(PROCESSOR)) {
          // mark that we enter the processors area
          this.processors = true;
        }
        else if (localName.equals(ATTRIBUTE)) {
          // create the attribute and add it to the container
          attribute = createAttribute(descriptor, properties);
        }
        else if (localName.equals(INBOUND)) {
          if (this.transformations && this.processors)
            throw new CSVException(CSVError.INVALID_STATE, TRANSFORMATION + " | " + PROCESSOR);

          if (this.transformations)
            this.transformer = createTransformation(this.attribute, properties, true);
          else
            throw new CSVException(CSVError.INVALID_STATE, TRANSFORMATION);
        }
        else if (localName.equals(OUTBOUND)) {
          if (this.transformations && this.processors)
            throw new CSVException(CSVError.INVALID_STATE, TRANSFORMATION + " | " + PROCESSOR);

          if (this.transformations)
            this.transformer = createTransformation(this.attribute, properties, false);
          else
            throw new CSVException(CSVError.INVALID_STATE, TRANSFORMATION);
        }
        else if (localName.equals(DICTIONARY)) {
          if (!(transformer instanceof DictionaryTransformer))
            throw new CSVException(CSVError.INVALID_STATE, TRANSFORMATION + " | " + DICTIONARY);

          @SuppressWarnings("unchecked")
          final DictionaryTransformer<String, String> transformer = (DictionaryTransformer<String, String>)this.transformer;
          addRule(transformer, properties);
        }
        else if (localName.equals(PROPERTIES)) {
          if (this.attributes)
            setProperties(this.descriptor, this.attribute, properties);
          else
            throw new CSVException(CSVError.INVALID_STATE, ATTRIBUTES);
        }
        else
          throw new CSVException(CSVError.INVALID_ELEMENT, ATTRIBUTES);
      }
      catch (CSVException e) {
        throw new SAXException(e.getLocalizedMessage());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   endElement (overridden)
    /**
     ** Receive notification of the end of an element.
     ** <br>
     ** Takes specific actions at the end of each element (such as creating the
     ** CSVDescriptor.
     **
     ** @param  nameSpace        the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed.
     ** @param qualifiedName     the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     ** @see DefaultHandler#endElement
     */
    @Override
    public void endElement(final String nameSpace, final String localName, final String qualifiedName)
      throws SAXException {

      if (localName.equals(ATTRIBUTES)) {
        // mark that we leaving the attributes area
        this.attributes = false;
      }
      else if (localName.equals(TRANSFORMATION)) {
        // mark that we leaving the transformations area
        this.transformations = false;
      }
      else if (localName.equals(PROCESSOR)) {
        // mark that we leaving the processors area
        this.processors = false;
      }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   create
    /**
     ** Constructor for <code>CSVDescriptor</code>
     **
     ** @return                    the {@link CSVDescriptor} created from the
     **                            specified propertyFile.
     **
     ** @throws CSVException       in the event of misconfiguration (such as
     **                            failure to set an essential property) or if
     **                            initialization fails.
    */
    protected CSVDescriptor create()
      throws CSVException {

      try {
        ClassLoader loader = this.getClass().getClassLoader();
        String      name   = ClassUtility.classNameToFile(CSVDescriptor.class.getName(), ClassUtility.XSD);
        InputStream stream = loader.getResourceAsStream(name);
        if (stream == null)
          throw new CSVException(SystemError.FILE_MISSING, name);

        XSDBuilder builder = new XSDBuilder();
        SAXParser  parser  = new SAXParser();
        XMLSchema  schema  = builder.build(stream, null);

        parser.setXMLSchema(schema);
        parser.setValidationMode(SAXParser.SCHEMA_VALIDATION);
        parser.setPreserveWhitespace(false);
        parser.setContentHandler(this);
        parser.parse(new InputStreamReader(new FileInputStream(this.descriptorFile), "ISO-8859-1"));
      }
      catch (XSDException e) {
        throw new CSVException(SystemError.UNHANDLED, e);
      }
      catch (SAXException e) {
        throw new CSVException(SystemError.UNHANDLED, e);
      }
      catch (IOException e) {
        throw new CSVException(SystemError.UNHANDLED, e);
      }
      return this.descriptor;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Constructor for <code>CSVDescriptor</code>
   **
   ** @param  propertyFile       the CSV configuration file for the descriptor
   **                            to create.
   **
   ** @return                    the {@link CSVDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws CSVException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static CSVDescriptor create(final String propertyFile)
    throws CSVException {

    return create(FileSystem.url(propertyFile));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Constructor for <code>CSVDescriptor</code>
   **
   ** @param  propertyFile       the CSV configuration file for the descriptor
   **                            to create.
   ** @param  clazz              the class whose classloader should be used to
   **                            load the configuration file.
   **
   ** @return                    the {@link CSVDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws CSVException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static CSVDescriptor create(final String propertyFile, final Class<?> clazz)
    throws CSVException {

    return create(FileSystem.url(propertyFile, clazz));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Constructor for <code>CSVDescriptor</code>
   **
   ** @param  descriptorFile     the CSV configuration file for the descriptor
   **                            to create as XML.
   **
   ** @return                    the {@link CSVDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws CSVException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static CSVDescriptor create(final URL descriptorFile)
    throws CSVException {

    if (descriptorFile == null)
      throw new CSVException(SystemError.ARGUMENT_IS_NULL, "descriptorFile");

    return new XMLDescriptorParser(descriptorFile).create();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDescriptor
  /**
   ** Constructor for a {@link CSVDescriptor).
   **
   ** @return                    the created {@link CSVDescriptor).
   */
  private static CSVDescriptor createDescriptor() {
    return new CSVDescriptor();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createComparator
  /**
   ** Constructor for a {@link Comparator).
   **
   ** @param  descriptor         the {@link CSVDescriptor) that will receive the
   **                            {@link Comparator}.
   ** @param  properties         the attributes attached to the element. If
   **                            there are no attributes, it shall be an empty
   **                            {@link Attributes} object.
   **
   ** @return                    the created {@link Comparator).
   **
   ** @throws SAXException       in case an error occurs
   */
  private static Comparator<?> createComparator(final CSVDescriptor descriptor, final Attributes properties)
    throws CSVException
    ,      SAXException {

    String className = null;
    int    sequence  = Integer.MAX_VALUE;
    for (int i = 0; i < properties.getLength(); i++) {
      final String property = properties.getLocalName(i);
       if (CLASS.equals(property))
         className = properties.getValue(i);
       else if (SEQUENCE.equals(property))
         sequence = Integer.parseInt(properties.getValue(i));
    }
    Comparator<?> comparator = createComparator(className);
    /*
    if (priority >= sortingOutbound.size())
      sortingOutbound.add(comparator);
    else
      sortingOutbound.set(priority, comparator);
     */
     return comparator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttribute
  /**
   ** Constructor for a {@link CSVAttribute).
   **
   ** @param  descriptor         the {@link CSVDescriptor) that will receive the
   **                            {@link CSVAttribute}.
   ** @param  properties         the attributes attached to the element. If
   **                            there are no attributes, it shall be an empty
   **                            {@link Attributes} object.
   **
   ** @return                    the created {@link CSVAttribute).
   **
   ** @throws SAXException       in case an error occurs
   */
  private static CSVAttribute createAttribute(final CSVDescriptor descriptor, final Attributes properties)
    throws SAXException {

    String name       = null;
    String type       = null;
    String external   = null;
    String internal   = null;
    String constraint = null;
    int size = properties.getLength();
    for (int i = 0; i < size; i++) {
      final String property = properties.getLocalName(i);
      if (NAME.equals(property))
        name = properties.getValue(i);
      else if (TYPE.equals(property))
        type = properties.getValue(i);
      else if (FORMATEXTERNAL.equals(property))
        external = properties.getValue(i);
      else if (FORMATINTERNAL.equals(property))
        internal = properties.getValue(i);
      else if (CONSTRAINT.equals(property))
        constraint = properties.getValue(i);
      else
        throw new SAXException("An attribute name, type, formatInbound or formatOutbuond was expected");
    }
    // validate the format specification
    if (StringUtility.isEmpty(external) && !StringUtility.isEmpty(internal))
      throw new SAXException("formatInternal requires formatExternal to be set");
    if (StringUtility.isEmpty(internal) && !StringUtility.isEmpty(external))
      throw new SAXException("formatExternal requires formatInternal to be set");

    // create the attribute and add it to the container
    return descriptor.addAttribute(name, type, external, internal, constraint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setProperties
  /**
   ** Sets the properties of the {@link CSVAttribute)
   **
   ** @param  descriptor         the {@link CSVDescriptor) that owns the
   **                            {@link CSVAttribute}.
   ** @param  attribute          the {@link CSVAttribute) that will
   **                            receive the properties.
   ** @param  properties         the attributes attached to the element. If
   **                            there are no attributes, it shall be an empty
   **                            {@link Attributes} object.
   **
   ** @throws CSVException       in case the specified {@link CSVAttribute) is
   **                            marked as an identifier and the
   **                            {@link CSVDescriptor) does not know anything
   **                            about the specified {@link CSVAttribute).
   ** @throws SAXException       in case an error occurs
   */
  private static void setProperties(final CSVDescriptor descriptor, final CSVAttribute attribute, final Attributes properties)
    throws CSVException
    ,      SAXException {

    for (int i = 0; i < properties.getLength(); i++) {
      final String property = properties.getLocalName(i);
      final String value    = properties.getValue(i);
      if (IDENTIFIER.equals(property)) {
        if (Boolean.valueOf(value).booleanValue())
          descriptor.addIdentifier(attribute);
      }
      else if (READONLY.equals(property))
        attribute.setReadonly(Boolean.valueOf(value).booleanValue());
      else if (MANDATORY.equals(property)) {
        if (!descriptor.isIdentifier(attribute.name()))
          attribute.setMandatory(Boolean.valueOf(value).booleanValue());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTransformation
  /**
   ** Constructor for a {@link Transformer}.
   **
   ** @param  attribute          the {@link CSVAttribute) that will receive the
   **                            {@link Transformer}.
   ** @param  properties         the attributes attached to the element. If
   **                            there are no attributes, it shall be an empty
   **                            {@link Attributes} object.
   **
   ** @return                    the created {@link Transformer).
   **
   ** @throws CSVException       in case a class name is specified and the class
   **                            cannot be found or instantiated.
   ** @throws SAXException       in case an error occurs
   */
  private static Transformer<String> createTransformation(final CSVAttribute attribute, final Attributes properties, final boolean inbound)
    throws CSVException
    ,      SAXException {

    int    sequence     = Integer.MAX_VALUE;
    String defaultValue = null;
    String className    = null;
    for (int i = 0; i < properties.getLength(); i++) {
      final String property = properties.getLocalName(i);
      if (CLASS.equals(property))
        className = properties.getValue(i);
      else if (SEQUENCE.equals(property))
        sequence = Integer.parseInt(properties.getValue(i));
      else if (DEFAULT.equals(property))
        defaultValue = properties.getValue(i);
      else
        throw new SAXException("An attribute sequence, default or className was expected");
    }
    Transformer<String> transformer = null;
    if (StringUtility.isEmpty(className)) {
      transformer = new DictionaryTransformer<String, String>();
      ((DictionaryTransformer)transformer).setDefaultValue(defaultValue);
    }
    else {
      transformer = createTransformer(className);
      if (transformer instanceof DictionaryTransformer) {
        ((DictionaryTransformer)transformer).setDefaultValue(defaultValue);
      }
    }

    attribute.addTransformer(sequence, transformer, inbound);
    return transformer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addRule
  /**
   ** Adds a dictionary rule to the specified {@link DictionaryTransformer)
   **
   ** @param  transformer        the {@link DictionaryTransformer) that will
   **                            receive the translation rule.
   ** @param  properties         the attributes attached to the element. If
   **                            there are no attributes, it shall be an empty
   **                            {@link Attributes} object.
   **
   ** @throws SAXException       in case an error occurs
   */
  private static void addRule(final DictionaryTransformer<String, String> transformer, final Attributes properties)
    throws SAXException {

    String origin = null;
    String value  = null;
    for (int i = 0; i < properties.getLength(); i++) {
      final String property = properties.getLocalName(i);
      if (ORIGIN.equals(property))
        origin = properties.getValue(i);
      else if (VALUE.equals(property))
        value = properties.getValue(i);
      else
        throw new SAXException("An attribute origin, or value was expected");
    }
    transformer.put(origin, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTransformer
  /**
   ** Constructor for a <code>Transformer</code>
   **
   ** @param  className          the name of the class
   **                            {@link Transformer} to create.
   **
   ** @return                    the {@link Transformer} instance instantiated
   **                            from the specified full qualified class name.
   **
   ** @throws CSVException       in the event of the instance cannot be created.
   */
  private static Transformer<String> createTransformer(final String className)
    throws CSVException {

    Transformer<String> transformer = Transformer.NULL;
    try {
      // obtain the Class-Object
      Class<?> parserClass = Class.forName(className);
      // obtain the Constructor-Object
      Constructor<?> constructor = parserClass.getConstructor((Class[])null);
      // create a new instance of the transformer
      transformer = (Transformer<String>)constructor.newInstance((Object[])null);
    }
    catch (ClassNotFoundException e) {
      throw new CSVException(SystemError.CLASSNOTFOUND, e.getMessage());
    }
    catch (Exception e) {
      throw new CSVException(SystemError.UNHANDLED, e);
    }
    return transformer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createComparator
  /**
   ** Constructor a <code>Comparator</code>
   **
   ** @param  className          the full qualified class name of the comparator
   **                            class.
   **
   ** @return                    the {@link Comparator} instance instantiated
   **                            from the specified full qualified class name.
   **
   ** @throws CSVException       in the event of the instance cannot be created.
   */
  private static Comparator<?> createComparator(final String className)
    throws CSVException {

    Comparator<?> comparator = null;
    try {
      // obtain the Class-Object
      Class<?> parserClass = Class.forName(className);
      // obtain the Constructor-Object
      Constructor<?> constructor = parserClass.getConstructor((Class[])null);
      // create a new instance of the transformer
      comparator = (Comparator)constructor.newInstance((Object[])null);
    }
    catch (ClassNotFoundException e) {
      throw new CSVException(SystemError.CLASSNOTFOUND, e.getMessage());
    }
    catch (Exception e) {
      throw new CSVException(SystemError.UNHANDLED, e);
    }
    return comparator;
  }
}
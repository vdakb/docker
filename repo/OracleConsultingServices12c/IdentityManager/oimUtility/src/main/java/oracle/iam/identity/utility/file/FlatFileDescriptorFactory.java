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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   FlatFileDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileDescriptor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.lang.reflect.Constructor;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import oracle.xml.parser.v2.SAXParser;

import oracle.xml.parser.schema.XMLSchema;
import oracle.xml.parser.schema.XSDBuilder;
import oracle.xml.parser.schema.XSDException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MDSIOException;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLError;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.Transformer;
import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileDescriptorFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the superclass for the meta information about queryable and mutable
 ** attributes in a flat file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public final class FlatFileDescriptorFactory extends DefaultHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // element tags
  static final String ELEMENT_DESCRIPTOR       = "descriptor";
  static final String ELEMENT_ATTRIBUTE        = "attribute";
  static final String ELEMENT_PROPERTIES       = "properties";
  static final String ELEMENT_TRANSFORMATION   = "transformation";
  static final String ELEMENT_DICTIONARY       = "dictionary";
  static final String ELEMENT_INBOUND          = "inbound";
  static final String ELEMENT_OUTBOUND         = "outbound";

  // attribute tags
  static final String ATTRIBUTE_NAME           = "name";
  static final String ATTRIBUTE_TYPE           = "type";
  static final String ATTRIBUTE_START          = "start";
  static final String ATTRIBUTE_LENGTH         = "length";
  static final String ATTRIBUTE_IDENTIFIER     = "identifier";
  static final String ATTRIBUTE_READONLY       = "readonly";
  static final String ATTRIBUTE_MANDATORY      = "mandatory";
  static final String ATTRIBUTE_FORMATEXTERNAL = "formatExternal";
  static final String ATTRIBUTE_FORMATINTERNAL = "formatInternal";
  static final String ATTRIBUTE_DIRECTION      = "direction";
  static final String ATTRIBUTE_CLASS          = "className";
  static final String ATTRIBUTE_SEQUENCE       = "sequence";
  static final String ATTRIBUTE_DEFAULT        = "default";
  static final String ATTRIBUTE_ORIGIN         = "origin";
  static final String ATTRIBUTE_VALUE          = "value";
  static final String ATTRIBUTE_CONSTRAINT     = "constraint";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Grammar
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants.
   */
  enum Grammar {
      /** the encoded valid grammar state transitions in the parser. */
      INIT(new Grammar[0], "")
    , DESCRIPTOR(INIT,                               ELEMENT_DESCRIPTOR)
    , ATTRIBUTE(DESCRIPTOR,                          ELEMENT_ATTRIBUTE)
    , PROPERTIES(ATTRIBUTE,                          ELEMENT_PROPERTIES)
    , TRANSFORMATION(ATTRIBUTE,                      ELEMENT_TRANSFORMATION)
    , INBOUND(TRANSFORMATION,                        ELEMENT_INBOUND)
    , OUTBOUND(TRANSFORMATION,                       ELEMENT_OUTBOUND)
    , DICTIONARY(new Grammar[] {INBOUND, OUTBOUND},  ELEMENT_DICTIONARY)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8124309909030891763")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** array of parent states to this one. */
    final Grammar parents[];

    /** the name of the tag for this state. */
    final String  tag;

    ////////////////////////////////////////////////////////////////////////////
    // static init block
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** This code executes after the enums have been constructed.
     ** <p>
     ** Because of order of execution when initializing an enum, you can't call
     ** static functions in an enum constructor. (They are constructed before
     ** static initialization).
     ** <p>
     ** Instead, we use a static initializer to populate the lookup map after
     ** all the enums are constructed.
      */
    static {
      for (Grammar state : Grammar.values()) {
        lookup.put(state.tag, state);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Grammar</code> with a single parent state.
     **
     ** @param  parent           the predecessor where this grammar state can
     **                          occure within.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     */
    Grammar(final Grammar parent, final String tag) {
      this(new Grammar[]{parent}, tag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Grammar</code> with a multiple parent states.
     **
     ** @param  parents          the predecessors where this grammar state can
     **                          occure within.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     */
    Grammar(final Grammar[] parents, String tag) {
      this.parents = parents;
      this.tag     = tag;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: lookup
    /**
     ** Lookups the <code>Grammar</code> for the specified a tag name.
     **
     ** @param  tag              the name of the tag the <code>Grammar</code>
     **                          is requested for.
     **
     ** @return                  the <code>Grammar</code> for that tag.
     */
    public static Grammar lookup(final String tag) {
      return lookup.get(tag);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   transition
    /**
     ** Checks whether it is valid to transition to the specified state from
     ** this state.
     **
     ** @param  state
     **
     ** @return
     */
    public boolean transition(final Grammar state) {
      if (this.equals(state))
        return true;

      for (int i = 0; i < state.parents.length; i++) {
        if (state.parents[i].equals(this))
          return true;
      }
      for (int i = 0; i < this.parents.length; i++) {
        if (this.parents[i].equals(state))
          return true;
      }
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** Handles parsing of a XML file which defines the descriptor.
   */
  private static class Parser extends SAXInput {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private InputSource          source     = null;
    private FlatFileDescriptor   descriptor = null;

    private Grammar              cursor     = Grammar.INIT;
    private final Stack<Grammar> state      = new Stack<Grammar>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>SAXInput</code> that will use a third-party SAX
     ** parser (chosen by JAXP)  to handle the parsing duties and simply listens
     ** to the SAX events to construct a document.
     **
     ** @param  source           the flat file configuration file for the
     **                          descriptor to create from XML.
     **
     ** @throws SAXException     in case <code>SAXParserFactory</code> is not
     **                          able to create an appropriate
     **                          {@link SAXParser}.
     */
    protected Parser(final InputSource source)
      throws SAXException {

      // ensure inheritance
      super();

      // initialize instance attributes
      this.source = source;
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
     ** @param  attributes       the attributes attached to the element. If
     **                          there are no attributes, it shall be an empty
     **                          {@link Attributes} object.
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see DefaultHandler#startElement
     */
    @Override
    @SuppressWarnings("unchecked")
    public void startElement(final String nameSpace, final String localName, final String qualifiedName, final Attributes attributes)
      throws SAXException {

      // check for state transition
      Grammar state = Grammar.lookup(localName);
      if (state == null) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), localName };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_UNKNOWN, arguments));
      }
      // we know it's a valid tag
      if (!this.cursor.transition(state)) {
        // invalid transition
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), this.cursor.toString(), state.toString() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_TRANSISTION, arguments));
      }
      // change FSA state
      this.state.push(this.cursor);
      this.cursor = state;

      switch (this.cursor) {
        case DESCRIPTOR      : this.descriptor = new FlatFileDescriptor();
                               break;
        case ATTRIBUTE       : // there must be a file descriptor on the value
                               // stack; we need to cast her to FlatFileDescriptor
                               push(this.descriptor.addAttribute(attributes.getValue(ATTRIBUTE_NAME), attributes.getValue(ATTRIBUTE_TYPE), Integer.valueOf(attributes.getValue(ATTRIBUTE_START)).intValue(), Integer.valueOf(attributes.getValue(ATTRIBUTE_LENGTH)).intValue()));
                               break;
        case PROPERTIES     : // there must be a file attribute on the value
                              // stack; we need to cast her to FlatFileAttribute
                              final FlatFileAttribute attribute = ((FlatFileAttribute)peek());
                              for (int i = 0; i < attributes.getLength(); i++) {
                                final String property = attributes.getLocalName(i);
                                final String value    = attributes.getValue(i);
                                if (ATTRIBUTE_IDENTIFIER.equals(property)) {
                                  if (Boolean.valueOf(value).booleanValue())
                                    this.descriptor.addIdentifier(attribute);
                                }
                                else if (ATTRIBUTE_READONLY.equals(property))
                                  attribute.readonly(Boolean.valueOf(value).booleanValue());
                                else if (ATTRIBUTE_MANDATORY.equals(property)) {
                                  if (!descriptor.isIdentifier(attribute.name()))
                                    attribute.mandatory(Boolean.valueOf(value).booleanValue());
                                }
                              }
                              break;
        case TRANSFORMATION : // there must be a file attribute on the value
                              // stack; we need to cast her to FlatFileAttribute
                              //((FlatFileAttribute)peek()).mandatory(mandatory);
                              break;
        case INBOUND        : // there must be a file attribute on the value
                              // stack; we need to cast her to FlatFileAttribute
                              push(createTransformation((FlatFileAttribute)peek(), attributes, true));
                              break;
        case OUTBOUND       : // there must be a file attribute on the value
                              // stack; we need to cast her to FlatFileAttribute
                              push(createTransformation((FlatFileAttribute)peek(), attributes, false));
                              break;
        case DICTIONARY     : // there must be a attribute transformer on the
                              // value stack; we need to cast her to
                              // Transformer
                              addRule((DictionaryTransformer)peek(), attributes);
                              break;
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

      switch (this.cursor) {
        case ATTRIBUTE       :
        case INBOUND         :
        case OUTBOUND        : pop();
                               break;
      }
      // change FSA state
      this.cursor = this.state.pop();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: character (overridden)
    /**
     ** Receive notification of character data inside an element.
     **
     ** @param ch                the characters.
     ** @param start             the start position in the character array.
     ** @param length            the number of characters to use from the
     **                          character array.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @Override
    public void characters(char ch[], int start, int length) {
      switch (this.cursor) {
        default     : break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create
    /**
     ** Constructor for <code>FlatFileDescriptor</code>
     **
     ** @return                    the {@link FlatFileDescriptor} created from
     **                            the specified source.
     **
     ** @throws FlatFileException  in the event of misconfiguration (such as
     **                            failure to set an essential property) or if
     **                            initialization fails.
    */
    protected FlatFileDescriptor create()
      throws FlatFileException {

      try {
        ClassLoader loader = this.getClass().getClassLoader();
        String      name   = ClassUtility.classNameToFile(FlatFileDescriptor.class.getName(), ClassUtility.XSD);
        InputStream stream = loader.getResourceAsStream(name);
        if (stream == null)
          throw new FlatFileException(SystemError.FILE_MISSING, name);

        XSDBuilder builder = new XSDBuilder();
        SAXParser  parser  = new SAXParser();
        XMLSchema  schema  = builder.build(stream, null);

        parser.setXMLSchema(schema);
        parser.setValidationMode(SAXParser.SCHEMA_VALIDATION);
        parser.setPreserveWhitespace(false);
        parser.setContentHandler(this);
        parser.parse(this.source);
        return this.descriptor;
      }
      catch (XSDException e) {
        throw new FlatFileException(e);
      }
      catch (SAXException e) {
        throw new FlatFileException(e);
      }
      catch (IOException e) {
        throw new FlatFileException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method for {@link FlatFileDescriptor} to create.
   **
   ** @param  propertyFile       the flat file configuration file for the
   **                            descriptor to create.
   **
   ** @return                    the {@link FlatFileDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static FlatFileDescriptor create(final String propertyFile)
    throws TaskException {

    return create(FileSystem.url(propertyFile));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method for {@link FlatFileDescriptor} to create.
   **
   ** @param  propertyFile       the flat file configuration file for the
   **                            descriptor to create.
   **
   ** @return                    the {@link FlatFileDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static FlatFileDescriptor create(final File propertyFile)
    throws TaskException {

    return create(FileSystem.url(propertyFile.getAbsolutePath()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Constructor for <code>FlatFileDescriptor</code>
   **
   ** @param  propertyFile       the CSV configuration file for the descriptor
   **                            to create.
   ** @param  clazz              the class whose classloader should be used to
   **                            load the configuration file.
   **
   ** @return                    the {@link FlatFileDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static FlatFileDescriptor create(final String propertyFile, final Class<?> clazz)
    throws TaskException {

    return create(FileSystem.url(propertyFile, clazz));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Constructor for <code>FlatFileDescriptor</code>
   **
   ** @param  descriptorFile     the CSV configuration file for the descriptor
   **                            to create as XML.
   **
   ** @return                    the {@link FlatFileDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static FlatFileDescriptor create(final URL descriptorFile)
    throws TaskException {

    if (descriptorFile == null)
      throw TaskException.argumentIsNull("descriptorFile");

    try {
      return create(descriptorFile.openStream());
    }
    catch (IOException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to configure a {@link FlatFileDescriptor} from an
   ** {@link InputStream}.
   **
   ** @param  stream             the configuration stream for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @return                    the {@link FlatFileDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static FlatFileDescriptor create(final InputStream stream)
    throws TaskException {

    return create(new InputSource(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link FlatFileDescriptor} from a
   ** {@link PDocument}.
   **
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **
   ** @return                    the {@link FlatFileDescriptor} created from the
   **                            specified document.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static FlatFileDescriptor create(final PDocument document)
    throws TaskException {

    // prevent bogus input
    if (document == null)
      throw TaskException.argumentIsNull("document");

    try {
      return create(document.read());
    }
    catch (MDSIOException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to configure a {@link FlatFileDescriptor} from an
   ** {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @return                    the {@link FlatFileDescriptor} created from the
   **                            specified propertyFile.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static FlatFileDescriptor create(final InputSource source)
    throws TaskException {

    // prevent bogus input
    if (source == null)
      throw TaskException.argumentIsNull("source");

    try {
      final Parser parser = new Parser(source);
      return parser.create();
    }
    catch (SAXException e) {
      throw new FlatFileException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTransformation
  /**
   ** Constructor for a {@link Transformer}.
   **
   ** @param  attribute          the {@link FlatFileAttribute) that will receive
   **                            the {@link Transformer}.
   ** @param  properties         the attributes attached to the element. If
   **                            there are no attributes, it shall be an empty
   **                            {@link Attributes} object.
   **
   ** @return                    the created {@link Transformer).
   **
   ** @throws SAXException       in case a class name is specified and the class
   **                            cannot be found or instantiated.
   */
  private static Transformer<String> createTransformation(final FlatFileAttribute attribute, final Attributes properties, final boolean inbound)
    throws SAXException {

    int    sequence     = Integer.MAX_VALUE;
    String defaultValue = null;
    String className    = null;
    for (int i = 0; i < properties.getLength(); i++) {
      final String property = properties.getLocalName(i);
      if (ATTRIBUTE_CLASS.equals(property))
        className = properties.getValue(i);
      else if (ATTRIBUTE_SEQUENCE.equals(property))
        sequence = Integer.parseInt(properties.getValue(i));
      else if (ATTRIBUTE_DEFAULT.equals(property))
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
      if (ATTRIBUTE_ORIGIN.equals(property))
        origin = properties.getValue(i);
      else if (ATTRIBUTE_VALUE.equals(property))
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
   ** @throws SAXException       in the event of the instance cannot be created.
   */
  @SuppressWarnings("unchecked")
  private static Transformer<String> createTransformer(final String className)
    throws SAXException {

    try {
      // obtain the Class-Object
      Class<?> parserClass = Class.forName(className);
      // obtain the Constructor-Object
      Constructor<?> constructor = parserClass.getConstructor((Class[])null);
      // create a new instance of the transformer
      return (Transformer<String>)constructor.newInstance((Object[])null);
    }
    catch (ClassNotFoundException e) {
      throw new SAXException(TaskBundle.format(TaskError.CLASSNOTFOUND, className));
    }
    catch (Exception e) {
      throw new SAXException(e);
    }
  }
}
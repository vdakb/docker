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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   CatalogFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CatalogFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;

import oracle.iam.identity.common.spi.CatalogAMInstance;
import oracle.iam.identity.common.spi.UsageInstance;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.tools.ant.BuildException;

import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MDSIOException;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLError;

////////////////////////////////////////////////////////////////////////////////
// class CatalogFactory
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The unmarshaller to spool out the ADF application module that's published
 ** in <code>Identity Manager</code>.
 ** <br>
 ** Those descriptors includes the bindings for a certain view usages and the
 ** customization the taskflow binding of those usages uploaded to MDS
 ** later on by publishing the <code>Sandbox</code>.
 ** <p>
 ** The class is kept abstract due to it expose only static methods.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** The tag names of the created XML file are not declared by constants. This
 ** violations is an exception regarding the coding guidelines but is acceptable
 ** due to those tags are only used inside of this class and occurs mostly only
 ** once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class CatalogFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ELEMENT_ATTRIBUTE     = "attribute";
  public static final String ELEMENT_CUSTOMIZATION = "customization";

  public static final String ELEMENT_INSERT        = "insert";
  public static final String ELEMENT_UPDATE        = "modify";

  public static final String ATTRIBUTE_NAME        = "name";
  public static final String ATTRIBUTE_VALUE       = "value";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Catalog catalog;

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
      INIT(new Grammar[0],    "")
    , CUSTOMIZATION(INIT,   ELEMENT_CUSTOMIZATION)
    , INSERT(CUSTOMIZATION, ELEMENT_INSERT)
    , MODIFY(CUSTOMIZATION, ELEMENT_UPDATE)
    , VIEW_USAGE(INSERT,    Catalog.VIEW_USAGE)
    , LINK_USAGE(INSERT,    Catalog.LINK_USAGE)
    , ATTRIBUTE(MODIFY,     ELEMENT_ATTRIBUTE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8359176875112780432")
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
      for (Grammar state : CatalogFactory.Grammar.values()) {
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
  // ~~~~~ ~~~~~
  /**
   ** Handles parsing of a XML file which defines the mapping descriptor.
   */
  private class Parser extends SAXInput {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Grammar              cursor = Grammar.INIT;
    private final Stack<Grammar> state  = new Stack<Grammar>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Parser</code>.
     **
     ** @throws SAXException     in case {@link SAXInput} is not able to create
     **                          an appropriate parser.
     */
    private Parser()
      throws SAXException {

      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: startElement (overridden)
    /**
     ** Receive notification of the beginning of an element.
     ** <p>
     ** The Parser will invoke this method at the beginning of every element in
     ** the XML document; there will be a corresponding
     ** {@link #endElement(String, String, String) endElement} event for every
     ** startElement event (even when the element is empty). All of the
     ** element's content will be reported, in order, before the corresponding
     ** endElement event.
     ** <p>
     ** This event allows up to three name components for each element:
     ** <ol>
     **   <li>the Namespace URI;</li>
     **   <li>the local name; and</li>
     **   <li>the qualified (prefixed) name.</li>
     ** </ol>
     ** Any or all of these may be provided, depending on the values of the
     ** <code>http://xml.org/sax/features/namespaces</code> and the
     ** <code>http://xml.org/sax/features/namespace-prefixes</code> properties:
     ** <ul>
     **   <li>the Namespace URI and local name are required when the namespaces
     **       property is <code>true</code> (the default), and are optional when
     **       the namespaces property is <code>false</code> (if one is
     **       specified, both must be);
     **   <li>the qualified name is required when the namespace-prefixes
     **       property is <code>true</code>, and is optional when the
     **       namespace-prefixes property is <code>false</code> (the default).
     ** </ul>
     ** Note that the attribute list provided will contain only attributes with
     ** explicit values (specified or defaulted):
     **   #IMPLIED attributes will be omitted.
     **   The attribute list will contain attributes used for Namespace
     **   declarations (xmlns* attributes) only if the
     **   <code>http://xml.org/sax/features/namespace-prefixes</code> property
     **   is <code>true</code> (it is <code>false</code> by default, and support
     **   for a <code>true</code> value is optional).
     ** <p>
     ** Like {@link #characters(char[], int, int) characters()}, attribute
     ** values may have characters that need more than one <code>char</code>
     ** value.
     **
     ** @param  uri              the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     ** @param  attributes       the attributes attached to the element.
     **                          If there are no attributes, it shall be an
     **                          empty {@link Attributes} object. The value of
     **                          this object after startElement returns is
     **                          undefined.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     **
     ** @see    #endElement(String, String, String)
     ** @see    org.xml.sax.Attributes
     ** @see    org.xml.sax.helpers.AttributesImpl
     */
    @Override
    public void startElement(final String uri, final String localName, final String qualifiedName, final Attributes attributes)
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
      // dispatch to handling method
      switch (this.cursor) {
        case CUSTOMIZATION : processCustomization(CatalogFactory.this.catalog, attributes);
                             break;
        case MODIFY        : // there must be a customization descriptor on the
                             // value stack; we need to cast here to Catalog
                             push(new Catalog.Module());
                             break;
        case VIEW_USAGE    : processUsage(CatalogFactory.this.catalog, attributes);
                             break;
        case LINK_USAGE    : processLink(CatalogFactory.this.catalog, attributes);
                             break;
        case ATTRIBUTE     : final String name  = attributes.getValue(ATTRIBUTE_NAME);
                             final String value = attributes.getValue(ATTRIBUTE_VALUE);
                             // there must be a customization descriptor on the
                             // value stack; we need to cast here to Catalog
                             final Catalog.Module stack = (Catalog.Module)peek();
                             switch(name) {
                               case Catalog.MODULE_EMPTY : stack.empty(Boolean.valueOf(value));
                                                           break;
                               case Catalog.MODULE_MAJOR : stack.major(value);
                                                           break;
                               case Catalog.MODULE_MINOR : stack.minor(Integer.valueOf(value));
                                                           break;
                               case Catalog.MODULE_NAME  : stack.name(value);
                                                           break;
                             }
                             break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endElement (overridden)
    /**
     ** Receive notification of the end of an element.
     ** <p>
     ** The SAX parser will invoke this method at the end of every element in
     ** the XML document; there will be a corresponding
     ** {@link #startElement(String, String, String, Attributes) startElement}
     ** event for every endElement event (even when the element is empty).
     ** <p>
     ** For information on the names, see
     ** {@link #startElement (String, String, String, Attributes)}.
     **
     ** @param  uri              the Namespace URI, or the empty string if the
     **                          element has no Namespace URI or if Namespace
     **                          processing is not being performed.
     ** @param  localName        the local name (without prefix), or the empty
     **                          string if Namespace processing is not being
     **                          performed
     ** @param  qualifiedName    the qualified name (with prefix), or the empty
     **                          string if qualified names are not available.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @Override
    public void endElement(final String uri, final String localName, final String qualifiedName)
      throws SAXException {

      // dispatch to handling method
      switch (this.cursor) {
        case MODIFY        : CatalogFactory.this.catalog.module((Catalog.Module)pop());
                             break;
      }
      // change FSA state
      this.cursor = this.state.pop();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CatalogFactory</code> that configures the specified
   ** {@link Catalog}.
   **
   ** @param  catalog            the {@link Catalog} to configure by this
   **                            <code>CatalogFactory</code>.
   **
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private CatalogFactory(final Catalog catalog)
    throws BuildException {

    // ensure inheritance
    super();

    // prevent bogus input
    if (catalog == null)
      throw new BuildException("catalog");

    // initialize instance attributes
    this.catalog = catalog;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Catalog} from a {@link File}.
   **
   ** @param  module             the {@link Catalog} to be configured by
   **                            this <code>CatalogFactory</code>.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Catalog module, final File file)
    throws BuildException {

    try {
      configure(module, new FileInputStream(file));
    }
    catch (IOException e) {
      throw new BuildException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Catalog} from an {@link InputStream}.
   **
   ** @param  module             the {@link Catalog} to be configured by this
   **                            <code>CatalogFactory</code>.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Catalog module, final InputStream stream)
    throws BuildException {

    // prevent bogus input
    if (stream == null)
      throw new BuildException("stream");

    configure(module, new InputSource(stream));
    try {
      stream.close();
    }
    catch (IOException e) {
      throw new BuildException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Catalog} from an {@link InputSource}.
   **
   ** @param  module             the {@link Catalog} to be configured by this
   **                            <code>CatalogFactory</code>.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Catalog module, final InputSource source)
    throws BuildException {

    // validate the provided stream against the schema
    final CatalogFactory factory = new CatalogFactory(module);
    factory.configure(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Catalog} from a {@link PDocument}.
   **
   ** @param  module             the {@link Catalog} to be configured by this
   **                            <code>CatalogFactory</code>.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Catalog module, final PDocument document)
    throws BuildException {

    // prevent bogus input
    if (document == null)
      throw new BuildException("document");

    final CatalogFactory factory = new CatalogFactory(module);
    try {
      factory.configure(document.read());
    }
    catch (MDSIOException e) {
      throw new BuildException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Catalog} from a {@link CatalogAMInstance}.
   **
   ** @param  module             the {@link Catalog} to be configured by this
   **                            <code>CatalogFactory</code>.
   ** @param  source             the configuration for the descriptor to create
   **                            by parsing the specified {@link CatalogAMInstance}.
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final Catalog module, final CatalogAMInstance source)
    throws BuildException {

    // validate the provided stream against the schema
    final CatalogFactory factory = new CatalogFactory(module);
    factory.configure(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processCustomization
  /**
   ** Initialize the instance attributes of the specified {@link Catalog}
   ** instance from the provided {@link Attributes}.
   **
   ** @param  module             the {@link Catalog} the instance
   **                            attributes has to be set for.
   ** @param  attributes         the {@link Attributes} as the source.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   */
  protected void processCustomization(final Catalog module, final Attributes attributes)
    throws SAXException {

    // get the uri of the catalog module customization
    module.uri(attributes.getValue(Catalog.CUST_URI));
    // get the name of the catalog module customization
    module.name(attributes.getValue(Catalog.CUST_NAME));
    // get the version of the catalog module customization
    module.version(attributes.getValue(Catalog.CUST_VERSION));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processCustomization
  /**
   ** Initialize the instance attributes of the specified {@link Catalog}
   ** instance from the provided {@link Attributes}.
   **
   ** @param  catalog            the {@link Catalog} the instance
   **                            attributes has to be set for.
   ** @param  attributes         the {@link Attributes} as the source.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   */
  protected void processUsage(final Catalog catalog, final Attributes attributes)
    throws SAXException {

    catalog.usage(new Catalog.View.Usage(attributes.getValue(Catalog.USAGE_NAME), attributes.getValue(Catalog.USAGE_VIEW)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processLink
  /**
   ** Initialize the instance attributes of the specified {@link Catalog}
   ** instance from the provided {@link Attributes}.
   **
   ** @param  catalog            the {@link Catalog} the instance
   **                            attributes has to be set for.
   ** @param  attributes         the {@link Attributes} as the source.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   */
  protected void processLink(final Catalog catalog, final Attributes attributes)
    throws SAXException {

    catalog.link(new Catalog.View.Link(attributes.getValue(Catalog.USAGE_NAME), attributes.getValue(Catalog.USAGE_LINK), attributes.getValue(Catalog.USAGE_LINK_SRC), attributes.getValue(Catalog.USAGE_LINK_DST)));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Descriptor} from an
   ** {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private void configure(final InputSource source)
    throws BuildException {

    // prevent bogus input
    if (source == null)
      throw new BuildException("source");

    try {
      final Parser parser = new Parser();
      parser.processDocument(source);
    }
    catch (SAXException e) {
      throw new BuildException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link Catalog} from an
   ** {@link CatalogAMInstance}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing a {@link CatalogAMInstance}.
   **
   ** @throws BuildException     in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private void configure(final CatalogAMInstance source)
      throws BuildException {

    if (source == null) {
      throw new BuildException("source");
    }

    for (final UsageInstance usage : source.usages()) {
      this.catalog.usage(usage.name());
      for (final String form : usage.forms()) {
        this.catalog.usage(usage.name(), form);
        this.catalog.link(usage.name(), form);
      }
    }
  }
}
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
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryFeatureParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFeatureFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/
package oracle.iam.identity.plp.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import oracle.hst.foundation.resource.XMLStreamBundle;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.xml.XMLError;

import oracle.iam.identity.connector.integration.FeatureParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
////////////////////////////////////////////////////////////////////////////////
// final class DirectoryFeatureParser
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to unmarshalls meta information of for a Directory
 ** Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
final class DirectoryFeatureParser extends FeatureParser {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String ELEMENT_GROUP               = "group";
  public static final String ELEMENT_ACCOUNT             = "account";
  public static final String ELEMENT_ENTERPRISE_DATABASE = "enterprise-database";
  public static final String ELEMENT_ENTERPRISE_DOMAIN   = "enterprise-domain";
  public static final String ELEMENT_ENTERPRISE_ROLE     = "enterprise-role";
  public static final String ELEMENT_ENTERPRISE_SCHEMA   = "enterprise-schema";
  public static final String ELEMENT_MULTIVALUED         = "multi-valued";
  public static final String ELEMENT_ATTRIBUTE           = "attribute";
  public static final String ELEMENT_SERVER              = "server";
  public static final String ELEMENT_ACCOUNT_LINK        = "account-link";
  public static final String ELEMENT_GROUP_LINK          = "group-link";

  public static final String ATTRIBUTE_HOST              = "host";
  public static final String ATTRIBUTE_PORT              = "port";
  public static final String ATTRIBUTE_OBJECT_CLASS      = "object-class";
  public static final String ATTRIBUTE_OBJECT_PREFIX     = "object-prefix";
  public static final String ATTRIBUTE_OBJECT_MEMBER     = "object-member";
  public static final String ATTRIBUTE_HOME_ROOT_DN      = "home-organization-dn";
  public static final String ATTRIBUTE_PASSWORD          = "password";
  public static final String ATTRIBUTE_ATTRIBUTE         = "attribute";
  public static final String ATTRIBUTE_DN                = "dn";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Grammar              cursor = Grammar.INIT;
  private final Stack<Grammar> state  = new Stack<Grammar>();

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
    , FEATURE            (INIT,                                                    FeatureParser.ELEMENT_FEATURE)
    , FAILOVER           (FEATURE,                                                 Directory.OIM.Feature.FAILOVER_CONFIGURATION)
    , SERVER             (FAILOVER,                                                ELEMENT_SERVER)
    , DISTINGUISHED_NAMES(FEATURE,                                                 Directory.OIM.Feature.DISTINGUISHED_NAME)
    , BINARY             (FEATURE,                                                 Directory.OIM.Feature.BINARY_OBJECT_ATTRIBUTE)
    , ACCOUNT            (new Grammar[]{FEATURE},                                  ELEMENT_ACCOUNT)
    , ENTERPRISE_DATABASE(FEATURE,                                                 ELEMENT_ENTERPRISE_DATABASE)
    , ENTERPRISE_DOMAIN  (FEATURE,                                                 ELEMENT_ENTERPRISE_DOMAIN)
    , ENTERPRISE_ROLE    (FEATURE,                                                 ELEMENT_ENTERPRISE_ROLE)
    , ENTERPRISE_SCHEMA  (FEATURE,                                                 ELEMENT_ENTERPRISE_SCHEMA)
    , GROUP              (new Grammar[]{FEATURE},                                  ELEMENT_GROUP)
    , MULTIVALUED        (new Grammar[]{GROUP, ACCOUNT},                           ELEMENT_MULTIVALUED)
    , ATTRIBUTE          (new Grammar[]{DISTINGUISHED_NAMES, BINARY, MULTIVALUED}, ELEMENT_ATTRIBUTE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
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
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for <code>DirectoryFeatureParser</code>.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureParser}.
   **
   ** @throws SAXException       in case {@link SAXInput} is not able to create
   **                            an appropriate parser.
   */
  protected DirectoryFeatureParser(final DirectoryFeature feature)
    throws SAXException {

    // ensure inheritance
    super(feature, Directory.PROPERTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startElement (overridden)
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
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   ** @param  attributes         the attributes attached to the element.
   **                            If there are no attributes, it shall be an
   **                            empty {@link Attributes} object. The value of
   **                            this object after startElement returns is
   **                            undefined.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
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
    switch (state) {
      case FEATURE             : processFeature(attributes);
                                 break;
      case SERVER              : processServer(attributes);
                                 break;
      case GROUP               : this.feature.put(Directory.OIM.Feature.GROUP_CLASS,                   attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                 this.feature.put(Directory.OIM.Feature.GROUP_PREFIX,                  attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                 this.feature.put(Directory.OIM.Feature.GROUP_MEMBER_PREFIX,           attributes.getValue(ATTRIBUTE_OBJECT_MEMBER));
                                 break;
      case ACCOUNT             : this.feature.put(Directory.OIM.Feature.ACCOUNT_CLASS,                 attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                 this.feature.put(Directory.OIM.Feature.ACCOUNT_PREFIX,                attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                 this.feature.put(Directory.OIM.Feature.ACCOUNT_MEMBER_PREFIX,         attributes.getValue(ATTRIBUTE_OBJECT_MEMBER));
                                 this.feature.put(Directory.OIM.Feature.ACCOUNT_CREDENTIAL_PREFIX,     attributes.getValue(ATTRIBUTE_PASSWORD));
                                 break;
      case ENTERPRISE_DATABASE : this.feature.put(Directory.OIM.Feature.ENTERPRISE_DATABASE_CLASS,     attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                 this.feature.put(Directory.OIM.Feature.ENTERPRISE_DATABASE_PREFIX,    attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                 break;
      case ENTERPRISE_DOMAIN   : this.feature.put(Directory.OIM.Feature.ENTERPRISE_DOMAIN_CLASS,       attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                 this.feature.put(Directory.OIM.Feature.ENTERPRISE_DOMAIN_PREFIX,      attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                 break;
      case ENTERPRISE_SCHEMA   : this.feature.put(Directory.OIM.Feature.ENTERPRISE_SCHEMA_CLASS,       attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                 this.feature.put(Directory.OIM.Feature.ENTERPRISE_SCHEMA_PREFIX,      attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                 break;
      case ENTERPRISE_ROLE     : this.feature.put(Directory.OIM.Feature.ENTERPRISE_ROLE_CLASS,         attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                 this.feature.put(Directory.OIM.Feature.ENTERPRISE_ROLE_PREFIX,        attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                 this.feature.put(Directory.OIM.Feature.ENTERPRISE_ROLE_MEMBER_PREFIX, attributes.getValue(ATTRIBUTE_OBJECT_MEMBER));
                                 break;
      case MULTIVALUED         : List<String> list = new ArrayList<String>();
                                 switch (this.state.peek()) {
                                   case ACCOUNT             : this.feature.put(Directory.OIM.Feature.ACCOUNT_MULTIVALUE, list);
                                                              break;
                                 }
                                 push(list);
                                 break;
      default                  : break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
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
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed.
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   */
  @Override
  public void endElement(final String uri, final String localName, final String qualifiedName)
    throws SAXException {

    // dispatch to handling method
    switch (this.cursor) {
      case MULTIVALUED         : pop();
                                 break;
      default                  : break;
    }
    // change FSA state
    this.cursor = this.state.pop();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   characters (overridden)
  /**
   ** Receive notification of character data.
   ** <p>
   ** The Parser will call this method to report each chunk of character data.
   ** SAX parsers may return all contiguous character data in a single chunk,
   ** or they may split it into several chunks; however, all of the characters
   ** in any single event must come from the same external entity so that the
   ** {@link org.xml.sax.Locator} provides useful information.
   ** <p>
   ** The application must not attempt to read from the array outside of the
   ** specified range.
   ** <p>
   ** Individual characters may consist of more than one Java
   ** <code>char</code> value. There are two important cases where this
   ** happens, because characters can't be represented in just sixteen bits.
   ** In one case, characters are represented in a <em>Surrogate Pair</em>,
   ** using two special Unicode values. Such characters are in the so-called
   ** "Astral Planes", with a code point above U+FFFF. A second case involves
   ** composite characters, such as a base character combining with one or
   ** more accent characters.
   ** <p>
   ** Your code should not assume that algorithms using
   ** <code>char</code>-at-a-time idioms will be working in character units;
   ** in some cases they will split characters. This is relevant wherever XML
   ** permits arbitrary characters, such as attribute values, processing
   ** instruction data, and comments as well as in data reported from this
   ** method. It's also generally relevant whenever Java code manipulates
   ** internationalized text; the issue isn't unique to XML.
   ** <p>
   ** Note that some parsers will report whitespace in element content using
   ** the {@link #ignorableWhitespace ignorableWhitespace} method rather than
   ** this one (validating parsers <em>must</em> do so).
   **
   ** @param  buffer             the characters from the XML document.
   ** @param  start              the start position in the array.
   ** @param  length             the number of characters to read from the
   **                            array.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception
   ** @see    #ignorableWhitespace
   ** @see    org.xml.sax.Locator
   */
  @Override
  @SuppressWarnings("unchecked")
  public void characters(final char[] buffer, final int start, final int length)
    throws SAXException {

    final DirectoryFeature feature = (DirectoryFeature)this.feature;
    // where we are?
    switch (this.state.peek()) {
      case DISTINGUISHED_NAMES : Set<String> list = feature.distinguishedName();
                                 if (list == null || list.isEmpty()) {
                                   list = new HashSet<String>();
                                   list.add(String.copyValueOf(buffer, start, length));
                                 }
                                 feature.put(Directory.OIM.Feature.DISTINGUISHED_NAME, list);
                                 break;
      case BINARY              : final String exist = feature.binaryObjectAttribute();
                                 final String value = StringUtility.isEmpty(exist) ? String.copyValueOf(buffer, start, length) : exist + feature.multiValueSeparator() + String.copyValueOf(buffer, start, length);
                                 feature.put(Directory.OIM.Feature.BINARY_OBJECT_ATTRIBUTE, value);
                                 break;
      case MULTIVALUED         : break;
      case ATTRIBUTE           : ((List<String>)peek()).add(String.copyValueOf(buffer, start, length));
                                 break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFeature (overridden)
  /**
   ** Factory method to fetch the <code>Feature</code> attributes from a
   ** XML stream.
   **
   ** @throws SAXException       if the required attribute name has no value.
   */
  @Override
  protected void processFeature(final Attributes attributes)
    throws SAXException {

    // ensure inheritance
    super.processFeature(attributes);
    
    this.feature.put(Directory.OIM.Feature.HOME_ORGANIZATION_DN, attributes.getValue(ATTRIBUTE_HOME_ROOT_DN));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processServer
  /**
   ** Factory method to fetch the <code>Server</code> attributes from a
   ** XML stream.
   */
  private void processServer(final Attributes attributes) {
    final DirectoryFeature feature = (DirectoryFeature)this.feature;
    List<DirectoryServer>  exist   = feature.failoverConfiguration();
    if (exist == null) {
      exist = new ArrayList<DirectoryServer>();
      this.feature.put(Directory.OIM.Feature.FAILOVER_CONFIGURATION, exist);
    }
    exist.add(new DirectoryServer(attributes.getValue(ATTRIBUTE_HOST), Integer.parseInt(attributes.getValue(ATTRIBUTE_PORT))));
  }
}
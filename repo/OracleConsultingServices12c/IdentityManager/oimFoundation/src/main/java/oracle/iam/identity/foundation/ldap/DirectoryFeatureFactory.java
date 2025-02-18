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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DirectoryFeatureFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFeatureFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-03-11  DSteding    First release version
*/

package oracle.iam.identity.foundation.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import oracle.hst.foundation.resource.XMLStreamBundle;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.mds.persistence.MDSIOException;
import oracle.mds.persistence.PDocument;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

////////////////////////////////////////////////////////////////////////////////
// final class DirectoryFeatureFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of for a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public final class DirectoryFeatureFactory extends XMLProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String NAMESPACE                   = "http://www.oracle.com/schema/oim/directory";

  public static final String ELEMENT_FEATURE             = "feature";
  public static final String ELEMENT_FAILOVER            = DirectoryConstant.FAILOVER_CONFIGURATION;
  public static final String ELEMENT_BINARY              = DirectoryConstant.BINARY_OBJECT_ATTRIBUTE;
  public static final String ELEMENT_DISTINGUISHED_NAMES = "distinguishedNames";
  public static final String ELEMENT_DOMAIN              = "domain";
  public static final String ELEMENT_COUNTRY             = "country";
  public static final String ELEMENT_LOCALITY            = "locality";
  public static final String ELEMENT_ROLE                = "role";
  public static final String ELEMENT_GROUP               = "group";
  public static final String ELEMENT_ORGANIZATION        = "organization";
  public static final String ELEMENT_ORGANIZATIONAL_UNIT = "organizationalUnit";
  public static final String ELEMENT_ACCOUNT             = "account";
  public static final String ELEMENT_ENTERPRISE_DATABASE = "enterprise-database";
  public static final String ELEMENT_ENTERPRISE_DOMAIN   = "enterprise-domain";
  public static final String ELEMENT_ENTERPRISE_ROLE     = "enterprise-role";
  public static final String ELEMENT_ENTERPRISE_SCHEMA   = "enterprise-schema";
  public static final String ELEMENT_MULTIVALUED         = "multi-valued";
  public static final String ELEMENT_ATTRIBUTE           = "attribute";
  public static final String ELEMENT_SERVER              = "server";
  public static final String ELEMENT_GROUP_LINK          = "group-link";
  public static final String ELEMENT_ROLE_LINK           = "role-link";

  public static final String ATTRIBUTE_NAMESPACE         = "xmlns";
  public static final String ATTRIBUTE_HOST              = "host";
  public static final String ATTRIBUTE_PORT              = "port";
  public static final String ATTRIBUTE_OBJECT_CLASS      = "object-class";
  public static final String ATTRIBUTE_OBJECT_PREFIX     = "object-prefix";
  public static final String ATTRIBUTE_OBJECT_MEMBER     = "object-member";
  public static final String ATTRIBUTE_PASSWORD          = "password";
  public static final String ATTRIBUTE_ATTRIBUTE         = "attribute";
  public static final String ATTRIBUTE_DN                = "dn";

  private static final Set<String> registry = new HashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final DirectoryFeature feature;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    registry.add(DirectoryConstant.INITIAL_CONTEXT_FACTORY);
    registry.add(DirectoryConstant.SECURITY_PROVIDER);
    registry.add(DirectoryConstant.CONNECTION_TIMEOUT);
    registry.add(DirectoryConstant.RESPONSE_TIMEOUT);
    registry.add(DirectoryConstant.REFERENTIAL_INTEGRITY);
    registry.add(DirectoryConstant.TIMESTAMP_FORMAT);

    registry.add(DirectoryConstant.OBJECT_CLASS_NAME);
    registry.add(DirectoryConstant.DISTINGUISHED_NAME_ATTRIBUTE);
    registry.add(DirectoryConstant.DISTINGUISHED_NAME_CASESENSITIVE);
    registry.add(DirectoryConstant.ENTRY_CREATED_ATTRIBUTE);
    registry.add(DirectoryConstant.ENTRY_MODIFIED_ATTRIBUTE);

    registry.add(DirectoryConstant.PASSWORD_OPERATION_SECURED);
    registry.add(DirectoryConstant.ENTITLEMENT_PREFIX_REQUIRED);

    registry.add(DirectoryConstant.DOMAIN_CONTAINER);
    registry.add(DirectoryConstant.COUNTRY_CONTAINER);
    registry.add(DirectoryConstant.LOCALITY_CONTAINER);
    registry.add(DirectoryConstant.ROLE_CONTAINER);
    registry.add(DirectoryConstant.GROUP_CONTAINER);
    registry.add(DirectoryConstant.SCHEMA_CONTAINER);
    registry.add(DirectoryConstant.CATALOG_CONTAINER);
    registry.add(DirectoryConstant.ACCOUNT_CONTAINER);
    registry.add(DirectoryConstant.GENERIC_CONTAINER);
    registry.add(DirectoryConstant.ORGANIZATION_CONTAINER);
    registry.add(DirectoryConstant.CHANGELOG_CONTAINER);
    registry.add(DirectoryConstant.CHANGELOG_CHANGETYPE);
    registry.add(DirectoryConstant.CHANGELOG_CHANGENUMBER);
    registry.add(DirectoryConstant.CHANGELOG_TARGETGUID);
    registry.add(DirectoryConstant.CHANGELOG_TARGETDN);
    registry.add(DirectoryConstant.CHANGELOG_TARGETDN);
  }

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
    , FEATURE            (INIT,     ELEMENT_FEATURE)
    , FAILOVER           (FEATURE,  ELEMENT_FAILOVER)
    , SERVER             (FAILOVER, ELEMENT_SERVER)
    , DISTINGUISHED_NAMES(FEATURE,  ELEMENT_DISTINGUISHED_NAMES)
    , BINARY             (FEATURE,  ELEMENT_BINARY)
    , DOMAIN             (FEATURE,  ELEMENT_DOMAIN)
    , COUNTRY            (FEATURE,  ELEMENT_COUNTRY)
    , LOCALITY           (FEATURE,  ELEMENT_LOCALITY)
    , ROLE               (FEATURE,  ELEMENT_ROLE)
    , GROUP              (FEATURE,  ELEMENT_GROUP)
    , ORGANIZATION       (FEATURE,  ELEMENT_ORGANIZATION)
    , ORGANIZATIONAL_UNIT(FEATURE,  ELEMENT_ORGANIZATIONAL_UNIT)
    , ACCOUNT            (FEATURE,  ELEMENT_ACCOUNT)
    , ENTERPRISE_DATABASE(FEATURE,  ELEMENT_ENTERPRISE_DATABASE)
    , ENTERPRISE_DOMAIN  (FEATURE,  ELEMENT_ENTERPRISE_DOMAIN)
    , ENTERPRISE_ROLE    (FEATURE,  ELEMENT_ENTERPRISE_ROLE)
    , ENTERPRISE_SCHEMA  (FEATURE,  ELEMENT_ENTERPRISE_SCHEMA)
    , MULTIVALUED        (new Grammar[]
        { DOMAIN
        , COUNTRY
        , LOCALITY
        , ROLE
        , GROUP
        , ORGANIZATION
        , ORGANIZATIONAL_UNIT
        , ACCOUNT
        },                          ELEMENT_MULTIVALUED)
    , GROUPLINK         (new Grammar[]
        { ORGANIZATION
        , ORGANIZATIONAL_UNIT
        , ACCOUNT
        },                          ELEMENT_GROUP_LINK)
    , ROLELINK         (new Grammar[]
        { ORGANIZATION
        , ORGANIZATIONAL_UNIT
        , ACCOUNT
        },                          ELEMENT_ROLE_LINK)
    , ATTRIBUTE          (new Grammar[]
        { DISTINGUISHED_NAMES
        , BINARY
        , MULTIVALUED
        },                          ELEMENT_ATTRIBUTE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:8524985031639050365")
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
      switch (state) {
        case FEATURE             : processFeature(attributes);
                                   break;
        case SERVER              : processServer(attributes);
                                   break;
        case ROLE                : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ROLE_OBJECT_CLASS,                 attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ROLE_OBJECT_PREFIX,                attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ROLE_OBJECTMEMBER_ATTRIBUTE,       attributes.getValue(ATTRIBUTE_OBJECT_MEMBER));
                                   break;
        case GROUP               : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.GROUP_OBJECT_CLASS,                attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.GROUP_OBJECT_PREFIX,               attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.GROUP_OBJECTMEMBER_ATTRIBUTE,      attributes.getValue(ATTRIBUTE_OBJECT_MEMBER));
                                   break;
        case DOMAIN              : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.DOMAIN_OBJECT_CLASS,               attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.DOMAIN_OBJECT_PREFIX,              attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case COUNTRY             : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.COUNTRY_OBJECT_CLASS,              attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.COUNTRY_OBJECT_PREFIX,             attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case LOCALITY            : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.LOCALITY_OBJECT_CLASS,             attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.LOCALITY_OBJECT_PREFIX,            attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case ORGANIZATION        : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATION_OBJECT_CLASS,         attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATION_OBJECT_PREFIX,        attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case ORGANIZATIONAL_UNIT : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATIONALUNIT_OBJECT_CLASS,   attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATIONALUNIT_OBJECT_PREFIX,  attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case ACCOUNT             : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_OBJECT_CLASS,              attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_OBJECT_PREFIX,             attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_PASSWORD_ATTRIBUTE,        attributes.getValue(ATTRIBUTE_PASSWORD));
                                   break;
        case ENTERPRISE_DATABASE : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_DATABASE_OBJECT_CLASS,  attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_DATABASE_OBJECT_PREFIX, attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case ENTERPRISE_DOMAIN   : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_DOMAIN_OBJECT_CLASS,    attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_DOMAIN_OBJECT_PREFIX,   attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case ENTERPRISE_SCHEMA   : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_CLASS,    attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_SCHEMA_OBJECT_PREFIX,   attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   break;
        case ENTERPRISE_ROLE     : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_ROLE_OBJECT_CLASS,      attributes.getValue(ATTRIBUTE_OBJECT_CLASS));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_ROLE_OBJECT_PREFIX,     attributes.getValue(ATTRIBUTE_OBJECT_PREFIX));
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ENTERPRISE_ROLE_MEMBER_ATTRIBUTE,  attributes.getValue(ATTRIBUTE_OBJECT_MEMBER));
                                   break;
        case MULTIVALUED         : List<String> list = new ArrayList<String>();
                                   switch (this.state.peek()) {
                                     case DOMAIN              : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.DOMAIN_MULTIVALUE_ATTRIBUTE, list);
                                                                break;
                                     case LOCALITY            : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.LOCALITY_MULTIVALUE_ATTRIBUTE, list);
                                                                break;
                                     case COUNTRY             : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.COUNTRY_MULTIVALUE_ATTRIBUTE, list);
                                                                break;
                                     case ORGANIZATION        : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATION_MULTIVALUE_ATTRIBUTE, list);
                                                                break;
                                     case ORGANIZATIONAL_UNIT : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATIONALUNIT_MULTIVALUE_ATTRIBUTE, list);
                                                                break;
                                     case ACCOUNT             : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_MULTIVALUE_ATTRIBUTE, list);
                                                                break;
                                   }
                                   push(list);
                                   break;
        case GROUPLINK            : switch (this.state.peek()) {
                                     case ORGANIZATION        : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATION_GROUPMEMBER_ATTRIBUTE,          attributes.getValue(ATTRIBUTE_ATTRIBUTE));
                                                                DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATION_GROUPMEMBER_ATTRIBUTE_DN,       attributes.getValue(ATTRIBUTE_DN));
                                                                break;
                                     case ORGANIZATIONAL_UNIT : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATIONALUNIT_GROUPMEMBER_ATTRIBUTE,    attributes.getValue(ATTRIBUTE_ATTRIBUTE));
                                                                DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATIONALUNIT_GROUPMEMBER_ATTRIBUTE_DN, attributes.getValue(ATTRIBUTE_DN));
                                                                break;
                                     case ACCOUNT             : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE,               attributes.getValue(ATTRIBUTE_ATTRIBUTE));
                                                                DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_GROUPMEMBER_ATTRIBUTE_DN,            attributes.getValue(ATTRIBUTE_DN));
                                                                break;
                                   }
                                   break;
        case ROLELINK            : switch (this.state.peek()) {
                                     case ORGANIZATION        : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATION_ROLEMEMBER_ATTRIBUTE,           attributes.getValue(ATTRIBUTE_ATTRIBUTE));
                                                                DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATION_ROLEMEMBER_ATTRIBUTE_DN,        attributes.getValue(ATTRIBUTE_DN));
                                                                break;
                                     case ORGANIZATIONAL_UNIT : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATIONALUNIT_ROLEMEMBER_ATTRIBUTE,    attributes.getValue(ATTRIBUTE_ATTRIBUTE));
                                                                DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ORGANIZATIONALUNIT_ROLEMEMBER_ATTRIBUTE_DN, attributes.getValue(ATTRIBUTE_DN));
                                                                break;
                                     case ACCOUNT             : DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE,               attributes.getValue(ATTRIBUTE_ATTRIBUTE));
                                                                DirectoryFeatureFactory.this.feature.put(DirectoryConstant.ACCOUNT_ROLEMEMBER_ATTRIBUTE_DN,            attributes.getValue(ATTRIBUTE_DN));
                                                                break;
                                   }
                                   break;
        default                  : break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: endElement (overridden)
    /**
     ** Receive notification of the end of an element.
     ** <p>
     ** The SAX parser will invoke this method at the end of every element in
     ** the XML document; there will be a corresponding
     ** {@link #startElement(String, String, String, Attributes) startElement} event for every endElement event
     ** (even when the element is empty).
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
        case MULTIVALUED         : pop();
        default                  : break;
      }
      // change FSA state
      this.cursor = this.state.pop();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: characters (overridden)
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
     ** @param  buffer           the characters from the XML document.
     ** @param  start            the start position in the array.
     ** @param  length           the number of characters to read from the
     **                          array.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     ** @see    #ignorableWhitespace
     ** @see    org.xml.sax.Locator
     */
    @Override
    @SuppressWarnings("unchecked")
    public void characters(final char[] buffer, final int start, final int length)
      throws SAXException {

      // where we are?
      switch (this.state.peek()) {
        case DISTINGUISHED_NAMES : List<String> list = DirectoryFeatureFactory.this.feature.distinguishedNames();
                                   if (list == null) {
                                     list = new ArrayList<String>();
                                     DirectoryFeatureFactory.this.feature.put(DirectoryConstant.DISTINGUISHED_NAMES, list);
                                   }
                                   list.add(String.copyValueOf(buffer, start, length));
                                   break;
        case BINARY              : final String exist = DirectoryFeatureFactory.this.feature.binaryObjectAttribute();
                                   final String value = StringUtility.isEmpty(exist) ? String.copyValueOf(buffer, start, length) : exist + DirectoryFeatureFactory.this.feature.multiValueSeparator() + String.copyValueOf(buffer, start, length);
                                   DirectoryFeatureFactory.this.feature.put(DirectoryConstant.BINARY_OBJECT_ATTRIBUTE, value);
                                   break;
        case MULTIVALUED         : break;
        case ATTRIBUTE           : ((List<String>)peek()).add(String.copyValueOf(buffer, start, length));
                                   break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   processFeature
    /**
     ** Factory method to fetch the <code>Feature</code> attributes from a
     ** XML stream.
     **
     ** @throws SAXException       if the required attribute name has no value.
     */
    private void processFeature(final Attributes attributes)
      throws SAXException {

      for (int i = 0; i < attributes.getLength(); i++) {
        // obtain the local name of the attribute from the underlying XML stream
        final String external = attributes.getLocalName(i);
        // an empty local name occurs only if we have a namespace declaration in
        // a namespace aware parser
        if (StringUtility.isEmpty(external))
          continue;
        // verify the declared namespace
        if (ATTRIBUTE_NAMESPACE.equals(external)) {
          final String value = attributes.getValue(i);
          if (!NAMESPACE.equals(value)) {
            final int[]    position  = position();
            final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), external, value, ELEMENT_FEATURE };
            throw new SAXException(XMLStreamBundle.format(XMLError.NAMESPACE_INVALID, arguments));
          }
          // skip any namespace declaration so far
          continue;
        }
        if (!registry.contains(external)) {
          final int[]    position  = position();
          final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), external, ELEMENT_FEATURE };
          throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_UNKNOWN, arguments));
        }
        DirectoryFeatureFactory.this.feature.put(external, attributes.getValue(i));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   processServer
    /**
     ** Factory method to fetch the <code>Server</code> attributes from a
     ** XML stream.
     */
    private void processServer(final Attributes attributes) {
      List<DirectoryServer> exist = DirectoryFeatureFactory.this.feature.failoverConfiguration();
      if (exist == null) {
        exist = new ArrayList<DirectoryServer>();
        DirectoryFeatureFactory.this.feature.put(DirectoryConstant.FAILOVER_CONFIGURATION, exist);
      }
      exist.add(DirectoryServer.build(attributes.getValue(ATTRIBUTE_HOST), Integer.parseInt(attributes.getValue(ATTRIBUTE_PORT))));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryFeatureFactory</code>.
   **
   ** @param  feature            the {@link DirectoryFeature} that this factory
   **                            will configure.
   */
  private DirectoryFeatureFactory(final DirectoryFeature feature) {
    // ensure inheritance
    super();

    // initialize instance
    this.feature = feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from a
   ** {@link File}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link XMLProcessor}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final File file)
    throws DirectoryException {

    configure(feature, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from a
   ** {@link File}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link XMLProcessor}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final File file, final boolean validate)
    throws DirectoryException {

    try {
      configure(feature, new FileInputStream(file), validate);
    }
    catch (IOException e) {
      throw new DirectoryException(DirectoryError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link XMLProcessor}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final InputStream stream)
    throws DirectoryException {

    configure(feature, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to create a {@link DirectoryFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link XMLProcessor}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final InputStream stream, final boolean validate)
    throws DirectoryException {

    // prevent bogus input
    if (feature == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "feature");

    if (stream == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "stream");

    configure(feature, new InputSource(stream), validate);
    try {
      stream.close();
    }
    catch (IOException e) {
      throw new DirectoryException(DirectoryError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link XMLProcessor}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final InputSource source)
    throws DirectoryException {

    configure(feature, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link XMLProcessor}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final InputSource source, final boolean validate)
    throws DirectoryException {

    // prevent bogus input
    if (feature == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "feature");

    if (source == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "source");

    final DirectoryFeatureFactory factory = new DirectoryFeatureFactory(feature);
    // validate the provided soource against the schema if requested
    if (validate) {
      try {
        factory.validate(DirectoryFeature.class, source);
      }
      catch (XMLException e) {
        throw new DirectoryException(DirectoryError.ABORT, e);
      }
    }
    factory.configure(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link XMLProcessor}.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final PDocument document)
    throws DirectoryException {

    // prevent bogus input
    if (document == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "document");

    final DirectoryFeatureFactory factory = new DirectoryFeatureFactory(feature);
    try {
      factory.configure(document.read());
    }
    catch (MDSIOException e) {
      throw new DirectoryException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws DirectoryException in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private void configure(final InputSource source)
    throws DirectoryException {

    // prevent bogus input
    if (source == null)
      throw new DirectoryException(DirectoryError.ARGUMENT_IS_NULL, "source");

    try {
      final Parser parser = new Parser();
      parser.processDocument(source);
    }
    catch (SAXException e) {
      throw new DirectoryException(DirectoryError.ABORT, e);
    }
  }
}
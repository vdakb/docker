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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   RedHat KeyCloak Connector

    File        :   ServiceFeatureParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceFeatureParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-04-06  SBernet    First release version
*/

package oracle.iam.identity.gws.integration.keycloak;

import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.XMLError;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.iam.identity.connector.integration.FeatureParser;

////////////////////////////////////////////////////////////////////////////////
// final class ServiceFeatureParser
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to unmarshalls meta information of for a Service Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
final class ServiceFeatureParser extends FeatureParser {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private  static final String ELEMENT_PROPERTY = "property";

  private static final String  ATTRIBUTE_NAME   = "name";
  private static final String  ATTRIBUTE_VALUE  = "value";

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
      INIT    (new Grammar[0], "")
    , FEATURE (INIT,    ELEMENT_FEATURE)
    , PROPERTY(FEATURE, ELEMENT_PROPERTY)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

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
     **                          <br>
     **                          Allowed object is {@link Grammar}.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
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
     **                          <br>
     **                          Allowed object is array {@link Grammar}s.
     ** @param  tag              the logical name of this grammar state to
     **                          lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
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
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Grammar</code> for that tag.
     **                          <br>
     **                          Possible object is {@link Grammar}.
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
     **                          <br>
     **                          Allowed object is {@link Grammar}.
     **
     ** @return
     **                          <br>
     **                          Allowed object is <code>boolean</code>
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
   ** Constructor for <code>ServiceFeatureParser</code>.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured
   **                            by this {@link FeatureFactory.Parser}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   **
   ** @throws SAXException       in case {@link SAXInput} is not able to create
   **                            an appropriate parser.
   */
  protected ServiceFeatureParser(final ServiceFeature feature)
    throws SAXException {

    // ensure inheritance
    super(feature, Service.PROPERTY);
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attributes         the attributes attached to the element.
   **                            If there are no attributes, it shall be an
   **                            empty {@link Attributes} object. The value of
   **                            this object after startElement returns is
   **                            undefined.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
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
    switch (this.cursor) {
      case FEATURE     : processFeature(attributes);
                         break;
      case PROPERTY    : processProperty(attributes);
                         break;
      default          : break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
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
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   */
  @Override
  public void endElement(final String uri, final String localName, final String qualifiedName)
    throws SAXException {

    // change FSA state
    this.cursor = this.state.pop();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processProperty
  /**
   ** Factory method to fetch the <code>Feature</code> properties from a
   ** XML stream.
   **
   ** @param  attributes         the {@link Attributes} providing access to a
   **                            the property configuration element.
   **                            <br>
   **                            Allowed object is {@link Attributes}.
   **
   ** @throws SAXException       if the required properties name has no value.
   */
  protected void processProperty(final Attributes attributes)
    throws SAXException {

    final String external = attributes.getValue(ATTRIBUTE_NAME);
    if (Service.PROPERTY.contains(external))
      this.feature.put(external, attributes.getValue(ATTRIBUTE_VALUE));
    else {
      final int[]    position  = position();
      final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), external, ELEMENT_PROPERTY };
      throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_UNKNOWN, arguments));
    }
  }
}
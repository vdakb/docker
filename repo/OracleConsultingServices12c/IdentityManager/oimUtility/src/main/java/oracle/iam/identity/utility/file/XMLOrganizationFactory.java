/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   XMLOrganizationFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    XMLOrganizationFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Stack;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.XMLError;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.offline.Entity;
import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntityListener;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.OrganizationEntity;

////////////////////////////////////////////////////////////////////////////////
// final class XMLOrganizationFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information for an attribute mapping used
 ** by a organization reconciliation task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public final class XMLOrganizationFactory extends XMLEntityFactory<OrganizationEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Grammar
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants of {@link XMLIdentityFactory}.
   */
  private enum Grammar {
      /** the encoded valid grammar transitions in the parser. */
      INIT         (new Grammar[0], "")
    , ORGANIZATIONS(INIT,          OrganizationEntity.MULTIPLE)
    , ORGANIZATION (ORGANIZATIONS, OrganizationEntity.SINGLE)
    , ROLE         (ORGANIZATION,  RoleEntity.SINGLE)
    , APPLICATION  (ORGANIZATION,  ApplicationEntity.SINGLE)
    , NAMESPACE    (APPLICATION,   ApplicationEntity.MULTIPLE)
    , ENTITLEMENT  (NAMESPACE,     EntitlementEntity.SINGLE)
    , ATTRIBUTE    (
        new Grammar[] {
          ORGANIZATION
        , ROLE
        , APPLICATION
        , ENTITLEMENT
        },                      EntityFactory.ELEMENT_ATTRIBUTE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4299519797768190874")
    static final long                 serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** array of parent states to this one. */
    final Grammar                     parents[];

    /** the name of the tag for this state. */
    final String                      tag;

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
      this(new Grammar[] { parent }, tag);
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
      this.tag = tag;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: lookup
    /**
     ** Lookups the <code>Grammar</code> for the specified a tag name.
     **
     ** @param  tag              the name of the tag the <code>Grammar</code> is
     **                          requested for.
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
     ** @param  grammar
     **
     ** @return
     */
    public boolean transition(final Grammar grammar) {
      if (this.equals(grammar))
        return true;

      for (int i = 0; i < grammar.parents.length; i++) {
        if (grammar.parents[i].equals(this))
          return true;
      }
      for (int i = 0; i < this.parents.length; i++) {
        if (this.parents[i].equals(grammar))
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
  private class Parser extends XMLEntityFactory<OrganizationEntity>.Parser {

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
     ** @throws SAXException     in case {@link XMLEntityFactory.Parser} is not
     **                          able to create an appropriate parser.
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
      switch (this.cursor) {
        case ORGANIZATIONS : push(new LinkedList<OrganizationEntity>());
                             break;
        case ORGANIZATION  : final OrganizationEntity organization = new OrganizationEntity(attributes.getValue(ATTRIBUTE_ID));
                             push(organization);
                             break;
        case ROLE          : unmarshalRole(attributes);
                             break;
        case APPLICATION   : unmarshalApplication(attributes);
                             break;
        case NAMESPACE     : unmarshalNamespace(attributes);
                             break;
        case ENTITLEMENT   : unmarshalEntitlement(attributes);
                             break;
        case ATTRIBUTE     : unmarshalAttribute(attributes);
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
    @SuppressWarnings("unchecked")
    public void endElement(final String uri, final String localName, final String qualifiedName)
      throws SAXException {

      // dispatch to handling method
      switch (this.cursor) {
        case ORGANIZATIONS : dispatchBulk();
                             break;
        case ORGANIZATION  : dispatchOrganization();
                             break;
        case ROLE          : dispatchRole();
                             break;
        case APPLICATION   : dispatchApplication();
                             break;
        case NAMESPACE     : dispatchEntitlements();
                             break;
        case ENTITLEMENT   : // put the entitlement provider and its attributes
                             // in the collection on top of the value stack by
                             // removing itself from the value stack
                             final EntitlementEntity entitlement = (EntitlementEntity)pop();
                             ((List<EntitlementEntity>)peek()).add(entitlement);
                             break;
        case ATTRIBUTE     : final StringBuilder builder = (StringBuilder)pop();
                             final String        id      = (String)pop();
                             ((Entity)peek()).addAttribute(id, builder.toString());
                             break;
      }
      // change FSA state
      this.cursor = this.state.pop();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: character (overridden)
    /**
     ** Receive notification of character data inside an element.
     ** <p>
     **
     ** @param buffer            the character buffer parsed so far.
     ** @param start             the start position in the character array.
     ** @param length            the number of characters to use from the
     **                          character array.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @Override
    public void characters(char buffer[], int start, int length) {
      switch (this.cursor) {
        case ATTRIBUTE : // if we have an attribute there must be a
                         // StringBuilder on the value stack
                         ((StringBuilder)peek()).append(buffer, start, length);
        default        : break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchRole
    /**
     ** Accordingly to the <code>Action</code> on the stack the parsed
     ** {@link RoleEntity} is dispatched to the correct collection of the
     ** {@link OrganizationEntity}.
     */
    private void dispatchRole()
      throws SAXException {

      final RoleEntity         role         = (RoleEntity)pop();
      final OrganizationEntity organization = (OrganizationEntity)peek();
      final List<RoleEntity>   collection   = organization.role();
      if (XMLOrganizationFactory.this.unique == Unique.IGNORE) {
        organization.addRole(role);
      }
      else if (!collection.contains(role) || XMLOrganizationFactory.this.unique == Unique.LAST) {
        organization.addRole(role);
      }
      else if (XMLOrganizationFactory.this.unique == Unique.STRONG) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), role.entity(), role.name() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_AMBIGUOUS, arguments));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchApplication
    /**
     ** Accordingly to the <code>Action</code> on the stack the parsed
     ** {@link ApplicationEntity} is dispatched to the correct collection of
     ** the {@link OrganizationEntity}.
     */
    private void dispatchApplication()
      throws SAXException {

      final ApplicationEntity       application  = (ApplicationEntity)pop();
      final OrganizationEntity      organization = (OrganizationEntity)peek();
      final List<ApplicationEntity> collection   = organization.application();
      if (XMLOrganizationFactory.this.unique == Unique.IGNORE) {
        organization.addApplication(application);
      }
      else if (!collection.contains(application) || XMLOrganizationFactory.this.unique == Unique.LAST) {
        organization.addApplication(application);
      }
      else if (XMLOrganizationFactory.this.unique == Unique.STRONG) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), application.entity(), application.name() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_AMBIGUOUS, arguments));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchEntitlements
    /**
     ** Accordingly to the <code>Action</code> on the stack the parsed set of
     ** {@link EntitlementEntity}'s is dispatched to the correct collection of
     ** the {@link ApplicationEntity}.
     */
    private void dispatchEntitlements() {
      @SuppressWarnings("unchecked")
      final List<EntitlementEntity> entitlement = (List<EntitlementEntity>)pop();
      final String                  id          = (String)pop();
      final ApplicationEntity       application = (ApplicationEntity)peek();
      application.add(id, entitlement);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchOrganization
    /**
     ** Invoke the associated {@link EntityListener} to process a particular
     ** bulk that is populated sor far from the source.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @SuppressWarnings("unchecked")
    private void dispatchOrganization()
      throws SAXException {

      final OrganizationEntity       organization = (OrganizationEntity)pop();
      final List<OrganizationEntity> bulk         = (List<OrganizationEntity>)peek();
      if (XMLOrganizationFactory.this.unique == Unique.IGNORE) {
        bulk.add(organization);
      }
      else if (!bulk.contains(organization) || XMLOrganizationFactory.this.unique == Unique.LAST) {
        bulk.add(organization);
      }
      else if (XMLOrganizationFactory.this.unique == Unique.STRONG) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), OrganizationEntity.SINGLE, organization.name() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_AMBIGUOUS, arguments));
      }

      if (bulk.size() >= XMLOrganizationFactory.this.listener.batchSize()) {
        dispatchBulk();
        push(new LinkedList<OrganizationEntity>());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchBulk
    /**
     ** Invoke the associated {@link EntityListener} to process a particular
     ** bulk that is populated so far from the source.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @SuppressWarnings("unchecked")
    private void dispatchBulk()
      throws SAXException {

      // put the identity provider and its roles/application/entitlements in the
      // collection by removing it from the value stack
      final List<OrganizationEntity> bulk = (List<OrganizationEntity>)pop();
      if (bulk.isEmpty())
        return;


      try {
        XMLOrganizationFactory.this.listener.process(bulk);
      }
      catch (TaskException e) {
        throw new SAXException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XMLOrganizationFactory</code> to populate identity data
   ** from a XML file with enforcement of unique names.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public XMLOrganizationFactory() {
    // ensure inheritance
    super(OrganizationEntity.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XMLOrganizationFactory</code> to populate identity data
   ** from a XML file with the specified enforcement of unique names.
   **
   ** @param  unique             the constraint how to handle dublicate names in
   **                            the source.
   */
  public XMLOrganizationFactory(final Unique unique) {
    // ensure inheritance
    super(OrganizationEntity.class, unique);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate (EntityFactory)
  /**
   ** Factory method to create an {@link OrganizationEntity}s from a
   ** {@link File}.
   ** <p>
   ** The {@link OrganizationEntity} will be validate accordingly to the state
   ** of <code>validate</code>. If <code>validate</code> is set to
   ** <code>true</code> the entire XML {@link File} is validated before the
   ** real parsing process is started. If this step should be skipped
   ** <code>false</code> has to e passed as the value for <code>validate</code>.
   **
   ** @param  listener           the {@link EntityListener} that will
   **                            process a particular batch of identities
   **                            populated from the {@link File}.
   ** @param  file               the input source for the
   **                            {@link OrganizationEntity}'s to reconcile by
   **                            unmarshalling the {@link File}.
   ** @param  validate           <code>true</code> if the provided {@link File}
   **                            has to be validated.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  @Override
  public void populate(final EntityListener<OrganizationEntity> listener, final File file, final boolean validate)
    throws TaskException {

    try {
      if (validate)
        validate(new FileInputStream(file));

      populate(listener, new FileInputStream(file));
    }
    catch (IOException e) {
      throw TaskException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Factory method to create an {@link OrganizationEntity} from an
   ** {@link InputStream}.
   ** <p>
   ** Unmarshal XML data from the specified XML {@link InputSource} by creating
   ** the appropriate {@link XMLOrganizationFactory.Parser} and populates the
   ** {@link OrganizationEntity} elements.
   **
   ** @param  listener           the {@link EntityListener} that will process
   **                            a particular batch of identities populated from
   **                            the {@link InputStream}.
   ** @param  stream             the input source for the
   **                            {@link OrganizationEntity}s to reconcile by
   **                            unmarshalling the underlying {@link InputStream}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void populate(final EntityListener<OrganizationEntity> listener, final InputStream stream)
    throws TaskException {

    // prevent bogus input
    if (stream == null)
      throw TaskException.argumentIsNull("stream");

    try {
      populate(listener, new InputSource(stream));
    }
    finally {
      try {
        stream.close();
      }
      catch (IOException e) {
        throw TaskException.abort(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Factory method to create an {@link OrganizationEntity} from an
   ** {@link InputSource}.
   **
   ** @param  listener           the {@link EntityListener} that will
   **                            process a particular batch of lookups populated
   **                            from the {@link InputSource}.
   ** @param  source             the input source for the
   **                            {@link OrganizationEntity}s to reconcile by
   **                            unmarshalling the underlying
   **                            {@link InputSource}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void populate(final EntityListener<OrganizationEntity> listener, final InputSource source)
    throws TaskException {

    // prevent bogus input
    if (listener == null)
      throw TaskException.argumentIsNull("EntityListener");

    if (source == null)
      throw TaskException.argumentIsNull("InputSource");

    this.listener = listener;
    try {
      final Parser parser = new Parser();
      parser.processDocument(source);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }
}
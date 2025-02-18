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

    File        :   XMLCatalogFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    XMLCatalogFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.XMLError;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.CatalogEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntityListener;
import oracle.iam.identity.foundation.offline.CatalogListener;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationEntity;

////////////////////////////////////////////////////////////////////////////////
// final class XMLCatalogFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information for an attribute mapping used
 ** by a catalog reconciliation task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public final class XMLCatalogFactory extends XMLEntityFactory<CatalogEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the namespace definition of the catalog interface */
  public static final String NAMESPACE       = "http://www.oracle.com/schema/oim/catalog";

  /** the schema definition to be used if catalog items needs to be exported */
  public static final String SCHEMA_LOCATION = NAMESPACE + " CatalogEntity.xsd";

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
      INIT        (new Grammar[0], "")
    , CATALOG     (INIT,         CatalogEntity.SINGLE)
    , ROLES       (CATALOG,      RoleEntity.MULTIPLE)
    , ROLE        (ROLES,        RoleEntity.SINGLE)
    , APPLICATIONS(CATALOG,      ApplicationEntity.MULTIPLE)
    , APPLICATION (APPLICATIONS, ApplicationEntity.SINGLE)
    , NAMESPACE   (APPLICATION,  EntitlementEntity.MULTIPLE)
    , ENTITLEMENT (NAMESPACE,    EntitlementEntity.SINGLE)
    , ATTRIBUTE  (
        new Grammar[] {
          ROLE
        , APPLICATION
        , ENTITLEMENT
        },                       EntityFactory.ELEMENT_ATTRIBUTE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:8150300804035635370")
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
  private class Parser extends XMLEntityFactory<CatalogEntity>.Parser {

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
        case CATALOG      : push(new CatalogEntity());
                            break;
        case ROLE         : unmarshalRole(attributes);
                            break;
        case APPLICATION  : unmarshalApplication(attributes);
                            break;
        case NAMESPACE    : unmarshalNamespace(attributes);
                            break;
        case ENTITLEMENT  : unmarshalEntitlement(attributes);
                            break;
        case ATTRIBUTE    : unmarshalAttribute(attributes);
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
    public void endElement(final String uri, final String localName, final String qualifiedName)
      throws SAXException {

      // dispatch to handling method
      switch (this.cursor) {
        case CATALOG      : dispatchCatalog();
                            break;
        case ROLE         : dispatchRole();
                            break;
        case ROLES        : processRole();
                            break;
        case APPLICATION  : dispatchInstance();
                            break;
        case APPLICATIONS : processInstance();
                            break;
        case ENTITLEMENT  : dispatchEntitlement();
                            break;
        case NAMESPACE    : processNamespace();
                            break;
        case ATTRIBUTE    : dispatchAttribute();
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
        case ATTRIBUTE : // if we have an attribute than there must be a
                         // StringBuilder on the value stack
                         ((StringBuilder)peek()).append(buffer, start, length);
        default        : break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchCatalog
    /**
     ** Accordingly to the requested batch size ....
     **
     ** @throws SAXException     if the {@link CatalogEntity} to dispatch is
     **                          already dispatched.
     */
    private void dispatchCatalog()
      throws SAXException {

      final Set<CatalogEntity> bulk = new HashSet<CatalogEntity>();
      bulk.add(((CatalogEntity)pop()));
      try {
        XMLCatalogFactory.this.listener.process(bulk);
      }
      catch (TaskException e) {
        throw new SAXException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchRole
    /**
     ** Accordingly to the requested batch size ....
     **
     ** @throws SAXException     if the {@link RoleEntity} to dispatch is
     **                          already dispatched.
     */
    private void dispatchRole()
      throws SAXException {

      final RoleEntity      role        = (RoleEntity)pop();
      final CatalogEntity   catalog     = (CatalogEntity)peek();
      final List<RoleEntity> collection = catalog.role();
      if (XMLCatalogFactory.this.unique == Unique.IGNORE)
        catalog.addRole(role);
      else if (!collection.contains(role) || XMLCatalogFactory.this.unique == Unique.LAST) {
        catalog.addRole(role);
      }
      else if (XMLCatalogFactory.this.unique == Unique.STRONG) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), role.entity(), role.name() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_AMBIGUOUS, arguments));
      }

      if (catalog.role().size() >= XMLCatalogFactory.this.listener.batchSize())
        processRole();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: processRole
    /**
     ** Accordingly to the requested batch size ....
     **
     ** @throws SAXException     if the {@link RoleEntity} to dispatch is
     **                          already dispatched.
     */
    private void processRole()
      throws SAXException {

      final CatalogEntity catalog = (CatalogEntity)peek();
      try {
        ((CatalogListener)XMLCatalogFactory.this.listener).processRole(catalog.role());
        catalog.role().clear();
      }
      catch (TaskException e) {
        throw new SAXException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchInstance
    /**
     ** Accordingly to the requested batch size ....
     **
     ** @throws SAXException     if the {@link ApplicationEntity} to dispatch
     **                          is already dispatched.
     */
    private void dispatchInstance()
      throws SAXException {

      final ApplicationEntity       instance   = (ApplicationEntity)pop();
      final CatalogEntity           catalog    = (CatalogEntity)peek();
      final List<ApplicationEntity> collection = catalog.application();
      if (XMLCatalogFactory.this.unique == Unique.IGNORE)
        catalog.addApplication(instance);
      else if (!collection.contains(instance) || XMLCatalogFactory.this.unique == Unique.LAST) {
        catalog.addApplication(instance);
      }
      else if (XMLCatalogFactory.this.unique == Unique.STRONG) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), instance.entity(), instance.name() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_AMBIGUOUS, arguments));
      }

      if (catalog.application().size() >= XMLCatalogFactory.this.listener.batchSize())
        processInstance();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: processInstance
    /**
     ** Accordingly to the requested batch size ....
     **
     ** @throws SAXException     if the {@link ApplicationEntity} to dispatch
     **                          is already dispatched.
     */
    private void processInstance()
      throws SAXException {

      final CatalogEntity catalog = (CatalogEntity)peek();
      try {
        ((CatalogListener)XMLCatalogFactory.this.listener).processInstance(catalog.application());
        catalog.application().clear();
      }
      catch (TaskException e) {
        throw new SAXException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchEntitlement
    /**
     ** Accordingly to the <code>Namespace</code> on the stack the parsed set of
     ** {@link EntitlementEntity} is dispatched to the superior
     ** {@link ApplicationEntity}.
     */
    private void dispatchEntitlement()
      throws SAXException {

      // put the entitlement provider and its attributes in the application on
      // top of the value stack by removing itself from the value stack
      final EntitlementEntity       entitlement = (EntitlementEntity)pop();
      final String                  id          = (String)pop();
      final ApplicationEntity       instance    = (ApplicationEntity)peek();
      final List<EntitlementEntity> collection  = instance.entitlement(id);
      if (XMLCatalogFactory.this.unique == Unique.IGNORE)
        instance.add(id, entitlement);
      else if (!collection.contains(instance) || XMLCatalogFactory.this.unique == Unique.LAST) {
        instance.add(id, entitlement);
      }
      else if (XMLCatalogFactory.this.unique == Unique.STRONG) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), entitlement.entity(), entitlement.name() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_AMBIGUOUS, arguments));
      }

      if (collection.size() >= XMLCatalogFactory.this.listener.batchSize()) {
        processNamespace(instance, id);
      }
      // push back the namespace id on top of the value stack to maintain the
      // rest of the entitlements correctly that needs still to be parsed
      push(id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: processNamespace
    /**
     ** Accordingly to the requested batch size ....
     **
     ** @throws SAXException     if the {@link ApplicationEntity} to dispatch
     **                          is already dispatched.
     */
    private void processNamespace()
      throws SAXException {

      final String namespace = (String)pop();
      processNamespace((ApplicationEntity)peek(), namespace);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: processNamespace
    /**
     ** Accordingly to the requested batch size ....
     **
     ** @throws SAXException     if the {@link ApplicationEntity} to dispatch
     **                          is already dispatched.
     */
    private void processNamespace(final ApplicationEntity instance, final String namespace)
      throws SAXException {

      try {
        ((CatalogListener)XMLCatalogFactory.this.listener).processEntitlement(instance, namespace);
        instance.entitlement(namespace).clear();
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
   ** Constructs a <code>XMLCatalogFactory</code> to populate catalog data from
   ** a XML file with enforcement of unique names.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public XMLCatalogFactory() {
    // ensure inheritance
    super(CatalogEntity.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XMLCatalogFactory</code> to populate identity data
   ** from a XML file with the specified enforcement of unique names.
   **
   ** @param  unique             the constraint how to handle dublicate names in
   **                            the source.
   */
  public XMLCatalogFactory(final Unique unique) {
    // ensure inheritance
    super(CatalogEntity.class, unique);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate (EntityFactory)
  /**
   ** Factory method to populate {@link CatalogEntity} items from a XML
   ** {@link File}.
   ** <p>
   ** The {@link CatalogEntity} items will be validate accordingly to the
   ** state of <code>validate</code>. If <code>validate</code> is set to
   ** <code>true</code> the entire XML {@link File} is validated before the
   ** real parsing process is started. If this step should be skipped
   ** <code>false</code> has to be passed as the value for <code>validate</code>.
   **
   ** @param  listener           the {@link CatalogListener} that will process a
   **                            particular batch of catalog items populated
   **                            from the {@link File}.
   ** @param  file               the input source for the
   **                            {@link CatalogEntity} to synchronize by
   **                            unmarshalling the {@link File}.
   ** @param  validate           <code>true</code> if the provided {@link File}
   **                            has to be validated.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  @Override
  public void populate(final EntityListener<CatalogEntity> listener, final File file, final boolean validate)
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
   ** Factory method to create an {@link CatalogEntity} items from an
   ** {@link InputStream}.
   **
   ** @param  listener           the {@link CatalogListener} that will process a
   **                            particular batch of lookups populated from the
   **                            {@link InputStream}.
   ** @param  stream             the input source for the
   **                            {@link CatalogEntity} to synchronize by
   **                            unmarshalling the {@link InputStream}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void populate(final EntityListener<CatalogEntity> listener, final InputStream stream)
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
   ** Factory method to create an {@link CatalogEntity} items from an
   ** {@link InputSource}.
   ** <p>
   ** Parse the {@link InputSource} by creating the appropriate
   ** {@link XMLCatalogFactory.Parser} and populates the {@link CatalogEntity}
   ** elements.
   **
   ** @param  listener           the {@link CatalogListener} that will process a
   **                            particular batch of lookups populated from the
   **                            {@link InputSource}.
   ** @param  source             the input source for the
   **                            {@link CatalogEntity} to synchronize by
   **                            unmarshalling the {@link InputSource}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void populate(final EntityListener<CatalogEntity> listener, final InputSource source)
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
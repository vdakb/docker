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

    File        :   XMLAccountFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    XMLAccountFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.util.LinkedList;

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

import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntityListener;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.ApplicationAccount;

////////////////////////////////////////////////////////////////////////////////
// final class XMLAccountFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information for an attribute mapping used
 ** by an appplication account reconciliation task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public final class XMLAccountFactory extends XMLEntityFactory<ApplicationAccount> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Grammar
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants of {@link XMLAccountFactory}.
   */
  private enum Grammar {
      /** the encoded valid grammar transitions in the parser. */
      INIT        (new Grammar[0], "")
    , APPLICATION (INIT,        ApplicationEntity.SINGLE)
    , ACCOUNT     (APPLICATION, ApplicationAccount.SINGLE)
    , ENTITLEMENTS(ACCOUNT,     EntitlementEntity.MULTIPLE)
    , ENTITLEMENT(ENTITLEMENTS, EntitlementEntity.SINGLE)
    , ATTRIBUTE  (
        new Grammar[] {
          ACCOUNT
        , ENTITLEMENT
        },                      EntityFactory.ELEMENT_ATTRIBUTE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Map<String, Grammar> lookup = new HashMap<String, Grammar>();

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-3632534342050765271")
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
  private class Parser extends XMLEntityFactory<ApplicationAccount>.Parser {

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
        case APPLICATION  : push(new LinkedList<ApplicationAccount>());
                            break;
        case ACCOUNT      : unmarshalAccount(attributes);
                            break;
        case ENTITLEMENTS : push(attributes.getValue(ATTRIBUTE_ID));
                            push(new LinkedList<EntitlementEntity>());
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
    @SuppressWarnings("unchecked")
    public void endElement(final String uri, final String localName, final String qualifiedName)
      throws SAXException {

      // dispatch to handling method
      switch (this.cursor) {
        case APPLICATION  : dispatchBulk();
                            break;
        case ACCOUNT      : dispatchAccount();
                            break;
        case ENTITLEMENTS : dispatchEntitlement();
                            break;
        case ENTITLEMENT  : // put the entitlement provider and its attributes
                            // in the collection on to of the value stack by
                            // removing itself from the value stack
                            final EntitlementEntity entitlement = (EntitlementEntity)pop();
                            ((List<EntitlementEntity>)peek()).add(entitlement);
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
    // Method: dispatchEntitlement
    /**
     ** Accordingly to the <code>Action</code> on the stack the parsed set of
     ** {@link EntitlementEntity} is dispatched to the correct collection of the
     ** {@link ApplicationAccount}.
     */
    @SuppressWarnings("unchecked")
    private void dispatchEntitlement() {
      final List<EntitlementEntity> entitlement = (List<EntitlementEntity>)pop();
      final String                  id          = (String)pop();
      final ApplicationAccount      account     = (ApplicationAccount)peek();
      account.add(id, entitlement);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchAccount
    /**
     ** Invoke the associated {@link EntityListener} to process a
     ** particular account that is populated so far from the source.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    @SuppressWarnings("unchecked")
    private void dispatchAccount()
      throws SAXException {

      final ApplicationAccount       account = (ApplicationAccount)pop();
      final List<ApplicationAccount> bulk    = (List<ApplicationAccount>)peek();
      if (XMLAccountFactory.this.unique == Unique.IGNORE) {
        bulk.add(account);
      }
      else if (!bulk.contains(account) || XMLAccountFactory.this.unique == Unique.LAST) {
        bulk.add(account);
      }
      else if (XMLAccountFactory.this.unique == Unique.STRONG) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ApplicationAccount.SINGLE, account.name() };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ELEMENT_AMBIGUOUS, arguments));
      }

      if (bulk.size() >= XMLAccountFactory.this.listener.batchSize()) {
        dispatchBulk();
        push(new LinkedList<ApplicationAccount>());
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

      // put the account provider and its application/entitlements in the
      // collection by removing it from the value stack
      final List<ApplicationAccount> bulk = (List<ApplicationAccount>)pop();
      if (bulk.isEmpty())
        return;

      try {
        XMLAccountFactory.this.listener.process(bulk);
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
   ** Constructs a <code>XMLAccountFactory</code> to populate account data from
   ** a XML file with enforcement of unique names.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public XMLAccountFactory() {
    // ensure inheritance
    super(ApplicationAccount.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XMLAccountFactory</code> to populate identity data
   ** from a XML file with the specified enforcement of unique names.
   **
   ** @param  unique             the constraint how to handle dublicate names in
   **                            the source.
   */
  public XMLAccountFactory(final Unique unique) {
    // ensure inheritance
    super(ApplicationAccount.class, unique);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate (EntityFactory)
  /**
   ** Factory method to populate {@link ApplicationAccount}s from a XML
   ** {@link File}.
   ** <p>
   ** The {@link ApplicationAccount}s will be validate accordingly to the state
   ** of <code>validate</code>. If <code>validate</code> is set to
   ** <code>true</code> the entire XML {@link File} is validated before the
   ** real parsing process is started. If this step should be skipped
   ** <code>false</code> has to e passed as the value for <code>validate</code>.
   **
   ** @param  listener           the {@link EntityListener} that will process a
   **                            particular batch of accounts populated from the
   **                            {@link File}.
   ** @param  file               the input source for the
   **                            {@link ApplicationAccount} to reconcile by
   **                            unmarshalling the {@link File}.
   ** @param  validate           <code>true</code> if the provided {@link File}
   **                            has to be validated.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void populate(final EntityListener<ApplicationAccount> listener, final File file, final boolean validate)
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
   ** Factory method to create an {@link ApplicationAccount} from an
   ** {@link InputStream}.
   **
   ** @param  listener           the {@link EntityListener} that will process a
   **                            particular batch of accounts populated from the
   **                            {@link InputStream}.
   ** @param  stream             the input source for the
   **                            {@link ApplicationAccount} to reconcile by
   **                            unmarshalling the {@link InputStream}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void populate(final EntityListener<ApplicationAccount> listener, final InputStream stream)
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
   ** Factory method to create an {@link ApplicationAccount} from an
   ** {@link InputSource}.
   ** <p>
   ** Parse the {@link InputSource} by creating the appropriate
   ** {@link XMLAccountFactory.Parser} and populates the
   ** {@link ApplicationAccount} elements.
   **
   ** @param  listener           the {@link EntityListener} that will process a
   **                            particular batch of accounts populated from the
   **                            {@link InputSource}.
   ** @param  source             the input source for the
   **                            {@link ApplicationAccount} to reconcile by
   **                            unmarshalling the {@link InputSource}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void populate(final EntityListener<ApplicationAccount> listener, final InputSource source)
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
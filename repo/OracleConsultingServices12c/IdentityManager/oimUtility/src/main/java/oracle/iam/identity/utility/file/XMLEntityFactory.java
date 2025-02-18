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

    File        :   XMLEntityFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    XMLEntityFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.io.InputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.SAXInput;
import oracle.hst.foundation.xml.XMLCodec;
import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.XMLException;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.xml.XMLProcessor;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.offline.Entity;
import oracle.iam.identity.foundation.offline.RoleEntity;
import oracle.iam.identity.foundation.offline.EntityFactory;
import oracle.iam.identity.foundation.offline.EntityListener;
import oracle.iam.identity.foundation.offline.EntitlementEntity;
import oracle.iam.identity.foundation.offline.ApplicationEntity;
import oracle.iam.identity.foundation.offline.ApplicationAccount;

////////////////////////////////////////////////////////////////////////////////
// abstract class XMLEntityFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information for an attribute mapping used
 ** by a reconciliation task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public abstract class XMLEntityFactory<E extends Entity> extends    XMLProcessor
                                                         implements EntityFactory<E> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the resource location to pick up the XML schema definition. */
  protected final Class<E>    schema;

  /** the constraint how to handle dublicate names in the source. */
  protected Unique            unique;

  /** the listener notified to process a particular bulk of {@link Entity}'s. */
  protected EntityListener<E> listener;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** Handles parsing of a XML file which defines the mapping descriptor.
   */
  protected class Parser extends SAXInput {

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
    protected Parser()
      throws SAXException {

      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalRole
    /**
     ** Starts unmarshalling of a {@link RoleEntity} from the XML stream.
     **
     ** @param  attributes       the attributes attached to the role element.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    protected void unmarshalRole(final Attributes attributes)
      throws SAXException {

      final String     id     = unmarshalIdentifier(attributes);
      final RoleEntity role   = new RoleEntity(id);
      unmarshalAttribute(role, attributes);
      push(role);
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalApplication
    /**
     ** Starts unmarshalling of an {@link ApplicationEntity} from the XML
     ** stream.
     **
     ** @param  attributes       the attributes attached to the entitlement
     **                          element.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     */
    protected void unmarshalApplication(final Attributes attributes)
      throws SAXException {

      final String            id          = unmarshalIdentifier(attributes);
      final ApplicationEntity application = new ApplicationEntity(id);
      unmarshalAttribute(application, attributes);
      push(application);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalAccount
    /**
     ** Starts unmarshalling of an {@link ApplicationAccount} from the XML
     ** stream.
     **
     ** @param  attributes       the attributes attached to the application
     **                          element.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception
     */
    protected void unmarshalAccount(final Attributes attributes)
      throws SAXException {

      final String             id      = unmarshalIdentifier(attributes);
      final ApplicationAccount account = new ApplicationAccount(id);

      // narrow down the action requested for the account
      final String action = StringUtility.collapseWhitespace(attributes.getValue(ATTRIBUTE_ACTION));
      try {
        account.action(action);
      }
      catch (TaskException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_ACTION, attributes.getValue(ATTRIBUTE_ACTION) };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
      }

      push(account);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalNamespace
    /**
     ** Starts unmarshalling a namespace of an {@link EntitlementEntity} from
     ** the XML stream.
     **
     ** @param  attributes       the attributes attached to the entitlement
     **                          element.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     */
    protected void unmarshalNamespace(final Attributes attributes)
      throws SAXException {

      final String            id       = unmarshalIdentifier(attributes);
      // a entitlement namespace can only be exists within an application hence
      // an entity of this type mus be on the top of the value stack
      final ApplicationEntity instance = (ApplicationEntity)peek();
      instance.namespace(id);
      push(id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalEntitlement
    /**
     ** Starts unmarshalling of an {@link EntitlementEntity} from the XML
     ** stream.
     **
     ** @param  attributes       the attributes attached to the entitlement
     **                          element.
     **
     ** @throws SAXException     any SAX exception, possibly wrapping another
     **                          exception.
     */
    protected void unmarshalEntitlement(final Attributes attributes)
      throws SAXException {

      final String            id          = unmarshalIdentifier(attributes);
      final EntitlementEntity entitlement = new EntitlementEntity(id);
      unmarshalAttribute(entitlement, attributes);
      push(entitlement);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalAttribute
    /**
     ** Fullfills unmarshalling of the <code>Attribute</code>s action and risk
     ** for the specified {@link EntitlementEntity} from the XML stream.
     **
     ** @param  entity           the {@link EntitlementEntity} the specific
     **                          attributes has to be parsed.
     ** @param  attributes       the attributes attached to the
     **                          <code>Attribute</code> element.
     **
     ** @throws SAXException     if the paser detects that there is no
     **                          identifier for the attribute or the identifier
     **                          itself is empty.
     */
    protected void unmarshalAttribute(final EntitlementEntity entity, final Attributes attributes)
      throws SAXException {

      // narrow down the action requested for the role
      final String action = StringUtility.collapseWhitespace(attributes.getValue(ATTRIBUTE_ACTION));
      try {
        entity.action(action);
      }
      catch (TaskException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_ACTION, action };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
      }

      // narrow down the risk level requested for the role
      final String risk = StringUtility.collapseWhitespace(attributes.getValue(ATTRIBUTE_RISK));
      try {
        entity.risk(risk);
      }
      catch (TaskException e) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_RISK, risk };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_VALUE, arguments));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalAttribute
    /**
     ** Starts unmarshalling of an <code>Attribute</code> from the XML stream.
     **
     ** @param  attributes       the attributes attached to the
     **                          <code>Attribute</code> element.
     **
     ** @throws SAXException     if the paser detects that there is no
     **                          identifier for the attribute or the identifier
     **                          itself is empty.
     */
    protected void unmarshalAttribute(final Attributes attributes)
      throws SAXException {

      push(unmarshalIdentifier(attributes));
      push(new StringBuilder());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unmarshalIdentifier
    /**
     ** Unmarshalling of an <code>ID</code> from the XML stream.
     **
     ** @param  attributes       the attributes attached to the
     **                          <code>XML Node</code>.
     **
     ** @return                  the unmarshalled identifier.
     **
     ** @throws SAXException     if the paser detects that there is no
     **                          identifier od the identifier itself is
     **                          empty.
     */
    protected String unmarshalIdentifier(final Attributes attributes)
      throws SAXException {

      // XSD specifies the id of any element and attribute must exists
      String id = attributes.getValue(ATTRIBUTE_ID);
      if (StringUtility.isEmpty(id)) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_ID };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_MISSING, arguments));
      }

      // XSD specifies the id of any element and attribute is xsd:token hence we
      // have to collapse all whitespaces with the constraint that it has not be
      // empty afterwards
      id = StringUtility.collapseWhitespace(XMLCodec.instance().unescape(id));
      if (StringUtility.isEmpty(id)) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), ATTRIBUTE_ID };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_EMPTY, arguments));
      }
      return id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: dispatchAttribute
    /**
     ** Starts unmarshalling of an <code>Attribute</code> from the XML stream.
     **
     ** @throws SAXException     if the identifier of the attribute is already
     **                          dispatch to the entity on the value stack.
     **                          Unique Constraint Violation
     */
    protected void dispatchAttribute()
      throws SAXException {

      final StringBuilder builder = (StringBuilder)pop();
      final String        id      = (String)pop();
      final Entity        entity  = (Entity)peek();
      if (entity.attribute().containsKey(id)) {
        final int[]    position  = position();
        final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), id };
        throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_AMBIGUOUS, arguments));
      }
      // XSD specifies the value of an atttribute is xsd:token hence we have to
      // collapse all whitespaces
      final String value = StringUtility.collapseWhitespace(XMLCodec.instance().unescape(builder.toString()));
      // we adding only those attributes that have an information if the parsed
      // text is empty we skipp it.
      if (!StringUtility.isEmpty(value))
        entity.addAttribute(id, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XMLEntityFactory</code>.
   ** <p>
   ** Unique names are enforced to ensure that dublicate names contained in the
   ** source will stop any further execution.
   **
   ** @param  schema             the resource location to pick up the XML schema
   **                            definition.
   */
  protected XMLEntityFactory(final Class<E> schema) {
    // ensure inheritance
    this(schema, Unique.STRONG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>XMLEntityFactory</code>.
   **
   ** @param  schema             the resource location to pick up the XML schema
   **                            definition.
   ** @param  unique             the constraint how to handle dublicate names in
   **                            the source.
   */
  protected XMLEntityFactory(final Class<E> schema, final Unique unique) {
    // ensure inheritance
    super();

    // initialize instance
    this.schema = schema;
    this.unique = unique;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName (EntityFactory)
  /**
   ** Set the mode to enforce uniqueness.
   **
   ** @param  mode               the mode of {@link EntityFactory.Unique} to
   **                            set.
   */
  @Override
  public final void uniqueName(final Unique mode) {
    this.unique = mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uniqueName (EntityFactory)
  /**
   ** Return the mode to enforce uniqueness.
   **
   ** @return                    the mode of {@link EntityFactory.Unique} used.
   */
  @Override
  public final Unique uniqueName() {
    return this.unique;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates all {@link Entity} items provided by an {@link InputStream}.
   **
   ** @param  stream             the {@link InputStream} providing the content
   **                            to validate all {@link Entity} items.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void validate(final InputStream stream)
    throws TaskException {

    try {
      super.validate(this.schema, stream);
    }
    catch (XMLException e) {
      throw TaskException.abort(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates all {@link Entity} items provided by an {@link InputSource}.
   **
   ** @param  source             the {@link InputSource} providing the content
   **                            to validate all {@link Entity} items.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void validate(final InputSource source)
    throws TaskException {

    try {
      super.validate(this.schema, source);
    }
    catch (XMLException e) {
      throw TaskException.abort(e);
    }
  }
}
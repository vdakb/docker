/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   Schema.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Schema.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.io.InputStream;
import java.io.OutputStream;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

import oracle.hst.platform.core.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// abstract class Schema
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** The entity marshaling/unmarshaling scheme.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Schema {
  private static final String CLASS        = Schema.class.getName();
  private static final Logger LOGGER       = Logger.create(CLASS);
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  static final String            VIOLATED = "Schema violated for element [%1$s].";

  static final JsonWriterFactory factory  = Json.createWriterFactory(null);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** The schema descriptor for marshaling/unmarshaling generic attribute
   ** collections.
   */
  enum AttributeValue {
      /**
       ** The name of the element for a collection of generic attributes.
       */
      ID("attributeValues")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The element name. */
    public final String tag;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // enum Pair
    // ~~~~ ~~~~
    /**
     ** The schema descriptor for marshaling/unmarshaling generic tagged-value
     ** pairs.
     */
    public enum Pair {
        /**
         ** The name of the element for the identifier of a generic value pair.
         */
        ID("id"),

      /**
       ** The name of the element for the value of a generic pair of values.
       */
      VALUE("value")
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      /** The element name. */
      public final String tag;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructor for <code>Pair</code> element.
       **
       ** @param  tag              the element tag for this <code>Pair</code>.
       **                          <br>
       **                          Possible object is {@link String}.
       */
      Pair(final String tag) {
        this.tag = tag;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Attribute</code> with a single state.
     **
     ** @param  tag              the element tag for this
     **                          <code>Attribute</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    AttributeValue(final String tag) {
      this.tag = tag;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** The schema descriptor for marshaling/unmarshaling generic attribute
   ** collections.
   */
  enum Attribute {
      /**
       ** The name of the element for a collection of generic attributes.
       */
      ID("attributes")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The element name. */
    public final String tag;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // enum Pair
    // ~~~~ ~~~~
    /**
     ** The schema descriptor for marshaling/unmarshaling generic tagged-value
     ** pairs.
     */
    public enum Pair {
        /**
         ** The name of the element for the identifier of a generic value pair.
         */
        ID("id"),

      /**
       ** The name of the element for the value of a generic pair of values.
       */
      VALUE("value")
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      /** The element name. */
      public final String tag;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructor for <code>Pair</code> element.
       **
       ** @param  tag              the element tag for this <code>Pair</code>.
       **                          <br>
       **                          Possible object is {@link String}.
       */
      Pair(final String tag) {
        this.tag = tag;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Attribute</code> with a single state.
     **
     ** @param  tag              the element tag for this
     **                          <code>Attribute</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Attribute(final String tag) {
      this.tag = tag;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Application
  // ~~~~ ~~~~~~~~~~~
  /**
   ** The schema descriptor for marshaling/unmarshaling an application.
   */
  enum Application {
    /**
     ** The name of the entity element for a single application.
     */
    ID("application"),

    /**
     ** The name of the risk level element of an application.
     */
    RISK("risk"),

    /**
     ** The name of the account collection element for a single application.
     */
    ACCOUNT("accounts")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The element name. */
    public final String tag;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Application</code> element tag.
     **
     ** @param  tag              the element tag for this
     **                          <code>Application</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Application(final String tag) {
      this.tag = tag;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Account
  // ~~~~ ~~~~~~~
  /**
   ** The schema descriptor for marshaling/unmarshaling an account.
   */
  enum Account {
      /**
       ** The name of the identifier element for an account.
       */
      ID("id"),

    /**
     ** The type of the account.
     */
    TYPE("type"),

    /**
     ** The status of the account.
     */
    STATUS("status"),

    /**
     ** The name of the element for an account's action to be performed.
     */
    ACTION("action"),

    /**
     ** The name of the attribute collection element for a single account.
     */
    ATTRIBUTE("attributes"),

    /**
     ** The name of the attribute collection element for a single account.
     */
    ACCOUNTFORM("accountForm"),

    /**
     ** The name of the entitlement collection element for a single account.
     */
    ENTITLEMENT("entitlements")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The element name. */
    public final String tag;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Account</code> element tag.
     **
     ** @param  tag              the element tag for this
     **                          <code>Account</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Account(final String tag) {
      this.tag = tag;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Namespace
  // ~~~~ ~~~~~~~~~
  /**
   ** The schema descriptor for marshaling/unmarshaling an namespace.
   */
  enum Namespace {
      /**
       ** The name of the element for an entitlement's namespace.
       */
      ID("namespace"),

    /**
     ** The name of the element for the entitlement's action collection.
     */
    ACTION("actions")
    ;

    /** The element name. */
    public final String tag;

    ////////////////////////////////////////////////////////////////////////////
    // enum Entitlement
    // ~~~~ ~~~~~~~~~~~
    /**
     ** The schema descriptor for marshaling/unmarshaling an entitlement.
     */
    enum Entitlement {
        /**
         ** The name of the element for an entitlement's action to be performed.
         */
        ACTION("action"),

      /**
       ** The ID (key) of the element.
       */
      ID("id"),
      
      /**
       ** The name of the risk level element of an entitlement.
       */
      RISK("risk"),

      /**
       ** The status of an entitlement.
       */
      STATUS("status"),

      /**
       ** The display name of an entitlement.
       */
      DISPLAY_NAME("displayName"),

      /**
       ** The creation date of an entitlement.
       */
      CREATE_DATE("createDate"),

      /**
       ** The last update date of an entitlement.
       */
      UPDATE_DATE("updateDate"),

      /**
       ** The name of the attribute value collection element for a single entitlement.
       */
      ADDITIONAL_ATTRIBUTES("additionalAttributes"),

      /**
       ** The name of the attribute collection element for a single entitlement.
       */
      ATTRIBUTE("attributes"),
      
      /**
       ** The name of the attribute value collection element for a single entitlement.
       */
      MEMBERS("members")
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      /** The element name. */
      public final String tag;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructor for <code>Entitlement</code> element.
       **
       ** @param  tag            the element tag for this
       **                        <code>Entitlement</code>.
       **                        <br>
       **                        Possible object is {@link String}.
       */
      Entitlement(final String tag) {
        this.tag = tag;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Namespace</code> element tag.
     **
     ** @param  tag              the element tag for this
     **                          <code>Namespace</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Namespace(final String tag) {
      this.tag = tag;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Policy
  // ~~~~ ~~~~~~
  /**
   ** The schema descriptor for marshaling/unmarshaling a policy.
   */
  enum Policy {
    /**
     ** The name of the identifier element for a policy.
     */
    NAME("name"),

    /**
     ** The type of the policy.
     */
    TYPE("type"),

    /**
     ** The owner type of the policy.
     */
    OWNER_TYPE("ownerType"),

    /**
     ** The owner ID of the policy.
     */
    OWNER_ID("ownerId"),

    /**
     ** The description of the policy.
     */
    CREATE_DATE("createDate"),

    /**
     ** The description of the policy.
     */
    UPDATE_DATE("updateDate"),

    /**
     ** The description of the policy.
     */
    DESCRIPTION("description"),

    /**
     ** The priority of the policy.
     */
    PRIORITY("priority"),

    /**
     ** The name of the attribute collection element for a single policy.
     */
    ATTRIBUTE("attributes"),

    /**
     ** The name of the accounts collection element for a single policy.
     */
    APPLICATIONS("applications")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The element name. */
    public final String tag;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Policy</code> element tag.
     **
     ** @param  tag              the element tag for this
     **                          <code>Policy</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Policy(final String tag) {
      this.tag = tag;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Schema</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Schema()" and enforces use of the public method below.
   */
  private Schema() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalApplication
  /**
   ** Marshals an {@link ApplicationEntity} to an {@link OutputStream}.
   **
   ** @param  application        the {@link ApplicationEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link ApplicationEntity}.
   ** @param  stream             the {@link OutputStream} for the HTTP entity.
   **                            The implementation should not close the output
   **                            stream.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   */
  public static void marshalApplication(final ApplicationEntity application, final OutputStream stream) {
    final JsonWriter writer = Schema.factory.createWriter(stream);
    try {
      writer.writeObject(Schema.marshalApplication(application));
    }
    catch (JsonException e) {
      e.printStackTrace();
    }
    finally {
      writer.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalApplication
  /**
   ** Marshals an {@link ApplicationEntity} into a {@link JsonObject} resource.
   **
   ** @param  application        the {@link ApplicationEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link ApplicationEntity}.
   **
   ** @return                    the {@link ApplicationEntity} represented as a
   **                            {@link JsonObject}.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject marshalApplication(final ApplicationEntity application) {
    final JsonObjectBuilder result  = Json.createObjectBuilder().add(Application.ID.tag, application.id());
    // marshall accounts only if they exists
    if (application.size() > 0)
       result.add(Application.ACCOUNT.tag, marshalAccounts(application.element()));
    return result.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalApplication
  /**
   ** Factory method to create an {@link ApplicationEntity} form the specified
   ** {@link JsonReader}.
   **
   ** @param  reader             the {@link JsonReader} of the HTTP entity.
   **                            <br>
   **                            Allowed object is {@link JsonReader}.
   **
   ** @return                    the populated {@link ApplicationEntity}.
   **                            <br>
   **                            Possible object is {@link ApplicationEntity}.
   **
   ** @throws JsonException      if a schema violation occurred.
   */
  public static ApplicationEntity unmarshalApplication(final JsonReader reader) {
    return unmarshalApplication(reader.readObject());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalApplication
  /**
   ** Factory method to create an {@link ApplicationEntity} form the specified
   ** {@link InputStream}.
   **
   ** @param  stream             the {@link InputStream} of the HTTP entity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The caller is responsible for ensuring that the
   **                            input stream ends when the entity has been
   **                            consumed. The implementation should not close
   **                            the input stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the populated {@link ApplicationEntity}.
   **                            <br>
   **                            Possible object is {@link ApplicationEntity}.
   **
   ** @throws JsonException      if a schema violation occurred.
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-collection-return")
  public static ApplicationEntity unmarshalApplication(final InputStream stream) {
    final JsonReader reader = Json.createReader(stream);
    try {
      return unmarshalApplication(reader.readObject());
    }
    catch (JsonException e) {
      e.printStackTrace();
      return null;
    }
    finally {
      reader.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalApplication
  /**
   ** Factory method to create an {@link ApplicationEntity} form the specified
   ** {@link JsonObject} <code>payload</code>.
   **
   ** @param  payload            the pre-parsed payload {@link JsonObject}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the populated {@link ApplicationEntity}.
   **                            <br>
   **                            Possible object is {@link ApplicationEntity}.
   **
   ** @throws JsonException      if a schema violation occurred.
   */
  private static ApplicationEntity unmarshalApplication(final JsonObject payload) {
    // validate that the application element is well-formed
    if (payload.size() != 2)
      throw new JsonException(String.format(VIOLATED, Application.ID.tag));

    // create the entity with the identifier
    final ApplicationEntity entity = Entity.application(requireNonNull(payload, Application.ID.tag));
    // obtain the accounts attached to the application
    final JsonArray collection = payload.getJsonArray(Application.ACCOUNT.tag);
    if (collection.size() > 0) {
      for (int i = 0; i < collection.size(); i++) {
        entity.element(unmarshalAccount(collection.getJsonObject(i)));
      }
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccounts
  /**
   ** Marshals the collection of {@link AccountEntity}'s to a {@link JsonArray}
   ** resource.
   **
   ** @param  collection         the collection of {@link AccountEntity} to
   **                            jsonify.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link AccountEntity}.
   **
   ** @return                    the collection of {@link AccountEntity}
   **                            represented as a {@link JsonArray}.
   **                            <br>
   **                            Possible object is {@link JsonArray}.
   */
  private static JsonArray marshalAccounts(final List<AccountEntity> collection) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    for (AccountEntity cursor : collection) {
      collector.add(marshalAccount(cursor));
    }
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccount
  /**
   ** Marshals an {@link ApplicationEntity} to an {@link OutputStream}.
   **
   ** @param  account            the {@link AccountEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link AccountEntity}.
   ** @param  stream             the {@link OutputStream} for the HTTP entity.
   **                            The implementation should not close the output
   **                            stream.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   */
  public static void marshalAccount(final AccountEntity account, final OutputStream stream) {
    final JsonWriter writer = Schema.factory.createWriter(stream);
    try {
      writer.writeObject(Schema.marshalAccount(account));
    }
    finally {
      writer.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAccount
  /**
   ** Marshals an {@link AccountEntity} into a {@link JsonObject} resource.
   **
   ** @param  account            the {@link AccountEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link AccountEntity}.
   **
   ** @return                    the {@link AccountEntity} represented as a
   **                            {@link JsonObject}.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject marshalAccount(final AccountEntity account) {

    final JsonObjectBuilder result = Json.createObjectBuilder()
      .add(Account.ID.tag,          account.id() != null ? account.id() : "")
      .add(Account.TYPE.tag,        account.type() != null ? account.type() : "")
      .add(Account.STATUS.tag,      account.status() != null ? account.status() : "")
      .add(Account.ACTION.tag,      account.action() == null ? AccountEntity.Action.create.id : account.action().id)
      .add(Account.ACCOUNTFORM.tag, account.accountForm() != null ? account.accountForm() : "")
    ;
    // marshall attributes only if they are available on the account
    if (account.attribute().size() > 0)
      // downstream the attributes array with the tag 'attributes'
      result.add(Account.ATTRIBUTE.tag,   marshalAttributes(account.attribute()));

    // marshall namespaces only if they are available on the account
    final Map<String, List<NamespaceEntity>> namespace = account.namespace();
    if (namespace != null && namespace.size() > 0) {
      // downstream the namespace array with the tag 'entitlements'
      final JsonArrayBuilder collector = Json.createArrayBuilder();
      for (Map.Entry<String, List<NamespaceEntity>> nsMap : namespace.entrySet()) {
        for (NamespaceEntity cursor : nsMap.getValue()) {
          collector.add(marshalNamespace(cursor));
        }
      }
      result.add(Account.ENTITLEMENT.tag, collector);
    }
    return result.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalAccount
  /**
   ** Factory method to create an {@link AccountEntity} form the specified
   ** {@link JsonReader}.
   **
   ** @param  reader             the {@link JsonReader} of the HTTP entity.
   **                            <br>
   **                            Allowed object is {@link JsonReader}.
   **
   ** @return                    the populated {@link AccountEntity}.
   **                            <br>
   **                            Possible object is {@link AccountEntity}.
   **
   ** @throws JsonException      if a schema violation occurred.
   */
  public static AccountEntity unmarshalAccount(final JsonReader reader) {
    return unmarshalAccount(reader.readObject());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalAccount
  /**
   ** Factory method to create an {@link AccountEntity} form the specified
   ** {@link InputStream}.
   **
   ** @param  stream             the {@link InputStream} of the HTTP entity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The caller is responsible for ensuring that the
   **                            input stream ends when the entity has been
   **                            consumed. The implementation should not close
   **                            the input stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the populated {@link AccountEntity}.
   **                            <br>
   **                            Possible object is {@link AccountEntity}.
   */
  public static AccountEntity unmarshalAccount(final InputStream stream) {
    final JsonReader reader = Json.createReader(stream);
    try {
      return Schema.unmarshalAccount(reader.readObject());
    }
    finally {
      reader.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalAccount
  /**
   ** Factory method to create an {@link AccountEntity} form the specified
   ** {@link JsonObject} payload
   **
   ** @param  payload            the pre-parsed payload {@link JsonObject}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the populated {@link AccountEntity}.
   **                            <br>
   **                            Possible object is {@link AccountEntity}.
   */
  private static AccountEntity unmarshalAccount(final JsonObject payload) {
    // create the entity with the identifier and action
    final AccountEntity entity = Entity.account(requireNonNull(payload, Account.ID.tag)).action(requireInRange(payload, Account.ACTION.tag, AccountEntity.Action.class));
    // collect attributes metadata first if any
    collectAttributes(payload, entity);
    // collect namespaces metadata if any
    collectNamespaces(payload, entity);
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalNamespaces
  /**
   ** Marshals the a collection of {@link NamespaceEntity} to a
   ** {@link JsonArray} resource.
   **
   ** @param  collection         the collection of {@link NamespaceEntity} to
   **                            jsonify.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link NamespaceEntity}.
   **
   ** @return                    the {@link NamespaceEntity} represented as a
   **                            {@link JsonArray}.
   **                            <br>
   **                            Possible object is {@link JsonArray}.
   */
  private static JsonArray marshalNamespaces(final List<NamespaceEntity> collection) {
    // stream the namespace data
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    for (NamespaceEntity cursor : collection) {
      collector.add(marshalNamespace(cursor));
    }
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalNamespace
  /**
   ** Marshals an {@link NamespaceEntity} to a {@link JsonObject} resource.
   **
   ** @param  entity             the {@link NamespaceEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link NamespaceEntity}.
   **
   ** @return                    the {@link NamespaceEntity} represented as a
   **                            {@link JsonObject}.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  private static JsonObject marshalNamespace(final NamespaceEntity entity) {
    return Json.createObjectBuilder()
      .add(Namespace.ID.tag,     entity.id)
      .add(Namespace.ACTION.tag, marshalEntitlements(entity.element())
    ).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlements
  /**
   ** Marshals a collection of {@link EntitlementEntity}'s to a
   ** {@link JsonArray} resource.
   **
   ** @param  collection         the collection {@link EntitlementEntity}'s to
   **                            jsonify.
   **                            <br>
   **                            Allowed object is {@link EntitlementEntity}.
   **
   ** @return                    the collection of {@link EntitlementEntity}'s
   **                            represented as a {@link JsonArray}.
   **                            <br>
   **                            Possible object is {@link JsonArray}.
   */
  public static JsonArray marshalEntitlements(final List<EntitlementEntity> collection) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    for (EntitlementEntity cursor : collection) {
      collector.add(marshalEntitlement(cursor));
    }
    return collector.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalEntitlement
  /**
   ** Marshals an {@link EntitlementEntity} to a {@link JsonObject} resource.
   **
   ** @param  entity             the {@link EntitlementEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link EntitlementEntity}.
   **
   ** @return                    the {@link EntitlementEntity} represented as a
   **                            {@link JsonObject}.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject marshalEntitlement(final EntitlementEntity entity) {
    JsonObjectBuilder result = Json.createObjectBuilder();
    result
      .add(Namespace.Entitlement.ID.tag,           entity.id()          != null ? entity.id()                     : "")
      .add(Namespace.Entitlement.ACTION.tag,       entity.action()      != null ? entity.action().id              : EntitlementEntity.Action.assign.id)
      .add(Namespace.Entitlement.RISK.tag,         entity.risk()        != null ? entity.risk().toString()        : Risk.low.toString())
      .add(Namespace.Entitlement.STATUS.tag,       entity.status()      != null ? entity.status().toString()      : "")
      .add(Namespace.Entitlement.ATTRIBUTE.tag,    marshalAttributes(entity))
      ;
    if (entity.displayName() != null) {
      result.add(Namespace.Entitlement.DISPLAY_NAME.tag, entity.displayName());
    }
    if (entity.displayName() != null) {
      result.add(Namespace.Entitlement.CREATE_DATE.tag,  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(entity.createDate()));
    }
    if (entity.displayName() != null) {
      result.add(Namespace.Entitlement.UPDATE_DATE.tag,  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(entity.updateDate()));
    }
    if (entity.members().size() > 0) {
      result.add(Namespace.Entitlement.MEMBERS.tag, createArrayBuilder(entity.members()));
    }
    if (entity.additionalAttributes().size() > 0) {
      result.add(Namespace.Entitlement.ADDITIONAL_ATTRIBUTES.tag, marshalAdditionalAttributes(entity.additionalAttributes()));
    }
    return result.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAttributeValues
  /**
   ** Marshals a collection of tagged value pairs into a {@link JsonArray}
   **
   ** @param  entity             the collection of tagged-value pairs to
   **                            jsonify.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the {@link JsonArray} representation of the
   **                            given collection.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  private static JsonArray marshalAdditionalAttributes(final List<AdditionalAttributeEntity> entity) {
    final JsonArrayBuilder builder = Json.createArrayBuilder();
    if (entity.size() > 0) {
      for (AdditionalAttributeEntity cursor : entity) {
        JsonObjectBuilder attrValue = Json.createObjectBuilder();
        attrValue.add(Namespace.Entitlement.ATTRIBUTE.tag, marshalAttributes(cursor.attribute()));
        if (cursor.members() != null && cursor.members().size() > 0) {
          final JsonArrayBuilder memberBuilder = Json.createArrayBuilder();
          for (String member : cursor.members()) {
            memberBuilder.add(member);
          }
          attrValue.add(Namespace.Entitlement.MEMBERS.tag, memberBuilder);
        }
        builder.add(attrValue);
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalAttributes
  /**
   ** Marshals a collection of tagged value pairs into a {@link JsonArray}
   **
   ** @param  entity             the collection of tagged-value pairs to
   **                            jsonify.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the {@link JsonArray} representation of the
   **                            given collection.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  private static JsonArray marshalAttributes(final Map<String, Object> entity) {
    // stream the account data
    final JsonArrayBuilder builder = Json.createArrayBuilder();
    if (entity.size() > 0) {
      for (Map.Entry<String, Object> cursor : entity.entrySet()) {
        builder.add(convert(cursor.getKey(), cursor.getValue()));
      }
    }
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectNamespaces
  /**
   ** Collect the namespaces from the specified {@link JsonObject}
   ** <code>payload</code> and downstream it to the {@link AccountEntity}
   ** <code>entity</code>.
   **
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  entity             the {@link AccountEntity} collecting the
   **                            namespaces.
   **                            <br>
   **                            Allowed object is {@link AccountEntity}.
   */
  private static void collectNamespaces(final JsonObject payload, final AccountEntity entity) {
    // collect entitlement namespaces if any
    final JsonArray namespace = payload.getJsonArray(Account.ENTITLEMENT.tag);
    if (namespace != null && namespace.size() > 0) {
      for (int i = 0; i < namespace.size(); i++) {
        entity.namespace(unmarshalNamespace(namespace.getJsonObject(i)));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalNamespace
  /**
   ** Factory method to create an {@link NamespaceEntity} form the specified
   ** {@link JsonObject} payload
   **
   ** @param  payload            the pre-parsed payload {@link JsonObject}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the populated {@link NamespaceEntity}.
   **                            <br>
   **                            Possible object is {@link NamespaceEntity}.
   */
  private static NamespaceEntity unmarshalNamespace(final JsonObject payload) {
    final NamespaceEntity entity = Entity.namespace(requireNonNull(payload, Namespace.ID.tag));
    collectEntitlements(payload, entity);
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectEntitlements
  /**
   ** Collect the entitlements from the specified {@link JsonObject}
   ** <code>payload</code> and downstream it to the {@link NamespaceEntity}
   ** <code>entity</code>.
   **
   ** @param  payload            the pre-parsed payload {@link JsonObject}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  entity             the {@link NamespaceEntity} collecting the
   **                            entitlements.
   **                            <br>
   **                            Allowed object is {@link NamespaceEntity}.
   **
   ** @return                    the populated {@link NamespaceEntity}.
   **                            <br>
   **                            Possible object is {@link NamespaceEntity}.
   */
  private static NamespaceEntity collectEntitlements(final JsonObject payload, final NamespaceEntity entity) {
    // unmarshal the entitlements to apply
    final JsonArray entitlement = payload.getJsonArray(Namespace.ACTION.tag);
    if (entitlement != null && entitlement.size() > 0) {
      for (int i = 0; i < entitlement.size(); i++) {
        // associate the collected entitlements with the entity
        entity.element(unmarshallEntitlement(entitlement.getJsonObject(i)));
      }
    }
    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalAccount
  /**
   ** Factory method to create an {@link EntitlementEntity} form the specified
   ** {@link JsonObject} payload
   **
   ** @param  payload            the pre-parsed payload {@link JsonObject}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the populated {@link EntitlementEntity}.
   **                            <br>
   **                            Possible object is {@link EntitlementEntity}.
   */
  private static EntitlementEntity unmarshallEntitlement(final JsonObject payload) {
    final EntitlementEntity entity = Entity.entitlement(
      requireInRange(payload,    Namespace.Entitlement.ACTION.tag, EntitlementEntity.Action.class)
    , optionalNull  (payload,    Namespace.Entitlement.RISK.tag,   Risk.class)
    );
    entity.id(payload.getString(Namespace.Entitlement.ID.tag,           null));
    entity.id(payload.getString(Namespace.Entitlement.DISPLAY_NAME.tag, null));
    entity.id(payload.getString(Namespace.Entitlement.CREATE_DATE.tag,  null));
    entity.id(payload.getString(Namespace.Entitlement.UPDATE_DATE.tag,  null));
    collectAttributes(payload, entity);
    collectAdditionalAttributes(payload, entity);
    entity.members(collectMembers(payload));
    return entity;    
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPolicies
  /**
   ** Marshals the collection of {@link PolicyEntity}'s to a {@link JsonArray}
   ** resource.
   **
   ** @param  collection         the collection of {@link PolicyEntity} to
   **                            jsonify.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link PolicyEntity}.?ENTRY
   **                            
   **
   ** @return                    the collection of {@link PolicyEntity}
   **                            represented as a {@link JsonArray}.
   **                            <br>
   **                            Possible object is {@link JsonArray}.
   */
  public static JsonArray marshalPolicies(final List<PolicyEntity> collection) {
    final JsonArrayBuilder collector = Json.createArrayBuilder();
    for (PolicyEntity cursor : collection) {
      collector.add(marshalPolicy(cursor));
    }
    JsonArray result = collector.build();
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPolicy
  /**
   ** Marshals an {@link PolicyEntity} to an {@link OutputStream}.
   **
   ** @param  policy             the {@link PolicyEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link PolicyEntity}.
   ** @param  stream             the {@link OutputStream} for the HTTP entity.
   **                            The implementation should not close the output
   **                            stream.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   */
  public static void marshalPolicy(final PolicyEntity policy, final OutputStream stream) {
    final JsonWriter writer = Schema.factory.createWriter(stream);
    try {
      writer.writeObject(Schema.marshalPolicy(policy));
    }
    finally {
      writer.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshalPolicy
  /**
   ** Marshals an {@link PolicyEntity} into a {@link JsonObject} resource.
   **
   ** @param  policy             the {@link PolicyEntity} to jsonify.
   **                            <br>
   **                            Allowed object is {@link PolicyEntity}.
   **
   ** @return                    the {@link PolicyEntity} represented as a
   **                            {@link JsonObject}.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject marshalPolicy(final PolicyEntity policy) {
    JsonObjectBuilder resultBuilder = Json.createObjectBuilder();
    if (policy != null) {
      resultBuilder.add(Policy.NAME.tag,        policy.id())
            .add(Policy.TYPE.tag,        policy.type())
            .add(Policy.DESCRIPTION.tag, policy.description())
            .add(Policy.CREATE_DATE.tag, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(policy.createDate()))
            .add(Policy.UPDATE_DATE.tag, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(policy.updateDate()))
            .add(Policy.PRIORITY.tag,    policy.priority())
            .add(Policy.OWNER_TYPE.tag,  policy.ownerType())
            .add(Policy.OWNER_ID.tag,    policy.ownerId());
      // marshall attributes only if they are available on the policy
      if (policy.attribute() != null && policy.attribute().size() > 0)
        // downstream the attributes array with the tag 'attributes'
        resultBuilder.add(Policy.ATTRIBUTE.tag,        marshalAttributes(policy.attribute()));
  
      // marshall accounts only if they are available on the policy
      final List<AccountEntity> account = policy.account();
      if (account != null && account.size() > 0) {
        // downstream the account array with the tag 'accounts'
        final JsonArrayBuilder collector = Json.createArrayBuilder();
        for (AccountEntity acc : account) {
            collector.add(marshalAccount(acc));
        }
        resultBuilder.add(Policy.APPLICATIONS.tag, collector);
      }
    }
    JsonObject result = resultBuilder.build();
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAccounts
  /**
   ** Collect the accounts from the specified {@link JsonObject}
   ** <code>payload</code> and downstream it to the {@link PolicyEntity}
   ** <code>entity</code>.
   **
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  entity             the {@link PolicyEntity} collecting the
   **                            namespaces.
   **                            <br>
   **                            Allowed object is {@link AccountEntity}.
   */
  private static void collectAccounts(final JsonObject payload, final PolicyEntity entity) {
    // collect entitlement namespaces if any
    final JsonArray accounts = payload.getJsonArray(Policy.APPLICATIONS.tag);
    final List<AccountEntity> collector = new ArrayList<>();
    if (accounts != null && accounts.size() > 0) {
      for (int i = 0; i < accounts.size(); i++) {
        collector.add(unmarshalAccount(accounts.getJsonObject(i)));
      }
    }
    entity.account(collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalPolicy
  /**
   ** Factory method to create an {@link PolicyEntity} form the specified
   ** {@link JsonReader}.
   **
   ** @param  reader             the {@link JsonReader} of the HTTP entity.
   **                            <br>
   **                            Allowed object is {@link JsonReader}.
   **
   ** @return                    the populated {@link PolicyEntity}.
   **                            <br>
   **                            Possible object is {@link PolicyEntity}.
   **
   ** @throws JsonException      if a schema violation occurred.
   */
  private static PolicyEntity unmarshalPolicy(final JsonReader reader) {
    return unmarshalPolicy(reader.readObject());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalPolicy
  /**
   ** Factory method to create an {@link PolicyEntity} form the specified
   ** {@link InputStream}.
   **
   ** @param  stream             the {@link InputStream} of the HTTP entity.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The caller is responsible for ensuring that the
   **                            input stream ends when the entity has been
   **                            consumed. The implementation should not close
   **                            the input stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the populated {@link PolicyEntity}.
   **                            <br>
   **                            Possible object is {@link PolicyEntity}.
   */
  public static PolicyEntity unmarshalPolicy(final InputStream stream) {
    final JsonReader reader = Json.createReader(stream);
    try {
      return Schema.unmarshalPolicy(reader.readObject());
    }
    finally {
      reader.close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshalPolicy
  /**
   ** Factory method to create an {@link PolicyEntity} form the specified
   ** {@link JsonObject} payload
   **
   ** @param  payload            the pre-parsed payload {@link JsonObject}.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the populated {@link PolicyEntity}.
   **                            <br>
   **                            Possible object is {@link PolicyEntity}.
   */
  private static PolicyEntity unmarshalPolicy(final JsonObject payload) {
    // create the entity with the identifier and action
    String objectName = payload.getString(Policy.TYPE.tag);
    if ("AccessPolicy".equals(objectName)) {
      final PolicyEntity entity = Entity.policy(requireNonNull(payload, Policy.NAME.tag))
        .description(requireNonNull(payload, Policy.DESCRIPTION.tag))
        .ownerType(requireNonNull(payload, Policy.OWNER_TYPE.tag))
        .ownerId(requireNonNull(payload, Policy.OWNER_ID.tag))
      ;
      long prio = payload.getInt(Policy.PRIORITY.tag);
      entity.priority(prio);
      // collect attributes metadata first if any
      collectAttributes(payload, entity);
      // collect namespaces metadata if any
      collectAccounts(payload, entity);
      return entity;
    } else {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAttributes
  /**
   ** Collect the generic attribute mapping from the specified
   ** {@link JsonObject} payload and downstream it to the
   ** {@link Entity.Attribute} <code>entity</code>.
   **
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  entity             the {@link Entity.Attribute} collecting the
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Entity.Attribute}.
   */
  private static void collectAttributes(final JsonObject payload, final Map<String, Object> entity) {
    final JsonArray attribute = payload.getJsonArray(Attribute.ID.tag);
    if (attribute != null && attribute.size() > 0) {
      for (int i = 0; i < attribute.size(); i++) {
        final JsonObject pair = attribute.getJsonObject(i);
        entity.put(requireNonNull(pair, Attribute.Pair.ID.tag), convert(pair.get(Attribute.Pair.VALUE.tag)));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAttributes
  /**
   ** Collect the generic attribute mapping from the specified
   ** {@link JsonObject} payload and downstream it to the
   ** {@link Entity.Attribute} <code>entity</code>.
   **
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  entity             the {@link Entity.Attribute} collecting the
   **                            attributes.
   **                            <br>
   **                            Allowed object is {@link Entity.Attribute}.
   */
  private static void collectAdditionalAttributes(final JsonObject payload, final EntitlementEntity entity) {
    final JsonArray additionalAttributesArray = payload.getJsonArray(Namespace.Entitlement.ADDITIONAL_ATTRIBUTES.tag);
    if (additionalAttributesArray != null && additionalAttributesArray.size() > 0) {
      for (int i = 0; i < additionalAttributesArray.size(); i++) {
        final JsonObject additionalAttributeObject = additionalAttributesArray.getJsonObject(i);
        AdditionalAttributeEntity additionalAttribute = Entity.additionalAttribute();
        collectAttributes(additionalAttributeObject, additionalAttribute);
        additionalAttribute.members(collectMembers(additionalAttributeObject));
        entity.addAdditionalAttribute(additionalAttribute);
      }
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectMembers
  /**
   ** Collect the generic attribute mapping from the specified
   ** {@link JsonObject} payload and downstream it to the
   ** {@link Entity.Attribute} <code>entity</code>.
   **
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   */
  private static List<String> collectMembers(final JsonObject payload) {
    final JsonArray membersArray = payload.getJsonArray(Namespace.Entitlement.MEMBERS.tag);
    if (membersArray != null && membersArray.size() > 0) {
      List<String> members = new ArrayList<>();
      for (int i = 0; i < membersArray.size(); i++) {
        final String member = membersArray.getString(i);
        members.add(member);
      }
      return(members);
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionalInRange
  /**
   ** Ensures that the JSON resource <code>element</code> provides a value that
   ** is in range with the {@link Enum} class <code>range</code>.
   **
   ** @param  <T>                the type class of the enumeration.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the element to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  range              the enumeration of valid value.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    the enum value mapped at <code>name</code> in
   **                            the JSON resource <code>payload</code>.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  private static <T extends Enum<T>> T optionalNull(final JsonObject payload, final String element, final Class<T> range) {
    final String value = optionalNull(payload, element, "");
    try {
      return Enum.valueOf(range, value);
    }
    catch (IllegalArgumentException e) {
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   optionalNull
  /**
   ** Ensures that the JSON resource <code>element</code> provides a value.
   **
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the element to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value to return if no value mapped at
   **                            <code>name</code> in the JSON resource
   **                            <code>payload</code> or the value itself is
   **                            empty.
   **
   ** @return                    the string representation of the value mapped
   **                            at <code>name</code> in the JSON resource
   **                            <code>payload</code> or
   **                            <code>defaultValue</code> if no value mapped at
   **                            <code>name</code> in the JSON resource
   **                            <code>payload</code> or the value itself is
   **                            empty.
   **                            <br>
   **                            Never been <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String optionalNull(final JsonObject payload, final String element, final String defaultValue) {
    try {
      return requireNonNull(payload, element);
    }
    catch (NullPointerException e) {
      return defaultValue;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requireInRange
  /**
   ** Ensures that the JSON resource <code>element</code> provides a value that
   ** is in range with the {@link Enum} class <code>range</code>.
   **
   ** @param  <T>                the type class of the enumeration.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the element to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  range              the enumeration of valid value.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>T</code>.
   **
   ** @return                    the enum value mapped at <code>name</code> in
   **                            the JSON resource <code>payload</code>.
   **                            <br>
   **                            Never been <code>null</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **                     
   ** @throws JsonException        if the parsed value for <code>element</code>
   **                              isn't in <code>range</code>.
   ** @throws NullPointerException if no value is mapped at <code>name</code> in
   **                              the JSON resource <code>payload</code>.
   */
  private static <T extends Enum<T>> T requireInRange(final JsonObject payload, final String element, final Class<T> range) {
    final String value = requireNonNull(payload, element);
    try {
      return Enum.valueOf(range, value);
    }
    catch (IllegalArgumentException e) {
      throw new JsonException(String.format("Value [%2$s] is not valid for [%1$s].", element, value), e) ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requireNonNull
  /**
   ** Ensures that the JSON resource <code>element</code> provides a value.
   **
   ** @param  payload            the {@link JsonObject} payload to parse.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the element to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string representation of the value mapped
   **                            at <code>name</code> in the JSON resource
   **                            <code>payload</code>.
   **                            <br>
   **                            Never been <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws NullPointerException if no value is mapped at <code>name</code> in
   **                              the JSON resource <code>payload</code>.
   */
  private static String requireNonNull(final JsonObject payload, final String element) {
    // parse an validate an required element
    return Objects.requireNonNull(
      // that the default value to null so that JSON-P doesn't complain and
      // return null that we can catch by the enclosing method
      payload.getString(element, null)
    , String.format("A value is required for element [%1$s].", element)
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Returns an appropriate Java type for a {@link JsonValue} representation.
   **
   ** @param  value              the {@link JsonValue} provided.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    the Java representation of the
   **                            {@link JsonValue} provided.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  private static Object convert(final JsonValue value) {
    if (value != null) {
      switch (value.getValueType()) {
        case NULL   : return null;
        case TRUE   : return Boolean.TRUE;
        case FALSE  : return Boolean.FALSE;
        case STRING : return ((JsonString)value).getString();
        case NUMBER : return convert((JsonNumber)value);
        case ARRAY  : return ((JsonArray)value).stream().map(e -> convert(e)).collect(Collectors.toList());
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Returns an appropriate Java type for a {@link JsonNumber} representation.
   **
   ** @param  value              the {@link JsonNumber} provided.
   **                            <br>
   **                            Allowed object is {@link JsonNumber}.
   **
   ** @return                    the Java representation of the
   **                            {@link JsonNumber} provided.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  private static Object convert(final JsonNumber value) {
    if (value.isIntegral()) {
      // or other methods to get integral value
      return Long.valueOf(value.longValue());
    }
    else {
      // or other methods to get decimal number value
      return Long.valueOf(value.longValue());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Converts a tagged-value pair to an anonymous {@link JsonObject}.
   ** <br>
   ** The result of the convertion is in the form:
   ** <pre>
   **   { "id"    : "&lt;id&gt;"
   **   , "value" : "&lt;value&gt;";
   **   }
   ** </pre>
   **
   ** @param  id                 the public identifier of the tagged value-pair.
   **                            <br>
   **                            Rerenced as <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value mapped for the public identifier.
   **                            <br>
   **                            Rerenced as <code>value</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the {@link JsonObject} representation of the
   **                            given arguments.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  private static JsonObjectBuilder convert(final String id, final Object value) {
    final JsonObjectBuilder builder = Json.createObjectBuilder();
    builder.add(Attribute.Pair.ID.tag, id);
    if (value == null) {
      builder.add(Attribute.Pair.VALUE.tag, JsonValue.NULL);
    }
    else if (value instanceof Boolean) {
      builder.add(Attribute.Pair.VALUE.tag, (Boolean)value);
    }
    else if (value instanceof Integer) {
      builder.add(Attribute.Pair.VALUE.tag, (Integer)value);
    }
    else if (value instanceof Long) {
      builder.add(Attribute.Pair.VALUE.tag, (Long)value);
    }
    else if (value instanceof Double) {
      builder.add(Attribute.Pair.VALUE.tag, (Double)value);
    }
    else if (value instanceof Float) {
      builder.add(Attribute.Pair.VALUE.tag, (Double)value);
    }
    else {
      builder.add(Attribute.Pair.VALUE.tag, value.toString());
    }
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrieveAttributesFromRecords
  /**
   ** Helper method to iterate through records and retrieve name/value pairs.
   **
   ** @param  list               the {@code List} of {@link String} objects to
   **                            convert to JsonArray.
   **                            <br>
   **                            Allowed object is {@code List} of {@link String}s.
   **
   ** @return                    the {@link JsonArrayBuilder} composed of the elements.
   */
  private static JsonArrayBuilder createArrayBuilder(List<String> list) {
      JsonArrayBuilder jsonArray = Json.createArrayBuilder();
      list.stream().forEach(element -> jsonArray.add(element));
      return jsonArray;
  }
}
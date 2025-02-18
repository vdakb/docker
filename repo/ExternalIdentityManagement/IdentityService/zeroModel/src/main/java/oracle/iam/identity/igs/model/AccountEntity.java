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

    File        :   AccountEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AccountEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import oracle.hst.platform.core.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class AccountEntity
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** An <code>AccountEntity</code> includes all single-valued attributes as well
 ** as the permissions.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="account"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://www.oracle.com/schema/igs}entity"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="entitlements" minOccurs="0" maxOccurs="unbounded"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="actions"   type="{http://www.oracle.com/schema/igs}entitlement" minOccurs="0" maxOccurs="unbounded"/&gt;
 **                 &lt;/sequence&gt;
 **                 &lt;attribute name="namespace" type="{http://www.oracle.com/schema/igs}token" use="required"/&gt;
 **               &lt;/restriction&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="action" use="required"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://www.oracle.com/schema/igs}token"&gt;
 **             &lt;enumeration value="create"/&gt;
 **             &lt;enumeration value="delete"/&gt;
 **             &lt;enumeration value="modify"/&gt;
 **             &lt;enumeration value="enable"/&gt;
 **             &lt;enumeration value="disable"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 ** <b>Note</b>:
 ** <br>
 ** The name of the class is chossen in this way to avoid conflicts if the
 ** implementation hits IdentityManager where a class with similar semantic is
 ** offered by the EJB's.
 ** <p>
 ** <b>Note to myself</b>
 ** It would be interesting to find out how to implement this class as
 ** Map&lt;String, Set&lt;NamespaceEntity&gt;&gt; would do quite a bit.
 ** Unfortunately, the superclass already implements the map interface, which
 ** then refers to the attributes.
 ** Have we reached the limits of Java, or am I just too stupid?
 **
 ** @see     Entity
 ** @see     NamespaceEntity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccountEntity extends Entity.Attribute<AccountEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4770672055858591765")
  private static final long serialVersionUID = -5630828996295313963L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /** The {@link Action} to apply. */
  private Action                                       action = Action.create;

  /**
   ** The collection that provides the <code>Account Data</code> the user have
   ** in the target system.
   */
  private AccountEntity                                origin;

  /**
   ** The type of this particular account - "Primary", "Other" etc., values from OIM.
   */
  private String                                       type;
  
  /**
   ** The status of this particular account - "Revoked", "Provisioning",
   ** "Provisioned" etc., values from OIM.
   */
  private String                                       status;
  
  /**
   ** The account form name.
   */
  private String                                       accountForm;
  
  /**
   ** The collection with the <code>NamespaceEntity</code>'s the user should
   ** have in the target system.
   */
  private transient Map<String, List<NamespaceEntity>> namespace = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Action
  // ~~~~ ~~~~~~
  /**
   ** This enum store the grammar's constants of {@link Action}s.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   **   &lt;simpleType name="action"&gt;
   **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
   **       &lt;enumeration value="create"/&gt;
   **       &lt;enumeration value="delete"/&gt;
   **       &lt;enumeration value="modify"/&gt;
   **       &lt;enumeration value="enable"/&gt;
   **       &lt;enumeration value="disable"/&gt;
   **     &lt;/restriction&gt;
   **   &lt;/simpleType&gt;
   ** </pre>
   */
  public static enum Action {
      /** The encoded action values that can by applied on accounts. */
      create("create")
    , delete("delete")
    , modify("modify")
    , enable("enable")
    , disable("disable")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The human readable state value for this <code>Action</code>. */
    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Action</code> with a single state.
     **
     ** @param  id               the human readable state value for this
     **                          <code>Action</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    Action(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Action</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Action</code> should
     **                          be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Action</code> mapped at
     **                          <code>id</code>.
     **                          <br>
     **                          Possible object is <code>Action</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Action</code>.
     */
    public static Action from(final String id) {
      for (Action cursor : Action.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AccountEntity</code> with the specified public
   ** identifier but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>AccountEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 public identifier of
   **                            <code>AccountEntity</code> (usually the
   **                            value of the <code>UID</code> for the account
   **                            in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public AccountEntity(final String id) {
    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   is
  /**
   ** Verifies if the given {@link Action} <code>value</code> match the
   ** {@link Action} value of this <code>AccountEntity</code>.
   **
   ** @param  value              the {@link Action} to test.
   **                            <br>
   **                            Allowed object is {@link Action}.
   **
   ** @return                    <code>true</code> if the given {@link Action}
   **                            <code>value</code> match the {@link Action}
   **                            value of this <code>AccountEntity</code>;
   **                            otherwise <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public boolean is(final Action value) {
    return this.action.equals(Objects.requireNonNull(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} to be applied on this <code>AccountEntity</code>.
   **
   ** @param  value              the encoded action to be applied on this
   **                            <code>AccountEntity</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   **
   ** @throws IllegalArgumentException if the passed <code>value</code> cannot
   **                                  be matched with any value of the
   **                                  enumeration {@link Action}.
   */
  public AccountEntity action(final String value) {
    return action(StringUtility.empty(value) ? Action.create : Action.from(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} to be applied on this <code>AccountEntity</code>.
   **
   ** @param  action             the {@link Action} to be applied on this
   **                            account.
   **                            <br>
   **                            Allowed object is {@link Action}.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   **
   ** @throws NullPointerException if <code>action</code> is <code>null</code>.
   */
  public AccountEntity action(final Action action) {
    // prevent bogus input
    this.action = Objects.requireNonNull(action, "Action must not be null");
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the {@link Action} to be applied on this
   ** <code>AccountEntity</code>.
   **
   ** @return                    the {@link Action} to be applied on this
   **                            <code>AccountEntity</code>.
   **                            <br>
   **                            Possible object is {@link Action}.
   */
  public Action action() {
    return this.action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Sets the value of a specific property.
   **
   ** @param  id                 the identifier of the attribute to set
   **                            <code>Account Data</code> for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the attribute to set as
   **                            <code>Account Data</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity origin(final String id, final Object value) {
    this.origin.put(id, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Returns the value of a specific property.
   **
   ** @param  id                 the identifier of the attribute to set
   **                            <code>Account Data</code> for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the attribute to set as
   **                            <code>Account Data</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  public final Object origin(final String id) {
    return this.origin.get(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Sets the {@link Map} of <code>Account Data</code>s that are
   ** provisioned to the user in the source system.
   **
   ** @param  data               the  <code>Account Data</code>s that are
   **                            provisioned to the user in the target system.
   **                            <br>
   **                            Allowed object is <code>AccountEntity</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity origin(final AccountEntity data) {
    this.origin = data;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Returns the <code>Account Data</code>s that are provisioned to the user in
   ** the target system.
   **
   ** @return                    the <code>Account Data</code>s that are
   **                            provisioned to the user in the target system.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity origin() {
    return this.origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the {@link NamespaceEntity}'s that are provisioned by this
   ** <code>AccountEntity</code>.
   **
   ** @return                    the {@link NamespaceEntity}'s that are
   **                            provisioned by this
   **                            <code>AccountEntity</code>.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link List} of type
   **                            {@link NamespaceEntity} as the value.
   */
  public final Map<String, List<NamespaceEntity>> namespace() {
    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toAssign
  /**
   ** Returns the collection {@link EntitlementEntity}'s to assign for a
   ** specific <code>namespace</code> contained in the collection of
   ** {@link NamespaceEntity}'s.
   **
   ** @param  namespace          the namespace of {@link NamespaceEntity}'s to
   **                            discover.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a collection of {@link EntitlementEntity}'s for
   **                            a {@link NamespaceEntity} that are mapped at
   **                            <code>namespace</code> in the collection of
   **                            {@link NamespaceEntity}'s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link EntitlementEntity}.
   */
  public final Collection<EntitlementEntity> toAssign(final String namespace) {
    final List<NamespaceEntity> namespaces = this.namespace().get(namespace);
    for (NamespaceEntity n : namespaces) {
      return n.toAssign();
    }
    // Original code, not actually reachable
    final List<EntitlementEntity> test = null;
    test.stream().filter(e -> e.is(EntitlementEntity.Action.assign)).collect(Collectors.toList());
    // TODO: complete stream implementation
    /*
    return this.entitlement.containsKey(namespace) ? this.entitlement.get(namespace).stream().filter(e -> e.is(EntitlementEntity.Action.assign)).collect(Collectors.toList()) : Collections.emptySet()
    */
    return Collections.emptySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toRevoke
  /**
   ** Returns the collection {@link EntitlementEntity}'s to revoke for a
   ** specific <code>namespace</code> contained in the collection of
   ** {@link NamespaceEntity}'s.
   **
   ** @param  namespace          the namespace of {@link EntitlementEntity}'s to
   **                            discover.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a collection of {@link EntitlementEntity}'s for
   **                            a {@link NamespaceEntity} that are mapped at
   **                            <code>namespace</code> in the collection of
   **                            {@link NamespaceEntity}'s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link EntitlementEntity}.
   */
  public final Collection<EntitlementEntity> toRevoke(final String namespace) {
    final List<NamespaceEntity> namespaces = this.namespace().get(namespace);
    for (NamespaceEntity n : namespaces) {
      return n.toRevoke();
    }
    // Original code, not actually reachable
    final List<EntitlementEntity> test = null;
    test.stream().filter(e -> e.is(EntitlementEntity.Action.revoke)).collect(Collectors.toList());
    // TODO: complete stream implementation
    /*
    return this.entitlement.containsKey(namespace) ? this.entitlement.get(namespace).stream().filter(c -> c.action() == EntitlementEntity.Action.revoke).collect(Collectors.toList()) : Collections.emptySet();
    */
    return Collections.emptySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toModify
  /**
   ** Returns the collection {@link EntitlementEntity}'s to modify for a
   ** specific <code>namespace</code> contained in the collection of
   ** {@link NamespaceEntity}'s.
   **
   ** @param  namespace          the namespace of {@link NamespaceEntity}'s to
   **                            discover.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a collection of {@link EntitlementEntity}'s for
   **                            a {@link NamespaceEntity} that are mapped at
   **                            <code>namespace</code> in the collection of
   **                            {@link NamespaceEntity}'s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link EntitlementEntity}.
   */
  public final Collection<EntitlementEntity> toModify(final String namespace) {
    final List<NamespaceEntity> namespaces = this.namespace().get(namespace);
    for (NamespaceEntity n : namespaces) {
      return n.toModify();
    }
    // Original code, not actually reachable
    final List<EntitlementEntity> test = null;
    test.stream().filter(e -> e.is(EntitlementEntity.Action.modify)).collect(Collectors.toList());
    // TODO: complete stream implementation
    /*
    return this.entitlement.containsKey(namespace) ? this.entitlement.get(namespace).stream().filter(c -> c.action() == EntitlementEntity.Action.revoke).collect(Collectors.toList()) : Collections.emptySet();
    */
    return Collections.emptySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of this <code>Entity</code>.
   **
   ** @return                    the type of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the {@link Map} of <code>Account Data</code>s that are
   ** provisioned to the user in the source system.
   **
   ** @param  type               the type of the account.
   **                            <br>
   **                            Allowed object is <code>String</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity type(final String type) {
    this.type = type;
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Sets the status to be applied on this account.
   **
   ** @param  value              the status describing this account.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>t</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>t</code>.
   */
  public AccountEntity status(final String value) {
    this.status = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of this account.
   **
   ** @return                    the status of this account.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountForm
  /**
   ** Returns the account form name for this <code>Account Entity</code>.
   **
   ** @return                    the account form name for this
   **                            <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String accountForm() {
    return this.accountForm;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountForm
  /**
   ** Sets the account form name for this <code>Account Entity</code>.
   **
   ** @param  value              name of the form name for Account.
   **                            <br>
   **                            Allowed object is <code>String</code>.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity accountForm(final String value) {
    this.accountForm = value;
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Adds the specified {@link NamespaceEntity} to the collection of
   ** namespaces that should be provisioned by this <code>AccountEntity</code>.
   **
   ** @param  value              the {@link NamespaceEntity} representation
   **                            (Hashed Value) that should be provisioned by
   **                            this <code>AccountEntity</code>.
   **                            <br>
   **                            Allowed object is array of
   **                            {@link NamespaceEntity}.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity namespace(final NamespaceEntity value) {
    return namespace(Arrays.asList(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Adds the specified {@link EntitlementEntity}'s to the collection of
   ** namespaces that should be provisioned by this <code>AccountEntity</code>.
   **
   ** @param  value              the {@link NamespaceEntity}'s representation
   **                            (Hashed Value) that should be provisioned by
   **                            this <code>AccountEntity</code>.
   **                            <br>
   **                            Allowed object is array of
   **                            {@link NamespaceEntity}.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity namespace(final NamespaceEntity... value) {
    return namespace(Arrays.asList(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Adds the specified {@link EntitlementEntity}'s to the collection of
   ** namespaces that should be provisioned by this <code>AccountEntity</code>.
   **
   ** @param  values             the {@link EntitlementEntity}'s representation
   **                            that should be provisioned to this
   **                            <code>AccountEntity</code> to add.
   **                            <br>
   **                            Allowed object is {@link Collection} where
   **                            each element is of type
   **                            {@link EntitlementEntity}.
   **
   ** @return                    the <code>AccountEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   */
  public final AccountEntity namespace(final Collection<NamespaceEntity> values) {
    if (this.namespace == null)
      // we use a TreeMap to ensure that the order in which the collection will
      // return the KeySet is always the same
      this.namespace = new TreeMap<String, List<NamespaceEntity>>();

    for (NamespaceEntity e : values) {
      List<NamespaceEntity> list = this.namespace.get(e.id);
      if (list == null) {
        list = new ArrayList<NamespaceEntity>();
        this.namespace.put(e.id, list);
      }
      list.addAll(values);
    }
    return this;
  }
}
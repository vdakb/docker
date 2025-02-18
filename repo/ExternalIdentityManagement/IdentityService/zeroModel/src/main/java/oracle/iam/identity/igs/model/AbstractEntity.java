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

    File        :   AbstractEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.util.Objects;

import oracle.hst.platform.core.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class AbstractEntity
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** An <code>AbstractEntity</code> is an all purpose attributed root entity like
 ** identities, organizations or roles.
 ** <p>
 ** It defines the commen actions applicable on those entities.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** The name of the class is chossen in this way to avoid conflicts if the
 ** implementation hits IdentityManager where a class with similar semantic is
 ** offered by the EJB's.
 **
 ** @see     Entity
 ** @see     AbstractEntity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractEntity<T extends AbstractEntity> extends Entity.Attribute<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1304929836163743439")
  private static final long serialVersionUID = -5966237731874200905L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /** The {@link Action} to apply. */
  private Action            action = Action.create;

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
      /** The encoded action values that can by applied on identities. */
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
   ** Constructs an <code>AbstractEntity</code> with the specified public
   ** identifier but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>AbstractEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 public identifier of
   **                            <code>AbstractEntity</code> (usually the
   **                            value of the <code>Login Name</code> for the
   **                            identity in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  protected AbstractEntity(final String id) {
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
   ** {@link Action} value of this <code>AbstractEntity</code>.
   **
   ** @param  value              the {@link Action} to test.
   **                            <br>
   **                            Allowed object is {@link Action}.
   **
   ** @return                    <code>true</code> if the given {@link Action}
   **                            <code>value</code> match the {@link Action}
   **                            value of this <code>AbstractEntity</code>;
   **                            otherwise <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public final boolean is(final Action value) {
    return this.action.equals(Objects.requireNonNull(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} to be applied on this <code>AbstractEntity</code>.
   **
   ** @param  value              the encoded action to be applied on this
   **                            <code>AbstractEntity</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AbstractEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IllegalArgumentException if the passed <code>value</code> cannot
   **                                  be matched with any value of the
   **                                  enumeration {@link Action}.
   */
  public final T action(final String value) {
    return action(StringUtility.empty(value) ? Action.create : Action.from(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} to be applied on this <code>IdentityEntity</code>.
   **
   ** @param  action             the {@link Action} to be applied on this
   **                            identity.
   **                            <br>
   **                            Allowed object is {@link Action}.
   **
   ** @return                    the <code>IdentityEntity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws NullPointerException if <code>action</code> is <code>null</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T action(final Action action) {
    // prevent bogud input
    this.action = Objects.requireNonNull(action, "Action must not be null");
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the {@link Action} to be applied on this
   ** <code>IdentityEntity</code>.
   **
   ** @return                    the {@link Action} to be applied on this
   **                            <code>IdentityEntity</code>.
   **                            <br>
   **                            Possible object is {@link Action}.
   */
  public final Action action() {
    return this.action;
  }
}
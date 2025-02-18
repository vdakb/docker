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

    File        :   Publication.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Publication.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.util.Objects;

import oracle.hst.platform.core.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Publication
// ~~~~~ ~~~~~~~~~~~
/**
 ** <code>Publication</code>s managing the organizations associated with
 ** entities are done by publishing the entity to organizations or revoking
 ** them.
 ** Entity <code>Publication</code> is applicable to:
 ** <ul>
 **   <li>Roles
 **   <li>Application Instances
 **   <li>Entitlements
 ** </ul>
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="publication"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="id" type="{http://www.oracle.com/schema/igs}token"/&gt;
 **       &lt;attribute name="hierarchy" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true"/&gt;
 **       &lt;attribute name="action" default="assign"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://www.oracle.com/schema/igs}token"&gt;
 **             &lt;enumeration value="assign"/&gt;
 **             &lt;enumeration value="revoke"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @see     Entity
 ** @see     RoleEntity
 ** @see     EntitlementEntity
 ** @see     ApplicationEntity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Publication {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link Action} to apply.
   */
  private Action                              action    = Action.assign;
  private Boolean                             hierarchy = Boolean.TRUE;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Action
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants of {@link Publication.Action}s.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   ** &lt;simpleType name="action"&gt;
   **   &lt;restriction base="{http://www.oracle.com/schema/igs}token"&gt;
   **     &lt;enumeration value="assign"/&gt;
   **     &lt;enumeration value="revoke"/&gt;
   **   &lt;/restriction&gt;
   ** &lt;/simpleType&gt;
   ** </pre>
   */
  public static enum Action {
      /** the encoded action values that can by applied on publications. */
      assign("assign")
    , revoke("revoke")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the human readable state value for this <code>Action</code>. */
    public final String  id;

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
      for (Action cursor : Publication.Action.values()) {
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
   ** Constructs an empty <code>Publication</code>.
   */
  Publication() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} to be applied on this publication.
   **
   ** @param  value              the encoded action to be applied on this
   **                            publication.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Publication</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Publication</code>.
   **
   ** @throws IllegalArgumentException if the passed <code>value</code> cannot
   **                                  be matched with any value in the range
   **                                  of {@link Action}.
   */
  public Publication action(final String value) {
    return with(StringUtility.empty(value) ? Action.assign : Action.from(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   with
  /**
   ** Sets the {@link Publication.Action} to be applied on this publication.
   **
   ** @param  value              the {@link Publication.Action} to be applied on
   **                            this publication.
   **                            <br>
   **                            Allowed object is {@link Publication.Action}.
   **
   ** @return                    the <code>Publication</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>Publication</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public Publication with(final Action value) {
    // prevent bogud input
    this.action = Objects.requireNonNull(value, "Action must not be null");
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the {@link Publication.Action} to be applied on this
   ** publication.
   **
   ** @return                    the {@link Publication.Action} to be applied on
   **                            this publication.
   **                            <br>
   **                            Possible object is {@link Publication.Action}.
   */
  public Action action() {
    return this.action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   with
  /**
   ** Whether the publication should be inherited by all subordinated
   ** organizations in Identity Manager.
   **
   ** @param  value              <code>true</code> if the publication should be
   **                            inherited by all subordinated organizations in
   **                            Identity Manager.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Publication</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>Publication</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code>.
   */
  public Publication with(final Boolean value) {
    // prevent bogud input
    this.hierarchy = Objects.requireNonNull(value, "Hierarchy must not be null");
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hierarchy
  /**
   ** Whether the publication should be inherited by all subordinated
   ** organizations in Identity Manager.
   **
   ** @return                    <code>true</code> if the publication should be
   **                            inherited by all subordinated organizations in
   **                            Identity Manager.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean hierarchy() {
    return this.hierarchy;
  }
}
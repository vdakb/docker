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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   RoleEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleEntity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class RoleEntity
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>RoleEntity</code> act as generic wrapper for a <code>Role</code>
 ** instance of Oracle Identity Manager.
 **
 ** @author Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleEntity extends EntitlementEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  public static final String MULTIPLE         = "roles";
  public static final String SINGLE           = "role";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7785243294279327675")
  private static final long  serialVersionUID = -974991883646161424L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /** The category of Oracle Identity Manager this role belongs to. */
  private Long               category         = null;

  /**
   ** the <code>Access Policies</code> that this role is triggering by assigning
   ** the role member ship <code>Resource Object</code>
   */
  private AccessPolicy       policy           = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleEntity</code> with the specified name but without
   ** an valid identifier.
   ** <p>
   ** The identifier the <code>RoleEntity</code> belongs to has to be populated
   ** manually.
   **
   ** @param  name               the identifying name of the
   **                            <code>RoleEntity</code>.
   */
  public RoleEntity(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleEntity</code> with the specified key and name.
   ** task.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>RoleEntity</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>RoleEntity</code>.
   */
  public RoleEntity(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleEntity</code> with the specified key and name.
   ** task.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>RoleEntity</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>RoleEntity</code>.
   ** @param  policy             the <code>Access Policy</code> that is
   **                            triggered by the group membership operation.
   */
  public RoleEntity(final long identifier, final String name, final AccessPolicy policy) {
    // ensure inheritance
    super(identifier, name);

    // initialize instannce attributes
    this.policy = policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Sets the internal system identifier of the category this
   ** {@link RoleEntity}.
   **
   ** @param  category           internal system identifier of this
   **                            {@link RoleEntity}.
   */
  public final void category(final long category) {
    category(new Long(category));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Sets the internal system identifier of the category this
   ** {@link RoleEntity}.
   **
   ** @param  category           internal system identifier of this
   **                            {@link RoleEntity}.
   */
  public final void category(final Long category) {
    this.category = category;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the <code>Category</code> this role belongs to.
   **
   ** @return                    the <code>Category</code> this role belongs to.
   */
  public final Long category() {
    return this.category;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policy
  /**
   ** Returns the <code>Access Policy</code> that is triggered by the role
   ** membership operation.
   **
   ** @return                    the <code>Access Policy</code> that is
   **                            triggered.
   */
  public final AccessPolicy policy() {
    return this.policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elements (overridden)
  /**
   ** Returns the XML element tag to marshal/unmarshal multiple entities of
   ** this type.
   **
   ** @return                    the XML element tag to marshal/unmarshal
   **                            multiple entity of this type.
   */
  @Override
  public final String elements() {
    return MULTIPLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (overridden)
  /**
   ** Returns the XML element tag to marshal/unmarshal a single entity of this
   ** type.
   **
   ** @return                    the XML element tag to marshal/unmarshal a
   **                            single entity of this type.
   */
  @Override
  public final String element() {
    return SINGLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity (overridden)
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  @Override
  public final String entity() {
    return TaskBundle.string(TaskMessage.ENTITY_ROLE);
  }
}
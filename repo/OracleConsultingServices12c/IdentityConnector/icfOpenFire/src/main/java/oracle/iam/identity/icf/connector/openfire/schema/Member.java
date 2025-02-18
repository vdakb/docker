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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Member.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Member.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.schema;

import java.util.Map;

////////////////////////////////////////////////////////////////////////////////
// final class Member
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** The <code>Member</code> database resource in Openfire.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Member extends Entity<Member> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The alias name of the administrator attribute. */
  public static final String ADM = "administrator";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Member</code> target resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Member() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Member</code> target resource with the values
   ** supplied by the given mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  private Member(final Map<String, Object> data) {
    // ensure inheritance
    super(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gid
  /**
   ** Sets the gid of the <code>Member</code>.
   **
   ** @param  value              the gid of the <code>Member</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public final Member gid(final String value) {
    attribute(GID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gid
  /**
   ** Returns the gid of the <code>Member</code>.
   **
   ** @return                    the gid of the <code>Member</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String gid() {
    return attribute(GID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Sets the uid of the <code>Member</code>.
   **
   ** @param  value              the uid of the <code>Member</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public final Member uid(final String value) {
    attribute(UID, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uid
  /**
   ** Returns the uid of the <code>Member</code>.
   **
   ** @return                    the uid of the <code>Member</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String uid() {
    return attribute(UID);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Sets the administrator flag of the <code>Member</code>.
   **
   ** @param  value              the administrator flag of the 
   **                            <code>Member</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Member</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public final Member administrator(final Integer value) {
    attribute(ADM, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   administrator
  /**
   ** Returns the administrator flag of the <code>Member</code>.
   **
   ** @return                    the administrator flag of the 
   **                            <code>Member</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer administrator() {
    return integerValue(ADM);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Resource)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                      the string representation for this instance in
   **                              its minimal form.
   **                              <br>
   **                              Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return gid() + "#" + uid();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary (Entity)
  /**
   ** Returns the value array of the uniquely identifying attributes.
   ** <br>
   ** Such a value is mainly used in join operations with other entities.
   **
   ** @return                    the value array of the uniquely identifying
   **                            attribute.
   **                            <br>
   **                            Possible object is array of {@link Object}.
   */
  @Override
  public final Object[] primary() {
    return new Object[]{attribute(GID), attribute(UID)};
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link User} <code>Member</code> instance from
   ** the specified {@link User}.
   **
   ** @param  entity             the {@link User} entity to populate the
   **                            required values from.
   **                            <br>
   **                            Allowed object is {@link User}.
   **
   ** @return                    the <code>Member</code> instance created.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public static Member build(final User entity) {
    return build().uid(entity.uid());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link Group} <code>Member</code> instance from
   ** the specified {@link Group}.
   **
   ** @param  entity             the {@link Group} entity to populate the
   **                            required values from.
   **                            <br>
   **                            Allowed object is {@link Group}.
   **
   ** @return                    the <code>Member</code> instance created.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public static Member build(final Group entity) {
    return build().gid(entity.gid());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Member</code> instance that populates its
   ** values from the given attribute mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   **
   ** @return                    the <code>Member</code> instance populated.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public static Member build(final Map<String, Object> data) {
    return new Member(data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Member</code> instance with the
   ** properties probided.
   **
   ** @param  gid                the gid of the <code>Member</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  uid                the uid of the <code>Member</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Member</code> instance created.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public static Member build(final String gid, final String uid) {
    return build().uid(uid).gid(gid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Member</code>
   ** instance.
   **
   ** @return                    the <code>Member</code> instance created.
   **                            <br>
   **                            Possible object is <code>Member</code>.
   */
  public static Member build() {
    return new Member().administrator(0);
  }
}
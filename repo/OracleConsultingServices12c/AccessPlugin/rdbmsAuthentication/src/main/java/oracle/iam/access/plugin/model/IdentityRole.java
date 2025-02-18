/*
    ORACLE Deutschland B.V. & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Plugin
    Subsystem   :   OpenIdConnect Discovery

    File        :   IdentityRole.java

    Compiler    :   Java Development Kit 8

    Author      :   nitin.popli@oracle.com

    Purpose     :   This file implements the class
                    IdentityRole.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-11-11  npopli      First release version
*/

package oracle.iam.access.plugin.model;

import java.security.Principal;

import oracle.security.idm.User;
import oracle.security.idm.RoleProfile;
import oracle.security.idm.SearchFilter;
import oracle.security.idm.SearchResponse;

////////////////////////////////////////////////////////////////////////////////
// class IdentityRole
// ~~~~~ ~~~~~~~~~~~~
/**
 ** This class represents detailed profile of a role identity.
 ** <br>
 ** It allows for user properties to be access in generic manner.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.1
 */
public class IdentityRole extends    AbstractProfile<IdentityRole, String>
                          implements RoleProfile {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5301359740782455077")
  private static final long serialVersionUID = 7343997688238558277L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityRole</code> database resource that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public IdentityRole() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>IdentityRole</code> database resource.
   **
   ** @param  identifier         the primary identifier of an
   **                            <code>IdentityRole</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private IdentityRole(final String identifier) {
    // ensure inheritance
    super();

    // initialite inheritance
    primaryIdentifier(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayName (Identity)
  /**
   ** Sets the <code>displayName</code> property of the
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the <code>displayName</code> property of the
   **                            <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDisplayName(final String value) {
    property(RoleProfile.DISPLAY_NAME, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName (Identity)
  /**
   ** Returns the <code>displayName</code> property of the
   ** <code>IdentityRole</code>.
   **
   ** @return                    the <code>displayName</code> property of the
   **                            <code>IdentityRole</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDisplayName() {
    return stringValue(RoleProfile.DISPLAY_NAME);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRoleProfile (Role)
  /**
   ** Returns <code>IdentityRole</code> instance.
   **
   ** @return                    <code>IdentityRole</code> instance.
   **                            <br>
   **                            Allowed object is {@link RoleProfile}.
   */
  @Override
  public final RoleProfile getRoleProfile() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSeeded (RoleProfile)
  /**
   ** Returns <code>true</code> if this <code>IdentityRole</code> is seeded.
   **
   ** @return                    <code>true</code> if this
   **                            <code>IdentityRole</code> is seeded; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean isSeeded() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEnterpriseRole (RoleProfile)
  /**
   ** Returns <code>true</code> if this <code>IdentityRole</code> represents a
   ** enterprise role.
   **
   ** @return                    <code>true</code> if this
   **                            <code>IdentityRole</code> represents a
   **                            enterprise role; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean isEnterpriseRole() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isApplicationRole (RoleProfile)
  /**
   ** Returns <code>true</code> if this <code>IdentityRole</code> represents an
   ** application role.
   **
   ** @return                    <code>true</code> if this
   **                            <code>IdentityRole</code> represents an
   **                            application role; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean isApplicationRole() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription (RoleProfile)
  /**
   ** Sets the <code>description</code> property of the
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the <code>description</code> property of the
   **                            <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setDescription(final String value) {
    property(RoleProfile.DESCRIPTION, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (RoleProfile)
  /**
   ** Returns the <code>description</code> property of the
   ** <code>IdentityRole</code>.
   **
   ** @return                    the <code>description</code> property of the
   **                            <code>IdentityRole</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getDescription() {
    return stringValue(RoleProfile.DESCRIPTION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOwner (RoleProfile)
  /**
   ** Adds the {@link User} representation as an owner of this
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the {@link User} representation to add as an
   **                            owner of this <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link User}.
   */
  @Override
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public final void addOwner(final User value) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeOwner (RoleProfile)
  /**
   ** Removes the {@link User} representation as an owner from this
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the {@link User} representation to remove as an
   **                            owner from this
   **                            <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link User}.
   */
  @Override
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public final void removeOwner(final User value) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOwner (RoleProfile)
  /**
   ** Adds the {@link Principal} representation as an owner of this
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the {@link Principal} representation to add as
   **                            an owner of this <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   */
  @Override
  public final void addOwner(final Principal value) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeOwner (RoleProfile)
  /**
   ** Removes the {@link Principal} representation as an owner from this
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the {@link Principal} representation to remove
   **                            as an owner from this
   **                            <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   */
  @Override
  public final void removeOwner(final Principal value) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOwners (RoleProfile)
  /**
   ** Returns the collection of owners of this <code>IdentityRole</code>.
   **
   ** @param  filter             the optional {@link SearchFilter} for filtering
   **                            the results.
   **                            <br>
   **                            By default the filter is set to
   **                            <code>null</code> i.e. all the results will be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link SearchResponse} instance containing
   **                            all the owners matching the
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Each result will be instance of
   **                            User or Role class.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  @Override
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public final SearchResponse getOwners(final SearchFilter filter) {
    return getOwners(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOwners (RoleProfile)
  /**
   ** Returns the collection of owners of this <code>IdentityRole</code>.
   **
   ** @param  filter             the optional {@link SearchFilter} for filtering
   **                            the results.
   **                            <br>
   **                            By default the filter is set to
   **                            <code>null</code> i.e. all the results will be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  direct             <code>true</code> if all the direct owners will
   **                            be returned only.
   **                            <br>
   **                            <code>false</code> if both the direct and
   **                            indirect owners will be returned.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link SearchResponse} instance containing
   **                            all the owners matching the
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Each result will be instance of
   **                            User or Role class.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  @Override
  public final SearchResponse getOwners(final SearchFilter filter, final boolean direct) {
    final SearchResponse result = null;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addManager (RoleProfile)
  /**
   ** Adds the {@link Principal} representation as an manager of this
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the {@link Principal} representation to add as
   **                            an manager of this <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   */
  @Override
  public final void addManager(final Principal value) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeManager (RoleProfile)
  /**
   ** Removes the {@link Principal} representation as a manager from this
   ** <code>IdentityRole</code>.
   **
   ** @param  value              the {@link Principal} representation to remove
   **                            as a manager from this
   **                            <code>IdentityRole</code>.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   */
  @Override
  public final void removeManager(final Principal value) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManagers (RoleProfile)
  /**
   ** Returns the collection of managers of this <code>IdentityRole</code>.
   **
   ** @param  filter             the optional {@link SearchFilter} for filtering
   **                            the results.
   **                            <br>
   **                            By default the filter is set to
   **                            <code>null</code> i.e. all the results will be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the {@link SearchResponse} instance containing
   **                            all the managers matching the
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Each result will be instance of
   **                            User or Role class.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  @Override
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public final SearchResponse getManagers(final SearchFilter filter) {
    return getManagers(filter, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManagers (RoleProfile)
  /**
   ** Returns the collection of managers of this <code>IdentityRole</code>.
   **
   ** @param  filter             the optional {@link SearchFilter} for filtering
   **                            the results.
   **                            <br>
   **                            By default the filter is set to
   **                            <code>null</code> i.e. all the results will be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  direct             <code>true</code> if all the direct managers
   **                            will be returned only.
   **                            <br>
   **                            <code>false</code> if both the direct and
   **                            indirect managers will be returned.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link SearchResponse} instance containing
   **                            all the managers matching the
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Each result will be instance of
   **                            User or Role class.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  @Override
  public final SearchResponse getManagers(final SearchFilter filter, final boolean direct) {
    final SearchResponse result = null;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getGrantees (RoleProfile)
  /**
   ** Returns the collection of grantees of this <code>IdentityRole</code>.
   **
   ** @param  filter             the optional {@link SearchFilter} for filtering
   **                            the results.
   **                            <br>
   **                            By default the filter is set to
   **                            <code>null</code> i.e. all the results will be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   ** @param  direct             <code>true</code> if all the direct grantees
   **                            will be returned only.
   **                            <br>
   **                            <code>false</code> if both the direct and
   **                            indirect grantees will be returned.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link SearchResponse} instance containing
   **                            all the managers matching the
   **                            {@link SearchFilter}.
   **                            <br>
   **                            Each result will be instance of
   **                            User or Role class.
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   */
  @Override
  public final SearchResponse getGrantees(final SearchFilter filter, final boolean direct) {
    final SearchResponse result = null;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a minimal <code>IdentityRole</code> database
   ** resource.
   **
   ** @param  primaryIdentifier  the primary identifier of an
   **                            <code>IdentityRole</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>IdentityRole</code> created.
   **                            <br>
   **                            Possible object is <code>IdentityRole</code>.
   */
  public static IdentityRole of(final String primaryIdentifier) {
    return new IdentityRole(primaryIdentifier);
  }
}
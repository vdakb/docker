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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Descriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Descriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-03-11  DSteding    First release version
*/

package oracle.iam.identity.foundation.reconciliation;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskDescriptor;
import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class Descriptor
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Descriptor</code> is intend to use where inbound attributes of an
 ** Oracle Identity Manager Object (core or user defined) are mapped to
 ** reconciliation source.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Descriptor extends TaskDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The <code>Reconciliation Field</code> defined on an Identity targeted for
   ** trusted identity reconciliation if the native field <code>usr_login</code>
   ** has to be handled by the reconciliation.
   */
  public static final String IDENTITY_LOGIN                = "Identity Login";

  /**
   ** Default value for {@link #IDENTITY_LOGIN} if nothing else is defined in
   ** the descriptor loaded from the Metadata Service.
   */
  public static final String IDENTITY_LOGIN_DEFAULT        = "User ID";

  /**
   ** The <code>Reconciliation Field</code> defined on an Identity targeted for
   ** trusted identity reconciliation if the native field
   ** <code>usr_status</code> has to be handled by the reconciliation.
   */
  public static final String IDENTITY_STATUS               = "Identity Status";

  /**
   ** Default value for {@link #IDENTITY_STATUS} if nothing else is defined in
   ** the descriptor loaded from the Metadata Service.
   */
  public static final String IDENTITY_STATUS_DEFAULT       = "Status";

  /**
   ** The <code>Reconciliation Field</code> defined on an Identity targeted for
   ** trusted identity reconciliation if the native field
   ** <code>usr_emp_type</code> has to be handled by the reconciliation.
   */
  public static final String IDENTITY_ROLE                 = "Identity Role";

  /**
   ** Default value for {@link #IDENTITY_ROLE} if nothing else is defined in the
   ** descriptor loaded from the Metadata Service.
   */
  public static final String IDENTITY_ROLE_DEFAULT         = "User Type";

  /**
   ** The <code>Reconciliation Field</code> defined on an Identity targeted for
   ** trusted identity reconciliation if the native field
   ** <code>usr_xellerate_type</code> has to be handled by the reconciliation.
   */
  public static final String IDENTITY_TYPE                 = "Identity Type";

  /**
   ** Default value for {@link #IDENTITY_ROLE} if nothing else is defined in the
   ** descriptor loaded from the Metadata Service.
   */
  public static final String IDENTITY_TYPE_DEFAULT         = "Employee Type";

  /**
   ** The <code>Reconciliation Field</code> defined on an Identity targeted for
   ** trusted identity reconciliation if the native field <code>act_key</code>
   ** has to be handled by the reconciliation.
   */
  public static final String IDENTITY_ORGANIZATION         = "Identity Organization";

  /**
   ** Default value for {@link #IDENTITY_ORGANIZATION} if nothing else is
   ** defined in the descriptor loaded from the Metadata Service.
   */
  public static final String IDENTITY_ORGANIZATION_DEFAULT = "Organization";

  /**
   ** The <code>Reconciliation Field</code> defined on an Identity targeted for
   ** trusted identity reconciliation if the native field
   ** <code>usr_manager_key</code> has to be handled by the reconciliation.
   */
  public static final String IDENTITY_MANAGER              = "Identity Manager";

  /**
   ** Default value for {@link #IDENTITY_MANAGER} if nothing else is defined in
   ** the descriptor loaded from the Metadata Service.
   */
  public static final String IDENTITY_MANAGER_DEFAULT      = "Manager User ID";

  /**
   ** The <code>Reconciliation Field</code> defined on an Organization targeted
   ** for trusted organization reconciliation if the native field
   ** <code>act_status</code> has to be handled by the reconciliation.
   */
  public static final String ORGANIZATION_STATUS           = "Organization Status";

  /**
   ** Default value for {@link #ORGANIZATION_STATUS} if nothing else is defined
   ** in the descriptor loaded from the Metadata Service.
   */
  public static final String ORGANIZATION_STATUS_DEFAULT   = "Status";

  /**
   ** The <code>Reconciliation Field</code> defined on an Organization targeted
   ** for trusted organization reconciliation if the native field
   ** <code>act_type</code> has to be handled by the reconciliation.
   */
  public static final String ORGANIZATION_TYPE             = "Organization Type";

  /**
   ** Default value for {@link #ORGANIZATION_STATUS} if nothing else is defined
   ** in the descriptor loaded from the Metadata Service.
   */
  public static final String ORGANIZATION_TYPE_DEFAULT     = "Type";

  /**
   ** The <code>Reconciliation Field</code> defined on an Organization targeted
   ** for trusted organization reconciliation if the native field
   ** <code>parent_key</code> has to be handled by the reconciliation.
   */
  public static final String ORGANIZATION_PARENT           = "Organization Parent";

  /**
   ** Default value for {@link #ORGANIZATION_STATUS} if nothing else is defined
   ** in the descriptor loaded from the Metadata Service.
   */
  public static final String ORGANIZATION_PARENT_DEFAULT   = "Parent Name";

  /**
   ** The <code>Reconciliation Field</code> defined on a Role targeted for
   ** trusted role reconciliation if the status of a role has to be handled by
   ** the reconciliation.
   */
  public static final String ROLE_STATUS                   = "Role Status";

  /**
   ** Default value for {@link #ROLE_STATUS} if nothing else is defined
   ** in the descriptor loaded from the Metadata Service.
   */
  public static final String ROLE_STATUS_DEFAULT           = "Status";

  /**
   ** The <code>Reconciliation Field</code> defined on a Role targeted for
   ** trusted role reconciliation if the category of a role has to be handled by
   ** the reconciliation.
   */
  public static final String ROLE_CATEGORY                 = "Role Category";

  /**
   ** Default value for {@link #ROLE_CATEGORY} if nothing else is defined
   ** in the descriptor loaded from the Metadata Service.
   */
  public static final String ROLE_CATEGORY_DEFAULT         = "Category";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link AttributeMapping} of mapping of multi-valued attribute name
   ** transformation
   */
  private final AttributeMapping multivalued;

  /** the mapping of attribute profile translations */
  private final AbstractLookup   profile;

  /** the mapping of entitlement */
  private final AbstractLookup   entitlement;

  /** the mapping of attribute lookup transformation */
  private final AbstractLookup   lookup;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Descriptor</code> which is associated with the
   ** specified {@link Loggable} for dubigging purpose.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this wrapper.
   */
  public Descriptor(final Loggable loggable) {
    // ensure inheritance
    super(loggable);

    // initialize instance
    this.multivalued = new AttributeMapping(loggable);
    this.profile     = new AbstractLookup(loggable);
    this.entitlement = new AbstractLookup(loggable);
    this.lookup      = new AbstractLookup(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Descriptor</code> which is associated with the
   ** specified {@link Loggable} for dubigging purpose.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this wrapper.
   ** @param  mapping            the {@link AttributeMapping} of varaiables
   **                            provided by this reference descriptor.
   ** @param  constant           the {@link AttributeMapping} of constants
   **                            provided by this reference descriptor.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   */
  public Descriptor(final Loggable loggable, final AttributeMapping mapping, final AttributeMapping constant, final AttributeTransformation transformation) {
    // ensure inheritance
    super(loggable, mapping, constant, transformation);

    // initialize instance
    this.multivalued = new AttributeMapping(loggable);
    this.profile     = new AbstractLookup(loggable);
    this.entitlement = new AbstractLookup(loggable);
    this.lookup      = new AbstractLookup(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multivalued
  /**
   ** Returns the multi-valued {@link AttributeMapping} this descriptor holds.
   **
   ** @return                    the profile {@link AttributeMapping} of
   **                            this descriptor.
   */
  public final AttributeMapping multivalued() {
    return this.multivalued;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profile
  /**
   ** Returns the profile {@link AbstractLookup} this descriptor holds.
   **
   ** @return                    the profile {@link AbstractLookup} of
   **                            this descriptor.
   */
  public final AbstractLookup profile() {
    return this.profile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Returns the entitlement {@link AbstractLookup} this descriptor holds.
   **
   ** @return                    the entitlement {@link AbstractLookup} of
   **                            this descriptor.
   */
  public final AbstractLookup entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Returns the lookup {@link AbstractLookup} this descriptor holds.
   **
   ** @return                    the lookup {@link AbstractLookup} of
   **                            this descriptor.
   */
  public final AbstractLookup lookup() {
    return this.lookup;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityLogin
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on an
   ** Identity targeted for trusted identity reconciliation if the native field
   ** <code>usr_login</code> has to be handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTITY_LOGIN}.
   ** <p>
   ** If {@link #IDENTITY_LOGIN} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #IDENTITY_LOGIN_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted identity reconciliation process that's
   **                            identifies the login field of an user profile.
   */
  public final String identityLogin() {
    return this.profile.stringValue(IDENTITY_LOGIN, IDENTITY_LOGIN_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityType
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on an
   ** Identity targeted for trusted identity reconciliation if the native field
   ** <code>usr_xellerate_type</code> has to be handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTITY_TYPE}.
   ** <p>
   ** If {@link #IDENTITY_TYPE} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #IDENTITY_TYPE_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted identity reconciliation process that's
   **                            identifies the type of an user profile.
   */
  public final String identityType() {
    return this.profile.stringValue(IDENTITY_TYPE, IDENTITY_TYPE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityRole
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on an
   ** Identity targeted for trusted identity reconciliation if the native field
   ** <code>usr_emp_type</code> has to be handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTITY_ROLE}.
   ** <p>
   ** If {@link #IDENTITY_ROLE} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #IDENTITY_ROLE_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted identity reconciliation process that's
   **                            identifies the role of an user profile.
   */
  public final String identityRole() {
    return this.profile.stringValue(IDENTITY_ROLE, IDENTITY_ROLE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityStatus
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on an
   ** Identity targeted for trusted identity reconciliation if the native field
   ** <code>usr_status</code> has to be handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTITY_STATUS}.
   ** <p>
   ** If {@link #IDENTITY_STATUS} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #IDENTITY_STATUS_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted identity reconciliation process that's
   **                            identifies the status of an user profile.
   */
  public final String identityStatus() {
    return this.profile.stringValue(IDENTITY_STATUS, IDENTITY_STATUS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityOrganization
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on an
   ** Identity targeted for trusted identity reconciliation if the native field
   ** <code>act_key</code> has to be handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTITY_ORGANIZATION}.
   ** <p>
   ** If {@link #IDENTITY_ORGANIZATION} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #IDENTITY_ORGANIZATION_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted identity reconciliation process that's
   **                            identifies the organization of an user profile.
   */
  public final String identityOrganization() {
    return this.profile.stringValue(IDENTITY_ORGANIZATION, IDENTITY_ORGANIZATION_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identityManager
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on an
   ** Identity targeted for trusted identity reconciliation if the native field
   ** <code>usr_manager_key</code> has to be handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #IDENTITY_ORGANIZATION}.
   ** <p>
   ** If {@link #IDENTITY_ORGANIZATION} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #IDENTITY_ORGANIZATION_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted identity reconciliation process that's
   **                            identifies the user manager of an user profile.
   */
  public final String identityManager() {
    return this.profile.stringValue(IDENTITY_MANAGER, IDENTITY_MANAGER_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationType
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on an
   ** Organization targeted for trusted organization reconciliation if the
   ** native field <code>act_type</code> has to be handled by the
   ** reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #ORGANIZATION_TYPE}.
   ** <p>
   ** If {@link #ORGANIZATION_TYPE} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #ORGANIZATION_TYPE_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted organization reconciliation process
   **                            that's identifies the type of an organization
   **                            profile.
   */
  public final String organizationType() {
    return this.profile.stringValue(ORGANIZATION_TYPE, ORGANIZATION_TYPE_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationStatus
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on a
   ** Organization targeted for trusted organization reconciliation if the
   ** native field <code>act_status</code> has to be handled by the
   ** reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #ORGANIZATION_STATUS}.
   ** <p>
   ** If {@link #ORGANIZATION_STATUS} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #ORGANIZATION_STATUS_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted organization reconciliation process
   **                            that's identifies the status of an organization
   **                            profile.
   */
  public final String organizationStatus() {
    return this.profile.stringValue(ORGANIZATION_STATUS, ORGANIZATION_STATUS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organizationParent
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on a
   ** Organization targeted for trusted organization reconciliation if the
   ** native field <code>parent_key</code> has to be handled by the
   ** reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #ORGANIZATION_PARENT}.
   ** <p>
   ** If {@link #ORGANIZATION_PARENT} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #ORGANIZATION_PARENT_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted organization reconciliation process
   **                            that's identifies the parent organization of an
   **                            organization profile.
   */
  public final String organizationParent() {
    return this.profile.stringValue(ORGANIZATION_PARENT, ORGANIZATION_PARENT_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleStatus
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on a
   ** Role targeted for trusted role reconciliation if the status of the role
   ** has to be handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #ROLE_STATUS}.
   ** <p>
   ** If {@link #ROLE_STATUS} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #ROLE_STATUS_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted organization reconciliation process
   **                            that's identifies the status of an organization
   **                            profile.
   */
  public final String roleStatus() {
    return this.profile.stringValue(ROLE_STATUS, ROLE_STATUS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleCategory
  /**
   ** Returns the name of the <code>Reconciliation Field</code> defined on a
   ** Role targeted for trusted role reconciliation if the category has to be
   ** handled by the reconciliation.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #ROLE_CATEGORY}.
   ** <p>
   ** If {@link #ROLE_CATEGORY} is not mapped in the underlying
   ** <code>Metadata Descriptor</code> this method returns
   ** {@link #ROLE_CATEGORY_DEFAULT}
   **
   ** @return                    the name of the reconciliation field in a
   **                            trusted identity reconciliation process that's
   **                            identifies the type of an user profile.
   */
  public final String roleCategory() {
    return this.profile.stringValue(ROLE_CATEGORY, ROLE_CATEGORY_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(super.toString());
    if (this.profile.size() > 0)
      builder.append(TaskBundle.format(TaskMessage.PROFILE_MAPPING,             this.profile.toString()));
    if (this.multivalued.size() > 0)
      builder.append(TaskBundle.format(TaskMessage.MULTIVALUED_TRANSFORMATION,  this.multivalued.toString()));
    if (this.lookup.size() > 0)
      builder.append(TaskBundle.format(TaskMessage.LOOKUP_TRANSFORMATION,       this.lookup.toString()));

    if (this.entitlement.size() > 0) {
      for (String name : this.entitlement.keySet())
        builder.append(TaskBundle.format(TaskMessage.ENTITLEMENT_MAPPING, name, this.entitlement.get(name).toString()));
    }
    return builder.toString();
  }
}
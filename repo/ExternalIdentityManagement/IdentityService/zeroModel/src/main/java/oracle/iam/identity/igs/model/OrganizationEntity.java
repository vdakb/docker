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

    File        :   OrganizationEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OrganizationEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

////////////////////////////////////////////////////////////////////////////////
// class OrganizationEntity
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An <code>OrganizationEntity</code> represents a logical container of
 ** entities such as identities and other organizations in Identity Manager.
 ** <br>
 ** <code>OrganizationEntity</code> in Identity Manager is used only for
 ** security purposes.
 ** <p>
 ** <code>OrganizationEntity</code> allow you to:
 ** <ul>
 **   <li>Logically and securely manage user accounts and administrators.
 **   <li>Limit access to users, applications, roles, and entitlements.
 ** </ul>
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="organization"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://www.oracle.com/schema/igs}entity"&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
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
public class OrganizationEntity extends AbstractEntity<OrganizationEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-656439820572494295")
  private static final long serialVersionUID = -96977532129678950L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>OrganizationEntity</code> with the specified public
   ** identifier but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the
   ** <code>OrganizationEntity</code> belongs must be populated in manually.
   **
   ** @param  id                 the public identifier of
   **                            <code>OrganizationEntity</code> (usually the
   **                            value of the <code>Organization Name</code> for
   **                            the organization in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  OrganizationEntity(final String id) {
    // ensure inheritance
    super(id);
  }
}

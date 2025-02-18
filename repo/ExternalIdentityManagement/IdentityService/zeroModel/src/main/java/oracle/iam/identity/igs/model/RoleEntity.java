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

    File        :   RoleEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

////////////////////////////////////////////////////////////////////////////////
// class RoleEntity
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>RoleEntity</code>'s make it easier to assign access levels to
 ** identities and to audit those assignments on an ongoing basis.
 ** <p>
 ** Identity Manager provides a comprehensive set of role-based access control
 ** capabilities. <code>RoleEntity</code>-based access control ensures higher
 ** visibility and ease in assigning and unassigning access privileges to
 ** identities, enforces the notion of least privilege, and enables compliance
 ** and audit insight.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="role"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://www.oracle.com/schema/igs}entity"&gt;
 *+       &lt;sequence&gt;
 **         &lt;element name="identities"    type="{http://www.oracle.com/schema/igs}member"      minOccurs="0" maxOccurs="unbounded"/&gt;
 **         &lt;element name="organizations" type="{http://www.oracle.com/schema/igs}publication" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;/sequence&gt;
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
public class RoleEntity extends AbstractEntity<RoleEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1346688114567837109")
  private static final long serialVersionUID = 1978439389645712457L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>RoleEntity</code> with the specified public identifier
   ** but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>RoleEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 public identifier of <code>RoleEntity</code>
   **                            (usually the value of the
   **                            <code>Tole Name</code> for the role in Identity
   **                            Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  RoleEntity(final String id) {
    // ensure inheritance
    super(id);
  }
}
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

    File        :   NamespaceEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    NamespaceEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.util.Collection;

import java.util.stream.Collectors;

////////////////////////////////////////////////////////////////////////////////
// class NamespaceEntity
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A <code>NamespaceEntity</code> that wrappes the details of an
 ** <code>NamespaceEntity</code> information.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="actions" type="{http://www.oracle.com/schema/igs}entitlement" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="namespace" use="required" type="{http://www.oracle.com/schema/igs}token" /&gt;
 **     &lt;/restriction&gt;
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
 ** @see     AccountEntity
 ** @see     EntitlementEntity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class NamespaceEntity extends ContainerEntity<NamespaceEntity, EntitlementEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6164216805760333471")
    private static final long serialVersionUID = 7220037808589465042L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>NamespaceEntity</code> with the specified public
   ** identifier but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>NamespaceEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 the public identifier of
   **                            <code>NamespaceEntity</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  NamespaceEntity(final String id) {
    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toAssign
  /**
   ** Returns the collection of {@link EntitlementEntity}'s to assign for the
   ** <code>NamespaceEntity</code>.
   **
   ** @return                    the collection of {@link EntitlementEntity}'s
   **                            marked as assignable in the collection of
   **                            {@link EntitlementEntity}'s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link EntitlementEntity}.
   */
  public final Collection<EntitlementEntity> toAssign() {
    return this.element.stream().filter(e -> e.is(EntitlementEntity.Action.assign)).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toRevoke
  /**
   ** Returns the collection of {@link EntitlementEntity}'s to revoke for the
   ** <code>NamespaceEntity</code>.
   **
   ** @return                    the collection of {@link EntitlementEntity}'s
   **                            marked as revokable in the collection of
   **                            {@link EntitlementEntity}'s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link EntitlementEntity}.
   */
  public final Collection<EntitlementEntity> toRevoke() {
    return this.element.stream().filter(e -> e.is(EntitlementEntity.Action.revoke)).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toModify
  /**
   ** Returns the collection of {@link EntitlementEntity}'s to modify for the
   ** <code>NamespaceEntity</code>.
   **
   ** @return                    the collection of {@link EntitlementEntity}'s
   **                            marked as modifiable in the collection of
   **                            {@link EntitlementEntity}'s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link EntitlementEntity}.
   */
  public final Collection<EntitlementEntity> toModify() {
    return this.element.stream().filter(e -> e.is(EntitlementEntity.Action.modify)).collect(Collectors.toList());
  }
}
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

    File        :   ApplicationEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.util.Collection;

import java.util.stream.Collectors;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationEntity
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An <code>ApplicationEntity</code> that wrappes the details of accounts
 ** provisioned for or revoked from an identity belonging to an target system.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="application"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="accounts"    type="{http://www.oracle.com/schema/igs}account" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="application" type="{http://www.oracle.com/schema/igs}token"   use="required"/&gt;
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
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ApplicationEntity extends ContainerEntity<ApplicationEntity, AccountEntity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3971298368758689869")
  private static final long serialVersionUID = 2280831635074051408L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ApplicationEntity</code> with the specified public
   ** identifier but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier the <code>ApplicationEntity</code> belongs
   ** to has to be populated manually.
   **
   ** @param  id                 the public identifier of the
   **                            <code>ApplicationEntity</code> (usually the
   **                            name of the <code>Application Instance</code>
   **                            in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  ApplicationEntity(final String id) {
    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toCreate
  /**
   ** Returns the collection {@link AccountEntity}s to create for the
   ** <code>ApplicationEntity</code>.
   **
   ** @return                    the collection of {@link AccountEntity}s marked
   **                            as assignable in the collection of
   **                            {@link AccountEntity}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link AccountEntity}.
   */
  public final Collection<AccountEntity> toCreate() {
    return this.element.stream().filter(e -> e.is(AccountEntity.Action.create)).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toDelete
  /**
   ** Returns the collection {@link AccountEntity} to delete from the
   ** <code>ApplicationEntity</code>.
   **
   ** @return                    the collection of {@link AccountEntity}s marked
   **                            as revokable in the collection of
   **                            {@link AccountEntity}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link AccountEntity}.
   */
  public final Collection<AccountEntity> toDelete() {
    return this.element.stream().filter(e -> e.is(AccountEntity.Action.delete)).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toModify
  /**
   ** Returns the collection {@link AccountEntity} to modify in the
   ** <code>ApplicationEntity</code>.
   **
   ** @return                    the collection of {@link AccountEntity}s marked
   **                            as modifieable in the collection of
   **                            {@link AccountEntity}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link AccountEntity}.
   */
  public final Collection<AccountEntity> toModify() {
    return this.element.stream().filter(e -> e.is(AccountEntity.Action.modify)).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toEnable
  /**
   ** Returns the collection {@link AccountEntity} to enable in the
   ** <code>ApplicationEntity</code>.
   **
   ** @return                    the collection of {@link AccountEntity}s marked
   **                            as enabled in the collection of
   **                            {@link AccountEntity}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link AccountEntity}.
   */
  public final Collection<AccountEntity> toEnable() {
    return this.element.stream().filter(e -> e.is(AccountEntity.Action.enable)).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toDisable
  /**
   ** Returns the collection {@link AccountEntity} to enable in the
   ** <code>ApplicationEntity</code>.
   **
   ** @return                    the collection of {@link AccountEntity}s marked
   **                            as disabled in the collection of
   **                            {@link AccountEntity}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link AccountEntity}.
   */
  public final Collection<AccountEntity> toDisable() {
    return this.element.stream().filter(e -> e.is(AccountEntity.Action.disable)).collect(Collectors.toList());
  }
}
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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Unique Identifier Assembler

    File        :   Tenant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Tenant.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.model;

import java.util.List;
import java.util.Collections;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import bka.iam.identity.jpa.provider.Named;

////////////////////////////////////////////////////////////////////////////////
// class Tenant
// ~~~~~ ~~~~~~
/**
 ** The <code>Tenant</code> database resources.
 ** <p>
 ** The primary intention of this implementation is providing a permissions to
 ** service users.
 ** <pre>
 **   Einführung einer personenbezogenen ID im Progamm Polizei 2020
 **   Unique Identifier (UID)
 **   Version 1.2 | Stand 15.03.2022
 ** </pre>
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** <i>CascadeType.REMOVE</i> applied at the collection of {@link Claim}s is the
 ** way to delete or child entity or entities whenever the deletion of the
 ** <code>Tenant</code> itself happens. Due to the requirement that a
 ** <code>Tenant</code> can only be deactivated its a minor issue to annotate
 ** such behavior on the relationship. More important is to annotate the
 ** relation with <i>orphanRemoval=true</i> to delete orphaned entities from the
 ** persistence layer. An entity that is no longer attached to its parent is the
 ** definition of being an orphan.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The counterpart of the relation declared in {@link Claim} must not be
 ** annotated with any <i>CascadeType</i> or <i>orphanRemoval</i>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=Tenant.NAME)
@Table(name=Tenant.ENTITY)
public class Tenant extends Named<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3409437420526743588")
  private static final long serialVersionUID = -1258335465458798140L;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME             = "Tenant";

  /** The name of the persistent entity */
  public static final String ENTITY           = "uit_tenants";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @Column(name=ID, nullable=false, unique=true, updatable=false, length=20)
  private String             id;

  @OneToMany(mappedBy="tenant", fetch=FetchType.LAZY, orphanRemoval=true, cascade={CascadeType.REMOVE, CascadeType.PERSIST})
  protected List<Claim>      claim;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** Attribute descriptor of the database entity.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Only database server managed attributes enlisted.
   */
  public enum Attribute {
      /** The name of the identifying attribute. */
      ID(Named.ID)
      /** The visibilty status attribute. */
    , ACTIVE(Named.ACTIVE)
      /** The human readable naming attribute. */
    , NAME(Named.NAME)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String  id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> that allows use as a JavaBean.
     **
     ** @param  id               the value representation of the
     **                          <code>Attribute</code> identifier.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Attribute(final String id) {
      // initailize instance attributes
      this.id = id;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Tenant</code> database resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public Tenant() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>Tenant</code> database resource.
   **
   ** @param  id                 the primary identifier of the
   **                            <code>Tenant</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Tenant(final String id) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setClaim
  /**
   ** Sets the claim collection.
   **
   ** @param  value              the claim collection.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Claim}.
   */
  public final void setClaim(final List<Claim> value) {
    this.claim = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClaim
  /**
   ** Returns the claim collection.
   ** <p>
   ** This accessor method returns a reference to the live {@link List}, not a
   ** snapshot. Therefore any modification you make to the returned list will be
   ** present inside the JPA entity.
   **
   ** @return                    the claim collection.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Claim}.
   */
  public final List<Claim> getClaim() {
    return this.claim != null ? this.claim : Collections.emptyList();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId (Identifiable)
  /**
   ** Sets the <code>id</code> property of the <code>Tenant</code>.
   **
   ** @param  value              the <code>id</code> property of the
   **                            <code>Tenant</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setId(final String value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId (Identifiable)
  /**
   ** Returns the <code>id</code> property of the <code>Tenant</code>.
   **
   ** @return                    the <code>id</code> property of the
   **                            <code>Tenant</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getId() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>Tenant</code> database resource.
   **
   ** @param  id                 the primary identifier of the
   **                            <code>Tenant</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Tenant</code> populated with the
   **                            given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>Tenant</code>.
   */
  public static Tenant build(final String id) {
    return new Tenant(id);
  }
}
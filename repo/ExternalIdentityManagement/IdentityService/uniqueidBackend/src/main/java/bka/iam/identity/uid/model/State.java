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

    File        :   State.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    State.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.model;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;

import bka.iam.identity.jpa.provider.Named;

import javax.validation.constraints.Size;

////////////////////////////////////////////////////////////////////////////////
// class State
// ~~~~~ ~~~~~
/**
 ** The <code>State</code> database resources.
 ** <p>
 ** The primary intention of this implementation is providing a catalog lookup
 ** to validate identitifier belonging to the environment type catalog.
 ** <pre>
 **   Einführung einer personenbezogenen ID im Progamm Polizei 2020
 **   Unique Identifier (UID)
 **   Version 1.2 | Stand 15.03.2022
 **
 **   Reference to Page 21 Table 2 Row 6 Codelist - Vorgabe durch P2020 PG IAM
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=State.NAME)
@Table(name=State.ENTITY)
public class State extends Named<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME             = "State";

  /** The name of the persistent entity */
  public static final String ENTITY           = "uit_states";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:959449299099231798")
  private static final long  serialVersionUID = -8024703969614187828L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @Size(min=1, max=2)
  @Column(name=ID, nullable=false, unique=true, updatable=false, length=3)
  private String id;

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
      /** The name of the identifying attribute */
      ID(Named.ID)
      /** The visibilty status attribute */
    , ACTIVE(Named.ACTIVE)
      /** The human readable naming attribute */
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
   ** Constructs an empty <code>State</code> database resource that allows use
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
  public State() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>State</code> database resource.
   **
   ** @param  id                 the primary identifier of the
   **                            <code>State</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private State(final String id) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId (Identifiable)
  /**
   ** Sets the <code>id</code> property of the <code>State</code>.
   **
   ** @param  value              the <code>id</code> property of the
   **                            <code>State</code> to set.
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
   ** Returns the <code>id</code> property of the <code>State</code>.
   **
   ** @return                    the <code>id</code> property of the
   **                            <code>State</code>.
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
   ** Factory method to create a minimal <code>State</code> database
   ** resource.
   **
   ** @param  id                 the primary identifier of the
   **                            <code>State</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>State</code> populated with the
   **                            given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>State</code>.
   */
  public static State build(final String id) {
    return new State(id);
  }
}
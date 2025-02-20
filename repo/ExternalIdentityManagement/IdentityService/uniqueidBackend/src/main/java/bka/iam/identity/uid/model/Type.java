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

    File        :   Type.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Type.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.model;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.validation.constraints.Size;

import bka.iam.identity.jpa.provider.Described;

////////////////////////////////////////////////////////////////////////////////
// class Type
// ~~~~~ ~~~~
/**
 ** The <code>Type</code> database resources.
 ** <p>
 ** The primary intention of this implementation is providing a catalog lookup
 ** to validate identitifier belonging to the identity type catalog.
 ** <pre>
 **   Einführung einer personenbezogenen ID im Progamm Polizei 2020
 **   Unique Identifier (UID)
 **   Version 1.2 | Stand 15.03.2022
 **
 **   Reference to Page 21 Table 2 Row 1 Codelist - Vorgabe durch P2020 PG IAM
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=Type.NAME)
@Table(name=Type.ENTITY)
public class Type extends Described<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME             = "Type";

  /** The name of the persistent entity */
  public static final String ENTITY           = "uit_types";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2955308078489807017")
  private static final long  serialVersionUID = -6417473048619495707L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @Size(min=3, max=3)
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
      /** The name of the identifying attribute. */
      ID(Described.ID)
      /** The visibilty status attribute. */
    , ACTIVE(Described.ACTIVE)
      /** The human readable naming attribute. */
    , NAME(Described.NAME)
      /** The human readable description attribute. */
    , DESCRIPTION("description")
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
   ** Constructs an empty <code>Type</code> database resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public Type() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>Type</code> database resource.
   **
   ** @param  id                 the primary identifier of the <code>Type</code>
   **                            database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Type(final String id) {
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
   ** Sets the <code>id</code> property of the <code>Type</code>.
   **
   ** @param  value              the <code>id</code> property of the
   **                            <code>Type</code> to set.
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
   ** Returns the <code>id</code> property of the <code>Type</code>.
   **
   ** @return                    the <code>id</code> property of the
   **                            <code>Type</code>.
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
   ** Factory method to create a minimal <code>Type</code> database resource.
   **
   ** @param  id                 the primary identifier of the <code>Type</code>
   **                            database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Type</code> populated with the given
   **                            <code>id</code>.
   **                            <br>
   **                            Possible object is <code>Type</code>.
   */
  public static Type build(final String id) {
    return new Type(id);
  }
}
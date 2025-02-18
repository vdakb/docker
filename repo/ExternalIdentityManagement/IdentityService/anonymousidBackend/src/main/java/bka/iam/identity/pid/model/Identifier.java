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
    Subsystem   :   Anonymous Identifier Assembler

    File        :   State.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    State.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.pid.model;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Convert;

import bka.iam.identity.jpa.provider.Base;

import oracle.hst.platform.jpa.converter.Boolean2Integer;

////////////////////////////////////////////////////////////////////////////////
// class Identifier
// ~~~~~ ~~~~~~~~~~
/**
 ** The anonymous <code>Identifier</code> database resources.
 ** <p>
 ** The object intentionally used to provided uniqueness of an identifier.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name="Identifier")
@Table(name=Identifier.ENTITY)
public class Identifier extends Base<String>{

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME             = "Identifier";

  /** The name of the entity */
  public static final String ENTITY           = "pit_identifiers";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2851193627417366372")
  private static final long  serialVersionUID = 617332110895972436L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @Column(name="id", nullable=false, unique=true, updatable=false, length=8)
  private String             id;

  @Column(name="active", nullable=false)
  @Convert(converter=Boolean2Integer.class)
  private Boolean            active = true;

  @Column(name="usedby", nullable=false, updatable=false, length=36)
  private String             usedby;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** Attribute descriptor of the database entity
   */
  public enum Attribute {
      ID("id")
    , ACTIVE("active")
    , USEDBY("usedby")
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
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    Attribute(final String id) {
      // initailize instance attributes
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Attribute</code> from the given
     ** <code>id</code> value.
     **
     ** @param  id               the string value the <code>Attribute</code>
     **                          should be returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Attribute</code> mapped at
     **                          <code>id</code>.
     **
     ** @throws IllegalArgumentException if the given <code>id</code> is not
     **                                  mapped to an <code>Attribute</code>.
     */
    public static Attribute from(final String id) {
      for (Attribute cursor : Attribute.values()) {
        if (cursor.id.equals(id))
          return cursor;
      }
      throw new IllegalArgumentException(id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Identifier</code> database resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public Identifier() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>Identifier</code> database resource.
   **
   ** @param  id                 the primary identifier of the
   **                            <code>Identifier</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Identifier(final String id) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUsedBy
  /**
   ** Sets the identifier the anonymous <code>Identifier</code> generated
   ** belongs.
   **
   ** @param  value              the identifier the anonymous
   **                            <code>Identifier</code> generated belongs.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Identifier</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Identifier</code>.
   */
  public final Identifier setUsedBy(final String value) {
    this.usedby = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUsedBy
  /**
   ** Returns the identifier the anonymous <code>Identifier</code> generated
   ** belongs.
   **
   ** @return                    the identifier the anonymous
   **                            <code>Identifier</code> generated belongs.
   **                            <br>
   **                            Possible object is {@link String}.
   */
 public final String getUsedBy() {
    return this.usedby;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setActive
  /**
   ** Sets the <code>active</code> property of the <code>Identifier</code>
   ** entity.
   **
   ** @param  value              the <code>active</code> property of the
   **                            <code>Identifier</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Identifier</code> entity to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>Identifier</code>.
   */
  public final Identifier setActive(final Boolean value) {
    this.active = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActive
  /**
   ** Returns the <code>active</code> property of the <code>Identifier</code>
   ** entity.
   **
   ** @return                    the <code>active</code> property of the
   **                            <code>Identifier</code> entity.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean getActive() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId (Identifiable)
  /**
   ** Sets the identifying attribute of the <code>Identifier</code>.
   **
   ** @param  value              the identifiyng attribute of the
   **                            <code>Identifier</code> to set.
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
   ** Returns the identifying attribute of the <code>Identifier</code>.
   **
   ** @return                    the identifying attribute of the
   **                            <code>Identifier</code>.
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
   ** Factory method to create a minimal <code>Identifier</code> database
   ** resource.
   **
   ** @param  id                 the primary identifier of the
   **                            <code>Identifier</code> database resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Identifier</code> populated with the
   **                            given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>Identifier</code>.
   */
  public static Identifier build(final String id) {
    return new Identifier(id);
  }
}
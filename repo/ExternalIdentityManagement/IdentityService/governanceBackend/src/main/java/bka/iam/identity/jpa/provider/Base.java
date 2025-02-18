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
    Subsystem   :   Identity Governance Service

    File        :   Base.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Base.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.jpa.provider;

import java.util.Calendar;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.MappedSuperclass;

import oracle.hst.platform.jpa.Auditable;
import oracle.hst.platform.jpa.Versionable;
import oracle.hst.platform.jpa.PersistenceEntity;

////////////////////////////////////////////////////////////////////////////////
// abstract class Base
// ~~~~~~~~ ~~~~~ ~~~~
/**
 ** The base class for persited metadata of a resources.
 **
 ** @param  <I>                  the type of the identifiying value
 **                              implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@MappedSuperclass
public abstract class Base<I extends Comparable<I> & Serializable> extends    PersistenceEntity<I>
                                                                   implements Auditable<String, Calendar>
                                                                   ,          Versionable<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The string representing the field name <code>"name"</code>. */
  public static final String NAME             = "name";

  /** The string representing the field name <code>"active"</code>. */
  public static final String ACTIVE           = "active";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:179546634843081766")
  private static final long  serialVersionUID = -5482960441422313279L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /*
   * ===========================================================================
   * Note:
   * ---------------------------------------------------------------------------
   * The attributes declared below are all defined as required in the database
   * but are also managed by the database.
   * For this reason, 'nullable" is not set to false in the annotations of these
   * attributes to prevent that values have to be set for these attributes when
   * generating the statements.
   * ===========================================================================
   */

  @Column(name="rowversion", insertable=false, updatable=false, length=30)
  protected String          version;

  @Column(name="created_by", insertable=false, updatable=false, length=32)
  protected String          createdBy;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="created_on", insertable=false, updatable=false)
  protected Calendar        createdOn;

  @Column(name="updated_by", insertable=false, updatable=false, length=32)
  protected String          updatedBy;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="updated_on", insertable=false, updatable=false)
  protected Calendar        updatedOn;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** Attribute descriptor of the database entity.
   */
  public enum Attribute {
      VERSION("version")
    , CREATEDBY("createdBy")
    , CREATEDON("createdOn")
    , UPDATEDBY("updatedBy")
    , UPDATEDON("updatedOn")
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
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty persistable <code>Base</code> instance that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Base() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVersion (Versionisable)
  /**
   ** Sets the <code>version</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>version</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setVersion(final String value) {
    this.version = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVersion  (Versionisable)
  /**
   ** Returns the <code>version</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>version</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getVersion() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCreatedBy (Auditable)
  /**
   ** Sets the <code>createdBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>createdBy</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setCreatedBy(final String value) {
    this.createdBy = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCreatedBy (Auditable)
  /**
   ** Returns the <code>createdBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>createdBy</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getCreatedBy() {
    return this.createdBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCreatedOn (Auditable)
  /**
   ** Sets the <code>createdOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>createdOn</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   */
  @Override
  public final void setCreatedOn(final Calendar value) {
    this.createdOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCreatedOn (Auditable)
  /**
   ** Returns the <code>createdOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>createdOn</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  @Override
  public final Calendar getCreatedOn() {
    return this.createdOn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUpdatedBy (Auditable)
  /**
   ** Sets the <code>updatedBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>updatedBy</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void setUpdatedBy(final String value) {
    this.updatedBy = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUpdatedBy (Auditable)
  /**
   ** Returns the <code>updatedBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>updatedBy</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String getUpdatedBy() {
    return this.updatedBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUpdatedOn (Auditable)
  /**
   ** Sets the <code>updatedOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>updatedOn</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   */
  @Override
  public final void setUpdatedOn(final Calendar value) {
    this.updatedOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUpdatedOn (Auditable)
  /**
   ** Returns the <code>updatedOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>updatedOn</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  @Override
  public final Calendar getUpdatedOn() {
    return this.updatedOn;
  }
}
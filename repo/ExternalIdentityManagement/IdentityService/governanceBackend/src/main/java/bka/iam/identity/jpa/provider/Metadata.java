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

    File        :   Metadata.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Metadata.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.jpa.provider;

import java.util.Objects;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Embeddable;
import javax.persistence.TemporalType;

import oracle.hst.platform.jpa.Auditable;
import oracle.hst.platform.jpa.Versionable;

////////////////////////////////////////////////////////////////////////////////
// class Metadata
// ~~~~~ ~~~~~~~~
/**
 ** The base class for persited metadata of a resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Embeddable
public class Metadata implements Auditable<String, Calendar>
                      ,          Versionable<String> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX          = "meta";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4487008145667973923")
  private static final long serialVersionUID = 3903099539174439555L;

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
   ** Constructs an empty persistable <code>Metadata</code> instance that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Metadata() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Persistable)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of
   **       the two objects must produce distinct integer results. However,
   **       the programmer should be aware that producing distinct integer
   **       results for unequal objects may improve the performance of hash
   **       tables.
   ** </ul>
   ** The implementation use the identifier of the persisted entity if available
   ** only. This is sufficient because the identifier is the primary key of the
   ** entity.
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.version, this.createdBy, this.createdOn, this.updatedBy, this.updatedOn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (Persistable)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Metadata</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Metadata</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other            the reference object with which to compare.
   **                          <br>
   **                          Allowed object is {@link Object}.
   **
   ** @return                  <code>true</code> if this object is the same as
   **                          the object argument; <code>false</code>
   **                          otherwise.
   **                          <br>
   **                          Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Metadata that = (Metadata)other;
    return Objects.equals(this.version,   that.version)
        && Objects.equals(this.createdBy, that.createdBy)
        && Objects.equals(this.createdOn, that.createdOn)
        && Objects.equals(this.updatedBy, that.updatedBy)
        && Objects.equals(this.updatedOn, that.updatedOn)
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Persistable)
  /**
   ** Returns the string representation for the <code>Metadata</code> entity in
   ** its minimal form, without any additional whitespace.
   **
   ** @return                    a string representation that represents this
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), "@" + hashCode());
  }

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
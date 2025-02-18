/*
    Oracle Deutschland GmbH

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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   JPA Unit Testing

    File        :   Metadata.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Metadata.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.junit.model;

import java.util.Date;
import java.util.Objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.Embeddable;
import javax.persistence.TemporalType;

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
@SuppressWarnings("oracle.jdeveloper.java.annotation-callback")
public class Metadata implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX          = "meta";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4193668327544957969")
  private static final long serialVersionUID = 5788592935786098259L;

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

  @Temporal(TemporalType.DATE)
  @Column(name="created_on", insertable=false, updatable=false)
  protected Date            createdOn;

  @Column(name="updated_by", insertable=false, updatable=false, length=32)
  protected String          updatedBy;

  @Temporal(TemporalType.DATE)
  @Column(name="updated_on", insertable=false, updatable=false)
  protected Date            updatedOn;

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
  // Method:   setVersion
  /**
   ** Sets the <code>version</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>version</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setVersion(final String value) {
    this.version = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getVersion
  /**
   ** Returns the <code>version</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>version</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getVersion() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCreatedBy
  /**
   ** Sets the <code>createdBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>createdBy</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setCreatedBy(final String value) {
    this.createdBy = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCreatedBy
  /**
   ** Returns the <code>createdBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>createdBy</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getCreatedBy() {
    return this.createdBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCreatedOn
  /**
   ** Sets the <code>createdOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>createdOn</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Date}.
   */
  public final void setCreatedOn(final Date value) {
    this.createdOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCreatedOn
  /**
   ** Returns the <code>createdOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>createdOn</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public final Date getCreatedOn() {
    return this.createdOn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUpdatedBy
  /**
   ** Sets the <code>updatedBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>updatedBy</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setUpdatedBy(final String value) {
    this.updatedBy = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUpdatedBy
  /**
   ** Returns the <code>updatedBy</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>updatedBy</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getUpdatedBy() {
    return this.updatedBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUpdatedOn
  /**
   ** Sets the <code>updatedOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @param  value              the <code>updatedOn</code> property of the
   **                            <code>Metadata</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Date}.
   */
  public final void setUpdatedOn(final Date value) {
    this.updatedOn = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUpdatedOn
  /**
   ** Returns the <code>updatedOn</code> property of the <code>Metadata</code>
   ** entity.
   **
   ** @return                    the <code>updatedOn</code> property of the
   **                            <code>Metadata</code> entity.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public final Date getUpdatedOn() {
    return this.updatedOn;
  }
}
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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic REST Interface

    File        :   ErrorResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ErrorResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.rest.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import oracle.iam.system.simulation.rest.schema.Resource;

import oracle.iam.system.simulation.scim.utility.StatusSerializer;
import oracle.iam.system.simulation.scim.utility.StatusDeserializer;

////////////////////////////////////////////////////////////////////////////////
// class ErrorResponse
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object is returned whenever by REST when an error occurs.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** <br>
 ** This class isnt't annotated by a schema annotation because the intended
 ** usage is for clients only where the schema isn't importatnt
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ErrorResponse implements Resource<ErrorResponse> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("status")
  @JsonSerialize(using=StatusSerializer.class)
  @JsonDeserialize(using=StatusDeserializer.class)
  private final int status;

  @JsonProperty("type")
  private String    type;

  @JsonProperty("detail")
  private String    detail;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a REST <code>ErrorResponse</code> with the provided status.
   **
   ** @param  status             the HTTP Status of the REST error.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @JsonCreator
  public ErrorResponse(@JsonProperty(value="status", required=true) final int status) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.status = status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the HTTP status of the REST error.
   **
   ** @return                    the HTTP status of the REST error.
   **                            <br>
   **                            Possible object array of <code>int</code>.
   */
  public final Integer status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the detailed error keyword.
   **
   ** @param  value              the detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ErrorResponse} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ErrorResponse}.
   */
  public final ErrorResponse type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the detailed error keyword.
   **
   ** @return                    the detailed error keyword.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detail
  /**
   ** Sets the detailed, human readable message.
   **
   ** @param  value              the detailed, human readable message.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ErrorResponse} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ErrorResponse}.
   */
  public final ErrorResponse detail(final String value) {
    this.detail = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detail
  /**
   ** Returns the detailed, human readable message.
   **
   ** @return                    the detailed, human readable message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String detail() {
    return this.detail;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (type   != null ? type.hashCode()   : 0);
    result = 31 * result + (detail != null ? detail.hashCode() : 0);
    result = 31 * result + status;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>SchemaResource</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>SchemaResource</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    if (!super.equals(other)) {
      return false;
    }
    final ErrorResponse that = (ErrorResponse)other;
    if (this.status != that.status) {
      return false;
    }
    if (this.detail != null ? !this.detail.equals(that.detail) : that.detail != null) {
      return false;
    }
    return (this.type == null ? that.type == null : this.type.equals(that.type));
  }
}
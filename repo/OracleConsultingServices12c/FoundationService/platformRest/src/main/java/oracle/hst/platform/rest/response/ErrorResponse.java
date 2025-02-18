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

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   ErrorResponse.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ErrorResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.response;

import java.util.Objects;

import java.io.Serializable;

import javax.json.Json;
import javax.json.JsonObject;

////////////////////////////////////////////////////////////////////////////////
// class ErrorResponse
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object is returned by REST whenever an error occurs.
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
public class ErrorResponse implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The property name of the HTTP status code in a REST Error response. */
  public static final String STATUS           = "status";

  /** The property name of the error keyword in a REST Error response. */
  public static final String TYPE             = "type";

  /**
   ** The property name of the detailed, human readable message in a REST Error
   ** response.
   */
  public static final String DETAIL           = "detail";

  /**
   ** The property name of the description related to the error status.
   */
  public static final String DESCRIPTION      = "description";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7865724973853684382")
  private static final long  serialVersionUID = -5978311774491287355L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int status;
  private Type      type;
  private String    detail;
  private String    description;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** The SCIM detailed error keywords of a BadRequest (HTTP-400) response
   ** status.
   */
  public enum Type {
      /**
       ** A fallback value used if a Service Provider like PCF is unwilling
       ** to specify a type in the error response.
       */
      NONE("none"),

    /**
     ** The specified filter yields many more results than the server is
     ** willing to calculate or process.
     */
    TOO_MANY("tooMany"),

    /**
     ** The attempted modification is not compatible with the target
     ** attributes mutability or current state (e.g., modification of an
     ** immutable attribute with an existing value).
     */
    MUTABILITY("mutability"),

    /**
     ** One or more of the attribute values is already in use or is reserved.
     */
    UNIQUENESS("uniqueness"),

    /**
     ** The specified request cannot be completed, due to the passing of
     ** sensitive (e.g., personal) information in a request URI.
     */
    SENSITIVE("sensitive"),

    /**
     ** The specified filter syntax was invalid or the specified attribute and
     ** filter comparison combination is not supported.
     */
    INVALID_FILTER("invalidFilter"),

    /**
     ** The request body message structure was invalid or did not conform to
     ** the request schema.
     */
    INVALID_SYNTAX("invalidSyntax"),

    /**
     ** The path attribute was invalid or malformed.
     */
    INVALID_PATH("invalidPath"),

    /**
     ** A required value was missing, or the value specified was not
     ** compatible with the operation or attribute type (see Section 2.2 of [
     ** <a href="https://tools.ietf.org/html/rfc7643">RFC7643</a>), or
     ** resource schema (see
     ** <a href="https://tools.ietf.org/html/rfc7644#section-4">Section 4</a>
     ** of [<a href="https://tools.ietf.org/html/rfc7643">RFC7643</a>)
     */
    INVALID_VALUE("invalidValue"),

    /**
     ** The specified SCIM protocol version is not supported
     */
    INVALID_VERSION("invalidVers")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Type</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in SCIM schemas) of
     **                          the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Type(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Type</code> constraint from the
     ** given string value.
     **
     ** @param  value            the string value the order constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     */
    public static Type from(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      return NONE;
    }
  }

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
  public ErrorResponse(final int status) {
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
  // Method:   detail
  /**
   ** Sets the description, human readable message.
   **
   ** @param  value              the description, human readable message.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ErrorResponse</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ErrorResponse</code>.
   */
  public final ErrorResponse description(final String value) {
    this.description = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description, human readable message.
   **
   ** @return                    the description, human readable message.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type error keyword.
   **
   ** @param  value              the detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ErrorResponse</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ErrorResponse</code>.
   */
  public final ErrorResponse type(final String value) {
    return type(Type.from(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the type error keyword.
   **
   ** @param  value              the detailed error keyword.
   **                            <br>
   **                            Allowed object is {@link Type}.
   **
   ** @return                    the <code>ErrorResponse</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ErrorResponse</code>.
   */
  public final ErrorResponse type(final Type value) {
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
   **                            Possible object is {@link Type}.
   */
  public final Type type() {
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
   ** @return                    the <code>ErrorResponse</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ErrorResponse</code>.
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
  // Method:   of
  /**
   ** Factory method to create a new <code>ErrorResponse</code> with HTTP Status
   ** a detailed, human readable message
   **
   ** @param  status             the HTTP Status of the REST error.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  detail             the detailed, human readable message.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ErrorResponse</code> populated with
   **                            the provided properties.
   **                            <br>
   **                            Possible object is <code>ErrorResponse</code>.
   */
  public static ErrorResponse of(final int status, final String detail) {
    return new ErrorResponse(status).detail(detail);
  }

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
    return Objects.hash(this.status, this.type, this.detail, this.description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>ErrorResponse</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>ErrorResponse</code>s may be different even though they contain the
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
    final ErrorResponse that = (ErrorResponse)other;
    return Objects.equals(this.status,      that.status)
        && Objects.equals(this.type,        that.type)
        && Objects.equals(this.detail,      that.detail)
        && Objects.equals(this.description, that.description)
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  /**
   ** Returns a string representation in JSON format of this instance.
   **
   ** @return                  the string representation of this instance.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final JsonObject jsonify() {
    return Json.createObjectBuilder()
      .add(STATUS,      this.status)
      .add(TYPE,        this.type.value)
      .add(DETAIL,      this.detail)
      .add(DESCRIPTION, this.description)
      .build();
  }
}
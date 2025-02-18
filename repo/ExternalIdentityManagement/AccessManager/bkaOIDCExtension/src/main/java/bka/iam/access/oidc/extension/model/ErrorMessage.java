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

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   ErrorMessage.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    ErrorMessage.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-07  TSebo     First release version
*/
package bka.iam.access.oidc.extension.model;

import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// class ErrorMessage
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Data Value Object for Error Message
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ErrorMessage {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String error;
  private String description;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ErrorMessage</code> allows use as a JavaBean.
   ** <br>
   ** Default Constructor
   */
  public ErrorMessage() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ErrorMessage</code> with the given erro code and
   ** description.
   **
   ** @param  error              the error code of the error response.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  description        the error description of the error response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public ErrorMessage(final String error, final String description) {
    // ensure inheritance
    super();

    this.error       = error;
    this.description = description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setError
  /**
   ** Sets the error code of the error response.
   **
   ** @param  value              the error code of the error response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setError(final String value) {
    this.error = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getError
  /**
   ** Returns the error code of the error response.
   **
   ** @return                    the error code of the error response.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getError() {
    return this.error;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setError_description
  /**
   ** Sets the error description of the error response.
   **
   ** @param  value              the error description of the error response.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setError_description(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getError_description
  /**
   ** Returns the error description of the error response.
   **
   ** @return                    the error description of the error response.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getError_description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorization
  /**
   ** Factory method to create a new <code>ErrorMessage</code> with HTTP Status
   ** <code>400</code> and a type of <code>invalid_request</code>.
   **
   ** @return                    the <code>ErrorMessage</code> populated with
   **                            the provided properties.
   **                            <br>
   **                            Possible object is <code>ErrorMessage</code>.
   */
  public static ErrorMessage authorization() {
    return invalidRequest("Authorization header or x-oauth-identity-domain-name header is missing");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidRequest
  /**
   ** Factory method to create a new <code>ErrorMessage</code> with HTTP Status
   ** <code>400</code> and a type of <code>invalid_request</code>.
   **
   ** @param  description        the error description of the error response.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ErrorMessage</code> populated with
   **                            the provided properties.
   **                            <br>
   **                            Possible object is <code>ErrorMessage</code>.
   */
  public static ErrorMessage invalidRequest(final String description) {
    return of("invalid_request", description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a new <code>ErrorMessage</code> with HTTP Status
   ** a detailed, human readable message
   **
   ** @param  error              the error code of the error response.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  description        the error description of the error response.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ErrorMessage</code> populated with
   **                            the provided properties.
   **                            <br>
   **                            Possible object is <code>ErrorMessage</code>.
   */
  public static ErrorMessage of(final String error, final String description) {
    return new ErrorMessage(error, description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overidden)
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
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
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
    return Objects.hash(this.error, this.description);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>ErrorMessage</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>ErrorMessage</code>s may be different even though they contain the
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
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final ErrorMessage that = (ErrorMessage)other;
    return (this.error != null ? this.error.equals(that.error) : that.error != null)
        && (this.description != null ? this.description.equals(that.description) : that.description != null)
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overidden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append("\"error\": \"").append(this.error).append("\",");
    sb.append("\"error_description\": \"").append(this.description).append("\"");
    sb.append("}");
    return sb.toString();
  }
}
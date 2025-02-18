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

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Error.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Error.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Error
// ~~~~~ ~~~~~
/**
 ** This object is returned whenever by REST when an error occurs.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This class isnt't annotated by a schema annotation because the intended
 ** usage is for clients only where the schema isn't important.
 ** <p>
 ** This class is a look ahead implementation of the SCIM services but
 ** implemented on the generic REST due to have for all services a proper error
 ** handling.
 ** <p>
 ** The SCIM protocol uses the HTTP response status codes defined in  of
 ** <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-6"> Section 6 [RFC7231]</a>
 ** to indicate operation success or failure. In addition to returning an HTTP
 ** response code, implementers MUST return the errors in the body of the
 ** response in a JSON format, using the attributes described below.
 ** Error responses are identified using the following "schema" URI:
 ** <code>urn:ietf:params:scim:api:messages:2.0:Error</code>. The following
 ** attributes are defined for a SCIM error response using a JSON body:
 ** <ul>
 **   <li>status   - The HTTP status code (see Section 6 of
 **                  <a href="https://datatracker.ietf.org/doc/html/rfc7231#section-6"> Section 6 [RFC7231]</a>
 **                  expressed as a JSON string.
 **                  <br>
 **                  This attribute is <b>required</b>.
 **   <li>type     - A detail SCIM error keyword.
 **                  <br>
 **                  This attribute is <b>optional</b>.
 **   <li>detail   - A detailed human-readable message.
 **                  <br>
 **                  This attribute is <b>optional</b>.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Error implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The property name of the HTTP status code in a REST Error response. */
  public static final String CODE             = "error";

  /**
   ** The property name of the detailed, human readable message in a SCIM Error
   ** response.
   */
  public static final String DETAIL           = "error_description";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8089184984696298991")
  private static final long  serialVersionUID = 7885525101189584395L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The HTTP status code in an error response. */
  private final int          status;
  /** The detailed, human readable message in  an error response. */
  private String             code;
  /** The detailed, human readable message in  an error response. */
  private String             detail;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a REST <code>Error</code> with the provided status.
   **
   ** @param  status             the HTTP Status of the REST error.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public Error(final int status) {
    // ensure inheritance
    super();

      // initialize instance attributes
    this.status = status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the HTTP status of the SCIM error.
   **
   ** @return                    the HTTP status of the SCIM error.
   **                            <br>
   **                            Possible object array of <code>int</code>.
   */
  public final Integer status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Sets the human readable message code.
   **
   ** @param  value              the human readable message code.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Error</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Error</code>.
   */
  public final Error code(final String value) {
    this.code = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   code
  /**
   ** Returns the human readable message code.
   **
   ** @return                    the human readable message code.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String code() {
    return this.code;
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
   ** @return                    the <code>Error</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Error</code>.
   */
  public final Error detail(final String value) {
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
    int result = this.status;
    result = 31 * result + (this.code   != null ? this.code.hashCode()   : 0);
    result = 31 * result + (this.detail != null ? this.detail.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Error</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Error</code>s may be different even though they contain the
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
    final Error that = (Error)other;
    if (this.status != that.status) {
      return false;
    }
    if (this.code != null ? !this.code.equals(that.code) : that.code != null) {
      return false;
    }
    return (this.detail != null ? !this.detail.equals(that.detail) : that.detail != null);
  }
}
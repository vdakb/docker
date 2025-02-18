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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Error.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Error.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.symbol;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.service.captcha.resources.SymbolBundle;

////////////////////////////////////////////////////////////////////////////////
// class Error
// ~~~~~ ~~~~~
/**
 ** The <code>Error</code> response send back to the client.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Error {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty
  private final String code;
  @JsonProperty
  private final String message;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Error</code> with the specified properties.
   **
   ** @param  code               the <code>Error</code> code send back to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the <code>Error</code> message send back to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Error(final String code, final String message) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.code    = code;
    this.message = message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   code
  /**
   ** Returns the code of the <code>Error</code>.
   **
   ** @return                    the code of the <code>Error</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String code() {
    return this.code;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   message
  /**
   ** Returns the message of the <code>Error</code>.
   **
   ** @return                    the message of the <code>Error</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String message() {
    return this.message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Error</code> with the specified properties.
   **
   ** @param  code               the <code>Error</code> code send back to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the error instance pupolated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is <code>Error</code>.
   */
  public static Error build(final String code) {
    return build(code, SymbolBundle.string(code));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Error</code> with the specified
   ** properties.
   **
   ** @param  code               the <code>Error</code> code send back to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the <code>Error</code> message send back to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the error instance pupolated with the
   **                            specified properties.
   **                            <br>
   **                            Allowed object is <code>Error</code>.
   */
  public static Error build(final String code, final String message) {
    return new Error(code, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode
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
  public int hashCode() {
    int result = this.code != null ? this.code.hashCode() : 0;
    result = 31 * result + (this.message != null ? this.message.hashCode() : 0);
    return result;
  }
}
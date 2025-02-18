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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   FilterConfig.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterConfig.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class FilterConfig
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A complex type that specifies filter options.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FilterConfig {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute(description="Boolean value specifying whether the operation is supported.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final boolean supported;

  @Attribute(description="Integer value specifying the maximum number of resources returned in a response.", mutability=Definition.Mutability.READ_ONLY, required=true)
  private final int     maxResults;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new complex type that specifies Etag configuration options.
   **
   ** @param  supported          a boolean value indicating whether the
   **                            operation is supported.
   **                            Allowed object is <code>boolean</code>.
   ** @param  maxResults         a integer value specifying the maximum number
   **                            of resources returned in a response.
   **                            Allowed object is <code>int</code>.
   */
  @JsonCreator
  public FilterConfig(@JsonProperty(value="supported", required=true) final boolean supported, @JsonProperty(value="maxResults", required=true) final int maxResults) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.supported  = supported;
    this.maxResults = maxResults;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supported
  /**
   ** Returns the boolean value indicating whether the operation is supported.
   **
   ** @return                    the boolean value indicating whether this
   **                            operation is supported.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean supported() {
    return this.supported;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maxResults
  /**
   ** Returns the integer value specifying the maximum number of resources
   ** returned in a response.
   **
   ** @return                    the integer value specifying the maximum number
   **                            of resources returned in a response.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int maxResults() {
    return this.maxResults;
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
   */
  @Override
  public int hashCode() {
    int result = (this.supported ? 1 : 0);
    result = 31 * result + this.maxResults;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>FilterConfig</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>FilterConfig</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final FilterConfig that = (FilterConfig)other;
    if (this.maxResults != that.maxResults)
      return false;

    return (this.supported == that.supported);
  }
}
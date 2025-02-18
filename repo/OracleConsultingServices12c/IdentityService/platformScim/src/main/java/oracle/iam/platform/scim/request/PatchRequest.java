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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   PatchRequest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PatchRequest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.request;

import java.util.List;
import java.util.Objects;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.rest.ServiceException;

import oracle.iam.platform.scim.annotation.Schema;
import oracle.iam.platform.scim.annotation.Attribute;

import oracle.iam.platform.scim.schema.Entity;
import oracle.iam.platform.scim.schema.Generic;

////////////////////////////////////////////////////////////////////////////////
// class PatchRequest
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Class representing a SCIM 2 patch request.
 */
@Schema(id="urn:ietf:params:scim:api:messages:2.0:PatchOp", name="Patch Operation", description="SCIM 2.0 Patch Operation Request")
public class PatchRequest extends    Entity<PatchRequest>
                          implements Iterable<PatchOperation> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  /**
   ** The attribute name, whose value is an array of one or more PATCH
   ** operations.
   */
  public static final String OPERATIONS = "Operations";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute(description="Patch Operations", required=true, multiValueClass=PatchOperation.class)
  @JsonProperty(value=OPERATIONS)
  private final List<PatchOperation> operation;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PatchRequest</code> with the specified properties.
   **
   ** @param  operation          the list of operations to include.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link PatchOperation}.
   */
  @JsonCreator
  public PatchRequest(final @JsonProperty(value=OPERATIONS) List<PatchOperation> operation) {
	  // ensure inheritance
    super();

	  // initialize instance attributes
    this.operation = CollectionUtility.unmodifiable(operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Returns all the individual operations in this patch request.
   **
   ** @return                    all the individual operations in this patch request.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link PatchOperation}.
   */
  public List<PatchOperation> operation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Iterable)
  /**
   ** Returns an {@link Iterator} over the elements.
   **
   ** @return                    an {@link Iterator} over the elements.
   **                            <br>
   **                            Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<PatchOperation> iterator() {
    return this.operation.iterator();
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
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>PatchRequest</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>PatchRequest</code>s may be different even though they contain the
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
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    final PatchRequest that = (PatchRequest)other;
    // ensure inheritance
    return super.equals(that) && Objects.equals(this.operation, that.operation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Apply this patch request to the {@link Generic}.
   **
   ** @param  object             the generic resource object to apply this
   **                            patch to.
   **                            <br>
   **                            Allowed object is {@link Generic}.
   **
   ** @throws ServiceException   if the one or more patch operations are
   **                            invalid.
   */
  public void apply(final Generic object)
    throws ServiceException {

    for (PatchOperation operation : this) {
      operation.apply(object.objectNode());
    }
  }
}
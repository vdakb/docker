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

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Resource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.schema;

import java.util.Collection;

import oracle.hst.platform.rest.schema.Discoverable;

////////////////////////////////////////////////////////////////////////////////
// interface Resource
// ~~~~~~~~~ ~~~~~~~~
/**
 ** Interface that can be used to access data from all SCIM objects.
 **
 ** @param  <T>                the type of the resource implementation.
 **                            <br>
 **                            This parameter is used for convenience to allow
 **                            better implementations of the resources
 **                            implementing this interface (resources can return
 **                            their own specific type instead of type defined
 **                            by this interface only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Resource<T extends Resource> extends Discoverable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the multi-valued schema containing the requested
   ** resources.
   */
  static final String SCHEMA     = "schemas";
  /**
   ** The property name of the resource metadata.
   */
  static final String METADATA   = "meta";
  /**
   ** The property name of the identifier for the resource as defined by the
   ** provisioning client.
   */
  static final String EXTERNAL   = "externalId";
  /**
   ** The property name of the unique identifier for a SCIM resource as defined
   ** by the service provider.
   */
  static final String IDENTIFIER = "id";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the identifier of the object.
   **
   ** @param  id                 the identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Resource} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Resource}.
   */
  T id(final String id);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the identifier of the object.
   **
   ** @return                    the identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String id();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId
  /**
   ** Sets the external identifier of the object.
   **
   ** @param  id                 the external identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Resource} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Resource}.
   */
  T externalId(final String id);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId
  /**
   ** Returns the external identifier of the object.
   **
   ** @return                    the external identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String externalId();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Sets the schema namespaces for this object.
   ** <br>
   ** This set should contain all schema namespaces including the one for this
   ** object and all extensions.
   **
   ** @param  namespace          a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Allowed object is {@link Collection} of
   **                            {@link String}.
   **
   ** @return                    the {@link Resource} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Resource}.
   */
  T namespace(final Collection<String> namespace);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the schema namespaces for this object.
   ** <br>
   ** This includes the one for the class that extends this class (taken from
   ** the annotation), as well as any that are present in the extensions.
   **
   ** @return                    a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Possible object is {@link Collection} of
   **                            {@link String}.
   */
  Collection<String> namespace();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata
  /**
   ** Sets metadata for the object.
   **
   ** @param  meta               <code>Meta</code> containing metadata about the
   **                            object.
   **                            <br>
   **                            Allowed object is {@link Metadata}.
   **
   ** @return                    the {@link Resource} to allow method chaining.
   **                            <br>
   **                            Possible object is {@link Resource}.
   */
  T metadata(final Metadata meta);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata
  /**
   ** Returns metadata about the object.
   **
   ** @return                    <code>Meta</code> containing metadata about the
   **                            object.
   **                            <br>
   **                            Possible object is {@link Metadata}.
   */
  Metadata metadata();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generic
  /**
   ** Returns the {@link Generic} SCIM resource representation of this
   ** <code>Resource</code>.
   ** <br>
   ** If this <code>Resource</code> is already a {@link Generic}, this same
   ** instance will be returned.
   **
   ** @return                    the {@link Generic} representation of
   **                            this <code>Resource</code>.
   **                            <br>
   **                            Possible object is {@link Generic}.
   */
  Generic generic();

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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  int hashCode();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Resource</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Resource</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
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
  boolean equals(final Object other);
}
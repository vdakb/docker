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

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Resource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import java.util.Collection;

import oracle.iam.system.simulation.rest.schema.Discoverable;

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
  // Method:   metadata
  /**
   ** Returns the {@link GenericResource} representation of this
   ** <code>Resource</code>.
   ** <br>
   ** If this <code>Resource</code> is already a {@link GenericResource}, this
   ** same instance will be returned.
   **
   ** @return                    the {@link GenericResource} representation of
   **                            this <code>Resource</code>.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   */
  GenericResource generic();
}
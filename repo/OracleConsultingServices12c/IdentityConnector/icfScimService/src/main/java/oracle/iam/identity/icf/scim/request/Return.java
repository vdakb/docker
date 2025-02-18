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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Library

    File        :   Return.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Return.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.request;

import java.util.Set;

import javax.ws.rs.client.WebTarget;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.scim.domain.SearchRequest;

////////////////////////////////////////////////////////////////////////////////
// class Return
// ~~~~~ ~~~~~~~
/**
 ** Abstract SCIM request for resource returning requests.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request extending
 **                              this class (requests can return their own
 **                              specific type instead of type defined by this
 **                              class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Return <T extends Return> extends Request<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Whether the attribute list is for omitted attributes */
  protected boolean     omitted;

  /** the attribute list of include or exclude */
  protected Set<String> attributes;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new SCIM request.
   **
   ** @param  target             the {@link WebTarget} to send the request.
   **                            <br>
   **                            Allowed object is {@link WebTarget}.
   ** @param  content            a string describing the media type of content
   **                            sent to the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  accept             a string (or strings) describing the media type
   **                            that will be accepted from the Service
   **                            Provider.
   **                            <br>
   **                            This parameter must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected Return(final WebTarget target, final String content, final String... accept) {
    // ensure inheritance
    super(target, content, accept);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emit
  /**
   ** Sets a multi-valued list of strings indicating the names of resource
   ** attributes to return in the response overriding the set of attributes that
   ** would be returned by default.
   ** <br>
   ** Any existing excluded attributes will be removed.
   **
   ** @param  attribute          the name of a resource attributes to return.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Return</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Return</code> for
   **                            type <code>T</code>.
   */
  public T emit(final String attribute) {
    return emit(CollectionUtility.set(attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emit
  /**
   ** Sets a multi-valued list of strings indicating the names of resource
   ** attributes to return in the response overriding the set of attributes that
   ** would be returned by default.
   ** <br>
   ** Any existing excluded attributes will be removed.
   **
   ** @param  attribute          the name of a resource attributes to return.
   **                            <br>
   **                            Allowed object is arry of {@link String}.
   **
   ** @return                    the <code>Return</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Return</code> for
   **                            type <code>T</code>.
   */
  public T emit(final String... attribute) {
    return emit(CollectionUtility.set(attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emit
  /**
   ** Sets a multi-valued list of strings indicating the names of resource
   ** attributes to return in the response overriding the set of attributes that
   ** would be returned by default.
   ** <br>
   ** Any existing excluded attributes will be removed.
   **
   ** @param  attributes         the names of resource attributes to return.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Return</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Return</code> for
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T emit(final Set<String> attributes) {
    this.attributes = attributes;
    this.omitted    = false;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omit
  /**
   ** Sets a multi-valued list of strings indicating the names of resource
   ** attributes to be removed from the default set of attributes to return.
   ** <br>
   ** Any existing excluded attributes will be removed.
   **
   ** @param  attribute          the name of a resource attributes to removed
   **                            from the default set of attributes to return.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Return</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Return</code> for
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T omit(final String attribute) {
    return omit(CollectionUtility.set(attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omit
  /**
   ** Sets a multi-valued list of strings indicating the names of resource
   ** attributes to be removed from the default set of attributes to return.
   ** <br>
   ** Any existing excluded attributes will be removed.
   **
   ** @param  attribute          the names of resource attributes to removed
   **                            from the default set of attributes to return.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the <code>Return</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Return</code> for
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T omit(final String... attribute) {
    return omit(CollectionUtility.set(attribute));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   omit
  /**
   ** Sets a multi-valued list of strings indicating the names of resource
   ** attributes to be removed from the default set of attributes to return.
   ** <br>
   ** Any existing excluded attributes will be removed.
   **
   ** @param  attributes         the names of resource attributes to removed
   **                            from the default set of attributes to return.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Return</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Return</code> for
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T omit(final Set<String> attributes) {
    this.attributes = attributes;
    this.omitted    = true;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build (overidden)
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   **
   ** @return                    the {@link WebTarget} for the request.
   **                            <br>
   **                            Possible object is {@link WebTarget}.
   */
  @Override
  WebTarget build() {
    if (this.attributes != null && this.attributes.size() > 0) {
      return super.build().queryParam(this.omitted ? SearchRequest.OMITTED : SearchRequest.EMITTED, StringUtility.collectionToString(this.attributes, ','));
    }
    // ensure inheritance
    return super.build();
  }
}
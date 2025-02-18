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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   URI.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    URI.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.type;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.access.common.spi.schema.UriList;
import oracle.iam.access.common.spi.schema.UriResource;
import oracle.iam.access.common.spi.schema.DeletedUriList;

////////////////////////////////////////////////////////////////////////////////
// class URI
// ~~~~~ ~~~
/**
 ** <code>URI</code> specifies the resource URL's ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class URI extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final UriResource delegate = factory.createUriResource();

  //////////////////////////////////////////////////////////////////////////////
  // class Added
  // ~~~~~ ~~~~~
  /**
   ** <code>Added</code> specifies ...
   */
  public static class Added extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final UriList delegate = factory.createUriList();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Added</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Added() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link UriList} delegate of configured <code>URI</code>s.
     **
     ** @return                    the {@link UriList} delegate.
     */
    public final oracle.iam.access.common.spi.schema.UriList delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURI
    /**
     ** Call by the ANT deployment to inject the argument for adding a uri.
     **
     ** @param  uri              the {@link URI} instance to add.
     */
    public void addConfiguredURI(final URI uri) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending URI property
      this.delegate.getUriResource().add(uri.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Deleted
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Deleted</code> specifies ...
   */
  public static class Deleted extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
    final DeletedUriList delegate = factory.createDeletedUriList();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Deleted</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Deleted() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delegate
    /**
     ** Returns the {@link DeletedUriList} delegate of configured
     ** <code>URI</code>s.
     **
     ** @return                    the {@link DeletedUriList} delegate.
     */
    public final DeletedUriList delegate() {
      return this.delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredURI
    /**
     ** Call by the ANT deployment to inject the argument for adding a uri.
     **
     ** @param  uri              the {@link URI} instance to add.
     */
    public void addConfiguredURI(final URI uri) {
      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending URI property
      this.delegate.getUriResource().add(uri.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>URI</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public URI() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   ** Call by the ANT kernel to inject the argument for <code>value</code>
   ** property.
   **
   ** @param  value              the value of the <code>value</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setValue(final String value) {
    this.delegate.setUri(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the <code>value</code> property.
   **
   ** @return                    the value of the <code>value</code> property.
   **                            Possible object is {@link String}.
   */
  public final String value() {
    return this.delegate.getUri();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Call by the ANT kernel to inject the argument for <code>description</code>
   ** property.
   **
   ** @param  value              the value of the <code>description</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.delegate.setUri(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the value of the <code>description</code> property.
   **
   ** @return                    the value of the <code>description</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.delegate.getUri();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setQueryString
  /**
   ** Call by the ANT kernel to inject the argument for <code>queryString</code>
   ** property.
   **
   ** @param  value              the value of the <code>queryString</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setQueryString(final String value) {
    this.delegate.setQueryString(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getQueryString
  /**
   ** Returns the value of the <code>queryString</code> property.
   **
   ** @return                    the value of the <code>queryString</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getQueryString() {
    return this.delegate.getQueryString();
  }
}
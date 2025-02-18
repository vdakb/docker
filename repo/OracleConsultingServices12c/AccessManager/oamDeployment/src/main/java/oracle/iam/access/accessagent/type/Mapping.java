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

    File        :   Mapping.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Mapping.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.accessagent.type;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceDataType;

import oracle.iam.access.common.type.DelegatingDataType;

import oracle.iam.access.common.spi.schema.AttributeMapping;
import oracle.iam.access.common.spi.schema.ProfileAttributeMapping;
import oracle.iam.access.common.spi.schema.SessionAttributeMapping;
import oracle.iam.access.common.spi.schema.ResponseAttributeMapping;

////////////////////////////////////////////////////////////////////////////////
// class Mapping
// ~~~~~ ~~~~~~~
/**
 ** Mapping Attribute retrieval fetches and sets user attributes in the HTTP
 ** request for consumption by the applications.
 ** <p>
 ** The following Attribute Mapping panels are available:
 ** <ul>
 **   <li>Profile Attributes
 **   <li>Response Attributes
 **   <li>Session Attributes
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Mapping extends DelegatingDataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final AttributeMapping delegate = factory.createAttributeMapping();

  //////////////////////////////////////////////////////////////////////////////
  // class Profile
  // ~~~~~ ~~~~~~~
  /**
   ** User <code>Profile</code> information can be populated under specific
   ** names for the currently authenticated user. For example:
   ** <br>
   ** Fetch Mode: <code>REQUEST_ATTRIBUTE</code>
   ** <ul>
   **   <li>
   **     <ol>
   **       <li>Name (Map key): cn
   **       <li>Value: CUSTOM-Common-Name
   **     </ol>
   **   </li>
   **   <li>
   **     <ol>
   **       <li>Name (Map key): mail
   **       <li>Value: CUSTOM-Email
   **     </ol>
   **   </li>
   ** </ul>
   */
  public static class Profile extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final ProfileAttributeMapping delegate = factory.createProfileAttributeMapping();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Profile</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Profile() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredMapping
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** <code>Attribute Mapping</code>.
     **
     ** @param  value            the {@link Mapping} instance to add.
     **
     ** @throws BuildException   if this instance is referencing a predefined
     **                          element.
     */
    public void addConfiguredMapping(final Mapping value)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending resource property
      this.delegate.getAttributeMapping().add(value.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Session
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Session</code> attribute mapping are the session object maintained
   ** by the <code>Access Server</code>. These are sent as part of a session
   ** validation response to the <code>Access Agent</code>s.
   ** <br>
   ** Fetch Mode: <code>REQUEST_ATTRIBUTE</code>
   ** <ul>
   **   <li>
   **     <ol>
   **       <li>Name (Map key): UserToken
   **       <li>Value: CUSTOM-userid
   **     </ol>
   **   </li>
   ** </ul>
   */
  public static class Session extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final SessionAttributeMapping delegate = factory.createSessionAttributeMapping();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Session</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Session() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredMapping
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** <code>Attribute Mapping</code>.
     **
     ** @param  value            the {@link Mapping} instance to add.
     **
     ** @throws BuildException   if this instance is referencing a predefined
     **                          element.
     */
    public void addConfiguredMapping(final Mapping value)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending resource property
      this.delegate.getAttributeMapping().add(value.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Response
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Response</code> attribute mapping obtains user-specific information
   ** by fetching policy response attributes, assigns a mode to the policy
   ** response attribute property, and maps the policy response attributes to be
   ** populated under specific names for the currently authenticated user.
   ** <br>
   ** Fetch Mode: <code>REQUEST_ATTRIBUTE</code>
   ** <ul>
   **   <li>
   **     <ol>
   **       <li>Name (Map key): cn
   **       <li>Value: CUSTOM-Common-Name
   **     </ol>
   **   </li>
   **   <li>
   **     <ol>
   **       <li>Name (Map key): mail
   **       <li>Value: CUSTOM-Email
   **     </ol>
   **   </li>
   ** </ul>
   */
  public static class Response extends ServiceDataType {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final ResponseAttributeMapping delegate = factory.createResponseAttributeMapping();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Response</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Response() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredMapping
    /**
     ** Call by the ANT deployment to inject the argument for adding an
     ** <code>Attribute Mapping</code>.
     **
     ** @param  value            the {@link Mapping} instance to add.
     **
     ** @throws BuildException   if this instance is referencing a predefined
     **                          element.
     */
    public void addConfiguredMapping(final Mapping value)
      throws BuildException {

      // verify if the reference id is set on the element to prevent
      // inconsistency
      if (isReference())
        throw noChildrenAllowed();

      // assign the correspondending resource property
      this.delegate.getAttributeMapping().add(value.delegate);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Mapping</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Mapping() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the value of the <code>name</code> property.
   **
   ** @param  value              the value of the <code>name</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.delegate.setName(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue
  /**
   ** Sets the value of the <code>value</code> property.
   **
   ** @param  value              the value of the <code>value</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setValue(final String value) {
    this.delegate.setValue(value);
  }
}

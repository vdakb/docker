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

    File        :   RregAuthenticationPolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RregAuthenticationPolicy.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class RregAuthenticationPolicy
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>RregAuthenticationPolicy</code> are applied to specific resources
 ** within an <code>Application Domain</code>.
 ** <br>
 ** Each authentication policy:
 ** <ul>
 **   <li>Identifies the specific resources covered by this policy, which must
 **       be defined on the Resources tab of this policy and in the Resources
 **       container for the <code>Application Domain</code>
 **   <li>Specifies the authentication scheme that provides the challenge
 **       method to be used to authenticate the user
 **   <li>Specifies the Success URL (and the failure URL) that redirects the
 **       user based on the results of this policy evaluation
 **   <li>Defines optional Responses that identify post-authentication actions
 **       to be carried out by the Agent.
 **       <br>
 **       Policy responses provide the ability to insert information into a
 **       session and pull it back out at any later point. This is more robust
 **       and flexible than Oracle Access Manager 10g, which provided data
 **       passage to (and between) applications by redirecting to URLs in a
 **       specific sequence.
 **       <br>
 **       Policy responses are optional. These must be configured by an
 **       Administrator and are applied to specific resources defined within
 **       the <code>Application Domain</code>.
 ** </ul>
 ** <p>
 ** Java class for anonymous complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="name"            type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="description"     type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="authnSchemeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element ref="{}uriList"                                                        minOccurs="0"/&gt;
 **         &lt;element ref="{}deletedUriList"                                                 minOccurs="0"/&gt;
 **         &lt;element ref="{}successResponseList"                                            minOccurs="0"/&gt;
 **       &lt;sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=RregAuthenticationPolicy.LOCAL)
@XmlType(name="", propOrder={"name", "description", "authenticationSchemeName", "uriList", "deletedUriList", "successResponseList"})
public class RregAuthenticationPolicy {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "rregAuthenticationPolicy";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String              name;
  protected String              description;
  @XmlElement(name="authnSchemeName")
  protected String              authenticationSchemeName;
  protected UriList             uriList;
  protected DeletedUriList      deletedUriList;
  protected SuccessResponseList successResponseList;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>RregAuthenticationPolicy</code> that allows use
   ** as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public RregAuthenticationPolicy() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RregAuthenticationPolicy</code> with the specified
   ** properties.
   **
   ** @param  name               the initial name of the policy.
   **                            Allowed object is {@link String}.
   */
  public RregAuthenticationPolicy(final String name) {
    // ensure inheritance
    super();

    // initialize instance
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
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
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the value of the <code>name</code> property.
   **
   ** @return                    the value of the <code>name</code> property.
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the <code>description</code> property.
   **
   ** @param  value              the value of the <code>description</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
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
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthnSchemeName
  /**
   ** Sets the value of the <code>authenticationSchemeName</code> property.
   **
   ** @param  value              the value of the
   **                            <code>authenticationSchemeName</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setAuthenticationSchemeName(final String value) {
    this.authenticationSchemeName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthenticationSchemeName
  /**
   ** Returns the value of the <code>authenticationSchemeName</code> property.
   **
   ** @return                    the value of the
   **                            <code>authenticationSchemeName</code> property.
   **                            Possible object is {@link String}.
   */
  public String getAuthenticationSchemeName() {
    return this.authenticationSchemeName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUriList
  /**
   ** Sets the value of the <code>uriList</code> property.
   **
   ** @param  value              the value of the <code>uriList</code>
   **                            property.
   **                            Allowed object is {@link UriList}.
   */
  public void setUriList(final UriList value) {
    this.uriList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUriList
  /**
   ** Returns the value of the <code>uriList</code> property.
   **
   ** @return                    the value of the <code>uriList</code>
   **                            property.
   **                            Possible object is {@link UriList}.
   */
  public UriList getUriList() {
    return this.uriList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDeletedUriList
  /**
   ** Sets the value of the <code>deletedUriList</code> property.
   **
   ** @param  value              the value of the <code>deletedUriList</code>
   **                            property.
   **                            Allowed object is {@link DeletedUriList}.
   */
  public void setDeletedUriList(final DeletedUriList value) {
    this.deletedUriList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDeletedUriList
  /**
   ** Returns the value of the <code>deletedUriList</code> property.
   **
   ** @return                    the value of the <code>deletedUriList</code>
   **                            property.
   **                            Possible object is {@link DeletedUriList}.
   */
  public DeletedUriList getDeletedUriList() {
    return this.deletedUriList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSuccessResponseList
  /**
   ** Sets the value of the <code>successResponseList</code> property.
   **
   ** @param  value              the value of the
   **                            <code>successResponseList</code> property.
   **                            Allowed object is {@link SuccessResponseList}.
   */
  public void setSuccessResponseList(final SuccessResponseList value) {
    this.successResponseList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSuccessResponseList
  /**
   ** Returns the value of the <code>successResponseList</code> property.
   **
   ** @return                    the value of the
   **                            <code>successResponseList</code> property.
   **                            Possible object is {@link SuccessResponseList}.
   */
  public SuccessResponseList getSuccessResponseList() {
    return this.successResponseList;
  }
}
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

    File        :   RregApplicationDomain.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RregApplicationDomain.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

////////////////////////////////////////////////////////////////////////////////
// class RregApplicationDomain
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Access Manager enables the control who can access resources based on
 ** policies defined within an <code>Application Domain</code>. Users attempt to
 ** access a protected resource by entering a URL in a browser, by running an
 ** application, or by calling some other external business logic. When a user
 ** requests access to a protected resource, the request is evaluated according
 ** to policies that discriminate between authenticated users who are authorized
 ** and those who are not authorized for access to a particular resource.
 ** <p>
 ** <code>Application Domain</code>s do not have any hierarchical relationship
 ** to one another. Each <code>Application Domain</code> can be made to contain
 ** policy elements related to an entire application deployment, a particular
 ** tier of the deployment, or a single host.
 ** <p>
 ** Within each <code>Application Domain</code>, specific resources are
 ** identified for protection by specific policies that govern access.
 ** Authentication and authorization policies include Administrator-configured
 ** responses that are applied upon successful evaluation. Authorization
 ** policies include Administrator-configured conditions and rules that define
 ** how evaluation is performed, and responses to be applied upon successful
 ** evaluation.
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
 **         &lt;element name="name"        type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element ref="{}uriList"                    minOccurs="0"/&gt;
 **         &lt;element ref="{}deletedUriList"             minOccurs="0"/&gt;
 **         &lt;element ref="{}rregAuthenticationPolicies" minOccurs="0"/&gt;
 **         &lt;element ref="{}rregAuthorizationPolicies"  minOccurs="0"/&gt;
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
@XmlRootElement(name=RregAuthorizationPolicy.LOCAL)
@XmlType(name="", propOrder={"name", "description", "uriList", "deletedUriList", "rregAuthenticationPolicies", "rregAuthorizationPolicies"})
public class RregApplicationDomain {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "rregApplicationDomain";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String                     name;
  protected String                     description;
  protected UriList                    uriList;
  protected DeletedUriList             deletedUriList;
  protected RregAuthenticationPolicies rregAuthenticationPolicies;
  protected RregAuthorizationPolicies  rregAuthorizationPolicies;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>RregApplicationDomain</code> that allows use as
   ** a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public RregApplicationDomain() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            Possible object is {@link String}.
   */
  public String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the value of the description property.
   **
   ** @return                    the value of the description property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
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
  // Method:   setRregAuthenticationPolicies
  /**
   ** Sets the value of the <code>rregAuthenticationPolicies</code> property.
   **
   ** @param  value              the value of the
   **                            <code>rregAuthenticationPolicies</code>
   **                            property.
   **                            Allowed object is {@link RregAuthenticationPolicies}.
   */
  public void setRregAuthenticationPolicies(final RregAuthenticationPolicies value) {
    this.rregAuthenticationPolicies = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRregAuthenticationPolicies
  /**
   ** Returns the value of the <code>rregAuthenticationPolicies</code> property.
   **
   ** @return                    the value of the
   **                            <code>rregAuthenticationPolicies</code>
   **                            property.
   **                            Possible object is {@link RregAuthenticationPolicies}.
   */
  public RregAuthenticationPolicies getRregAuthenticationPolicies() {
    return this.rregAuthenticationPolicies;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRregAuthorizationPolicies
  /**
   ** Sets the value of the <code>rregAuthorizationPolicies</code> property.
   **
   ** @param  value              the value of the
   **                            <code>rregAuthorizationPolicies</code>
   **                            property.
   **                            Allowed object is {@link RregAuthorizationPolicies}.
   */
  public void setRregAuthorizationPolicies(final RregAuthorizationPolicies value) {
    this.rregAuthorizationPolicies = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRregAuthorizationPolicies
  /**
   ** Returns the value of the <code>rregAuthorizationPolicies</code> property.
   **
   ** @return                    the value of the
   **                            <code>rregAuthorizationPolicies</code>
   **                            property.
   **                            Possible object is {@link RregAuthorizationPolicies}.
   */
  public RregAuthorizationPolicies getRregAuthorizationPolicies() {
    return this.rregAuthorizationPolicies;
  }
}
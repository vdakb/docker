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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   Identity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Identity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-10-01  DSteding    First release version
*/

package oracle.iam.identity.offline.schema;

import javax.xml.namespace.QName;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class Identity
// ~~~~~ ~~~~~~~~
/**
 ** Java class for identity complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="identity"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="loginName"        type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="firstName"        type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="lastName"         type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="displayName"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="organizationName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **       &lt;/sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name=Identity.LOCAL, propOrder={"loginName", "firstName", "lastName", "displayName", "organizationName"})
public class Identity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "identity";
  public static final QName  QNAME = new QName(ObjectFactory.NAMESPACE, LOCAL);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String loginName;
  @XmlElement(required=true)
  protected String firstName;
  @XmlElement(required=true)
  protected String lastName;
  @XmlElement(required=true)
  protected String displayName;
  @XmlElement(required=true)
  protected String organizationName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Identity</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Identity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLoginName
  /**
   ** Sets the value of the loginName property.
   **
   ** @param  value              the value of the loginName property to set.
   **                            Allowed object is {@link String}.
   */
  public void setLoginName(final String value) {
    this.loginName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLoginName
  /**
   ** Returns the value of the loginName property.
   **
   ** @return                    the value of the loginName property.
   **                            Possible object is {@link String}.
   */
  public String getLoginName() {
    return this.loginName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFirstName
  /**
   ** Sets the value of the firstName property.
   **
   ** @param  value              the value of the firstName property to set.
   **                            Allowed object is {@link String}.
   */
  public void setFirstName(final String value) {
    this.firstName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFirstName
  /**
   ** Returns the value of the firstName property.
   **
   ** @return                    the value of the firstName property.
   **                            Possible object is {@link String}.
   */
  public String getFirstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLastName
  /**
   ** Sets the value of the lastName property.
   **
   ** @param  value              the value of the lastName property to set.
   **                            Allowed object is {@link String}.
   */
  public void setLastName(final String value) {
    this.lastName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLastName
  /**
   ** Returns the value of the lastName property.
   **
   ** @return                    the value of the lastName property.
   **                            Possible object is {@link String}.
   */
  public String getLastName() {
    return this.lastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDisplayName
  /**
   ** Sets the value of the displayName property.
   **
   ** @param  value              the value of the displayName property to set.
   **                            Allowed object is {@link String}.
   */
  public void setDisplayName(final String value) {
    this.displayName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDisplayName
  /**
   ** Returns the value of the displayName property.
   **
   ** @return                    the value of the displayName property.
   **                            Possible object is {@link String}.
   */
  public String getDisplayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOrganizationName
  /**
   ** Sets the value of the organizationName property.
   **
   ** @param  value              the value of the organizationName property to
   **                            set.
   **                            Allowed object is {@link String}.
   */
  public void setOrganizationName(final String value) {
    this.organizationName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOrganizationName
  /**
   ** Returns the value of the organizationName property.
   **
   ** @return                    the value of the organizationName property.
   **                            Possible object is {@link String}.
   */
  public String getOrganizationName() {
    return this.organizationName;
  }
}
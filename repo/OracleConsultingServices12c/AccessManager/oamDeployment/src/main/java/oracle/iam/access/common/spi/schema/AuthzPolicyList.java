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

    File        :   AuthzPolicyList.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AuthzPolicyList.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class AuthzPolicyList
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Java class for anonymous complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element ref="{}rregAuthorizationPolicy" minOccurs="0" maxOccurs="unbounded"/&gt;
 **         &lt;element ref="{}RREGPolicy"              minOccurs="0" maxOccurs="unbounded"/&gt;
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
@XmlRootElement(name=AuthzPolicyList.LOCAL)
@XmlType(name="", propOrder={"authorization", "registration"})
public class AuthzPolicyList {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "authnPolicyList";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(name=RREGPolicy.LOCAL)
  protected List<RREGPolicy>              registration;
  @XmlElement(name=RregAuthorizationPolicy.LOCAL)
  protected List<RregAuthorizationPolicy> authorization;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>AuthzPolicyList</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public AuthzPolicyList() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AuthzPolicyList</code> with the specified properties.
   **
   ** @param  authorization      the intial collection of authorization
   **                            policies.
   **                            Allowed object is {@link List} of
   **                            {@link RregAuthorizationPolicy }s.
   ** @param  registration       the intial collection of registration
   **                            policies.
   **                            Allowed object is {@link List} of
   **                            {@link RregAuthorizationPolicy }s.
   */
  public AuthzPolicyList(final List<RregAuthorizationPolicy> authorization, final List<RREGPolicy> registration) {
    // ensure inheritance
    super();

    // initialize instance
    this.authorization = authorization == null ? null : new ArrayList<RregAuthorizationPolicy>(authorization);
    this.registration  = registration  == null ? null : new ArrayList<RREGPolicy>(registration);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthorization
  /**
   ** Returns the value of the authorization property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getAuthorization().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the {@link List}:
   ** {@link RregAuthorizationPolicy}s.
   **
   ** @return                    the associated {@link List} of
   **                            {@link RregAuthorizationPolicy}s.
   **                            Returned value is never <code>null</code>.
   */
  public List<RregAuthorizationPolicy> getAuthorization() {
    if (this.authorization == null)
      this.authorization = new ArrayList<RregAuthorizationPolicy>();

    return this.authorization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRegistration
  /**
   ** Returns the value of the registration property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getRegistration().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the {@link List}:
   ** {@link RREGPolicy}s.
   **
   ** @return                    the associated {@link List} of
   **                            {@link RREGPolicy}s.
   **                            Returned value is never <code>null</code>.
   */
  public List<RREGPolicy> getRegistration() {
    if (this.registration == null)
      this.registration = new ArrayList<RREGPolicy>();

    return this.registration;
  }
}

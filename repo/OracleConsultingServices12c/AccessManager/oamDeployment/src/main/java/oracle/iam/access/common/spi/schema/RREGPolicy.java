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

    File        :   RREGPolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RREGPolicy.


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
// class RREGPolicy
// ~~~~~ ~~~~~~~~~~
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
 **         &lt;element name="policyName"          type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="scheme"              type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element ref="{}uriList"                                                            minOccurs="0"/&gt;
 **         &lt;element ref="{}successResponseList"/&gt;
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
@XmlRootElement(name=RREGPolicy.LOCAL)
@XmlType(name="", propOrder={"policyName", "scheme", "uriList", "successResponseList"})
public class RREGPolicy {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "RREGPolicy";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String              policyName;
  protected String              scheme;
  protected UriList             uriList;
  @XmlElement(required=true)
  protected SuccessResponseList successResponseList;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>RREGPolicy</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public RREGPolicy() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RREGPolicy</code> with the specified properties.
   **
   ** @param  policyName         the intial policy name
   **                            Allowed object is {@link String}.
   ** @param  response           the intial collection of responses.
   **                            Allowed object is {@link SuccessResponseList}.
   */
  public RREGPolicy(final String policyName, final SuccessResponseList response) {
 	  // ensure inheritance
    super();

    // initialize instance
    this.policyName          = policyName;
    this.successResponseList = response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPolicyName
  /**
   ** Sets the value of the policyName property.
   **
   ** @param  value              the value of the policyPolicyName property.
   **                            Allowed object is {@link String}.
   */
  public void setPolicyName(final String value) {
    this.policyName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPolicyName
  /**
   ** Returns the value of the policyName property.
   **
   ** @return                    the value of the policyPolicyName property.
   **                            Possible object is {@link String}.
   */
  public String getPolicyName() {
    return this.policyName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScheme
  /**
   ** Sets the value of the scheme property.
   **
   ** @param  value              the value of the scheme property.
   **                            Allowed object is {@link String}.
   */
  public void setScheme(final String value) {
    this.scheme = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getScheme
  /**
   ** Returns the value of the scheme property.
   **
   ** @return                    the value of the scheme property.
   **                            Possible object is {@link String}.
   */
  public String getScheme() {
    return this.scheme;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUriList
  /**
   ** Sets the value of the uriList property.
   **
   ** @param  value              the value of the uriList property.
   **                            Allowed object is {@link UriList}.
   */
  public void setUriList(final UriList value) {
    this.uriList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUriList
  /**
   ** Returns the value of the uriList property.
   **
   ** @return                    the value of the uriList property.
   **                            Possible object is {@link UriList}.
   */
  public UriList getUriList() {
    return this.uriList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSuccessResponseList
  /**
   ** Sets the value of the successResponseList property.
   **
   ** @param  value              the value of the successResponseList property.
   **                            Allowed object is {@link SuccessResponseList}.
   */
  public void setSuccessResponseList(final SuccessResponseList value) {
    this.successResponseList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSuccessResponseList
  /**
   ** Returns the value of the successResponseList property.
   **
   ** @return                    the value of the successResponseList property.
   **                            Possible object is {@link SuccessResponseList}.
   */
  public SuccessResponseList getSuccessResponseList() {
    return this.successResponseList;
  }
}

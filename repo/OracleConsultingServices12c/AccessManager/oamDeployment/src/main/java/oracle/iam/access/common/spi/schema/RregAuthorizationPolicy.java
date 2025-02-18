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

    File        :   RregAuthorizationPolicy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RregAuthorizationPolicy.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class RregAuthorizationPolicy
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>RregAuthorizationPolicy</code> protects access to one or more
 ** resources based on attributes of an authenticated user or the environment.
 ** The authorization policy provides the sole authorization protection for
 ** resources included in the policy.
 ** <p>
 ** A single policy can be defined to protect one or more resources in the
 ** <code>Application Domain</code>. However, each resource can be protected by
 ** only one authorization policy.
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
 **         &lt;element ref="{}uriList"             minOccurs="0"/&gt;
 **         &lt;element ref="{}deletedUriList"      minOccurs="0"/&gt;
 **         &lt;element ref="{}conditionsList"      minOccurs="0"/&gt;
 **         &lt;element ref="{}rules"               minOccurs="0"/&gt;
 **         &lt;element ref="{}successResponseList" minOccurs="0"/&gt;
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
public class RregAuthorizationPolicy {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "rregAuthorizationPolicy";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String              name;
  protected String              description;
  protected UriList             uriList;
  protected DeletedUriList      deletedUriList;
  protected ConditionsList      conditionsList;
  protected Rules               rules;
  protected SuccessResponseList successResponseList;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>RregAuthorizationPolicy</code> that allows use as
   ** a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public RregAuthorizationPolicy() {
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
  // Method:   setConditionsList
  /**
   ** Sets the value of the <code>conditionsList</code> property.
   **
   ** @param  value              the value of the <code>conditionsList</code>
   **                            property.
   **                            Allowed object is {@link ConditionsList}.
   */
  public void setConditionsList(final ConditionsList value) {
    this.conditionsList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConditionsList
  /**
   ** Returns the value of the <code>conditionsList</code> property.
   **
   ** @return                    the value of the <code>conditionsList</code>
   **                            property.
   **                            Possible object is {@link ConditionsList}.
   */
  public ConditionsList getConditionsList() {
    return this.conditionsList;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRules
  /**
   ** Sets the value of the <code>rules</code> property.
   **
   ** @param  value              the value of the <code>rules</code> property.
   **                            Allowed object is {@link Rules}.
   */
  public void setRules(final Rules value) {
    this.rules = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRules
  /**
   ** Returns the value of the <code>rules</code> property.
   **
   ** @return                    the value of the <code>rules</code> property.
   **                            Possible object is {@link Rules}.
   */
  public Rules getRules() {
    return this.rules;
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
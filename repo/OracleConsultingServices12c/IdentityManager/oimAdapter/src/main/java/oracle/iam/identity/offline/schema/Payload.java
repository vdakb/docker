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

    File        :   Payload.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Payload.


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
// class Payload
// ~~~~~ ~~~~~~~
/**
 ** Java class for payload complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="payload"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="beneficiary" type="{http://xmlns.oracle.com/bpel/workflow/task}identity"/&gt;
 **         &lt;element name="manager"     type="{http://xmlns.oracle.com/bpel/workflow/task}identity"/&gt;
 **         &lt;element name="requester"   type="{http://xmlns.oracle.com/bpel/workflow/task}identity"/&gt;
 **         &lt;element name="operation"   type="{http://xmlns.oracle.com/bpel/workflow/task}operation"/&gt;
 **         &lt;element name="resource"    type="{http://xmlns.oracle.com/bpel/workflow/task}resource"/&gt;
 **         &lt;element name="entity"      type="{http://xmlns.oracle.com/bpel/workflow/task}catalog"/&gt;
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
@XmlType(name="payload", propOrder={"beneficiary", "manager", "requester", "operation", "resource", "entity"})
public class Payload {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "payload";
  public static final QName  QNAME = new QName(ObjectFactory.NAMESPACE, LOCAL);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected Identity beneficiary;
  @XmlElement(required=true)
  protected Identity manager;
  @XmlElement(required=true)
  protected Identity requester;
  @XmlElement(required=true)
  protected Operation operation;
  @XmlElement(required=true)
  protected Resource resource;
  @XmlElement(required=true)
  protected Catalog entity;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Payload</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Payload() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBeneficiary
  /**
   ** Sets the value of the beneficiary property.
   **
   ** @param  value              the value of the beneficiary property to set.
   **                            Allowed object is {@link Identity}.
   */
  public void setBeneficiary(final Identity value) {
    this.beneficiary = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBeneficiary
  /**
   ** Returns the value of the beneficiary property.
   **
   ** @return                    the value of the beneficiary property.
   **                            Possible object is {@link Identity}.
   */
  public Identity getBeneficiary() {
    return this.beneficiary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setManager
  /**
   ** Sets the value of the manager property.
   **
   ** @param  value              the value of the manager property to set.
   **                            Allowed object is {@link Identity}.
   */
  public void setManager(final Identity value) {
    this.manager = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getManager
  /**
   ** Returns the value of the manager property.
   **
   ** @return                    the value of the manager property.
   **                            Possible object is {@link Identity}.
   */
  public Identity getManager() {
    return this.manager;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRequester
  /**
   ** Sets the value of the requester property.
   **
   ** @param  value              the value of the requester property to set.
   **                            Allowed object is {@link Identity}.
   */
  public void setRequester(final Identity value) {
    this.requester = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRequester
  /**
   ** Returns the value of the requester property.
   **
   ** @return                    the value of the requester property.
   **                            Possible object is {@link Identity}.
   */
  public Identity getRequester() {
    return this.requester;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperation
  /**
   ** Sets the value of the operation property.
   **
   ** @param  value              the value of the operation property to set.
   **                            Allowed object is {@link Operation}.
   */
  public void setOperation(final Operation value) {
    this.operation = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOperation
  /**
   ** Returns the value of the operation property.
   **
   ** @return                    the value of the operation property.
   **                            Possible object is {@link Operation}.
   */
  public Operation getOperation() {
    return this.operation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResource
  /**
   ** Sets the value of the resource property.
   **
   ** @param  value              the value of the resource property to set.
   **                            Allowed object is {@link Resource}.
   */
  public void setResource(final Resource value) {
    this.resource = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResource
  /**
   ** Returns the value of the resource property.
   **
   ** @return                    the value of the resource property.
   **                            Possible object is {@link Resource}.
   */
  public Resource getResource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntity
  /**
   ** Sets the value of the resource property.
   **
   ** @param  value              the value of the resource property to set.
   **                            Allowed object is {@link Catalog}.
   */
  public void setEntity(final Catalog value) {
    this.entity = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntity
  /**
   ** Returns the value of the entity property.
   **
   ** @return                    the value of the entity property.
   **                            Possible object is {@link Catalog}.
   */
  public Catalog getEntity() {
    return this.entity;
  }
}
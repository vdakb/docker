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

    File        :   Resource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Resource.


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
// class Resource
// ~~~~~ ~~~~~~~~
/**
 ** Java class for resource complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="resource"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="instance"    type="{http://xmlns.oracle.com/bpel/workflow/task}entity"/&gt;
 **         &lt;element name="object"      type="{http://xmlns.oracle.com/bpel/workflow/task}entity""/&gt;
 **         &lt;element name="endpoint"    type="{http://xmlns.oracle.com/bpel/workflow/task}entity""/&gt;
 **         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name=Resource.LOCAL, propOrder={"instance", "object", "endpoint", "description"})
public class Resource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "resource";
  public static final QName  QNAME = new QName(ObjectFactory.NAMESPACE, LOCAL);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected Entity instance;
  @XmlElement(required=true)
  protected Entity object;
  @XmlElement(required=true)
  protected Entity endpoint;
  @XmlElement(required=true)
  protected String description;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Resource</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Resource() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInstance
  /**
   ** Sets the value of the instance property.
   **
   ** @param  value              the value of the instance property to set.
   **                            Allowed object is {@link Entity}.
   */
  public void setInstance(final Entity value) {
    this.instance = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInstance
  /**
   ** Returns the value of the instance property.
   **
   ** @return                    the instance of the type property.
   **                            Possible object is {@link Entity}.
   */
  public Entity getInstance() {
    return this.instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setObject
  /**
   ** Sets the value of the object property.
   **
   ** @param  value              the value of the object property to set.
   **                            Allowed object is {@link Entity}.
   */
  public void setObject(final Entity value) {
    this.object = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getObject
  /**
   ** Returns the value of the object property.
   **
   ** @return                    the value of the object property.
   **                            Possible object is {@link Entity}.
   */
  public Entity getObject() {
    return this.object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEndpoint
  /**
   ** Sets the value of the endpoint property.
   **
   ** @param  value              the value of the endpoint property to set.
   **                            Allowed object is {@link Entity}.
   */
  public void setEndpoint(final Entity value) {
    this.endpoint = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEndpoint
  /**
   ** Returns the value of the endpoint property.
   **
   ** @return                    the value of the endpoint property.
   **                            Possible object is {@link Entity}.
   */
  public Entity getEndpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the description property.
   **
   ** @param  value              the value of the description property to set.
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
}
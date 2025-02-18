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

    File        :   AttributeCondition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeCondition.


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
// class AttributeCondition
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
 **         &lt;element name="namespace"     type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="attributeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="operator"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="value"         type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlRootElement(name=AttributeCondition.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"namespace", "attributeName", "operator", "value"})
public class AttributeCondition {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "attributeCondition";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected String namespace;
  protected String attributeName;
  @XmlElement(required=true)
  protected String operator;
  @XmlElement(required=true)
  protected String value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>AttributeCondition</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public AttributeCondition() {
    // ensure inheritance
    super();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setNamespace
  /**
   ** Sets the value of the <code>namespace</code> property.
   **
   ** @param  value              the value of the <code>namespace</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setNamespace(final String value) {
    this.namespace = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getNamespace
  /**
   ** Returns the value of the <code>namespace</code> property.
   **
   ** @return                    the value of the <code>namespace</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getNamespace() {
    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAttributeName
  /**
   ** Sets the value of the <code>attributeName</code> property.
   **
   ** @param  value              the value of the <code>attributeName</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setAttributeName(final String value) {
    this.attributeName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeName
  /**
   ** Returns the value of the <code>attributeName</code> property.
   **
   ** @return                    the value of the <code>attributeName</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getAttributeName() {
    return this.attributeName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOperator
  /**
   ** Sets the value of the <code>operator</code> property.
   **
   ** @param  value              the value of the <code>operator</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setOperator(final String value) {
    this.operator = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOperator
  /**
   ** Returns the value of the <code>operator</code> property.
   **
   ** @return                    the value of the <code>operator</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getOperator() {
    return this.operator;
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
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue
  /**
   ** Returns the value of the <code>value</code> property.
   **
   ** @return                    the value of the <code>value</code> property.
   **                            Possible object is {@link String}.
   */
  public String getValue() {
    return this.value;
  }
}
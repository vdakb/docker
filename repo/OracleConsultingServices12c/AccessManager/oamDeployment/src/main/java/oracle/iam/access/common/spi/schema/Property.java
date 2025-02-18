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

    File        :   Property.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Property.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class Property
// ~~~~~ ~~~~~~~~
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
 **         &lt;element name="name"               type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="value"              type="{http://www.w3.org/2001/XMLSchema}anySimpleType" minOccurs="0"/&gt;
 **         &lt;element name="{}listValue"                                                               minOccurs="0"/&gt;
 **         &lt;element name="{}AttrMappingValue"                                                        minOccurs="0"/&gt;
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
@XmlRootElement(name=Property.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"name", "value", "list", "mapping"})
public class Property {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "property";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String           name;
  @XmlSchemaType(name="anySimpleType")
  protected Object           value;
  @XmlElement(name="listValue")
  protected ListValue        list;
  @XmlElement(name="AttrMappingValue")
  protected AttrMappingValue mapping;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Property</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Property() {
    // ensure inheritance
    super();
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
  // Method:   setValue
  /**
   ** Sets the value of the <code>value</code> property.
   **
   ** @param  value              the value of the <code>value</code> property.
   **                            Allowed object is {@link Object}.
   */
  public void setValue(final Object value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue
  /**
   ** Returns the value of the <code>value</code> property.
   **
   ** @return                    the value of the <code>value</code> property.
   **                            Possible object is {@link Object}.
   */
  public Object getValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setList
  /**
   ** Sets the value of the <code>list</code> property.
   **
   ** @param  value              the value of the <code>list</code> property.
   **                            Allowed object is {@link ListValue}.
   */
  public void setList(final ListValue value) {
    this.list = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getList
  /**
   ** Returns the value of the <code>list</code> property.
   **
   ** @return                    the value of the <code>list</code> property.
   **                            Possible object is {@link ListValue}.
   */
  public ListValue getList() {
    return this.list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMapping
  /**
   ** Sets the attrMappingValue of the <code>mapping</code> property.
   **
   ** @param  value              the value of the <code>mapping</code> property.
   **                            Allowed object is {@link AttrMappingValue}.
   */
  public void setAttrMappingValue(final AttrMappingValue value) {
    this.mapping = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttrMappingValue
  /**
   ** Returns the attrMappingValue of the <code>mapping</code> property.
   **
   ** @return                    the value of the <code>mapping</code> property.
   **                             Possible object is {@link AttrMappingValue}.
   */
  public AttrMappingValue getAttrMappingValue() {
    return this.mapping;
  }
}
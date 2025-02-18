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

    File        :   Operation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Operation.


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
// class Operation
// ~~~~~ ~~~~~~~~~
/**
 ** Java class for operation complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="operation"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name=Operation.LOCAL, propOrder={"key", "type"})
public class Operation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "operation";
  public static final QName  QNAME = new QName(ObjectFactory.NAMESPACE, LOCAL);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String key;
  @XmlElement(required=true)
  protected String type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Operation</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Operation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setKey
  /**
   ** Sets the value of the key property.
   **
   ** @param  value              the value of the key property to set.
   **                            Allowed object is {@link String}.
   */
  public void setKey(final String value) {
    this.key = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKey
  /**
   ** Returns the value of the key property.
   **
   ** @return                    the value of the key property.
   **                            Possible object is {@link String}.
   */
  public String getKey() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the value of the type property.
   **
   ** @param  value              the value of the type property to set.
   **                            Allowed object is {@link String}.
   */
  public void setType(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType
  /**
   ** Returns the value of the type property.
   **
   ** @return                    the value of the type property.
   **                            Possible object is {@link String}.
   */
  public String getType() {
    return this.type;
  }
}
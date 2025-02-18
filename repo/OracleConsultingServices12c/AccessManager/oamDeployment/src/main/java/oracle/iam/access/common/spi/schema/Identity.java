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

    File        :   Identity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Identity.


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
// class Identity
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
 **         &lt;element name="storeName"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="entityType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="entityName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlRootElement(name=Identity.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"storeName", "entityType", "entityName"})
public class Identity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "identity";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String storeName;
  @XmlElement(required=true)
  protected String entityType;
  @XmlElement(required=true)
  protected String entityName;

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
  // Method:   setStoreName
  /**
   ** Sets the value of the <code>storeName</code> property.
   **
   ** @param  value              the value of the <code>storeName</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setStoreName(final String value) {
    this.storeName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStoreName
  /**
   ** Returns the value of the <code>storeName</code> property.
   **
   ** @return                    the value of the <code>storeName</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getStoreName() {
    return this.storeName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntityType
  /**
   ** Sets the value of the <code>entityType</code> property.
   **
   ** @param  value              the value of the <code>entityType</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityType(final String value) {
    this.entityType = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntityType
  /**
   ** Returns the value of the <code>entityType</code> property.
   **
   ** @return                    the value of the <code>entityType</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getEntityType() {
    return this.entityType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEntityName
  /**
   ** Sets the value of the <code>entityName</code> property.
   **
   ** @param  value              the value of the <code>entityName</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setEntityName(final String value) {
    this.entityName = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntityName
  /**
   ** Returns the value of the entityName property.
   **
   ** @return                    the value of the <code>entityName</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getEntityName() {
    return this.entityName;
  }
}
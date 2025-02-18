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

    File        :   CombinerMode.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CombinerMode.


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
// class CombinerMode
// ~~~~~ ~~~~~~~~~~~~
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
 **         &lt;element name="mode"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element ref="{}conditionNameList" minOccurs="0"/&gt;
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
@XmlRootElement(name=CombinerMode.LOCAL)
@XmlType(name="", propOrder={"mode", "conditionNameList"})
public class CombinerMode {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "combinerMode";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String            mode;
  protected ConditionNameList conditionNameList;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ConditionNameList</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public CombinerMode() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMode
  /**
   ** Sets the value of the <code>mode</code> property.
   **
   ** @param  value              the value of the <code>mode</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setMode(final String value) {
    this.mode = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMode
  /**
   ** Returns the value of the <code>mode</code> property.
   **
   ** @return                    the value of the <code>mode</code> property.
   **                            Possible object is {@link String}.
   */
  public String getMode() {
    return this.mode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConditionNameList
  /**
   ** Sets the value of the <code>conditionNameList</code> property.
   **
   ** @param  value              the value of the <code>conditionNameList</code>
   **                            property.
   **                            Allowed object is {@link ConditionNameList}.
   */
  public void setConditionNameList(final ConditionNameList value) {
    this.conditionNameList = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConditionNameList
  /**
   ** Returns the value of the <code>conditionNameList</code> property.
   **
   ** @return                    the value of the <code>conditionNameList</code>
   **                            property.
   **                            Possible object is {@link ConditionNameList}.
   */
  public ConditionNameList getConditionNameList() {
    return this.conditionNameList;
  }
}
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

    File        :   Temporal.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Temporal.


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
// class Temporal
// ~~~~~ ~~~~
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
 **         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="endTime"   type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlRootElement(name=Temporal.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"startTime", "endTime", "dayOfWeek", "daysToString"})
public class Temporal {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "temporal";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String    startTime;
  @XmlElement(required=true)
  protected String    endTime;
  protected DayOfWeek dayOfWeek;
  protected String    daysToString;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Temporal</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Temporal() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setStartTime
  /**
   ** Sets the value of the <code>startTime</code> property.
   **
   ** @param  value              the value of the <code>startTime</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setStartTime(final String value) {
    this.startTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStartTime
  /**
   ** Returns the value of the <code>startTime</code> property.
   **
   ** @return                    the value of the <code>startTime</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getStartTime() {
    return this.startTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEndTime
  /**
   ** Sets the value of the <code>endTime</code> property.
   **
   ** @param  value              the value of the <code>endTime</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setEndTime(final String value) {
    this.endTime = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEndTime
  /**
   ** Returns the value of the <code>endTime</code> property.
   **
   ** @return                    the value of the <code>endTime</code> property.
   **                            Possible object is {@link String}.
   */
  public String getEndTime() {
    return this.endTime;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDayOfWeek
  /**
   ** Sets the value of the <code>dayOfWeek</code> property.
   **
   ** @param  value              the value of the <code>dayOfWeek</code>
   **                            property.
   **                            Allowed object is {@link DayOfWeek}.
   */
  public void setDayOfWeek(final DayOfWeek value) {
    this.dayOfWeek = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDayOfWeek
  /**
   ** Returns the value of the <code>dayOfWeek</code> property.
   **
   ** @return                    the value of the <code>dayOfWeek</code>
   **                            property.
   **                            Possible object is {@link DayOfWeek}.
   */
  public DayOfWeek getDayOfWeek() {
    return this.dayOfWeek;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDaysToString
  /**
   ** Sets the value of the <code>daysToString</code> property.
   **
   ** @param  value              the value of the <code>daysToString</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setDaysToString(final String value) {
    this.daysToString = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDaysToString
  /**
   ** Returns the value of the <code>daysToString</code> property.
   **
   ** @return                    the value of the <code>daysToString</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getDaysToString() {
    return this.daysToString;
  }
}
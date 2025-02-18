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

    File        :   DayOfWeek.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DayOfWeek.


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
// class DayOfWeek
// ~~~~~ ~~~~~~~~~
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
 **         &lt;element name="Monday"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="Tuesday"   type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="Wednesday" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="Thursday"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="Friday"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="Saturday"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **         &lt;element name="Sunday"    type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlRootElement(name=DayOfWeek.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"})
public class DayOfWeek {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "dayOfWeek";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(name="Monday",    required=true)
  protected String monday;
  @XmlElement(name="Tuesday",   required=true)
  protected String tuesday;
  @XmlElement(name="Wednesday", required=true)
  protected String wednesday;
  @XmlElement(name="Thursday",  required=true)
  protected String thursday;
  @XmlElement(name="Friday",    required=true)
  protected String friday;
  @XmlElement(name="Saturday",  required=true)
  protected String saturday;
  @XmlElement(name="Sunday",    required=true)
  protected String sunday;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>DayOfWeek</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public DayOfWeek() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMonday
  /**
   ** Sets the value of the <code>monday</code> property.
   **
   ** @param  value              the value of the <code>monday</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setMonday(final String value) {
    this.monday = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMonday
  /**
   ** Returns the value of the <code>monday</code> property.
   **
   ** @return                    the value of the <code>monday</code> property.
   **                            Possible object is {@link String}.
   */
  public String getMonday() {
    return this.monday;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTuesday
  /**
   ** Sets the value of the <code>tuesday</code> property.
   **
   ** @param  value              the value of the <code>tuesday</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setTuesday(final String value) {
    this.tuesday = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTuesday
  /**
   ** Returns the value of the <code>tuesday</code> property.
   **
   ** @return                    the value of the <code>tuesday</code> property.
   **                            Possible object is {@link String}.
   */
  public String getTuesday() {
    return this.tuesday;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setWednesday
  /**
   ** Sets the value of the <code>wednesday</code> property.
   **
   ** @param  value              the value of the <code>wednesday</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setWednesday(final String value) {
    this.wednesday = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getWednesday
  /**
   ** Returns the value of the <code>wednesday</code> property.
   **
   ** @return                    the value of the <code>wednesday</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getWednesday() {
    return this.wednesday;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setThursday
  /**
   ** Sets the value of the <code>thursday</code> property.
   **
   ** @param  value              the value of the <code>thursday</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setThursday(final String value) {
    this.thursday = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getThursday
  /**
   ** Returns the value of the <code>thursday</code> property.
   **
   ** @return                    the value of the <code>thursday</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getThursday() {
    return this.thursday;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFriday
  /**
   ** Sets the value of the <code>friday</code> property.
   **
   ** @param  value              the value of the <code>friday</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setFriday(final String value) {
    this.friday = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFriday
  /**
   ** Returns the value of the <code>friday</code> property.
   **
   ** @return                    the value of the <code>friday</code> property.
   **                            Possible object is {@link String}.
   */
  public String getFriday() {
    return this.friday;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSaturday
  /**
   ** Sets the value of the <code>saturday</code> property.
   **
   ** @param  value              the value of the <code>saturday</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setSaturday(final String value) {
    this.saturday = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSaturday
  /**
   ** Returns the value of the <code>saturday</code> property.
   **
   ** @return                    the value of the <code>saturday</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getSaturday() {
    return this.saturday;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSunday
  /**
   ** Sets the value of the <code>sunday</code> property.
   **
   ** @param  value              the value of the <code>sunday</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setSunday(final String value) {
    this.sunday = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSunday
  /**
   ** Returns the value of the <code>sunday</code> property.
   **
   ** @return                    the value of the <code>sunday</code> property.
   **                            Possible object is {@link String}.
   */
  public String getSunday() {
    return this.sunday;
  }
}
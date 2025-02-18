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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Analytics Extension Library
    Subsystem   :   Offline Target Connector

    File        :   Status.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    Status.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

////////////////////////////////////////////////////////////////////////////////
// enum Status
// ~~~~ ~~~~~~
/**
 ** Java class for status.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;simpleType name="status"&gt;
 **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 **     &lt;enumeration value="success"/&gt;
 **     &lt;enumeration value="ignored"/&gt;
 **     &lt;enumeration value="warning"/&gt;
 **     &lt;enumeration value="error"/&gt;
 **     &lt;enumeration value="fatal"/&gt;
 **   &lt;/restriction&gt;
 ** &lt;/simpleType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlEnum
@XmlType(name=Status.LOCAL)
public enum Status {

    @XmlEnumValue("success") SUCCESS("success")
  , @XmlEnumValue("ignored") IGNORED("ignored")
  , @XmlEnumValue("warning") WARNING("warning")
  , @XmlEnumValue("error")   ERROR("error")
  , @XmlEnumValue("fatal")   FATAL("fatal")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "status";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9209504743580930634")
  private static final long  serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String       value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Status</code> that allows use as a JavaBean.
   **
   ** @param  value              the value.
   */
  Status(final String value) {
    // initialize instance attributes
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the value property.
   **
   ** @return                    possible object is {@link String}.
   */
  public String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isLess
  /**
   ** Returns <code>true</code> if the given value is less as this status.
   **
   ** @param  value              the value to compare.
   **
   ** @return                    <code>true</code> if the given value is greater
   **                            as this status; otherwise <code>false</code>.
   */
  public boolean isLess(final Status value) {
    return this.ordinal() < value.ordinal();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isGreater
  /**
   ** Returns <code>true</code> if the given value is greater as this status.
   **
   ** @param  value              the value to compare.
   **
   ** @return                    <code>true</code> if the given value is greater
   **                            as this status; otherwise <code>false</code>.
   */
  public boolean isGreater(final Status value) {
    return this.ordinal() > value.ordinal();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromValue
  /**
   ** Factory method to create a proper <code>Status</code> from the given
   ** string value.
   **
   ** @param  value              the string value the status should be returned
   **                            for.
   **
   ** @return                    the <code>Status</code>.
   */
  public static Status fromValue(final String value) {
    for (Status cursor : Status.values()) {
      if (cursor.value.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}
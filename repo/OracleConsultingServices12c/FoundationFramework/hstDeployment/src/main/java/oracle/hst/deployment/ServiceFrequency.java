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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ServiceFrequency.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    ServiceFrequency.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment;

////////////////////////////////////////////////////////////////////////////////
// class ServiceFrequency
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>ServiceFrequency</code> defines common operation frequency
 ** declarations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum ServiceFrequency implements ServiceAction {

    day("day")
  , hour("hour")
  , minute("minute")
  , second("second")
  , millisecond("millisecond")
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final long     SECOND    = 1000;
  public static final long     MINUTE    = 60;
  public static final long     HOUR      = 3600;
  public static final long     DAY       = 3600 * 24;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2089274195662888073")
  private static final long serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String id;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceFrequency</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  ServiceFrequency(final String id) {
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (ServiceOperation)
  /**
   ** Returns the id of the property.
   **
   ** @return                    the id of the property.
   */
  @Override
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a proper service freqency from the given string
   ** value.
   **
   ** @param  value              the string value the service freqency should
   **                            be returned for.
   **
   ** @return                    the service freqency property.
   */
  public static ServiceFrequency from(final String value) {
    for (ServiceFrequency cursor : ServiceFrequency.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}
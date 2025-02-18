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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Frequency.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Frequency.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.scheduler.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.deployment.ServiceFrequency;

////////////////////////////////////////////////////////////////////////////////
// class Frequency
// ~~~~~ ~~~~~~~~~
/**
 ** <code>Frequency</code> should meet your scheduling needs if you need to
 ** specify a job execute in time if you want it to fire at that time, and
 ** then fire five more times, every ten seconds.
 ** <ul>
 **   <li>a repeat count
 **   <li>a repeat interval
 ** </ul>
 ** All of these properties are exactly what you'd expect them to be, with
 ** only a couple special notes related to the end-time property.
 ** <p>
 ** The repeat count can be zero, a positive integer, or the constant value
 ** SimpleTrigger#REPEAT_INDEFINITELY. The repeat interval property must be
 ** zero, or a positive long value, and represents a number of milliseconds.
 ** <br>
 ** <b>Note</b> that a repeat interval of zero will cause 'repeat count'
 ** firings of the trigger to happen concurrently (or as close to concurrently
 ** as the scheduler can manage).
 */
public class Frequency extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[] frequency = { ServiceFrequency.minute.id(), ServiceFrequency.hour.id(), ServiceFrequency.day.id() };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Frequency</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Frequency() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the name of the type of this frequency.
   **
   ** @return                    the name of the type of this frequency.
   */
  public final ServiceFrequency value() {
    return ServiceFrequency.from(super.getValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   calculate
  /**
   ** Returns the interval of the job this trigger belongs to will be executed.
   **
   ** @param  interval           the interval of the job this trigger belongs to
   **                            will be executed.
   **
   ** @return                    the interval of the job this trigger belongs to
   **                            will be executed.
   */
  public long calculate(final long interval) {
    // starting value is one second in milliseconds
    long factor = ServiceFrequency.SECOND;
    if (ServiceFrequency.minute == this.value())
      factor *= ServiceFrequency.MINUTE;
    else if (ServiceFrequency.hour == this.value())
      factor *= ServiceFrequency.HOUR;
    else if (ServiceFrequency.day == this.value())
      factor *= ServiceFrequency.DAY;

    return factor * interval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                  an array holding all possible values of the
   **                          enumeration. The order of elements must be fixed
   **                          so that indexOfValue(String) always return the
   **                          same index for the same value.
   */
  @Override
  public String[] getValues() {
    return frequency;
  }
}
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

    File        :   Expression.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Expression.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.scheduler.type;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Expression
// ~~~~~ ~~~~~~~~~~
/**
 ** Call by the ANT deployment to inject the argument for parameter
 ** <code>expression</code>.
 ** <p>
 ** <b>Format</b>
 ** <br>
 ** A cron expression is a string comprised of 6 or 7 fields separated by white
 ** space. Fields can contain any of the allowed values, along with various
 ** combinations of the allowed special characters for that field. The fields
 ** are as follows:
 ** <br>
 ** <table summary="">
 ** <tr><th>Field Name</th><th>Mandatory</th><th>Allowed Values</th><th>Allowed Special Characters</th></tr>
 ** <tr><td>Seconds</td><td>yes</td><td>0-59</td><td>, - * /</td></tr>
 ** <tr><td>Minutes</td><td>yes</td><td>0-59</td><td>, - * /</td></tr>
 ** <tr><td>Hours</td><td>yes</td><td>0-23</td><td>, - * /</td></tr>
 ** <tr><td>Day of month</td><td>yes</td><td>1-31</td><td>, - * ? / L W</td></tr>
 ** <tr><td>Month</td><td>yes</td><td>1-12 or JAN-DEC</td><td>, - * /</td></tr>
 ** <tr><td>Day of week</td><td>yes</td><td>1-7 or SUN-SAT</td><td>, - * ? / L #</td></tr>
 ** <tr><td>Year</td><td>no</td><td>empty, 1970-2099</td><td>, - * /</td></tr>
 ** </table>
 ** <br>
 ** So cron expressions can be as simple as this: <code>* * * * ? *</code> or
 ** more complex, like this:
 ** <code>0/5 14,18,3-39,52 * ? JAN,MAR,SEP MON-FRI 2012-2020</code>
 ** <p>
 ** <b>Special characters</b>
 ** <br>
 ** <ul>
 **   <li><b>*</b> <i>("all values")</i> -
 **       used to select all values within a field.
 **       For example, <b>*</b> in the minute field means
 **       <i>"every minute"</i>.
 **   <li><b>?</b> <i>("no specific value")</i> -
 **        This character is allowed for the day-of-month and day-of-week fields.
 **       This is useful when you need to specify something in one of the two
 **       fields, but not the other.For example, if you want your trigger to
 **       fire on a particular day of the month (say, the 10th), but don't care
 **       what day of the week that happens to be, you would put <code>10</code>
 **       in the day-of-month field, and <b>?</b> in the day-of-week field. See
 **       the examples below for clarification.
 **   <li><b>-</b> -
 **       used to specify ranges. For example, <code>10-12</code> in the hour
 **       field means <i>the hours 10, 11 and 12</i>.
 **   <li><b>/</b> -
 **       used to specify increments. For example, <code>0/15</code> in the
 **       seconds field means <i>the seconds 0, 15, 30, and 45</i>. And
 **       <code>5/15</code> in the seconds field means
 **       <i>the seconds 5, 20, 35, and 50</i>. You can also specify
 **       <b>/</b> after the character <b>-</b> in this case is equivalent to
 **       having <code>0</code> before the <b>/</b>. <code>1/3</code> in the
 **       day-of-month field means
 **       <i>fire every 3 days starting on the first day of the month</i>.
 **   <li><b>L</b> <i>("last")</i> -
 **       is allowed for the day-of-month and day-of-week fields.
 **       This character has different meaning in each of the two fields. For
 **       example, the value <b>L</b> in the day-of-month field means
 **       <i>the last day of the month</i> - day 31 for January, day 28 for
 **       February on non-leap years. If used in the day-of-week field by
 **       itself, it simply means <code>7</code> or <code>SAT</code>. But if
 **       used in the day-of-week field after another value, it means
 **       <i>the last xxx day of the month</i> - for example <code>6L</code>
 **       means <i>the last friday of the month</i>. When using the <b>L</b>
 **       option, it is important not to specify lists, or ranges of values, as
 **       you'll get confusing results.
 **   <li><b>W</b> <i>("weekday")</i> -
 **       is allowed for the day-of-month field. This character is used to
 **       specify the weekday (Monday-Friday) nearest the given day. As an
 **       example, if you were to specify <code>15W</code> as the value for the
 **       day-of-month field, the meaning is:
 **       <i>the nearest weekday to the 15th of the month</i>. So if the 15th
 **       is a Saturday, the trigger will fire on Friday the 14th. If the 15th
 **       is a Sunday, the trigger will fire on Monday the 16th. If the 15th
 **       is a Tuesday, then it will fire on Tuesday the 15th. However if you
 **       specify <code>1W</code> as the value for day-of-month, and the 1st
 **       is a Saturday, the trigger will fire on Monday the 3rd, as it will not
 **       <b>jump</b> over the boundary of a month's days. The <b>W</b>
 **       character can only be specified when the day-of-month is a single day,
 **       not a range or list of days.
 **   <li><b>#</b> -
 **       is allowed for the day-of-week field. This character is  used to
 **       specify <i>"the nth"</i> XXX day of the month. For example, the value
 **       of <code>6#3</code> in the day-of-week field means
 **       <i>the third Friday of the month</i> (day 6 = Friday and
 **       <code>#3</code> = the 3rd one in the month). Other examples:
 **       <code>2#1</code> = the first Monday of the month and
 **       <code>4#5</code> = the fifth Wednesday of the month.
 **       <b>Note</b> that if you specify <code>#5</code> and there is not 5 of
 **       the given day-of-week in the month, then no firing will occur that
 **       month.
 ** </ul>
 ** <p>
 ** <b>Examples</b>
 ** <br>
 ** Here are some full examples:
 ** <br>
 ** <table summary="">
 ** <tr><th>Expression</th><th>Meaning</th></tr>
 ** <tr><td>0 0 12 * * ?</td><td>Fire at 12pm (noon) every day</td></tr>
 ** <tr><td>0 15 10 ? * *</td><td>Fire at 10:15am every day</td></tr>
 ** <tr><td>0 15 10 * * ?</td><td>Fire at 10:15am every day</td></tr>
 ** <tr><td>0 15 10 * * ? *</td><td>Fire at 10:15am every day</td></tr>
 ** <tr><td>0 15 10 * * ? 2012</td><td>Fire at 10:15am every day during the year 2012</td></tr>
 ** <tr><td>0 * 14 * * ?</td><td>Fire every minute starting at 2pm and ending at 2:59pm, every day</td></tr>
 ** <tr><td>0 0/5 14 * * ?</td><td>Fire every 5 minutes starting at 2pm and ending at 2:55pm, every day</td></tr>
 ** <tr><td>0 0/5 14,18 * * ?</td><td>Fire every 5 minutes starting at 2pm and ending at 2:55pm, AND fire every 5 minutes starting at 6pm and ending at 6:55pm, every day</td></tr>
 ** <tr><td>0 0-5 14 * * ?</td><td>Fire every minute starting at 2pm and ending at 2:05pm, every day</td></tr>
 ** <tr><td>0 10,44 14 ? 3 WED</td><td>Fire at 2:10pm and at 2:44pm every Wednesday in the month of March.</td></tr>
 ** <tr><td>0 15 10 ? * MON-FRI</td><td>Fire at 10:15am every Monday, Tuesday, Wednesday, Thursday and Friday</td></tr>
 ** <tr><td>0 15 10 15 * ?</td><td>Fire at 10:15am on the 15th day of every month</td></tr>
 ** <tr><td>0 15 10 L * ?</td><td>Fire at 10:15am on the last day of every month</td></tr>
 ** <tr><td>0 15 10 ? * 6L</td><td>Fire at 10:15am on the last Friday of every month</td></tr>
 ** <tr><td>0 15 10 ? * 6L 2012-2015</td><td>Fire at 10:15am on every last friday of every month during the years 2012, 2013, 2014 and 2015</td></tr>
 ** <tr><td>0 15 10 ? * 6#3</td><td>Fire at 10:15am on the third Friday of every month</td></tr>
 ** <tr><td>0 0 12 1/5 * ?</td><td>Fire at 12pm (noon) every 5 days every month, starting on the first day of the month.</td></tr>
 ** <tr><td>0 11 11 11 11 ?</td><td>Fire every November 11th at 11:11am.</td></tr>
 ** </table>
 ** <br>
 ** <b>Pay attention to the effects of '?' and '*' in the day-of-week and day-of-month fields!</b>
 ** <p>
 ** <b>Notes</b>
 ** <ul>
 **   <li>Support for specifying both a day-of-week and a day-of-month value is
 **       not complete (you must currently use the '?' character in one of these
 **       fields).
 **   <li>Be careful when setting fire times between mid-night and 1:00 AM -
 **       "daylight savings" can cause a skip or a repeat depending on whether
 **       the time moves back or jumps forward.
 ** </ul>
 */
abstract class Expression extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String DAILY = "daily";
  public static final String WEEKLY = "weekly";
  public static final String MONTHLY = "monthly";
  public static final String MWD = "monthlyweekdays";
  public static final String YEARLY = "yearly";

  private static final String[] TYPE = { DAILY, WEEKLY, MONTHLY, YEARLY, MWD };

  private static final Map<String, Integer> WEEK = new HashMap<String, Integer>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  protected String second = "*";
  protected String minute = "*";
  protected String hour = "*";
  protected String month = "*";
  protected String year = "*";
  protected String dayOfWeek = "?";
  protected String dayOfMonth = "*";

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    WEEK.put("SUN", new Integer(1));
    WEEK.put("MON", new Integer(2));
    WEEK.put("TUE", new Integer(3));
    WEEK.put("WED", new Integer(4));
    WEEK.put("THR", new Integer(5));
    WEEK.put("FRI", new Integer(6));
    WEEK.put("SAT", new Integer(7));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Daily
  // ~~~~~ ~~~~~
  /**
   ** <code>Daily</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   */
  public static class Daily extends Expression {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int interval = 0;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Daily</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Daily() {
      // ensure inheritance
      super(DAILY);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setInterval
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>interval</code>.
     **
     ** @param  interval         the interval of the schedule.
     */
    public void setInterval(final int interval) {
      this.interval = interval;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compose (Expression)
    /**
     ** Comspose the expression the scheduler needs to setup a cron job.
     **
     ** @return                  the expression the scheduler needs to setup a
     **                          cron job.
     */
    @Override
    public String compose(final Date startTime) {
      GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
      calendar.setTime(startTime);
      this.second = String.valueOf(calendar.get(GregorianCalendar.SECOND));
      this.minute = String.valueOf(calendar.get(GregorianCalendar.MINUTE));
      this.hour = String.valueOf(calendar.get(GregorianCalendar.HOUR));
      this.dayOfWeek = "?";

      if (this.interval != 0)
        this.dayOfMonth = calendar.get(GregorianCalendar.DATE) + "/" + this.interval;

      return make();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Weekly
  // ~~~~~ ~~~~~~
  /**
   ** <code>Weekly</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   */
  public static class Weekly extends Expression {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int[] interval = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Weekly</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Weekly() {
      // ensure inheritance
      super(WEEKLY);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setInterval
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>interval</code>.
     **
     ** @param  weekDays         the interval of weekly scheduled cron job
     **                          schedule.
     */
    public void setInterval(final String weekDays) {
      // prevent bogus input
      if (StringUtility.isEmpty(weekDays))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "interval"));

      final String[] day = weekDays.split(" ");
      this.interval = new int[day.length];
      for (int i = 0; i < day.length; i++) {
        if (WEEK.containsKey(day[i]))
          this.interval[i] = WEEK.get(day[i]);
        else
          throw new BuildException(ServiceResourceBundle.string(ServiceError.TYPE_ATTRIBUTE_VALUE));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compose (Expression)
    /**
     ** Comspose the expression the scheduler needs to setup a cron job.
     **
     ** @return                  the expression the scheduler needs to setup a
     **                          cron job.
     */
    @Override
    public String compose(final Date startTime) {
      GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
      calendar.setTime(startTime);
      this.second = String.valueOf(calendar.get(13));
      this.minute = String.valueOf(calendar.get(12));
      this.hour = String.valueOf(calendar.get(11));
      this.dayOfWeek = "";
      for (int i = 0; i < this.interval.length; i++) {
        if (i == 0)
          this.dayOfWeek = String.valueOf(this.interval[i]);
        else {
          this.dayOfWeek += ",";
          this.dayOfWeek += this.interval[i];
        }
      }
      this.dayOfMonth = "?";

      return make();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Monthly
  // ~~~~~ ~~~~~~~
  /**
   ** <code>Monthly</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   */
  public static class Monthly extends Expression {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int[] interval = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Monthly</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Monthly() {
      // ensure inheritance
      super(MONTHLY);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setInterval
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>interval</code>.
     **
     ** @param  monthDays        the interval of day of month the cron job has
     **                          to scheduled.
     */
    public void setInterval(final String monthDays) {
      // prevent bogus input
      if (StringUtility.isEmpty(monthDays))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "interval"));

      final String[] day = monthDays.split(" ");
      this.interval = new int[day.length];
      for (int i = 0; i < day.length; i++) {
        final int number = Integer.valueOf(day[i]).intValue();
        if ((number < 0) || (number > 31))
          throw new BuildException(ServiceResourceBundle.string(ServiceError.TYPE_ATTRIBUTE_VALUE));
        this.interval[i] = number;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compose (Expression)
    /**
     ** Comspose the expression the scheduler needs to setup a cron job.
     **
     ** @return                  the expression the scheduler needs to setup a
     **                          cron job.
     */
    @Override
    public String compose(final Date startTime) {
      GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
      calendar.setTime(startTime);
      this.second = String.valueOf(calendar.get(GregorianCalendar.SECOND));
      this.minute = String.valueOf(calendar.get(GregorianCalendar.MINUTE));
      this.hour = String.valueOf(calendar.get(GregorianCalendar.HOUR));
      this.dayOfWeek = "?";

      this.dayOfMonth = "";
      for (int i = 0; i < this.interval.length; i++) {
        if (i == 0)
          this.dayOfMonth = String.valueOf(this.interval[i]);
        else {
          this.dayOfMonth += ",";
          this.dayOfMonth += this.interval[i];
        }
      }

      return make();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MonthlyWeekly
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** <code>MonthlyWeekly</code> represents the information about the
   ** 'mechanism' by which Jobs are scheduled.
   */
  public static class MonthlyWeekly extends Expression {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int[] interval = null;
    private int[] which = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>MonthlyWeekly</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public MonthlyWeekly() {
      // ensure inheritance
      super(MWD);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setInterval
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>interval</code>.
     **
     ** @param  weekDays         the interval of weekly scheduled cron job
     **                          schedule.
     */
    public void setInterval(final String weekDays) {
      // prevent bogus input
      if (StringUtility.isEmpty(weekDays))
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "interval"));

      final String[] day = weekDays.split(" ");
      this.interval = new int[day.length];
      this.which = new int[day.length];
      for (int i = 0; i < day.length; i++) {
        final String[] part = day[i].split("#");
        final String[] argument = { day[i], "interval", "MON#1 WED#3" };
        if (part.length != 2)
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_VALUE, argument));

        if (WEEK.containsKey(part[0])) {
          this.interval[i] = WEEK.get(part[0]);
          this.which[i] = Integer.parseInt(part[1]);
        }
        else {
          argument[0] = part[0];
          throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_VALUE, argument));
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compose (Expression)
    /**
     ** Comspose the expression the scheduler needs to setup a cron job.
     **
     ** @return                  the expression the scheduler needs to setup a
     **                          cron job.
     */
    @Override
    public String compose(final Date startTime) {
      GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
      calendar.setTime(startTime);
      this.second = String.valueOf(calendar.get(GregorianCalendar.SECOND));
      this.minute = String.valueOf(calendar.get(GregorianCalendar.MINUTE));
      this.hour = String.valueOf(calendar.get(GregorianCalendar.HOUR));
      this.dayOfWeek = "";
      for (int i = 0; i < this.interval.length; i++) {
        if (i == 0)
          this.dayOfWeek = String.valueOf(this.interval[i]) + '#' + String.valueOf(this.which[i]);
        else {
          this.dayOfWeek += ',';
          this.dayOfWeek += this.interval[i];
          this.dayOfWeek += '#';
          this.dayOfWeek += this.which[i];
        }
      }
      this.dayOfMonth = "?";

      return make();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Yearly
  // ~~~~~ ~~~~~
  /**
   ** <code>Yearly</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   */
  public static class Yearly extends Expression {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Yearly</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Yearly() {
      // ensure inheritance
      super(YEARLY);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compose (Expression)
    /**
     ** Comspose the expression the scheduler needs to setup a cron job.
     **
     ** @return                  the expression the scheduler needs to setup a
     **                          cron job.
     */
    @Override
    public String compose(final Date startTime) {
      GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
      calendar.setTime(startTime);
      this.second = String.valueOf(calendar.get(GregorianCalendar.SECOND));
      this.minute = String.valueOf(calendar.get(GregorianCalendar.MINUTE));
      this.hour = String.valueOf(calendar.get(GregorianCalendar.HOUR));
      this.dayOfWeek = "?";
      this.dayOfMonth = String.valueOf(calendar.get(GregorianCalendar.DAY_OF_MONTH));
      this.month = String.valueOf(calendar.get(GregorianCalendar.MONTH) + 1);

      return make();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Expression</code> with the specified properties.
   **
   ** @param  type               the type of the expression.
   */
  protected Expression(final String type) {
    // ensure inheritance
    super();

    // initialize instance
    setValue(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** Build the expression the scheduler needs to setup a cron job.
   **
   ** @param  startTime          the timestamp the exprsssion has to build upon.
   **
   ** @return                    the expression the scheduler needs to setup a
   **                            cron job.
   */
  public abstract String compose(final Date startTime);

  //////////////////////////////////////////////////////////////////////////////
  // Method: compose
  /**
   ** Build the expression the scheduler needs to setup a cron job.
   **
   ** @return                  the expression the scheduler needs to setup a
   **                          cron job.
   */
  protected String make() {
    final StringBuilder builder = new StringBuilder();
    builder.append(this.second);
    builder.append(" ");
    builder.append(this.minute);
    builder.append(" ");
    builder.append(this.hour);
    builder.append(" ");
    builder.append(this.dayOfMonth);
    builder.append(" ");
    builder.append(this.month);
    builder.append(" ");
    builder.append(this.dayOfWeek);
    builder.append(" ");
    builder.append(this.year);
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return TYPE;
  }
}
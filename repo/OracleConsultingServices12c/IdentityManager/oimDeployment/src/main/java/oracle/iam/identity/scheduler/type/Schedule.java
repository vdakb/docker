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

    File        :   Schedule.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Schedule.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.scheduler.type;

import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.EnumeratedAttribute;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Schedule
// ~~~~~ ~~~~~~~~
/**
 ** <code>Schedule</code> represents the information about the 'mechanism' by
 ** which Jobs are scheduled.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Schedule extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String NONE = "none";
  public static final String SINGLE = "Single";
  public static final String CRON = "Cron";
  public static final String PRERIODIC = "Periodic";

  private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.SSS";

  private static final String[]         TYPE = { NONE, SINGLE, CRON, PRERIODIC };
  private static final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

  public static final Schedule NOW = new Schedule(NONE);

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /** indicates that a schedule trigger is assigned by a nested element. */
  private Schedule instance = null;

  /**
   ** the number of times the scheduler tries to start the job before assigning
   ** the <code>Stopped</code> status to the job.
   */
  private Integer retry = null;

  /**
   ** the time the job this trigger belongs to should be executed the first
   ** time.
   */
  private Date firstTime = null;

  /**
   ** the time the job this trigger belongs to should be executed the next time.
   */
  private Date lastTime = null;

  /** the number of times the job this trigger belongs to will be repeated. */
  private Integer repeat = null;

  /** the interval the job will be repeated in milliseconds. */
  private Long interval = null;

  /** the trigger expresssion to schedule a {@link Job}. */
  private String expression = SystemConstant.EMPTY;

  private String frequencyType = SystemConstant.EMPTY;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class None
  // ~~~~~ ~~~~
  /**
   ** <code>None</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   ** <p>
   ** <code>None</code> should meet your scheduling needs if you not have a job
   ** execution.
   */
  public static class None extends Schedule {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>None</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public None() {
      // ensure inheritance
      super(NONE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   setRetry
    /**
     ** Called to inject the argument for parameter
     ** <code>retry</code>.
     **
     ** @param  retry              the number of times the scheduler tries to
     **                            start the job before assigning the
     **                            <code>Stopped</code> status to the job.
     */
    public void setRetry(final Integer retry) {
      super.retry(retry);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Single
  // ~~~~~ ~~~~~~
  /**
   ** <code>Single</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   ** <p>
   ** <code>Single</code> should meet your scheduling needs if you need to have
   ** a job execute exactly once at a specific moment in time, or at a specific
   ** moment in time followed by repeats. For example, if you want the trigger
   ** to fire at exactly
   ** <i>11:23:54 AM on January 13, 2015</i>, or if you want it to fire at that
   ** time, and then fire five more times.
   ** <p>
   ** With this description, you may not find it surprising to find that the
   ** properties of a <code>Single</code> include:
   ** <ul>
   **   <li>a start-time
   ** </ul>
   ** All of these properties are exactly what you'd expect them to be, with
   ** only a couple special notes related to the end-time property.
   */
  public static class Single extends Schedule {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Single</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Single() {
      // ensure inheritance
      this(SINGLE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Single</code> with the specified type.
     **
     ** @param  type             the type of the schedule.
     */
    protected Single(final String type) {
      // ensure inheritance
      super(type);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   setRetry
    /**
     ** Called to inject the argument for parameter
     ** <code>retry</code>.
     **
     ** @param  retry              the number of times the scheduler tries to
     **                            start the job before assigning the
     **                            <code>Stopped</code> status to the job.
     */
    public void setRetry(final Integer retry) {
      super.retry(retry);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   setFirstTime
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>startTime</code>.
     **
     ** @param  firstTime          the timestamp the job this trigger belongs to
     **                            should be executed the first time.
     **
     ** @throws BuildException     if <code>firstTime</code> is either empty or
     **                            cannot be converted to a valid date.
     */
    public void setFirstTime(final String firstTime) {
      if (StringUtility.isEmpty(firstTime)) {
        final String[] argument = { "firstTime" };
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, argument));
      }

      try {
        super.firstTime(formatter.parse(firstTime));
      }
      catch (ParseException e) {
        final String[] argument = { "firstTime", DATE_FORMAT };
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_FORMAT, argument));
      }

      // a single trigger has a always a retry interval of 1. Due to the
      // inheritance of a simple trigger we has to perform the check to not
      // override the last retry interval value of this trigger type
      if (super.interval() == null)
        super.interval(1L);

      // a single trigger has a always a repeat count of 0. Due to the
      // inheritance of a simple trigger we has to perform the check to not
      // override the last retry interval value of this trigger type
      if (super.repeat() == null)
        super.repeat(0);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Simple
  // ~~~~~ ~~~~~~
  /**
   ** <code>Simple</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   ** <p>
   ** <code>Simple</code> should meet your scheduling needs if you need to have
   ** a job execute exactly once at a specific moment in time, or at a specific
   ** moment in time followed by repeats at a specific interval. For example, if
   ** you want the trigger to fire at exactly
   ** <i>11:23:54 AM on January 13, 2015</i>, or if you want it to fire at that
   ** time, and then fire five more times, every ten seconds.
   ** <p>
   ** With this description, you may not find it surprising to find that the
   ** properties of a <code>Simple</code> include:
   ** <ul>
   **   <li>a start-time, and last-time
   **   <li>a repeat count
   **   <li>a repeat interval
   **</ul>
   ** All of these properties are exactly what you'd expect them to be, with
   ** only a couple special notes related to the end-time property.
   ** <p>
   ** The repeat count can be zero, a positive integer, or the constant value
   ** Simple#REPEAT_INDEFINITELY. The repeat interval property must be zero, or
   ** a positive long value, and represents a number of milliseconds.
   ** <br>
   ** <b>Note</b> that a repeat interval of zero will cause 'repeat count'
   ** firings of the trigger to happen concurrently (or as close to concurrently
   ** as the scheduler can manage).
   ** <p>
   ** The repeat count can be zero, a positive integer, or the constant value
   ** Single#REPEAT_INDEFINITELY.
   ** <p>
   ** The <code>lastTime</code> property (if it is specified) overrides the
   ** repeat count property. This can be useful if you wish to create a trigger
   ** such as one that fires every 10 seconds until a given moment in time -
   ** rather than having to compute the number of times it would repeat between
   ** the start-time and the end-time, you can simply specify the end-time and
   ** then use a repeat count of Single#REPEAT_INDEFINITELY (you could
   ** even specify a repeat count of some huge number that is sure to be more
   ** than the number of times the trigger will actually fire before the
   ** end-time arrives).
   */
  public static class Simple extends Single {

    ////////////////////////////////////////////////////////////////////////////
    // instance attribute
    ////////////////////////////////////////////////////////////////////////////

    /** the frequency of the job this trigger belongs to will be executed. */
    private Frequency frequency = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Simple</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Simple() {
      // initialize instance
      this(PRERIODIC);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Simple</code> with the specified type.
     **
     ** @param  type             the type of the schedule.
     */
    protected Simple(final String type) {
      // ensure inheritance
      super(type);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setFrequencyType
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>frequencyType</code>.
     **
     ** @param  frequencyType    the interval of the job this trigger belongs to
     **                          will be repeated.
     */
    public final void setFrequencyType(final String frequencyType) {
      super.frequencyType(frequencyType);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setFrequency
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>frequency</code>.
     **
     ** @param  frequency        the {@link Frequency} of the schedule.
     */
    public void setFrequency(final Frequency frequency) {
      this.frequency = frequency;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setInterval
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>interval</code>.
     **
     ** @param  interval         the interval of the schedule.
     */
    public void setInterval(final Long interval) {
      // prevent bogus inpput
      if (this.frequency == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "frequency"));

      // copy the existing interval value to the passed parameter
      super.interval(this.frequency.calculate(interval));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Cron
  // ~~~~~ ~~~~
  /**
   ** <code>Cron</code> represents the information about the 'mechanism' by
   ** which Jobs are scheduled.
   ** <p>
   ** <code>Cron</code> uses "cron expressions", which are able to create firing
   ** schedules such as:
   ** <br>
   ** <i>At 8:00am every Monday through Friday</i> or
   ** <i>At 1:30am every last Friday of the month</i>.
   ** <br>
   ** Cron expressions are powerful, but can be pretty confusing. This tutorial
   ** aims to take some of the mystery out of creating a cron expression, giving
   ** users a resource which they can visit before having to ask in a forum or
   ** mailing list.
   */
  public static class Cron extends Simple {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String type = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Cron</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Cron() {
      // ensure inheritance
      super(CRON);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: schedule
    /**
     ** Returns the cron type of this trigger.
     ** <p>
     **
     ** @return                  the cron type of this trigger.
     */
    public final String type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   setLastTime
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>lastTime</code>.
     **
     ** @param  lastTime         the timestamp the job this trigger belongs to
     **                          should be executed the last time.
     **
     ** @throws BuildException   if <code>lastTime</code> is either empty or
     **                          cannot be converted to a valid date.
     */
    public void setLastTime(final String lastTime) {
      if (StringUtility.isEmpty(lastTime)) {
        final String[] argument = { "lastTime" };
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MANDATORY, argument));
      }

      try {
        this.lastTime(formatter.parse(lastTime));
      }
      catch (ParseException e) {
        final String[] argument = { "lastTime", DATE_FORMAT };
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_FORMAT, argument));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredDaily
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>expression</code>.
     **
     ** @param  expression       the expression to schedule the {@link Job}.
     */
    public final void addConfiguredDaily(final Expression.Daily expression) {
      expression(expression);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredWeekly
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>expression</code>.
     **
     ** @param  expression       the expression to schedule the {@link Job}.
     */
    public final void addConfiguredWeekly(final Expression.Weekly expression) {
      expression(expression);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredMonthly
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>expression</code>.
     **
     ** @param  expression       the expression to schedule the {@link Job}.
     */
    public final void addConfiguredMonthly(final Expression.Monthly expression) {
      expression(expression);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredMonthlyWeekly
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>expression</code>.
     **
     ** @param  expression       the expression to schedule the {@link Job}.
     **
     ** @throws BuildException   if the <code>type</code> is already set.
     */
    public final void addConfiguredMonthlyWeekly(final Expression.MonthlyWeekly expression) {
      expression(expression);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addConfiguredYearly
    /**
     ** Call by the ANT deployment to inject the argument for parameter
     ** <code>expression</code>.
     **
     ** @param  expression       the expression to schedule the {@link Job}.
     **
     ** @throws BuildException   if the <code>type</code> is already set.
     */
    public final void addConfiguredYearly(final Expression.Yearly expression) {
      expression(expression);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groupd by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: expression
    /**
     ** Called to inject the argument for parameter <code>expression</code>.
     **
     ** @param  expression       the expression to schedule the {@link Job}.
     **
     ** @throws BuildException   if the <code>type</code> is already set.
     */
    private void expression(final Expression expression) {
      if (this.type != null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_ONLYONCE, "daily, weekly, monthly, montlyweekly and yearly"));

      this.type = expression.getValue();
      super.expression(expression.compose(this.firstTime()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Schedule</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Schedule() {
    // ensure inheritance
    this(NONE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Schedule</code> with the specified properties.
   **
   ** @param  type               the type of the schedule.
   */
  protected Schedule(final String type) {
    // ensure inheritance
    super();

    // initialize instance
    setValue(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retry
  /**
   ** Called to inject the argument for parameter
   ** <code>retryCount</code>.
   **
   ** @param  retry              the number of times the scheduler tries to
   **                            start the job before assigning the
   **                            <code>Stopped</code> status to the job.
   */
  public void retry(final Integer retry) {
    this.retry = retry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retry
  /**
   ** Returns the number of times the scheduler tries to start the job before
   ** assigning the <code>Stopped</code> status to the job.
   **
   ** @return                    the number of times the scheduler tries to
   **                            start the job before assigning the
   **                            <code>Stopped</code> status to the job.
   */
  public final Integer retry() {
    return this.retry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstTime
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>startTime</code>.
   **
   ** @param  firstTime          the timestamp the job this trigger belongs to
   **                            should be executed the first time.
   **
   ** @throws BuildException     If the <code>lastTime</code> is already set
   **                            and <code>lastTime</code> is before
   **                            <code>firstTime</code>.
   */
  protected void firstTime(final Date firstTime) {
    if (this.lastTime != null && firstTime != null && this.lastTime.before(firstTime))
      throw new BuildException("Last time cannot be before first time");

    this.firstTime = firstTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstTime
  /**
   ** Returns the time the job this trigger belongs to should be executed the
   ** first time.
   **
   ** @return                    the time the job this trigger belongs to should
   **                            be executed the first time.
   */
  public final Date firstTime() {
    return this.firstTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastTime
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>lastTime</code>.
   **
   ** @param  lastTime            the timestamp the job this trigger belongs to
   **                            should be executed the last time.
   **
   ** @throws BuildException     If the <code>firstTime</code> is already set
   **                            and <code>lastTime</code> is before
   **                            <code>firstTime</code>.
   */
  protected void lastTime(final Date lastTime) {
    if (lastTime != null && this.firstTime != null && lastTime.before(this.firstTime))
      throw new BuildException("Last time cannot be before first time");

    this.lastTime = lastTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastTime
  /**
   ** Returns the time the job this trigger belongs to should be executed the
   ** last time.
   **
   ** @return                    the time the job this trigger belongs to should
   **                            be executed the last time.
   */
  public final Date lastTime() {
    return this.lastTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repeat
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>repeat</code>.
   **
   ** @param  repeat             the number of times the job this trigger
   **                            belongs to will be repeated.
   */
  protected final void repeat(final Integer repeat) {
    this.repeat = repeat;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   repeat
  /**
   ** Returns the number of times the job this trigger belongs to will be
   ** repeated.
   **
   ** @return                    the number of times the job this trigger
   **                            belongs to will be repeated.
   */
  public final Integer repeat() {
    return this.repeat;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interval
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>interval</code>.
   **
   ** @param  interval           the interval of the job this trigger belongs to
   **                            will be executed.
   */
  protected final void interval(final Long interval) {
    this.interval = interval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interval
  /**
   ** Returns the interval of the job this trigger belongs to will be repeated.
   **
   ** @return                    the interval of the job this trigger belongs to
   **                            will be executed.
   */
  public Long interval() {
    return this.interval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frequencyType
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>frequencyType</code>.
   **
   ** @param  frequencyType      the interval of the job this trigger belongs to
   **                            will be repeated.
   */
  protected final void frequencyType(final String frequencyType) {
    this.frequencyType = frequencyType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frequencyType
  /**
   ** Returns the interval of the job this trigger belongs to will be repeated.
   **
   ** @return                    the interval of the job this trigger belongs to
   **                            will be repeated.
   */
  public String frequencyType() {
    return this.frequencyType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expression
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>expression</code>.
   ** <p>
   ** <b>Format</b>
   ** <br>
   ** A cron expression is a string comprised of 6 or 7 fields separated by
   ** white space. Fields can contain any of the allowed values, along with
   ** various combinations of the allowed special characters for that field.
   ** The fields are as follows:
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
   **       useful when you need to specify something in one of the two fields
   **       in which the character is allowed, but not the other. For example,
   **       if you want your trigger to fire on a particular day of the month
   **       (say, the 10th), but don't care what day of the week that happens to
   **       be, you would put <code>10</code> in the day-of-month field, and
   **       <b>?</b> in the day-of-week field. See the examples below for
   **       clarification.
   **   <li><b>-</b> -
   **       used to specify ranges. For example, <code>10-12</code> in the hour
   **       field means <i>the hours 10, 11 and 12</i>.
   **   <li><b>/</b> -
   **       used to specify increments. For example, <code>0/15</code> in the
   **       seconds field means <i>the seconds 0, 15, 30, and 45</i>. And
   **       <code>5/15</code> in the seconds field means
   **       <i>the seconds 5, 20, 35, and 50</i>. You can also specify
   **       <b>/</b> after the character <b>-</b> in this case is equivalent
   **       to having <code>0</code> before the <b>/</b>. <code>1/3</code> in
   **       the day-of-month field means
   **       <i>fire every 3 days starting on the first day of the month</i>.
   **   <li><b>L</b> <i>("last")</i> -
   **       has different meaning in each of the two fields in which it is
   **       allowed. For example, the value <b>L</b> in the day-of-month field
   **       means <i>the last day of the month</i> - day 31 for January, day
   **       28 for February on non-leap years. If used in the day-of-week field
   **       by itself, it simply means <code>7</code> or <code>SAT</code>. But
   **       if used in the day-of-week field after another value, it means
   **       <i>the last xxx day of the month</i> - for example <code>6L</code>
   **       means <i>the last friday of the month</i>. When using the <b>L</b>
   **       option, it is important not to specify lists, or ranges of values,
   **       as you'll get confusing results.
   **   <li><b>W</b> <i>("weekday")</i> -
   **       used to specify the weekday (Monday-Friday) nearest the given day.
   **       As an example, if you were to specify <code>15W</code> as the value
   **       for the day-of-month field, the meaning is:
   **       <i>the nearest weekday to the 15th of the month</i>. So if the 15th
   **       is a Saturday, the trigger will fire on Friday the 14th. If the 15th
   **       is a Sunday, the trigger will fire on Monday the 16th. If the 15th
   **       is a Tuesday, then it will fire on Tuesday the 15th. However if you
   **       specify <code>1W</code> as the value for day-of-month, and the 1st
   **       is a Saturday, the trigger will fire on Monday the 3rd, as it will
   **       not <b>jump</b> over the boundary of a month's days. The <b>W</b>
   **       character can only be specified when the day-of-month is a single
   **       day, not a range or list of days.
   **   <li><b>#</b> -
   **       used to specify <i>"the nth"</i> XXX day of the month. For example,
   **       the value of <code>6#3</code> in the day-of-week field means
   **       <i>the third Friday of the month</i> (day 6 = Friday and
   **       <code>#3</code> = the 3rd one in the month). Other examples:
   **       <code>2#1</code> = the first Monday of the month and
   **       <code>4#5</code> = the fifth Wednesday of the month.
   **       <b>Note</b> that if you specify <code>#5</code> and there is not 5
   **       of the given day-of-week in the month, then no firing will occur
   **       that month.
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
   **   <li>Support for specifying both a day-of-week and a day-of-month value
   **       is not complete (you must currently use the '?' character in one of
   **       these fields).
   **   <li>Be careful when setting fire times between mid-night and 1:00 AM -
   **       "daylight savings" can cause a skip or a repeat depending on whether
   **       the time moves back or jumps forward.
   ** </ul>
   **
   ** @param  expression         the expression to schedule the {@link Job}.
   */
  protected final void expression(final String expression) {
    this.expression = expression;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expression
  /**
   ** Returns the trigger expression to schedule a cron job.
   **
   ** @return                    the expression to schedule the {@link Job}.
   */
  public String expression() {
    return this.expression;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredNone
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link None}.
   **
   ** @param  schedule           the {@link None} schedule to add.
   **
   ** @throws BuildException     if a <code>Schedule</code> instance is already
   **                            set.
   */
  public void addConfiguredNone(final None schedule) {
    this.instance(schedule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredSingle
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Single}.
   **
   ** @param  schedule           the {@link Single} schedule to add.
   **
   ** @throws BuildException     if a <code>Schedule</code> instance is already
   **                            set.
   */
  public void addConfiguredSingle(final Single schedule) {
    this.instance(schedule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredSimple
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Simple}.
   **
   ** @param  schedule           the {@link Simple} schedule to add.
   **
   ** @throws BuildException     if a <code>Schedule</code> instance is already
   **                            set.
   */
  public void addConfiguredSimple(final Simple schedule) {
    this.instance(schedule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredCron
  /**
   ** Call by the ANT deployment to inject the argument for adding a
   ** {@link Cron}.
   **
   ** @param  schedule           the {@link Cron} schedule to add.
   **
   ** @throws BuildException     if a <code>Schedule</code> instance is already
   **                            set.
   */
  public void addConfiguredCron(final Cron schedule) {
    this.instance(schedule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Sets the schedule instance that triggers a job.
   **
   ** @param  instance           the <code>Schedule</code> that triggers a job.
   **
   ** @throws BuildException     if a <code>Schedule</code> instance is already
   **                            set.
   */
  protected void instance(final Schedule instance) {
    // prevent bogus input
    if (this.instance != null)
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ELEMENT_ONLYONCE, "single,simple or cron"));

    this.instance = instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the schedule instance that triggers a job.
   **
   ** @return                    the <code>Schedule</code> that triggers a job.
   */
  public final Schedule instance() {
    return this.instance;
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
   **                          enumeration. The order of elements must be
   **                          fixed so that indexOfValue(String) always
   **                          return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return TYPE;
  }
}
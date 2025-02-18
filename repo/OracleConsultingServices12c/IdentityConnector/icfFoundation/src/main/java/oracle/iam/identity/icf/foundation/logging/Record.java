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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   Record.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Record.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

////////////////////////////////////////////////////////////////////////////////
// class Record
// ~~~~~ ~~~~~~
/**
 ** Record objects are used to pass logging requests between the logging
 ** framework and individual log Handlers.
 ** <p>
 ** When a Record is passed into the logging framework it logically belongs to
 ** the framework and should no longer be used or updated by the client
 ** application.
 ** <p>
 ** Note that if the client application has not specified an explicit source
 ** method name and source class name, then the Record class will infer them
 ** automatically when they are first accessed (due to a call on
 ** getSourceMethodName or getSourceClassName) by analyzing the call stack.
 ** Therefore, if a logging Handler wants to pass off a Record to another
 ** thread, or to transmit it over RMI, and if it wishes to subsequently obtain
 ** method name or class name information it should call one of
 ** getSourceClassName or getSourceMethodName to force the values to be filled
 ** in.
 ** <p>
 ** <b>Serialization notes:</b>
 ** <ul>
 **   <li>The LogRecord class is serializable.
 **   <li>Because objects in the parameters array may not be serializable,
 **       during serialization all objects in the parameters array are
 **       written as the corresponding Strings (using Object.toString).
 **   <li>The ResourceBundle is not transmitted as part of the serialized form,
 **       but the resource bundle name is, and the recipient object's
 **       readObject method will attempt to locate a suitable resource bundle.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class Record extends LogRecord {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String JDK_LOG_CLASS   = "java.util.logging.Logger";
  private static final String ICF_LOG_CLASS   = "oracle.iam.identity.icf.foundation.logging.Logger";

  private transient String  category          = "oracle.iam.identity.icf.foundation.logging.Logger";
  private transient boolean needToInferCaller = true;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-6714111552396665913")
  private static final long serialVersionUID = -1633895946358907528L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Record</code> with the given level and message values.
   ** <p>
   ** The sequence property will be initialized with a new unique value.
   ** These sequence values are allocated in increasing order within a VM.
   ** <p>
   ** The millis property will be initialized to the current time.
   ** <p>
   ** The thread ID property will be initialized with a unique ID for the
   ** current thread.
   ** <p>
   ** All other properties will be initialized to "null".
   **
   ** @param  level              a logging level value.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  message            the raw non-localized logging message
   **                            (may be null).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public Record(final Level level, final String message) {
    // ensure inheritance
    super(level, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Record</code> with the given level and message values.
   ** <p>
   ** The sequence property will be initialized with a new unique value.
   ** These sequence values are allocated in increasing order within a VM.
   ** <p>
   ** The millis property will be initialized to the current time.
   ** <p>
   ** The thread ID property will be initialized with a unique ID for the
   ** current thread.
   ** <p>
   ** All other properties will be initialized to "null".
   **
   ** @param  level              a logging level value.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  message            the raw non-localized logging message
   **                            (may be null).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public Record(final Class<?> logger, final Level level, final String message) {
    this(logger != null ? logger.getName() : null, level, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Record</code> with the given level and message values.
   ** <p>
   ** The sequence property will be initialized with a new unique value.
   ** These sequence values are allocated in increasing order within a VM.
   ** <p>
   ** The millis property will be initialized to the current time.
   ** <p>
   ** The thread ID property will be initialized with a unique ID for the
   ** current thread.
   ** <p>
   ** All other properties will be initialized to "null".
   **
   **
   ** @param  category           the category for the {@link Logger} this
   **                            record belongs.
   **                            <br>
   **                            This should be a dot-separated name and should
   **                            usually be based on the package or class name
   **                            of the subsystem, such as java.net or
   **                            javax.swing
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  level              a logging level value.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  message            the raw non-localized logging message
   **                            (may be null).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public Record(final String category, final Level level, final String message) {
    // ensure inheritance
    super(level, message);

    // initialize instance attributes
    if (category != null) {
      this.category = category;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSourceClassName (overridden)
  /**
   ** Set the name of the class that (allegedly) issued the logging request.
   **
   ** @param  clazz              the source class name (may be
   **                            <code>null</code>).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setSourceClassName(final String clazz) {
    this.needToInferCaller = false;
    // ensure inheritance
    super.setSourceClassName(clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSourceClassName (overridden)
  /**
   ** Return the name of the class that (allegedly) issued the logging request.
   ** <p>
   ** Note that this sourceClassName is not verified and may be spoofed.
   ** <br>
   ** This information may either have been provided as part of the logging
   ** call, or it may have been inferred automatically by the logging framework.
   ** In the latter case, the information may only be approximate and may in
   ** fact describe an earlier call on the stack frame.
   ** <p>
   ** May be <code>null</code> if no information could be obtained.
   **
   ** @return                    the source class name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getSourceClassName() {
    if (this.needToInferCaller) {
      inferCaller();
    }
    // ensure inheritance
    return super.getSourceClassName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSourceClassName (overridden)
  /**
   ** Set the name of the method that (allegedly) issued the logging request.
   **
   ** @param  method             the source method name(may be
   **                            <code>null</code>).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void setSourceMethodName(final String method) {
    this.needToInferCaller = false;
    // ensure inheritance
    super.setSourceMethodName(method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSourceMethodName (overridden)
  /**
   ** Return the name of the method that (allegedly) issued the logging request.
   ** <p>
   ** Note that this sourceMethodName is not verified and may be spoofed.
   ** <br>
   ** This information may either have been provided as part of the logging
   ** call, or it may have been inferred automatically by the logging framework.
   ** In the latter case, the information may only be approximate and may in
   ** fact describe an earlier call on the stack frame.
   ** <p>
   ** May be <code>null</code> if no information could be obtained.
   **
   ** @return                    the source method name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String getSourceMethodName() {
    if (this.needToInferCaller) {
      inferCaller();
    }
    // ensure inheritance
    return super.getSourceMethodName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inferCaller
  private void inferCaller() {
    this.needToInferCaller = false;
    final StackTraceElement[] stack = new Throwable().getStackTrace();
    int i = 0;
    while (i < stack.length) {
      final StackTraceElement frame = stack[i];
      final String            clazz = frame.getClassName();
      if ((clazz.equals(JDK_LOG_CLASS)) || (clazz.equals(ICF_LOG_CLASS)) || (clazz.equals(this.category)))
        break;
      i++;
    }

    while (i < stack.length) {
      final StackTraceElement frame = stack[i];
      final String            clazz = frame.getClassName();
      if ((clazz.equals(JDK_LOG_CLASS)) || (clazz.equals(ICF_LOG_CLASS)) || (clazz.equals(this.category)))
        i++;
      else {
        String mname = frame.getMethodName();
        if (("log".equals(mname)) || ("_log".equals(mname))) {
          i++;
        }
        else {
          setSourceClassName(clazz);
          setSourceMethodName(mname);
          return;
        }
      }
    }
    setSourceClassName(null);
    setSourceMethodName(null);
  }
}
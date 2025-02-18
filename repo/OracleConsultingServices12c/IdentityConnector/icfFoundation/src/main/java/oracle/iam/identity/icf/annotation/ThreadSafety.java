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

    File        :   ThreadSafety.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the annotation
                    ThreadSafety.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

////////////////////////////////////////////////////////////////////////////////
// annotation ThreadSafety
// ~~~~~~~~~~ ~~~~~~~~~~~~
/**
 ** This annotation type may be used to indicate the level of thread safety for
 ** a class or method.
 ** <br>
 ** Any class or interface which does not include the <code>ThreadSafety</code>
 ** annotation should be assumed to be not threadsafe unless otherwise specified
 ** in the documentation for that class or interface.
 ** <p>
 ** If the <code>ThreadSafety</code> annotation is applied to a method, then it
 ** will override the class-level annotation for the scope of that method.
 ** <br>
 ** That is, if a class is declared to be
 ** <code>ThreadSafetyLevel.MOSTLY_NOT_THREADSAFE</code> but a method within
 ** that class is declared to be
 ** <code>ThreadSafetyLevel.METHOD_THREADSAFE</code>, then that method may be
 ** invoked concurrently by multiple threads against the same instance.
 ** <br>
 ** If a class is declared to be
 ** <code>ThreadSafetyLevel.MOSTLY_THREADSAFE</code> but a method within that
 ** class is declared to be
 ** <code>ThreadSafetyLevel.METHOD_NOT_THREADSAFE</code>, then that method must
 ** not be invoked on an instance while any other thread is attempting to access
 ** the same instance.
 ** <br>
 ** Methods within a class may only be annotated with either the
 ** <code>ThreadSafetyLevel.METHOD_THREADSAFE</code> or
 ** <code>ThreadSafetyLevel.METHOD_NOT_THREADSAFE</code> level, and only if the
 ** class is annotated with one of the
 ** <code>ThreadSafetyLevel.MOSTLY_THREADSAFE</code>,
 ** <code>ThreadSafetyLevel.MOSTLY_NOT_THREADSAFE</code>, or
 ** <code>ThreadSafetyLevel.INTERFACE_NOT_THREADSAFE</code> level.
 ** <br>
 ** Classes annotated with either the
 ** <code>ThreadSafetyLevel.COMPLETELY_THREADSAFE</code> or
 ** <code>ThreadSafetyLevel.NOT_THREADSAFE</code> levels must not provide
 ** alternate method-level <code>ThreadSafety</code> annotations.
 ** <br>
 ** Note that there are some caveats regarding thread safety and immutability of
 ** elements in the LDAP SDK that are true regardless of the stated thread
 ** safety level:
 ** <ul>
 **   <li>If an array is provided as an argument to a constructor or a method,
 **       then that array must not be referenced or altered by the caller at any
 **       time after that point unless it is clearly noted that it is acceptable
 **       to do so.
 **   <li>If an array is returned by a method, then the contents of that array
 **       must not be altered unless it is clearly noted that it is acceptable
 **       to do so.
 **   <li>If a method is intended to alter the state of an argument (e.g.,
 **       appending to a <code>StringBuilder</code> or <code>ByteBuffer</code>
 **       or <code>ByteStringBuffer</code>, reading from a <code>Reader</code>
 **       or an <code>InputStream</code>, or writing to a <code>Writer</code> or
 **       <code>OutputStream</code>), then that object provided as an argument
 **       must not be accessed by any other thread while that method is active
 **       unless it is clearly noted that it is acceptable to do so.
 **   <li>Unless otherwise noted, public static methods may be assumed to be
 **       threadsafe independent of the thread safety level for the class that
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ThreadSafety {

  //////////////////////////////////////////////////////////////////////////////
  // enum Level
  // ~~~~ ~~~~~
  /**
   ** This enumeration defines a set of thread safety levels that may be used to
   ** indicate whether the associated code is safe to be accessed concurrently
   ** by multiple threads.
   */
  static enum Level {
      /**
       ** The associated code is not threadsafe.
       ** <br>
       ** Unless otherwise noted, multiple threads may not attempt to invoke
       ** methods on the same instance of objects of this type without external
       ** synchronization.
       */
      NOT
      /**
       ** The associated code is completely threadsafe and may be accessed
       ** concurrently by any number of threads, subject to the constraints
       ** described in the {@link ThreadSafety} documentation.
       */
    , COMPLETELY
      /**
       ** The associated code is mostly threadsafe, but there may be some
       ** methods which are not safe to be invoked when multiple threads are
       ** accessing an instance concurrently.
       ** <br>
       ** The class-level documentation for a class including this thread safety
       ** level should include comments indicating which methods are not
       ** threadsafe, and those methods should also be marked with their own
       ** {@link ThreadSafety} annotations using the {@link #METHOD_NOT} level.
       */
    , MOSTLY
      /**
       ** The associated code is mostly not threadsafe, but there may be some
       ** methods which are safe to be invoked concurrently by multiple threads.
       ** The class-level documentation for a class including this thread safety
       ** level should include comments indicating which methods are threadsafe,
       ** and those methods should also be marked with their own
       ** {@link ThreadSafety} annotations using the {@link #METHOD}
       ** level.
       */
    , MOSTLY_NOT
      /**
       ** Methods declared in the associated interface or abstract class must be
       ** threadsafe in classes which implement that interface or extend that
       ** abstract class.
       ** <br>
       ** No guarantees will be made about the thread safety of other methods
       ** contained in that class which are not declared in the parent interface
       ** or superclass.
       */
    , INTERFACE
      /**
       ** Methods declared in the associated interface or abstract class are not
       ** required to be threadsafe and classes which call them must not rely on
       ** the ability to concurrently invoke those methods on the same object
       ** instance without any external synchronization.
       */
    , INTERFACE_NOT
      /**
       ** The associated method may be considered threadsafe and may be invoked
       ** concurrently by multiple threads, subject to the constraints described
       ** in the {@link ThreadSafety} documentation, and in any additional notes
       ** contained in the method-level javadoc.
       */
    , METHOD
      /**
       ** The associated method may not be considered threadsafe and should not
       ** be invoked concurrently by multiple threads.
       */
    , METHOD_NOT
    ;  
  }

  /**
   ** The thread safety level for the associated class, interface, enum, or
   ** method.
   **
   ** @return                    the thread safety level for the associated
   **                            class, interface, enum, or method.
   */
  @NotNull()
  Level level();
}
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

    File        :   SystemLocale.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemLocale.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation;

import java.util.Locale;

////////////////////////////////////////////////////////////////////////////////
// class SystemLocale
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Thread local variable that impacts localization of all messages in the
 ** framework. This is roughly equivalent to .Net's Thread.CurrentCulture.
 ** <p>
 ** Note that this is an inheritable thread local so it is automatically
 ** inherited from parent to child thread. Of course, if the child thread is
 ** part of a thread pool, you will still need to manually propagate from
 ** parent to child.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemLocale {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final InheritableThreadLocal<Locale> VALUE = new InheritableThreadLocal<Locale>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** Access modifier private prevents other classes using
   ** "new FilterBuilder()"
   */
   private SystemLocale() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears the locale for the current thread.
   */
  public static void clear() {
    VALUE.remove();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns <code>true</code> if a thread-local {@link Locale} is specified on
   ** the current thread.
   **
   ** @return                    <code>true</code> if a thread-local
   **                            {@link Locale} is specified on the current
   **                            thread.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean set() {
    return VALUE.get() != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Sets the {@link Locale} for the current thread.
   **
   ** @param  locale             the {@link Locale} to use.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   */
  public static void set(final Locale locale) {
    VALUE.set(locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns the {@link Locale} from the current thread. Returns
   ** <code>Locale.getDefault</code> if no {@link Locale} is specified.
   **
   ** @return                    the {@link Locale} from the current thread.
   **                            <br>
   **                            Possible object is {@link Locale}.
   */
  public static Locale get() {
    final Locale current = VALUE.get();
    return current == null ? Locale.getDefault() : current;
  }
}
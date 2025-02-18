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

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   LocaleRegistry.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LocaleRegistry.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Locale;

import java.util.concurrent.atomic.AtomicReference;

////////////////////////////////////////////////////////////////////////////////
// abstract class LocaleRegistry
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** An immutable reference to a {@link Locale} instance.
 ** <p>
 ** Can be used as an immutable replacement for {@link Locale#getDefault()}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class LocaleRegistry {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link LocaleRegistry} implements the singleton pattern.
   ** <br>
   ** The static attribute {@link #INSTANCE} holds this single instance.
   */
  private static final AtomicReference<Locale> INSTANCE = new AtomicReference<Locale>();

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>LocaleRegistry</code> is a singleton class.
   ** This method returns the sole instance of the registry.
   **
   ** @return                     the sole instance of the registry.
   */
  public static Locale instance() {
    INSTANCE.compareAndSet(null, Locale.getDefault());
    return INSTANCE.get();
  }
}
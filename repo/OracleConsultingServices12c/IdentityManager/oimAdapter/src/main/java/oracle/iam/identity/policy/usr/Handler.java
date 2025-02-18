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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   Handler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Handler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.policy.usr;

import java.util.Map;
import java.util.Date;

import java.io.Serializable;

import oracle.iam.platform.kernel.EventFailedException;

import oracle.iam.identity.policy.Bundle;

////////////////////////////////////////////////////////////////////////////////
// interface Handler
// ~~~~~~~~~ ~~~~~~~
/**
 ** Declares global visible identifier used or has to be implemented by any
 ** pre- or post-process handler.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public interface Handler {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Obtains a value for the specfied <code>name</code> from the user profile
   ** atribute mapping.
   **
   ** @param  name               the name of the user profile attribute mapping
   **                            thats needs to be obtained.
   ** @param  profile            the user profile attribute mapping.
   **
   ** @return                    the attribute value mapped to <code>name</code>
   **                            in the user profile attribute mapping.
   **                            May be <code>null</code> if no mapping exists
   **                            for <code>name</code>.
   */
  static String stringValue(final String name, final Map<String, Serializable> profile) {
    return profile.get(name) == null ? null : profile.get(name).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Obtains a value for the specfied <code>name</code> from the user profile
   ** atribute mapping.
   **
   ** @param  name               the name of the user profile attribute mapping
   **                            thats needs to be obtained.
   ** @param  profile            the user profile attribute mapping.
   **
   ** @return                    the attribute value mapped to <code>name</code>
   **                            in the user profile attribute mapping.
   **                            May be <code>null</code> if no mapping exists
   **                            for <code>name</code>.
   */
  static Date dateValue(final String name, final Map<String, Serializable> profile) {
    return profile.get(name) == null ? null : (Date)profile.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exception
  /**
   ** Throws aa appropriate {@link EventFailedException}.
   **
   ** @param  t                  if this exception thrown because of another
   **                            exception.
   ** @param  method             the event handler method name this excepetion
   **                            occured.
   ** @param  code               the error code associated with this exception
   ** @param  data               the contextual process information this
   **                            excepetion occured.
   **
   ** @return                    the {@link EventFailedException} with
   **                            the properties to describe the error.
   */
  static EventFailedException exception(final Throwable t, final String method, final String code, final Object[] data) {
    final String message  = (data != null) ? Bundle.format(code, data) : Bundle.string(code);
    final String addition = (t != null) ? t.getMessage() : null;
    final EventFailedException e = new EventFailedException(code, message, addition, method, t);
    if (data != null) {
      e.setErrorData(data);
    }
    return e;
  }
}
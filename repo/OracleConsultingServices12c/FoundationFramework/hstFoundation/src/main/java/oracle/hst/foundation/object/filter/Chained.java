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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared collection facilities

    File        :   Chained.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Chained.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.object.Filter;

import oracle.hst.foundation.resource.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// class Chained
// ~~~~~ ~~~~~~~
/**
 ** Externally chained filters e.g. the filter implementing case insensitive
 ** searches.
 ** <br>
 ** They are removed from translation.
 ** <p>
 ** <b>IMPORTANT</b>:
 ** <br>
 ** Currently, these filters have to be chained at the beginning of the filter
 ** tree.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Chained {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the {@link Filter} this {@link Filter} is chaining. */
  private final Filter filter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Chained</code> which chains the specified
   ** {@link Filter}.
   **
   ** @param  filter             the {@link Filter} that is being chained.
   */
  public Chained(final Filter filter) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (filter == null)
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "filter"));

    // initialize instance attributes
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the internal filter that is being negated.
   **
   ** @return                    the internal filter that is being negated.
   */
  public Filter filter() {
    return this.filter;
  }
}
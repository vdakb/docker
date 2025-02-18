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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   NameToOrganizationWeak.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NameToOrganizationWeak.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import oracle.hst.foundation.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class NameToOrganizationWeak
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>NameToOrganizationWeak</code> transforms a given name to the
 ** system identifier of an organization by looking up the organization the with
 ** the given name.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public class NameToOrganizationWeak extends NameToOrganizationStrict {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>NameToOrganizationWeak</code> which use the
   ** specified {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose.
   */
  public NameToOrganizationWeak(final Logger logger) {
    // ensure inheritance
    super(logger);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (overriden)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation in <code>subject</code>.
   ** <p>
   ** The {@link Map} <code>origin</code> contains all untouched values and has
   ** it to be aftre this method completes. The {@link Map} <code>subject</code>
   ** contains all transformed values hence tha transformation done here too.
   ** <p>
   ** <b>Attention:</b>
   ** <br>
   ** This transformer is a weak implementation to enuser that a
   ** <code>attributeName</code> which should be transformed has to be contained
   ** in {@link Map} <code>origin</code>. If <code>attributeName</code> isn't
   ** contained in {@link Map} <code>origin</code> no transformation will
   ** happend and therfore no value will be put in {@link Map}
   ** <code>subject</code>.
   ** <b>Note:</b>
   ** <br>
   ** If it has to be ensured that regardless if <code>attributeName</code> is
   ** contained in {@link Map} <code>origin</code> a value is downstreamed to
   ** {@link Map} <code>subject</code> us the {@link NameToOrganizationStrict}
   ** transformation in the configuration.
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  @Override
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    if (origin.containsKey(attributeName)) {
      super.transform(attributeName, origin, subject);
    }
  }
}
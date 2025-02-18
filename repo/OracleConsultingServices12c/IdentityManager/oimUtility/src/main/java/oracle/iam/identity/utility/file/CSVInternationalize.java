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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVInternationalize.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVInternationalize.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import oracle.hst.foundation.utility.Transformer;
import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class CSVInternationalize
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>CSVInternationalize</code> transform a String to an
 ** internationalized version.
 ** <p>
 ** Removes any localized characters in the string string.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class CSVInternationalize implements Transformer<String> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CSVInternationalize</code> attribute transformer
   ** that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
   public CSVInternationalize() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (Transformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** translation.
   ** <br>
   ** If the specified <code>origin</code> is not contained in the dictionary of
   ** this transformer the defaultValue will be returned.
   **
   ** @param  origin             the <code>Object</code> to transform.
   **
   ** @return                    the translation of the specified
   **                            <code>origin</code>.
   **
   ** @throws ClassCastException if the provided <code>Object</code> is
   **                            not castable to a <code>String</code>.
   */
  public Object transform(final Object origin) {
    return StringUtility.replaceAccents(origin.toString());
  }
}
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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   BooleanTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    BooleanTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.transform;

import java.util.Map;

import java.math.BigDecimal;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

////////////////////////////////////////////////////////////////////////////////
// class BooleanTransformer
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>BooleanTransformer</code> transform a numeric value coming from a
 ** source to <code>true</code> if the value is <code>1</code> or
 ** <code>yes</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class BooleanTransformer extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>BooleanTransformer</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   */
  public BooleanTransformer(final Logger logger) {
    // ensure inheritance
    super(logger);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (AttributeTransformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation in <code>subject</code>.
   ** <p>
   ** The {@link Map} <code>origin</code> contains all untouched values and has
   ** it to be after this method completes. The {@link Map} <code>subject</code>
   ** contains all transformed values hence the transformation done here too.
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    BigDecimal value = (BigDecimal)origin.get(attributeName);
    // if we not got a null value put it without transformation in the returning
    // container
    if (value == null)
      // if we not got an empty value return the default
      subject.put(attributeName, Boolean.FALSE);
    else
      subject.put(attributeName, value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE);
  }
}
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

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Enterprise User Security

    File        :   SplitAccount.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SplitAccount.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2011-03-01  DSteding    First release version
*/

package oracle.iam.identity.eus.transformer;

import java.util.List;
import java.util.Map;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

import oracle.iam.identity.foundation.ldap.DirectoryName;

////////////////////////////////////////////////////////////////////////////////
// class SplitAccount
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>SplitAccount</code> transform a String that must be a DN to a RDN
 ** by obtaining the first component from the DN and returns this component.
 ** The attribute type and equals sign are removed from the returned string.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class SplitAccount extends AbstractAttributeTransformer {

  /**
   ** Default value of the Enterprise User Account attribute name in a
   ** <code>Reconciliation Object</code>.
   */
  public static final String ATTRIBUTE_ACCOUNT_NAME   = "Account";

  /**
   ** Default value of the Account Hierarchy attribute name in a
   ** <code>Reconciliation Object</code>.
   */
  public static final String ATTRIBUTE_HIERARCHY_NAME = "Directory Hierarchy";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SplitAccount</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose
   */
  public SplitAccount(final Logger logger) {
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
   ** transformation.
   ** <br>
   ** The {@link Map} <code>origin</code> contains all untouched values. The
   ** {@link Map} <code>subject</code> contains all transformed values
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  @Override
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    Object value = origin.get(attributeName);
    // if we not got a null value put it without transformation in the returning
    // container
    if (value == null) {
      subject.put(ATTRIBUTE_ACCOUNT_NAME,   value);
      subject.put(ATTRIBUTE_HIERARCHY_NAME, value);
    }
    else {
      final String transform = value.toString();
      List<String[]> component  = DirectoryName.explode(transform);
      String[]       identifier = component.remove(0);
      subject.put(ATTRIBUTE_ACCOUNT_NAME,   identifier[1]);
      subject.put(ATTRIBUTE_HIERARCHY_NAME, DirectoryName.compose(component));
    }
    subject.remove(attributeName);
  }
}

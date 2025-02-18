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

    File        :   DN2Name.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DN2Name.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import javax.naming.InvalidNameException;

import javax.naming.ldap.LdapName;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

////////////////////////////////////////////////////////////////////////////////
// class DN2Name
// ~~~~~ ~~~~~~~
/**
 ** The <code>DN2Name</code> transform a String that must be a DN to a RDN by
 ** obtaining the first component from the DN and returns this component.
 ** The attribute type and equals sign are removed from the returned string.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DN2Name extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DN2Name</code> which use the specified {@link Logger}
   ** for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  public DN2Name(final Logger logger) {
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
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    Object value = origin.get(attributeName);
    // if we not got a null value put it without transformation in the returning
    // container
    if (value == null)
      subject.put(attributeName, value);
    else {
      final String transform = value.toString();
      subject.put(attributeName, (StringUtility.isEmpty(transform)) ? transform : transform(transform));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   **
   ** @param  origin             the <code>String</code> to transform.
   **
   ** @return                    the transformation of the specified
   **                            <code>origin</code>; or origin if it is a
   **                            invalid name.
   */
  public String transform(final String origin) {
    try {
      // create a directory name from the specified argument
      // if this will not be possible the cachted exception will return the
      // value as is
      final LdapName name = new LdapName(origin);
      return (String)name.getRdn(name.size() - 1).getValue();
    }
    catch (InvalidNameException e) {
      return origin;
    }
  }
}
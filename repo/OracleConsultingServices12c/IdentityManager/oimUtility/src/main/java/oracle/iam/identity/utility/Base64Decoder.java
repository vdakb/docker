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

    File        :   Base64Decoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Base64Decoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;

import com.thortech.xl.util.Base64;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

////////////////////////////////////////////////////////////////////////////////
// class Base64Decoder
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The <code>Base64Decoder</code> transform a base 64 representation in a
 ** String representation.
 ** <p>
 ** The term Base64 refers to a specific MIME content transfer encoding. It is
 ** also used as a generic term for any similar encoding scheme that encodes
 ** binary data by treating it numerically and translating it into a base 64
 ** representation. The particular choice of base is due to the history of
 ** character set encoding: one can choose a set of 64 characters that is both
 ** part of the subset common to most encodings, and also printable. This
 ** combination leaves the data unlikely to be modified in transit through
 ** systems, such as email, which were traditionally not 8-bit clean.
 ** <p>
 ** MIME Base64 uses A-Z, a-z, and 0-9 for the first 62 digits. There are other
 ** similar systems, usually derived from Base64, that share this property but
 ** differ in the symbols chosen for the last two digits; an example is UTF-7.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 **
 ** @see Base64Encoder
 */
public class Base64Decoder extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Base64Decoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   */
  public Base64Decoder(final Logger logger) {
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
      subject.put(attributeName, (StringUtility.isEmpty(transform)) ? transform : new String(Base64.decode(transform.getBytes())));
    }
  }
}
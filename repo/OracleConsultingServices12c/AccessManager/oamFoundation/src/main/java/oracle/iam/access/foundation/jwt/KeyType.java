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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   KeyType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    KeyType.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

////////////////////////////////////////////////////////////////////////////////
// enum KeyType
// ~~~~ ~~~~~~~
/**
 ** Available Cryptographic Algorithms for Keys as described in
 ** <a href="https://tools.ietf.org/html/rfc7518#section-6.1">RFC 7518 Section 6.1</a>.
 ** <ul>
 **   <li>ES Elliptic Curve [DDS]</li>
 **   <li>RSA as defined by  <a href="https://tools.ietf.org/html/rfc3447">RFC 3447</a></li>
 **   <li>oct: Octet Sequence (used to represent symmetric keys)</li>
 ** </ul>
 ** Currently only the RSA Key Type is implemented and supported in this library.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum KeyType {
  /** RSA as defined by <a href="https://tools.ietf.org/html/rfc3447">RFC 3447</a> */
  RSA
}
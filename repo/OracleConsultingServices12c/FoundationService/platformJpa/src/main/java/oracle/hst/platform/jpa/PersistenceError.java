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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Common Shared Utility

    File        :   PersistenceError.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    PersistenceError.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jpa;

////////////////////////////////////////////////////////////////////////////////
// interface PersistenceError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface PersistenceError {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the default message prefix. */
  static final String PREFIX         = "JPA-";

  // 00001 - 00010 system related errors
  static final String UNHANDLED       = PREFIX + "00001";
  static final String GENERAL         = PREFIX + "00002";
  static final String ABORT           = PREFIX + "00003";
  static final String NOTIMPLEMENTED  = PREFIX + "00004";

  // 00011 - 00020 operational errors
  static final String EXISTS          = PREFIX + "00011";
  static final String NOT_EXISTS      = PREFIX + "00012";
  static final String AMBIGUOUS       = PREFIX + "00013";
  static final String NOT_CREATABLE   = PREFIX + "00014";
  static final String NOT_CREATED     = PREFIX + "00015";
  static final String NOT_DELETABLE   = PREFIX + "00016";
  static final String NOT_DELETED     = PREFIX + "00017";
  static final String NOT_MODIFIABLE  = PREFIX + "00018";
  static final String NOT_MODIFIED    = PREFIX + "00019";

  // 00021 - 00030 attribute validation errors
  static final String NOT_NULL        = PREFIX + "00021";

  // 00031 - 00040 search criteria errors
  static final String CRITERIA_SPEC   = PREFIX + "00031";
  static final String CRITERIA_NESTED = PREFIX + "00031";
}
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
    Subsystem   :   Common Shared Data Access Facilities

    File        :   DataAccessError.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    DataAccessError.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.dataaccess;

////////////////////////////////////////////////////////////////////////////////
// interface DataAccessError
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares global visible resource identifier used for user error information
 ** purpose.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface DataAccessError {

  /** the default message prefix. */
  static final String PREFIX                    = "DAE-";

  static final String ERROR                     = PREFIX + "00000";

  static final String NORESOURCE                = PREFIX + "00001";
  static final String NORESOURCEPROCESS         = PREFIX + "00002";
  static final String NOPROCESSDEFINITION       = PREFIX + "00003";
  static final String NOPROCESSINSTANCE         = PREFIX + "00004";
  static final String NOFORMDEFINITION          = PREFIX + "00005";
  static final String NOFORMACTIVATED           = PREFIX + "00006";
  static final String NOFORMINSTANCE            = PREFIX + "00007";
  static final String NOFORMVERSION             = PREFIX + "00008";

  static final String RESOURCE_AMBIGUOUS        = PREFIX + "00009";
  static final String RESOURCEPROCESS_AMBIGUOUS = PREFIX + "00010";
  static final String FORMVERSION_AMBIGUOUS     = PREFIX + "00011";
}
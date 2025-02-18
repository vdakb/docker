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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   LookupValue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    LookupValue.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface LookupValue
// ~~~~~~~~~ ~~~~~~~~~~~
/**
 ** The <code>Lookup</code> declares the usefull constants to deal with
 ** <code>Lookup Value</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface LookupValue extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for lookups. */
  static final String PREFIX              = "Lookup Definition.Lookup Code Information.";

  /** Status indicating lookup definition entry is enabled. */
  static final String STATUS_ENABLED      = "0";

  /** Status indicating lookup definition entry is disabled. */
  static final String STATUS_DISABLED     = "1";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** code key should be mapped
   */
  static final String FIELD_ENCODED       = "Code Key";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** code key should be mapped
   */
  static final String FIELD_DECODED       = "Decode";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** status should be mapped
   */
  static final String FIELD_DISABLED      = "Disabled";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** language should be mapped
   */
  static final String FIELD_LANGUAGE      = "Language";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** language county should be mapped
   */
  static final String FIELD_COUNTRY       = "Country";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** language variant should be mapped
   */
  static final String FIELD_VARIANT       = "Variant";

  /**
   ** the mapping key contained in a collection to specify that the key value
   ** of the lookup value entry should be resolved.
   */
  static final String KEY                 = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the encoded
   ** value of the lookup value entry should be resolved.
   */
  static final String ENCODED             = PREFIX + FIELD_ENCODED;

  /**
   ** the mapping key contained in a collection to specify that the decoded
   ** value of the lookup value entry should be resolved.
   */
  static final String DECODED             = PREFIX + FIELD_DECODED;

  /**
   ** the mapping key contained in a collection to specify that the status
   ** value of the lookup value entry should be resolved.
   */
  static final String DISABLED            = PREFIX + FIELD_DISABLED;

  /**
   ** the mapping key contained in a collection to specify that the language
   ** value of the lookup value entry should be resolved.
   */
  static final String LANGUAGE            = PREFIX + FIELD_LANGUAGE;

  /**
   ** the mapping key contained in a collection to specify that the language
   ** country value of the lookup value entry should be resolved.
   */
  static final String COUNTRY             = PREFIX + FIELD_COUNTRY;

  /**
   ** the mapping key contained in a collection to specify that the language
   ** variant value of the lookup value entry should be resolved.
   */
  static final String VARIANT             = PREFIX + FIELD_VARIANT;

  /**
   ** the mapping key contained in a collection to specify that the note value
   ** of the lookup value entry should be resolved.
   */
  static final String NOTE                = PREFIX + FIELD_NOTE;
}
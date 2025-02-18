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

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.naming;

////////////////////////////////////////////////////////////////////////////////
// interface Lookup
// ~~~~~~~~~ ~~~~~~
/**
 ** The <code>Lookup</code> declares the usefull constants to deal with
 ** <code>Lookup Definition</code>s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface Lookup extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Standard prefix name for lookups. */
  static final String PREFIX              = "Lookup Definition.";

  /** Status indicating lookup definition entry is enabled. */
  static final String STATUS_ENABLED      = "0";

  /** Status indicating lookup definition entry is disabled. */
  static final String STATUS_DISABLED     = "1";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup code key should be mapped
   */
  static final String FIELD_CODE          = "Code";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup type should be mapped
   */
  static final String FIELD_TYPE          = "Type";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup required property should be mapped
   */
  static final String FIELD_REQUIRED      = "Required";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup group key should be mapped
   */
  static final String FIELD_GROUP         = "Group";

  /**
   ** the reconciliation key contained in a collection to specify that the
   ** lookup field key should be mapped
   */
  static final String FIELD_FIELD         = "Field";

  /**
   ** the mapping key contained in a collection to specify that the lookup key
   ** value of the lookup definition should be resolved
   */
  static final String KEY                 = PREFIX + FIELD_KEY;

  /**
   ** the mapping key contained in a collection to specify that the lookup code
   ** key value of the lookup definition should be resolved
   */
  static final String CODE                = PREFIX + FIELD_CODE;

  /**
   ** the mapping key contained in a collection to specify that the lookup type
   ** value of the lookup definition should be resolved
   */
  static final String TYPE                = PREFIX + FIELD_TYPE;

  /**
   ** the mapping key contained in a collection to specify that the lookup
   ** required property of the lookup definition should be resolved
   */
  static final String REQUIRED            = PREFIX + FIELD_REQUIRED;

  /**
   ** the mapping key contained in a collection to specify that the lookup group
   ** key value of the lookup definition should be resolved
   */
  static final String GROUP               = PREFIX + FIELD_GROUP;

  /**
   ** the mapping key contained in a collection to specify that the lookup field
   ** key value of the lookup definition should be resolved
   */
  static final String FIELD               = PREFIX + FIELD_FIELD;

  /**
   ** the mapping key contained in a collection to specify that the lookup group
   ** key value of the lookup definition should be resolved
   */
  static final String NOTE                = PREFIX + FIELD_NOTE;

  /**
   ** The <code>Lookup Definition</code> that defines the possible types of
   ** <code>Organization</code>s.
   */
  static final String ORGANIZATION_TYPE   = "Lookup.Organization.Type";

  /**
   ** The <code>Lookup Definition</code> that defines the possible status of
   ** <code>Organization Profile</code>s.
   */
  static final String ORGANIZATION_STATUS = "Lookup.WebClient.Organizations.Status";

  /**
   ** The <code>Lookup Definition</code> that defines the possible role of
   ** <code>User Profile</code>s.
   */
  static final String IDENTITY_ROLE       = "Lookup.Users.Role";

  /**
   ** The <code>Lookup Definition</code> that defines the possible type of
   ** <code>User Profile</code>s.
   */
  static final String IDENTITY_TYPE       = "Lookup.Users.XellerateType";

  /**
   ** The <code>Lookup Definition</code> that defines the possible status of
   ** <code>User Profile</code>s.
   */
  static final String IDENTITY_STATUS     = "Lookup.WebClient.Users.Status";
}
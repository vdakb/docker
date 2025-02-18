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

    File        :   NamespaceValue.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NamespaceValue.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.persistence.dialect;

import oracle.iam.identity.foundation.persistence.DatabaseEntity;

////////////////////////////////////////////////////////////////////////////////
// class NamespaceValue
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>NamespaceValue</code> declares the usefull constants to deal with
 ** <code>Lookup Value</code>s in Oracle Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class NamespaceValue extends DatabaseEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the entity to use. */
  public static final String ENTITY   = "lkv";

  /** the attribute name of the system identifier of an entry in the entity. */
  public static final String PRIMARY  = "lkv_key";

  /** the attribute name of the unique identifier of an entry in the entity. */
  public static final String FOREIGIN = "lku_key";

  /** the attribute name of the encoded value of an entry in the entity. */
  public static final String ENCODED  = "lkv_encoded";

  /** the attribute name of the decoded value of an entry in the entity. */
  public static final String DECODED  = "lkv_decoded";

  /** the attribute name of the create timestamp of an entry in the entity. */
  public static final String CREATE   = "lkv_created";

  /** the attribute name of the modify timestamp of an entry in the entity. */
  public static final String MODIFY   = "lkv_modified";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>NamespaceValue</code> with the specified
   ** properties.
   */
  protected NamespaceValue() {
    // ensure inheritance
    super(null, ENTITY, PRIMARY);
  }
}
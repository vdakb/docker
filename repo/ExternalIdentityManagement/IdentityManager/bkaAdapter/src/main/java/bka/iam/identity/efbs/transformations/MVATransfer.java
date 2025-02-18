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

    Copyright 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Federated Identity Management
    Subsystem   :   eFBS SCIM Transformation

    File        :   MVATransfer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    MVATransfer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.efbs.transformations;

import java.util.ArrayList;
import java.util.HashMap;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class MVATransfer
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>MVATransfer</code> obtains the values from a multi-valued
 ** attribute provided by the eFBS Target System and synchronize the single
 ** value accordingly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class MVATransfer {

  //////////////////////////////////////////////////////////////////////////////
  // statis final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the reconciliation type that's target of this transformer */
  static final String TYPE_PHONE = "phones";

  /** the name of the reconciliation type that's target of this transformer */
  static final String TYPE_EMAIL = "emails";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String type;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>MVATransfer</code> adpater that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  MVATransfer(final String type) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type  = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Method for transforming the attributes.
   ** <br>
   ** Incoming identity and entitlements are from the target system.
   ** <p>
   ** The framework in actual facts is a stupid piece of code.
   ** <br>
   ** So what really happened is instead of putting the transformed value in the
   ** map of atributes to reconcile, the frameork checks after this transformer
   ** returns if the mapping contains the given attribute name as a key.
   ** Only if this is the case the framework will map the transformed value at
   ** the given attribute name
   ** <p>
   ** Ridiculous!
   **
   ** @param  account            the {@link HashMap} containing account data
   **                            details.
   ** @param  entitlement        the {@link HashMap} containing child data
   **                            details.
   ** @param  attribute          the name of reconciliation attribute being
   **                            transformed.
   **
   ** @return                    the new value for reconciliation field being
   **                            operated on.
   */
  @SuppressWarnings("unused")
  public Object transform(final HashMap<String,Object> account, final HashMap<String, ArrayList<HashMap<String, String>>> entitlement, final String attribute) {
    String value = null;
    final ArrayList<HashMap<String, String>> values = entitlement.get(this.type);
    if (!CollectionUtility.empty(values)) {
      // we are expecting only one
      // if more are provided ignore them
      final HashMap<String, String> record = values.get(0);
      value = record.get(attribute);
      account.put(attribute, value);
    }
    return value;
  }
}
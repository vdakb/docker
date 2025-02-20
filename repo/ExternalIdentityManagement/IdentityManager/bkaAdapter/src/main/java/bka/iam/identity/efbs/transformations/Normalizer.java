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

    File        :   Normalizer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    Normalizer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.efbs.transformations;

import java.util.HashMap;
import java.util.ArrayList;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Normalizer
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>Normalizer</code> generate a name an account feeded from an eFBS
 ** Target System by:
 ** <ol>
 **   <li>obtain the user name from the account data provided by the
 **       data source.
 **   <li>remove the prefix <code>GED</code> from the user name if there is any.
 **   <li>remove the prefix <code>gfa</code> from the user name if there is any..
 **   <li>remove the prefix <code>gsu</code> from the user name if there is any..
 **   <li>remove the prefix <code>su</code> from the user name if there is any..
 **   <li>remove the prefix <code>fa</code> from the user name if there is any..
 ** </ol>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Normalizer {

  //////////////////////////////////////////////////////////////////////////////
  // statis final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the reconciliation field that's target of this transformer */
  static final String SRC = "userName";

  /** the prefix of an <code>GED</code> account */
  static final String GED = "ged";

  /** the suffix of an <code>Global Super User</code> account */
  static final String GSU = "gsu";

  /** the suffix of an <code>Global Admin User</code> account */
  static final String GFA = "gfa";

  /** the suffix of an <code>Super User</code> account */
  static final String SU  = "su";

  /** the suffix of an <code>Admin User</code> account */
  static final String FA  = "fa";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Normalizer</code> adpater that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Normalizer() {
    // ensure inheritance
    super();
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
    String value = (String)account.get(SRC);
    if (StringUtility.startsWithIgnoreCase(value, GED)) {
      value = value.substring(GED.length());
    }
    else if (StringUtility.endsWithIgnoreCase(value, GFA)) {
      value = value.substring(0, value.length() - GFA.length());
    }
    else if (StringUtility.endsWithIgnoreCase(value, GSU)) {
      value = value.substring(0, value.length() - GSU.length());
    }
    else if (StringUtility.endsWithIgnoreCase(value, SU)) {
      value = value.substring(0, value.length() - SU.length());
    }
    else if (StringUtility.endsWithIgnoreCase(value, FA)) {
      value = value.substring(0, value.length() - FA.length());
    }
    // its a shame that weed need to to it in such a way
    account.put(attribute, value);
    return value;
  }
}
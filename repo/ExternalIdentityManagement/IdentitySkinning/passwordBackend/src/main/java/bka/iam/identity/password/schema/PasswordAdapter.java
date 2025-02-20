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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Password Reset

    File        :   PasswordAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PasswordAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.password.schema;

import oracle.iam.ui.platform.model.common.ModelAttr;
import oracle.iam.ui.platform.model.common.StaticModelAdapterBean;

import oracle.jbo.Row;

////////////////////////////////////////////////////////////////////////////////
// abstract class PasswordAdapter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Password</code> customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class PasswordAdapter extends StaticModelAdapterBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @ModelAttr
  private String user;
  @ModelAttr
  private String password;
  @ModelAttr
  private String confirmed;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccountPasswordAdapter</code> value object that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  PasswordAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PasswordAdapter</code> value object which use the
   ** specified {@link Row} to populate its value.
   **
   ** @param  row                the {@link Row} to populate the values from.
   */
  PasswordAdapter(final Row row) {
    // ensure inheritance
    super(row);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setUser
  /**
   ** Sets the value of the user property.
   **
   ** @param  value              the value of the user property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setUser(final String value) {
    this.user = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getUser
  /**
   ** Returns the value of the password property.
   **
   ** @return                    the value of the password property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getUser() {
    return this.user;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setPassword
  /**
   ** Sets the value of the password property.
   **
   ** @param  value              the value of the password property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setPassword(final String value) {
    this.password = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getPassword
  /**
   ** Returns the value of the password property.
   **
   ** @return                    the value of the password property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getPassword() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setConfirmed
  /**
   ** Sets the value of the confirmed property.
   **
   ** @param  value              the value of the confirmed property.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setConfirmed(final String value) {
    this.confirmed = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getConfirmed
  /**
   ** Returns the value of the confirmed property.
   **
   ** @return                    the value of the confirmed property.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getConfirmed() {
    return this.confirmed;
  }
}
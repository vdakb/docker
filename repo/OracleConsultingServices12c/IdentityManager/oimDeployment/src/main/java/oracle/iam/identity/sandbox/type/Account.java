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

    System      :   Identity Manager Library
    Subsystem   :   Sandbox Service Utilities 11g

    File        :   Account.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Account.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.sandbox.type;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.common.spi.FormInstance;

////////////////////////////////////////////////////////////////////////////////
// class Account
// ~~~~~ ~~~~~~~
/**
 ** <code>Account</code> defines the accumulated attribute that can
 ** be passed as a nested parameter used for account form information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Account extends Form {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Account</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Account() {
    // ensure inheritance
    super(new FormInstance.Account());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delegate (overridden)
  /**
   ** Returns the {@link FormInstance.Account} this ANT type wrappes.
   **
   ** @return                    the {@link FormInstance.Account} this ANT type
   **                            wrappes.
   */
  @Override
  public FormInstance.Account delegate() {
    return (FormInstance.Account)this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredForm
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Form}.
   **
   ** @param  instance           the {@link Form} instance to take in account
   **                            during generation.
   **
   ** @throws BuildException     if the {@link Form} already contained in
   **                            the collection of this generation operation.
   */
  public void addConfiguredForm(final Form instance)
    throws BuildException {

    delegate().add(instance.delegate());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPanel
  /**
   ** Call by the ANT deployment to inject the argument for adding an
   ** {@link Panel}.
   **
   ** @param  instance           the {@link Panel} instance to take in account
   **                            during generation.
   **
   ** @throws BuildException     if the {@link Panel} already contained in
   **                            the collection of this generation operation.
   */
  public void addConfiguredPanel(final Panel instance)
    throws BuildException {
    
    final FormInstance.Account account = (FormInstance.Account)this.delegate;
    account.add(instance.delegate);
    for (Attribute cursor : instance.attribute) {
      // add the attribute to the global collection of attributes
      // this will raise a BuildException if the attribute is enlisted more than
      // once
      account.add(instance.delegate, cursor.delegate());
    }
  }
}
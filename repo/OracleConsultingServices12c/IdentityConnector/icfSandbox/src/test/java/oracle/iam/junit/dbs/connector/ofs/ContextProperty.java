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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Openfire Database Connector

    File        :   ContextProperty.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ContextProperty.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.connector.ofs;

import org.junit.Test;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.openfire.schema.SystemProperty;

////////////////////////////////////////////////////////////////////////////////
// class ContextProperty
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The test case to fetch properties leveraging the database connection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ContextProperty extends Transaction {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ContextProperty</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ContextProperty() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   xmppDomain
  /**
   ** Test that the system domain property is fetched.
   */
  @Test
  public void xmppDomain() {
    try {
      final SystemProperty property = this.context.systemProperty("xmpp.domain");
      notNull(property);
      equals("hardy.vm.oracle.com", property.value());
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordKey
  /**
   ** Test that the system password key property is fetched.
   */
  @Test
  public void passwordKey() {
    try {
      final SystemProperty property = this.context.systemProperty("xmpp.domain");
      notNull(property);
      equals("hardy.vm.oracle.com", property.value());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}
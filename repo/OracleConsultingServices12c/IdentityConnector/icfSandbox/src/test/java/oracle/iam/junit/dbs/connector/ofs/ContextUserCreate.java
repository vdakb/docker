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

    System      :   Oracle Identity Manager UnitTest
    Subsystem   :   Openfire Database Connector

    File        :   ContextUserCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ContextUserCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.connector.ofs;

import java.util.Set;

import org.junit.Test;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.connector.service.AttributeFactory;

import oracle.iam.identity.icf.connector.openfire.schema.Marshaller;

////////////////////////////////////////////////////////////////////////////////
// class ContextUserCreate
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The test case belonging to create a user leveraging the the
 ** <code>Database Server</code> JDBC driver.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ContextUserCreate extends Transaction {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Set<Attribute> attribute = AttributeFactory.set(
    new String[]{
        "__NAME__"
      , "__PASSWORD__"
      , "name"
      , "email"
    }
    , new Object[]{
        "BE0815125"
      , new GuardedString("Welcome1".toCharArray())
      , "Sophie Strecke"
      , "0815125@polizei-be.de"
    }
  );

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ContextUserCreate</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ContextUserCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSophie
  /**
   ** Test that an account could be created.
   */
  @Test
  public void createSophie() {
    try {
      this.context.userCreate(Marshaller.inboundUser(attribute));
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}
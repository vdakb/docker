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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   TestJabberIdentifier.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TestJabberIdentifier.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

////////////////////////////////////////////////////////////////////////////////
// class TestJabberIdentifier
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Test cases for the JabberIdentifier class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestJabberIdentifier {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TestJabberIdentifier</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestJabberIdentifier() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testDomain
	@Test
  public void testDomain() {
    JabberIdentifier.build("mycomapny.com");
    JabberIdentifier.build("wfink-adm");
    boolean failed = false;
    try {
      JabberIdentifier.build("wfink adm");
    }
    catch (Exception e) {
      failed = true;
    }
    assertTrue("A domain with spaces was accepted", failed);
    failed = false;
    try {
      JabberIdentifier.build("wfink_adm");
    }
    catch (Exception e) {
      failed = true;
    }
    assertTrue("A domain with _ was accepted", failed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testNode
	@Test
  public void testNode() {
    JabberIdentifier.build("john@example.com");
    JabberIdentifier.build("john_paul@example.com");
    JabberIdentifier.build("john-paul@example.com");
    boolean failed = false;
    try {
      JabberIdentifier.build("john paul@mycomapny.com");
    }
    catch (Exception e) {
      failed = true;
    }
    assertTrue("A username with spaces was accepted", failed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testCompare
	@Test
  public void testCompare() {
    JabberIdentifier jid1 = JabberIdentifier.build("john@example.com");
    JabberIdentifier jid2 = JabberIdentifier.build("john@example.com");
    assertEquals("Failed to compare 2 similar JIDs", 0,    jid1.compareTo(jid2));
    assertEquals("Failed to recognize equal JIDs",   jid1, jid2);
    
    jid2 = JabberIdentifier.build("example.com");
    assertTrue("Failed to recognized bigger JID",     jid1.compareTo(jid2) > 0);
    assertFalse("Failed to recognize different JIDs", jid1.equals(jid2));

    jid2 = JabberIdentifier.build("example.com/mobile");
    assertTrue("Failed to recognized bigger JID",     jid1.compareTo(jid2) > 0);
    assertFalse("Failed to recognize different JIDs", jid1.equals(jid2));

    jid2 = JabberIdentifier.build("john@example.com/mobile");
    assertTrue("Failed to recognized bigger JID",     jid1.compareTo(jid2) < 0);
    assertFalse("Failed to recognize different JIDs", jid1.equals(jid2));
  }
}
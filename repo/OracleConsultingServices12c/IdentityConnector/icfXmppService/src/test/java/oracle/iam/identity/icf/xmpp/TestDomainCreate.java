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

    File        :   TestDomainCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TestDomainCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import org.junit.Test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

////////////////////////////////////////////////////////////////////////////////
// class TestDomainCreate
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Tests compliance of {@link JabberIdentifier} with the restrictions defined
 ** in RFC-3920 for the domain identifier.
 **
 ** @see <a href="http://www.ietf.org/rfc/rfc3920.txt">RFC 3920 - Extensible Messaging and Presence Protocol (XMPP): Core</a>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestDomainCreate {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** A node identifier that's RFC 3920 valid. */
  public static final String NODE     = "node";
  /** A domain identifier that's RFC 3920 valid. */
  public static final String DOMAIN   = "domain";
  /** A resource identifier that's RFC 3920 valid. */
  public static final String RESOURCE = "resource";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TestDomainCreate</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestDomainCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testOptionality
	/**
   ** Domain identifiers are a required part of a JID. This testcase verifies
   ** that node identifiers can be left out of the creation of a JID.
   */
  @Test
  public void testOptionality()
    throws Exception {
    try {
      JabberIdentifier.build(null);
      fail("Domain identifiers should be a required part of a JID. No exception occurred while trying to leave out a domain identifier");
    }
    catch (IllegalArgumentException e) {
      // intentionally left blank
      ;
    }
    catch (NullPointerException e) {
      // intentionally left blank
      ;
    }

    try {
      JabberIdentifier.build(null, null, null);
      fail("Domain identifiers should be a required part of a JID. No exception occurred while trying to leave out a domain identifier");
    }
    catch (IllegalArgumentException e) {
      // intentionally left blank
      ;
    }
    catch (NullPointerException e) {
      // intentionally left blank
      ;
    }

    try {
      JabberIdentifier.build(NODE, null, null);
      fail("Domain identifiers should be a required part of a JID. No exception occurred while trying to leave out a domain identifier");
    }
    catch (IllegalArgumentException e) {
      // intentionally left blank
      ;
    }
    catch (NullPointerException e) {
      // intentionally left blank
      ;
    }

    try {
      JabberIdentifier.build(null, null, RESOURCE);
      fail("Domain identifiers should be a required part of a JID. No exception occurred while trying to leave out a domain identifier");
    }
    catch (IllegalArgumentException ex) {
      // intentionally left blank
      ;
    }
    catch (NullPointerException ex) {
      // intentionally left blank
      ;
    }

    try {
      JabberIdentifier.build(NODE, null, RESOURCE);
      fail("Domain identifiers should be a required part of a JID. No exception occurred while trying to leave out a domain identifier");
    }
    catch (IllegalArgumentException ex) {
      // intentionally left blank
      ;
    }
    catch (NullPointerException ex) {
      // intentionally left blank
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testMaximumSize
	/**
   ** The maximum size of the domain identifier is 1023 bytes (note: bytes, not
   ** characters!). This test verifies that using as much characters as possible
   ** without crossing the 1023 byte boundry, will not cause a problem.
   */
  @Test
  public void testMaximumSize()
    throws Exception {

    // setup
    final StringBuilder builder = new StringBuilder("a");
    for (int i = 0; i < 511; i++) {
      builder.append(".a");
    }
    final String big = builder.toString();
    // do magic / verify
    JabberIdentifier.build(NODE, big, RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testOverMaximumSize
	/**
	 ** The maximum size of the domain identifier is 1023 bytes (note: bytes, not
	 ** characters!). This test verifies that using more bytes will cause an
	 ** exception to be thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testOverMaximumSize()
    throws Exception {

		// setup
		final StringBuilder builder = new StringBuilder("a");
		for (int i = 0; i < 512; i++) {
			builder.append(".a");
		}
		final String big = builder.toString();

		// do magic / verify
		JabberIdentifier.build(NODE, big, RESOURCE);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBare
	/**
	 ** Verifies that the bare representation of a JID that contains a domain
   * name only corresponds to the domain name itself.
   */
  @Test
  public void testBare()
    throws Exception {
    
    // setup
    final JabberIdentifier fullJID = JabberIdentifier.build(null, DOMAIN, null);
    // do magic
    final String bareJID = fullJID.bare();
    // verify
    assertEquals(DOMAIN, bareJID);
  }
}

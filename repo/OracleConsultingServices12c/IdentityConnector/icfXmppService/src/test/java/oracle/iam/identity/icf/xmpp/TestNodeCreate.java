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

    File        :   TestNodeCreate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TestNodeCreate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

////////////////////////////////////////////////////////////////////////////////
// class TestNodeCreate
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Tests compliance of {@link JID} with the restrictions defined in RFC-3920
 ** for the node identifier.
 **
 ** @see <a href="http://www.ietf.org/rfc/rfc3920.txt">RFC 3920 - Extensible Messaging and Presence Protocol (XMPP): Core</a>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestNodeCreate {

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
   ** Constructs an empty <code>TestNodeCreate</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestNodeCreate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testOptionality
  /**
   ** Node identifiers are an optional part of a JID. This testcase verifies
   ** that node identifiers can be left out of the creation of a JID.
   */
  @Test
  public void testOptionality()
    throws Exception {

    assertEquals(DOMAIN,                  JabberIdentifier.build(DOMAIN).toString());
    assertEquals(DOMAIN,                  JabberIdentifier.build(null, DOMAIN, null).toString());
    assertEquals(DOMAIN + '/' + RESOURCE, JabberIdentifier.build(null, DOMAIN, RESOURCE).toString());
    assertEquals(DOMAIN + '/' + RESOURCE, JabberIdentifier.build("",   DOMAIN, RESOURCE).toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testMaximumSizeOneByteChar
  /**
   ** The maximum size of the node identifier is 1023 bytes (note: bytes, not
	 ** characters!). This test verifies that using as much characters as
	 ** possible without crossing the 1023 byte boundry, will not cause a
	 ** problem.
	 */
	@Test
	public void testMaximumSizeOneByteChar()
    throws Exception {

		// setup
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 1023; i++) {
			builder.append('a');
		}
		final String longestPossibleValue = builder.toString();
		// do magic / verify
		JabberIdentifier.build(longestPossibleValue, DOMAIN, RESOURCE);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testOverMaximumSizeOneByteChar
  /**
   ** The maximum size of the node identifier is 1023 bytes (note: bytes, not
   ** characters!). This test verifies that using more bytes will cause an
   ** exception to be thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOverMaximumSizeOneByteChar()
    throws Exception {

    // setup
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < 1024; i++) {
      builder.append('a');
    }
    builder.append('a');
    final String big = builder.toString();
    // do magic / verify
    JabberIdentifier.build(big, DOMAIN, RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testMaximumSizeWithThreeByteChars
  /**
   ** UTF-8 characters use 1 to 4 bytes. The JID implementation should correctly
   ** identify the length of all UTF-8 characters.
	 */
	@Test
	public void testMaximumSizeWithThreeByteChars()
    throws Exception {
		// "\u20AC", is a one character, three byte unicode char.

		// setup
		final String three = "\u20AC";
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 341; i++) {
			// 1023 / 3 = 341
			builder.append(three);
		}
		final String longestPossibleValue = builder.toString();
		// do magic / verify
		JabberIdentifier.build(longestPossibleValue, DOMAIN, RESOURCE);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testOverMaximumSizeWithThreeByteChars
  /**
   ** This test verifies that strings longer than 1023 characters are not
   ** accepted, if UTF-8 characters are used that are represented with 3 bytes.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testOverMaximumSizeWithThreeByteChars()
    throws Exception {

    // "\u20AC", is a one character, three byte unicode char.

    // setup
    final String        three = "\u20AC";
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < 342; i++) {
      // 1023 / 3 = 341
      builder.append(three);
    }
    final String longestPossibleValue = builder.toString();
    // do magic / verify
    JabberIdentifier.build(longestPossibleValue, DOMAIN, RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testBare
  /**
   ** Verifies that the bare representation of a JID that contains a node and a
   ** domain name corresponds to the node and domain name combination.
   */
  @Test
  public void testBare()
    throws Exception {

    // setup
    final JabberIdentifier bare = JabberIdentifier.build(NODE, DOMAIN, null);
    // do magic
    final String bareJID = bare.bare();
    // verify
    assertEquals(NODE + '@' + DOMAIN, bareJID);
  }
}

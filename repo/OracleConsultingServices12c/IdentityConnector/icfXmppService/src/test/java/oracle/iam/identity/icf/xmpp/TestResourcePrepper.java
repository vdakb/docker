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

    File        :   TestResourcePrepper.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TestResourcePrepper.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

////////////////////////////////////////////////////////////////////////////////
// class TestResourcePrepper
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Verifies {@link JabberIdentifier#resourcePrepper(String)}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestResourcePrepper {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TestResourcePrepper</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestResourcePrepper() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testValidString
  /**
   ** Basic test that verifies that a string that shouldn't be modified by
   ** resource-prepping gets prepped without a problem.
   */
  @Test
  public void testValidString()
    throws Exception {

    // setup
    final String resource = "resource";
    // do magic
    final String result = JabberIdentifier.resourcePrepper(resource);
    // verify
    assertEquals(resource, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testValidString
  /**
   ** Checks that resource-prepping is case sensitive.
	 */
	@Test
	public void testCaseSensitivity()
    throws Exception {

		// setup
		final String resource = "rEsOuRcE";
		// do magic
		final String result = JabberIdentifier.resourcePrepper(resource);
		// verify
		assertEquals(resource, result);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testToLong
  /**
   ** Verifies that an input value bigger than 1023 bytes will cause an
   ** exception to be thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testToLong()
    throws Exception {

		// setup
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 1024; i++) {
			builder.append('a');
		}
		builder.append('a');
		final String big = builder.toString();
		// do magic / verify
		JabberIdentifier.resourcePrepper(big);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testMapping
  /**
   ** Verifies that Stringprep mapping is correctly executed. This test uses a
   *' 'word joiner' character, which is listed on the B1 table of Stringprep.
   ** Characters on this table must be mapped in resource strings, according to
   ** RFC 3920. This specific character should be mapped to nothing.
   */
  @Test
  public void testMapping()
    throws Exception {
    // setup;
    final String input = "word\u2060joiner";
    // do magic
    final String result = JabberIdentifier.resourcePrepper(input);
    // verify
    assertEquals("wordjoiner", result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testCachedResult
  /**
   ** Checks cache usage, by making sure that a subsequent request returns the
   ** stringprepped answer, not the input data. Input data often equals the
   ** prepped answer, which allows a bug like this to slip by easily.
   */
  @Test
  public void testCachedResult()
    throws Exception {

    // setup;
    final String input = "bword\u2060joiner";
    // do magic
    final String result1 = JabberIdentifier.resourcePrepper(input);
    final String result2 = JabberIdentifier.resourcePrepper(input);
    // verify
    assertEquals("bwordjoiner", result1);
    assertEquals(result1, result2);
  }
}

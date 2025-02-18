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

    File        :   TestNodeEscaping.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TestNodeEscaping.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

////////////////////////////////////////////////////////////////////////////////
// class TestNodeEscaping
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Tests that verify the JID escape and unescape functionality, as specified by
 ** XEP-0106.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestNodeEscaping {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TestNodeEscaping</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestNodeEscaping() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample01
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
	 ** the first JID presented in table 3 of XEP-0106 and comparing the result
	 ** with the expected value presented in that table.
	 */
	@Test
	public void testEscapeExample01()
    throws Exception {

		assertEquals(JabberIdentifier.escape("space cadet"), "space\\20cadet");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample02
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the second JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample02()
    throws Exception {

    assertEquals(JabberIdentifier.escape("call me \"ishmael\""), "call\\20me\\20\\22ishmael\\22");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample03
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the third JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample03()
    throws Exception {

    assertEquals(JabberIdentifier.escape("at&t guy"), "at\\26t\\20guy");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample04
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the fourth JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample04()
    throws Exception {

    assertEquals(JabberIdentifier.escape("d'artagnan"), "d\\27artagnan");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample05
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the fifth JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample05()
    throws Exception {

    assertEquals(JabberIdentifier.escape("/.fanboy"), "\\2f.fanboy");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample06
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the sixth JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample06()
    throws Exception {

    assertEquals(JabberIdentifier.escape("::foo::"), "\\3a\\3afoo\\3a\\3a");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample07
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the seventh JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample07()
    throws Exception {

    assertEquals(JabberIdentifier.escape("<foo>"), "\\3cfoo\\3e");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample08
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the eight JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample08()
    throws Exception {

    assertEquals(JabberIdentifier.escape("user@host"), "user\\40host");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample09
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the ninth JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample09()
    throws Exception {

    assertEquals(JabberIdentifier.escape("c:\\net"), "c\\3a\\net");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample10
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the tenth JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample10()
    throws Exception {

    assertEquals(JabberIdentifier.escape("c:\\\\net"), "c\\3a\\\\net");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample11
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the eleventh JID presented in table 3 of XEP-0106 and comparing the
   ** result with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample11()
    throws Exception {

    assertEquals(JabberIdentifier.escape("c:\\cool stuff"), "c\\3a\\cool\\20stuff");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEscapeExample12
	/**
	 ** Verifies correct behavior of 'JID node escaping' by escaping the node of
   ** the twelfth JID presented in table 3 of XEP-0106 and comparing the result
   ** with the expected value presented in that table.
   */
  @Test
  public void testEscapeExample12()
    throws Exception {

    assertEquals(JabberIdentifier.escape("c:\\5commas"), "c\\3a\\5c5commas");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample01
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the first JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample01()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("space\\20cadet"), "space cadet");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample02
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the second JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample02()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("call\\20me\\20\\22ishmael\\22"), "call me \"ishmael\"");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample03
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the third JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample03()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("at\\26t\\20guy"), "at&t guy");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample04
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the fourth JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample04()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("d\\27artagnan"), "d'artagnan");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample05
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the fifth JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample05()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("\\2f.fanboy"), "/.fanboy");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample06
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the sixth JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample06()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("\\3a\\3afoo\\3a\\3a"), "::foo::");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample07
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the seventh JID presented in table 3 of XEP-0106
   ** and comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample07()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("\\3cfoo\\3e"), "<foo>");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample08
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the eight JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample08()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("user\\40host"), "user@host");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample09
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the ninth JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample09()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("c\\3a\\5cnet"), "c:\\net");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample10
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the tenth JID presented in table 3 of XEP-0106 and
   ** comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample10()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("c\\3a\\5c\\5cnet"), "c:\\\\net");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample11
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the eleventh JID presented in table 3 of XEP-0106
   ** and comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample11()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("c\\3a\\5ccool\\20stuff"), "c:\\cool stuff");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnescapeExample12
	/**
	 ** Verifies correct behavior of 'JID node escaping' by unescaping the
   ** escaped node value of the twelfth JID presented in table 3 of XEP-0106
   ** and comparing the result with the expected value presented in that table.
   */
  @Test
  public void testUnescapeExample12()
    throws Exception {

    assertEquals(JabberIdentifier.unescape("c\\3a\\5c5commas"), "c:\\5commas");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testPartialEscapeException
	/**
	 ** Verifies that the exceptions as listed in XEP-0106, paragraph 4.3, get
   ** processed as expected.
   */
  @Test
  public void testPartialEscapeException()
    throws Exception {

    assertEquals("\\2plus\\2is\\4", JabberIdentifier.escape("\\2plus\\2is\\4"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testPartialUnescapeException
	/**
	 ** Verifies that the exceptions as listed in XEP-0106, paragraph 4.3, get
   ** processed as expected.
   */
  @Test
  public void testPartialUnescapeException()
    throws Exception {

    assertEquals("\\2plus\\2is\\4", JabberIdentifier.unescape("\\2plus\\2is\\4"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testInvalidEscapeSequence1Exception
	/**
	 ** Verifies that the exceptions as listed in XEP-0106, paragraph 4.3, get
   ** processed as expected.
   */
  @Test
  public void testInvalidEscapeSequence1Exception()
    throws Exception {

    assertEquals("foo\\bar", JabberIdentifier.escape("foo\\bar"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testInvalidUnescapeSequence1Exception
	/**
	 ** Verifies that the exceptions as listed in XEP-0106, paragraph 4.3, get
   ** processed as expected.
   */
  @Test
  public void testInvalidUnescapeSequence1Exception()
    throws Exception {

    assertEquals("foo\\bar", JabberIdentifier.unescape("foo\\bar"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testInvalidEscapeSequence2Exception
	/**
	 ** Verifies that the exceptions as listed in XEP-0106, paragraph 4.3, get
   ** processed as expected.
   */
  @Test
  public void testInvalidEscapeSequence2Exception()
    throws Exception {

    assertEquals("foob\\41r", JabberIdentifier.escape("foob\\41r"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testInvalidUnescapeSequence2Exception
	/**
	 ** Verifies that the exceptions as listed in XEP-0106, paragraph 4.3, get
   ** processed as expected.
   */
  @Test
  public void testInvalidUnescapeSequence2Exception()
    throws Exception {

    assertEquals("foob\\41r", JabberIdentifier.unescape("foob\\41r"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEdgeCaseEscapingSlash
	/**
	 ** Verifies that the escaping code works if the characters that are
   ** (potentially) involved are either the first or last character in a
   ** string.
   ** <p>
   ** This test verifies that the character '\' does not cause any problems when
   ** used in either the start or at the end of a string that is being
   ** processed. The '\' character is the character used to escape, making it a
   ** likely cause for programming logic errors that this test attempts to
   ** expose.
   */
  @Test
  public void testEdgeCaseEscapingSlash()
    throws Exception {

    assertEquals("\\", JabberIdentifier.escape("\\"));
    assertEquals("\\", JabberIdentifier.unescape("\\"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEdgeCaseGreaterThan
	/**
	 ** Verifies that the escaping code works if the characters that are
   ** (potentially) involved are either the first or last character in a
   ** string.
   ** <p>
   ** This test verifies that the character '>' does not cause any problems
   ** when used in either the start or at the end of a string that is being
   ** processed. The '>' character is one of the character that are to be
   ** escaped, making it a likely cause for programming logic errors that this
   ** test attempts to expose.
   */
  @Test
  public void testEdgeCaseGreaterThan()
    throws Exception {

    assertEquals("\\3e", JabberIdentifier.escape(">"));
    assertEquals(">", JabberIdentifier.unescape("\\3e"));
  }
}
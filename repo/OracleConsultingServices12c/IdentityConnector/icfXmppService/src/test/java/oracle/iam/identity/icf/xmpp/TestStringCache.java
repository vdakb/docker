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

    File        :   TestStringCache.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TestStringCache.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import org.junit.Test;

////////////////////////////////////////////////////////////////////////////////
// class TestStringCache
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A testcase that verifies the effect of stringprep caching in the JID class.
 ** <p>
 ** <b>Implementation Note</b>:
 ** Do not re-use the same values in different tests. As we have no control over
 ** the JID cache, we might end up testing against a cached value of the cache
 ** that's being tested by this JUnit testcase.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestStringCache {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>TestStringCache</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestStringCache() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testNode
	/**
	 ** Verifies that when a cached instance is used to construct a JID, no
	 ** unexpected exceptions pop up.
	 */
	@Test
	public void testNode() {
		JabberIdentifier.build("validnode", "validdomain", "validresource");
		JabberIdentifier.build("validnode", "validdomain", "validresource");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testNodeDomainCacheLookup
	/**
	 ** Verify cache usage, by inserting a value in the cache that's a valid node,
   ** but an invalid domain identifier. Next, create a JID that uses this value
   ** for its domain identifier. This JID constructions should fail, but
   ** will succeed if the cache that was used to store the node-value is the
   ** same cache that's used to lookup previously stringprepped domain
   ** identifiers.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testNodeDomainCacheLookup() {
		// valid value for node identifier, invalid for domain identifier
		final String value = "-test-a-";
		// populate the cache (value is valid in the context of a node)
		JabberIdentifier.build(value, "validdomain.org", "validresource");
		// verify if the cache gets re-used to lookup value.
		JabberIdentifier.build("validnode", value, "validresource");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testNodeDomainCacheLookup
	/**
	 ** Verify cache usage, by inserting a value in the cache that's a valid
   ** resource, but an invalid domain identifier. Next, create a JID that uses
   ** this value for its domain identifier. This JID constructions should fail,
   ** but will succeed if the cache that was used to store the resource-value
   ** is the same cache that's used to lookup previously stringprepped domain
   ** identifiers.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testResourceDomainCacheLookup() {
    // valid value for resource identifier, invalid for domain identifier
    final String value = "-test-b-";
    // populate the cache (value is valid in the context of a node)
    JabberIdentifier.build("validnode", "validdomain.org", value);
    // verify if the cache gets re-used to lookup value.
    JabberIdentifier.build("validnode", value, "validresource");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testResourceNodeCacheLookup
	/**
	 ** Verify cache usage, by inserting a value in the cache that's a valid
	 ** resource, but an invalid node identifier. Next, create a JID that uses
	 ** this value for its domain identifier. This JID constructions should fail,
	 ** but will succeed if the cache that was used to store the resource-value
	 ** is the same cache that's used to lookup previously stringprepped node
	 ** identifiers.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testResourceNodeCacheLookup() {
		// valid value for resource identifier, invalid for nodeidentifier
		final String value = "test@c";
		// populate the cache (value is valid in the context of a resource)
		JabberIdentifier.build("validnode", "validdomain.org", value);
		// verify if the cache gets re-used to lookup value.
		JabberIdentifier.build(value, "valid_domain", "validresource");
	}
}

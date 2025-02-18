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

    File        :   JabberIdentifier.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    JabberIdentifier.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.xmpp;

import java.time.Duration;

import java.nio.charset.StandardCharsets;

import gnu.inet.encoding.IDNA;
import gnu.inet.encoding.Stringprep;

import oracle.iam.identity.icf.cache.Cache;
import oracle.iam.identity.icf.cache.Weighter;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class JabberIdentifier
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** An XMPP address (JID).
 ** <br>
 ** A JID is made up of a node (generally a username), a domain, and a resource.
 ** The node and resource are optional; domain is required. In simple ABNF form:
 ** <ul><li><code>jid ::= [ node "@" ] domain [ "/" resource ]</code></li></ul>
 ** <p>
 ** Some sample JID's:
 ** <ul>
 **   <li><code>user@example.com</code>
 **   <li><code>user@example.com/home</code>
 **   <li><code>example.com</code>
 ** </ul>
 ** Each allowable partition of a JID (node, domain, and resource) must not be
 ** more than 1023 bytes in length, resulting in a maximum total size (including
 ** the '@' and '/' separators) of 3071 bytes.
 ** <p>
 **  *
 ** JID instances are immutable. Multiple threads can act on data represented by
 ** JID objects without concern of the data being changed by other threads.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JabberIdentifier implements Comparable<JabberIdentifier> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // Stringprep operations are very expensive.
  // Therefore, we cache node, domain and resource values that have already had
  // stringprep applied so that we can check incoming values against the cache.
  public static final Cache<String, ValueWrapper<String>> NODE =
    Cache.Builder.<String, ValueWrapper<String>>of()
      .weighter(Singleton.INSTANCE)
      .maximumWeight(1000000)
      .expireAfterWrite(Duration.ofMinutes(30) )
      .recordMetric()
      .build();

  public static final Cache<String, ValueWrapper<String>> DOMAIN =
    Cache.Builder.<String, ValueWrapper<String>>of()
      .weighter(Singleton.INSTANCE)
      .maximumWeight(50000)
      .expireAfterWrite(Duration.ofMinutes(30) )
      .recordMetric()
      .build();

  public static final Cache<String, ValueWrapper<String>> RESOURCE =
    Cache.Builder.<String, ValueWrapper<String>>of()
      .weighter(Singleton.INSTANCE)
      .maximumWeight(1000000)
      .expireAfterWrite(Duration.ofMinutes(30) )
      .recordMetric()
      .build();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String node;
  private String domain;
  private String resource;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Singleton
  /**
  /**
   ** A {@link Weighter} that weights cache entries that contains
   ** {@link JabberIdentifier} partitions.
   ** <br>
   ** The weights that are used are byte-size based.
   */
  enum Singleton implements Weighter<String, ValueWrapper<String>> {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////
  
    ////////////////////////////////////////////////////////////////////////////
    // Method: weight (Weighter)
    /**
     ** Returns the weight of a cache entry.
     ** <br>
     ** There is no unit for entry weights; rather they are simply relative to
     ** each other.
     **
     ** @param  key              the key represented by the entry to weight.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value represented by the entry to weight.
     **                          <br>
     **                          Allowed object is {@link ValueWrapper} of type
     **                          {@link String}.
     **
     ** @return                  the weight of the entry; must be non-negative.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int weight(final String key, final ValueWrapper<String> value) {
      int result = 0;
      result += stringSize(key);
      result += valueSize(value);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JabberIdentifier</code> from it's string
   ** representation.
   ** <br>
   ** This construction allows the caller to specify if stringprep should be
   ** applied or not.
   **
   ** @param  identifier         a valid <code>JabberIdentifier</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  skip               <code>true</code> if stringprep should not be
   **                            applied.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws IllegalArgumentException if the JID is not valid.
   */
  private JabberIdentifier(final String identifier, final boolean skip) {
    // ensure inheritance
    this(partition(identifier), skip);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JabberIdentifier</code> from it's string
   ** representation.
   ** <br>
   ** This construction allows the caller to specify if stringprep should be
   ** applied or not.
   **
   ** @param  partition          a valid <code>JabberIdentifier</code>.
   **                            <br>
   **                            Allowed object array of {@link String}.
   ** @param  skip               <code>true</code> if stringprep should not be
   **                            applied.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws IllegalArgumentException if the JID is not valid.
   */
  public JabberIdentifier(final String[] partition, final boolean skip) {
    // ensure inheritance
    this(partition[0], partition[1], partition[2], skip);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JabberIdentifier</code> given a node, domain, and
   ** resource.
   **
   ** @param  node               the node.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domain             the domain, which must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException     if domain is <code>null</code>.
   ** @throws IllegalArgumentException if the JID is not valid.
   */
  public JabberIdentifier(final String node, final String domain,final String resource) {
    // ensure inheritance
    this(node, domain, resource, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JabberIdentifier</code> given a node, domain, and
   ** resource.
   ** <br>
   ** This construction allows the caller to specify if stringprep should be
   ** applied or not.
   **
   ** @param  node               the node.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domain             the domain, which must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  skip               <code>true</code> if stringprep should not be
   **                            applied.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** throws NullPointerException      if domain is <code>null</code>.
   ** @throws IllegalArgumentException if the JID is not valid.
   */
  public JabberIdentifier(final String node, final String domain,final String resource, final boolean skip) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (domain == null)
      throw new NullPointerException("Domain must not be null");

    if (skip) {
      this.node     = node;
      this.domain   = domain;
      this.resource = resource;
    }
    else {
      // set node and resource to null if they are empty strings
      if (StringUtility.empty(node))
        this.node = null;
      if (StringUtility.empty(resource))
        this.resource = null;

      // Stringprep (node prep, resourceprep, etc).
      try {
        this.node     = nodePrepper(node);
        this.domain   = domainPrepper(domain);
        this.resource = resourcePrepper(resource);
      }
      catch (Exception e) {
        final StringBuilder builder = new StringBuilder();
        if (node != null) {
          builder.append(node).append("@");
        }
        builder.append(domain);
        if (resource != null) {
          builder.append("/").append(resource);
        }
        throw new IllegalArgumentException("Illegal JID: " + builder.toString(), e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   node
	/**
	 ** Returns the node value, or <code>null</code> if this JID does not
   ** contain node information.
   **
   ** @return                    the node value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String node() {
    return this.node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domain
	/**
	 ** Returns the domain value, or <code>null</code>.
   **
   ** @return                    the domain value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String domain() {
    return this.domain;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
	/**
	 ** Returns the resource value, or <code>null</code> if this JID does not
   ** contain node information.
   **
   ** @return                    the resource value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String resource() {
    return this.resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bare
	/**
   ** Returns the String representation of the bare JID, which is the JID with
   ** resource information removed.
   ** <br>
   ** For example: <code>username@domain.com</code>
   **
   ** @return                    the string representation of the bare JID.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String bare() {
    final StringBuilder builder = new StringBuilder();
    if (this.node != null)
      builder.append(this.node).append('@');

    builder.append(this.domain);
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   full
	/**
	 ** Returns representation of the full JID.
   ** <br>
   ** For example: <code>username@domain.com/mobile</code>
   **
   ** @return                    the string representation of the full JID.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String full() {
    return toString();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object
   ** is less than, equal to, or greater than the specified object.
   **
   ** @param  other              the Object to be compared.
   **                            <br>
   **                            Allowed object is {@link JabberIdentifier}.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws ClassCastException if the specified object's type is not
   **                            an instance of <code>Accessor</code>.
   */
  @Override
  public int compareTo(final JabberIdentifier other) {
    // comparison order is domain, node, resource.
    int compare = this.domain.compareTo(other.domain);
    if (compare == 0) {
      final String thisNode = this.node  != null ? this.node  : "";
      final String thatNode = other.node != null ? other.node : "";
      compare = thisNode.compareTo(thatNode);
    }
    if (compare == 0) {
      final String thisResource = this.resource  != null ? this.resource  : "";
      final String thatResource = other.resource != null ? other.resource : "";
      compare = thisResource.compareTo(thatResource);
    }
    return compare;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>JabberIdentifier</code> from it's string
   ** representation.
   **
   ** @param  identifier         a valid <code>JabberIdentifier</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>JabberIdentifier</code> from it's
   **                            string representation.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static JabberIdentifier build(final String identifier) {
    return new JabberIdentifier(identifier, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>JabberIdentifier</code> given a node, domain, and
   ** resource.
   ** <br>
   ** This construction allows the caller to specify if stringprep should be
   ** applied or not.
   **
   ** @param  node               the node.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  domain             the domain, which must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException     if domain is <code>null</code>.
   ** @throws IllegalArgumentException if the JID is not valid.
   */
  public static JabberIdentifier build(final String node, final String domain,final String resource) {
    return new JabberIdentifier(node, domain, resource, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nodePrepper
  /**
   ** Returns a valid representation of a JID node, based on the provided input.
   ** <br>
   ** This method throws an {@link IllegalArgumentException} if the provided
   ** argument cannot be represented as a valid JID node (e.g. if
   ** StringPrepping fails).
   **
   ** @param  value              the raw node value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a string based JID node representation.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if <code>value</code> is not a valid JID
   **                                  node.
   */
  public static String nodePrepper(final String value) {
    // prevent bogus input
    if (value == null)
      return null;

    final ValueWrapper<String> cached = NODE.get(
      value
    , k -> {
        try {
          final String answer = Stringprep.nodeprep(k);
          // validate field is not greater than 1023 bytes
          // UTF-8 characters use one to four bytes
          if (answer.getBytes( StandardCharsets.UTF_8 ).length > 1023)
            return new ValueWrapper<>(new IllegalArgumentException("Node cannot be larger than 1023 bytes (after nodeprepping). Size is " + answer.getBytes( StandardCharsets.UTF_8 ).length + " bytes."));

          return answer.equals(value) ? new ValueWrapper<>() : new ValueWrapper<>(answer);
        }
        catch (Exception e) {
          // register the failure in the cache
          return new ValueWrapper<>(e);
        }
      }
    );
    // this should only be possible if the computation can lead to a null value,
    // which isn't the case in the implementation above
    assert cached != null;
    switch (cached.representation()) {
      case KEY     : return value;
      case VALUE   : return cached.value();
      case ILLEGAL : throw new IllegalArgumentException("The input '" + value + "' is not a valid JID node partiton: " + cached.exception());
      default      : // should not occur.
                     throw new IllegalStateException("The implementation of JabberIdentifier#nodePrepper(String) is broken.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   domainPrepper
  /**
   ** Returns a valid representation of a JID domain part, based on the provided
   ** input.
   ** <br>
   ** This method throws an {@link IllegalArgumentException} if the provided
   ** argument cannot be represented as a valid JID domain part
   ** (e.g. if Stringprepping fails).
   **
   ** @param  value              the raw domain value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a string based JID domain part representation.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if <code>value</code> is not a valid JID
   **                                  domain part.
   */
  public static String domainPrepper(String value) {
    // prevent bogus input
    if (value == null)
      throw new IllegalArgumentException("Argument 'value' cannot be null.");

    final ValueWrapper<String> cached = DOMAIN.get(
      value
    , k -> {
        try {
          final String answer = Stringprep.nameprep(IDNA.toASCII(k), false);
          // validate field is not greater than 1023 bytes
          // UTF-8 characters use one to four bytes
          if (answer.getBytes( StandardCharsets.UTF_8).length > 1023)
            return new ValueWrapper<>(new IllegalArgumentException("Domain cannot be larger than 1023 bytes (after nameprepping). Size is " + answer.getBytes( StandardCharsets.UTF_8 ).length + " bytes."));

          return answer.equals(value) ? new ValueWrapper<>() : new ValueWrapper<>(answer);
        }
        catch (Exception e) {
          // register the failure in the cache
          return new ValueWrapper<>(e);
        }
      }
    );
    // this should only be possible if the computation can lead to a null value,
    // which isn't the case in the implementation above
    assert cached != null;
    switch (cached.representation()) {
      case KEY     : return value;
      case VALUE   : return cached.value();
      case ILLEGAL : throw new IllegalArgumentException("The input '" + value + "' is not a valid JID domain partition: " + cached.exception());
      default      : // should not occur.
                     throw new IllegalStateException("The implementation of JabberIdentifier#domainPrepper(String) is broken.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourcePrepper
  /**
   ** Returns a valid representation of a JID resource, based on the provided
   ** input.
   ** <br>
   ** This method throws an {@link IllegalArgumentException} if the provided
   ** argument cannot be represented as a valid JID resource (e.g. if
   ** StringPrepping fails).
   **
   ** @param  value              the raw resource value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a string based JID resource representation.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException if <code>value</code> is not a valid JID
   **                                  resource.
   */
  public static String resourcePrepper(String value) {
    // prevent bogus input
    if (value == null)
      return null;

    final ValueWrapper<String> cached = RESOURCE.get(
      value
    , k -> {
        try {
          final String answer = Stringprep.resourceprep(value);
          // validate field is not greater than 1023 bytes
          // UTF-8 characters use one to four bytes
          if (answer.getBytes(StandardCharsets.UTF_8).length > 1023)
            return new ValueWrapper<>(new IllegalArgumentException("Resource cannot be larger than 1023 bytes (after resourceprepping). Size is " + answer.getBytes(StandardCharsets.UTF_8).length + " bytes."));

          return answer.equals(value) ? new ValueWrapper<>() : new ValueWrapper<>(answer);
        }
        catch (Exception e) {
          // register the failure in the cache
          return new ValueWrapper<>(e);
        }
      }
    );
    // this should only be possible if the computation can lead to a null value,
    // which isn't the case in the implementation above
    assert cached != null;
    switch (cached.representation()) {
      case KEY     : return value;
      case VALUE   : return cached.value();
      case ILLEGAL : throw new IllegalArgumentException("The input '" + value + "' is not a valid JID resource partition: " + cached.exception());
      default      : // should not occur.
                     throw new IllegalStateException("The implementation of JabberIdentifier#resourcePrepper(String) is broken.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escape
  /**
   ** Escapes the node portion of a JID according to "JID Escaping" (JEP-0106).
   ** <p>
   ** Escaping replaces characters prohibited by node-prep with escape
   ** sequences, as follows:
   ** <table border="1">
   **   <caption>&nbsp;</caption>
   **   <tr><td><b>Unescaped Character</b></td><td><b>Encoded Sequence</b></td></tr>
   **   <tr><td>&lt;space&gt;</td><td>\20</td></tr>
   **   <tr><td>"</td><td>\22</td></tr>
   **   <tr><td>&amp;</td><td>\26</td></tr>
   **   <tr><td>'</td><td>\27</td></tr>
   **   <tr><td>/</td><td>\2f</td></tr>
   **   <tr><td>:</td><td>\3a</td></tr>
   **   <tr><td>&lt;</td><td>\3c</td></tr>
   **   <tr><td>&gt;</td><td>\3e</td></tr>
   **   <tr><td>@</td><td>\40</td></tr>
   **   <tr><td>\</td><td>\5c</td></tr>
   ** </table>
   ** <p>
   ** This process is useful when the node comes from an external source that
   ** doesn't conform to nodeprep. For example, a username in LDAP may be
   ** "Joe Smith". Because the &lt;space&gt; character isn't a valid part of a
   ** node, the username should be escaped to "Joe\20Smith" before being made
   ** into a JID (e.g. "joe\20smith@example.com" after case-folding, etc. has
   ** been applied).
   ** <p>
   ** All node escaping and un-escaping must be performed manually at the
   ** appropriate time.
   **
   ** @param  node               the un-escaped version of the node.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the escaped version of the node.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String escape(final String node) {
    // prevent bogus input
    if (node == null)
      return null;

    final StringBuilder builder = new StringBuilder(node.length() + 8);
    for (int i = 0, n = node.length(); i < n; i++) {
      char c = node.charAt(i);
      switch (c) {
        case '"'  : builder.append("\\22");
                    break;
        case '&'  : builder.append("\\26");
                    break;
        case '\'' : builder.append("\\27");
                    break;
        case '/'  : builder.append("\\2f");
                    break;
        case ':'  : builder.append("\\3a");
                    break;
        case '<'  : builder.append("\\3c");
                    break;
        case '>'  : builder.append("\\3e");
                    break;
        case '@'  : builder.append("\\40");
                    break;
        case '\\' : final int c2 = (i + 1 < n) ? node.charAt(i + 1) : -1;
                    final int c3 = (i + 2 < n) ? node.charAt(i + 2) : -1;
                    if ((c2 == '2' && (c3 == '0' || c3 == '2' || c3 == '6' || c3 == '7' || c3 == 'f')) ||
                        (c2 == '3' && (c3 == 'a' || c3 == 'c' || c3 == 'e')) ||
                        (c2 == '4' && c3 == '0') ||
                        (c2 == '5' && c3 == 'c')) {
                        builder.append("\\5c");
                    }
                    else {
                      builder.append(c);
                    }
                    break;
        default   : builder.append(Character.isWhitespace(c) ? "\\20" : c);
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescape
  /**
   ** Un-escapes the node portion of a JID according to "JID Escaping"
   ** (JEP-0106).
   ** <p>
   ** Escaping replaces characters prohibited by node-prep with escape
   ** sequences, as follows:
   ** <table border="1">
   **   <caption>&nbsp;</caption>
   **   <tr><td><b>Unescaped Character</b></td><td><b>Encoded Sequence</b></td></tr>
   **   <tr><td>&lt;space&gt;</td><td>\20</td></tr>
   **   <tr><td>"</td><td>\22</td></tr>
   **   <tr><td>&amp;</td><td>\26</td></tr>
   **   <tr><td>'</td><td>\27</td></tr>
   **   <tr><td>/</td><td>\2f</td></tr>
   **   <tr><td>:</td><td>\3a</td></tr>
   **   <tr><td>&lt;</td><td>\3c</td></tr>
   **   <tr><td>&gt;</td><td>\3e</td></tr>
   **   <tr><td>@</td><td>\40</td></tr>
   **   <tr><td>\</td><td>\5c</td></tr>
   **   </table>
   ** <p>
   ** This process is useful when the node comes from an external source that
   ** doesn't conform to nodeprep. For example, a username in LDAP may be
   ** "Joe Smith". Because the &lt;space&gt; character isn't a valid part of a
   ** node, the username should be escaped to "Joe\20Smith" before being made
   ** into a JID (e.g. "joe\20smith@example.com" after case-folding, etc. has
   ** been applied).
   ** <p>
   ** All node escaping and un-escaping must be performed manually at the
   ** appropriate time.
   **
   ** @param  node               the escaped version of the node.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the un-escaped version of the node.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String unescape(final String node) {
    // prevent bogus input
    if (node == null)
      return null;

    final char[]        sequence = node.toCharArray();
    final StringBuilder builder  = new StringBuilder(sequence.length);
    for (int i = 0, n = sequence.length; i < n; i++) {
      compare:
      {
        char c = node.charAt(i);
        if (c == '\\' && i + 2 < n) {
          char c2 = sequence[i + 1];
          char c3 = sequence[i + 2];
          if (c2 == '2') {
            switch (c3) {
              case '0' : builder.append(' ');
                         i += 2;
                         break compare;
              case '2' : builder.append('"');
                         i += 2;
                         break compare;
              case '6' : builder.append('&');
                         i += 2;
                         break compare;
              case '7' : builder.append('\'');
                         i += 2;
                         break compare;
              case 'f' : builder.append('/');
                         i += 2;
                         break compare;
            }
          }
          else if (c2 == '3') {
            switch (c3) {
              case 'a' : builder.append(':');
                         i += 2;
                         break compare;
              case 'c' : builder.append('<');
                         i += 2;
                         break compare;
              case 'e' : builder.append('>');
                         i += 2;
                         break compare;
            }
          }
          else if (c2 == '4') {
            if (c3 == '0') {
              builder.append("@");
              i += 2;
              break compare;
            }
          }
          else if (c2 == '5') {
            if (c3 == 'c') {
              builder.append("\\");
              i += 2;
              break compare;
            }
          }
        }
        builder.append(c);
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   weighter
  /**
   ** Factory method to create a {@link }Weighter}.
   **
   ** @return                    a <code>Weighter</code> where an entry has a
   **                            weight of <code>1</code>
   **                            <br>
   **                            Possible object is <code>Weighter</code>.
   */
  static Weighter<String, ValueWrapper<String>> weighter() {
    return Singleton.INSTANCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueSize
  /**
   ** Returns the size in bytes of a String.
   ** Returns the size in bytes of a string.
   **
   ** @param  value              the object to determine the size of.
   **                            <br>
   **                            Allowed object is {@link ValueWrapper} for
   **                            type {@link String}.
   **                           
   ** @return                    the size of the given object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int valueSize(final ValueWrapper<String> value) {
    // prevent bogus input
    if (value == null)
      return 0;

    // 'object' overhead.
    int result = 4;
    result += stringSize(value.value());
    result += stringSize(value.exception());
    // for the reference to the enum value.
    result += 4;
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringSize
  /**
   ** Returns the size in bytes of a string.
   **
   ** @param  string             the string to determine the size of.
   **                            <br>
   **                            Allowed object is {@link String}.
   **                           
   ** @return                    the size of the given string.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int stringSize(String string) {
    if (string == null) {
      return 0;
    }
    return 4 + string.getBytes().length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return bare().hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>JID</code>s are considered equal if and only if they
   ** represent the same encoded, decoded and template value. As a consequence,
   ** two given <code>JID</code>s may be different even though they contain the
   ** same attribute value.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    // comparison order is domain, node, resource.
    JabberIdentifier that = (JabberIdentifier)other;
    if (this.domain != null ? !this.domain.equals(that.domain) : that.domain != null)
      return false;

    if (this.node != null ? !this.node.equals(that.node) : that.node != null)
      return false;

    return !(this.resource != null ? !this.resource.equals(that.resource) : that.resource != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    if (this.node != null)
      builder.append(this.node).append('@');

    builder.append(this.domain);
    if (this.resource != null)
      builder.append('/').append(this.resource);
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   partition
  /**
   ** Returns a string array with the parsed node, domain and resource.
   ** <br>
   ** No Stringprep is performed while parsing the textual representation.
   **
   ** @param  jid                the textual <code>JID</code> representation.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a string array with the parsed node, domain
   **                            and resource.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  static String[] partition(final String jid) {
    final String[] partition = new String[3];
    // prevent bogus input
    if (jid == null)
      return partition;

    final int at    = jid.indexOf("@");
    if (at + 1 > jid.length())
      throw new IllegalArgumentException("JID with empty domain not valid");

    // node
    if (at > 0)
      partition[0] = jid.substring(0, at);
    // domain
    final int slash = jid.indexOf("/");
    if (at < 0) {
      partition[1] = (slash > 0) ? jid.substring(0, slash) : jid;
    }
    else {
      partition[1] = (slash > 0) ? jid.substring(at + 1, slash) : jid.substring(at + 1);
    }
    // resource
    partition[2] = (slash + 1 > jid.length() || slash < 0) ? null : jid.substring(slash + 1);
    return partition;
  }
}
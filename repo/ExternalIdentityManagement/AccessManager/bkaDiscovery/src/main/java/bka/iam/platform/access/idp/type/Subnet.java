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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Embedded Credential Collector

    File        :   Subnet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Subnet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.idp.type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.InetAddress;
import java.net.UnknownHostException;

////////////////////////////////////////////////////////////////////////////////
// class Subnet
// ~~~~~ ~~~~~~
/**
 ** A class that performs some subnet calculations given a network address and a
 ** subnet mask.
 **
 ** @see   http://www.faqs.org/rfcs/rfc1519.html
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Subnet {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String  FORMAT_IPV4   = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
  private static final String  FORMAT_CIDR   = FORMAT_IPV4 + "/(\\d{1,3})";
  private static final Pattern PATTERN_IPV4  = Pattern.compile(FORMAT_IPV4);
  private static final Pattern PATTERN_CIDR  = Pattern.compile(FORMAT_CIDR);
  private static final int     NBITS         = 32;

  // mask to convert unsigned int to a long (i.e. keep 32 bits)
  private static final long    UNSIGNED_MASK = 0x0ffffffffL;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static InetAddress           LOOPBACK_IPV4 = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      LOOPBACK_IPV4  = InetAddress.getByAddress("localhost", new byte[]{0, 0, 0, 1});
		}
    catch (UnknownHostException e) {
  		e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final int            address;
  private final int            netmask;
  private final int            network;
  private final int            broadcast;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Subnet</code> that takes a CIDR-notation string, e.g.
   ** "192.168.0.1/16".
   **
   ** @param  notation          a CIDR-notation string, e.g. "192.168.0.1/16".
   */
  private Subnet(final String notation) {
    // ensure inheritance
    super();

    // initialize instance
    final Matcher matcher = PATTERN_CIDR.matcher(notation);
    if (matcher.matches()) {
      this.address = matchAddress(matcher);
      // create a binary netmask from the number of bits specification /x
      int trailingZeroes = NBITS - rangeCheck(Integer.parseInt(matcher.group(5)), 0, NBITS);
      // an IPv4 netmask consists of 32 bits, a contiguous sequence
      // of the specified number of ones followed by all zeros.
      // So, it can be obtained by shifting an unsigned integer (32 bits) to the
      // left by the number of trailing zeros which is (32 - the # bits
      // specification).
      // Note that there is no unsigned left shift operator, so we have to use
      // a long to ensure that the left-most bit is shifted out correctly.
      this.netmask   = (int)(UNSIGNED_MASK << trailingZeroes);
      // calculate base network address
      this.network   = (address & netmask);
      // calculate broadcast address
      this.broadcast = network | ~(netmask);
    }
    else
      throw new IllegalArgumentException("Could not parse [" + notation + "]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Subnet</code> that takes two dotted decimal addresses.
   **
   ** @param  address            an IP address, e.g. "192.168.0.1".
   ** @param  netmask            a dotted decimal netmask e.g. "255.255.0.0".
   */
  private Subnet(final String address, final String netmask) {
    // ensure inheritance
    this(address, intAddress(netmask));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Subnet</code> that takes two dotted decimal addresses.
   **
   ** @param  address            an IP address, e.g. "192.168.0.1".
   ** @param  netmask            the mask used in the CIDR notation.
   */
  private Subnet(final String address, final int netmask) {
    // ensure inheritance
    super();

    // initialize instance
    // initialize instance
    this.address = intAddress(address);
    this.netmask = netmask;
    if ((this.netmask & -this.netmask) - 1 != ~this.netmask)
      throw new IllegalArgumentException("Could not parse [" + netmask + "]");

    // calculate base network address
    this.network  = (this.address & this.netmask);
    // calculate broadcast address
    this.broadcast = this.network | ~(this.netmask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inetAddress
  /**
   ** Convert a IPv4 address from an integer to an {@link InetAddress}.
   **
   ** @param  address              an int corresponding to the IPv4 address in
   **                              network byte order.
   */
  public static InetAddress inetAddress(final int address) {
    final byte[] bytes = { (byte)(0xff & address), (byte)(0xff & (address >> 8)), (byte)(0xff & (address >> 16)), (byte)(0xff & (address >> 24)) };
    try {
      return InetAddress.getByAddress(bytes);
    }
    catch (UnknownHostException e) {
      throw new AssertionError();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intAddress
  /**
   ** Convert a IPv4 address from an {@link InetAddress} to an integer
   **
   ** @param  address            an {@link InetAddress} corresponding to the
   **                            IPv4 address.
   **
   ** @return                    the IP address as an integer in network byte
   **                            order.
   */
  public static int intAddress(final InetAddress address) {
    final byte[] bytes = address.getAddress();
    return ((bytes[3] & 0xff) << 24) | ((bytes[2] & 0xff) << 16) | ((bytes[1] & 0xff) << 8) | (bytes[0] & 0xff);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cidr
  /**
   ** Convert a IPv4 address from an {@link InetAddress} to an integer
   **
   ** @param  address            an IP address corresponding to the
   **                            IPv4 address.
   **
   ** @return                    the IP address as an integer in network byte
   **                            order.
   */
  public static String cidr(final Subnet address) {
    return stringAddress(address.address) + "/" + netmaskPrefixLength(address.netmask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringAddress
  /**
   ** Convert a IPv4 address from an {@link InetAddress} to an integer
   **
   ** @param  address            an IP address corresponding to the
   **                            IPv4 address.
   **
   ** @return                    the IP address as an integer in network byte
   **                            order.
   */
  public static String stringAddress(final int address) {
    return ((address >> 24 ) & 0xff) + "." + ((address >> 16) & 0xff) + "." + ((address >>  8) & 0xff) + "." + (address & 0xff);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   netmaskPrefixLength
  /**
   ** Convert a IPv4 netmask integer to a prefix length.
   **
   ** @param  netmask             as an integer in network byte order.
   **
   ** @return                     the network prefix length.
   */
  public static int netmaskPrefixLength(final int netmask) {
    return Integer.bitCount(netmask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rangeCheck
  /**
   ** Convenience function to check integer boundaries.
   */
  public static int rangeCheck(final int value, final int begin, final int end) {
    if (value >= begin && value <= end)
      return value;

    throw new IllegalArgumentException("Value out of range: [" + value + "]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Returns an {@link InetAddress} corresponding to the given numeric address
   ** where the string must be a standard representation of a V4 or V6 address
   ** (such as {@code "192.168.0.1"} or {@code "2001:4860:800d::68"}).
   ** <br>
   ** This method will never do a DNS lookup. Non-numeric addresses are errors.
   **
   ** @param  address            the numeric IP address, or <code>null</code>.
   **                            <br>
   **                            Allowed object {@link String}.
   **
   ** @return                    an {@link InetAddress} for the given numeric IP
   **                            address.
   **                            <br>
   **                            Possible object {@link InetAddress}.
   */
  public static InetAddress parse(final String address) {
    if (address == null || address.isEmpty()) {
      return LOOPBACK_IPV4;
    }
		try {
			return InetAddress.getByName(address);
		}
		catch (UnknownHostException e) {
      throw new IllegalArgumentException("Not a numeric address: " + address);
		}
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Subnet</code> that takes a CIDR-notation
   ** string, e.g. "192.168.0.1/16".
   **
   ** @param  notation          a CIDR-notation string, e.g. "192.168.0.1/16".
   **                            <br>
   **                            Allowed object {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>Subnet</code>.
   **                            Possible object <code>Subnet</code>.
   */
  public static Subnet build(final String notation) {
    return new Subnet(notation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Subnet</code> that takes two dotted
   ** decimal addresses.
   **
   ** @param  address            an IP address, e.g. "192.168.0.1".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  netmask            a dotted decimal netmask e.g. "255.255.0.0".
   **                            <br>
   **                            Allowed object {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>Subnet</code>.
   **                            Possible object <code>Subnet</code>.
   */
  public static Subnet build(final String address, final String netmask) {
    return new Subnet(address, netmask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Subnet</code> that takes two dotted
   ** decimal addresses.
   **
   ** @param  address            an IP address, e.g. "192.168.0.1".
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  netmask            the mask used in the CIDR notation.
   **                            <br>
   **                            Allowed object <code>int</code>.
   **
   ** @return                    an newly created instance of
   **                            <code>Subnet</code>.
   **                            Possible object <code>Subnet</code>.
   */
  public static Subnet build(final String address, final int netmask) {
    return new Subnet(address, netmask);
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
   */
  @Override
  public int hashCode() {
    return 31 * this.address + this.netmask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Pair</code>s are considered equal if and only if they represent
   ** the same encoded, decoded and template value. As a consequence, two given
   ** <code>Pair</code>s may be different even though they contain the same
   ** attribute value.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    final Subnet that = (Subnet)other;
    return ((this.address == that.address) && (this.netmask == that.netmask));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inRange
  /**
   ** Returns <code>true</code> if the parameter <code>address</code> is in the
   ** range of usable endpoint addresses for this subnet.
   ** <br>
   ** This excludes the network and broadcast addresses.
   **
   ** @param  address            a dot-delimited IPv4 address, e.g.
   **                            "192.168.0.1".
   **                            <br>
   **                            Allowed object {@link String}.
   **
   ** @return                    <code>true</code> if in range,
   **                            <code>false</code> otherwise.
   **                            Possible object <code>boolean</code>.
   */
  public boolean inRange(final String address) {
    return inRange(intAddress(address));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inRange
  /**
   ** Returns <code>true</code> if the parameter <code>address</code> is in the
   ** range of usable endpoint addresses for this subnet.
   ** <br>
   ** This excludes the network and broadcast addresses.
   **
   ** @param  address            the binary address to check.
   **                            <br>
   **                            Allowed object <code>int</code>.
   **
   ** @return                    <code>true</code> if in range,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object <code>boolean</code>.
   */
  public boolean inRange(final int address) {
    // cannot ever be in range; rejecting now avoids problems with CIDR/31,32
    if (address == 0)
      return false;

    long add  = address & UNSIGNED_MASK;
    long low  = this.network & UNSIGNED_MASK;
    long high = this.broadcast & UNSIGNED_MASK;
    return add >= low && add <= high;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cidr
  /**
   ** Convert a IPv4 address from an {@link InetAddress} to a string.
   **
   ** @return                    the CIDR-notation of the network subnet.
   */
  public String cidr() {
    return cidr(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intAddress
  /**
   ** Convert a IPv4 address from an {@link InetAddress} to an integer
   **
   ** @param  address            an {@link InetAddress} corresponding to the
   **                            IPv4 address.
   **
   ** @return                    the IP address as an integer in network byte
   **                            order.
   */
  private static int intAddress(final String address) {
    final Matcher matcher = PATTERN_IPV4.matcher(address);
    if (matcher.matches()) {
      return matchAddress(matcher);
    }
    else
      throw new IllegalArgumentException("Could not parse [" + address + "]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   matchAddress
  /**
   ** Convenience method to extract the components of a dotted decimal address
   ** and pack into an integer using a regex match.
   */
  private static int matchAddress(final Matcher matcher) {
    int address = 0;
    for (int i = 1; i <= 4; ++i) {
      int n = (rangeCheck(Integer.parseInt(matcher.group(i)), 0, 255));
      address |= ((n & 0xff) << 8 * (4 - i));
    }
    return address;
  }
}

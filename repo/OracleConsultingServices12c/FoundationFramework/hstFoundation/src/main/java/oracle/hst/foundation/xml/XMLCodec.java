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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared XML Stream Facilities

    File        :   XMLCodec.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLCodec.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import oracle.hst.foundation.utility.MimeCodec;

////////////////////////////////////////////////////////////////////////////////
// class XMLCodec
// ~~~~~ ~~~~~~~~
/**
 ** Implementation of the abstract class {@link MimeCodec} for XML entity
 ** encoding.
 ** <p>
 ** Only the following named entities are predefined:
 ** <ul>
 ** 	<li>lt
 ** 	<li>gt
 ** 	<li>amp
 ** 	<li>apos
 ** 	<li>quot
 ** </ul>
 ** However, the XML Specification 1.0 states in section 4.6 "Predefined
 ** Entities" that these should still be declared for interoperability purposes.
 ** As such, encoding in this class will not use them.
 ** <p>
 ** It's also worth noting that unlike the HTMLCodec, a trailing semicolon is
 ** required and all valid codepoints are accepted.
 ** <p>
 ** Note that it is a REALLY bad idea to use this for decoding as an XML
 ** document can declare arbitrary entities that this Codec has no way of
 ** knowing about. Decoding is included for completeness but it's use is not
 ** recommended. Use a XML parser instead!
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 */
public class XMLCodec extends MimeCodec {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the named entities this implementation supports */
  private static final String[][] DICTIONARY = {
    { "quot", "34" }
  , { "amp",  "38" }
  , { "apos", "39" }
  , { "lt",   "60" }
  , { "gt",   "62" }
  };

  /**
   ** the one and only instance of the <code>XMLCodecQuote</code>
   ** <p>
   ** Singleton Pattern
   */
  private static final XMLCodec instance = new XMLCodec();

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3681104349600322195")
  private static final long serialVersionUID = -6401708341120877947L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLCodec</code> object.
   ** <p>
   ** This creates an object that can be used to write escaped XML.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new XMLCodec()" and enforces use of the public factory method below.
   */
  private XMLCodec() {
    // ensure inheritance
    super(DICTIONARY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the sole instance of this of the registry.
   **
   ** @return                     the sole instance of this of the registry.
   */
  public static XMLCodec instance() {
    return instance;
  }
}
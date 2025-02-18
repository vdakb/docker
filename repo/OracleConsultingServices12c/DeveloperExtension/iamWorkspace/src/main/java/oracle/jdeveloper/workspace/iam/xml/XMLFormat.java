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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   XMLFormat.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLFormat.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

import java.io.Serializable;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

import oracle.jdeveloper.workspace.iam.utility.MimeCodec;

////////////////////////////////////////////////////////////////////////////////
// class XMLFormat
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>XMLFormat</code> object is used to provide information on  how a
 ** generated XML document should be structured. The information provided tells
 ** the formatter whether an XML prolog is required and the number of spaces
 ** that should be used for indenting. The prolog specified will be written
 ** directly before the XML document.
 ** <p>
 ** Should a <code>XMLFormat</code> be created with an indent of zero or less
 ** then no indentation is done, and the generated XML will be on the same line.
 ** The prolog can contain any legal XML heading, which can domain a DTD
 ** declaration and XML comments if required.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class XMLFormat implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:900749789524509200")
  private static final long        serialVersionUID = 8041255246818518812L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the indent size to use for the generated XML. */
  private final     int            indent;

  /** the prolog that appears in the generated XML. */
  private final     String         prolog;

  /** the style that is used internally by the format. */
  private final     XMLStyle       style;

  /** determines the verbosity preference of XML. */
  private final     XMLVerbosity   verbosity;

  /** the character encoding to write the XML document. */
  private transient CharsetEncoder encoder;

  /** creates the codec to escape predefined entities. */
  private transient MimeCodec      codec;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object. This creates an object
   ** that is used to describe how the formatter should create the XML
   ** document. This constructor uses an indent size of two.
   */
   public XMLFormat() {
    this(2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified indent size
   ** and a <code>null</code> prolog, which means no prolog is generated.
   **
   ** @param  indent             the number of spaces used in the indent.
   */
   public XMLFormat(int indent) {
     this(indent, null, StandardCharsets.US_ASCII, new XMLStyle.NULL());
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified prolog that
   ** is to be inserted at the start of the XML document.
   **
   ** @param  prolog             the prolog for the generated XML document.
   */
  public XMLFormat(final String prolog) {
    this(prolog, StandardCharsets.US_ASCII);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified indent size
   ** and a <code>null</code> prolog, which means no prolog is generated.
   **
   ** @param  charset            the requested charset for encoding.
   */
   public XMLFormat(final Charset charset) {
     this(2, null, charset, new XMLStyle.NULL());
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified prolog that
   ** is to be inserted at the start of the XML document.
   **
   ** @param  prolog             the prolog for the generated XML document.
   ** @param  charset            the requested charset for encoding.
   */
  public XMLFormat(final String prolog, final Charset charset) {
    this(2, prolog, charset);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object. This creates an object
   ** that is used to describe how the formatter should create the XML document.
   ** This constructor uses the specified indent size and the text to use in the
   ** generated prolog.
   **
   ** @param  indent             the number of spaces used in the indent.
   ** @param  prolog             the prolog for the generated XML document.
   */
  public XMLFormat(final int indent, final String prolog) {
    this(indent, prolog, StandardCharsets.US_ASCII, new XMLStyle.NULL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object. This creates an object
   ** that is used to describe how the formatter should create the XML document.
   ** This constructor uses the specified indent size and the text to use in the
   ** generated prolog.
   **
   ** @param  indent             the number of spaces used in the indent.
   ** @param  prolog             the prolog for the generated XML document.
   ** @param  charset            the requested charset for encoding.
   */
  public XMLFormat(final int indent, final String prolog, final Charset charset) {
    this(indent, prolog, charset, new XMLStyle.NULL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified style to
   ** style the attributes and elements of the XML document.
   **
   ** @param  charset            the requested charset for encoding.
   ** @param  verbosity          indicates the {@link XMLVerbosity} of the
   **                            format.
   */
  public XMLFormat(final Charset charset, final XMLVerbosity verbosity) {
    this(2, charset, verbosity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified style to
   ** style the attributes and elements of the XML document.
   **
   ** @param  indent             the number of spaces used in the indent.
   ** @param  charset            the requested charset for encoding.
   ** @param  verbosity          indicates the {@link XMLVerbosity} of the
   **                            format.
   */
  public XMLFormat(final int indent, final Charset charset, final XMLVerbosity verbosity) {
    this(indent, charset, new XMLStyle.NULL(), verbosity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified style to
   ** style the attributes and elements of the XML document.
   **
   ** @param  charset            the requested charset for encoding.
   ** @param  style              is the {@link XMLStyle} to apply to the format
   **                            object.
   */
  public XMLFormat(final Charset charset, final XMLStyle style) {
    this(2, charset, style);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified style to
   ** style the attributes and elements of the XML document.
   **
   ** @param  charset            the requested charset for encoding.
   ** @param  style              is the {@link XMLStyle} to apply to the format
   **                            object.
   ** @param  verbosity          indicates the {@link XMLVerbosity} of the
   **                            format.
   */
  public XMLFormat(final Charset charset, final XMLStyle style, final XMLVerbosity verbosity) {
    this(2, charset, style, verbosity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified indent size
   ** and the style provided to style the XML document.
   **
   ** @param  indent             the number of spaces used in the indent.
   ** @param  charset            the requested charset for encoding.
   ** @param  style              is the {@link XMLStyle} to apply to the format
   **                            object.
   */
  public XMLFormat(final int indent, final Charset charset, final XMLStyle style) {
    this(indent, null, charset, style);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified indent size
   ** and the style provided to style the XML document.
   **
   ** @param  indent             the number of spaces used in the indent.
   ** @param  charset            the requested charset for encoding.
   ** @param  style              is the {@link XMLStyle} to apply to the format
   **                            object.
   ** @param  verbosity          indicates the {@link XMLVerbosity} of the
   **                            format.
   */
  public XMLFormat(final int indent, final Charset charset, final XMLStyle style, final XMLVerbosity verbosity) {
    this(indent, null, charset, style, verbosity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified indent size
   ** and the text to use in the generated prolog.
   **
   ** @param  indent             the number of spaces used in the indent.
   ** @param  prolog             the prolog for the generated XML document.
   ** @param  charset            the requested charset for encoding.
   ** @param  style              is the {@link XMLStyle} to apply to the format
   **                            object.
   */
  public XMLFormat(final int indent, final String prolog, final Charset charset, final XMLStyle style) {
    this(indent, prolog, charset, style, XMLVerbosity.HIGH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormat</code> object.
   ** <p>
   ** This creates an object that is used to describe how the formatter should
   ** create the XML document. This constructor uses the specified indent size
   ** and the text to use in the generated prolog.
   **
   ** @param  indent             the number of spaces used in the indent.
   ** @param  prolog             the prolog for the generated XML document.
   ** @param  charset            the requested charset for encoding.
   ** @param  style              is the {@link XMLStyle} to apply to the format
   **                            object.
   ** @param  verbosity          indicates the {@link XMLVerbosity} of the
   **                            format.
   */
  public XMLFormat(final int indent, final String prolog, final Charset charset, final XMLStyle style, final XMLVerbosity verbosity) {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.indent    = indent;
    this.prolog    = prolog;
    this.style     = style;
    this.verbosity = verbosity;
    this.encoder   = charset.newEncoder();
    this.codec     = XMLCodec.instance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indent
  /**
   ** This method returns the size of the indent to use for the XML generated.
   ** <p>
   ** The indent size represents the number of spaces that are used for the
   ** indent, and indent of zero means no indenting.
   **
   ** @return                    the number of spaces to used for indenting.
   */
  public int indent() {
    return this.indent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prolog
  /**
   ** This method returns the prolog that is to be used at the start of the
   ** generated XML document.
   ** <p>
   ** This allows a DTD or a version to be specified at the start of a document.
   ** If this returns <code>null</code> then no prolog is written to the start
   ** of the XML document.
   **
   ** @return                    the prolog for the start of the document.
   */
  public String prolog() {
    return this.prolog;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   style
  /**
   ** This is used to acquire the <code>Style</code> for the format.
   ** <p>
   ** If no style has been set a default style is used, which does not modify
   ** the attributes and elements that are used to build the resulting XML
   ** document.
   **
   ** @return                    the style used for this format object.
   */
  public XMLStyle style() {
    return this.style;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verbosity
  /**
   ** This method is used to indicate the preference of verbosity for the
   ** resulting XML.
   ** <p>
   ** This is typically used when default serialization is used. It ensures that
   ** the various types that are serialized are of either high or low verbosity.
   **
   ** @return                    the verbosity preference for the XML.
   */
  public XMLVerbosity verbosity() {
    return this.verbosity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoder
  /**
   ** This method is used to acquire the <code>CharsetEncoder</code> for the
   ** format.
   **
   ** @param  encoder            the character encoding for the XML.
   **
   ** @return                    the <code>XMLFormat</code> for method chaining
   **                            purpose.
   */
  public XMLFormat encoder(final CharsetEncoder encoder) {
    this.encoder = encoder;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoder
  /**
   ** This method is used to acquire the <code>CharsetEncoder</code> for the
   ** format.
   **
   ** @return                    the character encoding for the XML.
   */
  public CharsetEncoder encoder() {
    return this.encoder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   codec
  /**
   ** This method is used to acquire the <code>MimeCodec</code> for the format.
   **
   ** @param  codec              the entity codec for the XML.
   **
   ** @return                    the <code>XMLFormat</code> for method chaining
   **                            purpose.
   */
  public XMLFormat codec(final MimeCodec codec) {
    this.codec = codec;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   codec
  /**
   ** This method is used to acquire the <code>MimeCodec</code> for the
   ** format.
   **
   ** @return                    the entity codec for the XML.
   */
  public MimeCodec codec() {
    return this.codec;
  }
}
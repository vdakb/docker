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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   FilterFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.marshal;

import java.io.Reader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.ObjectCodec;

import com.fasterxml.jackson.core.io.IOContext;

import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;

import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;

////////////////////////////////////////////////////////////////////////////////
// final class FilterFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Custom [@link JsonFactory} implementation for SCIM.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class FilterFactory extends JsonFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:379260136819874125")
  private static final long serialVersionUID = 3892721090521737751L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** A parser that can be used for parsing JSON objects contained within a
   ** filter specification.
   */
  static class Parser extends ReaderBasedJsonParser {

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Parser</code> when input comes as a {@link Reader},
     ** and buffer allocation can be done using default mechanism.
     **
     ** @param  context          the {@link IOContext} of the environment.
     **                          <br>
     **                          Allowed object is {@link IOContext}.
     ** @param  features         the enumeration that defines all on/off
     **                          features for the parser.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  reader           the {@link Reader} providing access to the
     **                          source to parse.
     **                          <br>
     **                          Allowed object is {@link Reader}.
     ** @param  codec            the {@link ObjectCodec} associated with the
     **                          parser, if any.
     **                          Codec is used by JsonParser.readValueAs(Class)
     **                          method (and its variants).
     **                          <br>
     **                          Allowed object is {@link ObjectCodec}.
     ** @param  canonicalizer    the specialized type-safe map from char array
     **                          to String value.
     **                          <br>
     **                          Allowed object is
     **                          {@link CharsToNameCanonicalizer}.
     */
    Parser(final IOContext context, final int features, final Reader reader, final ObjectCodec codec, final CharsToNameCanonicalizer canonicalizer) {
      // ensure inheritance
      super(context, features, reader, codec, canonicalizer);
      // By default the JSON read context is set to JsonStreamContext.TYPE_ROOT,
      // which will require whitespace after any unquoted token (for example, a number).
      // We don't want this restriction when parsing filters, so set the context
      // type to -1, which is effectively "none".
      this._parsingContext = new JsonReadContext(null, null, -1, 1, 0);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FilterFactory</code> REST JSON Factory that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public FilterFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFilterParser
  /**
   ** Create a parser that can be used for parsing JSON objects contained within
   ** a REST filter specification.
   **
   ** @param  reader             the {@link Reader} to use for reading JSON
   **                            content to parse.
   **
   ** @return                    a {@link JsonParser} object.
   **
   ** @throws IOException         on parse error.
   */
  public JsonParser createFilterParser(final Reader reader)
    throws IOException {

    final IOContext context = _createContext(reader, false);
    return new Parser(context, this._parserFeatures, reader, this._objectCodec, this._rootCharSymbols.makeChild(this._factoryFeatures));
  }
}
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
    Subsystem   :   Generic REST Library

    File        :   FilterParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.utility;

import java.io.Reader;

import com.fasterxml.jackson.core.ObjectCodec;

import com.fasterxml.jackson.core.io.IOContext;

import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;

import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;

////////////////////////////////////////////////////////////////////////////////
// class FilterParser
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A parser that can be used for parsing JSON objects contained within a REST
 ** filter specification.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FilterParser extends ReaderBasedJsonParser {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FilterParser</code> when input comes as a
   ** {@link Reader}, and buffer allocation can be done using default mechanism.
   **
   ** @param  context            the {@link IOContext} of the environment.
   **                            <br>
   **                            Allowed object is {@link IOContext}.
   ** @param  features           the enumeration that defines all on/off
   **                            features for the parser.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  reader             the {@link Reader} providing access to the
   **                            source to parse.
   **                            <br>
   **                            Allowed object is {@link Reader}.
   ** @param  codec              the {@link ObjectCodec} associated with the
   **                            parser, if any.
   **                            Codec is used by JsonParser.readValueAs(Class)
   **                            method (and its variants).
   **                            <br>
   **                            Allowed object is {@link ObjectCodec}.
   ** @param  canonicalizer      the specialized type-safe map from char array
   **                            to String value.
   **                            <br>
   **                            Allowed object is
   **                            {@link CharsToNameCanonicalizer}.
   */
  public FilterParser(final IOContext context, final int features, final Reader reader, final ObjectCodec codec, final CharsToNameCanonicalizer canonicalizer) {
    // ensure inheritance
    super(context, features, reader, codec, canonicalizer);
    // By default the JSON read context is set to JsonStreamContext.TYPE_ROOT,
    // which will require whitespace after any unquoted token (for example, a number).
    // We don't want this restriction when parsing a REST filter , so set the
    // context type to -1, which is effectively "none".
    this._parsingContext = new JsonReadContext(null, null, -1, 1, 0);
  }
}
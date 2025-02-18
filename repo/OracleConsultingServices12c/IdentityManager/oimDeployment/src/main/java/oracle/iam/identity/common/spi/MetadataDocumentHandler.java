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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   MetadataDocumentHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MetadataDocumentHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.common.spi;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

////////////////////////////////////////////////////////////////////////////////
// class MetadataDocumentHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>MetadataDocumentHandler</code> implemants the basic functionality to
 ** handle errors in Metadata Document creation.
 ** <p>
 ** This class provides default implementations for the callbacks of the SAX2
 ** handler class {@link org.xml.sax.ErrorHandler ErrorHandler}.
 **
 ** @see     org.xml.sax.ErrorHandler
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MetadataDocumentHandler implements ErrorHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  boolean           validationError = false;
  SAXParseException parseException = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MetadataDocumentHandler</code> handler that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MetadataDocumentHandler() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ErrorHandler)
  /**
   ** Receive notification of a non-recoverable error.
   ** <p>
   ** <strong>There is an apparent contradiction between the documentation for
   ** this method and the documentation for {@link org.xml.sax.ContentHandler#endDocument}.
   ** Until this ambiguity is resolved in a future major release, clients should
   ** make no assumptions about whether endDocument() will or will not be
   ** invoked when the parser has reported a fatalError() or thrown an
   ** exception.</strong>
   ** <p>
   ** This corresponds to the definition of "fatal error" in section 1.2 of the
   ** W3C XML 1.0 Recommendation.  For example, a parser would use this callback
   ** to report the violation of a well-formedness constraint.
   ** <p>
   ** The application must assume that the document is unusable after the parser
   ** has invoked this method, and should continue (if at all) only for the sake
   ** of collecting additional error messages: in fact, SAX parsers are free to
   ** stop reporting any other events once this method has been invoked.
   **
   ** @param  exception          the error information encapsulated in a SAX
   **                            parse exception.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    SAXParseException
   */
  public void fatalError(final SAXParseException exception)
    throws SAXException {

    this.validationError = true;
    this.parseException = exception;
    throw new SAXException("Fatal Failure in xml validation w.r.t. xsd", this.parseException);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ErrorHandler)
  /**
   ** Receive notification of a recoverable error.
   ** <p>
   ** This corresponds to the definition of "error" in section 1.2 of the W3C
   ** XML 1.0 Recommendation. For example, a validating parser would use this
   ** callback to report the violation of a validity constraint. The default
   ** behaviour is to take no action.
   ** <p>
   ** The SAX parser must continue to provide normal parsing events after
   ** invoking this method: it should still be possible for the application to
   ** process the document through to the end. If the application cannot do so,
   ** then the parser should report a fatal error even if the XML recommendation
   ** does not require it to do so.
   ** <p>
   ** Filters may use this method to report other, non-XML errors as well.
   **
   ** @param  exception          the error information encapsulated in a SAX
   **                            parse exception.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    SAXParseException
   */
  public void error(final SAXParseException exception)
    throws SAXException {

    this.validationError = true;
    this.parseException = exception;
    throw new SAXException("Failure in xml validation w.r.t. xsd", this.parseException);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ErrorHandler)
  /**
   ** Receive notification of a warning.
   ** <p>
   ** SAX parsers will use this method to report conditions that are not errors
   ** or fatal errors as defined by the XML recommendation. The default
   ** behaviour is to take no action.
   ** <p>
   ** The SAX parser must continue to provide normal parsing events after
   ** invoking this method: it should still be possible for the application to
   ** process the document through to the end.
   ** <p>
   ** Filters may use this method to report other, non-XML warnings as well.
   **
   ** @param exception           the warning information encapsulated in a SAX
   **                            parse exception.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    SAXParseException
   */
  public void warning(final SAXParseException exception)
    throws SAXException {

    // intentionally left blank
  }
}
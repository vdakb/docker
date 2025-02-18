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

    File        :   DSMLInput.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLInput.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.io.Reader;
import java.io.InputStream;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;

import javax.xml.transform.Source;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.xml.StreamInput;

////////////////////////////////////////////////////////////////////////////////
// class DSMLInput
// ~~~~~ ~~~~~~~~~
/**
 ** A Utility class to read a DSML document from a file into the DOM tree.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DSMLInput extends StreamInput {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DSMLInput</code> from a {@link Reader}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link XMLStreamReader} interface allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  reader             the {@link Reader} to read from.
   **
   ** @throws XMLStreamException in case {@link StreamInput} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  public DSMLInput(final Loggable loggable, final Reader reader)
    throws XMLStreamException {

    // ensure inheritance
    super(loggable, reader);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DSMLInput</code> from a {@link Source}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link XMLStreamReader} interface allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  source             the {@link Source} to read from.
   **
   ** @throws XMLStreamException in case {@link StreamInput} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  public DSMLInput(final Loggable loggable, final Source source)
    throws XMLStreamException {

    // ensure inheritance
    super(loggable, source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>DSMLInput</code> from a {@link InputStream}.
   ** <p>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   ** <p>
   ** The {@link XMLStreamReader} interface allows forward, read-only access
   ** to XML. It is designed to be the lowest level and most efficient way to
   ** read XML data.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  stream             the {@link InputStream} to read from.
   **
   ** @throws XMLStreamException in case {@link StreamInput} is not able to
   **                            create an appropriate {@link XMLStreamReader}.
   */
  public DSMLInput(final Loggable loggable, final InputStream stream)
    throws XMLStreamException {

    // ensure inheritance
    super(loggable, stream);
  }
}
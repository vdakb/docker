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

    File        :   ListResponseStream.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ListResponseStream.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.response;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.StreamingOutput;

import oracle.hst.platform.rest.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// abstract class ListResponseStream
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Interface for streaming list/query results using the REST
 ** {@link ListResponse} container to a StreamingOutput.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class ListResponseStream<T> implements StreamingOutput {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a REST <code>ListResponseStream</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ListResponseStream() {
   // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write (StreamingOutput)
  /**
   ** Called to write the message body.
   **
   ** @param  stream             the stream to write to.
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   */
  @Override
  public final void write(final OutputStream stream)
    throws IOException {

    final ListResponseWriter<T> handler = new ListResponseWriter<T>(stream).start();
    write(handler);
    handler.end();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Start streaming the contents of the list response.
   ** <br>
   ** The list response will be considered complete upon return.
   **
   ** @param  writer             the list response writer stream used to stream
   **                            back elements of the list response.
   **                            <br>
   **                            Allowed object is {@link ListResponseWriter}
   **                            for type <code>T</code>.
   **
   ** @throws IOException        if an exception occurs while writing to the
   **                            output stream.
   */
  public abstract void write(final ListResponseWriter<T> writer)
    throws IOException;
}
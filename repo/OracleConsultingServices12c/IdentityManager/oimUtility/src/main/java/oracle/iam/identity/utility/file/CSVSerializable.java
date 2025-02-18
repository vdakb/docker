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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVSerializable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVSerializable.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;

import java.io.IOException;
import java.io.EOFException;

////////////////////////////////////////////////////////////////////////////////
// interface CSVSerializable
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Declares method to marshal and unmarshal content to and from a CSV file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public interface CSVSerializable {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Extracts a record from the specified CSV reader.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling.
   ** @param  column             the array of string with the contont.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @return                    a {@link Map} with the attribute names as the
   **                            keys and the values extractd from the file
   **                            asssociated to the appropriate key.
   **
   ** @throws EOFException       at end of file after all the fields have
   **                            been read.
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  Map<String, Object> read(final CSVProcessor processor, final String[] column, final boolean applyTransformer)
    throws EOFException
    ,      CSVException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Write the a part to the specified CSV file writer.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling.
   ** @param  writer             the {@link CSVWriter} that receive to
   **                            output.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the transformation cannot be applied.
   */
  void write(final CSVProcessor processor, final CSVWriter writer, final boolean applyTransformer)
    throws IOException
    ,      CSVException;
}
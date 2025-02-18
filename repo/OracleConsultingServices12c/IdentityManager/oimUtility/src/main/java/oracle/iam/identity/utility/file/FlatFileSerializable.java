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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Flat File Stream Facilities

    File        :   FlatFileSerializable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileSerializable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;

import java.io.IOException;
import java.io.EOFException;
import java.io.Serializable;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// interface FlatFileSerializable
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Declares method to marshal and unmarshal content to and from a flat file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public interface FlatFileSerializable extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5046711843604102535")
  static final long serialVersionUID = -3898153324156635697L;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Extracts a record from the specified flat file reader.
   **
   ** @param  processor          the {@link FlatFileProcessor} to control the
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
   ** @throws FlatFileException       some problem reading the file, possibly
   **                            malformed data.
   */
  Map<String, Object> read(final FlatFileProcessor processor, final String[] column, final boolean applyTransformer)
    throws EOFException
    ,      TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Write the a part to the specified flat file writer.
   **
   ** @param  processor          the {@link FlatFileProcessor} to control the
   **                            marshalling.
   ** @param  writer             the {@link FlatFileWriter} that receive to
   **                            output.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already closed.
   ** @throws FlatFileException       if the transformation cannot be applied.
   */
  void write(final FlatFileProcessor processor, final FlatFileWriter writer, final boolean applyTransformer)
    throws IOException
    ,      TaskException;
}
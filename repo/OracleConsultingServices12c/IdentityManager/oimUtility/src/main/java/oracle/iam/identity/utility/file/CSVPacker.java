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

    File        :   CSVPacker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVPacker.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.io.IOException;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.FileSystem;

import oracle.iam.identity.utility.resource.CSVBundle;

////////////////////////////////////////////////////////////////////////////////
// final class CSVPacker
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Packs a CSV file removing excess space and commas.
 ** <br>
 ** Usage
 ** <pre>
 **   java oracle.iam.identity.utility.file.CSVPacker somefile.csv
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVPacker {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String THIS = ClassUtility.shortName(CSVPacker.class);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Pack a CSV file.
   **
   ** @param  file               CSV file to be packed to remove excess space
   **                            and quotes.
   ** @param  encoding           the name of a supported charset.
   ** @param  separator          field separator character, usually
   **                            <code>','</code> in North America,
   **                            <code>';'</code> in Europe and sometimes
   **                            <code>'\t'</code> for tab.
   ** @param quote               char to use to enclose fields containing a
   **                            separator, usually <code>'\"'</code>.
   **
   ** @throws IOException        in case the streams cannot be closed
   ** @throws CSVException       some problems in opening the file.
   */
  public CSVPacker(final File file, final String encoding, final char separator, final char quote)
    throws IOException
    ,      CSVException {

    File          tmp = null;
    CSVReader inp = null;
    CSVWriter out = null;
    try {
      inp = new CSVReader( new InputStreamReader(new FileInputStream(file), encoding)
                         , separator
                         , quote
                         , false // multiline
                         , true  // trim
                         );
      tmp = FileSystem.createTempFile(CSVOperation.TMPPREFIX, CSVOperation.TMPEXTENSION, file );
      out = new CSVWriter( new OutputStreamWriter(new FileOutputStream(tmp), encoding)
                         , 0 // minimal quotes
                         , separator
                         , quote
                         , true // trim
                         );
    }
    catch (IOException e){
      throw new CSVException(SystemError.GENERAL, e);
    }

    try {
      while (true) {
        String s = inp.get();
        // null means end of line
        out.put(s);
      }
    }
    catch (EOFException e) {
      inp.close();
      out.close();
      file.delete();
      tmp.renameTo(file);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to pack a CSV file.
   ** <br>
   ** Packs one csv file whose name is on the command line. Must have extension
   ** <code>.csv</code>
   ** <br>
   ** Usage:
   ** <pre>
   **   java oracle.iam.identity.foundation.filePacker somefile.csv
   ** </pre>
   **
   ** @param  args name of csv file to remove excess quotes and space
   **
   ** @throws CSVException       if commandline parameters incomplete specified.
   */
  public static void main(String[] args)
    throws CSVException {

    if (args.length != 2)
      throw new CSVException(CSVError.FILENAME_MISSING, THIS);

    String filename = args[0];
    if (!filename.endsWith(".csv"))
      throw new CSVException(CSVError.FILEEXTENSION_IS_BAD, THIS);

    try {
      new CSVPacker(new File(filename), args[1], SystemConstant.COMMA, SystemConstant.QUOTE);
    }
    catch (Exception e) {
      String[] arguments = {THIS, "pack", filename };
      System.err.println(CSVBundle.format(CSVError.EXECUTION_FAILED, arguments));
      System.err.println(e.getMessage());
    }
  }
}
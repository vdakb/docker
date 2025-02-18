/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   TestClient.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestClient.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.service;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

////////////////////////////////////////////////////////////////////////////////
// class Password
// ~~~~~ ~~~~~~~~
/**
 ** <code>Password</code> fetch a password credential from a file.
 ** <p>
 ** The file has no format to declare the password credential.
 ** <br>
 ** <b>Note</b>:
 ** The one and only information is a single line with the plain text password
 ** credential to return.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Password {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Read a password from a file.
   **
   ** @param  path               the file system path to the password file to
   **                            extract the credential from.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the passord credential fetched from the file.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static char[] read(final String path) {
    String         passwd = null;
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(path));
      passwd = reader.readLine();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
    }
    return passwd.toCharArray();
  }
}
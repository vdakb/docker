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

    System      :   Foundation Shared Library
    Subsystem   :   Common shared facilities

    File        :   CommandManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CommandManager.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemError;
import oracle.hst.foundation.SystemException;

////////////////////////////////////////////////////////////////////////////////
// class CommandManager
// ~~~~~ ~~~~~~~~~~~~~~
/**
 **  This class executes a given script/commnand using Runtime.exec().
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CommandManager {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String CLASSPATH     = "-classpath";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   java
  /**
   ** Execute java command.
   **
   ** @param  jarName            the java archive containing the class to
   **                            execute.
   ** @param  className          the class to execute.
   ** @param  arguments          parameter passed to the class.
   **
   ** @return                    the output of the executed java command or
   **                            <code>null</code> if the command has no output
   **                            generated.
   **
   ** @throws SystemException    if the system property for
   **                            {@link SystemConstant#SYSTEM_JAVA_HOME} isn't
   **                            set.
   */
  public static String java(String jarName, String className, String arguments)
    throws SystemException {

    String javaHome = System.getProperty(SystemConstant.SYSTEM_JAVA_HOME);
    if (javaHome == null)
      throw new SystemException(SystemError.JAVAHOME_MISSING);

    StringBuilder output      = new StringBuilder();
    String       command     = System.getProperty(javaHome) + File.separatorChar + "bin" + File.separatorChar + "java";
    String[]     commandLine = { command, CLASSPATH, jarName, className, arguments };
    exec(commandLine, null, output);

    return (output != null) ? output.toString() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exec
  /**
   ** Executes a command at operating system level.
   **
   ** @param  command            array containing the command to call and its
   **                            arguments.
   ** @param  environment        array of strings, each element of which has
   **                            environment variable settings in the format
   **                            <i>name</i>=<i>value</i>, or <code>null</code>
   **                            if the subprocess should inherit the
   **                            environment of the current process.
   ** @param  resultBuffer       the data piped from the standard output stream
   **                            of the process represented by the subprocess.
   **
   ** @return                    the exit value of the subprocess.
   **                            By convention, <code>0</code> indicates normal
   **                            termination.
   **
   ** @throws SystemException    if the operation fails for any reason.
   */
  public static int exec(String[] command, String[] environment, StringBuilder resultBuffer)
    throws SystemException {

    int status;
    BufferedReader reader  = null;
    Process        process = null;
    Runtime        runtime = Runtime.getRuntime();
    try {
      process = runtime.exec(command, environment);
      // get the input stream of the subprocess.
      reader  = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      if (resultBuffer != null) {
        resultBuffer.setLength(0);
        for (line = reader.readLine(); line != null; line = reader.readLine())
          resultBuffer.append(line).append(SystemConstant.LINEBREAK);
      }
      else {
        line = reader.readLine();
        while (line != null)
          line = reader.readLine();
      }
      // causes the current thread to wait, if necessary, until the subprocess
      // has terminated.
      status = process.waitFor();
    }
    catch (InterruptedException e) {
      throw new SystemException(SystemError.COMMAND_WAIT, command[0]);
    }
    catch (IOException exc) {
      throw new SystemException(SystemError.COMMAND_EXEC, command[0]);
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (IOException e) {
          ;
        }
      }
    }
    return status;
  }
}
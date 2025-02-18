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

    File        :   ChallengePassword.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ChallengePassword.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import java.util.Arrays;

import java.io.Console;

import oracle.hst.deployment.ServiceError;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;

import oracle.hst.deployment.AbstractFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ChallengePassword
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Basic task operations to challenge for password input.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 */
public class ChallengePassword extends    AbstractFrontend
                               implements InputHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String PROMPT = "    [input]%s \n";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ChallengePassword</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ChallengePassword() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleInput (InputHandler)
  /**
   ** Handle the request encapsulated in the argument.
   ** <p>
   ** Precondition: the request.getPrompt will return a non-null value.
   ** <br>
   ** Postcondition: request.getInput will return a non-null value,
   ** request.isInputValid will return true.
   **
   ** @param  request             the request to be processed.
   **
   ** @throws BuildException       if the input cannot be read from the console
   */
  @Override
  public void handleInput(final InputRequest request) {
    Console console = System.console();
    if (console == null)
      throw new BuildException(ServiceResourceBundle.string(ServiceError.CONSOLE_UNAVAILABLE));

    char[] pwd = null;
    try {
      do {
        pwd = console.readPassword(PROMPT, new Object[] { request.getPrompt() });
        request.setInput(new String(pwd));
        Arrays.fill(pwd, ' ');
      } while (!request.isInputValid());
    }
    catch (Exception e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CONSOLE_INPUT, e));
    }
  }
}
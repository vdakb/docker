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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Main.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs;

import java.util.Scanner;

import java.io.IOException;

import oracle.hst.foundation.logging.TableFormatter;
import oracle.hst.foundation.utility.CommandLine;

import oracle.iam.system.simulation.ServiceMessage;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.efbs.service.Server;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** The <code>Main</code> command-line tool enables you to start a HTTP service
 ** that simulates eFBS SCIM interface for debugging purpose.
 ** <p>
 ** This interface accepts HTTP request and duimps them to the system out stream
 ** only.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Debugger {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static int    port = 8011;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Entry point to the unit test.
   **
   ** @param  args               the array ouf command line arguments passed in.
   */
  public static void main(final String[] args) {
    parseCommandLine(args);
    try {
      new Debugger().execute();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(2);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseCommandLine
  /**
   ** Creates a {@link CommandLine} object, passing in the argument string, and
   ** makes repeated calls to {@link CommandLine#nextOption()} to set all or
   ** some of the command-line option member variables.
   **
   ** @param  args               the argument string to parse
   */
  private static void parseCommandLine(final String[] args) {
    final CommandLine.Option[] opts = new CommandLine.Option[6];
    opts[0] = new CommandLine.Option("port",      CommandLine.ARGUMENT_REQUIRED, null, 'p');

    final CommandLine cli = new CommandLine("eFBS SCIM Service Simulator", args, "-p:?", opts);

    int option = cli.nextOption();
    // keep parsing until we run out of options
    while (option != -1) {
      switch (option) {
        case 'p' : port = Integer.parseInt(cli.optionArgument());
                   break;
        // the value (?) can be requested on the command line, but is also
        // returned when CommandLine encounters an error
        case '?' : usage();
                   System.exit(0);
                   break;
        default  : break;
      }
      option = cli.nextOption();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Prints the usage of this class to stdout.
   */
  private static void usage() {
    System.out.println("Usage: java -cp <path to>/ocs-simulator.jar:<path to>/hst-foundation.jar oracle.iam.system.simulation.efbs.Main -h hostname -l listener -s service -u username -w password [-p serviceport]");
    System.out.println("Where is:");
    System.out.println("  <path to>     ... the directory of the Java Archives needed to acomplish");
    System.out.println("                    the runtime classpath");
    System.out.println("");
    System.out.println("  --port     -p ... the port number used to expose the HTTP Service");
    System.out.println("                    Defaults to port 8011");
    System.out.println("  --help     -? ... this information");
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  private void execute()
    throws Exception {
    
    Server.start(port);

    // cache context
    final String context = String.format("http://%s:%d", exec("hostname").trim(), port);
    TableFormatter formatter = new TableFormatter();
    formatter.header(ServiceBundle.string(ServiceMessage.CATEGORY_LABEL)).header(ServiceBundle.string(ServiceMessage.PROPERTY_LABEL)).header(ServiceBundle.string(ServiceMessage.PROPERTY_VALUE));
    formatter.row().column(ServiceBundle.string(ServiceMessage.SERVICES_CATEGORY)).column("").column("");
    formatter.row().column("").column(Server.CONTEXT_USER).column(String.format("%s%s", context, Server.contextUserURI()));
    formatter.row().column("").column(Server.CONTEXT_SCHEMA).column(String.format("%s%s", context, Server.contextSchemaURI()));
    formatter.row().column("").column(Server.CONTEXT_RESOURCE).column(String.format("%s%s", context, Server.contextResourceTypeURI()));
    System.out.println("\n");
    formatter.print();
    System.out.println("\nReady for requests");

    Runtime.getRuntime().addShutdownHook(
      new Thread() {
        @Override
        public void run() {
          System.out.println("Shutdown hook ran!");
          Server.stop();
        }
      }
    );
  }

  private static String exec(final String command)
    throws IOException {

    try (Scanner s = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A")) {
      return s.hasNext() ? s.next() : "";
    }
  }
}
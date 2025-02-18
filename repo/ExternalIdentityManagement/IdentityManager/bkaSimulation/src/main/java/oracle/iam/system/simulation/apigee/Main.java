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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   Main.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Main.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee;

import java.util.Scanner;

import java.io.IOException;

import oracle.hst.foundation.utility.CommandLine;
import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.iam.system.simulation.ServiceMessage;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.dbs.DatabaseResource;

import oracle.iam.system.simulation.apigee.service.Server;

import oracle.iam.system.simulation.apigee.persistence.Provider;

////////////////////////////////////////////////////////////////////////////////
// class Main
// ~~~~~ ~~~~
/**
 ** The <code>Main</code> command-line tool enables you to start a HTTP service
 ** that simulates Google API Gateway REST interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Main {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static int    port = 8015;

  static String flavor   = "orcl";
  static String hostname;
  static int    listener = 7021;
  static String service;
  static String network;
  static String username;
  static String password;

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
    if (StringUtility.isEmpty(flavor)) {
      System.err.println("You must specify the database flavor");
      usage();
      System.exit(-1);
    }
    if (StringUtility.isEmpty(hostname)) {
      System.err.println("You must specify the hostname the principal will connect to");
      usage();
      System.exit(-2);
    }
    if (StringUtility.isEmpty(service)) {
      System.err.println("You must specify the service the principal will connect to");
      usage();
      System.exit(-3);
    }
    if (StringUtility.isEmpty(username)) {
      System.err.println("You must specify the username of the principal");
      usage();
      System.exit(-4);
    }
    if (StringUtility.isEmpty(password)) {
      System.err.println("You must specify the password of the principal");
      usage();
      System.exit(-5);
    }
    if (StringUtility.isEmpty(network)) {
      System.out.println("Using default network interface binding");
      network = exec("hostname");
    }
    try {
      new Main().execute();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(-99);
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
    final CommandLine.Option[] opts = new CommandLine.Option[8];
    opts[ 0] = new CommandLine.Option("flavor",    CommandLine.ARGUMENT_REQUIRED, null, 'f');
    opts[ 1] = new CommandLine.Option("hostname",  CommandLine.ARGUMENT_REQUIRED, null, 'h');
    opts[ 2] = new CommandLine.Option("listener",  CommandLine.ARGUMENT_REQUIRED, null, 'l');
    opts[ 3] = new CommandLine.Option("service",   CommandLine.ARGUMENT_REQUIRED, null, 's');
    opts[ 4] = new CommandLine.Option("username",  CommandLine.ARGUMENT_REQUIRED, null, 'u');
    opts[ 5] = new CommandLine.Option("password",  CommandLine.ARGUMENT_REQUIRED, null, 'w');
    opts[ 6] = new CommandLine.Option("interface", CommandLine.ARGUMENT_REQUIRED, null, 'i');
    opts[ 7] = new CommandLine.Option("port",      CommandLine.ARGUMENT_REQUIRED, null, 'p');

    final CommandLine cli = new CommandLine("Google API Gatteway Simulator", args, "-f:h:l:s:u:w:i:p:?", opts);

    int option = cli.nextOption();
    // keep parsing until we run out of options
    while (option != -1) {
      switch (option) {
        case 'f' : flavor   = cli.optionArgument();
                   break;
        case 'h' : hostname = cli.optionArgument();
                   break;
        case 'l' : listener = Integer.parseInt(cli.optionArgument());
                   break;
        case 's' : service  = cli.optionArgument();
                   break;
        case 'u' : username = cli.optionArgument();
                   break;
        case 'w' : password = cli.optionArgument();
                   break;
        case 'i' : network  = cli.optionArgument();
                   break;
        case 'p' : port     = Integer.parseInt(cli.optionArgument());
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
    System.out.println("Usage: java -cp <path to>/ocs-simulator.jar:<path to>/hst-foundation.jar oracle.iam.system.simulation.apigee.Main -h hostname -l listener -s service -u username -w password [-p serviceport]");
    System.out.println("Where is:");
    System.out.println("  <path to>      ... the directory of the Java Archives needed to acomplish");
    System.out.println("                     the runtime classpath");
    System.out.println("");
    System.out.println("  --flavor    -f ... the Database flavor");
    System.out.println("                     Defaults to orcl; possible option are [orcl | psql | mysql]");
    System.out.println("  --hostname  -h ... the host name or IP address of the Database Listener");
    System.out.println("  --listener  -l ... the port number the Database Listener is awaiting requests");
    System.out.println("                     Defaults to port 7021 for Oracle RDBMS");
    System.out.println("  --service   -s ... the Service Name of the database to connect");
    System.out.println("  --username  -u ... the username of the principal to connect");
    System.out.println("  --password  -w ... the password of the principal to connect");
    System.out.println("  --interface -i ... the network interface to expose the HTTP Service");
    System.out.println("  --port      -p ... the port number used to expose the HTTP Service");
    System.out.println("                     Defaults to port 8011");
    System.out.println("  --help      -? ... this information");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  private void execute()
    throws Exception {

    Provider.instance.aquire(flavor, DatabaseResource.build(hostname, listener, service, username, password));
    Server.start(port);
    // cache context
    final String context = String.format("http://%s:%d", network.trim(), port);
    TableFormatter formatter = new TableFormatter();
    formatter.header(ServiceBundle.string(ServiceMessage.CATEGORY_LABEL)).header(ServiceBundle.string(ServiceMessage.PROPERTY_LABEL)).header(ServiceBundle.string(ServiceMessage.PROPERTY_VALUE));
    formatter.row().column(ServiceBundle.string(ServiceMessage.DATABASE_CATEGORY)).column("").column("");
    formatter.row().column("").column(ServiceBundle.string(ServiceMessage.DATABASE_HOSTNAME)).column(hostname);
    formatter.row().column("").column(ServiceBundle.string(ServiceMessage.DATABASE_LISTENER)).column(listener);
    formatter.row().column("").column(ServiceBundle.string(ServiceMessage.DATABASE_SERVICE)).column(service);
    formatter.row().column("").column(ServiceBundle.string(ServiceMessage.DATABASE_USERNAME)).column(username);
    formatter.row().column(ServiceBundle.string(ServiceMessage.SERVICES_CATEGORY)).column("").column("");
    formatter.row().column("").column(Server.CONTEXT_USER).column(String.format("%s%s", context, Server.contextUserURI()));
    formatter.row().column("").column(Server.CONTEXT_TENANT).column(String.format("%s%s", context, Server.contextTenantURI()));
    System.out.println("\n");
    formatter.print();
    System.out.println("\nReady for requests");

    Runtime.getRuntime().addShutdownHook(
      new Thread() {
        @Override
        public void run() {
          System.out.println("Shutdown hook ran!");
          Server.stop();
          Provider.instance.release();
        }
      }
    );
  }

  private static String exec(final String command) {
    try (
      final Scanner s = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A")) {
      return s.hasNext() ? s.next() : "";
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
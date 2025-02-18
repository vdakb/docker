package oracle.iam.identity.sap.diagnostic;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import oracle.hst.foundation.SystemConsole;
import oracle.hst.foundation.utility.CommandLine;

import oracle.iam.identity.sap.control.DestinationProvider;
import oracle.iam.identity.sap.control.Feature;
import oracle.iam.identity.sap.control.Resource;

public class AbstractUseCase {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static File                configFile;
  static File                featureFile;
  static String[]            argument     = new String[2];

  static String              timeStamp    = "20141015152120";
  static String              timeZone     = "CET";
  final boolean              isUM         = true;
  static boolean             incremental  = false;

  static SystemConsole       console      = new SystemConsole(AbstractUseCase.class.getSimpleName());
  static Map<String, String> translate    = new HashMap<String, String>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Feature    feature;
  protected final Resource   resource;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    translate.put(DestinationProvider.JCO_ASHOST,    Resource.APPLICATION_SERVER_HOST);
    translate.put(DestinationProvider.JCO_GROUP,     Resource.APPLICATION_SERVER_GROUP);
    translate.put(DestinationProvider.JCO_SAPROUTER, Resource.APPLICATION_SERVER_ROUTE);
    translate.put(DestinationProvider.JCO_MSHOST,    Resource.MESSAGE_SERVER_HOST);
    translate.put(DestinationProvider.JCO_MSSERV,    Resource.MESSAGE_SERVER_PORT);
    translate.put(DestinationProvider.JCO_GWHOST,    Resource.GATEWAY_SERVER_HOST);
    translate.put(DestinationProvider.JCO_GWSERV,    Resource.GATEWAY_SERVICE_NAME);
    translate.put(DestinationProvider.JCO_USER,      Resource.PRINCIPAL_NAME);
    translate.put(DestinationProvider.JCO_PASSWD,    Resource.PRINCIPAL_PASSWORD);
    translate.put(DestinationProvider.JCO_CLIENT,    Resource.CLIENT_LOGON);
    translate.put(DestinationProvider.JCO_R3NAME,    Resource.SYSTEM_NAME);
    translate.put(DestinationProvider.JCO_SYSNR,     Resource.SYSTEM_NUMBER);
    translate.put(DestinationProvider.JCO_LANG,      Resource.SYSTEM_LANGUAGE);
    translate.put("jco.server.system",               Resource.MASTER_SYSTEM_NAME);
    translate.put("jco.server.unicode",              Resource.APPLICATION_SERVER_UNICODE);
    translate.put(DestinationProvider.JCO_SNC_MODE,  Resource.SECURE_SOCKET);
    translate.put(DestinationProvider.JCO_TRACE,     Feature.TRACE_ENABLED);
    translate.put("jco.trace_level",                 Feature.TRACE_LEVEL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Convenience Constructor.
   **
   ** @param  resource           {@link File} source of property configuration
   **                            to read.
   */
  protected AbstractUseCase(final Resource resource, final Feature feature) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.resource = resource;
    this.feature  = feature;
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
  protected static void parseCommandLine(final String[] args) {
    final CommandLine.Option[] opts = new CommandLine.Option[7];
    opts[ 0] = new CommandLine.Option("config",      CommandLine.ARGUMENT_REQUIRED, null, 'c');
    opts[ 1] = new CommandLine.Option("feature",     CommandLine.ARGUMENT_REQUIRED, null, 'f');
    opts[ 2] = new CommandLine.Option("timestamp",   CommandLine.ARGUMENT_REQUIRED, null, 't');
    opts[ 3] = new CommandLine.Option("timezone",    CommandLine.ARGUMENT_OPTIONAL, null, 'z');
    opts[ 4] = new CommandLine.Option("read",        CommandLine.ARGUMENT_OPTIONAL, null, 'r');
    opts[ 5] = new CommandLine.Option("incremental", CommandLine.ARGUMENT_NO,       null, 'i');
    opts[ 6] = new CommandLine.Option("help",        CommandLine.ARGUMENT_NO,       null, '?');
    final CommandLine cli = new CommandLine("ChangeLog", args, "-c:f:r:t:z:i?", opts);

    int index  = 0;
    int option = cli.nextOption();
    // keep parsing until we run out of options
    while (option != -1) {
      switch (option) {
        case 'c' : configFile  = new File(cli.optionArgument());
                   break;
        case 'f' : featureFile = new File(cli.optionArgument());
                   break;
        case 't' : timeStamp   = cli.optionArgument();
                   break;
        case 'z' : timeZone    = cli.optionArgument();
                   break;
//        case 'r' : rfcTable    = cli.optionArgument();
//                   break;
        case 'i' : incremental = true;
                   break;
		    // the value (?) can be requested on the command line, but is also
        // returned when CommandLine encounters an error
        case '?' : usage();
                   System.exit(0);
                   break;
        default  : if (cli.index() < args.length)
                     argument[index++] = args[cli.index()];
                   break;
      }
      option = cli.nextOption();
    };
    if (configFile == null) {
      System.err.println("Connector configuration file required");
      System.exit(1);
    }

    if (featureFile == null) {
      System.err.println("Connector feature file required");
      System.exit(1);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Prints the usage of this class to stdout.
   */
  private static void usage() {
    System.out.println("Usage: java -Djava.library.path=./sap -cp <path to>/hst-foundation.jar:<path to>/oim-foundation.jar oracle.iam.identity.sap.diagnostic.ChangeLog -p propertyfile -f featurefile}");
    System.out.println("Where is:");
    System.out.println("  <path to>        ... the directory of the Java Archives needed to accomplish");
    System.out.println("                       the runtime classpath");
    System.out.println("");
    System.out.println("  --property    -p ... the property configuration used to connect to the");
    System.out.println("                       SAP System");
    System.out.println("  --feature     -f ... the descriptor with the feature description of the");
    System.out.println("                       SAP System");
    System.out.println("  --incremental -i ... the timestamp to query the change log of the");
    System.out.println("                       SAP System");
    System.out.println("  --timestamp   -t ... the timestamp to query the change log of the");
    System.out.println("                       SAP System");
    System.out.println("  --timezone    -z ... the timezone to query the change log of the");
    System.out.println("                       SAP System");
    System.out.println("  --read        -r ... the rfc table object to fetch the changes from");
    System.out.println("  --help        -? ... this information");
  }
}
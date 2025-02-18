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
    Subsystem   :   Common Shared LDAP Facilities

    File        :   LDAPImport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPImport.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;

import java.io.File;

import javax.naming.Binding;
import javax.naming.NamingException;

import javax.naming.directory.Attributes;

import javax.naming.ldap.LdapContext;

import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.CommandLine;
import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.ldap.DirectoryName;
import oracle.iam.identity.foundation.ldap.DirectoryFeature;
import oracle.iam.identity.foundation.ldap.DirectoryFileReader;
import oracle.iam.identity.foundation.ldap.DirectoryFileWriter;

import oracle.iam.identity.utility.resource.LDAPBundle;

////////////////////////////////////////////////////////////////////////////////
// class LDAPImport
// ~~~~~ ~~~~~~~~~~
/**
 ** The LDAPImport class populates a directory server back end with data read
 ** from an LDIF or DSML file.
 ** <p>
 ** In most cases, using LDAPImport is significantly faster than adding entries
 ** by using standard ldapmodify.
 ** <p>
 ** The <code>LDAPImport</code> tool input does not include operational data of
 ** the directory itself for example, cn=subschemasubentry, cn=catalogs, and
 ** cn=changelog entries.
 ** <p>
 ** LDAPImport -h hostname -D "binddn" -w password -b "basedn" [-p ldap_port] [-A] [-L|-X] inputfile {[attributes] [omit]|-f input_file}
 ** <table summary="">
 ** <tr><th>Short</th><th>Long</th><th>Allowed Values</th><th>Description</th><th>Mandatory</th></tr>
 ** <tr><td>-h</td><td>--hostname</td><td></td><td>The host name or IP address of the Directory Service.</td><td>yes</td></tr>
 ** <tr><td>-D</td><td>--bindDN</td><td>The DN of the Directory Service user needed to bind to the directory (for example, cn=orcladmin).</td><td>yes</td></tr>
 ** <tr><td>-w</td><td>--password</td><td>The password needed to bind to the Directory Service</td><td>yes</td></tr>
 ** <tr><td>-b</td><td>--baseDN</td><td>Specify the base DN for a branch or subtree of the data that should be included in the import. Entries contained in the import source that are not at or below one of the provided base DNs are rejected.</td><td>yes</td></tr>
 ** <tr><td>-p</td><td>--port</td><td>The port number used to connect to the Directory Service. Defaults to port 389.</td><td>no</td></tr>
 ** <tr><td>-a</td><td>--append</td><td>Append the imported data to the data that already exists in the back end, rather than clearing the back end before starting the import.</td><td>no</td></tr>
 ** <tr><td>-s</td><td>--skip</td><td>Use the specified file to identify entries that were skipped during the import. Skipped entries occur if entries cannot be placed under any specified base DN during an import.</td><td>no</td></tr>
 ** <tr><td>-L | -X</td><td>--ldiff | --dsml</td><td></td><td>Prints entries in LDIF (-L) or DSML format (-X).</td><td>no</td></tr>
 ** </table>
 */
public class LDAPImport extends LDAPService {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static String   host;
  static int      port;
  static String   basedn;
  static String   binddn;
  static String   password;
  static String   descriptor;
  static String   inputFile;
  static String   skipFile;
  static boolean  dsml       = false;

  static int      OMIT       = 0;
  static int      ATTRIBUTE  = 1;
  static String[] argument   = new String[2];

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   ** @param  basedn             the fully qualified context name to be
   **                            exported.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the LDAP Server.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
   ** @param  featurePath        the name of the file providing the target
   **                            system specific feature descriptor like
   **                            objectClasses, attribute id's etc.
   **
   ** @throws SystemException    if the feature configuration cannot be created.
   */
  public LDAPImport(final String serverName, final int serverPort, final String basedn, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String featurePath)
    throws SystemException {

    // ensure inheritance
    super(serverName, serverPort, basedn, principalName, principalPassword, secureSocket, relativeDN, featurePath);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** @param  serverName         Host name or IP address of the target system on
   **                            which the LDAP Server is installed
   ** @param  serverPort         the port the LDAP server is listening on
   **                            <br>
   **                            Default value for non-SSL: 389
   **                            Default value for SSL: 636
   ** @param  basedn             the fully qualified context name to be
   **                            exported.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the administrator with LDAP administrator
   **                            rights.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Oracle Identity Manager
   **                            and the LDAP Server.
   ** @param  relativeDN         whether all pathes are treated as relative to
   **                            the naming context of the connected LDAP
   **                            Server.
   ** @param  feature            the target system specific feature descriptor
   **                            like objectClasses, attribute id's etc.
   **
   ** @throws SystemException    if the feature configuration cannot be created.
   */
  public LDAPImport(final String serverName, final int serverPort, final String basedn, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final DirectoryFeature feature)
    throws SystemException {

    // ensure inheritance
    super(serverName, serverPort, basedn, principalName, principalPassword, secureSocket, relativeDN, feature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** The main method for LDIFImport tool.
   **
   ** @param  args               the command-line arguments provided to this
   **                            program.
   */
  public static void main(String[] args) {
    parseCommandLine(args);
    try {
      final LDAPImport service = new LDAPImport(host, port, basedn, binddn, password, false, false, descriptor);
      service.execute();
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
    final CommandLine.Option[] opts = new CommandLine.Option[11];
    opts[ 0] = new CommandLine.Option("hostname",  CommandLine.ARGUMENT_REQUIRED, null, 'h');
    opts[ 1] = new CommandLine.Option("port",      CommandLine.ARGUMENT_REQUIRED, null, 'p');
    opts[ 2] = new CommandLine.Option("binddn",    CommandLine.ARGUMENT_REQUIRED, null, 'D');
    opts[ 3] = new CommandLine.Option("password",  CommandLine.ARGUMENT_REQUIRED, null, 'w');
    opts[ 4] = new CommandLine.Option("basedn",    CommandLine.ARGUMENT_REQUIRED, null, 'b');
    opts[ 5] = new CommandLine.Option("append",    CommandLine.ARGUMENT_REQUIRED, null, 'a');
    opts[ 6] = new CommandLine.Option("skip",      CommandLine.ARGUMENT_REQUIRED, null, 's');
    opts[ 7] = new CommandLine.Option("feature",   CommandLine.ARGUMENT_REQUIRED, null, 'f');
    opts[ 8] = new CommandLine.Option("ldif",      CommandLine.ARGUMENT_REQUIRED, null, 'L');
    opts[ 9] = new CommandLine.Option("dsml",      CommandLine.ARGUMENT_REQUIRED, null, 'X');
    opts[10] = new CommandLine.Option("help",      CommandLine.ARGUMENT_NO,       null, '?');
    final CommandLine cli = new CommandLine("LDIFImport", args, "-h:p:b:D:w:f:s:LX?", opts);

    int index  = 0;
    int option = cli.nextOption();
    // keep parsing until we run out of options
    while (option != -1) {
      switch (option) {
        case 'h' : host = cli.optionArgument();
                   break;
        case 'p' : port = Integer.parseInt(cli.optionArgument());
                   break;
        case 'D' : binddn = cli.optionArgument();
                   break;
        case 'w' : password = cli.optionArgument();
                   break;
        case 'b' : basedn = cli.optionArgument();
                   break;
        case 's' : skipFile = cli.optionArgument();
                   break;
        case 'f' : descriptor = cli.optionArgument();
                   break;
        case 'L' : dsml = false;
                   inputFile = args[cli.index()];
                   break;
        case 'X' : dsml = true;
                   inputFile = args[cli.index()];
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
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Prints the usage of this class to stdout.
   */
  private static void usage() {
    warning("Usage: java -cp <path to>/hst-foundation.jar:<path to>/oim-foundation.jar:<path to>/oim-utility.jar oracle.iam.identity.utility.file.LDIFImport -h hostname -D \"binddn\" -w password -b \"basedn\" [-p ldap_port] [-L|-X] inputfile [-s skipfile] {[omit | attributes] [-f featurefile}]}");
    warning("Where is:");
    warning("  <path to>     ... the directory of the Java Archives needed to accomplish");
    warning("                    the runtime classpath");
    info("");
    info("  --host     -h ... the host name or IP address of the Directory Service");
    info("  --port     -p ... the port number used to connect to the Directory Service");
    info("                    Defaults to port 389");
    info("  --binddn   -D ... the DN of the directory user needed to bind to the");
    info("                    Directory Service (for example, cn=orcladmin)");
    info("  --password -w ... the user password needed to bind to the Directory Service");
    info("  --basedn   -b ... the base DN for the search");
    info("  --feature  -f ... the descriptor with the feature description of the");
    info("                    Directory Service");
    info("  --ldif     -L ... imports entries in LDIF format");
    info("  --dsml     -X ... imports entries in DSML format");
    info("  --skip     -s ... file caontaining the skipped records");
    info("  --help     -? ... this information");
  }

  private void execute()
    throws TaskException {

    if (StringUtility.isEmpty(inputFile)) {
      error(LDAPBundle.string(LDAPError.FILENAME_MISSING));
      usage();
      System.exit(-1);
    }

    final File input = new File(inputFile);
    if (!input.exists()) {
      error(LDAPBundle.string(LDAPError.NOTEXISTS));
      System.exit(-2);
    }

    if (!input.isFile()) {
      error(LDAPBundle.format(LDAPError.NOTAFILE, inputFile, ClassUtility.shortName(LDAPImport.class)));
      System.exit(-3);
    }

    if (!input.canRead()) {
      error(LDAPBundle.format(LDAPError.NOTREADABLE, inputFile, ClassUtility.shortName(LDAPImport.class)));
      System.exit(-3);
    }

    final DirectoryFileReader reader = dsml ? new DSMLReader(input) : new LDIFReader(input);

    if (StringUtility.isEmpty(basedn)) {
      System.err.println("You must specify the basedn the principal will connect to");
      usage();
      System.exit(-4);
    }

    if (StringUtility.isEmpty(binddn)) {
      System.err.println("You must specify the binddn of the principal");
      usage();
      System.exit(-5);
    }

    // see if there were any user-defined sets of exclude attributes or
    // filters.  If so, then process them.
    if (!StringUtility.isEmpty(argument[OMIT]))
      reader.parseOmitAttribute(argument[OMIT]);

    // see if there were any user-defined sets of include attributes or
    // filters.  If so, then process them.
    if (!StringUtility.isEmpty(argument[ATTRIBUTE]))
      reader.parseFetchedAttribute(argument[ATTRIBUTE]);

    // surpress import of any binary attribute
    reader.registerFetchedAttribute(this.service.binaryObjectAttribute());

    // see if there were any user-defined sets of exclude attributes or
    // filters.  If so, then process them.
    DirectoryFileWriter writer = null;
    if (skipFile != null) {
      final File skip = new File(skipFile);
      if (skip.isDirectory()) {
        error(LDAPBundle.format(LDAPError.NOTAFILE, "skipFile", skipFile));
        System.exit(-4);
      }
      if (!skip.canWrite()) {
        error(LDAPBundle.format(LDAPError.NOTWRITABLE, "skipFile", skipFile));
        System.exit(-4);
      }
      if (skip.exists())
        skip.delete();

      writer = dsml ? new DSMLWriter(skip) : new LDIFWriter(skip);
    }
    // Perform the initial bootstrap of the Directory Server and process the
    // configuration.
    this.service.connect();

    info(LDAPBundle.format(LDAPMessage.IMPORT_BEGIN, inputFile));
    int    success = 0;
    int    skipped = 0;
    try {
      // This while loop is used to read the LDAP entries in blocks.
      // This should decrease memory usage and help with server load.
      int    entry   = 0;
      // Launch the import.
      do {
        final Binding binding = reader.nextRecord();
        if (binding == null)
          break;

        final LDAPRecord source     = (LDAPRecord)binding.getObject();
        final Attributes attributes = source.attributes();
        // break out if the source record does not provide any attribute
        if (attributes.size() == 0)
          continue;

        boolean result = true;
        source.nameSpace(binding.getName());
        LdapContext context = null;
        try {
          final List<String[]> path = DirectoryName.explode(binding.getName());
          String[] name    = path.remove(0);
          String parentRDN = DirectoryName.compose(path);
          String objectRDN = DirectoryName.composeName(name);

          context = this.service.connect(parentRDN);
          // create object
          context.createSubcontext(objectRDN, attributes);
          success++;
        }
        catch (Exception e) {
          skipped++;
          if (writer != null) {
            writer.printComment(e.getMessage());
            writer.printComment(source.nameSpace());
            source.toStream(writer);
          }
        }
        finally {
          if (context != null)
            try {
              context.close();
            }
            catch (NamingException e) {
              ;
            }
        }
        System.out.print(result ? '.' : '?');
        if ((++entry % 80) == 0)
          System.out.print('\n');

      } while(true);
    }
    finally {
      this.service.disconnect();
      reader.close();
      if (writer != null)
        writer.close();
    }
    System.out.print("\n");
    info(LDAPBundle.format(LDAPMessage.IMPORT_FINISHED, inputFile, String.format("%d", success), String.format("%d", skipped)));
  }
}
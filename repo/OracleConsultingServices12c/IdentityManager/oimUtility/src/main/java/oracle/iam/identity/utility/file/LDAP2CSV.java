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

    File        :   LDAP2CSV.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAP2CSV.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.naming.NamingEnumeration;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.CommandLine;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.ldap.DirectorySearch;
import oracle.iam.identity.foundation.ldap.DirectoryFeature;
import oracle.iam.identity.foundation.ldap.DirectoryConstant;
import oracle.iam.identity.foundation.ldap.DirectoryConnector;
import oracle.iam.identity.foundation.ldap.DirectoryPageSearch;

////////////////////////////////////////////////////////////////////////////////
// class LDAP2CSV
// ~~~~~ ~~~~~~~~
/**
 ** The <code>LDAP2CSV</code> command-line tool enables you to convert to CSF
 ** all or part of the information residing in a Directory Service. Once you
 ** have converted the information, you can load it into a new node in a
 ** replicated directory or another node for backup storage.
 ** <p>
 ** The <code>LDAP2CSV</code> tool performs a subtree search, including all
 ** entries below the specified DN, including the DN itself.
 ** <p>
 ** The <code>LDAP2CSV</code> tool output does not include operational data of
 ** the directory itself for example, cn=subschemasubentry, cn=catalogs, and
 ** cn=changelog entries. To export these entries into LDIF or DSML format, use
 ** LDAPSearch with the -L flag.
 ** <p>
 ** LDAP2CSV -h hostname -D "binddn" -w password -b "basedn" {-s Object|OneLevel|SubTree} [-Y "proxy_dn"] [-p ldap_port] [-A] [-o] outputfile {"filter_string" [attributes] [omit]|-f input_file}
 ** <br>
 ** <table summary="">
 ** <tr><th>Short</th><th>Long</th><th>Allowed Values</th><th>Description</th><th>Mandatory</th></tr>
 ** <tr><td>-h</td><td>--hostname</td><td></td><td>The host name or IP address of the Directory Service.</td><td>yes</td></tr>
 ** <tr><td>-D</td><td>--bindDN</td><td>The DN of the Directory Service user needed to bind to the directory (for example, cn=orcladmin).</td><td>yes</td></tr>
 ** <tr><td>-w</td><td>--password</td><td>The password needed to bind to the Directory Service</td><td>yes</td></tr>
 ** <tr><td>-b</td><td>--baseDN</td><td>The base DN for the search.</td><td>yes</td></tr>
 ** <tr><td>-b</td><td>--baseDN</td><td>Specify the base DN for a branch or subtree of the data that should be included in the export.</td><td>yes</td></tr>
 ** <tr><td>-p</td><td>--port</td><td>The port number used to connect to the Directory Service. Defaults to port 389.</td><td>no</td></tr>
 ** <tr><td>-L | -X</td><td>--ldiff | --dsml</td><td></td><td>Prints entries in LDIF (-L) or DSML format (-X).</td><td>no</td></tr>
 ** </table>
 */
public class LDAP2CSV extends LDAPService {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static String   host;
  static int      port;
  static String   basedn;
  static String   binddn;
  static String   password;
  static String   descriptor;
  static String   filename;
  static String   scope          = DirectoryConstant.SCOPE_SUBTREE;

  static int                      FILTER         = 0;
  static int                      ATTRIBUTE      = 1;
  static int                      OMIT           = 2;
  static final String[]           argument       = new String[3];
  static final List<String>       omitAttribute  = new ArrayList<String>();
  static final List<CSVAttribute> fetchAttribute = new ArrayList<CSVAttribute>();

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
  public LDAP2CSV(final String serverName, final int serverPort, final String basedn, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final String featurePath)
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
  public LDAP2CSV(final String serverName, final int serverPort, final String basedn, final String principalName, final String principalPassword, final boolean secureSocket, final boolean relativeDN, final DirectoryFeature feature)
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
   ** The main method for LDAP2CSV tool.
   **
   ** @param  args               the command-line arguments provided to this
   **                            program.
   */
  public static void main(String[] args) {
    parseCommandLine(args);
    if (StringUtility.isEmpty(filename)) {
      System.err.println("You must specify the filename the results will be written to");
      usage();
      System.exit(-1);
    }
    if (StringUtility.isEmpty(basedn)) {
      System.err.println("You must specify the basedn the principal will connect to");
      usage();
      System.exit(-2);
    }

    if (StringUtility.isEmpty(binddn)) {
      System.err.println("You must specify the binddn of the principal");
      usage();
      System.exit(-3);
    }

    LDAP2CSV service;
    try {
      // if the descriptots are not related to a file parse it from the command
      // line
      if (StringUtility.isEmpty(descriptor)) {
        final DirectoryFeature feature = DirectoryFeature.build(null);
        service = new LDAP2CSV(host, port, basedn, binddn, password, false, false, feature);
      }
      else {
        service = new LDAP2CSV(host, port, basedn, binddn, password, false, false, descriptor);
      }
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
    opts[ 2] = new CommandLine.Option("username",  CommandLine.ARGUMENT_REQUIRED, null, 'D');
    opts[ 3] = new CommandLine.Option("password",  CommandLine.ARGUMENT_REQUIRED, null, 'w');
    opts[ 4] = new CommandLine.Option("basedn",    CommandLine.ARGUMENT_REQUIRED, null, 'b');
    opts[ 5] = new CommandLine.Option("scope",     CommandLine.ARGUMENT_REQUIRED, null, 's');
    opts[ 6] = new CommandLine.Option("feature",   CommandLine.ARGUMENT_REQUIRED, null, 'f');
    opts[ 7] = new CommandLine.Option("output",    CommandLine.ARGUMENT_REQUIRED, null, 'o');
    opts[ 9] = new CommandLine.Option("help",      CommandLine.ARGUMENT_NO,       null, '?');
    final CommandLine cli = new CommandLine("LDAP2CSV", args, "-h:p:b:D:w:f:s:o?", opts);

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
        case 's' : scope = cli.optionArgument();
                   break;
        case 'f' : descriptor = cli.optionArgument();
                   break;
        case 'o' : filename = args[cli.index()];
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
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Prints the usage of this class to stdout.
   */
  private static void usage() {
    System.out.println("Usage: java -cp <path to>/oim-utility.jar:<path to>/oim-foundation.jar:<path to>/hst-foundation.jar oracle.iam.identity.utility.file.LDAP2CSV -h hostname -D \"binddn\" -w password -b \"basedn\" {-s Object|OneLevel|SubTree} [-Y \"proxydn\"] [-p ldapport] [-o] outputfile {\"filterstring\" [omit | attributes] [-f featurefile}]}");
    System.out.println("Where is:");
    System.out.println("  <path to>     ... the directory of the Java Archives needed to accomplish");
    System.out.println("                    the runtime classpath");
    System.out.println("");
    System.out.println("  --host     -h ... the host name or IP address of the Directory Service");
    System.out.println("  --port     -p ... the port number used to connect to the Directory Service");
    System.out.println("                    Defaults to port 389");
    System.out.println("  --feature  -f ... the descriptor with the feature description of the");
    System.out.println("                    Directory Service");
    System.out.println("  --binddn   -D ... the DN of the directory user needed to bind to the");
    System.out.println("                    Directory Service (for example, cn=orcladmin)");
    System.out.println("  --password -w ... the user password needed to bind to the Directory Service");
    System.out.println("  --basedn   -b ... the base DN for the search");
    System.out.println("  --scope    -s ... the scope of the search within the DIT");
    System.out.println("                    Valid values are:");
    System.out.println("                    Object   - Retrieves a particular directory entry");
    System.out.println("                               Along with this search depth, you use the");
    System.out.println("                               search criteria bar to select the attribute");
    System.out.println("                               objectClass and the filter Present");
    System.out.println("                    OneLevel - Limits your search to all entries beginning");
    System.out.println("                               one level down from the root of your search");
    System.out.println("                    SubTree  - Searches entries within the entire subtree,");
    System.out.println("                               including the root of your search");
    System.out.println("  --output   -o ... the path of the file to print entries in CSV format");
    System.out.println("  --help     -? ... this information");
  }

  private void execute()
    throws Exception {

    // create a CVS writer leverage a FileWriter
    // the FileOutputStream will always append to the existing file regardless
    // to the state of the provisioning task
    final CSVWriter     writer     = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filename, true), "ISO-8859-1"), 2, ',', '"', true);

    final SearchControls controls = DirectoryConnector.searchScope(scope);

    // See if there were any user-defined sets of exclude attributes or
    // filters.  If so, then process them.
    if (!StringUtility.isEmpty(argument[OMIT])) {
      final String[] raw = argument[OMIT].split(",");
      for (int i = 0; i < raw.length; i++)
        omitAttribute.add(raw[i].toLowerCase().trim());
    }

    // See if there were any user-defined sets of include attributes or
    // filters.  If so, then process them.
    if (!StringUtility.isEmpty(argument[ATTRIBUTE])) {
      controls.setReturningAttributes(argument[ATTRIBUTE].split(","));
      final String[] raw = argument[ATTRIBUTE].split(",");
      for (int i = 0; i < raw.length; i++) {
        final CSVAttribute attribute = new CSVAttribute(raw[i].toLowerCase().trim(), "string");
        fetchAttribute.add(attribute);
      }
    }

    final CSVDescriptor descriptor = new CSVDescriptor(fetchAttribute);
    final CSVProcessor  processor  = new CSVProcessor(descriptor);
    descriptor.write(writer);

    // Perform the initial bootstrap of the Directory Server and process the
    // configuration.
    this.service.connect();
    try {
      // This while loop is used to read the LDAP entries in blocks.
      // This should decrease memory usage and help with server load.
      int    entry  = 0;
      // initialize PagedResultControl method to set the request
      // controls here we requesting a paginated result set
      final DirectorySearch search = new DirectoryPageSearch(this.service.connect(), "", argument[FILTER], null, controls, 1000);
      do {
        NamingEnumeration<SearchResult> results = search.next();
        // loop through the results and
        while (results != null && results.hasMoreElements()) {
          final SearchResult result  = results.nextElement();
          // the Attributes instance will have all the values from the source
          // system that requested by setting returning attributes in
          // SearchControls instance passed to the server or all if null was
          // passed by the caller to this method
          final LDAPRecord          record  = new LDAPRecord(result.getNameInNamespace(), result.getAttributes());
          final Attributes          vector  = record.attributes();
          final Map<String, Object> subject = new HashMap<String, Object>();
          for (CSVAttribute cursor : descriptor.attribute()) {
            final Attribute attribute = vector.get(cursor.name());
            if (attribute != null)
              subject.put(cursor.name(), attribute.get(0).toString());
            else
              subject.put(cursor.name(), SystemConstant.EMPTY);
          }
          CSVRecord.write(processor, writer, subject, false);
          System.out.print('.');
          if ((++entry % 80) == 0)
            System.out.print('\n');
        }
      } while (search.hasMore());
      System.out.print('\n');
    }
    finally {
      this.service.disconnect();
      try {
        writer.flush();
        writer.close();
      }
      catch (IOException e) {
        ;
      }
    }
  }
}
package oracle.iam.identity.foundation.xls;

import java.util.List;
import java.util.Iterator;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import oracle.hst.foundation.SystemConsole;

import oracle.hst.foundation.utility.CommandLine;
import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.utility.file.XMLEntityFactory;

public class ExportORG extends SystemConsole {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String        PROLOG                = "<?xml version=\"1.0\" encoding=\"%s\"?>";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static int                skip                   = -1;
  static String             input;
  static String             output;
  static final List<String> header                 = CollectionUtility.list(
    "kurzname"
  , "name"
  , "Authority-Group"
  );

  static String             now                    = String.format("%s", DateUtility.currentTimeMillis());

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the format specification constant over all requests*/
  protected XMLFormat       format     = new XMLFormat(String.format(XMLEntityFactory.PROLOG, "US-ASCII"));

  /** the roles root element of the XML file to produce */
  protected XMLOutputNode   identities = null;

  /** the abstract file which contains the data */
  private File              dataFile   = null;

  public ExportORG() {
    // ensure inheritance
    super("nsis2exp");
  }

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
  public static void main(String[] args) {
    parseCommandLine(args);
    if (StringUtility.isEmpty(input)) {
      System.err.println("You must specify the input file to transform");
      usage();
      System.exit(-1);
    }
    if (StringUtility.isEmpty(output)) {
      System.err.println("You must specify the output file to transform");
      usage();
      System.exit(-2);
    }
    if (skip == -1) {
      System.err.println("You must specify the amount of lines to skip (can be 0)");
      usage();
      System.exit(-2);
    }
    try {
      new ExportORG().execute();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(2);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  private void execute()
    throws Exception {

    final FileInputStream stream = new FileInputStream(new File(input));
    // create Workbook instance holding reference to .xlsx file
    final XSSFWorkbook  book  = new XSSFWorkbook(stream);
    // get first/desired sheet from the workbook
    final XSSFSheet     sheet = book.getSheetAt(0);
    // iterate through each rows one by one
    final Iterator<Row> r     = sheet.iterator();
    int i = 0;
    ensureRoot();
    organization(this.identities, "NSIS");
    while (r.hasNext()) {
      final Row row = r.next();
      if (i < WAS.skip) {
        i++;
        continue;
      }
      final Iterator<Cell> c = row.cellIterator();
      if (i == WAS.skip) {
        i++;
        // the next row MUST be the header
        // verify that we got our Excel
        while (c.hasNext()) {
          final Cell   cell  = c.next();
          final int    index = cell.getColumnIndex();
          final String value = cellValue(cell);
          // Excel is BASIC hence all collections are 1-based
          if (!header.get(index).equals(value))
            throw new RuntimeException(String.format("File corrupted: Excpected Column header %s; but got %s", header.get(cell.getColumnIndex()), value));
        }
        continue;
      }
      else {
        String              code = null;
        String              name = null;
        while (c.hasNext()) {
          final Cell   cell  = c.next();
          final int    index = cell.getColumnIndex();
          final String value = cellValue(cell);
          if (index == 0) {
            code = value;
          }
          else if (index == 1) {
            name = value;
          }
        }
        organization(this.identities, name).commit();
      }
    }
    this.identities.commit();
    this.identities.close();
    stream.close();
  }

  protected final XMLOutputNode organization(final XMLOutputNode node, final String name)
    throws Exception {

    final XMLOutputNode organization = node.element("object:category");
    organization.attribute("value", "Organization");
    organization.attribute("name",  name);
    return organization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataFile
  /**
   ** Creates the abstract {@link File} to access the file to import.
   **
   ** @return                    the abstract {@link File} to access file to
   **                            import.
   */
  protected final File dataFile() {
    if (this.dataFile == null) {
      // create the file handle
      this.dataFile = new File(output);
    }
    return this.dataFile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cellValue
  /**
   ** Returns the string value from a {@link Cell} accordingly converted from
   ** the type of the cell.
   **
   ** @return                    the string value from a {@link Cell}
   **                            accordingly converted from the type of the
   **                            cell.
   */
  private String cellValue(final Cell cell) {
    final CellType type = cell.getCellType();
    switch(type) {
      case NUMERIC : return String.format("%.0f", cell.getNumericCellValue());
      default      : return cell.getStringCellValue().trim();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureRoot
  /**
   ** This is used to create the error report root element.
   */
  private void ensureRoot() {
    if (this.identities == null) {
      try {
        final File dataFile = dataFile();
        // check if we have write access to the file if its exists
        if (dataFile.exists() && !dataFile.canWrite()) {
          throw new RuntimeException("Cannot write to file " + dataFile.getAbsolutePath());
        }

        this.identities = XMLProcessor.marshal(this, dataFile, this.format).element("object:exportSet");
        this.identities.attribute("exportfile",  "1701 systemConnector N.SIS/xml/exp-organization-dm.xml");
        this.identities.attribute("description", "Federated Identity Management Organization Configuration ${appication.release}");
      }
      catch (XMLException e) {
        throw new RuntimeException(e);
      }
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
    opts[ 0] = new CommandLine.Option("input",  CommandLine.ARGUMENT_REQUIRED, null, 'i');
    opts[ 1] = new CommandLine.Option("output", CommandLine.ARGUMENT_REQUIRED, null, 'o');
    opts[ 2] = new CommandLine.Option("skip",   CommandLine.ARGUMENT_REQUIRED, null, 's');

    final CommandLine cli = new CommandLine("eFBS SCIM Service Simulator", args, "-i:o:s:?", opts);

    int option = cli.nextOption();
    // keep parsing until we run out of options
    while (option != -1) {
      switch (option) {
        case 'i' : input = cli.optionArgument();
                   break;
        case 'o' : output = cli.optionArgument();
                   break;
        case 's' : skip   = Integer.parseInt(cli.optionArgument());
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
    System.out.println("Usage: java -cp <path to>/ocs-incubator.jar:<path to>/hst-foundation.jar oracle.iam.identity.foundation.xls.Nsis -i input -o output -s skip");
    System.out.println("Where is:");
    System.out.println("  <path to>     ... the directory of the Java Archives needed to accomplish");
    System.out.println("                    the runtime classpath");
    System.out.println("");
    System.out.println("  --input  -i ... the path to the XLSX file to transform");
    System.out.println("  --output -o ... the path to the to transformation output");
    System.out.println("  --skip   -s ... the amount of lines to skip because they are for informational purpose only");
    System.out.println("  --help   -? ... this information");
  }
}
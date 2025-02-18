package oracle.iam.identity.foundation.xls;

import java.util.List;
import java.util.Iterator;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.XMLConstants;

import oracle.hst.foundation.SystemConsole;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import oracle.hst.foundation.xml.XMLFormat;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.hst.foundation.utility.CommandLine;
import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.utility.file.XMLEntityFactory;

import org.apache.poi.ss.usermodel.CellType;

public class Main extends SystemConsole {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String       PROLOG                = "<?xml version=\"1.0\" encoding=\"%s\"?>";
  static final String       NAMESPACE             = "http://www.oracle.com/schema/oim/offline";
  static final String       SCHEMA                = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
  /**
   ** the schema definition to be used if identities needs to be exported or
   ** imported
   */
  static final String       SCHEMA_LOCATION        = "http://www.oracle.com/schema/oim/policy Identity.xsd";

  static final String       MULTIPLE               = "identities";
  static final String       SINGLE                 = "identity";

  static final String       ELEMENT_ATTRIBUTE      = "attribute";

  static final String       ATTRIBUTE_ID           = "id";
  static final String       ATTRIBUTE_VALUE        = "value";
  static final String       ATTRIBUTE_ACTION       = "action";

  static final String       ELEMENT_PRINCIPALNAME  = "principalName";
  static final String       ELEMENT_LASTNAME       = "lastName";
  static final String       ELEMENT_FIRSTNAME      = "firstName";
  static final String       ELEMENT_ORGANIZATION   = "organizationalUnit";
  static final String       ELEMENT_DIVISION       = "division";
  static final String       ELEMENT_DEPARTMENT     = "department";
  static final String       ELEMENT_EMAIL          = "email";
  static final String       ELEMENT_PHONE          = "phoneNumber";
  static final String       ELEMENT_COUNTRY        = "country";
  static final String       ELEMENT_STATE          = "state";
  static final String       ELEMENT_STREET         = "street";
  static final String       ELEMENT_POSTALCODE     = "postalcode";
  static final String       ELEMENT_LOCALIY        = "locality";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static int                skip                   = -1;
  static String             input;
  static String             output;
  static final List<String> header                 = CollectionUtility.list(
    "Name"
  , "Vorname"
  , "Kennung im Bundesland"
  , "Bundesland"
  , "Dienststelle"
  , "Funktion"
  , "Telefonnummer"
  , "Mailadresse"
  , "Straße und Hausnummer"
  , "Postleitzahl"
  , "Ort"
  );

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the format specification constant over all requests*/
  protected XMLFormat       format     = new XMLFormat(String.format(XMLEntityFactory.PROLOG, "US-ASCII"));
;

  /** the roles root element of the XML file to produce */
  protected XMLOutputNode   identities = null;

  /** the abstract file which contains the data */
  private File              dataFile   = null;

  public Main() {
    // ensure inheritance
    super("xlsx2xlm");
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
      new Main().execute();
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
    System.out.println("Usage: java -cp <path to>/ocs-incubator.jar:<path to>/hst-foundation.jar oracle.iam.identity.foundation.xls.Main -i input -o output -s skip");
    System.out.println("Where is:");
    System.out.println("  <path to>     ... the directory of the Java Archives needed to accomplish");
    System.out.println("                    the runtime classpath");
    System.out.println("");
    System.out.println("  --input  -i ... the path to the XLSX file to transform");
    System.out.println("  --output -o ... the path to the to transformation output");
    System.out.println("  --skip   -s ... the amount of lines to skip because they are for informational purpose only");
    System.out.println("  --help   -? ... this information");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  private void execute()
    throws Exception {

    final FileInputStream stream = new FileInputStream(new File(input));
    // create Workbook instance holding reference to .xlsx file
    final XSSFWorkbook    book  = new XSSFWorkbook(stream);
    // get first/desired sheet from the workbook
    final XSSFSheet       sheet = book.getSheetAt(0);
    // iterate through each rows one by one
    final Iterator<Row> r = sheet.iterator();
    int i = 0;
    ensureRoot();
    while (r.hasNext()) {
      final Row row = r.next();
      if (i < skip) {
        i++;
        continue;
      }
      final Iterator<Cell> c = row.cellIterator();
      if (i == skip) {
        i++;
        // the next row MUST be the header
        // verify that we got our Excel
        while (c.hasNext()) {
          final Cell   cell  = c.next();
          final String value = cellValue(cell);
          if (!header.get(cell.getColumnIndex()).equals(value))
            throw new RuntimeException(String.format("File corrupted: Excpected Column header %s; but got %s", header.get(cell.getColumnIndex()), value));
        }
        continue;
      }
      else {
        String              principalName = null;
        String              userName      = null;
        String              lastName      = null;
        String              firstName     = null;
        String              organization  = null;
        String              phone         = null;
        String              email         = null;
        String              division      = null;
        String              department    = null;
        String              street        = null;
        String              state         = null;
        String              locality      = null;
        String              postalCode    = null;
        while (c.hasNext()) {
          final Cell   cell  = c.next();
          final int    index = cell.getColumnIndex();
          final String value = cellValue(cell);
          if (index == 0) {
            lastName = value;
          }
          else if (index == 1) {
            firstName = value;
          }
          else if (index == 2) {
            userName = value;
          }
          else if (index == 3) {
            state        = value;
            organization = value;
          }
          else if (index == 4) {
            division   = value;
            department = value;
          }
          else if (index == 6) {
            phone = value;
          }
          else if (index == 7) {
            email = value;
            principalName = String.format("%s@%s", userName, value.split("@")[1]);
          }
          else if (index == 8) {
            street = value;
          }
          else if (index == 9) {
            postalCode = value;
          }
          else if (index == 10) {
            locality = value;
          }
        }
        final XMLOutputNode identity  = this.identities.element(SINGLE);
        identity.attribute(ATTRIBUTE_ID, userName);
        XMLOutputNode attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, "principalName");
        attribute.value(principalName);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_LASTNAME);
        attribute.value(lastName);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_FIRSTNAME);
        attribute.value(firstName);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_EMAIL);
        attribute.value(email);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_PHONE);
        attribute.value(phone);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_ORGANIZATION);
        attribute.value(organization);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_DIVISION);
        attribute.value(division);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_DEPARTMENT);
        attribute.value(department);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_COUNTRY);
        attribute.value("Deutschland");
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_STATE);
        attribute.value(state);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_STREET);
        attribute.value(street);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_POSTALCODE);
        attribute.value(postalCode);
        attribute = identity.element(ELEMENT_ATTRIBUTE);
        attribute.attribute(ATTRIBUTE_ID, ELEMENT_LOCALIY);
        attribute.value(locality);
        identity.commit();
      }
    }
    this.identities.commit();
    this.identities.close();
    stream.close();
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

        // any role element can only be created within a roles element as its
        // parent
        this.identities = XMLProcessor.marshal(this, dataFile, this.format).element(MULTIPLE);
        this.identities.attribute(XMLProcessor.ATTRIBUTE_XMLNS,     NAMESPACE);
        this.identities.attribute(XMLProcessor.ATTRIBUTE_XMLNS_XSI, SCHEMA);
        this.identities.attribute(XMLProcessor.ATTRIBUTE_SCHEMA,    SCHEMA_LOCATION);
      }
      catch (XMLException e) {
        throw new RuntimeException(e);
      }
    }
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
}